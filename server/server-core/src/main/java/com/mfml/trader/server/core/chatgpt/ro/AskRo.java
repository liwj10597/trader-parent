package com.mfml.trader.server.core.chatgpt.ro;

import com.mfml.trader.common.core.model.base.ToString;
import lombok.Data;

/**
 * @author caozhou
 * @date 2023-03-15 17:26
 */
@Data
public class AskRo extends ToString {
    public String prompt;
    public String model;
}
