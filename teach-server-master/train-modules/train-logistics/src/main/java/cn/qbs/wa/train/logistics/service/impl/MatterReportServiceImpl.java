package cn.qbs.wa.train.logistics.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.qbs.wa.teach.common.core.context.SecurityContextHolder;
import cn.qbs.wa.train.logistics.entity.MatterReport;
import cn.qbs.wa.train.logistics.entity.MatterReportAttach;
import cn.qbs.wa.train.logistics.enums.FileTypeEnum;
import cn.qbs.wa.train.logistics.mapper.MatterReportMapper;
import cn.qbs.wa.train.logistics.pojo.matter.LiteMatterReportResponse;
import cn.qbs.wa.train.logistics.pojo.matter.MatterReportSaveRequest;
import cn.qbs.wa.train.logistics.service.MatterReportAttachService;
import cn.qbs.wa.train.logistics.service.MatterReportService;
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
@Service("matterReportService")
public class MatterReportServiceImpl extends ServiceImpl<MatterReportMapper, MatterReport> implements MatterReportService {

    @Resource
    private MatterReportAttachService matterReportAttachService;

    @Override
    public MatterReport save(MatterReportSaveRequest params) {
        MatterReport matterReport = BeanUtil.copyProperties(params, MatterReport.class);
        Long orgId = SecurityContextHolder.getOrgId();
        matterReport.setOrgId(orgId);
        boolean save = this.save(matterReport);
        if (save && CollUtil.isNotEmpty(params.getAttachList())) {
            Long id = matterReport.getId();
            List<MatterReportAttach> attachList = params.getAttachList().stream().map(img -> {
                MatterReportAttach attach = new MatterReportAttach();
                attach.setMatterReportId(id);
                attach.setFileType(FileTypeEnum.pic.getCode());
                attach.setFileUrl(img);
                return attach;
            }).collect(Collectors.toList());
            matterReportAttachService.saveBatch(attachList);
        }
        return matterReport;
    }

    @Override
    public LiteMatterReportResponse detail(Long id) {
        LiteMatterReportResponse reportResponse = this.baseMapper.detail(id);
        if (reportResponse != null) {
            List<MatterReportAttach> list = matterReportAttachService.lambdaQuery().eq(MatterReportAttach::getMatterReportId, id).list();
            list.sort(Comparator.comparing(MatterReportAttach::getSort));
            reportResponse.setAttachList(list);
        }
        return reportResponse;
    }

}

