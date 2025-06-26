package com.controller;

import com.annotation.IgnoreAuth;
import com.entity.TokenEntity;
import com.service.TokenService;
import com.utils.R;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * Token 相关接口
 */
@RestController
@RequestMapping("/token")
public class TokenController {

    @Autowired
    private TokenService tokenService;

    /**
     * 校验 Token 是否有效
     *
     * 示例请求：GET /token/check?token=xxx
     */

    @IgnoreAuth
    @GetMapping("/check")
    public R checkToken(@RequestParam String token) {
        TokenEntity tokenEntity = tokenService.getTokenEntity(token);
        if (tokenEntity == null) {
            return R.error("Token 无效或已过期");
        }
        return R.ok();
    }
}
