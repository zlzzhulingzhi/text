package cn.qbs.wa.teach.course.standard.pojo.dto;


import com.baomidou.mybatisplus.extension.activerecord.Model;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author makejava
 * @since 2021-12-29 17:14:33
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class CourseAttachmentDTO extends Model {

    private static final long serialVersionUID = -16423202658802770L;

    @ApiModelProperty(value = "【主键标识】")
    private Long id;

    @ApiModelProperty(value = "【组织机构ID】")
    private Long orgId;

    @ApiModelProperty(value = "【课程ID】")
    private Long courseId;

    @ApiModelProperty(value = "【讲义ID】")
    private Long resourceFileId;

    @ApiModelProperty(value = "【讲义名】")
    private String resourceFileName;

    @ApiModelProperty(value = "【讲义类型】")
    private String resourceFileType;

    @ApiModelProperty(value = "【排序】")
    private Integer sort;

    @ApiModelProperty(value = "查看次数")
    private Integer views;

    @ApiModelProperty(value = "下载次数")
    private Integer downloads;

}
