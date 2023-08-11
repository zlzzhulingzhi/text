package cn.qbs.wa.train.logistics.api.pojo.DTO;


import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 班级学员表(ClazzStudent)分页查询参数
 *
 * @author makejava
 * @since 2022-10-08 17:28:15
 */
@Data
public class ClazzStudentPageRequestDTO{

    @ApiModelProperty(value = "机构ID")
    private Long orgId;

    @ApiModelProperty(value = "班级ID")
    private Long clazzId;

}

