package cn.qbs.wa.train.logistics.pojo.matter;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * @author makejava
 * @since 2022-11-24 15:31:34
 */
@Data
public class MatterReportSaveRequest {

    @ApiModelProperty(value = "类别(1-开课、2-维修、3-更换、4-结课)")
    private Integer category;

    @ApiModelProperty(value = "报事说明")
    private String description;

    @ApiModelProperty(value = "联系人")
    private String contactPerson;

    @ApiModelProperty(value = "联系电话")
    private String contactNumber;

    @ApiModelProperty(value = "关联教室")
    private Long classroomId;

    @ApiModelProperty(value = "报事图片")
    private List<String> attachList;

}
