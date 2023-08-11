package cn.qbs.wa.train.logistics.pojo.classroomattach;


import lombok.Data;
import io.swagger.annotations.ApiModelProperty;

/**
 * 教室附件(ClassroomAttach)创建教室附件参数
 *
 * @author makejava
 * @since 2022-10-12 15:36:12
 */
@Data
public class ClassroomAttachAddRequest {

    @ApiModelProperty(value = "教室ID")
    private Long classroomId;

    @ApiModelProperty(value = "文件类型(pic-图片 doc-文件)")
    private String fileType;

    @ApiModelProperty(value = "文件地址")
    private String[] fileUrl;

    @ApiModelProperty(value = "排序序号")
    private Integer sort;

}

