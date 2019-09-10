package com.neotys.ethereumJ.common.utils.Whiteblock.data;

public class WhiteblockContract {
    String contractAddress;
    String contractName;
    String deployedNodeAddress;

    public WhiteblockContract(String contractAddress, String contractName, String deployedNodeAddress) {
        contractAddress = contractAddress;
        contractName = contractName;
        deployedNodeAddress = deployedNodeAddress;
    }

    public String getContractAddress() {
        return contractAddress;
    }

    public void setContractAddress(String contractAddress) {
        this.contractAddress = contractAddress;
    }

    public String getContractName() {
        return contractName;
    }

    public void setContractName(String contractName) {
        this.contractName = contractName;
    }

    public String getDeployedNodeAddress() {
        return deployedNodeAddress;
    }

    public void setDeployedNodeAddress(String deployedNodeAddress) {
        this.deployedNodeAddress = deployedNodeAddress;
    }
}
