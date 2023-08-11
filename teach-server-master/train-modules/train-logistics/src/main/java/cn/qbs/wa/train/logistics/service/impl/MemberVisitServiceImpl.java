package cn.qbs.wa.train.logistics.service.impl;

import cn.qbs.wa.teach.common.core.context.SecurityContextHolder;
import cn.qbs.wa.teach.common.core.exception.IllegalParamsException;
import cn.qbs.wa.teach.common.core.utils.AesUtil;
import cn.qbs.wa.teach.common.core.utils.StringUtils;
import cn.qbs.wa.teach.common.security.utils.SecurityUtils;
import cn.qbs.wa.train.logistics.entity.Organization;
import cn.qbs.wa.train.logistics.mapper.MemberVisitMapper;
import cn.qbs.wa.train.logistics.entity.MemberVisit;
import cn.qbs.wa.train.logistics.mapper.OrganizationMapper;
import cn.qbs.wa.train.logistics.service.MemberVisitService;
import cn.qbs.wa.train.logistics.pojo.membervisit.*;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * 学员访问管理(MemberVisit)表服务实现类
 *
 * @author makejava
 * @since 2022-12-28 16:24:20
 */
@Slf4j
@Service("memberVisitService")
public class MemberVisitServiceImpl extends ServiceImpl<MemberVisitMapper, MemberVisit> implements MemberVisitService {

    @Resource
    private OrganizationMapper organizationMapper;

    @Override
    public boolean add(MemberVisitAddRequest params) {
        MemberVisit memberVisit = new MemberVisit();
        BeanUtils.copyProperties(params, memberVisit);
        if(StringUtils.isNotBlank(params.getPhone())){
            memberVisit.setPhone(AesUtil.encode(params.getPhone()));
        }
        if(StringUtils.isNotBlank(params.getCarNumber())){
            memberVisit.setCarNumber(AesUtil.encode(params.getCarNumber()));
        }
        if(params.getVisitOrgId()!=null){
            Organization organization=organizationMapper.selectById(params.getVisitOrgId());
            if(organization!=null){
                memberVisit.setVisitDepartment(organization.getName());
            }
        }
        return this.save(memberVisit);
    }

    @Override
    public IPage<MemberVisitPageResponse> page(MemberVisitPageRequest params) {
        List<String> roles=new ArrayList<String>(SecurityUtils.getLoginUser().getRoles());
        params.setRoles(roles);
        if(CollectionUtils.isEmpty(roles)){
            return params.createMpPage();
        }
        if(!roles.contains("plat_manager") && !roles.contains("zxwy")){
            return params.createMpPage();
        }
        if(roles.contains("plat_manager") && roles.contains("zxwy")){
            params.setRoles(null);
        }
        if(StringUtils.isNotBlank(params.getPhone())){
            params.setPhone(AesUtil.encode(params.getPhone()));
        }
        IPage<MemberVisitPageResponse> memberVisitPageResponseIPage=baseMapper.page(params.createMpPage(), params);
        for (MemberVisitPageResponse memberVisitPageResponse:memberVisitPageResponseIPage.getRecords()) {
            if(StringUtils.isNotBlank(memberVisitPageResponse.getPhone())){
                memberVisitPageResponse.setPhone(AesUtil.decode(memberVisitPageResponse.getPhone()));
            }
            if(StringUtils.isNotBlank(memberVisitPageResponse.getCarNumber())){
                memberVisitPageResponse.setCarNumber(AesUtil.decode(memberVisitPageResponse.getCarNumber()));
            }
        }
        return memberVisitPageResponseIPage;
    }

    @Override
    public IPage<MemberVisitPageResponse> pageLite(MemberVisitPageRequest params) {
        IPage<MemberVisitPageResponse> memberVisitPageResponseIPage=baseMapper.page(params.createMpPage(), params);
        for (MemberVisitPageResponse memberVisitPageResponse:memberVisitPageResponseIPage.getRecords()) {
            if(StringUtils.isNotBlank(memberVisitPageResponse.getPhone())){
                memberVisitPageResponse.setPhone(AesUtil.decode(memberVisitPageResponse.getPhone()));
            }
            if(StringUtils.isNotBlank(memberVisitPageResponse.getCarNumber())){
                memberVisitPageResponse.setCarNumber(AesUtil.decode(memberVisitPageResponse.getCarNumber()));
            }
        }
        return memberVisitPageResponseIPage;
    }

    @Override
    public MemberVisitDetailResponse detail(Serializable id) {
        MemberVisitDetailResponse memberVisitDetailResponse=baseMapper.selectDetailById(id);
        if(StringUtils.isNotBlank(memberVisitDetailResponse.getPhone())){
            memberVisitDetailResponse.setPhone(AesUtil.decode(memberVisitDetailResponse.getPhone()));
        }
        if(StringUtils.isNotBlank(memberVisitDetailResponse.getCarNumber())){
            memberVisitDetailResponse.setCarNumber(AesUtil.decode(memberVisitDetailResponse.getCarNumber()));
        }
        return memberVisitDetailResponse;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean update(MemberVisitUpdateRequest params) {
        if (params.getId() == null) {
            throw new IllegalParamsException("ID不能为空！");
        }
        if(params.getAuditStats()!=null){
            params.setAuditTime(LocalDateTime.now());
            params.setAuditBy(SecurityContextHolder.getUserId());
        }
        MemberVisit memberVisit = new MemberVisit();
        BeanUtils.copyProperties(params, memberVisit);
        if(StringUtils.isNotBlank(params.getPhone())){
            memberVisit.setPhone(AesUtil.encode(params.getPhone()));
        }
        if(StringUtils.isNotBlank(params.getCarNumber())){
            memberVisit.setCarNumber(AesUtil.encode(params.getCarNumber()));
        }
        if(memberVisit.getVisitOrgId()==null){
            baseMapper.update(memberVisit,Wrappers.<MemberVisit>lambdaUpdate().eq(MemberVisit::getId,memberVisit.getId()).set(MemberVisit::getVisitOrgId,null));
        }
        return this.updateById(memberVisit);
    }

    @Override
    public boolean deleteByIds(List<Long> idList) {
        return this.removeByIds(idList);
    }

}

