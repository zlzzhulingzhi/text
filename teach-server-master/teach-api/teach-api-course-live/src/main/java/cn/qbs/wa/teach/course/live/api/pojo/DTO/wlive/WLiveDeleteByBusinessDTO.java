package cn.qbs.wa.teach.course.live.api.pojo.DTO.wlive;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * TODO
 *
 * @author vieux
 * @version 1.0
 * @date 2022/3/7 13:45
 */
@Data
public class WLiveDeleteByBusinessDTO {

    @NotEmpty(message = "ID不能为空")
    List<Long> idList;

    @ApiModelProperty(value = "1 course")
    @NotNull
    Integer businessType;
}
