package com.itl.im.provider.util;

import org.springframework.beans.factory.annotation.Value;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * 雪花算法工具类
 *
 * @author tanq
 * @date 2020-09-25
 * @since jdk1.8
 */
public class SnowIdUtil {
    @Value("${sys.snowflake.work.id}")
    private transient static long workerId;
    @Value("${sys.snowflake.data.center.id}")
    private transient static long dataCenterId;

    private static long sequence = 0L;
    private static final long ORIGIN = 1525500000000L;
    private static final long WORKER_ID_BITS = 5L;
    private static final long DATA_CENTER_ID_BITS = 5L;
    private static final long SEQUENCE_BITS = 12L;
    private static final long WORKER_ID_SHIFT = SEQUENCE_BITS;
    private static final long DATA_CENTER_ID_SHIFT = SEQUENCE_BITS + WORKER_ID_BITS;
    private static final long TIMESTAMP_LEFT_SHIFT = SEQUENCE_BITS + WORKER_ID_BITS + DATA_CENTER_ID_BITS;
    private static final long SEQUENCE_MASK = ~(-1L << SEQUENCE_BITS);
    private transient static long lastTimestamp = -1L;

    /**
     * 生成新的雪花算法的ID
     *
     * @return
     */
    public synchronized static String getLongId() {
        SnowIdUtil snowIdUtil = new SnowIdUtil();
        return snowIdUtil.getStringId();
    }

    private String getStringId() {
        long timestamp = System.currentTimeMillis();

        if (lastTimestamp == timestamp) {
            sequence = (sequence + 1) & SEQUENCE_MASK;
            if (sequence == 0) {
                timestamp = tilNextMillis(lastTimestamp);
            }
        } else {
            sequence = 0L;
        }
        lastTimestamp = timestamp;
        long id = ((timestamp - ORIGIN) << TIMESTAMP_LEFT_SHIFT) | (dataCenterId << DATA_CENTER_ID_SHIFT) | (workerId << WORKER_ID_SHIFT) | sequence;
        return String.valueOf(id);
    }

    private long tilNextMillis(long lastTimestamp) {
        long timestamp = System.currentTimeMillis();
        while (timestamp <= lastTimestamp) {
            timestamp = System.currentTimeMillis();
        }
        return timestamp;
    }

}
