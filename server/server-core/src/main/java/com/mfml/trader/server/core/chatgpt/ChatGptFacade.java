package com.mfml.trader.server.core.chatgpt;

import com.mfml.trader.server.core.chatgpt.ro.AskRo;

/**
 * @author caozhou
 * @date 2023-03-15 17:24
 */
public interface ChatGptFacade {

    String chatGPT(AskRo ro);
}
