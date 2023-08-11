package cn.qbs.wa.train.logistics.pojo.dormitoryattach;


import lombok.Data;
import io.swagger.annotations.ApiModelProperty;
import cn.qbs.wa.teach.domain.BasePageRequest;

/**
 * 宿舍附件(DormitoryAttach)分页查询参数
 *
 * @author makejava
 * @since 2022-10-13 09:36:14
 */
@Data
public class DormitoryAttachPageRequest extends BasePageRequest {

    @ApiModelProperty(value = "宿舍ID")
    private Long dormitoryId;

    @ApiModelProperty(value = "文件类型(pic-图片 doc-文件)")
    private String fileType;

    @ApiModelProperty(value = "文件地址")
    private String fileUrl;

    @ApiModelProperty(value = "排序序号")
    private Integer sort;

}

