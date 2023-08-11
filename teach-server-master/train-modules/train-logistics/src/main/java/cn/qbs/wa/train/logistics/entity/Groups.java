package cn.qbs.wa.train.logistics.entity;


import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Date;

/**
 * @author makejava
 * @since 2022-03-28 09:27:33
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class Groups extends Model {

    private static final long serialVersionUID = 729445952842186147L;


    @ApiModelProperty(value = "主键ID")
    private Long id;

    @ApiModelProperty(value = "机构ID")
    private Long orgId;

    @ApiModelProperty(value = "父id")
    private Long parentId;

    @ApiModelProperty(value = "业务编码(编码一般为表名)")
    private String businessCode;

    @ApiModelProperty(value = "组名称")
    private String groupName;

    @ApiModelProperty(value = "组编码")
    private String groupCode;

    @ApiModelProperty(value = "0 禁用 1启用")
    private Integer enabled;

    @ApiModelProperty(value = "排序")
    private Integer sort;

    @ApiModelProperty(value = "备注")
    private String remark;

    @ApiModelProperty(value = "")
    @TableField(fill = FieldFill.INSERT)
    private Long createBy;

    @ApiModelProperty(value = "")
    @TableField(fill = FieldFill.INSERT)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date createTime;

}
