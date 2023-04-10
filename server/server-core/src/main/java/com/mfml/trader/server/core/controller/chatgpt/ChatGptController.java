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
import org.springframework.web.bind.annotation.*;
import javax.annotation.Resource;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
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
    @RequestMapping(value="/models",method = {RequestMethod.POST,RequestMethod.GET})
    @ResponseBody
    public Object models() {
        return chatGptFacade.models();
    }

    @ApiOperation(value = "chatGPT", notes = "chatGPT", tags = {"ChatGPT"})
    @PostMapping(value = "chatGPT")
    @ResponseBody
    public Object chatGPT(@RequestBody AskRo ro) {
        return chatGptFacade.chatGPT(ro);
    }

    @ApiOperation(value = "addToken", notes = "addToken", tags = {"Console"})
    @PostMapping(value = "addToken")
    public Result<Boolean> addToken(@RequestParam(value = "key") String key) {
        if (StringUtils.isBlank(key)) {
            return ResultUtil.success(true);
        }
        try {
            // 写入文件
            FileWriter writer = new FileWriter(new File("/home/work/server/accessTokens"));
            BufferedWriter buffer = new BufferedWriter(writer);
            buffer.write(key);
            buffer.close();
            writer.close();

            // 写入内存
            if (!ChatGptFacadeImpl.accessTokens.contains(key)) {
                ChatGptFacadeImpl.accessTokens.add(key);
            }
        } catch (Exception e) {
            log.warn("addToken warn ", e);
            return ResultUtil.success(false);
        }

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
