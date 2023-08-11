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
 * @since 2021-12-13 17:27:21
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class Rule extends Model {

    private static final long serialVersionUID = 737161474501736590L;


    @ApiModelProperty(value = "")
    private Long id;

    @ApiModelProperty(value = "机构id")
    private Long orgId;

    @ApiModelProperty(value = "规则类型 1-防作弊规则 2-试题规则 3-考试规则 4-查看试卷作答情况")
    private Integer type;

    @ApiModelProperty(value = "分组code")
    private String groupCode;

    @ApiModelProperty(value = "规则编码")
    private String code;

    @ApiModelProperty(value = "规则名称")
    private String ruleName;

    @ApiModelProperty(value = "0 禁用 1启用")
    private Boolean enabled;

    @ApiModelProperty(value = "")
    @TableField(fill = FieldFill.INSERT)
    private Long createBy;

    @ApiModelProperty(value = "")
    @TableField(fill = FieldFill.INSERT)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private LocalDateTime createTime;

    @ApiModelProperty(value = "UI类型 [radio, checkbox]")
    private String uiType;

}
