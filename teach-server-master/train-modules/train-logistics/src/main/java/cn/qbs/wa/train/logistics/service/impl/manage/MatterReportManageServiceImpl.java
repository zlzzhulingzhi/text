package cn.qbs.wa.train.logistics.service.impl.manage;


import cn.qbs.wa.train.logistics.entity.Classroom;
import cn.qbs.wa.train.logistics.entity.MatterReport;
import cn.qbs.wa.train.logistics.entity.MatterReportAttach;
import cn.qbs.wa.train.logistics.mapper.MatterReportMapper;
import cn.qbs.wa.train.logistics.pojo.matter.MatterReportPageRequest;
import cn.qbs.wa.train.logistics.pojo.matter.MatterReportResponse;
import cn.qbs.wa.train.logistics.service.MatterReportAttachService;
import cn.qbs.wa.train.logistics.service.manage.MatterReportManageService;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 报事报修(MatterReport)表服务实现类
 *
 * @author makejava
 * @version 1.0
 * @date 2022-11-24 15:31:34
 */
@Slf4j
@Service("matterReportManageService")
public class MatterReportManageServiceImpl extends ServiceImpl<MatterReportMapper, MatterReport> implements MatterReportManageService {

    @Resource
    private MatterReportAttachService matterReportAttachService;


    @Override
    public List<Classroom> listMatterReportClassroom(Long orgId) {
        return baseMapper.listMatterReportClassroom(orgId);
    }

    @Override
    public IPage<MatterReportResponse> pageMatterReport(MatterReportPageRequest params) {
        return baseMapper.pageMatterReport(params.createMpPage(), params);
    }

    @Override
    public MatterReportResponse manageDetail(Long id) {
        MatterReportResponse reportResponse = this.baseMapper.manageDetail(id);
        if (reportResponse != null) {
            List<MatterReportAttach> list = matterReportAttachService.lambdaQuery().eq(MatterReportAttach::getMatterReportId, id).list();
            list.sort(Comparator.comparing(MatterReportAttach::getSort));
            reportResponse.setAttachList(list);
        }
        return reportResponse;
    }

}

