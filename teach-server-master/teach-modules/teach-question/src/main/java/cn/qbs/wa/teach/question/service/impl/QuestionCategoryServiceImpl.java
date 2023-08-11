package cn.qbs.wa.teach.question.service.impl;

import cn.qbs.wa.teach.question.entity.QuestionCategory;
import cn.qbs.wa.teach.question.mapper.QuestionCategoryMapper;
import cn.qbs.wa.teach.question.service.QuestionCategoryService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * 试题分类关联(QuestionCategory)表服务实现类
 *
 * @author zcm
 * @version 1.0
 * @date 2021-11-09 10:20:58
 */
@Slf4j
@Service("questionCategoryService")
public class QuestionCategoryServiceImpl extends ServiceImpl<QuestionCategoryMapper, QuestionCategory> implements QuestionCategoryService {


}

