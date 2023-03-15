package com.mfml.trader.server.core.controller.chatgpt;

import com.mfml.trader.common.core.annotation.ApiScan;
import com.mfml.trader.server.core.chatgpt.ChatGptFacade;
import com.mfml.trader.server.core.chatgpt.ro.AskRo;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import javax.annotation.Resource;

/**
 * @author caozhou
 * @date 2023-03-15 18:05
 */
@Slf4j
@ApiScan
@RestController
public class ChatGptController {

    @Resource
    ChatGptFacade chatGptFacade;

    @ApiOperation(value = "ask", notes = "ask", tags = {"ChatGPT"})
    @PostMapping(value = "ask")
    public String ask(AskRo ro) {
        return chatGptFacade.ask(ro);
    }
}
