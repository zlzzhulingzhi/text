package cn.qbs.wa.train.logistics.pojo.importrecorddetail;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 导入记录详情表(ImportRecordDetail)创建导入记录详情表参数
 *
 * @author makejava
 * @since 2022-06-22 13:48:25
 */
@Data
public class ImportRecordDetailAddRequest {

	@ApiModelProperty(value = "机构id")
	private Long orgId;

	@ApiModelProperty(value = "导入记录id")
	private Long importRecordId;

	@ApiModelProperty(value = "姓名")
	private String realName;

	@ApiModelProperty(value = "电话号码")
	private String phone;

	@ApiModelProperty(value = "性别 0-未知 1-男 2-女")
	private Integer sex;

	@ApiModelProperty(value = "身份证号码")
	private String idNumber;

	@ApiModelProperty(value = "所属部门")
	private String deptName;

	@ApiModelProperty(value = "学员标签")
	private String groupName;

	@ApiModelProperty(value = "是否成功 0-失败 1-成功")
	private Integer success;

	@ApiModelProperty(value = "失败原因")
	private String failureReason;

}

