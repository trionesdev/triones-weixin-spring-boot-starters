package com.trionesdev.weixin.miniprogram.spring.boot.rest;

import com.trionesdev.weixin.miniprogram.WeiXinMiniProgram;
import com.trionesdev.weixin.miniprogram.model.Code2SessionRequest;
import com.trionesdev.weixin.miniprogram.model.Code2SessionResponse;
import com.trionesdev.weixin.miniprogram.model.GetUserPhoneNumberRequest;
import com.trionesdev.weixin.miniprogram.model.UserPhoneNumberResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


@RequiredArgsConstructor
@RestController
@RequestMapping(value = "rest-api/weixin/miniprogram")
public class WeiXinMiniProgramResource {

    private final ObjectProvider<WeiXinMiniProgram> weiXinMiniProgram;


    /**
     * 获取微信小程序用户会话
     * @param code
     * @return
     */
    @GetMapping(value = "code-to-session")
    public Code2SessionResponse getCode2Session(@RequestParam(value = "code") String code,@RequestParam(value = "appId", required = false) String appId) {
        return weiXinMiniProgram.getIfAvailable().code2Session(Code2SessionRequest.builder().code(code).appId(appId).build());
    }

    /**
     * 获取微信小程序用户手机号
     * @param code
     * @return
     */
    @GetMapping(value = "user-phone-number")
    public UserPhoneNumberResponse getUserPhoneNumber(@RequestParam(value = "code") String code,@RequestParam(value = "appId", required = false) String appId) {
        var request = new GetUserPhoneNumberRequest();
        request.setCode(code);
        request.setAppId(appId);
        return weiXinMiniProgram.getIfAvailable().getUserPhoneNumber(request);
    }
}
