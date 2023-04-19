package com.mfml.trader.server.core.chatgpt.ro;

import com.mfml.trader.common.core.model.base.ToString;
import lombok.Data;

/**
 * @author caozhou
 * @date 2023-04-17 16:45
 */
@Data
public class StreamRo extends ToString {
    private String preResponse;
    private String content;
    private String prompt;
    private Integer maxTokens;
    private Double temperature;
    private String uid;
}
