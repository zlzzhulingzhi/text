package cn.qbs.wa.union.admin.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.lang.Validator;
import cn.hutool.core.util.StrUtil;
import cn.qbs.wa.teach.common.core.constant.Constants;
import cn.qbs.wa.teach.common.core.exception.ServiceException;
import cn.qbs.wa.teach.common.core.utils.AesUtil;
import cn.qbs.wa.union.admin.entity.UniMember;
import cn.qbs.wa.union.admin.mapper.UniMemberMapper;
import cn.qbs.wa.union.admin.pojo.unimember.UniMemberPageRequest;
import cn.qbs.wa.union.admin.pojo.unimember.UniMemberRegisterRequest;
import cn.qbs.wa.union.admin.pojo.unimember.UniMemberRegisterResponse;
import cn.qbs.wa.union.admin.pojo.unimember.UniMemberResponse;
import cn.qbs.wa.union.admin.service.UniMemberService;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * 统一会员用户表(UniMember)表服务实现类
 *
 * @author makejava
 * @version 1.0
 * @date 2022-07-21 09:11:24
 */
@Slf4j
@Service("uniMemberService")
public class UniMemberServiceImpl extends ServiceImpl<UniMemberMapper, UniMember> implements UniMemberService {

    @Override
    public UniMemberRegisterResponse registerFromInner(UniMemberRegisterRequest params) {
        // 查询账号是否存在
        UniMember member = this.lambdaQuery().select(UniMember::getId, UniMember::getPhone).eq(UniMember::getAccount, params.getAccount()).one();
        if (member == null) {
            // 不存在 则注册会员用户
            member = this.register(params);
        } else {
            BeanUtil.copyProperties(params, member);
            member.setPhone(null);
            member.setAccount(null);
            this.updateById(member);
        }
        return BeanUtil.copyProperties(member, UniMemberRegisterResponse.class);
    }

    @Override
    public UniMember register(UniMemberRegisterRequest params) {
        params.setPhone(params.getAccount());
        UniMember uniMember = BeanUtil.copyProperties(params, UniMember.class);
        this.save(uniMember);
        return uniMember;
    }

    @Override
    public Boolean changePhone(Long memberId, String phone) {
        UniMember member = this.getById(memberId);
        if (member == null) {
            throw new ServiceException("无法找到该用户");
        }
        String encodePhone = AesUtil.encode(phone);
        if (member.getAccount().equals(encodePhone)) {
            throw new ServiceException("新旧手机号一致，无需更改");
        }
        Optional<UniMember> opt = this.lambdaQuery().eq(UniMember::getAccount, encodePhone).oneOpt();
        if (opt.isPresent()) {
            throw new ServiceException("该手机号已存在");
        }
        UniMember update = new UniMember();
        update.setId(memberId);
        update.setAccount(encodePhone);
        update.setPhone(encodePhone);
        return this.updateById(update);
    }

    @Override
    public IPage<UniMemberResponse> page(UniMemberPageRequest params) {
        if(params.getPhone()!=null && !params.getPhone().isEmpty()){
            params.setPhone(AesUtil.encode(params.getPhone()));
        }
        if(params.getUnitId()==null){
            List<UniMember>  uniMemberList=this.lambdaQuery().list();
            List<Long> list=new ArrayList<>();
            for (UniMember uniMember:uniMemberList) {
                if (uniMember.getUnitId()!=null){
                    list.add(uniMember.getUnitId());
                }
            }
            params.setUnitIdList(list);
        }
        IPage<UniMemberResponse> uniMemberResponseIPage=baseMapper.page(params.createMpPage(), params);
        for (UniMemberResponse uniMemberResponse:uniMemberResponseIPage.getRecords()) {
            if (StrUtil.isNotBlank(uniMemberResponse.getPhone())) {
                String phone1=AesUtil.decode(uniMemberResponse.getPhone());
                String phone2=phone1.substring(0,3)+"****"+phone1.substring(phone1.length()-4,phone1.length());
                uniMemberResponse.setPhone(phone2);
                uniMemberResponse.setAccount(phone2);
            }
        }
        return uniMemberResponseIPage;
    }

    @Override
    public IPage<UniMemberResponse> pageStudent(UniMemberPageRequest params) {
        if (StringUtils.isNotBlank(params.getSearchContent()) && Validator.isMobile(params.getSearchContent())) {
            params.setSearchContent(AesUtil.encode(params.getSearchContent()));
        }
        IPage<UniMemberResponse> uniMemberResponseIPage=baseMapper.page(params.createMpPage(), params);
        for (UniMemberResponse uniMemberResponse:uniMemberResponseIPage.getRecords()) {
            if (StrUtil.isNotBlank(uniMemberResponse.getPhone())) {
                String phone1=AesUtil.decode(uniMemberResponse.getPhone());
                String phone2=phone1.substring(0,3)+"****"+phone1.substring(phone1.length()-4,phone1.length());
                uniMemberResponse.setPhone(phone2);
            }
        }
        return uniMemberResponseIPage;
    }
}

