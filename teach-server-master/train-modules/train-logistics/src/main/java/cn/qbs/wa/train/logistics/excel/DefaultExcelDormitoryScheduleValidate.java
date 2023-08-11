package cn.qbs.wa.train.logistics.excel;


import cn.qbs.wa.train.logistics.mapper.DormitoryScheduleMapper;
import org.apache.commons.lang3.StringUtils;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.HashSet;
import java.util.Set;

@Component
@Scope("prototype")
public class DefaultExcelDormitoryScheduleValidate implements DormitoryScheduleDataValidate {

    protected Set<String> errorReasons = new HashSet<>();

    protected DormitoryScheduleExcelData dormitoryScheduleExcelData;

    private String mark;

    @Resource
    private DormitoryScheduleMapper dormitoryScheduleMapper;


    /**
     * 验证入口
     * @param
     */
    @Override
    public void validate(DormitoryScheduleExcelData dormitoryScheduleExcelData) {
        if (dormitoryScheduleExcelData == null) {
            addErrorReason("信息不能为空！");
            return;
        }
        this.dormitoryScheduleExcelData = dormitoryScheduleExcelData;
        validateOrgName();
        validateBuilding();
        validateFloor();
        validateRoomNo();
        validateUseDate();
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

    private void validateOrgName() {
        String orgName = dormitoryScheduleExcelData.getOrgName();
        if (StringUtils.isBlank(orgName)) {
            addErrorReason("机构名称不能为空！");
            return;
        }
        dormitoryScheduleExcelData.setOrgName(orgName.trim());
    }
    private void validateBuilding() {
        String building = dormitoryScheduleExcelData.getBuilding();
        if (StringUtils.isBlank(building)) {
            addErrorReason("宿舍单元不能为空！");
            return;
        }
        dormitoryScheduleExcelData.setOrgName(building.trim());
    }
    private void validateFloor() {
        String floor = dormitoryScheduleExcelData.getFloor();
        if (StringUtils.isBlank(floor)) {
            addErrorReason("楼层不能为空！");
            return;
        }
        dormitoryScheduleExcelData.setOrgName(floor.trim());
    }
    private void validateRoomNo() {
        String roomNo = dormitoryScheduleExcelData.getRoomNo();
        if (StringUtils.isBlank(roomNo)) {
            addErrorReason("房号不能为空！");
            return;
        }
        dormitoryScheduleExcelData.setRoomNo(roomNo.trim());
    }

    private void validateUseDate() {
        String useDate = dormitoryScheduleExcelData.getUseDate();
        if (StringUtils.isBlank(useDate)) {
            addErrorReason("使用日期不能为空！");
            return;
        }
        dormitoryScheduleExcelData.setUseDate(useDate.trim());
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

