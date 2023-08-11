package cn.qbs.wa.teach.common.core.context;


import cn.qbs.wa.teach.common.core.constant.SecurityConstants;
import cn.qbs.wa.teach.common.core.utils.ConvertUtils;
import cn.qbs.wa.teach.common.core.utils.StringUtils;
import com.alibaba.ttl.TransmittableThreadLocal;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 获取当前线程变量中的 用户id、用户名称、Token等信息
 * 注意： 必须在网关通过请求头的方法传入，同时在HeaderInterceptor拦截器设置值。 否则这里无法获取
 *
 * @author ruoyi
 */
public class SecurityContextHolder {
    private static final TransmittableThreadLocal<Map<String, Object>> THREAD_LOCAL = new TransmittableThreadLocal<>();

    public static void set(String key, Object value) {
        Map<String, Object> map = getLocalMap();
        map.put(key, value == null ? StringUtils.EMPTY : value);
    }

    public static String get(String key) {
        Map<String, Object> map = getLocalMap();
        return ConvertUtils.toStr(map.getOrDefault(key, StringUtils.EMPTY));
    }

    public static <T> T get(String key, Class<T> clazz) {
        Map<String, Object> map = getLocalMap();
        return StringUtils.cast(map.getOrDefault(key, null));
    }

    public static Map<String, Object> getLocalMap() {
        Map<String, Object> map = THREAD_LOCAL.get();
        if (map == null) {
            map = new ConcurrentHashMap<String, Object>();
            THREAD_LOCAL.set(map);
        }
        return map;
    }

    public static void setLocalMap(Map<String, Object> threadLocalMap) {
        THREAD_LOCAL.set(threadLocalMap);
    }

    public static Long getUserId() {
        return ConvertUtils.toLong(get(SecurityConstants.DETAILS_USER_ID), null);
    }

    public static void setUserId(String account) {
        set(SecurityConstants.DETAILS_USER_ID, account);
    }

    public static String getUserName() {
        return get(SecurityConstants.DETAILS_USERNAME);
    }

    public static void setUserName(String username) {
        set(SecurityConstants.DETAILS_USERNAME, username);
    }

    public static String getUserKey() {
        return get(SecurityConstants.USER_KEY);
    }

    public static void setUserKey(String userKey) {
        set(SecurityConstants.USER_KEY, userKey);
    }

    public static String getUserType() {
        return get(SecurityConstants.USER_TYPE);
    }

    public static void setUserType(String userKey) {
        set(SecurityConstants.USER_TYPE, userKey);
    }

    public static Long getOrgId() {
        return ConvertUtils.toLong(get(SecurityConstants.DETAILS_ORG_ID), 0L);
    }

    public static void setOrgId(String orgId) {
        set(SecurityConstants.DETAILS_ORG_ID, orgId);
    }

    //针对管理员筛选的orgId
    public static Long getSelectOrgId() {
        return ConvertUtils.toLong(get(SecurityConstants.SELECT_ORG_ID), null);
    }

    public static void setSelectOrgId(String orgId) {
        set(SecurityConstants.SELECT_ORG_ID, orgId);
    }

    //针对管理员筛选的orgId
    public static Long getEmployeeId() {
        return ConvertUtils.toLong(get(SecurityConstants.EMPLOYEE_ID), null);
    }

    public static void setEmployeeId(String employeeId) {
        set(SecurityConstants.EMPLOYEE_ID, employeeId);
    }

    public static Long getStudentId() {
        return ConvertUtils.toLong(get(SecurityConstants.STUDENT_ID), null);
    }

    public static void setStudentId(String studentId) {
        set(SecurityConstants.STUDENT_ID, studentId);
    }

    /**
     * 判断是否既是学员也是机构的职员
     */
    public static Boolean isMember() {
        return ConvertUtils.toBool(get(SecurityConstants.IS_MEMBER), Boolean.FALSE);
    }

    public static void remove() {
        THREAD_LOCAL.remove();
    }
}
