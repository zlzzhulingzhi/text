package cn.qbs.wa.train.allowance.pojo.stat;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * @author makejava
 * @since 2022-11-14 15:35:53
 */
@Data
public class StudysResponse {

    @ApiModelProperty(value = "总学时")
    private List<Integer> totalLessonNumList;

    @ApiModelProperty(value = "总人数")
    private List<Integer> totalStudentNumList;

}
