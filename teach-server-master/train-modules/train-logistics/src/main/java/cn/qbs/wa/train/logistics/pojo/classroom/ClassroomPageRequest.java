package cn.qbs.wa.train.logistics.pojo.classroom;


import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import io.swagger.annotations.ApiModelProperty;
import cn.qbs.wa.teach.domain.BasePageRequest;

import java.time.LocalDate;

/**
 * 教室表(Classroom)分页查询参数
 *
 * @author makejava
 * @since 2022-10-11 17:30:11
 */
@Data
public class ClassroomPageRequest extends BasePageRequest {

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

    @ApiModelProperty(value = "备注")
    private String remark;

    @ApiModelProperty(value = "租用模式(1-短时申请 2-长期使用)")
    private Integer mode;

    @ApiModelProperty(value = "启用状态(0-不可用  1-可用)")
    private Integer enabled;

    @ApiModelProperty(value = "删除状态 0-正常 1-已删除")
    private Integer deleted;

    @ApiModelProperty(value = "使用状态(0-未使用，1-已使用)")
    private Integer useState;

    @ApiModelProperty(value = "使用日期")
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private LocalDate startDate;

    @ApiModelProperty(value = "使用日期")
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private LocalDate endDate;

    @ApiModelProperty(value = "机构ID")
    private Long orgId;

}

