package cn.qbs.wa.train.logistics.pojo.dormitoryattach;


import lombok.Data;
import io.swagger.annotations.ApiModelProperty;

/**
 * 宿舍附件(DormitoryAttach)更新宿舍附件参数
 *
 * @author makejava
 * @since 2022-10-13 09:36:15
 */
@Data
public class DormitoryAttachUpdateRequest {

    @ApiModelProperty(value = "主键")
    private Long id;

    @ApiModelProperty(value = "宿舍ID")
    private Long dormitoryId;

    @ApiModelProperty(value = "文件类型(pic-图片 doc-文件)")
    private String fileType;

    @ApiModelProperty(value = "文件地址")
    private String[] fileUrl;

    @ApiModelProperty(value = "排序序号")
    private Integer sort;

}

