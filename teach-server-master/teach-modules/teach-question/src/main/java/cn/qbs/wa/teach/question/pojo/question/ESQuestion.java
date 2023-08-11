package cn.qbs.wa.teach.question.pojo.question;

import com.alibaba.fastjson.annotation.JSONField;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * 存入ElasticSearch的试题
 *
 * @Author zcm
 * @Date 2022-05-16 15:17
 * @Version 1.0
 */
@Data
public class ESQuestion {

    @ApiModelProperty(value = "试题ID")
    private Long id;

    @ApiModelProperty(value = "题型ID")
    private Long questionTypeId;

    @ApiModelProperty(value = "难度ID")
    private Long difficultyId;

    @ApiModelProperty(value = "机构ID")
    private Long orgId;

    @ApiModelProperty(value = "分类ID列表")
    private List<Long> categoryIds = new ArrayList<>();

    /** 主体部分 */
    @ApiModelProperty(value = "题干")
    private String content;

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

    @ApiModelProperty(value = "搜索权重")
    private Integer weight;

    @ApiModelProperty(value = "编辑状态 0 不可编辑 1可编辑")
    private Boolean editable = true;

    @ApiModelProperty(value = "是否启用 【1-启用，0-废弃】")
    private Boolean enabled;

}

