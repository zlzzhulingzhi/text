package cn.qbs.wa.teach.gateway.filter;

import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.StrUtil;
import cn.qbs.wa.teach.common.core.constant.*;
import cn.qbs.wa.teach.common.core.domain.LoginUser;
import cn.qbs.wa.teach.common.core.utils.JwtUtils;
import cn.qbs.wa.teach.common.core.utils.ServletUtils;
import cn.qbs.wa.teach.common.core.utils.StringUtils;
import cn.qbs.wa.teach.common.redis.service.RedisService;
import cn.qbs.wa.teach.gateway.config.IgnoreWhiteProperties;
import io.jsonwebtoken.Claims;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

/**
 * 网关鉴权
 *
 */
@Component
public class AuthFilter implements GlobalFilter, Ordered
{
    private static final Logger log = LoggerFactory.getLogger(AuthFilter.class);

    // 排除过滤的 uri 地址，nacos自行添加
    @Autowired
    private IgnoreWhiteProperties ignoreWhite;

    @Autowired
    private RedisService redisService;


    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain)
    {
        ServerHttpRequest request = exchange.getRequest();
        ServerHttpRequest.Builder mutate = request.mutate();

        // 学员端，根据域名识别租户ID加入请求头
        String client = request.getHeaders().getFirst(TenantConstants.CLIENT_MODE_HEADER);
        boolean isTenantMode = TenantConstants.DOMAIN.equals(client);
        String tenantId = null;
        if (isTenantMode) {
            String hostName = request.getURI().getHost();
            // 跳过不需要验证域名
            if (StringUtils.matches(hostName, ignoreWhite.getHosts())) {
                isTenantMode = false;
            } else {
                if (StrUtil.isNotBlank(hostName)) {
                    String secondDomain = hostName.split("\\.")[0];
                    tenantId = redisService.getCacheMapValue(CacheConstants.TENANT_HOST_MAPPING, secondDomain);
                    if (StrUtil.isNotBlank(tenantId)) {
                        addHeader(mutate, TenantConstants.TENANT_ID, tenantId);
                    } else {
                        log.error("can not find mapping by hostName：{}", hostName);
                        return notFoundResponse(exchange, "无法访问此页面");
                    }
                }
            }
        }

        String url = request.getURI().getPath();
        // 跳过不需要验证的路径
        addHeader(mutate, Constants.TRACE_ID_HTTP_FIELD, IdUtil.fastSimpleUUID());
        if (StringUtils.matches(url, ignoreWhite.getWhites()))
        {
            return chain.filter(exchange);
        }
        String token = getToken(request);
        if (StringUtils.isEmpty(token))
        {
            return unauthorizedResponse(exchange, "令牌不能为空");
        }
        Claims claims = JwtUtils.parseToken(token);
        if (claims == null)
        {
            return unauthorizedResponse(exchange, "令牌已过期或验证不正确！");
        }
        String userkey = JwtUtils.getUserKey(claims);
        boolean islogin = redisService.hasKey(getTokenKey(userkey));
        if (!islogin)
        {
            return unauthorizedResponse(exchange, "登录状态已过期");
        }
        String userid = JwtUtils.getUserId(claims);
        String username = JwtUtils.getUserName(claims);
        String userType = JwtUtils.getUserType(claims);
        if (StringUtils.isEmpty(userid))
        {
            return unauthorizedResponse(exchange, "令牌验证失败");
        }

        if (isTenantMode) {
            // 验证 token 中的orgId是否是对应域名，若不对应说明域名已变更
            LoginUser loginUser = redisService.getCacheObject(getTokenKey(userkey));
            if (loginUser == null || loginUser.getOrgId() == null) {
                return unauthorizedResponse(exchange, "登录状态已过期");
            }
            if (!loginUser.getOrgId().toString().equals(tenantId)) {
                return unauthorizedResponse(exchange, "令牌已不正确！");
            }
        }

        // 设置用户信息到请求
        addHeader(mutate, SecurityConstants.USER_KEY, userkey);
        addHeader(mutate, SecurityConstants.DETAILS_USER_ID, userid);
        addHeader(mutate, SecurityConstants.DETAILS_USERNAME, username);
        addHeader(mutate, SecurityConstants.USER_TYPE, userType);


        // 内部请求来源参数清除
        removeHeader(mutate, SecurityConstants.FROM_SOURCE);
        return chain.filter(exchange.mutate().request(mutate.build()).build());
    }

    private void addHeader(ServerHttpRequest.Builder mutate, String name, Object value)
    {
        if (value == null)
        {
            return;
        }
        String valueStr = value.toString();
        String valueEncode = ServletUtils.urlEncode(valueStr);
        mutate.header(name, valueEncode);
    }

    private void removeHeader(ServerHttpRequest.Builder mutate, String name)
    {
        mutate.headers(httpHeaders -> httpHeaders.remove(name)).build();
    }

    private Mono<Void> unauthorizedResponse(ServerWebExchange exchange, String msg)
    {
        log.error("[鉴权异常处理]请求路径:{}", exchange.getRequest().getPath());
        return ServletUtils.webFluxResponseWriter(exchange.getResponse(), msg, HttpStatus.UNAUTHORIZED);
    }

    private Mono<Void> notFoundResponse(ServerWebExchange exchange, String msg) {
        return ServletUtils.webFluxResponseWriter(exchange.getResponse(), org.springframework.http.HttpStatus.NOT_FOUND, msg, HttpStatus.NOT_FOUND);
    }

    /**
     * 获取缓存key
     */
    private String getTokenKey(String token)
    {
        return CacheConstants.LOGIN_TOKEN_KEY + token;
    }

    /**
     * 获取请求token
     */
    private String getToken(ServerHttpRequest request)
    {
        String token = request.getHeaders().getFirst(TokenConstants.AUTHENTICATION);
        // 如果前端设置了令牌前缀，则裁剪掉前缀
        if (StringUtils.isNotEmpty(token) && token.startsWith(TokenConstants.PREFIX))
        {
            token = token.replaceFirst(TokenConstants.PREFIX, StringUtils.EMPTY);
        }
        return token;
    }

    @Override
    public int getOrder()
    {
        return -200;
    }
}
