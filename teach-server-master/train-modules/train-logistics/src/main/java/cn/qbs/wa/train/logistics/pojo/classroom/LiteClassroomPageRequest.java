package cn.qbs.wa.train.logistics.pojo.classroom;

import cn.qbs.wa.teach.domain.BasePageRequest;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDate;

@Data
@EqualsAndHashCode(callSuper = true)
public class LiteClassroomPageRequest extends BasePageRequest {

    @ApiModelProperty(value = "使用日期")
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private LocalDate useDate;

    @ApiModelProperty(value = "单元(字典值)")
    private String building;

    @ApiModelProperty(value = "楼层(字典值)")
    private String floor;

    @ApiModelProperty(value = "教室类别(字典值)")
    private String roomType;

}
