package com.mfml.trader.server.core.controller.chatgpt;

import com.mfml.trader.common.core.result.Result;
import com.mfml.trader.server.core.service.chatgpt.EmbeddingFacade;
import com.mfml.trader.server.core.service.chatgpt.ro.EmbeddingRo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import javax.annotation.Resource;
import java.util.List;

/**
 * @author caozhou
 * @date 2023-05-04 18:03
 */
@Slf4j
@RestController
public class EmbeddingController {
    @Resource
    EmbeddingFacade embeddingFacade;

    @PostMapping(value = "embeddings")
    @ResponseBody
    public Result<List<String>> embeddings(@RequestBody EmbeddingRo ro) {
        return embeddingFacade.embeddings(ro);
    }
}
