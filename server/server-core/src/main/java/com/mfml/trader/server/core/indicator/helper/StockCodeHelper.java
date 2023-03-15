package com.mfml.trader.server.core.indicator.helper;

import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;
import org.springframework.util.ResourceUtils;

import javax.annotation.PostConstruct;
import java.io.*;
import java.util.List;

/**
 * @author caozhou
 * @date 2023-03-15 09:49
 */
@Slf4j
@Component
public class StockCodeHelper {

    public static final List<String> stockCode = Lists.newArrayList();

    @PostConstruct
    public void init() {
        try {
            File file = ResourceUtils.getFile("classpath:stock_code.txt");
            InputStream input = new FileInputStream(file);
            BufferedReader reader = new BufferedReader(new InputStreamReader(input));
            String line = "";
            while( (line =reader.readLine()) != null) {
                if (StringUtils.isNotBlank(line)) {
                    stockCode.add(line.trim());
                }
            }
            reader.close();
        } catch (Exception e) {
            log.warn("init stock_code warn", e);
        }
    }
}
