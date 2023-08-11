package cn.qbs.wa.teach.question.service.impl;

import cn.qbs.wa.teach.common.core.domain.EnableRequest;
import cn.qbs.wa.teach.common.core.exception.IllegalParamsException;
import cn.qbs.wa.teach.common.core.exception.ServiceException;
import cn.qbs.wa.teach.question.entity.QuestionType;
import cn.qbs.wa.teach.question.mapper.QuestionTypeMapper;
import cn.qbs.wa.teach.question.pojo.SelectOption;
import cn.qbs.wa.teach.question.pojo.question.type.*;
import cn.qbs.wa.teach.question.service.QuestionTypeService;
import cn.qbs.wa.teach.question.util.CacheUtil;
import cn.qbs.wa.teach.question.util.RedisKeyUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RedissonClient;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 题型(QuestionType)表服务实现类
 *
 * @author zcm
 * @version 1.0
 * @date 2021-11-03 13:35:24
 */
@Slf4j
@Service("questionTypeService")
@RequiredArgsConstructor
public class QuestionTypeServiceImpl extends ServiceImpl<QuestionTypeMapper, QuestionType> implements QuestionTypeService {

    private final CacheUtil cacheUtil;

    private final RedissonClient redissonClient;

    @Override
    public Long add(QuestionTypeAddRequest params) {
        String name = params.getName().trim();
        Long count = this.lambdaQuery().eq(QuestionType::getName, name).count();
        if (count > 0) {
            throw new ServiceException("已存在同名题型！");
        }

        QuestionType questionType = new QuestionType();
        BeanUtils.copyProperties(params, questionType);
        this.save(questionType);
        this.updateRedis(questionType);
        return questionType.getId();
    }

    @Override
    public IPage<QuestionTypePageResponse> page(QuestionTypePageRequest params) {
        return baseMapper.page(params.createMpPage(), params);
    }

    @Override
    public QuestionTypeDetailResponse detail(Serializable id) {
        return baseMapper.selectDetailById(id);
    }

    @Override
    public boolean update(QuestionTypeUpdateRequest params) {
        if (params.getId() == null) {
            throw new IllegalParamsException("ID不能为空！");
        }
        String name = params.getName().trim();
        Long count = this.lambdaQuery().eq(QuestionType::getName, name).ne(QuestionType::getId, params.getId()).count();
        if (count > 0) {
            throw new ServiceException("已存在同名题型！");
        }

        QuestionType questionType = new QuestionType();
        BeanUtils.copyProperties(params, questionType);
        boolean success = this.updateById(questionType);
        this.updateRedis(questionType);
        return success;
    }

    @Override
    public boolean deleteByIds(List<Long> idList) {
        redissonClient.getKeys().deleteByPattern(RedisKeyUtil.getQuestionTypePrefix() + "*");
        return this.removeByIds(idList);
    }

    @Override
    public boolean enable(EnableRequest request) {
        redissonClient.getKeys().deleteByPattern(RedisKeyUtil.getQuestionTypePrefix() + "*");
        return this.lambdaUpdate().in(QuestionType::getId, request.getIdList()).set(QuestionType::getEnabled, request.getEnabled()).update();
    }

    @Override
    public List<SelectOption> getSelectOptionList() {
        List<SelectOption> selectOptionList = cacheUtil.getFromBucket(RedisKeyUtil.getQuestionTypeSelectOptionListKey(), () -> {
            List<QuestionType> questionTypeList = this.lambdaQuery()
                    .eq(QuestionType::getEnabled, true)
                    .orderByAsc(QuestionType::getSortNum)
                    .list();
            return questionTypeList.stream().map(qt ->
                    new SelectOption(qt.getId(), qt.getName(), qt.getSortNum())
            ).collect(Collectors.toList());
        });
        return selectOptionList;
    }

    @Override
    public QuestionType getByCache(Long questionTypeId) {
        return cacheUtil.getFromBucket(RedisKeyUtil.getQuestionTypeIdKey(questionTypeId), () -> this.getById(questionTypeId));
    }

    private void updateRedis(QuestionType questionType) {
        if (questionType.getEnabled()) {
            redissonClient.getBucket(RedisKeyUtil.getQuestionTypeIdKey(questionType.getId())).set(questionType);
            redissonClient.getBucket(RedisKeyUtil.getQuestionTypeNameKey(questionType.getName())).set(questionType);
        } else {
            redissonClient.getBucket(RedisKeyUtil.getQuestionTypeIdKey(questionType.getId())).delete();
            redissonClient.getBucket(RedisKeyUtil.getQuestionTypeNameKey(questionType.getName())).delete();
        }
        redissonClient.getBucket(RedisKeyUtil.getQuestionTypeSelectOptionListKey()).delete();
    }

}

