package cn.qbs.wa.train.logistics.pojo.classroomattach;


import lombok.Data;
import io.swagger.annotations.ApiModelProperty;
import cn.qbs.wa.teach.domain.BasePageRequest;

/**
 * 教室附件(ClassroomAttach)分页查询参数
 *
 * @author makejava
 * @since 2022-10-12 15:36:11
 */
@Data
public class ClassroomAttachPageRequest extends BasePageRequest {

    @ApiModelProperty(value = "教室ID")
    private Long classroomId;

    @ApiModelProperty(value = "文件类型(pic-图片 doc-文件)")
    private String fileType;

    @ApiModelProperty(value = "文件地址")
    private String fileUrl;

    @ApiModelProperty(value = "排序序号")
    private Integer sort;

}

