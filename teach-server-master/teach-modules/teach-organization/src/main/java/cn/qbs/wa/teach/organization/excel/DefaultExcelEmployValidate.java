package cn.qbs.wa.teach.organization.excel;


import cn.hutool.core.lang.Validator;
import cn.qbs.wa.teach.common.core.context.SecurityContextHolder;
import cn.qbs.wa.teach.common.core.utils.AesUtil;
import cn.qbs.wa.teach.organization.entity.Dept;
import cn.qbs.wa.teach.organization.entity.Employee;
import cn.qbs.wa.teach.organization.mapper.DeptMapper;
import cn.qbs.wa.teach.organization.mapper.EmployeeMapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.apache.commons.lang3.StringUtils;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.HashSet;
import java.util.Set;

@Component
@Scope("prototype")
public class DefaultExcelEmployValidate implements EmployDataValidate {

    protected Set<String> errorReasons = new HashSet<>();

    protected EmployeeExcelData employeeExcelData;

    private String mark;

    //public static final String REGEX_MOBILE = "^((17[0-9])|(14[0-9])|(13[0-9])|(15[^4,\\D])|(18[0-9]))\\d{8}$";
    public static final String REGEX_MOBILE = "^((13[0-9])|(14[01｜(4-9)])|(15[(0-3)|(5-9)])|(16[2567])|(17[0-8])|(18[0-9])|(15[(0-3)|(5-9))]))\\d{8}$";

    @Resource
    private EmployeeMapper employeeMapper;

    @Resource
    private DeptMapper deptMapper;

    /**
     * 验证入口
     * @param
     */
    @Override
    public void validate(EmployeeExcelData employeeExcelData) {
        if (employeeExcelData == null) {
            addErrorReason("信息不能为空！");
            return;
        }
        this.employeeExcelData = employeeExcelData;
        validateStudentName();
        /*validateContent();
        validateScore();*/

        validatePhone();
        validateDeptName();
        //validateIdNumber();
        //validateTag();
        validateOther();
    }

    @Override
    public boolean passed() {
        return CollectionUtils.isEmpty(this.errorReasons);
    }

    @Override
    public void setMark(String mark) {
        this.mark = mark;
    }

    @Override
    public Set<String> getErrorReasons() {
        return errorReasons;
    }

    /**
     * 添加错误原因
     */
    protected void addErrorReason(String errorReason) {
        if (StringUtils.isNotBlank(errorReason)) {
            this.errorReasons.add(msgFormat(errorReason));
        }
    }

    private void validateStudentName() {
        String employeeTypeName = employeeExcelData.getRealName();
        if (StringUtils.isBlank(employeeTypeName)) {
            addErrorReason("姓名不能为空！");
            return;
        }if (employeeTypeName.length()>10){
            addErrorReason("姓名不能超过十个字！");
            return;
        }
        employeeExcelData.setRealName(employeeTypeName.trim());
    }

    private void validatePhone() {
        if (StringUtils.isBlank(employeeExcelData.getPhone())) {
            addErrorReason("手机号码不能为空！");
            return;
        }

        if (!Validator.isMobile(employeeExcelData.getPhone())) {
            addErrorReason("手机号码格式不正确！");
            return;
        }

        encodeInfo(employeeExcelData);
        Long count = employeeMapper.selectCount(new QueryWrapper<Employee>().eq("phone", employeeExcelData.getPhone()).eq("org_id", SecurityContextHolder.getSelectOrgId()));
        if (count > 0) {
            addErrorReason("手机号码已存在！");
            return;
        }
        employeeExcelData.setPhone(employeeExcelData.getPhone().trim());
        //String difficultyName = excelQuestion.getDifficultyName();
        /*Difficulty difficulty = cacheUtil.getFromBucket(RedisKeyUtil.getDifficultyNameKey(difficultyName),
                () -> difficultyService.lambdaQuery().eq(Difficulty::getName, difficultyName).last("limit 1").one()
        );

        if (difficulty == null) {
            addErrorReason("不存在的试题难度[" + excelQuestion.getDifficultyName() + "]！");
            return;
        }*/
    }
    private void encodeInfo(EmployeeExcelData request) {
        if (StringUtils.isNotBlank(request.getPhone())) {
            request.setPhone(AesUtil.encode(request.getPhone()));
        }
    }

    private void validateDeptName() {
        if (!StringUtils.isBlank(employeeExcelData.getDeptName())) {
            String[] split = employeeExcelData.getDeptName().split("/");
            String deptName = split[split.length-1];
            Long count = deptMapper.selectCount(new QueryWrapper<Dept>().eq("dept_name", deptName));
            if (count == 0) {
                employeeExcelData.setDeptName("默认");
            }else {
                employeeExcelData.setDeptName(deptName);
            }
        }
        //employeeExcelData.setDeptName(employeeExcelData.getDeptName().trim());
    }
    /*private void validateTag() {
        if(StringUtils.isNotBlank(studentExcelData.getGroupName())){
            Student groupInfo = studentMapper.selectOne(new QueryWrapper<Group>().eq("group_name", studentExcelData.getGroupName()));
            if (cn.qbs.wa.teach.common.core.utils.StringUtils.isNull(groupInfo)) {
                addErrorReason("标签不存在！");
                return;
            }else if(cn.qbs.wa.teach.common.core.utils.StringUtils.isNotNull(groupInfo) && groupInfo.getEnabled){}
        }

        studentExcelData.setIdNumber(studentExcelData.getIdNumber().trim());
    }*/

   /* protected void validateContent() {
        if (StringUtils.isBlank(excelQuestion.getContent())) {
            addErrorReason("试题题干不能为空！");
            return;
        }
    }

    protected void validateScore() {
        if (excelQuestion.getScore() == null) {
            addErrorReason("试题分数不能为空！");
            return;
        }
        if (excelQuestion.getScore().compareTo(BigDecimal.ZERO) < 1) {
            addErrorReason("试题分数不能小于等于0！");
            return;
        }
    }*/

    protected String msgFormat(String msg) {
        if (StringUtils.isBlank(this.mark)) {
            return msg;
        }
        return String.format("【%s】%s", this.mark, msg);
    }

    /**
     * 其它验证，由子类复写
     */
    protected void validateOther() {
    }

}

