package com.neotys.ethereumJ.common.utils.Whiteblock.data;

public class WhiteblockContract {
    String ContractAddress;
    String ContractName;
    String DeployedNodeAddress;

    public WhiteblockContract(String contractAddress, String contractName, String deployedNodeAddress) {
        ContractAddress = contractAddress;
        ContractName = contractName;
        DeployedNodeAddress = deployedNodeAddress;
    }

    public String getContractAddress() {
        return ContractAddress;
    }

    public void setContractAddress(String contractAddress) {
        ContractAddress = contractAddress;
    }

    public String getContractName() {
        return ContractName;
    }

    public void setContractName(String contractName) {
        ContractName = contractName;
    }

    public String getDeployedNodeAddress() {
        return DeployedNodeAddress;
    }

    public void setDeployedNodeAddress(String deployedNodeAddress) {
        DeployedNodeAddress = deployedNodeAddress;
    }
}
