package com.neotys.ethereumJ.Web3J;

import com.google.common.base.Optional;
import com.neotys.ethereumJ.common.utils.Whiteblock.data.WhiteblockAccount;
import com.neotys.extensions.action.engine.Context;

import static com.neotys.ethereumJ.common.utils.Whiteblock.Constants.WHITEBLOCK_DEFAULT_PASSWORD;

public class Web3JContext {
    private final String ip;
    private Optional<String> tracemode;
    private Context context;
    private String port;
    private WhiteblockAccount account;

    public Web3JContext(String ip, String port, WhiteblockAccount account,
                        Optional<String> tracemode, Context context) {
        this.ip = ip;
        this.account = account;
        this.tracemode = tracemode;
        this.context = context;
        this.port = port;
    }

    public Web3JContext(String ip, String port, Context context) {
        this.ip = ip;
        this.port=port;
        this.context = context;
    }


    public String getPrivateKey() {
        return account.getPrivateKey();
    }
    public String getPublicKey() {
        return account.getPublicKey();
    }

    public String getWalletpassord() {
        return WHITEBLOCK_DEFAULT_PASSWORD;
    }

    public String getIpOftheNode() {
        return ip;
    }

    public String getAccountAddress() {
        return account.getAccount();
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

    public String getPort() {
        return port;
    }

    public void setPort(String port) {
        this.port = port;
    }
}
