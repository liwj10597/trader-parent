package com.mfml.trader.common.core.model.base;

import com.alibaba.fastjson.JSONObject;

import java.io.Serializable;

/**
 * 基类，负责toString
 */
public class ToString implements Serializable {
    @Override
    public String toString() {
        return JSONObject.toJSONString(this);
    }
}
