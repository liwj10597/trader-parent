package com.mfml.trader.server.core.service.chatgpt;

import com.mfml.trader.common.core.result.Result;
import com.mfml.trader.server.core.service.chatgpt.ro.EmbeddingRo;

import java.util.List;

/**
 * @author caozhou
 * @date 2023-05-04 15:14
 */
public interface EmbeddingFacade {
    /**
     * embeddings接口
     * 
     * @param ro
     * @return
     */
    Result<List<String>> embeddings(EmbeddingRo ro);
}
