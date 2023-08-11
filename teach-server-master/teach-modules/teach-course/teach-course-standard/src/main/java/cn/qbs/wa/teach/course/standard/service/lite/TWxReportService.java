package cn.qbs.wa.teach.course.standard.service.lite;

import cn.qbs.wa.teach.course.standard.entity.TWxReport;
import com.baomidou.mybatisplus.extension.service.IService;

public interface TWxReportService extends IService<TWxReport> {

    boolean add(TWxReport params);

    TWxReport page(TWxReport params);

}

