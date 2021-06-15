package com.macro.mall.service;

import com.macro.mall.common.exception.BusinessException;
import com.macro.mall.domain.Register;
import com.macro.mall.domain.UmsMember;

/**
 * @Author superUser
 * @Date 2021/6/9
 */
public interface IMemberService {

    /**
     * @return
     * 返回otpCode
     */
    public String getOtpCode(String telPhone) throws BusinessException;

    /**
     * 账号注册
     * @param register
     * @return
     */
    public int register(Register register) throws BusinessException;

    /**
     * 登录
     * @param username
     * @param password
     * @return
     */
    public UmsMember login(String username, String password) throws BusinessException;
}
