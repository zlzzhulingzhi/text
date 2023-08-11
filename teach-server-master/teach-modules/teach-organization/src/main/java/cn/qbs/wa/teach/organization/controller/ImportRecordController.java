package cn.qbs.wa.teach.organization.controller;

import cn.qbs.wa.teach.common.core.domain.R;

import cn.qbs.wa.teach.organization.pojo.importrecord.*;
import cn.qbs.wa.teach.organization.service.ImportRecordService;
import com.baomidou.mybatisplus.core.metadata.IPage;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

import cn.qbs.wa.teach.common.core.domain.IdListRequest;
import cn.qbs.wa.teach.common.core.domain.IdRequest;
import springfox.documentation.annotations.ApiIgnore;


/**
 * 导入记录表(ImportRecord)表控制层
 *
 * @author makejava
 * @since 2022-06-22 13:47:48
 */
@Api(tags = "导入记录")
@RestController
@RequestMapping("importRecord")
public class ImportRecordController {

	/**
	 * 服务对象
	 */
	@Resource
	private ImportRecordService importRecordService;


	/**
	 * 新增导入记录表
	 *
	 * @param params
	 * @return
	 */
	@ApiIgnore
	@PostMapping("add")
	public R<Boolean> add(@RequestBody @Validated ImportRecordAddRequest params) {
		return R.ok(this.importRecordService.add(params));
	}

	/**
	 * 分页查询导入记录表
	 *
	 * @param params
	 * @return
	 */
	@PostMapping("page")
	@ApiOperation("分页")
	public R<IPage<ImportRecordPageResponse>> page(@RequestBody ImportRecordPageRequest params) {
		return R.ok(this.importRecordService.page(params));
	}

	/**
	 * 下载导入报告
	 * @param response
	 * @throws Exception
	 */
	@ApiOperation("下载导入报告")
	@PostMapping("/download/report")
	public void downloadReport(@RequestBody IdRequest params, HttpServletResponse response) throws Exception {
		this.importRecordService.outExcel(params.getId(), response);
	}

	/**
	 * 查询导入记录表详情
	 *
	 * @param request 主键
	 * @return
	 */
	@ApiIgnore
	@PostMapping("detail")
	public R<ImportRecordDetailResponse> detail(@RequestBody IdRequest request) {
		return R.ok(this.importRecordService.detail(request.getId()));
	}

	/**
	 * 修改导入记录表
	 *
	 * @param params
	 * @return
	 */
	@ApiIgnore
	@PostMapping("update")
	public R<Boolean> update(@RequestBody @Validated ImportRecordUpdateRequest params) {
		return R.ok(this.importRecordService.update(params));
	}

	/**
	 * 删除导入记录表
	 *
	 * @param request 主键集合
	 * @return
	 */
	@ApiIgnore
	@PostMapping("delete")
	public R<Boolean> delete(@RequestBody IdListRequest request) {
		return R.ok(this.importRecordService.deleteByIds(request.getIdList()));
	}

}

