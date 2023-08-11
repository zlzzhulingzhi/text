package cn.qbs.wa.teach.organization.service.impl;

import cn.hutool.core.util.StrUtil;
import cn.qbs.wa.teach.common.core.context.SecurityContextHolder;
import cn.qbs.wa.teach.common.core.exception.IllegalParamsException;
import cn.qbs.wa.teach.common.core.utils.AesUtil;
import cn.qbs.wa.teach.organization.entity.Employee;
import cn.qbs.wa.teach.organization.entity.ImportRecordDetail;
import cn.qbs.wa.teach.organization.entity.Student;
import cn.qbs.wa.teach.organization.enums.SexEnum;
import cn.qbs.wa.teach.organization.excel.ImportReportResult;
import cn.qbs.wa.teach.organization.mapper.ImportRecordMapper;
import cn.qbs.wa.teach.organization.entity.ImportRecord;
import cn.qbs.wa.teach.organization.service.EmployeeService;
import cn.qbs.wa.teach.organization.service.ImportRecordDetailService;
import cn.qbs.wa.teach.organization.service.ImportRecordService;
import cn.qbs.wa.teach.organization.pojo.importrecord.*;
import com.alibaba.excel.EasyExcel;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.Serializable;
import java.net.URLEncoder;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 导入记录表(ImportRecord)表服务实现类
 *
 * @author makejava
 * @since 2022-06-22 13:47:48
 */
@Slf4j
@Service("importRecordService")
public class ImportRecordServiceImpl extends ServiceImpl<ImportRecordMapper, ImportRecord> implements ImportRecordService {

	@Value("${organization.importReportTemplate}")
	private String organizationImportReportTemplate;

	@Resource
	private EmployeeService employeeService;

	@Resource
	private ImportRecordDetailService detailService;

	@Override
	public boolean add(ImportRecordAddRequest params) {
		ImportRecord importRecord = new ImportRecord();
		BeanUtils.copyProperties(params, importRecord);
		return this.save(importRecord);
	}

	@Override
	public IPage<ImportRecordPageResponse> page(ImportRecordPageRequest params) {
		IPage<ImportRecordPageResponse> recordPageList = baseMapper.page(params.createMpPage(), params);
		if (CollectionUtils.isEmpty(recordPageList.getRecords())){
			return recordPageList;
		}
		List<Long> createByList = recordPageList.getRecords().stream().map(ImportRecord::getCreateBy).distinct().collect(Collectors.toList());
		List<Employee> employeeList = employeeService.lambdaQuery().eq(Employee::getOrgId, SecurityContextHolder.getOrgId()).in(Employee::getUserId, createByList).list();
		if (CollectionUtils.isEmpty(employeeList)){
			return recordPageList;
		}
		Map<Long,Employee> employeeMap = employeeList.stream().collect(Collectors.toMap(Employee::getUserId, o -> o));
		recordPageList.getRecords().forEach(i -> {
			i.setCreateName(employeeMap.get(i.getCreateBy()).getRealName());
			i.setPhone(AesUtil.decode(employeeMap.get(i.getCreateBy()).getPhone()));
		});
		return recordPageList;
	}

	@Override
	public void outExcel(Long importRecordId, HttpServletResponse response) throws Exception {
		ImportRecord importRecord = this.getById(importRecordId);
		if (importRecord == null){
			throw new IllegalParamsException("导入记录不存在！");
		}
		List<ImportRecordDetail> detailsList = detailService.lambdaQuery().eq(ImportRecordDetail::getImportRecordId, importRecord.getId()).list();
		List<ImportReportResult> importReportResultList = detailsList.stream().map(i -> {
			ImportReportResult importReportResult = new ImportReportResult();
			BeanUtils.copyProperties(i,importReportResult);
			importReportResult.setPassed(i.getSuccess() == 1 ? "成功" : "失败");
			importReportResult.setSexName(i.getSex() == null ? null : SexEnum.getEnum(i.getSex()).getName());
			return importReportResult;
		}).collect(Collectors.toList());

		//设置Excel信息
		response.setContentType("application/vnd.ms-excel");
		String charset = "UTF-8";
		response.setCharacterEncoding(charset);
		String fileName = StrUtil.format("{}导入报告{}", importRecord.getBusinessType() == 1 ? "学员" : "职工", DateTimeFormatter.ofPattern("yyyyMMddHHmmss").format(importRecord.getImportTime()));
		String encodedFileName = URLEncoder.encode(fileName, charset).replaceAll("\\+", "%20") + ".xlsx";
		response.addHeader("fileName", encodedFileName);
		response.setContentType("application/octet-stream");
		response.setHeader("Content-Disposition", "attachment;filename=" + encodedFileName);

		FileInputStream fileInputStream = new FileInputStream(new File(organizationImportReportTemplate));
		EasyExcel.write(response.getOutputStream()).withTemplate(fileInputStream).sheet(1).doFill(importReportResultList);
	}

	@Override
	public ImportRecordDetailResponse detail(Serializable id) {
		return baseMapper.selectDetailById(id);
	}

	@Override
	public boolean update(ImportRecordUpdateRequest params) {
		if (params.getId() == null) {
			throw new IllegalParamsException("ID不能为空！");
		}
		ImportRecord importRecord = new ImportRecord();
		BeanUtils.copyProperties(params, importRecord);
		return this.updateById(importRecord);
	}

	@Override
	public boolean deleteByIds(List<Long> idList) {
		return this.removeByIds(idList);
	}

}

