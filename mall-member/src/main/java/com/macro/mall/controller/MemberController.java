package com.macro.mall.controller;

import com.macro.mall.JwtKit;
import com.macro.mall.common.api.CommonResult;
import com.macro.mall.common.exception.BusinessException;
import com.macro.mall.config.properties.JwtProperties;
import com.macro.mall.domain.Register;
import com.macro.mall.domain.UmsMember;
import com.macro.mall.service.IMemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.util.HashMap;
import java.util.Map;

/**
 * @Author superUser
 * @Date 2021/6/9
 */
@RestController
@RequestMapping("/sso")
public class MemberController extends BaseController{

    @Autowired
    private IMemberService iMemberService;

    @Autowired
    private JwtKit jwtKit;

    @Autowired
    private JwtProperties jwtProperties;

    /**
     * 获取验证码
     * @param telPhone
     * @return
     * @throws BusinessException
     */
    @RequestMapping("/getOtpCode")
    public CommonResult getOtpCode(@RequestParam String telPhone) throws BusinessException {
        String otpCode = iMemberService.getOtpCode(telPhone);
        return CommonResult.success(otpCode);
    }

    /**
     * 注册
     * @param register
     * @return
     * @throws BusinessException
     */
    @Transactional(rollbackFor = Exception.class)
    @RequestMapping("/register")
    public CommonResult register(@Valid @RequestBody Register register) throws BusinessException {
        int registerCode = iMemberService.register(register);
        if(registerCode>0){
            return CommonResult.success("账号："+register.getUsername()+"注册成功！");
        }
        return CommonResult.failed();
    }

    /**
     * 登录
     * @param username
     * @param password
     * @return
     */
    @RequestMapping("/login")
    public CommonResult login(@RequestParam String username,@RequestParam String password) throws BusinessException {
        UmsMember umsMember = iMemberService.login(username,password);
        if(umsMember != null){
            HttpSession session = getHttpSession();
            session.setAttribute("member",umsMember);
            return CommonResult.success("用户："+username+"登陆成功！");
        }
        return CommonResult.failed();
    }


    @PostMapping("jwtLogin")
    public CommonResult jwtLogin(@RequestParam String username,@RequestParam String password)throws BusinessException{
        UmsMember umsMember = iMemberService.login(username,password);
        if(umsMember != null){
            Map<String,Object> tokenMap = new HashMap<>();
            String token = jwtKit.generateToken(umsMember);
            tokenMap.put("token",token);
            tokenMap.put("tokenHead",jwtProperties.getTokenHead());
            return CommonResult.success(tokenMap);
        }
        return CommonResult.failed();
    }
}
