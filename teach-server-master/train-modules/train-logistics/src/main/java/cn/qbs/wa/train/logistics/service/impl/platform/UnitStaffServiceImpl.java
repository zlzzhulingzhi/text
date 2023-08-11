package cn.qbs.wa.train.logistics.service.impl.platform;

import cn.hutool.core.collection.CollUtil;
import cn.qbs.wa.teach.basic.api.RemoteDictService;
import cn.qbs.wa.teach.basic.api.pojo.DTO.dict.DictPageRequestDTO;
import cn.qbs.wa.teach.common.core.constant.Constants;
import cn.qbs.wa.teach.common.core.exception.IllegalParamsException;
import cn.qbs.wa.teach.common.core.exception.ServiceException;
import cn.qbs.wa.teach.common.core.utils.AesUtil;
import cn.qbs.wa.teach.common.core.utils.StringUtils;
import cn.qbs.wa.train.logistics.enums.SexEnum;
import cn.qbs.wa.train.logistics.excel.UnitStaffDataValidate;
import cn.qbs.wa.train.logistics.excel.UnitStaffDataValidateFactory;
import cn.qbs.wa.train.logistics.excel.UnitStaffExcelData;
import cn.qbs.wa.train.logistics.mapper.UnitStaffMapper;
import cn.qbs.wa.train.logistics.entity.UnitStaff;
import cn.qbs.wa.train.logistics.pojo.importUnitStaff.UnitStaffDataParseResult;
import cn.qbs.wa.train.logistics.service.platform.UnitStaffService;
import cn.qbs.wa.train.logistics.pojo.unitstaff.*;
import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.enums.CellDataTypeEnum;
import com.alibaba.excel.event.AnalysisEventListener;
import com.alibaba.excel.metadata.Cell;
import com.alibaba.excel.metadata.data.ReadCellData;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * 单位职员表(UnitStaff)表服务实现类
 *
 * @author makejava
 * @since 2022-09-29 09:04:01
 */
@Slf4j
@Service("unitStaffService")
public class UnitStaffServiceImpl extends ServiceImpl<UnitStaffMapper, UnitStaff> implements UnitStaffService {

    @Autowired
    UnitStaffDataValidateFactory unitStaffDataValidateFactory;

    @Autowired
    RemoteDictService remoteDictService;

    @Override
    public boolean add(UnitStaffAddRequest params) {
        UnitStaff unitStaff = new UnitStaff();
        if (!StringUtils.isBlank(params.getPhone())){
            //判断是否存在手机号
            Long count = this.lambdaQuery().eq(UnitStaff::getPhone, AesUtil.encode(params.getPhone())).count();
            if (count != null && count > 0) {
                throw new ServiceException(String.format("手机号码 %s 已存在", AesUtil.decode(params.getPhone())));
            }
            //手机号账号加密
            params.setPhone(AesUtil.encode(params.getPhone()));
            params.setAccount(params.getPhone());
        }
        //身份证号加密
        if(!StringUtils.isBlank(params.getIdNumber())){
            params.setIdNumber(AesUtil.encode(params.getIdNumber()));
        }
        BeanUtils.copyProperties(params, unitStaff);
        return this.save(unitStaff);
    }

    @Override
    public IPage<UnitStaffPageResponse> page(UnitStaffPageRequest params) {
        IPage<UnitStaffPageResponse> unitStaffPageResponseIPage=baseMapper.page(params.createMpPage(), params);
        List<UnitStaffPageResponse> unitStaffPageResponseList=unitStaffPageResponseIPage.getRecords();
        for (UnitStaffPageResponse unitStaffPageResponse: unitStaffPageResponseList) {
            //手机号账号身份证号解密
            if (!StringUtils.isBlank(unitStaffPageResponse.getPhone())){
                unitStaffPageResponse.setPhone(AesUtil.decode(unitStaffPageResponse.getPhone()));
            }
            if (!StringUtils.isBlank(unitStaffPageResponse.getAccount())){
                unitStaffPageResponse.setAccount(AesUtil.decode(unitStaffPageResponse.getAccount()));
            }
            if (!StringUtils.isBlank(unitStaffPageResponse.getIdNumber())){
                unitStaffPageResponse.setIdNumber(AesUtil.decode(unitStaffPageResponse.getIdNumber()));
            }
            if (!StringUtils.isBlank(unitStaffPageResponse.getEducation())){
                DictPageRequestDTO dictPageRequestDTO=new DictPageRequestDTO();
                dictPageRequestDTO.setCode("education");
                dictPageRequestDTO.setDictKey(unitStaffPageResponse.getEducation());
                //获取学历字典名称
                String education=remoteDictService.getDictValue(dictPageRequestDTO).getRemoteData();
                unitStaffPageResponse.setEducation(education);
            }
        }
        return unitStaffPageResponseIPage;
    }

    @Override
    public UnitStaffDetailResponse detail(Serializable id) {
        return baseMapper.selectDetailById(id);
    }

    @Override
    public boolean update(UnitStaffUpdateRequest params) {
        if (params.getId() == null) {
            throw new IllegalParamsException("ID不能为空！");
        }
        UnitStaff unitStaff = new UnitStaff();
        //判断是否存在手机号
        if(!StringUtils.isBlank(params.getPhone())){
            Long count = this.lambdaQuery().eq(UnitStaff::getPhone, AesUtil.encode(params.getPhone())).count();
            if (count != null && count > 0) {
                throw new ServiceException(String.format("手机号码 %s 已存在", AesUtil.decode(params.getPhone())));
            }
            //手机号账号加密
            params.setPhone(AesUtil.encode(params.getPhone()));
            params.setAccount(params.getPhone());
        }
        //身份证号加密
        if(!StringUtils.isBlank(params.getIdNumber())){
            params.setIdNumber(AesUtil.encode(params.getIdNumber()));
        }
        BeanUtils.copyProperties(params, unitStaff);
        return this.updateById(unitStaff);
    }

    @Override
    public boolean updateBatch(List<UnitStaffUpdateBatchRequest> params) {
        List<UnitStaff> unitStaffList=new ArrayList<>();
        for (UnitStaffUpdateBatchRequest param:params) {
            UnitStaff unitStaff = new UnitStaff();
            if (param.getId() == null) {
                throw new IllegalParamsException("ID不能为空！");
            }
            BeanUtils.copyProperties(param, unitStaff);
            unitStaffList.add(unitStaff);
        }
        return this.updateBatchById(unitStaffList);
    }

    @Override
    public boolean deleteByIds(List<Long> idList) {
        return this.removeByIds(idList);
    }

    @Override
    public List<UnitStaffDataParseResult> importPre(MultipartFile file, Long unitId) {
        ExcelAnalysisListener dataListener = new ExcelAnalysisListener();
        try {
            EasyExcel.read(file.getInputStream(), UnitStaffExcelData.class, dataListener).ignoreEmptyRow(true).sheet(1).doRead();
        } catch (Exception e) {
            throw new ServiceException("上传的导入模板格式不正确！");
        }
        List<UnitStaffExcelData> dataList = dataListener.getDataList();
        if (CollectionUtils.isEmpty(dataList)){
            throw new ServiceException("解析不到数据！");
        }
        List<UnitStaffDataParseResult> resultList = new ArrayList<>();
        List<UnitStaffDataParseResult> errorList = new ArrayList<>();
        for (int i = 0; i < dataList.size(); i++) {
            UnitStaffExcelData unitStaffData = dataList.get(i);
            UnitStaffDataParseResult result = new UnitStaffDataParseResult();
            unitStaffData.setUnitId(unitId);
            BeanUtils.copyProperties(unitStaffData, result);
            UnitStaffDataValidate unitStaffDataValidate = unitStaffDataValidateFactory.getExcelUnitStaffValidate(unitId);
            unitStaffDataValidate.validate(unitStaffData);
            boolean passed = unitStaffDataValidate.passed();
            result.setSex(Optional.ofNullable(SexEnum.getEnum(unitStaffData.getSexName())).orElse(SexEnum.OTHER).getSex());
            result.setAccount(AesUtil.decode(unitStaffData.getPhone()));
            result.setPassed(passed);
            if (!passed) {
                result.setErrorReasons(unitStaffDataValidate.getErrorReasons());
                errorList.add(result);
            }
            resultList.add(result);
        }
        return resultList;
    }

    class ExcelAnalysisListener extends AnalysisEventListener<UnitStaffExcelData> {

        private List<UnitStaffExcelData> dataList = new ArrayList<>();

        /**
         * 每解析一行会回调此方法
         * 通过 AnalysisContext 对象还可以获取当前 sheet，当前行等数据
         *
         * @param
         * @param
         */
        @Override
        public void invoke(UnitStaffExcelData unitStaffExcelData, AnalysisContext analysisContext) {
            Integer rowIndex = analysisContext.readRowHolder().getRowIndex();
            unitStaffExcelData.setRowNum(rowIndex + 1);
            //数据存储到list，供批量处理，或后续自己业务逻辑处理。
            if (StringUtils.hasText(unitStaffExcelData.getPhone())) {
                dataList.add(unitStaffExcelData);
                return;
            }
            //dataList.add(employeeExcelData);
            // 判断是否所有属性都为空
            Map<Integer, Cell> cellMap = analysisContext.readRowHolder().getCellMap();
            cellMap.forEach((k,v) -> {
                ReadCellData cell = (ReadCellData) v;
                if (!CellDataTypeEnum.EMPTY.equals(cell.getType())) {
                    dataList.add(unitStaffExcelData);
                    return;
                }
            });
        }


        /**
         * 整个excel解析结束会执行此方法
         *
         * @param analysisContext
         */
        @Override
        public void doAfterAllAnalysed(AnalysisContext analysisContext) {

        }

        public List<UnitStaffExcelData> getDataList() {
            return dataList;
        }

    }

    @Override
    public  List<UnitStaffAddRequest> batchAdd(List<UnitStaffExcelAddRequest> params) {
        List<UnitStaffAddRequest> unitStaffAddRequests=new ArrayList<>();
        Long unitId = params.get(Constants.DB_FAIL).getUnitId();
        if (CollectionUtils.isNotEmpty(params)) {
            for (UnitStaffExcelAddRequest importUnitStaff : params) {
                if (!importUnitStaff.getPassed()) {
                    continue;
                }
                UnitStaffAddRequest unitStaffAddRequest = new UnitStaffAddRequest();
                BeanUtils.copyProperties(importUnitStaff, unitStaffAddRequest);
                unitStaffAddRequest.setUnitId(unitId);
                unitStaffAddRequest.setAccount(importUnitStaff.getPhone());
                //获取性别
                if (StringUtils.isNotBlank(importUnitStaff.getSexName())) {
                    unitStaffAddRequest.setSex(setSex(importUnitStaff.getSexName()));
                } else {
                    unitStaffAddRequest.setSex(SexEnum.OTHER.getSex());
                }
                if(!StringUtils.isBlank(unitStaffAddRequest.getEducation())){
                    DictPageRequestDTO dictPageRequestDTO=new DictPageRequestDTO();
                    dictPageRequestDTO.setCode("education");
                    dictPageRequestDTO.setDictValue(unitStaffAddRequest.getEducation());
                    //获取学历字典值
                    String education=remoteDictService.getDictKey(dictPageRequestDTO).getRemoteData();
                    unitStaffAddRequest.setEducation(education);
                }
                add(unitStaffAddRequest);
                unitStaffAddRequests.add(unitStaffAddRequest);
            }
        }
        return unitStaffAddRequests;
    }
    private Integer setSex(String sexName) {
        SexEnum sexEnum = SexEnum.getEnum(sexName);
        if (sexEnum == null) {
            return SexEnum.OTHER.getSex();
        }
        return sexEnum.getSex();
    }


}

