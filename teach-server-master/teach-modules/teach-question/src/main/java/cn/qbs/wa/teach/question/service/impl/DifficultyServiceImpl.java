package cn.qbs.wa.teach.question.service.impl;

import cn.qbs.wa.teach.common.core.domain.EnableRequest;
import cn.qbs.wa.teach.question.entity.Difficulty;
import cn.qbs.wa.teach.question.mapper.DifficultyMapper;
import cn.qbs.wa.teach.question.pojo.SelectOption;
import cn.qbs.wa.teach.question.service.DifficultyService;
import cn.qbs.wa.teach.question.util.CacheUtil;
import cn.qbs.wa.teach.question.util.RedisKeyUtil;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RedissonClient;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 难度(Difficulty)表服务实现类
 *
 * @author zcm
 * @version 1.0
 * @date 2021-11-09 10:42:49
 */
@Slf4j
@Service("difficultyService")
@RequiredArgsConstructor
public class DifficultyServiceImpl extends ServiceImpl<DifficultyMapper, Difficulty> implements DifficultyService {

    private final CacheUtil cacheUtil;

    private final RedissonClient redissonClient;


    @Override
    public boolean enable(EnableRequest request) {
        return this.lambdaUpdate().in(Difficulty::getId, request.getIdList()).set(Difficulty::getEnabled, request.getEnabled()).update();
    }

    @Override
    public List<SelectOption> getSelectOptionList() {
        List<SelectOption> selectOptionList = cacheUtil.getFromBucket(RedisKeyUtil.getDifficultySelectOptionListKey(), () -> {
            List<Difficulty> difficultyList = this.lambdaQuery()
                    .eq(Difficulty::getEnabled, true)
                    .orderByAsc(Difficulty::getSortNum)
                    .list();
            return difficultyList.stream().map(qt ->
                    new SelectOption(qt.getId(), qt.getName(), qt.getSortNum())
            ).collect(Collectors.toList());
        });

        return selectOptionList;
    }

    @Override
    public Difficulty getByCache(Long difficultyId) {
        return cacheUtil.getFromBucket(RedisKeyUtil.getDifficultyIdKey(difficultyId), () -> this.getById(difficultyId));
    }

    private void updateRedis(Difficulty difficulty) {
        if (difficulty.getEnabled()) {
            redissonClient.getBucket(RedisKeyUtil.getDifficultyIdKey(difficulty.getId())).set(difficulty);
            redissonClient.getBucket(RedisKeyUtil.getDifficultyNameKey(difficulty.getName())).set(difficulty);
        } else {
            redissonClient.getBucket(RedisKeyUtil.getDifficultyIdKey(difficulty.getId())).delete();
            redissonClient.getBucket(RedisKeyUtil.getDifficultyNameKey(difficulty.getName())).delete();
        }
        redissonClient.getBucket(RedisKeyUtil.getDifficultySelectOptionListKey()).delete();
    }

}

