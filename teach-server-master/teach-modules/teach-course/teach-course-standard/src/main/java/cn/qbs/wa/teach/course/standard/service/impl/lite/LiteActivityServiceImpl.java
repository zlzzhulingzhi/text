package cn.qbs.wa.teach.course.standard.service.impl.lite;

import cn.qbs.wa.teach.course.standard.mapper.LiteActivityMapper;
import cn.qbs.wa.teach.course.standard.pojo.tactivity.LiteActivityPageRequest;
import cn.qbs.wa.teach.course.standard.pojo.tactivity.LiteActivityPageResponse;
import cn.qbs.wa.teach.course.standard.pojo.tactivity.LiteActivityResponse;
import cn.qbs.wa.teach.course.standard.service.lite.LiteActivityService;
import com.baomidou.mybatisplus.core.metadata.IPage;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service("liteActivityService")
public class LiteActivityServiceImpl implements LiteActivityService {

    @Resource
    private LiteActivityMapper liteActivityMapper;

    @Override
    public IPage<LiteActivityPageResponse> page(LiteActivityPageRequest request) {
        return liteActivityMapper.page(request.createMpPage(), request);
    }

    @Override
    public LiteActivityResponse detail(Long id) {
        return liteActivityMapper.getById(id);
    }
}
