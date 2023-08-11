package cn.qbs.wa.teach.course.standard.service.lite;

import cn.qbs.wa.teach.course.standard.pojo.tactivity.LiteActivityPageRequest;
import cn.qbs.wa.teach.course.standard.pojo.tactivity.LiteActivityPageResponse;
import cn.qbs.wa.teach.course.standard.pojo.tactivity.LiteActivityResponse;
import com.baomidou.mybatisplus.core.metadata.IPage;

/**
 * 活动小程序业务接口
 */
public interface LiteActivityService {

    IPage<LiteActivityPageResponse> page(LiteActivityPageRequest request);

    LiteActivityResponse detail(Long id);
}
