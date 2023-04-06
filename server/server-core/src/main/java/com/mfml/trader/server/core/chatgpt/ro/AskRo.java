package com.mfml.trader.server.core.chatgpt.ro;

import com.mfml.trader.common.core.model.base.ToString;
import lombok.Data;

import java.util.List;

/**
 * @author caozhou
 * @date 2023-03-15 17:26
 */
@Data
public class AskRo extends ToString {
    private String model;
    private List<Messages> messages;
    private String prompt;
    private Integer max_token;
    private Double temperature;
    private Boolean stream;
}
