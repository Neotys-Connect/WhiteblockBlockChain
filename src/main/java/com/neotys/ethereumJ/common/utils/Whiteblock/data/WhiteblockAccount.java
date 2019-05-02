package com.neotys.ethereumJ.common.utils.Whiteblock.data;

public class WhiteblockAccount {
    String account;
    String balance;
    String txcount;
    String privateKey;
    String publicKey;


    public WhiteblockAccount(String account, String balance, String txcount,String privateKey,String publickey) {
        this.account = account;
        this.balance = balance;
        this.txcount = txcount;
        this.privateKey=privateKey;
        this.publicKey=publickey;
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


    public String getPrivateKey() {
        return privateKey;
    }

    public void setPrivateKey(String privateKey) {
        this.privateKey = privateKey;
    }

    public String getPublicKey() {
        return publicKey;
    }

    public void setPublicKey(String publicKey) {
        this.publicKey = publicKey;
    }
}
