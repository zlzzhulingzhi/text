package cn.qbs.wa.train.logistics.entity;


import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import java.time.LocalDateTime;

/**
 * @author makejava
 * @since 2022-11-24 15:31:34
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class MatterReportAttach extends Model {

    private static final long serialVersionUID = 947282317455360321L;

    @ApiModelProperty(value = "主键")
    private Long id;

    @ApiModelProperty(value = "报事ID")
    private Long matterReportId;

    @ApiModelProperty(value = "文件类型(pic-图片 doc-文件)")
    private String fileType;

    @ApiModelProperty(value = "文件地址")
    private String fileUrl;

    @ApiModelProperty(value = "排序序号")
    private Integer sort;

    @ApiModelProperty(value = "创建人ID")
    @TableField(fill = FieldFill.INSERT)
    private Long createBy;

    @ApiModelProperty(value = "创建时间")
    @TableField(fill = FieldFill.INSERT)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private LocalDateTime createTime;

}
