package cn.qbs.wa.teach.question.pojo.question;

import cn.qbs.wa.teach.question.entity.Category;
import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 试题(Question)试题详情
 *
 * @author zcm
 * @version 1.0
 * @date 2021-11-03 18:17:29
 */
@Data
public class QuestionDetailResponse extends BasicQuestion {

    @ApiModelProperty(value = "最后更新者ID")
    private Long updateBy;

    @ApiModelProperty(value = "最后更新日期")
    @TableField(fill = FieldFill.UPDATE)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private LocalDateTime updateTime;

    @ApiModelProperty(value = "分类集合")
    private List<Category> categoryList;

    @ApiModelProperty(value = "是否启用 【1：启用，0：禁用】")
    private Boolean enabled;

    @ApiModelProperty(value = "编辑状态 0 不可编辑 1可编辑")
    private Boolean editable;

}

