package cn.qbs.wa.train.allowance.service.impl;

import cn.hutool.core.util.StrUtil;
import cn.qbs.wa.teach.common.core.constant.Constants;
import cn.qbs.wa.teach.common.core.exception.IllegalParamsException;
import cn.qbs.wa.teach.common.core.exception.ServiceException;
import cn.qbs.wa.teach.common.core.utils.AesUtil;
import cn.qbs.wa.train.allowance.entity.ApplyAuditExpert;
import cn.qbs.wa.train.allowance.entity.Expert;
import cn.qbs.wa.train.allowance.mapper.ApplyAuditExpertMapper;
import cn.qbs.wa.train.allowance.mapper.ExpertMapper;
import cn.qbs.wa.train.allowance.pojo.expert.*;
import cn.qbs.wa.train.allowance.service.ExpertService;
import cn.qbs.wa.train.basic.api.RemoteTrainDictService;
import cn.qbs.wa.train.basic.api.pojo.DTO.dict.DictPageRequestDTO;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.io.Serializable;
import java.util.List;

/**
 * 专家信息(Expert)表服务实现类
 *
 * @author makejava
 * @since 2022-10-31 18:47:29
 */
@Slf4j
@Service("expertService")
public class ExpertServiceImpl extends ServiceImpl<ExpertMapper, Expert> implements ExpertService {

    @Resource
    RemoteTrainDictService remoteTrainDictService;

    @Resource
    ApplyAuditExpertMapper applyAuditExpertMapper;

    @Override
    public boolean add(ExpertAddRequest params) {
        Long count = baseMapper.selectCount(Wrappers.<Expert>lambdaQuery().eq(Expert::getName, params.getName()));
        if(count>Constants.DB_FAIL){
            throw new ServiceException("已有同名专家");
        }
        if (StrUtil.isNotBlank(params.getIdNumber())) {
            params.setIdNumber(AesUtil.encode(params.getIdNumber()));
        }
        if (StrUtil.isNotBlank(params.getPhone())) {
            params.setPhone(AesUtil.encode(params.getPhone()));
        }
        if (StrUtil.isNotBlank(params.getBankAccount())) {
            params.setBankAccount(AesUtil.encode(params.getBankAccount()));
        }
        Expert expert = new Expert();
        BeanUtils.copyProperties(params, expert);
        return this.save(expert);
    }

    @Override
    public IPage<ExpertPageResponse> page(ExpertPageRequest params) {
        if (StrUtil.isNotBlank(params.getPhone())) {
            params.setPhone(AesUtil.encode(params.getPhone()));
        }
        IPage<ExpertPageResponse> expertPageResponseIPage = baseMapper.page(params.createMpPage(), params);
        for (ExpertPageResponse expertPageResponse:expertPageResponseIPage.getRecords()) {
            if (StrUtil.isNotBlank(expertPageResponse.getIdNumber())) {
                String idNumber1=AesUtil.decode(expertPageResponse.getIdNumber());
                String idNumber2=idNumber1.substring(0,3)+"****"+idNumber1.substring(14,idNumber1.length());
                expertPageResponse.setIdNumber(idNumber2);
            }
            if (StrUtil.isNotBlank(expertPageResponse.getPhone())) {
                String phone1=AesUtil.decode(expertPageResponse.getPhone());
                String phone2=phone1.substring(0,3)+"****"+phone1.substring(7,phone1.length());
                expertPageResponse.setPhone(phone2);
            }
            if (StrUtil.isNotBlank(expertPageResponse.getBankAccount())) {
                String bankAccount1=AesUtil.decode(expertPageResponse.getBankAccount());
                String bankAccount2=bankAccount1.substring(0,3)+"****"+bankAccount1.substring(bankAccount1.length()-4,bankAccount1.length());
                expertPageResponse.setBankAccount(bankAccount2);
            }
            DictPageRequestDTO dictPageRequestDTO=new DictPageRequestDTO();
            if(expertPageResponse.getSex()!=null){
                dictPageRequestDTO.setCode("sex");
                dictPageRequestDTO.setDictKey(expertPageResponse.getSex().toString());
                //根据单元字典码和字典值获取字典名称
                String sex=remoteTrainDictService.getDictValue(dictPageRequestDTO).getRemoteData();
                expertPageResponse.setEsex(sex);
            }
            if(expertPageResponse.getTitle()!=null){
                dictPageRequestDTO.setCode("proTitle");
                dictPageRequestDTO.setDictKey(expertPageResponse.getTitle());
                //根据单元字典码和字典值获取字典名称
                String proTitle=remoteTrainDictService.getDictValue(dictPageRequestDTO).getRemoteData();
                expertPageResponse.setTitle(proTitle);
            }
        }
        return expertPageResponseIPage;
    }

    @Override
    public ExpertDetailResponse detail(Serializable id) {
        ExpertDetailResponse expertDetailResponse=baseMapper.selectDetailById(id);
        if (StrUtil.isNotBlank(expertDetailResponse.getIdNumber())) {
            expertDetailResponse.setIdNumber(AesUtil.decode(expertDetailResponse.getIdNumber()));
        }
        if (StrUtil.isNotBlank(expertDetailResponse.getPhone())) {
            expertDetailResponse.setPhone(AesUtil.decode(expertDetailResponse.getPhone()));
        }
        if (StrUtil.isNotBlank(expertDetailResponse.getBankAccount())) {
            expertDetailResponse.setBankAccount(AesUtil.decode(expertDetailResponse.getBankAccount()));
        }
        return expertDetailResponse;
    }

    @Override
    public boolean update(ExpertUpdateRequest params) {
        if (params.getId() == null) {
            throw new IllegalParamsException("ID不能为空！");
        }
        Expert expert=baseMapper.selectById(params.getId());
        Long count = baseMapper.selectCount(Wrappers.<Expert>lambdaQuery().eq(Expert::getName, params.getName()));
        if(!expert.getName().equals(params.getName()) && count>Constants.DB_FAIL){
            throw new ServiceException("已有同名专家");
        }
        if (StrUtil.isNotBlank(params.getIdNumber())) {
            params.setIdNumber(AesUtil.encode(params.getIdNumber()));
        }
        if (StrUtil.isNotBlank(params.getPhone())) {
            params.setPhone(AesUtil.encode(params.getPhone()));
        }
        if (StrUtil.isNotBlank(params.getBankAccount())) {
            params.setBankAccount(AesUtil.encode(params.getBankAccount()));
        }
        BeanUtils.copyProperties(params, expert);
        return this.updateById(expert);
    }

    @Override
    public boolean deleteByIds(List<Long> idList) {
        for (Long id:idList) {
            Long count = applyAuditExpertMapper.selectCount(Wrappers.<ApplyAuditExpert>lambdaQuery().eq(ApplyAuditExpert::getExpertId,id));
            if (count>Constants.DB_FAIL){
                throw new ServiceException("有评审记录专家无法删除");
            }
        }
        return this.removeByIds(idList);

    }

}

