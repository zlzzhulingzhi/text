package cn.qbs.wa.teach.auth.pojo.request;

import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class ExchangeTokenRequest {

    private String key;

    @NotNull
    private Long orgId;

    @NotNull
    private Long memberId;

}
