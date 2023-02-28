package com.mfml.trader.server;

import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * @author: caozhou
 * @create: 2022-07-16 23:55
 * @description:
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = ServerTestApplication.class)
@ActiveProfiles("local")
public abstract class BaseTest {
}
