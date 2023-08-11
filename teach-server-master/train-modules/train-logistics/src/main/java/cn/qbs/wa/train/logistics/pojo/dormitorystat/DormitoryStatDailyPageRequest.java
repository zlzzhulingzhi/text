package cn.qbs.wa.train.logistics.pojo.dormitorystat;


import cn.qbs.wa.teach.domain.BasePageRequest;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.time.LocalDate;

/**
 * 宿舍房型统计(DormitoryStat)分页查询参数
 *
 * @author makejava
 * @since 2023-06-05 11:10:10
 */
@Data
public class DormitoryStatDailyPageRequest extends BasePageRequest {

    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate startDate;

    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate endDate;

}

