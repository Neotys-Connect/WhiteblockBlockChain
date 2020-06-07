package com.neotys.ethereumJ.common.utils.Whiteblock.rpc;

import com.google.common.base.Optional;
import com.neotys.extensions.action.engine.Context;

public class WhiteblockHttpContext {
    private String whiteblocMasterHost;
    private String rpctoken;
    private Optional<String> tracemode;
    private Context context;
    private String rpcport;
    private Optional<String> proxy;
    public WhiteblockHttpContext(String whiteblocMasterHost, String rpctoken, Optional<String> tracemode,
                                 Context context, String rpcport,Optional<String> proxy) {
        this.whiteblocMasterHost = whiteblocMasterHost;
        this.rpctoken = rpctoken;
        this.tracemode = tracemode;
        this.context = context;
        this.rpcport = rpcport;
        this.proxy=proxy;
    }


    public Optional<String> getProxy() {
        return proxy;
    }

    public void setProxy(Optional<String> proxy) {
        this.proxy = proxy;
    }

    public String getWhiteblocMasterHost() {
        return whiteblocMasterHost;
    }

    public void setWhiteblocMasterHost(String whiteblocMasterHost) {
        this.whiteblocMasterHost = whiteblocMasterHost;
    }

    public String getRpctoken() {
        return rpctoken;
    }

    public void setRpctoken(String rpctoken) {
        this.rpctoken = rpctoken;
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

    public String getRpcport() {
        return rpcport;
    }

    public void setRpcport(String rpcport) {
        this.rpcport = rpcport;
    }
}



