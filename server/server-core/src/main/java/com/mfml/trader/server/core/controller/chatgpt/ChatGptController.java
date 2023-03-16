package com.mfml.trader.server.core.controller.chatgpt;

import com.mfml.trader.common.core.annotation.ApiScan;
import com.mfml.trader.common.core.result.Result;
import com.mfml.trader.common.core.result.ResultUtil;
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
    public Result<String> ask(AskRo ro) {
        String ask = chatGptFacade.ask(ro);
        return ResultUtil.success(ask);
    }

    @ApiOperation(value = "chatGPT", notes = "chatGPT", tags = {"ChatGPT"})
    @PostMapping(value = "chatGPT")
    public Result<String> chatGPT(AskRo ro) {
        String chatGPT = chatGptFacade.chatGPT(ro);
        return ResultUtil.success(chatGPT);
    }
}
