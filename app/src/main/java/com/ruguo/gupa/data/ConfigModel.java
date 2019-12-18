package com.ruguo.gupa.data;

import cn.leancloud.AVObject;
import cn.leancloud.annotation.AVClassName;

@AVClassName("app_config")
public class ConfigModel extends AVObject {

    private String version;

    private String script_version;

    private String apk_url;

    private String content;

    public String getContent() {
        return getString("content");
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getVersion() {
        return getString("version");
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getScript_version() {
        return getString("script_version");
    }

    public void setScript_version(String script_version) {
        this.script_version = script_version;
    }

    public String getApk_url() {
        return getString("apk_url");
    }

    public void setApk_url(String apk_url) {
        this.apk_url = apk_url;
    }
}
