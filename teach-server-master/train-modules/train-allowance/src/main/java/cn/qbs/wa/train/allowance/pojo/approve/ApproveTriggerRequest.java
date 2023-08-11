package cn.qbs.wa.train.allowance.pojo.approve;

import lombok.Data;

import javax.validation.constraints.NotEmpty;

@Data
public class ApproveTriggerRequest {

    @NotEmpty(message = "key不能为空")
    private String key;

}
