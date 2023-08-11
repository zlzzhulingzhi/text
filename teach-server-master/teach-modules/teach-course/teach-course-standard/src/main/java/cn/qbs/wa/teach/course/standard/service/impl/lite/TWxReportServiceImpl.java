package cn.qbs.wa.teach.course.standard.service.impl.lite;

import cn.qbs.wa.teach.course.standard.entity.TWxReport;
import cn.qbs.wa.teach.course.standard.mapper.TWxReportMapper;
import cn.qbs.wa.teach.course.standard.service.lite.TWxReportService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

@Slf4j
@Service("tWxReportService")
public class TWxReportServiceImpl extends ServiceImpl<TWxReportMapper, TWxReport> implements TWxReportService {

    @Override
    public boolean add(TWxReport params) {
        TWxReport tWxReport = new TWxReport();
        BeanUtils.copyProperties(params, tWxReport);
        return this.save(tWxReport);
    }

    @Override
    public TWxReport page(TWxReport params) {
        return baseMapper.page(params);
    }

}

