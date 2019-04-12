package com.neotys.ethereumJ.Web3J;

import static com.neotys.ethereumJ.common.utils.Whiteblock.Constants.WHITEBLOCK_DEFAULT_PASSWORD;
import static com.neotys.ethereumJ.common.utils.Whiteblock.Constants.WHITEBLOCK_RPC_PORT;

public class Web3UtilsWhiteblock extends Web3JUtils {
    public Web3UtilsWhiteblock(String hostname, String walletAdress,  String key) {
        super(hostname, WHITEBLOCK_RPC_PORT, walletAdress, WHITEBLOCK_DEFAULT_PASSWORD, key);
    }
}
