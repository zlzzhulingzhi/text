package cn.qbs.wa.train.logistics.pojo.stat;

import cn.qbs.wa.teach.domain.BasePageRequest;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.time.LocalDate;

@Data
@EqualsAndHashCode(callSuper = false)
public class AttendClassPageRequest extends BasePageRequest {

    @JsonFormat(pattern = "yyyy-MM-dd")
    @ApiModelProperty(value = "开班日期")
    private LocalDate startDate;

    @JsonFormat(pattern = "yyyy-MM-dd")
    @ApiModelProperty(value = "结班日期")
    private LocalDate endDate;

}