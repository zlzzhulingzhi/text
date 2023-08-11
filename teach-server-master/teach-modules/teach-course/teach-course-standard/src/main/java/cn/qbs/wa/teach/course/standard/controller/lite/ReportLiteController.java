package cn.qbs.wa.teach.course.standard.controller.lite;

import cn.qbs.wa.teach.common.core.domain.R;
import cn.qbs.wa.teach.course.standard.pojo.report.ReportQo;
import cn.qbs.wa.teach.course.standard.pojo.report.WxCodeRo;
import cn.qbs.wa.teach.course.standard.service.lite.ReportService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/lite/reporter")
@Api(tags = "海报管理")
@Slf4j
public class ReportLiteController {

    @Autowired
    private ReportService reportService;

    @ApiOperation(value = "小程序二维码")
    @PostMapping("/create")
    public R<WxCodeRo> createReportCode(@RequestBody ReportQo qo) {
        return R.ok(this.reportService.createReport(qo));
    }


}