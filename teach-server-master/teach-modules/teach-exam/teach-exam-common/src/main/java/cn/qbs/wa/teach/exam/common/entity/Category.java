package cn.qbs.wa.teach.exam.common.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import java.time.LocalDateTime;

/**
 * @author zcm
 * @since 2021-12-13 17:40:07
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class Category extends Model {

    private static final long serialVersionUID = 852938117498986620L;


    @ApiModelProperty(value = "")
    private Long id;

    @ApiModelProperty(value = "名称")
    private String name;

    @ApiModelProperty(value = "父亲id")
    private Long parentId;

    @ApiModelProperty(value = "0 禁用 1启用")
    private Boolean enabled;

    @ApiModelProperty(value = "")
    @TableField(fill = FieldFill.INSERT)
    private Long createBy;

    @ApiModelProperty(value = "")
    @TableField(fill = FieldFill.INSERT)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private LocalDateTime createTime;

    @ApiModelProperty(value = "")
    private Long updateBy;

    @ApiModelProperty(value = "")
    @TableField(fill = FieldFill.UPDATE)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private LocalDateTime updateTime;

    @ApiModelProperty(value = "机构id")
    private Long orgId;

    @ApiModelProperty(value = "分类编码")
    private String categoryCode;

    @ApiModelProperty(value = "备注")
    private String remark;

    @ApiModelProperty(value = "是否为模板 0-否 1-是")
    private Integer template;

    @ApiModelProperty(value = "编辑状态 0不能编辑  1能编辑")
    private Integer editd;

    @ApiModelProperty(value = "排序")
    private Integer sort;

}
