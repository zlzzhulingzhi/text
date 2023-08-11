package cn.qbs.wa.train.logistics.pojo.clazz;


import cn.qbs.wa.teach.domain.BasePageRequest;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import java.util.Date;


@Data
public class ClazzSummaryRequest extends BasePageRequest {

    @ApiModelProperty(value = "机构ID")
    private Long orgId;

    @ApiModelProperty(value = "开班日期")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date startDate;

    @ApiModelProperty(value = "结班日期")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date endDate;

    @ApiModelProperty(value = "班级ID")
    private Long clazzId;
}

