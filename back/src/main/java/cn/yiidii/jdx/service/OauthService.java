package cn.yiidii.jdx.service;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.jwt.JWTUtil;
import cn.yiidii.jdx.config.prop.SystemConfigProperties;
import cn.yiidii.jdx.config.prop.SystemConfigProperties.SocialPlatform;
import cn.yiidii.jdx.model.enums.SocialPlatformEnum;
import cn.yiidii.jdx.model.ex.BizException;
import cn.yiidii.jdx.model.ex.UnauthorizedException;
import com.alibaba.fastjson.JSONObject;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.zhyd.oauth.config.AuthConfig;
import me.zhyd.oauth.enums.AuthResponseStatus;
import me.zhyd.oauth.model.AuthCallback;
import me.zhyd.oauth.model.AuthResponse;
import me.zhyd.oauth.model.AuthUser;
import me.zhyd.oauth.request.AuthBaiduRequest;
import me.zhyd.oauth.request.AuthGiteeRequest;
import me.zhyd.oauth.request.AuthGithubRequest;
import me.zhyd.oauth.request.AuthRequest;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

/**
 * AuthService
 *
 * @author ed w
 * @since 1.0
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class OauthService {

    private final SystemConfigProperties systemConfigProperties;

    /**
     * 获取认证Request
     *
     * @param source source
     * @return AuthRequest
     */
    public AuthRequest getAuthRequest(String source) {
        SocialPlatformEnum socialPlatformEnum = SocialPlatformEnum.get(source);
        if (Objects.isNull(socialPlatformEnum)) {
            throw new BizException("暂不支持该平台登录");
        }
        SocialPlatform socialConfig = systemConfigProperties.getSocialConfig(socialPlatformEnum);
        if (Objects.isNull(socialConfig)) {
            throw new BizException("未配置该平台");
        }
        switch (socialPlatformEnum) {
            case GITHUB: {
                return new AuthGithubRequest(AuthConfig.builder()
                        .clientId(socialConfig.getClientId())
                        .clientSecret(socialConfig.getClientSecret())
                        .redirectUri(socialConfig.getRedirectUri())
                        .build());
            }
            case GITEE: {
                return new AuthGiteeRequest(AuthConfig.builder()
                        .clientId(socialConfig.getClientId())
                        .clientSecret(socialConfig.getClientSecret())
                        .redirectUri(socialConfig.getRedirectUri())
                        .build());
            }
            case BAIDU: {
                return new AuthBaiduRequest(AuthConfig.builder()
                        .clientId(socialConfig.getClientId())
                        .clientSecret(socialConfig.getClientSecret())
                        .redirectUri(socialConfig.getRedirectUri())
                        .build());
            }
            default: {
                throw new BizException("暂不支持该平台登录");
            }
        }
    }


    /**
     * 社交登录
     *
     * @param paramJo paramJo
     * @return JSONObject
     */
    public JSONObject login(JSONObject paramJo) throws Exception {
        String source = paramJo.getString("source");
        String code = paramJo.getString("code");
        String state = paramJo.getString("state");

        Assert.isTrue(StrUtil.isNotBlank(source), () -> {
            throw new BizException("source不能为空");
        });
        Assert.isTrue(StrUtil.isNotBlank(code), () -> {
            throw new BizException("code不能为空");
        });
        Assert.isTrue(StrUtil.isNotBlank(state), () -> {
            throw new BizException("state不能为空");
        });

        AuthRequest authRequest = this.getAuthRequest(source);
        AuthCallback authCallback = AuthCallback.builder()
                .code(code)
                .state(state)
                .build();
        AuthResponse<AuthUser> response = authRequest.login(authCallback);
        if (Objects.isNull(response) || AuthResponseStatus.SUCCESS.getCode() != response.getCode()) {
            throw new BizException("登陆失败");
        }

        AuthUser authUser = response.getData();
        String username = authUser.getUsername();
        SocialPlatform socialConfig = systemConfigProperties.getSocialConfig(source);
        List<String> admins = Arrays.stream(socialConfig.getAdmin().replaceAll("\\s+", "").split(",")).distinct().collect(Collectors.toList());
        if (!CollUtil.contains(admins, username)) {
            throw new UnauthorizedException("您不是系统管理员");
        }

        JSONObject jo = new JSONObject();
        jo.put("uuid", authUser.getUuid());
        jo.put("username", username);
        jo.put("nickname", authUser.getNickname());
        jo.put("avatar", authUser.getAvatar());
        jo.put("thirdToken", authUser.getToken().getAccessToken());
        jo.put("tokenType", authUser.getToken().getTokenType());
        jo.put("exp", DateUtil.offsetHour(new Date(), 24));
        jo.put("token", JWTUtil.createToken(jo, "jdx".getBytes(StandardCharsets.UTF_8)));
        return jo;
    }

}
