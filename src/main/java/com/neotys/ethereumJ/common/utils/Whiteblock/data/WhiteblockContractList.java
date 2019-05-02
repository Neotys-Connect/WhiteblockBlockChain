package com.neotys.ethereumJ.common.utils.Whiteblock.data;

import java.util.List;

public class WhiteblockContractList extends WhiteblockDataModel {
    //[
    //  {
    //    "ContractAddress": "0xDab5dA2210F58fEe2Dd43405C5d7E3974e
    //b8caF7",
    //    "ContractName": "helloworld.sol",
    //    "DeployedNodeAddress": "0x824809e406A29c40E4eF50777B361b
    //83d5c0704e"
    //  }
    //]
    List<WhiteblockContract> whiteblockContractList;

    public WhiteblockContractList(List<WhiteblockContract> whiteblockContractList) {
        this.whiteblockContractList = whiteblockContractList;
    }

    public List<WhiteblockContract> getWhiteblockContractList() {
        return whiteblockContractList;
    }

    public void setWhiteblockContractList(List<WhiteblockContract> whiteblockContractList) {
        this.whiteblockContractList = whiteblockContractList;
    }
}
