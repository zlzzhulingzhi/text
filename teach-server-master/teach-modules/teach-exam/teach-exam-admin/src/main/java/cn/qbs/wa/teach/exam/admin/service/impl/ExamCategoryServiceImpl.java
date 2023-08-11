package cn.qbs.wa.teach.exam.admin.service.impl;

import cn.qbs.wa.teach.exam.admin.mapper.ExamCategoryMapper;
import cn.qbs.wa.teach.exam.common.entity.ExamCategory;
import cn.qbs.wa.teach.exam.admin.service.ExamCategoryService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * 考试-分类关系表(ExamCategory)表服务实现类
 *
 * @author zcm
 * @since 2021-12-14 11:43:32
 */
@Slf4j
@Service("examCategoryService")
public class ExamCategoryServiceImpl extends ServiceImpl<ExamCategoryMapper, ExamCategory> implements ExamCategoryService {

}

