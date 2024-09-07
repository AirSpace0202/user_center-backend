package com.jixuan.user_centerbackend.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.jixuan.user_centerbackend.common.BaseResponse;
import com.jixuan.user_centerbackend.common.ErrorCode;
import com.jixuan.user_centerbackend.common.ResultUtils;
import com.jixuan.user_centerbackend.exception.BusinessException;
import com.jixuan.user_centerbackend.model.domain.User;
import com.jixuan.user_centerbackend.model.domain.request.UserLoginRequest;
import com.jixuan.user_centerbackend.model.domain.request.UserRegisterRequest;
import com.jixuan.user_centerbackend.service.UserService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.management.Query;
import javax.servlet.http.HttpServletRequest;
import javax.validation.constraints.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static com.jixuan.user_centerbackend.constant.UserConstant.ADMIN_ROLE;
import static com.jixuan.user_centerbackend.constant.UserConstant.USER_LOGIN_STATE;

@RestController
@RequestMapping("/user")
public class UserController {

    @Resource
    private UserService userService;

    @PostMapping("/register")
    public BaseResponse<Long> userRegister(@RequestBody UserRegisterRequest userRegisterRequest) {
        if (userRegisterRequest == null) {
            // return ResultUtils.error(ErrorCode.PARAMS_ERROR);
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        // 获取前端 json 数据中的数据绑定到 UserRegisterRequest 中的变量
        String userAccount = userRegisterRequest.getUserAccount();
        String userPassword = userRegisterRequest.getUserPassword();
        String checkPassword = userRegisterRequest.getCheckPassword();

        if (StringUtils.isAnyBlank(userAccount, userPassword, checkPassword)) {
            return ResultUtils.error(ErrorCode.PARAMS_ERROR);
        }
        long result =  userService.userRegister(userAccount, userPassword, checkPassword);
        return ResultUtils.success(result);                         // 调用返回工具类的 success 的方法
    }

    @PostMapping("/login")
    // 需要获取到 HttpServletRequest 对象来操作 session
    public BaseResponse<User> userLogin(@RequestBody UserLoginRequest userLoginRequest, HttpServletRequest request) {
        if (userLoginRequest == null) {
            return ResultUtils.error(ErrorCode.PARAMS_ERROR);
        }
        String userAccount = userLoginRequest.getUserAccount();
        String userPassword = userLoginRequest.getUserPassword();

        if (StringUtils.isAnyBlank(userAccount, userPassword)) {
            return ResultUtils.error(ErrorCode.PARAMS_ERROR);
        }
        User user =  userService.userLogin(userAccount, userPassword, request);
        return ResultUtils.success(user);
    }

    @PostMapping("/logout")
    public BaseResponse<Integer> userLogout(HttpServletRequest request) {
        if (request == null) {
            return null;
        }
        int result =  userService.userLogout(request);
        return ResultUtils.success(result);
    }

    @GetMapping("/current")
    public BaseResponse<User> getCurrentUser(@NotNull HttpServletRequest request) {

        Object userObj = request.getSession().getAttribute(USER_LOGIN_STATE);           // 获取当前用户登录态并进行数据类型转换
        User currentUser = (User) userObj;
        long userId = currentUser.getId();              // 通过 id 进行数据库查询，查询出当前用户信息并返回脱敏后的用户信息
        User user = userService.getById(userId);

        User safetyUser =  userService.getSafetyUser(user);
        return ResultUtils.success(safetyUser);
    }

    @GetMapping("/search")
    public BaseResponse<List<User>> searchUsers(String userName, HttpServletRequest request) {

        if (!isAdmin(request)) {                // 非管理员返回空列表，todo 告诉用户无权限
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        if (StringUtils.isNotBlank(userName)) {
            queryWrapper.like("userName", userName);
        }
        List<User> userList = userService.list(queryWrapper);
        List<User> list =  userList.stream().map(user -> userService.getSafetyUser(user)).collect(Collectors.toList());     // 先将 userList 转换为数据流，进行脱敏后再转换成 List
        return ResultUtils.success(list);
    }

    @PostMapping("/delete")
    public BaseResponse<Boolean> deleteUsers(@RequestBody long id, HttpServletRequest request) {
        if (!isAdmin(request)) {
            return null;
        }
        if (id <= 0) {
            return null;
        }
        boolean result =  userService.removeById(id);
        return ResultUtils.success(result);
    }

    private boolean isAdmin(HttpServletRequest request) {                       // 用户鉴权
        Object userObj = request.getSession().getAttribute(USER_LOGIN_STATE);
        User user = (User) userObj;
        return user != null && user.getUserRole() == ADMIN_ROLE;
    }

}
