package cn.qbs.wa.union.auth.mapper;

import cn.qbs.wa.union.auth.pojo.member.MemberInfo;

public interface MemberLoginMapper {

    /**
     * 根据小程序用户ID获取用户信息
     */
    MemberInfo getByOpenId(String openId);

    /**
     * 根据账号获取用户信息
     */
    MemberInfo getByAccount(String account);

    void updateByMemberId(MemberInfo memberInfo);

    void insert(MemberInfo memberInfo);
}
