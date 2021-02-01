package com.github.lyr2000.common.enums;

import java.time.Duration;

/**
 * @Author lyr
 * @create 2021/2/1 22:34
 */
public enum Unit {
    MILLIS,
    /**
     * second
     */
    SECOND,
    /**
     * 小时
     */
    HOUR,
    /**
     * 分钟
     */
    MINUTE,
    /**
     * 一天
     */
    Day;




    public Duration toDuration(long ttl) {
        if (this==Day) return Duration.ofDays(ttl);
        if (this==SECOND) return Duration.ofSeconds(ttl);
        if (this == HOUR) return Duration.ofHours(ttl);
        if (this == MINUTE) return Duration.ofMinutes(ttl);
        if (this==MILLIS) return Duration.ofMillis(ttl);

        throw new RuntimeException("找不到对应的时间枚举");
    }

}
