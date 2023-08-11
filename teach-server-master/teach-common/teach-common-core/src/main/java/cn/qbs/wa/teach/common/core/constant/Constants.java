package cn.qbs.wa.teach.common.core.constant;

/**
 * 通用常量信息
 *
 * @author ruoyi
 */
public class Constants
{
    /**
     * UTF-8 字符集
     */
    public static final String UTF8 = "UTF-8";

    /**
     * GBK 字符集
     */
    public static final String GBK = "GBK";

    /**
     * RMI 远程方法调用
     */
    public static final String LOOKUP_RMI = "rmi://";

    /**
     * LDAP 远程方法调用
     */
    public static final String LOOKUP_LDAP = "ldap://";

    /**
     * http请求
     */
    public static final String HTTP = "http://";

    /**
     * https请求
     */
    public static final String HTTPS = "https://";

    /**
     * 成功标记
     */
    public static final Integer SUCCESS = 200;

    /**
     * 失败标记
     */
    public static final Integer FAIL = 500;

    /**
     * 登录成功
     */
    public static final String LOGIN_SUCCESS = "Success";

    /**
     * 注销
     */
    public static final String LOGOUT = "Logout";

    /**
     * 注册
     */
    public static final String REGISTER = "Register";

    /**
     * 登录失败
     */
    public static final String LOGIN_FAIL = "Error";

    /**
     * 初始密码
     */
    public static final String INIT_PASSWORD = "waj123456";


    /**
     * 平台机构
     */
    public static final Long INIT_ORG = 0L;


    /**
     * 平台分类应用
     */
    public static final Long PLAT_APP_TYPE_ID = 2L;

    /**
     * 机构分类应用
     */
    public static final Long ORG_APP_TYPE_ID = 3L;

    /**
     * 平台机构管理应用前缀
     */
    public static final String INIT_APP_NAME_PERFIX="平台";

    /**
     * 当前记录起始索引
     */
    public static final String PAGE_NUM = "pageNum";

    /**
     * 每页显示记录数
     */
    public static final String PAGE_SIZE = "pageSize";

    /**
     * 排序列
     */
    public static final String ORDER_BY_COLUMN = "orderByColumn";

    /**
     * 排序的方向 "desc" 或者 "asc".
     */
    public static final String IS_ASC = "isAsc";

    /**
     * 验证码 redis key
     */
    public static final String CAPTCHA_CODE_KEY = "captcha_codes:";

    /**
     * 验证码有效期（分钟）
     */
    public static final long CAPTCHA_EXPIRATION = 2;


    /**
     * 参数管理 cache key
     */
    public static final String SYS_CONFIG_KEY = "sys_config:";

    /**
     * 字典管理 cache key
     */
    public static final String SYS_DICT_KEY = "sys_dict:";

    /**
     * 资源映射路径 前缀
     */
    public static final String RESOURCE_PREFIX = "/profile";

    /**
     * 数据库 启用
     */
    public static final Integer DB_TRUE = 1;

    /**
     * 数据库 停用
     */
    public static final Integer DB_FAIL = 0;



    /**
     * 数据库 无限次
     */
    public static final Integer DB_UNLIMITED = -1;


    /**
     * 日志跟踪标识
     */
    public static final String TRACE_ID_MDC_FIELD = "traceId";

    public static final String TRACE_ID_HTTP_FIELD = "trace_id";

    /**
     * 支付日期格式化
     */
    public static final String PAY_DATE_FORMAT = "yyyyMMddHHmmss";

    /**
     * 退款日期格式化
     */
    public static final String REFUND_DATE_FORMAT = "yyyy-MM-dd HH:mm:ss";

    /**
     * 分类父节点ID串层级分隔符
     */
    public static final String CATEGORY_PARENT_CODE_SEPARATOR = ",";

    /**
     * 分类全称层级分隔符
     */
    public static final String CATEGORY_FULL_NAME_SEPARATOR = "/";

}
