package cn.qbs.wa.teach.question.api.pojo.DTO.paper;

import cn.qbs.wa.teach.question.api.pojo.DTO.CategoryDTO;
import com.alibaba.fastjson.annotation.JSONField;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

/**
 * @Author zcm
 * @Date 2021/11/26 16:28
 * @Version 1.0
 */
@Data
public class PaperDTO {

    @ApiModelProperty(value = "ID")
    private Long id;

    @ApiModelProperty(value = "试卷名称")
    private String name;

    @ApiModelProperty(value = "试卷题目总数")
    private Integer questionCount;

    @ApiModelProperty(value = "试卷总分")
    private BigDecimal totalScore;

    @ApiModelProperty(value = "是否启用 【1：启用，0：废弃】")
    private Boolean enabled;

    @ApiModelProperty(value = "备注")
    private String remark;

    @ApiModelProperty(value = "机构ID")
    private Long orgId;

    @ApiModelProperty(value = "分类ID")
    private Long categoryId;

    @ApiModelProperty(value = "分类名称")
    private String categoryName;

    @ApiModelProperty(value = "创建者ID")
    private Long createBy;

    @ApiModelProperty(value = "创建日期")
    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private LocalDateTime createTime;

    @ApiModelProperty(value = "最后更新者ID")
    private Long updateBy;

    @ApiModelProperty(value = "最后更新日期")
    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private LocalDateTime updateTime;

    @ApiModelProperty(value = "分类列表")
    private List<CategoryDTO> categoryList;

}
