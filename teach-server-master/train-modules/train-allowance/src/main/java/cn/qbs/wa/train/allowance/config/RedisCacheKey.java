package cn.qbs.wa.train.allowance.config;

public class RedisCacheKey {

    private RedisCacheKey() {

    }

    /**
     * 前缀
     */
    private static final String PREFIX = "apply:";

    /**
     * 教室宿舍申请
     */
    public static final String LOCK_SETTLE_PREFIX = PREFIX + "settle:";

    /**
     * 资助申请
     */
    public static final String LOCK_ALLOWANCE_PREFIX = PREFIX + "allowance:";

    /**
     * 资质申请
     */
    public static final String LOCK_QUALIFICATION_PREFIX = PREFIX + "qualification:";

    public static String getLockKey(Long applyId) {
        return PREFIX + applyId;
    }

    public static String getSettleLockKey(Long orgId) {
        return LOCK_SETTLE_PREFIX + orgId;
    }

    public static String getAllowanceLockKey(Long orgId) {
        return LOCK_ALLOWANCE_PREFIX + orgId;
    }

    public static String getQualificationLockKey(Long orgId) {
        return LOCK_ALLOWANCE_PREFIX + orgId;
    }
}
