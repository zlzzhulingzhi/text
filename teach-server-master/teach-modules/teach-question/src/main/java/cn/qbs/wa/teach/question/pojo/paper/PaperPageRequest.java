package cn.qbs.wa.teach.question.pojo.paper;


import lombok.Data;
import io.swagger.annotations.ApiModelProperty;
import cn.qbs.wa.teach.domain.BasePageRequest;

import java.math.BigDecimal;

/**
 * 试卷(Paper)分页查询参数
 *
 * @author zcm
 * @version 1.0
 * @date 2021-11-18 20:48:48
 */
@Data
public class PaperPageRequest extends BasePageRequest {
    
    @ApiModelProperty(value = "试卷名称")
    private String name;
    
    @ApiModelProperty(value = "难度ID")
    private Integer difficultyId;
    
    @ApiModelProperty(value = "试卷题目总数")
    private Integer questionCount;
    
    @ApiModelProperty(value = "试卷总分")
    private BigDecimal totalScore;
    
    @ApiModelProperty(value = "考试时长(分钟)")
    private Integer duration;
    
    @ApiModelProperty(value = "是否启用 【1：启用，0：废弃】")
    private Integer enabled;
    
    @ApiModelProperty(value = "备注")
    private String remark;
    
    @ApiModelProperty(value = "机构ID")
    private Long orgId;

}

