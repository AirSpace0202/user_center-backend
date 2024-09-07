package com.jixuan.user_centerbackend.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.jixuan.user_centerbackend.model.domain.User;


import javax.servlet.http.HttpServletRequest;

/**
* @author Jaychou
* @description 针对表【user(用户)】的数据库操作Service
* @createDate 2024-09-04 10:57:26
*/
public interface UserService extends IService<User> {

    /**
     * 用户注册功能
     * @param userAccount 用户账户
     * @param userPassword 用户密码
     * @param checkPassword 校验密码
     * @return 新注册的用户 id
     */
    long userRegister(String userAccount, String userPassword, String checkPassword);

    /**
     * 用户登录功能，登录后返回脱敏后的用户信息
     * @param userAccount 用户账户
     * @param userPassword 用户密码
     * @param request 前端传来的请求
     * @return 脱敏后的用户信息
     */
    User userLogin(String userAccount, String userPassword, HttpServletRequest request);

    /**
     * 进行用户脱敏，返回脱敏后安全的用户信息
     * @param originUser 未脱敏的用户
     * @return  安全的用户信息
     */
    User getSafetyUser(User originUser);

    /**
     * 用户注销功能
     * @param request 前端传来的请求
     * @return 1 表示注销成功
     */
    int userLogout(HttpServletRequest request);

}
