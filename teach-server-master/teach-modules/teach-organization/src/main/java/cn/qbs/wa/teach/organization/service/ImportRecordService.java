package cn.qbs.wa.teach.organization.service;

import cn.qbs.wa.teach.organization.entity.ImportRecord;
import cn.qbs.wa.teach.organization.pojo.importrecord.*;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;

import javax.servlet.http.HttpServletResponse;
import java.io.Serializable;
import java.util.List;

/**
 * 导入记录表(ImportRecord)表服务接口
 *
 * @author makejava
 * @since 2022-06-22 13:47:48
 */
public interface ImportRecordService extends IService<ImportRecord> {

	/**
	 * 新增导入记录表
	 *
	 * @param params
	 * @return
	 */
	boolean add(ImportRecordAddRequest params);

	/**
	 * 分页查询导入记录表
	 *
	 * @param params
	 * @return
	 */
	IPage<ImportRecordPageResponse> page(ImportRecordPageRequest params);

	/**
	 * 输出Excel
	 * @param importRecordId
	 * @param response
	 */
	void outExcel(Long importRecordId, HttpServletResponse response) throws Exception;

	/**
	 * 获取详细信息
	 *
	 * @param id
	 * @return
	 */
	ImportRecordDetailResponse detail(Serializable id);

	/**
	 * 更新导入记录表
	 *
	 * @param params
	 * @return
	 */
	boolean update(ImportRecordUpdateRequest params);

	/**
	 * 删除导入记录表
	 *
	 * @param idList
	 * @return
	 */
	boolean deleteByIds(List<Long> idList);

}

