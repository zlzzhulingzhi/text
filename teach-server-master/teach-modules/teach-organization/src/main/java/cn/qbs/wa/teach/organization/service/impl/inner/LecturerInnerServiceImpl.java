package cn.qbs.wa.teach.organization.service.impl.inner;

import cn.qbs.wa.teach.organization.entity.Lecturer;
import cn.qbs.wa.teach.organization.mapper.LecturerMapper;
import cn.qbs.wa.teach.organization.pojo.lecturer.*;
import cn.qbs.wa.teach.organization.service.inner.LecturerInnerService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import java.io.Serializable;


/**
 * 讲师表(Lecturer)表服务实现类
 *
 * @author makejava
 * @since 2021-11-17 11:25:32
 */
@Slf4j
@Service("lecturerInnerService")
public class LecturerInnerServiceImpl extends ServiceImpl<LecturerMapper, Lecturer> implements LecturerInnerService {

    @Override
    public LecturerDetailResponse detail(Serializable id) {
        LecturerDetailResponse lecturerDetailResponse = baseMapper.selectDetailById(id);
        return lecturerDetailResponse;
    }
}

