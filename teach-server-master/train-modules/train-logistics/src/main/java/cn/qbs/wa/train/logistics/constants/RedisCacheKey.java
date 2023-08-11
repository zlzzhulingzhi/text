package cn.qbs.wa.train.logistics.constants;

public class RedisCacheKey {

    private RedisCacheKey() {

    }

    /**
     * 前缀
     */
    public static final String PREFIX = "logistic:";

    /**
     * 学员签到
     */
    public static final String LOCK_MEMBER_SIGN_IN_PREFIX = PREFIX + "signIn:member:";

    /**
     * 班级批量签到
     */
    public static final String LOCK_CLASS_SIGN_IN_PREFIX = PREFIX + "signIn:class:";

    /**
     * 学员签到锁key
     * @param clazzId 班级ID
     * @param memberId 学员用户ID
     * @return 锁key
     */
    public static String getMemberSignInLockKey(Long clazzId, Long memberId) {
        return LOCK_MEMBER_SIGN_IN_PREFIX + clazzId + ":" + memberId;
    }

    /**
     * 学员批量签到锁key
     * @param clazzId 班级ID
     * @return 锁key
     */
    public static String getMemberSignInLockKey(Long clazzId) {
        return LOCK_CLASS_SIGN_IN_PREFIX + clazzId;
    }
}
