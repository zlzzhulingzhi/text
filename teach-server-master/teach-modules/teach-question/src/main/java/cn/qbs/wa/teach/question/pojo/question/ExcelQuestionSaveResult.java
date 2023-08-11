package cn.qbs.wa.teach.question.pojo.question;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashSet;
import java.util.Set;

/**
 * Excel试题保存结果类
 *
 * @author zcm
 * @version 1.0
 * @date 2021-11-10 12:00:29
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ExcelQuestionSaveResult {

    @ApiModelProperty(value = "数据标识")
    private String key;

    @ApiModelProperty(value = "保存后的ID")
    private Long id;

    @ApiModelProperty(value = "错误原因")
    private Set<String> errorReasons = new HashSet<>();


    public void addErrorReason(String errorReason) {
        this.errorReasons.add(errorReason);
    }

}

