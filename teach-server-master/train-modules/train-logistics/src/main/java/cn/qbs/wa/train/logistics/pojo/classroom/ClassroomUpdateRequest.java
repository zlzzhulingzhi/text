package cn.qbs.wa.train.logistics.pojo.classroom;


import lombok.Data;
import io.swagger.annotations.ApiModelProperty;

import java.math.BigDecimal;
import java.util.List;

/**
 * 教室表(Classroom)更新教室表参数
 *
 * @author makejava
 * @since 2022-10-11 17:30:11
 */
@Data
public class ClassroomUpdateRequest {

    @ApiModelProperty(value = "主键")
    private Long id;

    @ApiModelProperty(value = "宿舍单元(字典值)")
    private String building;

    @ApiModelProperty(value = "楼层(字典值)")
    private String floor;

    @ApiModelProperty(value = "教室编号")
    private String roomNo;

    @ApiModelProperty(value = "教室类别(字典值)")
    private String roomType;

    @ApiModelProperty(value = "座位数")
    private Integer seats;

    @ApiModelProperty(value = "面积")
    private BigDecimal area;

    @ApiModelProperty(value = "面积单位")
    private String areaUnit;

    @ApiModelProperty(value = "备注")
    private String remark;

    @ApiModelProperty(value = "租用模式(1-短时申请 2-长期使用)")
    private Integer mode;

    @ApiModelProperty(value = "启用状态(0-不可用  1-可用)")
    private Integer enabled;

    @ApiModelProperty(value = "删除状态 0-正常 1-已删除")
    private Integer deleted;

    @ApiModelProperty(value = "文件类型(pic-图片 doc-文件)")
    private String fileType;

    @ApiModelProperty(value = "文件地址")
    private String[] fileUrl;

    @ApiModelProperty(value = "设备ID列表")
    private List<Long> sceneDeviceId;
}

