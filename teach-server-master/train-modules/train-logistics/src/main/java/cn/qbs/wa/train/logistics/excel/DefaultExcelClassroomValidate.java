package cn.qbs.wa.train.logistics.excel;


import cn.qbs.wa.train.basic.api.RemoteTrainDictService;
import cn.qbs.wa.train.basic.api.pojo.DTO.dict.DictPageRequestDTO;
import cn.qbs.wa.train.logistics.enums.BuldingFloorRoomTypeEnum;
import cn.qbs.wa.train.logistics.mapper.ClassroomMapper;
import cn.qbs.wa.train.logistics.mapper.DormitoryScheduleMapper;
import org.apache.commons.lang3.StringUtils;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;

@Component
@Scope("prototype")
public class DefaultExcelClassroomValidate implements ClassroomDataValidate {

    protected Set<String> errorReasons = new HashSet<>();

    protected ClassroomExcelData classroomExcelData;

    private String mark;


    /**
     * 验证入口
     * @param
     */
    @Override
    public void validate(ClassroomExcelData classroomExcelData) {
        if (classroomExcelData == null) {
            addErrorReason("信息不能为空！");
            return;
        }
        this.classroomExcelData = classroomExcelData;
        validateBuilding();
        validateFloor();
        validateRoomNo();
        validateArea();
        validateSeats();
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

    private void validateBuilding() {
        String building = classroomExcelData.getBuilding();
        if (StringUtils.isBlank(building)) {
            addErrorReason("教室单元不能为空！(没填或者字典里没值)");
            return;
        }
        classroomExcelData.setBuilding(building.trim());

    }
    private void validateFloor() {
        String floor = classroomExcelData.getFloor();
        if (StringUtils.isBlank(floor)) {
            addErrorReason("楼层不能为空！(没填或者字典里没值)");
            return;
        }
        classroomExcelData.setFloor(floor.trim());
    }
    private void validateRoomNo() {
        String roomNo = classroomExcelData.getRoomNo();
        if (StringUtils.isBlank(roomNo)) {
            addErrorReason("房号不能为空！(没填或者字典里没值)");
            return;
        }
        classroomExcelData.setRoomNo(roomNo.trim());
    }

    private void validateArea() {
        BigDecimal area = classroomExcelData.getArea();
        if (area==null) {
            addErrorReason("面积不能为空！");
            return;
        }
        classroomExcelData.setArea(area);
    }

    private void validateSeats() {
        Integer seats = classroomExcelData.getSeats();
        if (seats==null) {
            addErrorReason("座位不能为空！");
            return;
        }
        classroomExcelData.setSeats(seats);
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

