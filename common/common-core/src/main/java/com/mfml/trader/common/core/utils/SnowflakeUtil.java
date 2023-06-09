package com.mfml.trader.common.core.utils;

import java.util.Random;

/**
 * SnowflakeUtil

 * @Author 曹州
 * @date 2022/07/15
 */
public class SnowflakeUtil {
    /**
     * 起始的时间戳
     * 作者写代码时的时间戳
     */
    private final static long START_STAMP = 1657871045000L;
    /**
     * 可分配的位数
     */
    private final static int REMAIN_BIT_NUM = 22;
    private static final SnowflakeUtil idUtil;

    static {
        Factory factory = new Factory();
        Random random = new Random();
        int idcId = random.nextInt(31);
        int machineId = random.nextInt(31);
        idUtil = factory.create(idcId, machineId);
        System.out.println(String.format("snow flake id util初始化完毕。idcId:%s machineId:%s", idcId, machineId));
    }

    /**
     * idc编号
     */
    private final long idcId;
    /**
     * 机器编号
     */
    private final long machineId;
    /**
     * 当前序列号
     */
    private long sequence = 0L;
    /**
     * 上次最新时间戳
     */
    private long lastStamp = -1L;
    /**
     * idc偏移量：一次计算出，避免重复计算
     */
    private final int idcBitLeftOffset;
    /**
     * 机器id偏移量：一次计算出，避免重复计算
     */
    private final int machineBitLeftOffset;
    /**
     * 时间戳偏移量：一次计算出，避免重复计算
     */
    private final int timestampBitLeftOffset;
    /**
     * 最大序列值：一次计算出，避免重复计算
     */
    private final int maxSequenceValue;

    private SnowflakeUtil(int idcBitNum, int machineBitNum, long idcId, long machineId) {
        int sequenceBitNum = REMAIN_BIT_NUM - idcBitNum - machineBitNum;

        if (idcBitNum <= 0 || machineBitNum <= 0 || sequenceBitNum <= 0) {
            throw new IllegalArgumentException("error bit number");
        }

        this.maxSequenceValue = ~(-1 << sequenceBitNum);

        machineBitLeftOffset = sequenceBitNum;
        idcBitLeftOffset = idcBitNum + sequenceBitNum;
        timestampBitLeftOffset = idcBitNum + machineBitNum + sequenceBitNum;
        this.idcId = idcId;
        this.machineId = machineId;
    }

    public static long nextId() {
        return idUtil._nextId();
    }

    /**
     * 产生下一个ID
     */
    private synchronized long _nextId() {
        long currentStamp = getTimeMill();
        if (currentStamp < lastStamp) {
            throw new RuntimeException(String.format("Clock moved backwards. Refusing to generate id for %d milliseconds", lastStamp - currentStamp));
        }

        //新的毫秒，序列从0开始，否则序列自增
        if (currentStamp == lastStamp) {
            sequence = (sequence + 1) & this.maxSequenceValue;
            if (sequence == 0L) {
                //Twitter源代码中的逻辑是循环，直到下一个毫秒
                lastStamp = tilNextMillis();
                //throw new IllegalStateException("sequence over flow");
            }
        } else {
            sequence = 0L;
        }

        lastStamp = currentStamp;

        return (currentStamp - START_STAMP) << timestampBitLeftOffset | idcId << idcBitLeftOffset | machineId << machineBitLeftOffset | sequence;
    }

    public static void main(String[] args) {
        long id = SnowflakeUtil.nextId();
        System.out.println(Long.toBinaryString(id));
    }

    private long getTimeMill() {
        return System.currentTimeMillis();
    }

    private long tilNextMillis() {
        long timestamp = getTimeMill();
        while (timestamp <= lastStamp) {
            timestamp = getTimeMill();
        }
        return timestamp;
    }

    public static class Factory {
        /**
         * 每一部分占用位数的默认值
         */
        private final static int DEFAULT_MACHINE_BIT_NUM = 5;   //机器标识占用的位数
        private final static int DEFAULT_IDC_BIT_NUM = 5;//数据中心占用的位数

        private final int machineBitNum;
        private final int idcBitNum;

        public Factory() {
            this.idcBitNum = DEFAULT_IDC_BIT_NUM;
            this.machineBitNum = DEFAULT_MACHINE_BIT_NUM;
        }

        public Factory(int machineBitNum, int idcBitNum) {
            this.idcBitNum = idcBitNum;
            this.machineBitNum = machineBitNum;
        }

        public SnowflakeUtil create(long idcId, long machineId) {
            return new SnowflakeUtil(this.idcBitNum, this.machineBitNum, idcId, machineId);
        }
    }
}
