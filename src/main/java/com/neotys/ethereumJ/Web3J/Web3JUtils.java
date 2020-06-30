package com.neotys.ethereumJ.Web3J;

import com.neotys.ethereumJ.common.utils.Whiteblock.management.WhiteblockConstants;
import org.web3j.contracts.eip20.generated.ERC20;
import org.web3j.contracts.eip721.generated.ERC721;
import org.web3j.crypto.Credentials;
import org.web3j.crypto.RawTransaction;
import org.web3j.crypto.TransactionEncoder;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.admin.Admin;
import org.web3j.protocol.admin.methods.response.PersonalUnlockAccount;
import org.web3j.protocol.core.DefaultBlockParameterName;
import org.web3j.protocol.core.methods.request.Transaction;
import org.web3j.protocol.core.methods.response.*;
import org.web3j.protocol.http.HttpService;
import org.web3j.tx.FastRawTransactionManager;
import org.web3j.tx.TransactionManager;
import org.web3j.tx.Transfer;
import org.web3j.tx.response.NoOpProcessor;
import org.web3j.utils.Convert;
import org.web3j.utils.Numeric;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ExecutionException;


public class Web3JUtils {

    private final Admin web3JA;
    private final Web3j web3j;
    private final Web3JContext context;


    public Web3JUtils(Web3JContext context) {

        HttpService httpService= new HttpService("http://"+context.getIpOftheNode()+":"+ context.getPort());
        this.web3JA = Admin.build(httpService);
        this.web3j = Web3j.build(httpService);
        this.context=context;
    }

    /**
     * Returns the list of addresses owned by this client.
     */
    public List<String> getAccounts() throws InterruptedException, ExecutionException {
        return web3j
                .ethAccounts()
                .sendAsync()
                .get().getAccounts();
    }

    /**
     * Returns the balance (in Ether) of the specified account address.
     */
    public  BigDecimal getBalanceEther(String address) throws InterruptedException, ExecutionException {
        return weiToEther(getBalanceWei( address));
    }

    /**
     * Returns the balance (in Wei) of the specified account address.
     */
    public BigInteger getBalanceWei(String address) throws InterruptedException, ExecutionException {
        return web3j
                .ethGetBalance(address, DefaultBlockParameterName.PENDING)
                .sendAsync()
                .get().getBalance();
    }

    public String getNetID(Web3j web3j) throws InterruptedException, ExecutionException {
        return web3j.netVersion().sendAsync().get().getNetVersion();
    }


    private String getClientVersion(Admin web3j) throws InterruptedException, ExecutionException {
        Web3ClientVersion client = web3j
                .web3ClientVersion()
                .sendAsync()
                .get();

        return client.getWeb3ClientVersion();
    }

    private EthCoinbase getCoinbase(Admin web3j) throws InterruptedException, ExecutionException {
        return web3j
                .ethCoinbase()
                .sendAsync()
                .get();
    }
    public static BigInteger getBalanceWei(Admin web3j, String address) throws InterruptedException, ExecutionException {
        EthGetBalance balance = web3j
                .ethGetBalance(address, DefaultBlockParameterName.PENDING)
                .sendAsync()
                .get();

        return balance.getBalance();
    }

    /**
     * Return the nonce (tx count) for the specified address.
     */
    public static BigInteger getNonce(Admin web3j, String address) throws InterruptedException, ExecutionException, IOException {
        EthGetTransactionCount ethGetTransactionCount =
                web3j.ethGetTransactionCount(address, DefaultBlockParameterName.PENDING).send();

        return ethGetTransactionCount.getTransactionCount();
    }

    /**
     * Converts the provided Wei amount (smallest value Unit) to Ethers.
     */
    public static BigDecimal weiToEther(BigInteger wei) {
        return Convert.fromWei(wei.toString(), Convert.Unit.ETHER);
    }


    private void logInfo(String text)
    {
        if(this.context.getTracemode().isPresent())
        {
            if(this.context.getTracemode().get().toLowerCase().equals(WhiteblockConstants.TRUE))
                this.context.getContext().getLogger().info(text);
        }
    }

    public static BigInteger etherToWei(BigDecimal ether) {
        return Convert.toWei(ether, Convert.Unit.ETHER).toBigInteger();
    }
    private BigDecimal convertWeiStringTOBigInteger(String amount)
    {
        logInfo(" amount  :"+amount);
        BigDecimal amountWei = Convert.fromWei(amount, Convert.Unit.ETHER);
        logInfo("conveted amount  :"+amountWei.toString());

        return amountWei;
    }
    private BigInteger convertEtherStringTOBigInteger(String amount)
    {
        logInfo(" amount  :"+amount);
        BigInteger amountWei = Convert.toWei(amount, Convert.Unit.ETHER).toBigInteger();
        logInfo("conveted amount  :"+amountWei.toString());

        return amountWei;
    }
    public String getTransactionByHash(String hash) throws IOException {
        StringBuilder result=new StringBuilder();
        EthTransaction transaction= web3JA.ethGetTransactionByHash(hash).send();
        if(transaction.getTransaction().isPresent())
        {
            org.web3j.protocol.core.methods.response.Transaction transac=transaction.getTransaction().get();
            result.append("From :").append(transac.getFrom());
            result.append("to :").append(transac.getTo());
            result.append("blocknumber :").append(transac.getBlockNumberRaw());
            result.append("nonce :").append(transac.getNonceRaw());
            result.append("Value :").append(transac.getValueRaw());
            result.append("gas :").append(transac.getGasRaw());
            result.append("Gas Price :").append(transac.getGasPriceRaw());

        }else
            result.append("No transaction with hash ").append(hash);

        return result.toString();
    }
    public String transfertFunds(String to,String amoutwei) throws Exception {
        Credentials credentials = Credentials.create(this.context.getPrivateKey());
        TransactionReceipt txReceipt = Transfer.sendFunds(web3JA,credentials,to,convertWeiStringTOBigInteger(amoutwei), Convert.Unit.ETHER).send();
        logInfo("transfertfunds receipt :" + txReceipt);
        String transactionhash= txReceipt.getTransactionHash();
        if(transactionhash==null)
            throw new Web3JExeption("Transaction Hash is null - not appble to send funds to : "+to +" amountwei  :" +
                    amoutwei +" from "+ this.context.getAccountAddress()+ " error "+txReceipt.getLogs().toString());
        else
            return transactionhash;
    }

    public String createEtherSignedTransaction(String to, String amountWei) throws ExecutionException, InterruptedException, IOException, Web3JExeption {
        if (unlockAccount()){

            // get the next available nonce
            BigInteger nonce = getNonce(web3JA, this.context.getAccountAddress());
            logInfo("nonce :" + nonce.toString());
            Credentials credentials = Credentials.create(this.context.getPrivateKey());
            //--get gas price
            BigInteger gasprice = web3JA.ethGasPrice().send().getGasPrice();
            logInfo("Gas Price :" + gasprice.toString());
            //--get gas limit
            logInfo(" adress from credentional: "+credentials.getAddress());
            BigInteger blockGasLimit = web3JA.ethGetBlockByNumber(DefaultBlockParameterName.LATEST, false).send().getBlock().getGasLimit();

            logInfo("Gaslimit :" + blockGasLimit.toString());
            // create our transaction
            RawTransaction rawTransaction = RawTransaction.createEtherTransaction(nonce, gasprice, blockGasLimit, to, convertEtherStringTOBigInteger(amountWei));
            // sign & send our transaction
            byte[] signedMessage = TransactionEncoder.signMessage(rawTransaction, credentials);
            logInfo("Signed message :"+ Arrays.toString(signedMessage));

            String hexValue = Numeric.toHexString(signedMessage);
            logInfo("hex Signed message :"+hexValue);

            EthSendTransaction ethSendTransaction = web3JA.ethSendRawTransaction(hexValue).send();

            String transactionHash = ethSendTransaction.getTransactionHash();
            if(transactionHash==null)
                throw new Web3JExeption("Transaction Hash is null - not appble to send  signed transaction to : "+to +" amountwei  :" +
                        amountWei +" from "+ this.context.getAccountAddress()+" error "+ethSendTransaction.getError()+
                        " result "+ethSendTransaction.getResult());


            return transactionHash;
        }
        else
            throw new Web3JExeption("Unable to unlock account :"+context.getAccountAddress());
    }
    public String createSignedContractTransaction(String contractAddress, String amoutwei) throws ExecutionException, InterruptedException, IOException, Web3JExeption {
        if (unlockAccount()){

            // get the next available nonce
            BigInteger nonce = getNonce(web3JA, this.context.getAccountAddress());
            logInfo("nonce :" + nonce.toString());
            Credentials credentials = Credentials.create(this.context.getPrivateKey());
            //--get gas price
            BigInteger gasprice = web3JA.ethGasPrice().send().getGasPrice();
            logInfo("Gas Price :" + gasprice.toString());
            //--get gas limit
            BigInteger blockGasLimit = web3JA.ethGetBlockByNumber(DefaultBlockParameterName.LATEST, false).send().getBlock().getGasLimit();

            logInfo("Gaslimit :" + blockGasLimit.toString());
            // create our transaction
            RawTransaction rawTransaction = RawTransaction.createContractTransaction(nonce, gasprice, blockGasLimit,
                    convertEtherStringTOBigInteger(amoutwei),contractAddress);
            // sign & send our transaction
            byte[] signedMessage = TransactionEncoder.signMessage(rawTransaction, credentials);
            logInfo("Signed message :"+ Arrays.toString(signedMessage));

            String hexValue = Numeric.toHexString(signedMessage);
            logInfo("hex Signed message :"+hexValue);

            EthSendTransaction ethSendTransaction = web3JA.ethSendRawTransaction(hexValue).send();

            String transactionHash = ethSendTransaction.getTransactionHash();

            if(transactionHash==null)
                throw new Web3JExeption("Transaction Hash is null - not appble to send signed contract transaction on contract : "+
                        contractAddress +" amountwei  :" +amoutwei +" from "+ this.context.getAccountAddress()+
                        " error "+ethSendTransaction.getRawResponse());

            return transactionHash;
        }
        else
            throw new Web3JExeption("Unable to unlock account :"+context.getAccountAddress());
    }

    public String getBalance() throws IOException, ExecutionException, InterruptedException, Web3JExeption {
        BigDecimal balance;
        if(unlockAccount())
        {
            balance=getBalanceEther(context.getAccountAddress());
            return balance.toPlainString();
        }
        else
            throw new Web3JExeption("Unable to unlock account :"+context.getAccountAddress());
    }

    public String createERC721Transaction(String tokenid,String contractAddress,String value, String to) throws Exception {
        if (unlockAccount()) {
            NoOpProcessor processor = new NoOpProcessor(this.web3JA);
            Credentials credentials = Credentials.create(this.context.getPrivateKey());
            BigInteger gasprice = web3JA.ethGasPrice().send().getGasPrice();
            logInfo("Gas Price :"+gasprice.toString());
            //--get gas limit
            BigInteger blockGasLimit = web3JA.ethGetBlockByNumber(DefaultBlockParameterName.LATEST, false).send()
                    .getBlock().getGasLimit();

            //deploy new contract
            TransactionManager txManager = new FastRawTransactionManager(this.web3JA, credentials, processor);
            ERC721 token = ERC721.load(contractAddress, this.web3JA, txManager,gasprice, blockGasLimit);



            TransactionReceipt receipt=token.safeTransferFrom(this.context.getAccountAddress(),to,
                    BigInteger.valueOf(Long.parseLong(tokenid)),convertEtherStringTOBigInteger(value)).send();
             String transactionHash = receipt.getTransactionHash();
            if(transactionHash==null)
                throw new Web3JExeption("Transaction Hash is null - not able to send  EC721 transaction on contract : "+
                        contractAddress +" amountwei  :" +value +" from "+ this.context.getAccountAddress()+" error "+
                        receipt.getStatus());

            return transactionHash;
        }
        else
            throw new Web3JExeption("Unable to unlock account :"+context.getAccountAddress());
    }

    public String createERC20Transaction(String contractadress,String value, String to) throws Exception {
        if (unlockAccount()) {
            NoOpProcessor processor = new NoOpProcessor(this.web3JA);
            Credentials credentials = Credentials.create(this.context.getPrivateKey());
            BigInteger gasprice = web3JA.ethGasPrice().send().getGasPrice();
            logInfo("Gas Price :"+gasprice.toString());
            //--get gas limit
            BigInteger blockGasLimit = web3JA.ethGetBlockByNumber(DefaultBlockParameterName.LATEST, false).send()
                    .getBlock().getGasLimit();

            //deploy new contract
            TransactionManager txManager = new FastRawTransactionManager(this.web3JA, credentials, processor);
            ERC20 token = ERC20.load(contractadress, this.web3JA, txManager,gasprice, blockGasLimit);

            TransactionReceipt receipt = token.transfer(to, convertEtherStringTOBigInteger(value)).send();
            String transactionHash = receipt.getTransactionHash();


            if(transactionHash==null)
                throw new Web3JExeption("Transaction Hash is null - not able to send  EC20 transaction on contract : "+
                        contractadress +" amountwei  :" +value +" from "+ this.context.getAccountAddress()+
                        " error "+ receipt.getStatus());

            return transactionHash;
        }
        else
            throw new Web3JExeption("Unable to unlock account :"+context.getAccountAddress());
    }


    public String createContractTransaction(String contractadress, String amountWei)
            throws ExecutionException, InterruptedException, IOException, Web3JExeption {
         if (unlockAccount()) {

            // get the next available nonce
            BigInteger nonce = getNonce(web3JA, context.getAccountAddress());
            logInfo("nonce :"+nonce.toString());
            //--get gas price
            BigInteger gasprice = web3JA.ethGasPrice().send().getGasPrice();

            logInfo("Gas Price :"+gasprice.toString());
            //--get gas limit
            BigInteger blockGasLimit = web3JA.ethGetBlockByNumber(DefaultBlockParameterName.LATEST, false).send()
                    .getBlock().getGasLimit();

            logInfo("Gaslimit :"+blockGasLimit.toString());
            // create our transaction
            Transaction transaction= Transaction.createContractTransaction(context.getAccountAddress(),nonce,
                    gasprice,blockGasLimit,convertEtherStringTOBigInteger(amountWei),contractadress);

            EthSendTransaction ethSendTransaction = web3JA.ethSendTransaction(transaction).send();
            // ...

            String transactionHash = ethSendTransaction.getTransactionHash();


             if(transactionHash==null)
                 throw new Web3JExeption("Transaction Hash is null - not appble to send  contract transaction on contract : "+
                         contractadress +" amountwei  :" +amountWei +" from "+ this.context.getAccountAddress()+" error "+
                         ethSendTransaction.getRawResponse());

            return transactionHash;

        }
        else
             throw new Web3JExeption("Unable to unlock account :"+context.getAccountAddress());
    }

    public Boolean unlockAccount() throws IOException {
        PersonalUnlockAccount personalUnlockAccount = web3JA.personalUnlockAccount(context.getAccountAddress(),
                context.getWalletpassord()).send();
        if(personalUnlockAccount.accountUnlocked()==null)
            logInfo(personalUnlockAccount.getError().getMessage());
        return personalUnlockAccount.accountUnlocked() != null && personalUnlockAccount.accountUnlocked();
    }

    public String createEtherTransaction( String to, String amountWei) throws IOException, ExecutionException, InterruptedException, Web3JExeption {
        logInfo("adress :"+context.getAccountAddress()+" pawsd:"+ context.getWalletpassord() +
                " ip "+context.getIpOftheNode()+ " publickey " + context.getPublicKey()+ " private "+
                context.getPrivateKey());
        if (unlockAccount()) {

            // get the next available nonce
            BigInteger nonce = getNonce(web3JA, context.getAccountAddress());
            logInfo("nonce :"+nonce.toString());
            //--get gas price
            BigInteger gasprice = web3JA.ethGasPrice().send().getGasPrice();
            logInfo("Gas Price :"+gasprice.toString());
           //--get gas limit
            BigInteger blockGasLimit = web3JA.ethGetBlockByNumber(DefaultBlockParameterName.LATEST, false).send().getBlock().getGasLimit();

           logInfo("Gas Limit :"+blockGasLimit.toString());
            // create our transaction
            Transaction transaction= Transaction.createEtherTransaction(context.getAccountAddress(),nonce,
                    gasprice,blockGasLimit,to,convertEtherStringTOBigInteger(amountWei));

            EthSendTransaction ethSendTransaction = web3JA.ethSendTransaction(transaction).send();
            // ...

            String transactionHash = ethSendTransaction.getTransactionHash();

            if(transactionHash==null)
                throw new Web3JExeption("Transaction Hash is null - not able to send  ether transaction to : "+to +
                        " amountwei  :" +amountWei +" from "+ this.context.getAccountAddress()+" error "+
                        ethSendTransaction.getRawResponse());


            return transactionHash;

        }
        else
            throw new Web3JExeption("Unable to unlock account :"+context.getAccountAddress());
    }
}
