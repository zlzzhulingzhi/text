package cn.qbs.wa.teach.question.api.pojo.DTO;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * 分类(Category)创建分类参数
 *
 * @author zcm
 * @version 1.0
 * @date 2021-11-03 17:08:47
 */
@Data
public class QuestionCategoryAddRequestDTO {

    @ApiModelProperty(value = "分类名称")
    private String name;

    @ApiModelProperty(value = "父节点ID")
    private Long parentId;

    @ApiModelProperty(value = "排序号")
    private Integer sortNum;

    @ApiModelProperty(value = "分组ID【1：试题，2：试卷】")
    private Integer groupId;

    @ApiModelProperty(value = "【组织机构ID】")
    private Long orgId;

}

