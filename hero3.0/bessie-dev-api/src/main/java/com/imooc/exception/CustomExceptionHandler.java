package com.imooc.exception;

import com.imooc.utils.JSONReturn;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.multipart.MaxUploadSizeExceededException;

/**
 * @program: bessie-dev
 * @description: 用于提示前端有错误出现
 * @author: Bessie
 * @create: 2021-10-24 03:02
 **/
@RestControllerAdvice
public class CustomExceptionHandler {
    @ExceptionHandler(MaxUploadSizeExceededException.class)
    public JSONReturn handlerMaxUploadFile(MaxUploadSizeExceededException exception){
        return JSONReturn.errorMsg("文件上传大小不能超过 200KB, 请压缩图片");
    }
}
