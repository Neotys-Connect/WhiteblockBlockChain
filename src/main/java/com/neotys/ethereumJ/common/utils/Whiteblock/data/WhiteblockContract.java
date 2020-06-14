package com.neotys.ethereumJ.common.utils.Whiteblock.data;

public class WhiteblockContract {
    String contractAddress;
    String contractName;
    String deployedNodeAddress;

    public WhiteblockContract(String contractAddress, String contractName, String deployedNodeAddress) {
        this.contractAddress = contractAddress;
        this.contractName = contractName;
        this.deployedNodeAddress = deployedNodeAddress;
    }

    public String getContractAddress() {
        return contractAddress;
    }

    public String getContractName() {
        return contractName;
    }

    public String getDeployedNodeAddress() {
        return deployedNodeAddress;
    }
}
