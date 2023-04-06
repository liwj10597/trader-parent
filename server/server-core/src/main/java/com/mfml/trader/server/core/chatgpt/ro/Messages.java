package com.mfml.trader.server.core.chatgpt.ro;

import com.mfml.trader.common.core.model.base.ToString;
import lombok.Data;

/**
 * @author caozhou
 * @date 2023-04-06 15:49
 */
@Data
public class Messages extends ToString {
    private String role;
    private String content;
}
