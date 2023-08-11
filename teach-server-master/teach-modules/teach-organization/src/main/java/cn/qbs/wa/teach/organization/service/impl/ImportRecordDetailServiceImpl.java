package cn.qbs.wa.teach.organization.service.impl;

import cn.qbs.wa.teach.common.core.exception.IllegalParamsException;
import cn.qbs.wa.teach.organization.mapper.ImportRecordDetailMapper;
import cn.qbs.wa.teach.organization.entity.ImportRecordDetail;
import cn.qbs.wa.teach.organization.service.ImportRecordDetailService;
import cn.qbs.wa.teach.organization.pojo.importrecorddetail.*;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.util.List;

/**
 * 导入记录详情表(ImportRecordDetail)表服务实现类
 *
 * @author makejava
 * @since 2022-06-22 13:48:25
 */
@Slf4j
@Service("importRecordDetailService")
public class ImportRecordDetailServiceImpl extends ServiceImpl<ImportRecordDetailMapper, ImportRecordDetail> implements ImportRecordDetailService {

	@Override
	public boolean add(ImportRecordDetailAddRequest params) {
		ImportRecordDetail importRecordDetail = new ImportRecordDetail();
		BeanUtils.copyProperties(params, importRecordDetail);
		return this.save(importRecordDetail);
	}

	@Override
	public IPage<ImportRecordDetailPageResponse> page(ImportRecordDetailPageRequest params) {
		return baseMapper.page(params.createMpPage(), params);
	}

	@Override
	public ImportRecordDetailDetailResponse detail(Serializable id) {
		return baseMapper.selectDetailById(id);
	}

	@Override
	public boolean update(ImportRecordDetailUpdateRequest params) {
		if (params.getId() == null) {
			throw new IllegalParamsException("ID不能为空！");
		}
		ImportRecordDetail importRecordDetail = new ImportRecordDetail();
		BeanUtils.copyProperties(params, importRecordDetail);
		return this.updateById(importRecordDetail);
	}

	@Override
	public boolean deleteByIds(List<Long> idList) {
		return this.removeByIds(idList);
	}

}

