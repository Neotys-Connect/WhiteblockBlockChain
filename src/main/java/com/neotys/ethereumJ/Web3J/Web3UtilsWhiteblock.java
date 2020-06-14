package com.neotys.ethereumJ.Web3J;

import com.google.common.base.Optional;
import com.neotys.extensions.action.engine.Context;

import static com.neotys.ethereumJ.common.utils.Whiteblock.Constants.WHITEBLOCK_DEFAULT_PASSWORD;

public class Web3UtilsWhiteblock extends Web3JUtils {
    public Web3UtilsWhiteblock(String hostname,String RPC, Optional<String> token,Optional<String> walletAdress, Optional<String> privatekey, Optional<String> publick, Optional<String> tracemode,Context context) {
        super(new Web3JContext(hostname, RPC,token, walletAdress, WHITEBLOCK_DEFAULT_PASSWORD,tracemode, privatekey,publick,context));
    }
}
