package com.mfml.trader.server.core.client;

import com.alipay.api.AlipayClient;
import com.alipay.api.AlipayConfig;
import com.alipay.api.DefaultAlipayClient;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.Map;

/**
 * @author caozhou
 * @date 2023-04-27 10:06
 */
@Slf4j
@Component
public class PayClient {
    private static Map<String, AlipayClient> clients = Maps.newHashMap();

    /**
     * 初始化客户端
     */
    @PostConstruct
    void initAli() {
        initAlipayBT();
        initAlipayBQ();
    }


    void initAlipayBT() {
        try {
            AlipayConfig config = new AlipayConfig();
            // 支付宝网关
            config.setServerUrl("https://openapi.alipay.com/gateway.do");
            // appid
            config.setAppId("2021003192670008");
            // 开发者私钥
            config.setPrivateKey("MIIEvgIBADANBgkqhkiG9w0BAQEFAASCBKgwggSkAgEAAoIBAQCRIyhUJ4g45wH6oGkp7Lf4hnawaYxQjEwwdYfVm0nQ0rQ4+Kldg3N7ulL9R2Q0OAARcD0Kt8dD35PeT5rlwsMHH6sy3VtbycmxGLlOTUOUXeR7HyxPhxwk4dJ8OtabnW7hzHWVZbYmjEUWU3NS9MHqOpwJPYQXgLvy9b6C9yCENBdgCMqZeBoVFb/Bmf2cgv76roAZ3q0GDPv5yiRp3yuZDZu6fas+J3KHjGcy3FkRhYxlTNL87ghopUMa1NOT8RQjaMwSPBLTH3DjdKah57s0IOCG4nUw6m4pbgER+wInpGEocBxcNpNXsF8dEcI5LWdV1eRbkKfVwVg9jF0JJOPzAgMBAAECggEBAILBfNzMPMKfBD5qdDQsKtZBNf7bKGmM7DsgN78uxwLWQT2ucM9p1QncJFZBEjqlVRJg3u2gAmrYq1w2taNXouLxGgiaOmtoUUg2sQOlQkQRjv2JaR9A5w8nT71SOPQ7kqDk1e5BkygJaVii2Bwdm/JM2TTN8gpGlY1e7kMJJ4dSFv99SnNQpoGQH8T5dKiCK3tR2T5s/Jsu2RyQu7W+tsFksU2WD3rKMgWtIW43TDz61J7VeHyE+cKgWLKYHrtkUaLHqc+AWiAODjs/FlJ7VfCR3/jhSGzVhUk5LSdjctzsF3TwK9JI7nhPfm3xV79RUW/F7t+WuW8toYaHCQj++hkCgYEAy7fMysXWM5vI+yA6nIAb+mz1sKBUOxHseWOdEjtqsnnopbBib1KrcTVsrozEeAyQUAc8LrZcPFKW4OPMqwPV3+cOHtL2e5L7hPPrl6FvHdeD+//wvSvcaVyYg4HdpEff1HOGan5LVig8VLeuo6TZb2M3CAMklGo8Erjzvv6GSl0CgYEAtmKhUbBRE7VjJwpEpoyfeGILtX0ukIdzs0R32G45c6Uh42hAu/u+wYdEdmwEH30bjrtw3pwgVg/8TmMzoeCEqgW1YknqY25txKOJI3XKlOPVwTD0fywZXXXMiQ4FIxQnRBlPerIlP8FlscqmvOqdbDvu8eWB2+Mfvam1WBU1Io8CgYEAmEhXACUPUBjeDkszRRXG1FZq+pN/I4DsgnliSuS7h5r1vBW0H39uxPuouHply0+o+QnPhSOwoy8htg7TSeteDzeLKKOOuSVQezxTGlAE3gvij5fs/5wv8VrUb0m+wzUvLoMFMcoiys7c+n8sPwvVthw2YPjoeUFIkr5LBrUA8ckCgYBK+OhPaCcr7tg/aB0oSY4UMlmV1Uua5dNN0ctiEI1VWIBBts1FeQrFCxG9jFF1kDwPxEV1fMBeIFwa7PSAioZ+TbJuIOxUy0QcHQ12oikQY0evDqyyyjHzFUVq4XSDIvmxKKZfTXO6bYmcOC2qYtcrcseacDmdZhPaJzUAbFdXSQKBgAshJfr3U327i98dzo+57ouX2sJgCOlrkkB5BVMY++moJJn5ZH6f4FamJivREtWaxWEqNyNk63iG6FGEC5F4DmEhLcH2bfOTpR/lYgsp62VqevAm+lm0ekmosjDI70Klf8eHDgjYaqRJdSxHN5RxDb1EGDCf1QXw2waWXF2uNidJ");
            // 参数返回格式
            config.setFormat("json");
            // 编码集
            config.setCharset("UTF-8");
            // 支付宝公钥
            config.setAlipayPublicKey("MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAgfXXqY+JnxNEjhJuS4WYYsIi346qFiFpXc58sORoIPH2oAWprNFa9dZnmef3h6j2QtGnSW3QlsomCqQ5yFs9eHm5X+tKGT1yqvAM26al/59Ql5rC44xo1PM3W3fXWuOH9w1IKfv1h7HxqsAQR8bJVyDbf/r13hKvt/noU0OMg26n9PmiM+zcjrSBgiB8rU0IoJfsqhHNglHktsgUtu42gTgKovnzDrTsmGwP3jxWvQTBTXjikRlYMr67KJugs6xUERYk367UDCQ8c9fgIfDA/cqq/GJfMHmaTyf39pLVueDKHB/jFVsDaUusTZPoBIJ01rhK0UuT+H4ubCI7U1SaZQIDAQAB");
            config.setSignType("RSA2");
            clients.put("AlipayBT", new DefaultAlipayClient(config));
        } catch (Exception e) {
            log.warn("init alipayClient error", e);
        }
    }

    void initAlipayBQ() {}


    /**
     * 获取客户端
     * @return
     */
    public AlipayClient getClientBT() {
        AlipayClient alipayClient = clients.get("AlipayBT");

        if (alipayClient == null) {
            synchronized (PayClient.class) {
                if (alipayClient == null) {
                    initAlipayBT();
                }
            }
        }
        return alipayClient;
    }

    /**
     * 获取客户端
     * @return
     */
    public AlipayClient getClientBQ() {
        AlipayClient alipayClient = clients.get("AlipayBQ");
        if (alipayClient == null) {
            synchronized (PayClient.class) {
                if (alipayClient == null) {
                    initAlipayBT();
                }
            }
        }
        return alipayClient;
    }
}
