package cn.qbs.wa.teach.question.pojo.question;

import cn.qbs.wa.teach.question.entity.QuestionImportRecord;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 试题(Question)分页查询试题响应
 *
 * @author zcm
 * @version 1.0
 * @date 2021-11-03 18:17:29
 */
@Data
public class QuestionImportRecordPageResponse extends QuestionImportRecord {

    @ApiModelProperty(value = "导入操作人")
    private String importer;

}

