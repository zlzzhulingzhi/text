package cn.qbs.wa.train.logistics.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.qbs.wa.teach.common.core.exception.IllegalParamsException;
import cn.qbs.wa.train.logistics.entity.WLecturer;
import cn.qbs.wa.train.logistics.mapper.LecturerMapper;
import cn.qbs.wa.train.logistics.mapper.WLecturerMapper;
import cn.qbs.wa.train.logistics.pojo.lecturer.LecturerListRequest;
import cn.qbs.wa.train.logistics.pojo.lecturer.LecturerListResponse;
import cn.qbs.wa.train.logistics.pojo.wlecturer.*;
import cn.qbs.wa.train.logistics.service.WLecturerService;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 插件-讲师表(WLecturer)表服务实现类
 *
 * @author makejava
 * @since 2022-03-01 13:44:49
 */
@Slf4j
@Service("wLecturerService")
public class WLecturerServiceImpl extends ServiceImpl<WLecturerMapper, WLecturer> implements WLecturerService {

    @Autowired
    LecturerMapper lecturerMapper;

    @Override
    public boolean add(WLecturerAddRequest params) {
        List<Long> idList = params.getIdList();
        List<WLecturer> wLecturers = listByIds(idList);
        if (CollUtil.isNotEmpty(wLecturers)) {
            idList.removeAll(wLecturers.stream().map(WLecturer::getId).collect(Collectors.toList()));
        }
        if (CollUtil.isNotEmpty(idList)) {
            LecturerListRequest lecturerListRequest = new LecturerListRequest();
            lecturerListRequest.setIdList(idList);
            List<LecturerListResponse> list = lecturerMapper.listLectureWithOutOrgId(lecturerListRequest);
            List<WLecturer> lecturerList = list.stream().map(i -> {
                WLecturer wLecturer = new WLecturer();
                wLecturer.setId(i.getId());
                wLecturer.setOrgId(i.getOrgId());
                return wLecturer;
            }).collect(Collectors.toList());
            saveBatch(lecturerList);
        }
        return true;
    }

    @Override
    public IPage<WLecturerPageResponse> page(WLecturerPageRequest params) {
        return baseMapper.page(params.createMpPage(), params);
    }

    @Override
    public WLecturerDetailResponse detail(Serializable id) {
        return baseMapper.selectDetailById(id);
    }

    @Override
    public boolean update(WLecturerUpdateRequest params) {
        if (params.getId() == null) {
            throw new IllegalParamsException("ID不能为空！");
        }
        WLecturer wLecturer = new WLecturer();
        BeanUtils.copyProperties(params, wLecturer);
        //清除默认传的orgId
        wLecturer.setOrgId(null);
        return this.updateById(wLecturer);
    }

    @Override
    public boolean deleteByIds(List<Long> idList) {
        return this.removeByIds(idList);
    }

}

