package com.macro.mall.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.ArrayUtil;
import com.alibaba.druid.util.StringUtils;
import com.macro.mall.common.exception.BusinessException;
import com.macro.mall.config.properties.RedisKeyPrefixConfig;
import com.macro.mall.domain.Register;
import com.macro.mall.domain.UmsMember;
import com.macro.mall.domain.UmsMemberExample;
import com.macro.mall.mapper.UmsMemberMapper;
import com.macro.mall.service.IMemberService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Random;
import java.util.concurrent.TimeUnit;

/**
 * @Author superUser
 * @Date 2021/6/9
 */
@Slf4j
@EnableConfigurationProperties(RedisKeyPrefixConfig.class)
@Service
public class MemberServiceImpl implements IMemberService {

    @Autowired
    private UmsMemberMapper umsMemberMapper;

    @Autowired
    private RedisTemplate redisTemplate;

    @Autowired
    private RedisKeyPrefixConfig redisKeyPrefixConfig;

    @Autowired
    private PasswordEncoder passwordEncoder;

    /**
     * 获取短信动态校验码
     * @param telPhone
     * @return
     */
    @Override
    public String getOtpCode(String telPhone) throws BusinessException {
        //1、查询当前用户有没有注册
        UmsMemberExample example = new UmsMemberExample();
        example.createCriteria().andPhoneEqualTo(telPhone);
        List<UmsMember> umsMembers = umsMemberMapper.selectByExample(example);
        if(!CollectionUtil.isEmpty(umsMembers)){
            throw new BusinessException("用户已经注册！不能重复注册！");
        }

        //2、校验60s后有没有再次发送
        if(redisTemplate.hasKey(redisKeyPrefixConfig.getPrefix().getOtpCode()+telPhone)){
            throw new BusinessException("请60s后再试！");
        }

        //3、生产随机校验码
        Random random = new Random();
        StringBuffer otpCode = new StringBuffer();
        for (int i = 0 ; i < 6 ; i++){
            otpCode.append(random.nextInt(10));
        }

        log.info("生成随机码：" + otpCode.toString());

        redisTemplate.opsForValue().set(redisKeyPrefixConfig.getPrefix().getOtpCode()+telPhone
                ,otpCode.toString(),redisKeyPrefixConfig.getExpire().getOtpCode(), TimeUnit.SECONDS);

        return otpCode.toString();
    }

    @Override
    public int register(Register register) throws BusinessException{
        //1、校验注册码
        String telPhone = register.getPhone();
        String optCode = register.getOptCode();
        String realOptCode = redisTemplate.opsForValue().get(redisKeyPrefixConfig.getPrefix().getOtpCode()+telPhone)+"";
        if(StringUtils.isEmpty(optCode) || !optCode.equals(realOptCode)){
            throw new BusinessException("验证码不正确！");
        }

        //2、校验用户是否已经存在
        UmsMemberExample example = new UmsMemberExample();
        example.createCriteria().andPhoneEqualTo(telPhone);
        List<UmsMember> umsMembers = umsMemberMapper.selectByExample(example);
        if(ArrayUtil.isNotEmpty(umsMembers.toArray())){
            throw new BusinessException("用户已存在，请跳转至登录页面直接登录！");
        }

        //3、新建用户
        UmsMember umsMember = new UmsMember();
        umsMember.setStatus(1);
        umsMember.setMemberLevelId(4L);
        register.setPassword(passwordEncoder.encode(register.getPassword()));
        BeanUtils.copyProperties(register,umsMember);
//        umsMember.setPhone(register.getTelphone());
//        umsMember.setUsername(register.getUsername());
//        umsMember.setPassword(register.getPassword());
        return umsMemberMapper.insertSelective(umsMember);
    }

    @Override
    public UmsMember login(String username, String password) throws BusinessException {
        //1、根据用户名获取用户信息
        UmsMemberExample example = new UmsMemberExample();
        example.createCriteria().andUsernameEqualTo(username);
        List<UmsMember> umsMembers = umsMemberMapper.selectByExample(example);
        if(CollectionUtil.isEmpty(umsMembers)){
            throw new BusinessException("用户："+username+"不存在！");
        }

        if(umsMembers.size()>1){
            throw new BusinessException("该用户被多次注册请联系客服人员！");
        }

        //2、对比密码
        UmsMember umsMember = umsMembers.get(0);
        String pwd = umsMember.getPassword();
        if(!passwordEncoder.matches(password,pwd)){
            throw new BusinessException("密码错误");
        }
        return umsMember;
    }

    public static void main(String[] args) {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        System.out.println(encoder.encode("yf141280"));
    }
}
