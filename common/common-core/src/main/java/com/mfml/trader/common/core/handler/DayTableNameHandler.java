package com.mfml.trader.common.core.handler;

import cn.hutool.core.date.DateUtil;
import com.baomidou.mybatisplus.extension.plugins.handler.TableNameHandler;
import org.apache.commons.lang3.StringUtils;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

/**
 * @author caozhou
 * @date 2022-11-25 19:45
 */
public class DayTableNameHandler implements TableNameHandler {
    //用于记录哪些表可以使用该月份动态表名处理器（即哪些表按日分表）
    private List<String> tableNames;

    //构造函数，构造动态表名处理器的时候，传递tableNames参数
    public DayTableNameHandler(String ...tableNames) {
        this.tableNames = Arrays.asList(tableNames);
    }

    //每个请求线程维护一个date数据，避免多线程数据冲突。所以使用ThreadLocal
    private static final ThreadLocal<String> DAY_DATA = new ThreadLocal<>();

    //设置请求线程的month数据
    public static void setData(String date) {
        DAY_DATA.set(date);
    }
    //删除当前请求线程的month数据
    public static void removeData() {
        DAY_DATA.remove();
    }

    //动态表名接口实现方法
    @Override
    public String dynamicTableName(String sql, String tableName) {
        String suffix = DAY_DATA.get();
        if (this.tableNames.contains(tableName) && StringUtils.isNotEmpty(suffix)){
            return tableName + "_" + suffix;  //表名增加日期后缀
        } else {
            return tableName;   //表名原样返回
        }
    }

    /**
     * 根据订单号生成动态表的后缀
     * @param orderNo
     * @return
     */
    public static String generateShardingKey(String orderNo) {
        return orderNo.substring(3, 9);
    }

    /**
     * 根据日期生成动态表的后缀
     */
    public static String generateShardingKey(Date date) {
        return DateUtil.format(date, "yyMMdd");
    }
}
