package com.neotys.ethereumJ.Web3J;

import com.google.common.base.Optional;
import com.neotys.ethereumJ.common.utils.Whiteblock.data.WhiteblockAccount;
import com.neotys.extensions.action.engine.Context;

public class Web3UtilsWhiteblock extends Web3JUtils {
    public Web3UtilsWhiteblock(String hostname, String port, WhiteblockAccount account,
                               Optional<String> tracemode,Context context) {
        super(new Web3JContext(hostname, port, account,tracemode, context));
    }
}
