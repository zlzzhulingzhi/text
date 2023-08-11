package cn.qbs.wa.teach.organization.service.impl;

import cn.hutool.core.lang.Validator;
import cn.hutool.core.util.StrUtil;
import cn.qbs.wa.teach.basic.api.RemoteUserService;
import cn.qbs.wa.teach.basic.api.pojo.DTO.user.UserInfoDTO;
import cn.qbs.wa.teach.basic.api.pojo.DTO.user.UserOneResultDTO;
import cn.qbs.wa.teach.common.core.domain.R;
import cn.qbs.wa.teach.common.core.exception.IllegalParamsException;
import cn.qbs.wa.teach.common.core.exception.ServiceException;
import cn.qbs.wa.teach.common.core.utils.AesUtil;
import cn.qbs.wa.teach.common.core.utils.StringUtils;
import cn.qbs.wa.teach.organization.entity.Dept;
import cn.qbs.wa.teach.organization.entity.Employee;
import cn.qbs.wa.teach.organization.entity.ImportEmployee;
import cn.qbs.wa.teach.organization.entity.Organization;
import cn.qbs.wa.teach.organization.enums.SexEnum;
import cn.qbs.wa.teach.organization.excel.*;
import cn.qbs.wa.teach.organization.mapper.EmployeeMapper;
import cn.qbs.wa.teach.organization.mapper.ImportEmployeeMapper;
import cn.qbs.wa.teach.organization.pojo.employee.EmployeeDownloadRequest;
import cn.qbs.wa.teach.organization.pojo.employee.EmployeeListRequest;
import cn.qbs.wa.teach.organization.pojo.employee.EmployeePageResponse;
import cn.qbs.wa.teach.organization.pojo.importemployee.*;
import cn.qbs.wa.teach.organization.service.DeptService;
import cn.qbs.wa.teach.organization.service.ImportEmployeeService;
import cn.qbs.wa.teach.organization.service.OrganizationService;
import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.enums.CellDataTypeEnum;
import com.alibaba.excel.event.AnalysisEventListener;
import com.alibaba.excel.metadata.Cell;
import com.alibaba.excel.metadata.data.ReadCellData;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * 导入职工表(ImportEmployee)表服务实现类
 *
 * @author makejava
 * @since 2021-11-16 11:29:43
 */
@Slf4j
@Service("importEmployeeService")
public class ImportEmployeeServiceImpl extends ServiceImpl<ImportEmployeeMapper, ImportEmployee> implements ImportEmployeeService {


    @Autowired
    DeptService deptService;

    @Autowired
    RemoteUserService remoteUserService;

    @Autowired
    OrganizationService organizationService;

    @Autowired
    EmployeeMapper employeeMapper;

    @Autowired
    EmployDataValidateFactory employDataValidateFactory;

    @Override
    public boolean add(ImportEmployeeAddRequest params) {
        ImportEmployee importEmployee = new ImportEmployee();
        BeanUtils.copyProperties(params, importEmployee);
        return this.save(importEmployee);
    }

    @Override
    public IPage<ImportEmployeePageResponse> page(ImportEmployeePageRequest params) {
        return baseMapper.page(params.createMpPage(), params);
    }

    @Override
    public ImportEmployeeDetailResponse detail(Serializable id) {
        return baseMapper.selectDetailById(id);
    }

    @Override
    public boolean update(ImportEmployeeUpdateRequest params) {
        if (params.getId() == null) {
            throw new IllegalParamsException("ID不能为空！");
        }
        ImportEmployee importEmployee = new ImportEmployee();
        BeanUtils.copyProperties(params, importEmployee);
        return this.updateById(importEmployee);
    }

    @Override
    public boolean deleteByIds(List<Long> idList) {
        return this.removeByIds(idList);
    }

    @Override
    public void saveImportData(List<EmployeeData> importEmployeeList, Long orgId, String eventId, Long deptId) {
        if (CollectionUtils.isNotEmpty(importEmployeeList)) {
            Organization organization = organizationService.getById(orgId);
            if (organization == null) {
                throw new ServiceException("无法找到该机构");
            }
            Dept dept = deptService.getById(deptId);
            if (dept == null) {
                throw new ServiceException("无法找到该部门");
            }
            List<ImportEmployee> importEmployees = new ArrayList<>();
            for (EmployeeData employeeData : importEmployeeList) {
                List<String> remarks = checkImportUserInfo(employeeData);
                ImportEmployee importEmployee = new ImportEmployee();
                BeanUtils.copyProperties(employeeData, importEmployee);
                checkUserExist(employeeData, orgId, remarks);

                importEmployee.setSex(setSex(employeeData.getSexName()));
                importEmployee.setDeptId(dept.getId());
                importEmployee.setDeptName(dept.getDeptName());
                importEmployee.setOrgId(orgId);
                importEmployee.setOrgName(organization.getName());
                importEmployee.setEventId(eventId);
                importEmployee.setRemark(remarks.stream().collect(Collectors.joining(",")));
                importEmployees.add(importEmployee);
            }
            saveBatch(importEmployees);
        }
    }

    @Override
    public List<EmployeeDownloadData> listDownloadData(EmployeeDownloadRequest request) {
        List<EmployeeDownloadData> employeeDownloadDataList = new ArrayList<>();
        EmployeeListRequest employeeListRequest = new EmployeeListRequest();
        BeanUtils.copyProperties(request, employeeListRequest);
        List<EmployeePageResponse> listEmployee = employeeMapper.listEmployeeWithRole(employeeListRequest);
        if (CollectionUtils.isNotEmpty(listEmployee)) {
            for (EmployeePageResponse employeePageResponse : listEmployee) {
                EmployeeDownloadData employeeDownloadData = new EmployeeDownloadData();
                BeanUtils.copyProperties(employeePageResponse, employeeDownloadData);
                employeeDownloadData.setSexName(SexEnum.getEnum(employeePageResponse.getSex()).name());
                employeeDownloadData.setPhone(AesUtil.decode(employeeDownloadData.getPhone()));
                employeeDownloadData.setIdNumber(AesUtil.decode(employeeDownloadData.getIdNumber()));
                employeeDownloadDataList.add(employeeDownloadData);
            }
        }
        return employeeDownloadDataList;
    }

    private void checkUserExist(EmployeeData employeeData, Long orgId, List<String> remarks) {
        UserInfoDTO userInfoDTO = new UserInfoDTO();
        userInfoDTO.setAccount(employeeData.getPhone());
        R<UserOneResultDTO> checkResult = remoteUserService.checkExistUser(userInfoDTO);
        if (checkResult.getRemoteData() != null) {
            Long userId = checkResult.getRemoteData().getId();
            Long count = employeeMapper.selectCount(Wrappers.<Employee>lambdaQuery().eq(Employee::getOrgId, orgId).eq(Employee::getUserId, userId));
            if (count != null && count > 0) {
                remarks.add("已存在相同手机号码的用户");
            }
        }
    }

    private Integer setSex(String sexName) {
        SexEnum sexEnum = SexEnum.getEnum(sexName);
        if (sexEnum == null) {
            return SexEnum.OTHER.getSex();
        }
        return sexEnum.getSex();
    }


    private List<String> checkImportUserInfo(EmployeeData employeeData) {
        List<String> remarks = new ArrayList<>();
        //String email = employeeData.getEmail();
        //if (StrUtil.isBlank(email)) {
        //    remarks.add("邮箱不能为空");
        //}
        //String idNumber = employeeData.getIdNumber();
        //if (StrUtil.isBlank(idNumber)) {
        //    remarks.add("身份证不能为空");
        //}
        String phone = employeeData.getPhone();
        if (StrUtil.isBlank(phone) || !Validator.isMobile(phone)) {
            remarks.add("号码不能为空或者非法手机号码");
        }
        String realName = employeeData.getRealName();
        if (StrUtil.isBlank(realName)) {
            remarks.add("用户不能为空");
        }
//        String deptName = employeeData.getDeptName();
//        if (StrUtil.isBlank(deptName)) {
//            remarks.add("部门不能为空");
//        }
        String sexName = employeeData.getSexName();
        if (StrUtil.isBlank(sexName)) {
            remarks.add("性别不能为空");
        }
        //String passWord = employeeData.getPassword();
        //if (StrUtil.isBlank(passWord)) {
        //    remarks.add("密码不能为空");
        //}
        return remarks;
    }

    @Override
    public List<EmployeeDataParseResult> importPre(MultipartFile file,Long orgId) {
        ExcelAnalysisListener dataListener = new ExcelAnalysisListener();
        try {
            EasyExcel.read(file.getInputStream(), EmployeeExcelData.class, dataListener).ignoreEmptyRow(true).sheet(1).doRead();
        } catch (Exception e) {
            throw new ServiceException("上传的导入模板格式不正确！");
        }
        List<EmployeeExcelData> dataList = dataListener.getDataList();
        if (CollectionUtils.isEmpty(dataList)){
            throw new ServiceException("解析不到数据！");
        }
        List<EmployeeDataParseResult> resultList = new ArrayList<>();
        List<EmployeeDataParseResult> errorList = new ArrayList<>();
        for (int i = 0; i < dataList.size(); i++) {
            EmployeeExcelData employeeData = dataList.get(i);
            EmployeeDataParseResult result = new EmployeeDataParseResult();
            employeeData.setOrgId(orgId);
            BeanUtils.copyProperties(employeeData, result);
            EmployDataValidate employDataValidate = employDataValidateFactory.getExcelEmployValidate(orgId);
            //String mark = String.format("第%d行", excelQuestion.getRowIndex());
            //excelQuestionValidate.setMark(mark);
            employDataValidate.validate(employeeData);
            boolean passed = employDataValidate.passed();
            result.setSex(Optional.ofNullable(SexEnum.getEnum(employeeData.getSexName())).orElse(SexEnum.OTHER).getSex());
            result.setPassed(passed);
            if (!passed) {
                result.setErrorReasons(employDataValidate.getErrorReasons());
                errorList.add(result);
            }
            resultList.add(result);
        }

//        if (StringUtils.isNotEmpty(errorList)) {
//            StringBuilder errorMsg = new StringBuilder();
//            for (int j = 0; j < errorList.size(); j++) {
//                EmployeeDataParseResult parseResult =  errorList.get(j);
//                if (j > 0) {
//                    errorMsg.append("<br/>");
//                }
//                errorMsg.append("【第").append(parseResult.getRowNum()).append("行】");
//                for (String errorReason : parseResult.getErrorReasons()) {
//                    errorMsg.append(errorReason);
//                }
//            }
//
//            throw new IllegalParamsException(errorMsg.toString());
//        }
        return resultList;
    }
    class ExcelAnalysisListener extends AnalysisEventListener<EmployeeExcelData> {

        private List<EmployeeExcelData> dataList = new ArrayList<>();

        /**
         * 每解析一行会回调此方法
         * 通过 AnalysisContext 对象还可以获取当前 sheet，当前行等数据
         *
         * @param
         * @param
         */
        @Override
        public void invoke(EmployeeExcelData employeeExcelData, AnalysisContext analysisContext) {
            Integer rowIndex = analysisContext.readRowHolder().getRowIndex();
            employeeExcelData.setRowNum(rowIndex + 1);
            //数据存储到list，供批量处理，或后续自己业务逻辑处理。
            if (StringUtils.hasText(employeeExcelData.getPhone())) {
                dataList.add(employeeExcelData);
                return;
            }
            //dataList.add(employeeExcelData);
            // 判断是否所有属性都为空
            Map<Integer, Cell> cellMap = analysisContext.readRowHolder().getCellMap();
            cellMap.forEach((k,v) -> {
                ReadCellData cell = (ReadCellData) v;
                if (!CellDataTypeEnum.EMPTY.equals(cell.getType())) {
                    dataList.add(employeeExcelData);
                    return;
                }
            });
            /*if (!ObjUtil.checkNullAndEmpty(questionExcelVo)) {
                dataList.add(questionExcelVo);
            }*/
        }


        /**
         * 整个excel解析结束会执行此方法
         *
         * @param analysisContext
         */
        @Override
        public void doAfterAllAnalysed(AnalysisContext analysisContext) {
            //log.info("所有数据解析完成！");
            // 这里也要保存数据，确保最后遗留的数据也存储到数据库
//            saveData();
        }

        public List<EmployeeExcelData> getDataList() {
            return dataList;
        }

    }
}

