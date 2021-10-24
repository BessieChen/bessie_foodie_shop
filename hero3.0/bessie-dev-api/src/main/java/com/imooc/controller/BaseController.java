package com.imooc.controller;

import java.io.File;

/**
 * @program: bessie-dev
 * @description: 基类controller
 * @author: Bessie
 * @create: 2021-10-22 22:19
 **/
public class BaseController {
    public static final Integer COMMENT_PAGE_SIZE = 20;
    public static final Integer COMMON_PAGE_SIZE = 5;
    public static final Integer PAGE_SIZE = 20;
    public static final String FOODIE_SHOPCART = "shopcart";
    public static final String IMAGE_USER_FACE_LOCATION = "/Users/bessie/Desktop/JOB/whole_systems/6_program/3_java/from_0_to_hero/software_used/workspaces/images/foodie/faces";
    // 或者你可以使用: public static final String image_user_face_location = File.separator + "Users" + File.separator + ... + File.separator + "faces";
}
