package com.mfml.trader.common.core.regression;

import com.google.common.collect.Lists;
import org.apache.commons.math3.stat.regression.RegressionResults;
import org.apache.commons.math3.stat.regression.SimpleRegression;
import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 数据拟合
 * @author caozhou
 * @date 2023-03-08 10:48
 */
public class Regression {
    /**
     * 数据
     * @param data
     * @return
     */
    public static double[][] linearScatters(List<String> data) {
        List<double[]> rs = Lists.newArrayList();

        List<Double> collect = data.stream().map(e -> Double.valueOf(e)).collect(Collectors.toList());
        // 获取最大、最小值的间隔
        List<Double> sortedCollect = Lists.newArrayList(collect);
        Collections.sort(sortedCollect);

        Double min = sortedCollect.get(0);
        Double distance = Math.abs(sortedCollect.get(0) - sortedCollect.get(sortedCollect.size() - 1));

        for (int idx = 0; idx < collect.size(); idx++) {
            Double curr = collect.get(idx);
             Double dis = curr - min;
            double y = BigDecimal.valueOf(dis).divide(BigDecimal.valueOf(distance), 3, BigDecimal.ROUND_HALF_UP).doubleValue();
            double[] p = {0.1 * idx, y};
            rs.add(p);
        }
        return rs.stream().toArray(double[][]::new);
    }

    /**
     * 拟合
     * @param data
     * @return
     */
    public static Double linearFit(double[][] data) {
        SimpleRegression regression = new SimpleRegression();
        regression.addData(data); // 数据集
        RegressionResults results = regression.regress();
        return results.getParameterEstimate(1);
    }

}
