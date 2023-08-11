package cn.qbs.wa.teach.exam.admin.service.impl;

import cn.qbs.wa.teach.common.core.domain.IdListAndUserIdRequest;
import cn.qbs.wa.teach.exam.admin.mapper.OrgDeskTaskExamMapper;
import cn.qbs.wa.teach.exam.admin.pojo.dto.orgdesk.OrgDeskTaskExamInfoDTO;
import cn.qbs.wa.teach.exam.admin.service.*;
import cn.qbs.wa.teach.exam.common.entity.*;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import javax.annotation.Resource;
import java.util.*;

/**
 * 考试表(Exam)表机构前台h5服务实现类
 *
 * @author WX
 * @since 2022-03-16 16:30:51
 */
@Slf4j
@Service("orgDeskTaskExamService")
@RequiredArgsConstructor
public class OrgDeskTaskExamServiceImpl extends ServiceImpl<OrgDeskTaskExamMapper, Exam> implements OrgDeskTaskExamService {

    @Resource
    private OrgDeskTaskExamMapper orgDeskTaskExamMapper;

    @Override
    public List<OrgDeskTaskExamInfoDTO> getExamList(IdListAndUserIdRequest params) {
        return orgDeskTaskExamMapper.getExamList(params);
    }
}

