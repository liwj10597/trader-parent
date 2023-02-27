package com.mfml.trader.server.launcher.config;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import org.apache.ibatis.reflection.MetaObject;
import org.springframework.stereotype.Component;

import java.util.Date;

/**
 * 自动填充配置类
 * @author: caozhou
 * @data: 2022-10-10 09:48
 *
 */
@Component
public class MyMetaObjectHandler implements MetaObjectHandler {


    @Override  //在执行mybatisPlus的insert()时，为我们自动给某些字段填充值，这样的话，我们就不需要手动给insert()里的实体类赋值了
    public void insertFill(MetaObject metaObject) {
        this.strictInsertFill(metaObject, "createTime", Date.class, new Date());
        this.strictUpdateFill(metaObject, "updateTime", Date.class, new Date());
        //TODO: 创建人
        this.strictInsertFill(metaObject, "createPerson", String.class, "system");
        //TODO: 更新人
        this.strictUpdateFill(metaObject, "updatePerson", String.class, "system");
    }

    @Override//在执行mybatisPlus的update()时，为我们自动给某些字段填充值，这样的话，我们就不需要手动给update()里的实体类赋值了
    public void updateFill(MetaObject metaObject) {
        this.strictUpdateFill(metaObject, "updateTime", Date.class, new Date());
        //TODO: 更新人
        this.strictUpdateFill(metaObject, "updatePerson", String.class, "system");
    }
}
