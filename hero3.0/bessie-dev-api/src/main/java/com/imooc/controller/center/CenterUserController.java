package com.imooc.controller.center;

import com.imooc.controller.BaseController;
import com.imooc.pojo.Users;
import com.imooc.pojo.bo.center.CenterUserBO;
import com.imooc.resource.FileUpload;
import com.imooc.service.center.CenterUserSerivce;
import com.imooc.utils.CookieUtils;
import com.imooc.utils.DateUtil;
import com.imooc.utils.JSONReturn;
import com.imooc.utils.JsonUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.imooc.utils.DateUtil.DATE_PATTERN;

/**
 * @program: bessie-dev
 * @description: 用户中心
 * @author: Bessie
 * @create: 2021-10-23 21:58
 **/
@Api(value = "用户信息接口", tags = {"用户信息的相关接口"})
@RestController
@RequestMapping("userInfo")
public class CenterUserController extends BaseController {
    @Autowired
    private CenterUserSerivce centerUserSerivce;

    @Autowired
    private FileUpload fileUpload; //这个就是为了关联 类+file-upload-dev.properites 的配置类

    @ApiOperation(value = "修改用户信息", notes = "修改用户信息", httpMethod = "POST")
    @PostMapping("update")
    public JSONReturn update(
            @ApiParam(name = "userId", value = "用户id", required = true)
            @RequestParam String userId,
            @ApiParam(name = "centerUserBO", value = "用户中心信息BO", required = true)
            @RequestBody @Valid CenterUserBO centerUserBO,
            BindingResult result,
            HttpServletRequest request,
            HttpServletResponse response)
    {
        //判断是否有错误:
        if(result.hasErrors())
        {
            Map<String, String> errorMap = getError(result);
            return JSONReturn.errorMap(errorMap);       //注意, 不再使用 errorMsg
        }
        if(StringUtils.isBlank(userId))
        {
            return JSONReturn.errorMsg("用户id不能为空");
        }

        //把信息更新到后端之后, 也要拿到更新之后的 Users, 用于更新 cookie
        Users res = centerUserSerivce.updateUserInfo(userId, centerUserBO);
        res = setNullProperties(res);
        CookieUtils.setCookie(request, response, "user", JsonUtils.objectToJson(res), true);

        //TODO 后续需要修改, 增加令牌 token, 整合进入 redis
        return JSONReturn.ok();
    }

    @ApiOperation(value = "修改用户头像", notes = "修改用户头像", httpMethod = "POST")
    @PostMapping("uploadFace")
    public JSONReturn uploadFace(
            @ApiParam(name = "userId", value = "用户id", required = true)
            @RequestParam String userId,
            @ApiParam(name = "file", value = "用户头像", required = true)
            MultipartFile file,
            HttpServletRequest request,
            HttpServletResponse response)
    {
        if(file == null)
        {
            return JSONReturn.errorMsg("头像不能为空");
        }

        FileOutputStream fileOutputStream = null;
        String userName = "";
        try {
            //1. 头像保存的地址: /foodie/faces
            // String fileLocation = IMAGE_USER_FACE_LOCATION; //通过 BaseController 获得
            String fileLocation = fileUpload.getImageUserFaceLocation(); //通过自定义的配置类 fileUpload, 获得 file-upload-dev.properties 的属性

            //2. 每个用户都有自己的文件夹: /jack
            String userPath = File.separator + userId;

            //3. 获得用户上传的文件名称, 例如"xxx.png", 我们需要重命名为: "face-userId.png"
            String fileName = file.getOriginalFilename();                               //"xxx.png"
            String fileNameArr[] = fileName.split("\\.");                          //["xxx", "png"], 我们只需要后缀 png
            String suffix = fileNameArr[fileNameArr.length - 1];                        //"png"

            //补充: 后缀限定, 防止后门
            if(!suffix.equalsIgnoreCase("png")
                    &&!suffix.equalsIgnoreCase("jpg")
                    &&!suffix.equalsIgnoreCase("jpeg"))
            {
                return JSONReturn.errorMsg("图片格式不正确");
            }

            //继续实现:
            userName = "face-" + userId + "." + suffix;                          //覆盖式上传
            //String finalFileName = "face-" + userId + "-" + DateUtil.getCurrentDateString(DATE_PATTERN) + "." + suffix;     //增量式上传, 添加时间戳
            String finalFacePath = fileLocation + userPath + File.separator + userName; // /foodie/faces/jack/face-jack.png

            //4. 以下内容需要熟练:
            //1. 创建 File, 参数是 最后的路径
            File outFile = new File(finalFacePath); //创建的路径: 最终路径
            //2. 创建路径
            //我们不需要创建 /foodie/faces/ 目录, 会帮我们自动生成
            if(outFile.getParentFile() != null){    //如果有父级目录
                //创建文件夹
                outFile.getParentFile().mkdirs();   //递归生成目录
            }else
            {
                System.out.println("wrong");
            }

            //3. OutputStream 是我们的 File, InputStream 是浏览器传过来的 file
            fileOutputStream = new FileOutputStream(outFile);
            InputStream inputStream = file.getInputStream();
            IOUtils.copy(inputStream, fileOutputStream);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if(fileOutputStream != null)
                {
                    fileOutputStream.flush();
                    fileOutputStream.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        //4. 将可以访问的 url, 存到后端数据库
        //4.1 组装 url: http://localhost:8088/foodie/faces/2110239YWWHYYGTC/face-2110239YWWHYYGTC.png
        //4.1.1 第一个部分: http://localhost:8088/foodie/faces
        String imageServerUrl = fileUpload.getImageServerUrl();
        //4.1.2 第二个部分: /2110239YWWHYYGTC
        String userUrl = "/" + userId;  //注意不使用 File.seperator, 因浏览器上面本来就是 /, 不存在 \
        //4.1.3 第三个部分: /face-2110239YWWHYYGTC.png
        userUrl += ( "/" + userName);
        //4.2 开始拼接
        String finalImageServerUrl = imageServerUrl + userUrl + "?t=" + DateUtil.getCurrentDateString(DATE_PATTERN);

        //System.out.println(finalImageServerUrl);    //http://localhost:8088/foodie/faces/2110239YWWHYYGTC/face-2110239YWWHYYGTC.png?t=20211024024721

        //5. 保存url, 拿到更新之后的 Users, 用于更新 cookie
        Users users = centerUserSerivce.updateUserFace(userId, finalImageServerUrl);
        users = setNullProperties(users);
        CookieUtils.setCookie(request, response, "user", JsonUtils.objectToJson(users), true);

        return JSONReturn.ok();
    }



    private Map<String, String> getError(BindingResult res){
        Map<String, String> map = new HashMap<>();
        List<FieldError> errList = res.getFieldErrors();
        for(FieldError err : errList)
        {
            String errorField = err.getField();         //发生验证错误, 所对应的某一个属性
            String errorMsg = err.getDefaultMessage();  //验证错误信息
            map.put(errorField, errorMsg);
        }
        return map;
    }

    private Users setNullProperties(Users user)
    {
        user.setPassword(null);
        user.setMobile(null);
        user.setEmail(null);
        user.setCreatedTime(null);
        user.setUpdatedTime(null);
        user.setBirthday(null);
        return user;
    }
}
