package com.neotys.ethereumJ.Web3J;

import com.google.common.base.Optional;
import com.neotys.extensions.action.engine.Context;
import static com.neotys.ethereumJ.common.utils.Whiteblock.Constants.WHITEBLOCK_RPC_PORT;
public class Web3JContext {
    private String ipOftheNode;
    private String accountAdress;
    private Optional<String> tracemode;
    private Context context;
    private String port;
    private String walletpassord;
    private Optional<String> privateKey;
    private Optional<String> publicKey;

    public Web3JContext(String ipOftheNode,String port, String accountAdress,String pwd, Optional<String> tracemode, Optional<String> privateKey, Optional<String> publicKey, Context context) {
        this.ipOftheNode = ipOftheNode;
        this.accountAdress = accountAdress;
        this.tracemode = tracemode;
        this.context = context;
        this.walletpassord=pwd;
        this.privateKey=privateKey;
        this.publicKey=publicKey;
        this.port=port;
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

    public void setIpOftheNode(String ipOftheNode) {
        this.ipOftheNode = ipOftheNode;
    }

    public String getAccountAdress() {
        return accountAdress;
    }

    public void setAccountAdress(String accountAdress) {
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
