package com.imooc.resource;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

/**
 * @program: bessie-dev
 * @description: 将 CenterUserController 类 和 file-upload-xxx.properties 关联
 * @author: Bessie
 * @create: 2021-10-24 01:33
 **/
@Component     //作为组件被 springboot 扫描
@ConfigurationProperties(prefix = "file")   //因为 file-upload-dev.properties 中写的是: file.imageUserFaceLocation
//@PropertySource("classpath:file-upload-dev.properties")
@PropertySource("classpath:file-upload-prod.properties")
public class FileUpload {
    private String imageUserFaceLocation;   //这个就是 file-upload-dev.properties 中定义的
    private String imageServerUrl;

    public String getImageUserFaceLocation() {
        return imageUserFaceLocation;
    }

    public void setImageUserFaceLocation(String imageUserFaceLocation) {
        this.imageUserFaceLocation = imageUserFaceLocation;
    }

    public String getImageServerUrl() {
        return imageServerUrl;
    }

    public void setImageServerUrl(String imageServerUrl) {
        this.imageServerUrl = imageServerUrl;
    }
}
