package cn.qbs.wa.teach.question.pojo.question;

import cn.qbs.wa.teach.question.pojo.question.option.QuestionOptionTo;
import com.alibaba.fastjson.annotation.JSONField;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * 最基本的试题（用于校验、ElasticSearch索引）
 *
 * @author zcm
 * @version 1.0
 * @date 2021-11-03 18:17:29
 */
@Data
public class BasicQuestion implements Serializable {

    @ApiModelProperty(value = "试题ID")
    private Long id;

    @ApiModelProperty(value = "题型ID")
    private Long questionTypeId;

    @ApiModelProperty(value = "使用次数")
    private Integer useCount;

    @ApiModelProperty(value = "难度ID")
    private Long difficultyId;

    @ApiModelProperty(value = "分数")
    private BigDecimal score;

    @ApiModelProperty(value = "机构ID")
    private Long orgId;

    @ApiModelProperty(value = "父题标记, 1: 父题 0:小题")
    private Integer parentFlag;

    @ApiModelProperty(value = "分类ID列表")
    private List<Long> categoryIds = new ArrayList<>();

    /** 主体部分 */
    @ApiModelProperty(value = "题干")
    private String content;

    @ApiModelProperty(value = "答案")
    private String answer;

    @ApiModelProperty(value = "答案解析")
    private String answerDesc;

    @ApiModelProperty(value = "选项集合")
    private List<QuestionOptionTo> options;

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

    @ApiModelProperty(value = "题型名称")
    private String questionTypeName;

    @ApiModelProperty(value = "难度名称")
    private String difficultyName;

    @ApiModelProperty(value = "搜索权重")
    private Integer weight;

    @ApiModelProperty(value = "编辑状态 0 不可编辑 1可编辑")
    private Boolean editable = true;

}

