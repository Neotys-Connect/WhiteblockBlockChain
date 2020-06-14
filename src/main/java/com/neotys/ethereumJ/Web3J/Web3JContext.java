package com.neotys.ethereumJ.Web3J;

import com.google.common.base.Optional;
import com.neotys.extensions.action.engine.Context;

public class Web3JContext {
    private final String ipOftheNode;
    private Optional<String> accountAdress;
    private Optional<String> tracemode;
    private Context context;
    private String port;
    private String walletpassord;
    private Optional<String> privateKey;
    private Optional<String> publicKey;
    private Optional<String> rpctoken;

    public Web3JContext(String ipOftheNode, String port,Optional<String> token, Optional<String> accountAdress,
                        String pwd, Optional<String> tracemode, Optional<String> privateKey, Optional<String> publicKey, Context context) {
        this.ipOftheNode = ipOftheNode;
        this.accountAdress = accountAdress;
        this.tracemode = tracemode;
        this.context = context;
        this.walletpassord=pwd;
        this.privateKey=privateKey;
        this.publicKey=publicKey;
        this.port=port;
        this.rpctoken=token;
    }

    public Web3JContext(String ip, String port, Context context) {
        this.ipOftheNode = ip;
        this.port=port;
        this.context = context;
    }

    public Optional<String> getRpctoken() {
        return rpctoken;
    }

    public void setRpctoken(Optional<String> rpctoken) {
        this.rpctoken = rpctoken;
    }

    public Optional<String> getPrivateKey() {
        return privateKey;
    }

    public void setPrivateKey(Optional<String> privateKey) {
        this.privateKey = privateKey;
    }

    public Optional<String> getPublicKey() {
        return publicKey;
    }

    public void setPublicKey(Optional<String> publicKey) {
        this.publicKey = publicKey;
    }

    public String getWalletpassord() {
        return walletpassord;
    }

    public void setWalletpassord(String walletpassord) {
        this.walletpassord = walletpassord;
    }

    public String getIpOftheNode() {
        return ipOftheNode;
    }

    public Optional<String> getAccountAdress() {
        return accountAdress;
    }

    public void setAccountAdress( Optional<String> accountAdress) {
        this.accountAdress = accountAdress;
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
