package cn.qbs.wa.train.logistics.pojo.invitation;

import cn.qbs.wa.teach.common.core.domain.IdListRequest;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 邀请表(Invitation)创建邀请表参数
 *
 * @author makejava
 * @since 2022-06-20 19:10:52
 */
@Data
public class BatchEnabledRequest extends IdListRequest {

	@ApiModelProperty(value = "0 禁用 1启用")
	private Integer enabled;

}

