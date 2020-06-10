package com.neotys.ethereumJ.common.utils.Whiteblock.data;

import org.web3j.crypto.Credentials;
import org.web3j.crypto.ECKeyPair;

public class WhiteblockAccount {
    Credentials cred;

    public WhiteblockAccount(String privateKey) {
        this.cred = Credentials.create(privateKey);
    }

    public WhiteblockAccount(ECKeyPair kp) {
        this.cred = Credentials.create(kp);
    }

    public String getAccount() {
        return cred.getAddress();
    }

    public String getPrivateKey() {
        return cred.getEcKeyPair().getPrivateKey().toString(16);
    }

    public String getPublicKey() {
        return cred.getEcKeyPair().getPublicKey().toString(16);
    }
}