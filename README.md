<p align="center"><img src="/screenshots/whiteblock.jpeg" width="40%" alt="whiteblock Logo" /></p>

# BlockChain support for  for NeoLoad

## Overview

These Advanced Actions allows you to generate Blockchain traffic on a blockchain network handled by [Whiteblock](https://www.whiteblock.io/) with [NeoLoad](https://www.neotys.com/neoload/overview) .

This bundle provides custom actions to manage your Whiteblock Network and generate transactions  
### Whiteblock Management
* **BuildWhiteblockNetwork:**
  This action will create a blockchain network on your Whiteblock Environement.
  You will specify the number of Nodes, the type of blockchain network to build your environement.
* **ApplyNetworkConstraints:**
  This action configure the network constraints on your blockChain Network.
  you will be able to apply the following constraints on a specific node number:
  * delay
  * loss
  * bandwidth limitation
  
* **ActivateNetworkConstraints:**
  This action will enable/disable the network constraints on your whiteblock environment.
  
 * **Miner:**
   This action is enable /disble the mining on your whiteblock environment.

* **GetAccountList:**
 This action will list the accounts available in the network.
 This action is required to generate the dataset for your load test.
 Each Virtual user will need it's own account ( account address, private key, public key)
 This Action will also be required to select randomly an available account to send a blockChain transaction

* **GetNodesList:**
  This action will list the number of nodes available in the whiteblock environement.
  This action is required to generate a dataset for you test.
  Each Virtual user will have to select a node to generate RPC calls.

* **GetContractList:**
   This action will list the number of contracts deployed on your whiteblock environement.
   This action is required to generate the dataset for you test. 
   THis action is required for all test generating transaction with a smart contract ( Send ContractTransction, TransfertERC20Token, SafeTransfertERC721Token)

* **GetMonitoringData:**
   This action will is reporting the monitoring information available in Whiteblock in your NeoLoad Web Dashboard.
   THis action will need to be used with one Virtual User iterating on the action during the duration of you test.

### Actions Generation RPC calls on your blockChain network
* **GetBalance:**
  This action will get the balance of a specific account.
  It's generate a RPC call "GetBalance" on you BlockChain Network.

* **SendTransaction:**
  This action is send a RAW transaction on your blockChain Network.
  
* **SendSignedTransaction:**
  This action is send a signed Ether transaction on your blockChain Network.
  
 * **SendFunds:**
   This action is sending Wei from one account to another.
   
* **SendContractTransaction:**
 This action is sending Wei to a smart contract.
   
* **TransfertERC20Token:**
  This action is sending a ERC20 Token to an account through a ERC20 contract.

* **SafeTransfertERC721Token:**
  This action is sending a ERC721 Token to an account through a ERC721 contract.
    
     

## Installation

1. Download the [latest release](https://github.com/Neotyslab/WhiteblockBlockChain/releases/latest) for NeoLoad from version 6.7 or this [release](https://github.com/Neotys-Labs/Dynatrace/releases/tag/Neotys-Labs%2FDynatrace.git-2.0.10) for previous NeoLoad versions.
1. Read the NeoLoad documentation to see [How to install a custom Advanced Action](https://www.neotys.com/documents/doc/neoload/latest/en/html/#25928.htm).

<p align="center"><img src="/screenshots/list_actions.png" alt="Whiteblock BlockChain Advanced Actions" /></p>

## NeoLoad Template Project
1. Download the entire folder [NeoLoad Template Project](https://github.com/Neotyslab/WhiteblockBlockChain/NeoLoad Template Project)
This project will have 2 preconfigured UserPath :
* Management : User that creates the Network and generate the dataset for your test
* Monitoring : The monitoring userpath will be required to report blockchain metrics in NeoLoad WEB.
* Transaction : The transaction userpath is an example on how to the required dataset for your test and send transactions on your blockchain Network.

   
## Parameters for BuildWhiteblockNetwork 
   
| Name             | Description |
| -----            | ----- |
| WhiteBlocMasterHost      | Ip or Hostname of your Whiteblock master Host|
| TypeofBlochacin  |  Type of blockchain network  : Ethereum, Syscoin, Rchain, EOS, Monero, NEM, Fabric, Quorum, or Custom |
| NumberOfNodes  |  number of nodes to create on the blockchain network |
| TraceMode |  optionnal value to enable logs ( debug mode of the custom action) |

## Parameters for  ApplyNetworkConstraints 
   
| Name             | Description |
| -----            | ----- |
| WhiteBlocMasterHost      | Ip or Hostname of your Whiteblock master Host|
| NodeNumber  |  Node numbner to apply the network constraints( 0-n) |
| TypeOfConstraint  |  Type of network constraints to apply on the node .Value Possible :<ul><li>delay</li><li>loss</li><li>bandwidth</li></ul>|
| ConstraintsValue  | Value of the network Constraint |
| ConstraintsValue  | (optionnal) unit of the bandwidth limitation , default value :mbps. Value Possible :<ul><li>kbps</li><li>mbps</li><li>gbps</li></ul>|
| TraceMode |  (optionnal)value to enable logs ( debug mode of the custom action) |
  
## Parameters for ActivateNetworkConstraints
   
| Name             | Description |
| -----            | ----- |
| WhiteBlocMasterHost      | Ip or Hostname of your Whiteblock master Host|
| NetworkMode  |  Enable or disable the network emulation. Value possible :<ul><li>ON : to enable </li><li>OFF : to disable</li></ul> |
| TraceMode |  (optionnal)value to enable logs ( debug mode of the custom action) |


## Parameters for GetAccountList,GetNodeList, GetMonitoring and GetContractList 
   
| Name             | Description |
| -----            | ----- |
| WhiteBlocMasterHost      | Ip or Hostname of your Whiteblock master Host|
| TraceMode |  (optionnal)value to enable logs ( debug mode of the custom action) |


## Parameters for Miner

| Name             | Description |
| -----            | ----- |
| WhiteBlocMasterHost      | Ip or Hostname of your Whiteblock master Host|
| MinerMode  |  Enable or disable the mining. Value possible :<ul><li>ON : to enable </li><li>OFF : to disable</li></ul> |
| TraceMode |  (optionnal)value to enable logs ( debug mode of the custom action) |

## Parameters for GetBalance

| Name             | Description |
| -----            | ----- |
| ipOfTheWhiteblockNode      | private ip of the node used to send the RPC call |
| from      |  account adress to send to use to send the traction from|
| privatekey      |  private key of the account |
| publickey      |  public key of the account  |
| TraceMode |  (optionnal)value to enable logs ( debug mode of the custom action) |

## Parameters for GetTransactionByHash

| Name             | Description |
| -----            | ----- |
| ipOfTheWhiteblockNode      | private ip of the node used to send the RPC call |
| transactionHash      |  Transaction Hash to retrieve|
| TraceMode |  (optionnal)value to enable logs ( debug mode of the custom action) |

## Parameters for SendTransaction

| Name             | Description |
| -----            | ----- |
| ipOfTheWhiteblockNode      | private ip of the node used to send the RPC call |
| from      |  account adress to send to use to send the traction from|
| to | account adress to send the transaction to|
| amount  | value to send |
| TraceMode |  (optionnal)value to enable logs ( debug mode of the custom action) |

## Parameters for SendSignedTransaction and SendFunds

| Name             | Description |
| -----            | ----- |
| ipOfTheWhiteblockNode      | private ip of the node used to send the RPC call |
| from      |  account adress to send to use to send the traction from|
| privatekey      |  private key of the account used to send the transaction|
| publickey      |  public key of the account used to send the transaction |
| to | account adress to send the transaction to|
| amount  | value to send |
| TraceMode |  (optionnal)value to enable logs ( debug mode of the custom action) |

  
## Parameters for SendContractTransaction

| Name             | Description |
| -----            | ----- |
| ipOfTheWhiteblockNode      | private ip of the node used to send the RPC call |
| from      |  account adress to send to use to send the traction from|
| contractadress      |  adress of the smart contract|
| amount  | value to send |
| TraceMode |  (optionnal)value to enable logs ( debug mode of the custom action) |

## Parameters for TransferERC20Token

| Name             | Description |
| -----            | ----- |
| ipOfTheWhiteblockNode      | private ip of the node used to send the RPC call |
| from      |  account adress to send to use to send the traction from|
| to | account adress to send the transaction to|
| contractadress      |  adress of the smart contract|
| amount  | value to send |
| TraceMode |  (optionnal)value to enable logs ( debug mode of the custom action) |


## Parameters for SafeTransfertERC721Token

| Name             | Description |
| -----            | ----- |
| ipOfTheWhiteblockNode      | private ip of the node used to send the RPC call |
| from      |  account adress to send to use to send the traction from|
| to | account adress to send the transaction to|
| contractadress      |  adress of the smart contract|
| tokenid      |  id of the ERC721 token|
| amount  | value to send |
| TraceMode |  (optionnal)value to enable logs ( debug mode of the custom action) |



## Status Codes
  * BuildWhiteblockNetwork actions : 
    * NL-WB_BUILD_ACTION-01: Invalid parameter
    * NL-WB_BUILD_ACTION-02: Technical Error
    * NL-WB_BUILD_ACTION-03 : Bad Context 
  * ApplyNetworkConstraintsAction actions : 
    * NL-WB_NETWORKCONSTRAINT_ACTION-01: Invalid parameter
    * NL-WB_NETWORKCONSTRAINT_ACTION-02: Technical Error
    * NL-WB_NETWORKCONSTRAINT_ACTION-03 : Bad Context
  * ActivateNetworkConstraints actions : 
      * NL-WB_NETWORKACTIVATION_ACTION-01: Invalid parameter
      * NL-WB_NETWORKACTIVATION_ACTION-02: Technical Error
      * NL-WB_NETWORKACTIVATION_ACTION-03 : Bad Context
  * Miner actions : 
      * NL-WB_MINER_ACTION-01: Invalid parameter
      * NL-WB_MINER_ACTION-02: Technical Error
      * NL-WB_MINER_ACTION-03 : Bad Context
 * GetAccountList actions : 
      * NL-WB_ACCOUNT_ACTION-01: Invalid parameter
      * NL-WB_ACCOUNT_ACTION-02: Technical Error
      * NL-WB_ACCOUNT_ACTION-03 : Bad Context
 * GetContractsList actions : 
      * NL-WB_CONTRACTS_ACTION-01: Invalid parameter
      * NL-WB_CONTRACTS_ACTION-02: Technical Error
      * NL-WB_CONTRACTS_ACTION-03 : Bad Context
 * GetNodeList actions : 
      * NL-WB_NODE_ACTION-01: Invalid parameter
      * NL-WB_NODE_ACTION-02: Technical Error
      * NL-WB_NODE_ACTION-03 : Bad Context
 * GetMonitoringData actions : 
      * NL-WB_MONITORING_ACTION-01: Invalid parameter
      * NL-WB_MONITORING_ACTION-02: Technical Error
      * NL-WB_MONITORING_ACTION-03 : Bad Context
 * GetBalance actions : 
      * NL-WB_GETBALANCEACTION_ACTION-01: Invalid parameter
      * NL-WB_GETBALANCEACTION_ACTION-02: Technical Error
      * NL-WB_GETBALANCEACTION_ACTION-03 : Bad Context
 * GetTransactionByHash actions : 
      * NL-WB_GETTRANSACTIONBYHASH_ACTION-01: Invalid parameter
      * NL-WB_GETTRANSACTIONBYHASH_ACTION-02: Technical Error
      * NL-WB_GETTRANSACTIONBYHASH_ACTION-03 : Bad Context
 * SafeTransfertERC721Token actions : 
      * NL-WB_ERC721TOKEN_ACTION-01: Invalid parameter
      * NL-WB_ERC721TOKEN_ACTION-02: Technical Error
      * NL-WB_ERC721TOKEN_ACTION-03 : Bad Context
 * SendContractTransaction actions : 
      * NL-WB_CONTRACTTRANSACTION_ACTION-01: Invalid parameter
      * NL-WB_CONTRACTTRANSACTION_ACTION-02: Technical Error
      * NL-WB_CONTRACTTRANSACTION_ACTION-03 : Bad Context
 * SendFunds actions : 
      * NL-WB_SENDFUNDS_ACTION-01: Invalid parameter
      * NL-WB_SENDFUNDS_ACTION-02: Technical Error
      * NL-WB_SENDFUNDS_ACTION-03 : Bad Context
 * SendSignedTransaction actions : 
      * NL-WB_SIGNEDTRANSACTION_ACTION-01: Invalid parameter
      * NL-WB_SIGNEDTRANSACTION_ACTION-02: Technical Error
      * NL-WB_SIGNEDTRANSACTION_ACTION-03 : Bad Context
 * SendTransaction actions : 
      * NL-WB_TRANSACTION_ACTION-01: Invalid parameter
      * NL-WB_TRANSACTION_ACTION-02: Technical Error
      * NL-WB_TRANSACTION_ACTION-03 : Bad Context
 * TransferERC20Token actions : 
      * NL-WB_ERC20TOKEN_ACTION-01: Invalid parameter
      * NL-WB_ERC20TOKEN_ACTION-02: Technical Error
      * NL-WB_ERC20TOKEN_ACTION-03 : Bad Context
 


| Property | Value |
| -----| -------------- |
| Maturity | Experimental |
| Author   | Neotys Partner Team |
| License  | [BSD Simplified](https://www.neotys.com/documents/legal/bsd-neotys.txt) |
| NeoLoad  | 6.7+ (Enterprise or Professional Edition w/ Integration & Advanced Usage and NeoLoad Web option required)|
| Requirements | NeoLoad Web, Whiteblock Account |
| Bundled in NeoLoad | No
| Download Binaries | <ul><li>[latest release](https://github.com/Neotyslab/TerminalEmulator/releases/latest) is only compatible with NeoLoad from version 6.7</li><li> Use this [release](https://github.com/Neotys-Labs/Dynatrace/releases/tag/Neotys-Labs%2FDynatrace.git-2.0.10) for previous NeoLoad versions</li></ul>|
