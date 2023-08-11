package cn.qbs.wa.train.screen.service;

import cn.qbs.wa.train.screen.pojo.imports.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface ScreenImportService {

    List<ScreenNoticeDataParseResult> importPreNotice(MultipartFile file);

    List<ScreenAttendClassDataParseResult> importPreAttendClass(MultipartFile file);

    List<ScreenStatDataDynamicDataParseResult> importPreStatDataDynamic(MultipartFile file);

    List<ScreenStatStudentMonthlyDataParseResult> importPreStatStudentMonthly(MultipartFile file);

    List<ScreenDataOverviewDataParseResult> importPreDataOverview(MultipartFile file);
}
