package cn.qbs.wa.teach.config.mybatisplus;

import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.context.config.annotation.RefreshScope;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author zcm
 * @Date 2021/10/28 16:46
 * @Version 1.0
 */
@Getter
@Setter
@RefreshScope
@ConfigurationProperties(prefix = "mate.tenant")
public class TenantProperties {

    /**
     * 是否开启租户模式
     */
    private Boolean enable = true;

    /**
     * 需要排除的多租户的表
     */
    @Value("#{'${mate.tenant.ignoreTables:}'.empty ? null : '${mate.tenant.ignoreTables}'.split(',')}")
    private List<String> ignoreTables = new ArrayList<>();

    /**
     * 多租户字段名称
     */
    private String column = "org_id";

}
