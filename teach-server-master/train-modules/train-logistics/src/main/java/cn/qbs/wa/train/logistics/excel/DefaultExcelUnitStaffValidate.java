package cn.qbs.wa.train.logistics.excel;


import cn.hutool.core.lang.Validator;
import cn.qbs.wa.teach.common.core.utils.AesUtil;
import cn.qbs.wa.train.logistics.entity.UnitStaff;
import cn.qbs.wa.train.logistics.mapper.UnitStaffMapper;
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
public class DefaultExcelUnitStaffValidate implements UnitStaffDataValidate {

    protected Set<String> errorReasons = new HashSet<>();

    protected UnitStaffExcelData unitStaffExcelData;

    private String mark;

    //public static final String REGEX_MOBILE = "^((17[0-9])|(14[0-9])|(13[0-9])|(15[^4,\\D])|(18[0-9]))\\d{8}$";
    public static final String REGEX_MOBILE = "^((13[0-9])|(14[01｜(4-9)])|(15[(0-3)|(5-9)])|(16[2567])|(17[0-8])|(18[0-9])|(15[(0-3)|(5-9))]))\\d{8}$";

    @Resource
    private UnitStaffMapper unitStaffMapper;


    /**
     * 验证入口
     * @param
     */
    @Override
    public void validate(UnitStaffExcelData unitStaffExcelData) {
        if (unitStaffExcelData == null) {
            addErrorReason("信息不能为空！");
            return;
        }
        this.unitStaffExcelData = unitStaffExcelData;
        validateUnitStaffName();
        validatePhone();
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

    private void validateUnitStaffName() {
        String unitStaffTypeName = unitStaffExcelData.getRealName();
        if (StringUtils.isBlank(unitStaffTypeName)) {
            addErrorReason("姓名不能为空！");
            return;
        }if (unitStaffTypeName.length()>10){
            addErrorReason("姓名不能超过十个字！");
            return;
        }
        unitStaffExcelData.setRealName(unitStaffTypeName.trim());
    }

    private void validatePhone() {
        if (StringUtils.isBlank(unitStaffExcelData.getPhone())) {
            addErrorReason("手机号码不能为空！");
            return;
        }

        if (!Validator.isMobile(unitStaffExcelData.getPhone())) {
            addErrorReason("手机号码格式不正确！");
            return;
        }

        encodeInfo(unitStaffExcelData);
        Long count = unitStaffMapper.selectCount(new QueryWrapper<UnitStaff>().eq("phone", unitStaffExcelData.getPhone()));
        if (count > 0) {
            addErrorReason("手机号码已存在！");
            return;
        }
        unitStaffExcelData.setPhone(unitStaffExcelData.getPhone().trim());
    }

    private void encodeInfo(UnitStaffExcelData request) {
        if (StringUtils.isNotBlank(request.getPhone())) {
            request.setPhone(AesUtil.encode(request.getPhone()));
            request.setAccount(AesUtil.encode(request.getPhone()));
        }
        if (StringUtils.isNotBlank(request.getIdNumber())) {
            request.setIdNumber(AesUtil.encode(request.getIdNumber()));
        }
    }

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

