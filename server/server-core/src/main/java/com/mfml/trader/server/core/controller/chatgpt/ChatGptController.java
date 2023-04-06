package com.mfml.trader.server.core.controller.chatgpt;

import com.mfml.trader.common.core.annotation.ApiScan;
import com.mfml.trader.common.core.result.Result;
import com.mfml.trader.common.core.result.ResultUtil;
import com.mfml.trader.server.core.chatgpt.ChatGptFacade;
import com.mfml.trader.server.core.chatgpt.ChatGptFacadeImpl;
import com.mfml.trader.server.core.chatgpt.ro.AskRo;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import javax.annotation.Resource;
import java.util.List;

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

    @ApiOperation(value = "models", notes = "models", tags = {"ChatGPT"})
    @GetMapping(value = "models")
    public String models() {
        return chatGptFacade.models();
    }

    @ApiOperation(value = "chatGPT", notes = "chatGPT", tags = {"ChatGPT"})
    @PostMapping(value = "chatGPT")
    public String chatGPT(AskRo ro) {
        return chatGptFacade.chatGPT(ro);
    }

    @ApiOperation(value = "addToken", notes = "addToken", tags = {"Console"})
    @PostMapping(value = "addToken")
    public Result<Boolean> addToken(@RequestParam(value = "key") String key) {
        if (StringUtils.isBlank(key)) {
            return ResultUtil.success(true);
        }
        ChatGptFacadeImpl.accessTokens.add(key);
        return ResultUtil.success(true);
    }

    @ApiOperation(value = "getToken", notes = "getToken", tags = {"Console"})
    @PostMapping(value = "getToken")
    public Result<List<String>> getToken() {
        return ResultUtil.success(ChatGptFacadeImpl.accessTokens);
    }

    @ApiOperation(value = "cleanToken", notes = "cleanToken", tags = {"Console"})
    @PostMapping(value = "cleanToken")
    public Result<Boolean> deleteToken(@RequestParam(value = "key") String key) {
        ChatGptFacadeImpl.accessTokens.clear();
        return ResultUtil.success(true);
    }
}
