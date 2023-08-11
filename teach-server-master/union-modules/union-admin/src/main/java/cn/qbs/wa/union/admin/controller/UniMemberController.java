package cn.qbs.wa.union.admin.controller;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import cn.qbs.wa.teach.common.core.constant.CacheConstants;
import cn.qbs.wa.teach.common.core.constant.Constants;
import cn.qbs.wa.teach.common.core.constant.SecurityConstants;
import cn.qbs.wa.teach.common.core.constant.UserConstants;
import cn.qbs.wa.teach.common.core.context.SecurityContextHolder;
import cn.qbs.wa.teach.common.core.domain.EnableRequest;
import cn.qbs.wa.teach.common.core.domain.IdListRequest;
import cn.qbs.wa.teach.common.core.domain.IdRequest;
import cn.qbs.wa.teach.common.core.domain.R;
import cn.qbs.wa.teach.common.core.utils.AesUtil;
import cn.qbs.wa.teach.common.core.utils.StringUtils;
import cn.qbs.wa.teach.common.redis.service.RedisService;
import cn.qbs.wa.teach.common.security.annotation.AccessAuth;
import cn.qbs.wa.union.admin.entity.UniMember;
import cn.qbs.wa.union.admin.pojo.unimember.*;
import cn.qbs.wa.union.admin.service.UniMemberService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 统一会员用户表(UniMember)表控制层
 *
 * @author makejava
 * @version 1.0
 * @date 2022-07-21 09:11:24
 */
@RestController
@RequestMapping("/uniMember")
public class UniMemberController {

    /**
     * 服务对象
     */
    @Resource
    private UniMemberService uniMemberService;

    @Autowired
    private RedisService redisService;

    /**
     * 注册统一会员用户表
     *
     * @param register 注册信息
     * @return 注册结果信息
     */
    @AccessAuth({SecurityConstants.INNER})
    @PostMapping("/inner/register")
    public R<UniMemberRegisterResponse> registerFromInner(@RequestBody UniMemberRegisterRequest register) {
        register.setAccount(AesUtil.encode(register.getAccount()));
        register.setPhone(AesUtil.encode(register.getPhone()));
        if (StrUtil.isNotBlank(register.getIdNumber())) {
            register.setIdNumber(AesUtil.encode(register.getIdNumber()));
        }
        return R.ok(this.uniMemberService.registerFromInner(register));
    }

    @AccessAuth({SecurityConstants.INNER})
    @PostMapping("/inner/info")
    public R<UniMemberResponse> info(@RequestBody IdRequest idRequest) {
        UniMember member = this.uniMemberService.getById(idRequest.getId());
        if (member == null) {
            return R.fail("该用户不存在");
        }
        return R.ok(BeanUtil.copyProperties(member, UniMemberResponse.class));
    }

    @AccessAuth({SecurityConstants.INNER})
    @PostMapping("/inner/getByNameOrPhone")
    public R<List<UniMember>> getByNameOrPhone(@RequestBody UniMemberTCourseStudentRequest uniMemberTCourseStudentRequest) {
        List<UniMember> uniMemberList=new ArrayList<>();
        if (StringUtils.isNotBlank(uniMemberTCourseStudentRequest.getRealName()) && StringUtils.isNotBlank(uniMemberTCourseStudentRequest.getPhone())){
            uniMemberTCourseStudentRequest.setPhone(AesUtil.encode(uniMemberTCourseStudentRequest.getPhone()));
            List<UniMember> uniMemberList1=uniMemberService.lambdaQuery().eq(UniMember::getPhone,uniMemberTCourseStudentRequest.getPhone()).
                    like(UniMember::getRealName,uniMemberTCourseStudentRequest.getRealName()).list();
            uniMemberList.addAll(uniMemberList1);
            return R.ok(uniMemberList);
        }
        if (StringUtils.isNotBlank(uniMemberTCourseStudentRequest.getRealName())){
            List<UniMember> uniMemberList1=uniMemberService.lambdaQuery().like(UniMember::getRealName,uniMemberTCourseStudentRequest.getRealName()).list();
            uniMemberList.addAll(uniMemberList1);
            return R.ok(uniMemberList);
        }
        if (StringUtils.isNotBlank(uniMemberTCourseStudentRequest.getPhone())){
            uniMemberTCourseStudentRequest.setPhone(AesUtil.encode(uniMemberTCourseStudentRequest.getPhone()));
            List<UniMember> uniMemberList2=uniMemberService.lambdaQuery().eq(UniMember::getPhone,uniMemberTCourseStudentRequest.getPhone()).list();
            uniMemberList.addAll(uniMemberList2);
            return R.ok(uniMemberList);
        }
        return R.ok(uniMemberList);
    }

    @AccessAuth({SecurityConstants.INNER})
    @PostMapping("/inner/list")
    public R<List<UniMemberResponse>> list(@RequestBody IdListRequest request) {
        List<UniMember> uniMembers = this.uniMemberService.listByIds(request.getIdList());
        return R.ok(BeanUtil.copyToList(uniMembers, UniMemberResponse.class));
    }

    @AccessAuth({SecurityConstants.INNER})
    @PostMapping("/inner/getCount")
    public R<Long> list(@RequestBody UniMemberPageRequest request) {
        Long count = this.uniMemberService.lambdaQuery().eq(UniMember::getEnabled,Constants.DB_TRUE).
                eq(UniMember::getUnitId,request.getUnitId()).count();
        return R.ok(count);
    }

    /**
     * 会员用户列表查询接口，根据条件选择性分页
     */
    @AccessAuth({SecurityConstants.INNER})
    @PostMapping("/inner/query")
    public R<IPage<UniMemberResponse>> query(@RequestBody UniMemberQuery request) {
        LambdaQueryWrapper<UniMember> wrapper = Wrappers.<UniMember>lambdaQuery()
                .eq(request.getEnabled() != null, UniMember::getEnabled, request.getEnabled())
                .eq(StrUtil.isNotBlank(request.getPhone()), UniMember::getPhone, request.getPhone())
                .like(StrUtil.isNotBlank(request.getRealName()), UniMember::getRealName, request.getRealName())
                .orderByDesc(UniMember::getId);
        IPage<UniMemberResponse> page;
        if (request.getCurrent() != null && request.getSize() != null) {
            // 分页查询
            Page<UniMember> memberPage = this.uniMemberService.page(new Page<>(request.getCurrent(), request.getSize()), wrapper);
            page = memberPage.convert(p -> BeanUtil.copyProperties(p, UniMemberResponse.class));
        } else {
            // 限制最大不超过200条
            wrapper.last("LIMIT 200");
            page = new Page<>();
            List<UniMember> memberList = this.uniMemberService.list(wrapper);
            page.setTotal(memberList.size());
            page.setRecords(BeanUtil.copyToList(memberList, UniMemberResponse.class));
        }
        return R.ok(page);
    }

    @ApiOperation("会员用户信息")
    @PostMapping("/info")
    public R<UniMemberResponse> info() {
        if (!UserConstants.USER_STUDENT.equals(SecurityContextHolder.getUserType())) {
            return null;
        }
        return R.ok(BeanUtil.copyProperties(this.uniMemberService.getById(SecurityContextHolder.getUserId()), UniMemberResponse.class));
    }

    @ApiOperation("修改基础信息")
    @PostMapping("/update")
    public R<Boolean> update(@RequestBody UniMemberUpdateRequest request) {
        UniMember uniMember = BeanUtil.copyProperties(request, UniMember.class);
        if (StrUtil.isNotBlank(uniMember.getIdNumber())) {
            uniMember.setIdNumber(AesUtil.encode(uniMember.getIdNumber()));
        }
        uniMember.setId(SecurityContextHolder.getUserId());
        return R.ok(this.uniMemberService.updateById(uniMember));
    }

    @ApiOperation("修改手机号码")
    @PostMapping("/change-phone")
    public R<Boolean> changePhone(@RequestBody @Validated PhoneUpdateRequest params) {
        String phone = params.getPhone();
        Object cacheCode = redisService.getCacheObject(CacheConstants.CHANGE_PHONE_CODE + phone);
        if (cacheCode == null) {
            return R.fail("验证码已过期请重新再试");
        }
        String code = cacheCode.toString();
        if (!code.equals(params.getCode())) {
            return R.fail("验证码错误,请重新输入");
        }
        Boolean changePhone = this.uniMemberService.changePhone(SecurityContextHolder.getUserId(), params.getPhone());
        redisService.deleteObject(CacheConstants.CHANGE_PHONE_CODE + phone);
        return R.ok(changePhone);
    }

    @PostMapping("page")
    @ApiOperation("分页查询单位职员")
    public R<IPage<UniMemberResponse>> page(@RequestBody UniMemberPageRequest params) {
        return R.ok(this.uniMemberService.page(params));
    }

    @PostMapping("pageStudent")
    @ApiOperation("分页查询学生信息")
    public R<IPage<UniMemberResponse>> pageStudent(@RequestBody UniMemberPageRequest params) {
        return R.ok(this.uniMemberService.pageStudent(params));
    }

    @ApiOperation("修改学生信息")
    @PostMapping("/enable")
    public R<Boolean> enable(@RequestBody EnableRequest request) {
        return R.ok(uniMemberService.lambdaUpdate().in(UniMember::getId, request.getIdList()).set(UniMember::getEnabled, request.getEnabled()).update());
    }


}

