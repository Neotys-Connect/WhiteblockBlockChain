package com.neotys.ethereumJ.common.utils.Whiteblock.rest;


import com.google.common.base.Optional;
import com.neotys.extensions.action.engine.Context;
import com.neotys.extensions.action.engine.Proxy;

public class WhiteblockHttpContext {
    private String bearertoken;
    private Optional<String> tracemode;
    private Context context;
    private Optional<String> proxy;
    public static final String HOST = "https://genesis.whiteblock.io";

    public WhiteblockHttpContext(String bearertoken, Optional<String> tracemode, Context context, Optional<String> proxy) {
        this.bearertoken = bearertoken;
        this.tracemode = tracemode;
        this.context = context;
        this.proxy=proxy;
    }


    public Optional<String> getProxy() {
        return proxy;
    }

    public void setProxy(Optional<String> proxy) {
        this.proxy = proxy;
    }

    public String getWhiteblocMasterHost() {
        return HOST;
    }

    public String getBearerToken() {
        // TODO: properly handle oauth.
        return bearertoken;
    }

    public void setBearerToken(String bearertoken) {
        this.bearertoken = bearertoken;
    }

    public Optional<String> getTracemode() {
        return tracemode;
    }

    public void setTracemode(Optional<String> tracemode) {
        this.tracemode = tracemode;
    }

    public Context getContext() {
        return context;
    }

    public void setContext(Context context) {
        this.context = context;
    }
}



