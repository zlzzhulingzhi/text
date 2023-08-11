package cn.qbs.wa.teach.course.standard.service.lite;


import cn.qbs.wa.teach.course.standard.pojo.report.ReportQo;
import cn.qbs.wa.teach.course.standard.pojo.report.WxCodeRo;

public interface ReportService {

    WxCodeRo createReport(ReportQo qo);
}
