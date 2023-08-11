package cn.qbs.wa.train.logistics.pojo.matter;

import cn.qbs.wa.teach.common.security.annotation.EncodeContent;
import cn.qbs.wa.train.logistics.entity.MatterReportAttach;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

/**
 * @author makejava
 * @since 2022-11-24 15:31:34
 */
@Data
public class LiteMatterReportResponse {

    @ApiModelProperty(value = "主键")
    private Long id;

    @ApiModelProperty(value = "机构ID")
    private Long orgId;

    @ApiModelProperty(value = "机构名称")
    private String orgName;

    @ApiModelProperty(value = "类别(1-开课、2-维修、3-更换、4-结课)")
    private Integer category;

    @ApiModelProperty(value = "报事说明")
    private String description;

    @ApiModelProperty(value = "联系人")
    private String contactPerson;

    @EncodeContent
    @ApiModelProperty(value = "联系电话")
    private String contactNumber;

    @ApiModelProperty(value = "关联教室")
    private Long classroomId;

    @ApiModelProperty(value = "宿舍单元(字典值)")
    private String building;

    @ApiModelProperty(value = "楼层(字典值)")
    private String floor;

    @ApiModelProperty(value = "教室编号")
    private String roomNo;

    @ApiModelProperty(value = "教室类别(字典值)")
    private String roomType;

    @ApiModelProperty(value = "上报时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private LocalDateTime createTime;

    @ApiModelProperty(value = "报事图片")
    private List<MatterReportAttach> attachList;

}
