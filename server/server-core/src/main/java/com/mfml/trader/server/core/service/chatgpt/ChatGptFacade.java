package com.mfml.trader.server.core.service.chatgpt;

import com.mfml.trader.server.core.service.chatgpt.ro.AskRo;

/**
 * @author caozhou
 * @date 2023-03-15 17:24
 */
public interface ChatGptFacade {
    Object models();

    Object chatGPT(AskRo ro);
}
