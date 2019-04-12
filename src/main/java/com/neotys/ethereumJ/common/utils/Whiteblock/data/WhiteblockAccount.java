package com.neotys.ethereumJ.common.utils.Whiteblock.data;

public class WhiteblockAccount {
    String account;
    String balance;
    String txcount;
    String keystore=null;



    public WhiteblockAccount(String account, String balance, String txcount) {
        this.account = account;
        this.balance = balance;
        this.txcount = txcount;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public String getBalance() {
        return balance;
    }

    public void setBalance(String balance) {
        this.balance = balance;
    }

    public String getTxcount() {
        return txcount;
    }

    public void setTxcount(String txcount) {
        this.txcount = txcount;
    }

    public String getKeystore() {
        return keystore;
    }

    public void setKeystore(String keystore) {
        this.keystore = keystore;
    }
}
