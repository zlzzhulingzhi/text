package cn.qbs.wa.teach.common.core.constant;

/**
 * 缓存的key 常量
 *
 *
 */
public class CacheConstants
{
    /**
     * 缓存有效期，默认10080（分钟）
     */
    public final static long EXPIRATION = 24 * 60;

    /**
     * 缓存刷新时间，默认120（分钟）
     */
    public final static long REFRESH_TIME = 55;

    /**
     * 权限缓存前缀
     */
    public final static String LOGIN_TOKEN_KEY = "login_tokens:";

    /**
     * 登录验证码前缀
     */
    public final static String LOGIN_CODE = "login_code:";

    /**
     * 登录验证码前缀
     */
    public final static String CHANGE_PHONE_CODE = "change_phone_code:";

    /**
     * 注册验证码前缀
     */
    public final static String REGISTER_PHONE_CODE = "register_phone_code:";

    /**
     * 禁用登录前缀 forbidden
     */
    public final static String FORIBDDEN_CODE = "forbidden_code:";

    /**
     * 禁用刷新时间，默认60（分钟）
     */
    public final static long FORIBDDEN_TIME = 60;

    /**
     * 禁用次数，默认5（分钟）
     */
    public final static long FORIBDDEN_COUNT = 5;

    /**
     * 租户域名映射关系
     */
    public final static String TENANT_HOST_MAPPING = "tenant_host_mapping";

    /**
     * 腾讯对象存储签名缓存
     */
    public final static String COS_CREDENTIAL_KEY = "cos_credential:";

    /**
     * 学员注册验证码前缀
     */
    public final static String BIND_PHONE_CODE = "bind_phone_code:";

    /**
     * 学员注册验证码前缀
     */
    public final static String FORGET_PWD_CODE = "forget_pwd_code:";


    /**
     * 图形验证码前缀
     */
    public final static String CAPTCHA_CODE = "captcha_code:";
}
