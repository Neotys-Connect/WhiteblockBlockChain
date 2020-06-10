package com.neotys.ethereumJ.common.utils.Whiteblock.tools;

import com.neotys.ethereumJ.common.utils.Whiteblock.data.WhiteblockAccount;
import org.json.JSONObject;
import org.web3j.crypto.Keys;

import java.util.ArrayList;
import java.util.List;

public class Ethereum {
    public static List<WhiteblockAccount> generateAccounts(int cnt) throws Exception {
        List<WhiteblockAccount> out = new ArrayList<>(cnt);
        for(int i = 0; i < cnt; i++) {
                out.add(new WhiteblockAccount(Keys.createEcKeyPair()));
        }
        return out;
    }

    public static JSONObject createGenesis(List<WhiteblockAccount> accounts) {
        // TODO: we should eventually make this more customizable
        JSONObject balance = new JSONObject("{\"balance\": \"1000000000000000000000000000\"}");
        JSONObject alloc = new JSONObject();
        accounts.forEach(account -> alloc.put(account.getAccount(),balance));
        JSONObject config = new JSONObject("{\n" +
                "    \"chainId\": 555555,\n" +
                "    \"homesteadBlock\": 0\n" +
                "  }");
        JSONObject genesis = new JSONObject();
        genesis.put("alloc",alloc);
        genesis.put("config", config);
        genesis.put("difficulty","0x400");
        genesis.put("gasLimit","0x2100000");
        return genesis;
    }

}
