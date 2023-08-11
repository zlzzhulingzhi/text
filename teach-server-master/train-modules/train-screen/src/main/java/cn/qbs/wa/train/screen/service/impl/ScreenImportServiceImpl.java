package cn.qbs.wa.train.screen.service.impl;

import cn.qbs.wa.teach.common.core.exception.ServiceException;
import cn.qbs.wa.teach.common.core.utils.StringUtils;
import cn.qbs.wa.train.screen.enums.DataOverviewItemEnum;
import cn.qbs.wa.train.screen.excel.*;
import cn.qbs.wa.train.screen.pojo.imports.*;
import cn.qbs.wa.train.screen.service.ScreenImportService;
import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.enums.CellDataTypeEnum;
import com.alibaba.excel.event.AnalysisEventListener;
import com.alibaba.excel.metadata.Cell;
import com.alibaba.excel.metadata.data.ReadCellData;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Slf4j
@Service("screenImportService")
public class ScreenImportServiceImpl implements ScreenImportService {

    @Autowired
    ScreenNoticeDataValidateFactory screenNoticeDataValidateFactory;

    @Autowired
    ScreenAttendClassDataValidateFactory screenAttendClassDataValidateFactory;

    @Autowired
    ScreenStatDataDynamicDataValidateFactory screenStatDataDynamicDataValidateFactory;

    @Autowired
    ScreenStatStudentMonthlyDataValidateFactory screenStatStudentMonthlyDataValidateFactory;

    @Autowired
    ScreenDataOverviewDataValidateFactory screenDataOverviewDataValidateFactory;

    @Override
    public List<ScreenDataOverviewDataParseResult> importPreDataOverview(MultipartFile file) {
        ExcelAnalysisListenerDataOverview dataListener = new ExcelAnalysisListenerDataOverview();
        try {
            EasyExcel.read(file.getInputStream(), ScreenDataOverviewExcelData.class, dataListener).ignoreEmptyRow(true).sheet(1).doRead();
        } catch (Exception e) {
            throw new ServiceException("上传的导入模板格式不正确！");
        }
        List<ScreenDataOverviewExcelData> dataList = dataListener.getDataList();
        if (CollectionUtils.isEmpty(dataList)){
            throw new ServiceException("解析不到数据！");
        }
        List<ScreenDataOverviewDataParseResult> resultList = new ArrayList<>();
        List<ScreenDataOverviewDataParseResult> errorList = new ArrayList<>();
        for (ScreenDataOverviewExcelData screenDataOverviewExcelData : dataList) {
            ScreenDataOverviewDataParseResult result = new ScreenDataOverviewDataParseResult();
            ScreenDataOverviewDataValidate screenDataOverviewDataValidate = screenDataOverviewDataValidateFactory.getExcelScreenDataOverviewValidate();
            screenDataOverviewDataValidate.validate(screenDataOverviewExcelData);
            BeanUtils.copyProperties(screenDataOverviewExcelData, result);
            boolean passed = screenDataOverviewDataValidate.passed();
            result.setPassed(passed);
            if (!passed) {
                result.setErrorReasons(screenDataOverviewDataValidate.getErrorReasons());
                errorList.add(result);
            }
            resultList.add(result);
        }
        return resultList;
    }

    class ExcelAnalysisListenerDataOverview extends AnalysisEventListener<ScreenDataOverviewExcelData> {

        private List<ScreenDataOverviewExcelData> dataList = new ArrayList<>();

        /**
         * 每解析一行会回调此方法
         * 通过 AnalysisContext 对象还可以获取当前 sheet，当前行等数据
         *
         * @param
         * @param
         */
        @Override
        public void invoke(ScreenDataOverviewExcelData screenDataOverviewExcelData, AnalysisContext analysisContext) {
            Integer rowIndex = analysisContext.readRowHolder().getRowIndex();
            screenDataOverviewExcelData.setRowNum(rowIndex + 1);
            String displayName = screenDataOverviewExcelData.getDisplayName();
            if (StringUtils.hasText(displayName)) {
                try {
                    screenDataOverviewExcelData.setFieldCode(DataOverviewItemEnum.getCodeName(displayName));
                } catch (Exception e) {
                    log.error("未找到对应的数据名称：{}", displayName);
                    return;
                }
                dataList.add(screenDataOverviewExcelData);
            }
        }

        /**
         * 整个excel解析结束会执行此方法
         *
         * @param analysisContext
         */
        @Override
        public void doAfterAllAnalysed(AnalysisContext analysisContext) {

        }

        public List<ScreenDataOverviewExcelData> getDataList() {
            return dataList;
        }

    }

    @Override
    public List<ScreenStatStudentMonthlyDataParseResult> importPreStatStudentMonthly(MultipartFile file) {
        ExcelAnalysisListenerStatStudentMonthly dataListener = new ExcelAnalysisListenerStatStudentMonthly();
        try {
            EasyExcel.read(file.getInputStream(), ScreenStatStudentMonthlyExcelData.class, dataListener).ignoreEmptyRow(true).sheet(1).doRead();
        } catch (Exception e) {
            throw new ServiceException("上传的导入模板格式不正确！");
        }
        List<ScreenStatStudentMonthlyExcelData> dataList = dataListener.getDataList();
        if (CollectionUtils.isEmpty(dataList)){
            throw new ServiceException("解析不到数据！");
        }
        List<ScreenStatStudentMonthlyDataParseResult> resultList = new ArrayList<>();
        List<ScreenStatStudentMonthlyDataParseResult> errorList = new ArrayList<>();
        for (int i = 0; i < dataList.size(); i++) {
            ScreenStatStudentMonthlyExcelData screenStatStudentMonthlyExcelData = dataList.get(i);
            ScreenStatStudentMonthlyDataParseResult result = new ScreenStatStudentMonthlyDataParseResult();
            ScreenStatStudentMonthlyDataValidate screenStatStudentMonthlyDataValidate = screenStatStudentMonthlyDataValidateFactory.getExcelScreenStatStudentMonthlyValidate();
            screenStatStudentMonthlyDataValidate.validate(screenStatStudentMonthlyExcelData);
            BeanUtils.copyProperties(screenStatStudentMonthlyExcelData, result);
            boolean passed = screenStatStudentMonthlyDataValidate.passed();
            result.setPassed(passed);
            if (!passed) {
                result.setErrorReasons(screenStatStudentMonthlyDataValidate.getErrorReasons());
                errorList.add(result);
            }
            resultList.add(result);
        }
        return resultList;
    }

    class ExcelAnalysisListenerStatStudentMonthly extends AnalysisEventListener<ScreenStatStudentMonthlyExcelData> {

        private List<ScreenStatStudentMonthlyExcelData> dataList = new ArrayList<>();

        /**
         * 每解析一行会回调此方法
         * 通过 AnalysisContext 对象还可以获取当前 sheet，当前行等数据
         *
         * @param
         * @param
         */
        @Override
        public void invoke(ScreenStatStudentMonthlyExcelData screenStatStudentMonthlyExcelData, AnalysisContext analysisContext) {
            Integer rowIndex = analysisContext.readRowHolder().getRowIndex();
            screenStatStudentMonthlyExcelData.setRowNum(rowIndex + 1);
            if (StringUtils.hasText(screenStatStudentMonthlyExcelData.getYear())) {
                dataList.add(screenStatStudentMonthlyExcelData);
                return;
            }
            // 判断是否所有属性都为空
            Map<Integer, Cell> cellMap = analysisContext.readRowHolder().getCellMap();
            cellMap.forEach((k,v) -> {
                ReadCellData cell = (ReadCellData) v;
                if (!CellDataTypeEnum.EMPTY.equals(cell.getType())) {
                    dataList.add(screenStatStudentMonthlyExcelData);
                    return;
                }
            });
        }


        /**
         * 整个excel解析结束会执行此方法
         *
         * @param analysisContext
         */
        @Override
        public void doAfterAllAnalysed(AnalysisContext analysisContext) {

        }

        public List<ScreenStatStudentMonthlyExcelData> getDataList() {
            return dataList;
        }

    }

    @Override
    public List<ScreenStatDataDynamicDataParseResult> importPreStatDataDynamic(MultipartFile file) {
        ExcelAnalysisListenerStatDataDynamic dataListener = new ExcelAnalysisListenerStatDataDynamic();
        try {
            EasyExcel.read(file.getInputStream(), ScreenStatDataDynamicExcelData.class, dataListener).ignoreEmptyRow(true).sheet(1).doRead();
        } catch (Exception e) {
            throw new ServiceException("上传的导入模板格式不正确！");
        }
        List<ScreenStatDataDynamicExcelData> dataList = dataListener.getDataList();
        if (CollectionUtils.isEmpty(dataList)){
            throw new ServiceException("解析不到数据！");
        }
        List<ScreenStatDataDynamicDataParseResult> resultList = new ArrayList<>();
        List<ScreenStatDataDynamicDataParseResult> errorList = new ArrayList<>();
        for (int i = 0; i < dataList.size(); i++) {
            ScreenStatDataDynamicExcelData screenStatDataDynamicExcelData = dataList.get(i);
            ScreenStatDataDynamicDataParseResult result = new ScreenStatDataDynamicDataParseResult();
            ScreenStatDataDynamicDataValidate screenStatDataDynamicDataValidate = screenStatDataDynamicDataValidateFactory.getExcelScreenStatDataDynamicValidate();
            screenStatDataDynamicDataValidate.validate(screenStatDataDynamicExcelData);
            BeanUtils.copyProperties(screenStatDataDynamicExcelData, result);
            boolean passed = screenStatDataDynamicDataValidate.passed();
            result.setPassed(passed);
            if (!passed) {
                result.setErrorReasons(screenStatDataDynamicDataValidate.getErrorReasons());
                errorList.add(result);
            }
            resultList.add(result);
        }
        return resultList;
    }

    class ExcelAnalysisListenerStatDataDynamic extends AnalysisEventListener<ScreenStatDataDynamicExcelData> {

        private List<ScreenStatDataDynamicExcelData> dataList = new ArrayList<>();

        /**
         * 每解析一行会回调此方法
         * 通过 AnalysisContext 对象还可以获取当前 sheet，当前行等数据
         *
         * @param
         * @param
         */
        @Override
        public void invoke(ScreenStatDataDynamicExcelData screenStatDataDynamicExcelData, AnalysisContext analysisContext) {
            Integer rowIndex = analysisContext.readRowHolder().getRowIndex();
            screenStatDataDynamicExcelData.setRowNum(rowIndex + 1);
            if (StringUtils.hasText(screenStatDataDynamicExcelData.getDataName())) {
                dataList.add(screenStatDataDynamicExcelData);
                return;
            }
            // 判断是否所有属性都为空
            Map<Integer, Cell> cellMap = analysisContext.readRowHolder().getCellMap();
            cellMap.forEach((k,v) -> {
                ReadCellData cell = (ReadCellData) v;
                if (!CellDataTypeEnum.EMPTY.equals(cell.getType())) {
                    dataList.add(screenStatDataDynamicExcelData);
                    return;
                }
            });
        }


        /**
         * 整个excel解析结束会执行此方法
         *
         * @param analysisContext
         */
        @Override
        public void doAfterAllAnalysed(AnalysisContext analysisContext) {

        }

        public List<ScreenStatDataDynamicExcelData> getDataList() {
            return dataList;
        }

    }


    @Override
    public List<ScreenAttendClassDataParseResult> importPreAttendClass(MultipartFile file) {
        ExcelAnalysisListenerAttendClass dataListener = new ExcelAnalysisListenerAttendClass();
        try {
            EasyExcel.read(file.getInputStream(), ScreenAttendClassExcelData.class, dataListener).ignoreEmptyRow(true).sheet(1).doRead();
        } catch (Exception e) {
            throw new ServiceException("上传的导入模板格式不正确！");
        }
        List<ScreenAttendClassExcelData> dataList = dataListener.getDataList();
        if (CollectionUtils.isEmpty(dataList)){
            throw new ServiceException("解析不到数据！");
        }
        List<ScreenAttendClassDataParseResult> resultList = new ArrayList<>();
        List<ScreenAttendClassDataParseResult> errorList = new ArrayList<>();
        for (int i = 0; i < dataList.size(); i++) {
            ScreenAttendClassExcelData screenAttendClassExcelData = dataList.get(i);
            ScreenAttendClassDataParseResult result = new ScreenAttendClassDataParseResult();
            ScreenAttendClassDataValidate screenAttendClassDataValidate = screenAttendClassDataValidateFactory.getExcelScreenAttendClassValidate();
            screenAttendClassDataValidate.validate(screenAttendClassExcelData);
            BeanUtils.copyProperties(screenAttendClassExcelData, result);
            boolean passed = screenAttendClassDataValidate.passed();
            result.setPassed(passed);
            if (!passed) {
                result.setErrorReasons(screenAttendClassDataValidate.getErrorReasons());
                errorList.add(result);
            }
            resultList.add(result);
        }
        return resultList;
    }

    class ExcelAnalysisListenerAttendClass extends AnalysisEventListener<ScreenAttendClassExcelData> {

        private List<ScreenAttendClassExcelData> dataList = new ArrayList<>();

        /**
         * 每解析一行会回调此方法
         * 通过 AnalysisContext 对象还可以获取当前 sheet，当前行等数据
         *
         * @param
         * @param
         */
        @Override
        public void invoke(ScreenAttendClassExcelData screenAttendClassExcelData, AnalysisContext analysisContext) {
            Integer rowIndex = analysisContext.readRowHolder().getRowIndex();
            screenAttendClassExcelData.setRowNum(rowIndex + 1);
            if (StringUtils.hasText(screenAttendClassExcelData.getOrgName())) {
                dataList.add(screenAttendClassExcelData);
                return;
            }
            // 判断是否所有属性都为空
            Map<Integer, Cell> cellMap = analysisContext.readRowHolder().getCellMap();
            cellMap.forEach((k,v) -> {
                ReadCellData cell = (ReadCellData) v;
                if (!CellDataTypeEnum.EMPTY.equals(cell.getType())) {
                    dataList.add(screenAttendClassExcelData);
                    return;
                }
            });
        }


        /**
         * 整个excel解析结束会执行此方法
         *
         * @param analysisContext
         */
        @Override
        public void doAfterAllAnalysed(AnalysisContext analysisContext) {

        }

        public List<ScreenAttendClassExcelData> getDataList() {
            return dataList;
        }

    }

    @Override
    public List<ScreenNoticeDataParseResult> importPreNotice(MultipartFile file) {
        ExcelAnalysisListener dataListener = new ExcelAnalysisListener();
        try {
            EasyExcel.read(file.getInputStream(), ScreenNoticeExcelData.class, dataListener).ignoreEmptyRow(true).sheet(1).doRead();
        } catch (Exception e) {
            throw new ServiceException("上传的导入模板格式不正确！");
        }
        List<ScreenNoticeExcelData> dataList = dataListener.getDataList();
        if (CollectionUtils.isEmpty(dataList)){
            throw new ServiceException("解析不到数据！");
        }
        List<ScreenNoticeDataParseResult> resultList = new ArrayList<>();
        List<ScreenNoticeDataParseResult> errorList = new ArrayList<>();
        for (int i = 0; i < dataList.size(); i++) {
            ScreenNoticeExcelData screenNoticeExcelData = dataList.get(i);
            ScreenNoticeDataParseResult result = new ScreenNoticeDataParseResult();
            ScreenNoticeDataValidate screenNoticeDataValidate = screenNoticeDataValidateFactory.getExcelScreenNoticeValidate();
            screenNoticeDataValidate.validate(screenNoticeExcelData);
            BeanUtils.copyProperties(screenNoticeExcelData, result);
            boolean passed = screenNoticeDataValidate.passed();
            result.setPassed(passed);
            if (!passed) {
                result.setErrorReasons(screenNoticeDataValidate.getErrorReasons());
                errorList.add(result);
            }
            resultList.add(result);
        }
        return resultList;
    }

    class ExcelAnalysisListener extends AnalysisEventListener<ScreenNoticeExcelData> {

        private List<ScreenNoticeExcelData> dataList = new ArrayList<>();

        /**
         * 每解析一行会回调此方法
         * 通过 AnalysisContext 对象还可以获取当前 sheet，当前行等数据
         *
         * @param
         * @param
         */
        @Override
        public void invoke(ScreenNoticeExcelData screenNoticeExcelData, AnalysisContext analysisContext) {
            Integer rowIndex = analysisContext.readRowHolder().getRowIndex();
            screenNoticeExcelData.setRowNum(rowIndex + 1);
            if (StringUtils.hasText(screenNoticeExcelData.getTitle())) {
                dataList.add(screenNoticeExcelData);
                return;
            }
            // 判断是否所有属性都为空
            Map<Integer, Cell> cellMap = analysisContext.readRowHolder().getCellMap();
            cellMap.forEach((k,v) -> {
                ReadCellData cell = (ReadCellData) v;
                if (!CellDataTypeEnum.EMPTY.equals(cell.getType())) {
                    dataList.add(screenNoticeExcelData);
                    return;
                }
            });
        }


        /**
         * 整个excel解析结束会执行此方法
         *
         * @param analysisContext
         */
        @Override
        public void doAfterAllAnalysed(AnalysisContext analysisContext) {

        }

        public List<ScreenNoticeExcelData> getDataList() {
            return dataList;
        }

    }
}
