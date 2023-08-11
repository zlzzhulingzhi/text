package cn.qbs.wa.teach.question.pojo.question;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * Excel试题保存传输类
 *
 * @author zcm
 * @version 1.0
 * @date 2021-11-10 12:00:29
 */
@Data
public class ExcelQuestionSaveTo extends ExcelQuestion {

    @ApiModelProperty(value = "数据标识")
    private String key;

}

