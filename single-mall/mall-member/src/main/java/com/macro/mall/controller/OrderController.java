package com.macro.mall.controller;

import com.macro.mall.domain.UmsMember;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author ：杨过
 * @date ：Created in 2020/3/22
 * @version: V1.0
 * @slogan: 天下风云出我辈，一入代码岁月催
 * @description:
 **/
@Slf4j
@RestController
public class OrderController extends BaseController {

    @GetMapping("/order")
    public String order(){
//        log.info("下订单");
        UmsMember member = (UmsMember) getHttpSession().getAttribute("member");
        log.info("下订单->JVM-8080,user:" + member.getUsername());
        return "下订单->JVM-8080,user:" + member.getUsername();
    }
}
