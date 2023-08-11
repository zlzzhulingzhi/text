package cn.qbs.wa.train.logistics.excel;


import cn.hutool.core.lang.Validator;
import cn.qbs.wa.teach.common.core.utils.AesUtil;
import cn.qbs.wa.train.logistics.entity.Groups;
import cn.qbs.wa.train.logistics.entity.Student;
import cn.qbs.wa.train.logistics.enums.EnableEnum;
import cn.qbs.wa.train.logistics.mapper.GroupsMapper;
import cn.qbs.wa.train.logistics.mapper.StudentMapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.apache.commons.lang3.StringUtils;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.HashSet;
import java.util.Set;
import java.util.regex.Pattern;

@Component
@Scope("prototype")
public class DefaultExcelStudentValidate implements StudentDataValidate {

    protected Set<String> errorReasons = new HashSet<>();

    protected StudentExcelData studentExcelData;

    private String mark;

    //public static final String REGEX_MOBILE = "^((17[0-9])|(14[0-9])|(13[0-9])|(15[^4,\\D])|(18[0-9]))\\d{8}$";
    public static final String REGEX_MOBILE = "^((13[0-9])|(14[01｜(4-9)])|(15[(0-3)|(5-9)])|(16[2567])|(17[0-8])|(18[0-9])|(15[(0-3)|(5-9))]))\\d{8}$";

    @Resource
    private StudentMapper studentMapper;

    @Resource
    private GroupsMapper groupsMapper;


    /**
     * 验证入口
     *
     * @param
     */
    @Override
    public void validate(StudentExcelData excelQuestion) {
        if (excelQuestion == null) {
            addErrorReason("信息不能为空！");
            return;
        }
        this.studentExcelData = excelQuestion;
        validateStudentName();
        /*validateContent();
        validateScore();*/

        validatePhone();
        //validateGroup();
        validateGroups();
        validateIdNumber();
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

    private void validateIdNumber() {
        if (StringUtils.isBlank(studentExcelData.getIdNumber())){
            return;
        }
        String idNumber = studentExcelData.getIdNumber();
        String str = "[1-9]{2}[0-9]{4}(19|20)[0-9]{2}"
                + "((0[1-9]{1})|(1[1-2]{1}))((0[1-9]{1})|([1-2]{1}[0-9]{1}|(3[0-1]{1})))"
                + "[0-9]{3}[0-9x]{1}";
        Pattern pattern = Pattern.compile(str);
        if (!pattern.matcher(idNumber).matches()){
            addErrorReason("身份证号码错误！");
        }
    }

    private void validateStudentName() {
        String questionTypeName = studentExcelData.getRealName();
        if (StringUtils.isBlank(questionTypeName)) {
            addErrorReason("姓名不能为空！");
            return;
        }if (questionTypeName.length()>10){
            addErrorReason("姓名不能超过十个字！");
            return;
        }
        studentExcelData.setRealName(questionTypeName.trim());
       /* QuestionTypeEnum questionTypeEnum = QuestionTypeEnum.fromName(questionTypeName);
        if (questionTypeEnum == null) {
            addErrorReason("不支持的试题题型【" + questionTypeName + "】！");
            return;
        }*/
    }

    private void validatePhone() {
        if (StringUtils.isBlank(studentExcelData.getPhone())) {
            addErrorReason("手机号码不能为空！");
            return;
        }
        if (!Validator.isMobile(studentExcelData.getPhone())) {
            addErrorReason("手机号码格式不正确！");
            return;
        }
        encodeInfo(studentExcelData);
        Long count = studentMapper.selectCount(new QueryWrapper<Student>().eq("phone", studentExcelData.getPhone()));
        if (count > 0) {
            addErrorReason("手机号码已存在！");
            return;
        }
        studentExcelData.setPhone(studentExcelData.getPhone().trim());
        //String difficultyName = excelQuestion.getDifficultyName();
        /*Difficulty difficulty = cacheUtil.getFromBucket(RedisKeyUtil.getDifficultyNameKey(difficultyName),
                () -> difficultyService.lambdaQuery().eq(Difficulty::getName, difficultyName).last("limit 1").one()
        );

        if (difficulty == null) {
            addErrorReason("不存在的试题难度[" + excelQuestion.getDifficultyName() + "]！");
            return;
        }*/
    }

    private void validateGroup() {
        if(StringUtils.isNotBlank(studentExcelData.getGroupName())) {
            Groups groups = groupsMapper.selectOne(new QueryWrapper<Groups>().eq("group_name", studentExcelData.getGroupName()));
            if (cn.qbs.wa.teach.common.core.utils.StringUtils.isNull(groups)) {
                addErrorReason("标签不存在！");
                return;
            } else if (EnableEnum.FORBIDDEN.getCode().toString().equals(groups.getEnabled().toString())) {
                addErrorReason("标签已禁用！");
                return;
            }
            studentExcelData.setGroupName(studentExcelData.getGroupName().trim());
        }

    }

    private void validateGroups() {
        if(StringUtils.isNotBlank(studentExcelData.getGroupName())) {
            Groups groups = groupsMapper.selectOne(new QueryWrapper<Groups>().eq("group_name", studentExcelData.getGroupName()));
            if (groups!=null){
                studentExcelData.setGroupName(studentExcelData.getGroupName().trim());
            }
        }
    }

    private void encodeInfo(StudentExcelData request) {
        if (StringUtils.isNotBlank(request.getPhone())) {
            request.setPhone(AesUtil.encode(request.getPhone()));

        }
    }

    /*private void validateIdNumber() {
        if (!StringUtils.isBlank(studentExcelData.getDeptName())) {
            Long count = studentMapper.selectCount(new QueryWrapper<Employee>().eq("dept_name", studentExcelData.getDeptName()));
            if (count > 0) {
                addErrorReason("部门填写不正确！");
                return;
            }
        }

        studentExcelData.setDeptName(studentExcelData.getDeptName().trim());
    }*/
    /*private void validateTag() {
        if(StringUtils.isNotBlank(studentExcelData.getGroupName())){
            Student groupInfo = studentMapper.selectOne(new QueryWrapper<Group>().eq("group_name", studentExcelData.getGroupName()));
            if (cn.qbs.wa.train.common.core.utils.StringUtils.isNull(groupInfo)) {
                addErrorReason("标签不存在！");
                return;
            }else if(cn.qbs.wa.train.common.core.utils.StringUtils.isNotNull(groupInfo) && groupInfo.getEnabled){}
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

