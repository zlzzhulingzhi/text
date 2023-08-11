package cn.qbs.wa.train.logistics.pojo.clazzsign;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.List;

/**
 * @author yjx
 */
@Data
public class SignInSearchRequest {

    @ApiModelProperty("班级ID")
    private Long clazzId;

    @ApiModelProperty(value = "机构ID", hidden = true)
    private Long orgId;

    private List<Long> memberIds;

    private List<Integer> signInStatusList;

    @ApiModelProperty(value = "签到日期")
    private LocalDate signInDate;

    @ApiModelProperty(value = "签到情况-开始节数", example = "1")
    private Integer lessonStart;

    @ApiModelProperty(value = "签到情况-结束节数", example = "10")
    private Integer lessonEnd;

}
