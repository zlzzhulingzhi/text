package cn.qbs.wa.train.logistics.pojo.dormitoryattach;


import lombok.Data;
import io.swagger.annotations.ApiModelProperty;

/**
 * 宿舍附件(DormitoryAttach)创建宿舍附件参数
 *
 * @author makejava
 * @since 2022-10-13 09:36:14
 */
@Data
public class DormitoryAttachAddRequest {

    @ApiModelProperty(value = "宿舍ID")
    private Long dormitoryId;

    @ApiModelProperty(value = "文件类型(pic-图片 doc-文件)")
    private String fileType;

    @ApiModelProperty(value = "文件地址")
    private String[] fileUrl;

    @ApiModelProperty(value = "排序序号")
    private Integer sort;

}

