package com.github.wxpay.sdk;

import java.io.InputStream;

public class Config extends  WXPayConfig{
    @Override
    public String getAppID() {
        return "wx8397f8696b538317";
    }

    @Override
    public String getMchID() {
        return "1473426802";
    }

    @Override
    public String getKey() {
        return "T6m9iK73b0kn9g5v426MKfHQH7X8rKwb";
    }

    @Override
    public String getNotifyUrl() {
        return "api.mch.weixin.qq.com/aa/bb";
    }

    @Override
    public InputStream getCertStream() {
        return null;
    }
}
