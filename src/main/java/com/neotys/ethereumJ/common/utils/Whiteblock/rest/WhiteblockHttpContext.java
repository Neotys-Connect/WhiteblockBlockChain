package com.neotys.ethereumJ.common.utils.Whiteblock.rest;


import com.google.common.base.Optional;
import com.neotys.extensions.action.engine.Context;

public class WhiteblockHttpContext {
    private String bearerToken;
    private Optional<String> tracemode;
    private Context context;
    private Optional<String> proxy;
    public static final String HOST = "https://infra.whiteblock.io";

    public WhiteblockHttpContext(String bearerToken, Optional<String> tracemode, Context context, Optional<String> proxy) {
        this.bearerToken = bearerToken;
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

    public String getBearerToken() {
        // TODO: properly handle oauth.
        return bearerToken;
    }

    public void setBearerToken(String bearertoken) {
        this.bearerToken = bearertoken;
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


    public boolean isTraceModeActive() {
        return this.getTracemode().isPresent() && this.getTracemode().get().equalsIgnoreCase("TRUE");
    }
}



