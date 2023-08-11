package cn.qbs.wa.train.logistics.excel;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Set;

@Data
public class StudentDataParseResult extends StudentData{

    @ApiModelProperty(value = "是否校验通过")
    private Boolean passed;

    @ApiModelProperty(value = "错误原因")
    private Set<String> errorReasons;

}
