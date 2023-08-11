package cn.qbs.wa.teach.organization.service;

import cn.qbs.wa.teach.organization.entity.ImportRecordDetail;
import cn.qbs.wa.teach.organization.pojo.importrecorddetail.*;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;

import java.io.Serializable;
import java.util.List;

/**
 * 导入记录详情表(ImportRecordDetail)表服务接口
 *
 * @author makejava
 * @since 2022-06-22 13:48:24
 */
public interface ImportRecordDetailService extends IService<ImportRecordDetail> {

	/**
	 * 新增导入记录详情表
	 *
	 * @param params
	 * @return
	 */
	boolean add(ImportRecordDetailAddRequest params);

	/**
	 * 分页查询导入记录详情表
	 *
	 * @param params
	 * @return
	 */
	IPage<ImportRecordDetailPageResponse> page(ImportRecordDetailPageRequest params);

	/**
	 * 获取详细信息
	 *
	 * @param id
	 * @return
	 */
	ImportRecordDetailDetailResponse detail(Serializable id);

	/**
	 * 更新导入记录详情表
	 *
	 * @param params
	 * @return
	 */
	boolean update(ImportRecordDetailUpdateRequest params);

	/**
	 * 删除导入记录详情表
	 *
	 * @param idList
	 * @return
	 */
	boolean deleteByIds(List<Long> idList);

}

