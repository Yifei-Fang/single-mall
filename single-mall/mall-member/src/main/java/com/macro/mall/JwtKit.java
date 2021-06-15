package com.macro.mall;

import com.macro.mall.common.exception.BusinessException;
import com.macro.mall.config.properties.JwtProperties;
import com.macro.mall.domain.UmsMember;
import io.jsonwebtoken.*;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * @Author superUser
 * @Date 2021/6/14
 */
public class JwtKit {

    @Autowired
    private JwtProperties jwtProperties;

    /**
     * 获取jwt令牌
     * @param umsMember
     * @return
     */
    public String generateToken(UmsMember umsMember){
        Map<String,Object> claims = new HashMap<>();
        //这里的数据是不安全的  基于base64编码的  js端可以直接解码出明文
        claims.put("sub",umsMember.getUsername());
        claims.put("createdate",new Date());
        claims.put("id",umsMember.getId());
        claims.put("memberLevelId",umsMember.getMemberLevelId());
        return Jwts.builder()
                .addClaims(claims)
                .setExpiration(new Date(System.currentTimeMillis() + jwtProperties.getExpiration() * 1000))
                .signWith(SignatureAlgorithm.HS256,jwtProperties.getSecret())
                .compact();
    }

    /**
     * 令牌解析
     * @param token
     * @return
     * @throws BusinessException
     */
    public Claims parseJwtToken(String token)throws BusinessException{
        Claims claims = null;
        try {
            claims = Jwts.parser()
                    .setSigningKey(jwtProperties.getSecret())
                    .parseClaimsJwt(token)
                    .getBody();
        } catch (ExpiredJwtException e) {
            throw new BusinessException("Jwt认证失败：token已经过期。");
        } catch (UnsupportedJwtException e) {
            throw new BusinessException("Jwt认证失败：token格式不支持。");
        } catch (MalformedJwtException e) {
            throw new BusinessException("Jwt认证失败：无效的token。");
        } catch (SignatureException e) {
            throw new BusinessException("Jwt认证失败：无效的token。");
        } catch (IllegalArgumentException e) {
            throw new BusinessException("Jwt认证失败：无效的token。");
        }
        return claims;
    }
}
