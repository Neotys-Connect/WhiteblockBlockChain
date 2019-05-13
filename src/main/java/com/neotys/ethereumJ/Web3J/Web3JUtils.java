package com.neotys.ethereumJ.Web3J;

import com.google.common.base.Optional;
import com.neotys.ethereumJ.common.utils.Whiteblock.management.WhiteBlockConstants;
import com.neotys.ethereumJ.common.utils.Whiteblock.management.WhiteBlockContext;
import org.web3j.crypto.Credentials;
import org.web3j.crypto.ECKeyPair;
import org.web3j.crypto.RawTransaction;
import org.web3j.crypto.TransactionEncoder;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.admin.Admin;
import org.web3j.protocol.admin.methods.response.PersonalUnlockAccount;
import org.web3j.protocol.core.DefaultBlockParameterName;
import org.web3j.protocol.core.methods.request.Transaction;
import org.web3j.protocol.core.methods.response.*;
import org.web3j.protocol.exceptions.TransactionException;
import org.web3j.protocol.http.HttpService;
import org.web3j.tx.Transfer;
import org.web3j.utils.Convert;
import org.web3j.utils.Numeric;

import com.neotys.extensions.action.engine.Context;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.concurrent.ExecutionException;



public class Web3JUtils {

    private Admin web3J;
    private Web3JContext context;


    public Web3JUtils(Web3JContext context) {

        this.web3J   = Admin.build(new HttpService("http://"+context.getIpOftheNode()+":"+ context.getPort()));
        this.context=context;
    }

    /**
     * Returns the list of addresses owned by this client.
     */
    public  EthAccounts getAccounts(Web3j web3j) throws InterruptedException, ExecutionException {
        return web3j
                .ethAccounts()
                .sendAsync()
                .get();
    }

    /**
     * Returns the balance (in Ether) of the specified account address.
     */
    public  BigDecimal getBalanceEther(Web3j web3j, String address) throws InterruptedException, ExecutionException {
        return weiToEther(getBalanceWei(web3j, address));
    }

    /**
     * Returns the balance (in Wei) of the specified account address.
     */
    public  BigInteger getBalanceWei(Web3j web3j, String address) throws InterruptedException, ExecutionException {
        EthGetBalance balance = web3j
                .ethGetBalance(address, DefaultBlockParameterName.LATEST)
                .sendAsync()
                .get();

        return balance.getBalance();
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
                .ethGetBalance(address, DefaultBlockParameterName.LATEST)
                .sendAsync()
                .get();

        return balance.getBalance();
    }

    /**
     * Return the nonce (tx count) for the specified address.
     */
    public static BigInteger getNonce(Admin web3j, String address) throws InterruptedException, ExecutionException {
        EthGetTransactionCount ethGetTransactionCount =
                web3j.ethGetTransactionCount(address, DefaultBlockParameterName.LATEST).sendAsync().get();

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
            if(this.context.getTracemode().get().toLowerCase().equals(WhiteBlockConstants.TRUE))
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
    public String transfertFunds(String to,String amoutwei) throws Exception {
        Credentials credentials = Credentials.create(this.context.getPrivateKey().get());
        TransactionReceipt txReceipt = Transfer.sendFunds(web3J,credentials,to,convertWeiStringTOBigInteger(amoutwei), Convert.Unit.ETHER).send();
        logInfo("transfertfunds receipt :" + txReceipt);
        return txReceipt.getTransactionHash();
    }

    public String createEtherSignedTransaction(String to, String amoutwei) throws ExecutionException, InterruptedException, IOException {
        if (unlockAccount()){

            // get the next available nonce
            BigInteger nonce = getNonce(web3J, this.context.getAccountAdress());
            logInfo("nonce :" + nonce.toString());
            Credentials credentials = Credentials.create(this.context.getPrivateKey().get());
            //--get gas price
            BigInteger gasprice = web3J.ethGasPrice().send().getGasPrice();
            logInfo("Gas Price :" + gasprice.toString());
            //--get gas limit
            BigInteger blockGasLimit = web3J.ethGetBlockByNumber(DefaultBlockParameterName.LATEST, false).send().getBlock().getGasLimit();

            logInfo("Gaslimit :" + blockGasLimit.toString());
            // create our transaction
            RawTransaction rawTransaction = RawTransaction.createEtherTransaction(nonce, gasprice, blockGasLimit, to, convertEtherStringTOBigInteger(amoutwei));
            // sign & send our transaction
            byte[] signedMessage = TransactionEncoder.signMessage(rawTransaction, credentials);
            logInfo("Signed message :"+ signedMessage.toString());

            String hexValue = Numeric.toHexString(signedMessage);
            logInfo("hex Signed message :"+hexValue);

            EthSendTransaction ethSendTransaction = web3J.ethSendRawTransaction(hexValue).send();

            String transactionHash = ethSendTransaction.getTransactionHash();


            return transactionHash;
        } else return null;
    }
    public String createSignedContractTransaction(String contradtadress, String amoutwei) throws ExecutionException, InterruptedException, IOException {
        if (unlockAccount()){

            // get the next available nonce
            BigInteger nonce = getNonce(web3J, this.context.getAccountAdress());
            logInfo("nonce :" + nonce.toString());
            Credentials credentials = Credentials.create(this.context.getPrivateKey().get());
            //--get gas price
            BigInteger gasprice = web3J.ethGasPrice().send().getGasPrice();
            logInfo("Gas Price :" + gasprice.toString());
            //--get gas limit
            BigInteger blockGasLimit = web3J.ethGetBlockByNumber(DefaultBlockParameterName.LATEST, false).send().getBlock().getGasLimit();

            logInfo("Gaslimit :" + blockGasLimit.toString());
            // create our transaction
            RawTransaction rawTransaction = RawTransaction.createContractTransaction(nonce, gasprice, blockGasLimit,  convertEtherStringTOBigInteger(amoutwei),contradtadress);
            // sign & send our transaction
            byte[] signedMessage = TransactionEncoder.signMessage(rawTransaction, credentials);
            logInfo("Signed message :"+ signedMessage.toString());

            String hexValue = Numeric.toHexString(signedMessage);
            logInfo("hex Signed message :"+hexValue);

            EthSendTransaction ethSendTransaction = web3J.ethSendRawTransaction(hexValue).send();

            String transactionHash = ethSendTransaction.getTransactionHash();


            return transactionHash;
        } else return null;
    }

    public String createContractTransaction(String contractadress, String amountWei) throws ExecutionException, InterruptedException, IOException {
         if (unlockAccount()) {

            // get the next available nonce
            BigInteger nonce = getNonce(web3J, context.getAccountAdress());
            logInfo("nonce :"+nonce.toString());
            //--get gas price
            BigInteger gasprice = web3J.ethGasPrice().send().getGasPrice();
            logInfo("Gas Price :"+gasprice.toString());
            //--get gas limit
            BigInteger blockGasLimit = web3J.ethGetBlockByNumber(DefaultBlockParameterName.LATEST, false).send().getBlock().getGasLimit();

            logInfo("Gaslimit :"+blockGasLimit.toString());
            // create our transaction
            Transaction transaction= Transaction.createContractTransaction(context.getAccountAdress(),nonce,gasprice,blockGasLimit,convertEtherStringTOBigInteger(amountWei),contractadress);

            EthSendTransaction ethSendTransaction = web3J.ethSendTransaction(transaction).send();
            // ...

            String transactionHash = ethSendTransaction.getTransactionHash();



            return transactionHash;

        }
        else
            return null;
    }

    public Boolean unlockAccount() throws IOException {
        PersonalUnlockAccount personalUnlockAccount = web3J.personalUnlockAccount(context.getAccountAdress(), context.getWalletpassord()).send();
        if(personalUnlockAccount.accountUnlocked()==null)
            logInfo(personalUnlockAccount.getError().getMessage());
        return personalUnlockAccount.accountUnlocked() != null && personalUnlockAccount.accountUnlocked();
    }

    public String createEtherTransaction( String to, String amountWei) throws IOException, ExecutionException, InterruptedException {
        logInfo("adress :"+context.getAccountAdress()+" pawsd:"+ context.getWalletpassord() + " ip "+context.getIpOftheNode()+ " publickey " + context.getPublicKey()+ " private "+context.getPrivateKey());
        if (unlockAccount()) {

            // get the next available nonce
            BigInteger nonce = getNonce(web3J, context.getAccountAdress());
            logInfo("nonce :"+nonce.toString());
            //--get gas price
            BigInteger gasprice = web3J.ethGasPrice().send().getGasPrice();
            logInfo("Gas Price :"+gasprice.toString());
           //--get gas limit
            BigInteger blockGasLimit = web3J.ethGetBlockByNumber(DefaultBlockParameterName.LATEST, false).send().getBlock().getGasLimit();

           logInfo("Gaslimit :"+blockGasLimit.toString());
            // create our transaction
            Transaction transaction= Transaction.createEtherTransaction(context.getAccountAdress(),nonce,gasprice,blockGasLimit,to,convertEtherStringTOBigInteger(amountWei));

            EthSendTransaction ethSendTransaction = web3J.ethSendTransaction(transaction).send();
            // ...

            String transactionHash = ethSendTransaction.getTransactionHash();



            return transactionHash;

        }
        else
            return null;
    }
}
