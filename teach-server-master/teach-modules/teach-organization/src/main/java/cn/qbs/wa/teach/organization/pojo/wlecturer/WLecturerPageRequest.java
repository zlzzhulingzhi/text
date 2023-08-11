package cn.qbs.wa.teach.organization.pojo.wlecturer;


import cn.qbs.wa.teach.domain.BasePageRequest;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 插件-讲师表(WLecturer)分页查询参数
 *
 * @author makejava
 * @since 2022-03-01 13:44:49
 */
@Data
public class WLecturerPageRequest extends BasePageRequest {

    @ApiModelProperty(value = "讲师名称")
    String name;


    @ApiModelProperty(value = "0 禁用 1启用")
    private Integer enabled;

}

