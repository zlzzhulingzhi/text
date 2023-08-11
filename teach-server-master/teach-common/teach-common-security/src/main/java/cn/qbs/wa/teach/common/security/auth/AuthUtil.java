package cn.qbs.wa.teach.common.security.auth;


import cn.qbs.wa.teach.common.core.domain.LoginUser;
import cn.qbs.wa.teach.common.security.annotation.RequiresPermissions;
import cn.qbs.wa.teach.common.security.annotation.RequiresRoles;

/**
 * Token 权限验证工具类
 *
 * @author ruoyi
 */
public class AuthUtil
{
    /**
     * 底层的 AuthLogic 对象
     */
    public static AuthLogic authLogic = new AuthLogic();

    /**
     * 会话注销
     */
    public static void logout()
    {
        authLogic.logout();
    }

    /**
     * 会话注销，根据指定Token
     *
     * @param tokenValue 指定token
     */
    public static void logoutByToken(String token)
    {
        authLogic.logoutByToken(token);
    }

    /**
     * 检验当前会话是否已经登录，如未登录，则抛出异常
     */
    public static void checkLogin()
    {
        authLogic.checkLogin();
    }

    /**
     * 获取当前登录用户信息
     */
    public static LoginUser getLoginUser(String token)
    {
        return authLogic.getLoginUser(token);
    }

    /**
     * 验证当前用户有效期
     */
    public static void verifyLoginUserExpire(LoginUser loginUser)
    {
        authLogic.verifyLoginUserExpire(loginUser);
    }



    /**
     * 当前账号是否含有指定权限, 返回true或false
     *
     * @param permission 权限码
     * @return 是否含有指定权限
     */
    public static boolean hasPermi(String permission)
    {
        return authLogic.hasPermi(permission);
    }

    /**
     * 当前账号是否含有指定权限, 如果验证未通过，则抛出异常: NotPermissionException
     *
     * @param permission 权限码
     */
    public static void checkPermi(String permission)
    {
        authLogic.checkPermi(permission);
    }

    /**
     * 根据注解传入参数鉴权, 如果验证未通过，则抛出异常: NotPermissionException
     *
     * @param requiresPermissions 权限注解
     */
    public static void checkPermi(RequiresPermissions requiresPermissions)
    {
        authLogic.checkPermi(requiresPermissions);
    }

    /**
     * 当前账号是否含有指定权限 [指定多个，必须全部验证通过]
     *
     * @param permissions 权限码数组
     */
    public static void checkPermiAnd(String... permissions)
    {
        authLogic.checkPermiAnd(permissions);
    }

    /**
     * 当前账号是否含有指定权限 [指定多个，只要其一验证通过即可]
     *
     * @param permissions 权限码数组
     */
    public static void checkPermiOr(String... permissions)
    {
        authLogic.checkPermiOr(permissions);
    }

    public static void checkRole(RequiresRoles requiresRoles) {
        authLogic.checkRole(requiresRoles);
    }
}
