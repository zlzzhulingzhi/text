package cn.qbs.wa.teach.course.common.entity;


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
 * @since 2021-11-18 16:46:12
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class Category extends Model {

    private static final long serialVersionUID = 138447772971100439L;


    @ApiModelProperty(value = "【主键标识】")
    private Long id;

    @ApiModelProperty(value = "【组织机构ID】")
    private Long orgId;

    @ApiModelProperty(value = "【上级分类ID】")
    private Long parentId;

    @ApiModelProperty(value = "【分类代号】")
    private String categoryCode;

    @ApiModelProperty(value = "【分类名称】")
    private String categoryName;

    @ApiModelProperty(value = "【备注】")
    private String remark;

    @ApiModelProperty(value = "【排序】")
    private Integer sort;

    @ApiModelProperty(value = "【启用状态 0-未启用 1-已启用】")
    private Integer enabled;

    @ApiModelProperty(value = "【是否为模板 0-否 1-是】")
    private Integer template;

    @ApiModelProperty(value = "【删除状态 0 正常 1删除】")
    private Integer deleted;

    @ApiModelProperty(value = "【【分类图标】")
    private String categoryIconUrl;

    @ApiModelProperty(value = "【创建人ID】")
    @TableField(fill = FieldFill.INSERT)
    private Long createBy;

    @ApiModelProperty(value = "【创建时间】")
    @TableField(fill = FieldFill.INSERT)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private LocalDateTime createTime;

    @ApiModelProperty(value = "【最后修改人ID】")
    @TableField(fill = FieldFill.UPDATE)
    private Long updateBy;

    @ApiModelProperty(value = "【最后修改时间】")
    @TableField(fill = FieldFill.UPDATE)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private LocalDateTime updateTime;

    @ApiModelProperty(value = "父节点ID串")
    private String parentCode;

    @ApiModelProperty(value = "分类全称")
    private String fullName;

}
