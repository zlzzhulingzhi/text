package cn.qbs.wa.teach.common.core.domain;

import lombok.Data;

import java.io.Serializable;
import java.util.List;
import java.util.Set;

/**
 * 用户信息
 *
 * @author ruoyi
 */

@Data
public class LoginUser implements Serializable
{
    private static final long serialVersionUID = 1L;

    /**
     * 用户唯一标识
     */
    private String token;


    /**
     * 用户头像
     */
    private String headImgUrl;

    private String nickName;

    /**
     * 用户类型
     */
    private String userType;

    /**
     * 账号
     */
    private String account;

    /**
     * 用户名id
     */
    private Long userid;

    /**
     * 机构id
     */

    private Long orgId;

    /**
     * 职工id
     */

    private Long employeeId;

    /**
     * 学员id
     */
    private Long studentId;


    /**
     * 用户名
     */
    private String username;

    /**
     * 登录时间
     */
    private Long loginTime;

    /**
     * 过期时间
     */
    private Long expireTime;

    /**
     * 登录IP地址
     */
    private String ipaddr;

    /**
     * 权限列表
     */
    private Set<String> permissions;

    /**
     * 角色列表
     */
    private Set<String> roles;

    /**
     * 角色id列表
     */
    private List<Long> roleIdList;

    /**
     * 用户信息
     */
    private SysUser sysUser;


    /**
     * 是否平台用户
     */
    private Boolean isAdmin;

    /**
     * 考试id
     */
    private Long examId;

    /**
     * 是否第一次登录
     */
    private Boolean firstLogin=false;

    /**
     * 域名
     */
    private String domain;

}
