package com.mfml.trader.server.core.service.chatgpt.ro;

import com.mfml.trader.common.core.model.base.ToString;
import lombok.Data;

/**
 * @author caozhou
 * @date 2023-05-04 15:15
 */
@Data
public class EmbeddingRo extends ToString {
    private String input;
}
