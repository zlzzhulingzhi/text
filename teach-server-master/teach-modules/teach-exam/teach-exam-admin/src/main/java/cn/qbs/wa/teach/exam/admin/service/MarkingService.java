package cn.qbs.wa.teach.exam.admin.service;

import cn.qbs.wa.teach.exam.admin.pojo.exam.*;
import com.baomidou.mybatisplus.core.metadata.IPage;

/**
 * @Author zcm
 * @Date 2022/1/15 16:54
 * @Version 1.0
 */
public interface MarkingService {

    /**
     * 阅卷分页查询
     * @param params
     * @return
     */
    IPage<MarkingPageResponse> markingPage(MarkingPageRequest params);

    /**
     * 阅卷-考试记录分页查询
     * @param params
     * @return
     */
    IPage<MarkingExamRecordPageResponse> examRecordPage(MarkingExamRecordPageRequest params);

    boolean correct(CorrectRequest params);

}
