package com.neotys.ethereumJ.Web3J;

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
import org.web3j.utils.Convert;
import org.web3j.utils.Numeric;

import com.neotys.extensions.action.engine.Context;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.concurrent.ExecutionException;

import static org.web3j.protocol.admin.Admin.build;

public class Web3JUtils {
    private String hostname;
    private String port;

    private String walletAdress;
    private String walletpassword;
    private Admin web3J;
    private String keystore;
    private Context context;

    public Web3JUtils(String hostname, String port, String walletAdress, String walletpassword,String key,Context context) {
        this.hostname = hostname;
        this.port = port;
        this.walletAdress = walletAdress;
        this.walletpassword = walletpassword;
        this.context=context;
        this.keystore=key;
        web3J   = build(new HttpService("http://"+hostname+":"+port));

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


    public static BigInteger etherToWei(BigDecimal ether) {
        return Convert.toWei(ether, Convert.Unit.ETHER).toBigInteger();
    }

    private BigInteger convertStringTOBigInteger(String amount)
    {
        context.getLogger().debug(" amount  :"+amount);
        BigInteger amountWei = Convert.toWei(amount, Convert.Unit.ETHER).toBigInteger();
        context.getLogger().debug("conveted amount  :"+amountWei.toString());

        return amountWei;
    }
    public String createEtherTransaction( String to, String amountWei) throws IOException, ExecutionException, InterruptedException {
        PersonalUnlockAccount personalUnlockAccount = web3J.personalUnlockAccount(walletAdress, walletpassword).send();
        if (personalUnlockAccount.accountUnlocked()) {

            // get the next available nonce
            BigInteger nonce = getNonce(web3J, walletAdress);
            context.getLogger().debug("nonce :"+nonce.toString());
            Credentials credentials = Credentials.create(walletAdress);
            //--get gas price
            BigInteger gasprice = web3J.ethGasPrice().send().getGasPrice();
            context.getLogger().debug("Gas Price :"+gasprice.toString());
           //--get gas limit
            BigInteger blockGasLimit = web3J.ethGetBlockByNumber(DefaultBlockParameterName.LATEST, false).send().getBlock().getGasLimit();

            context.getLogger().debug("Gaslimit :"+blockGasLimit.toString());
            // create our transaction
            Transaction transaction= Transaction.createEtherTransaction(walletAdress,nonce,gasprice,blockGasLimit,to,convertStringTOBigInteger(amountWei));
//            RawTransaction rawTransaction = RawTransaction.createEtherTransaction(nonce, gasprice, blockGasLimit, credentials.getAddress(), convertStringTOBigInteger(amountWei));
            // sign & send our transaction
         //   byte[] signedMessage = TransactionEncoder.signMessage(rawTransaction, credentials);
           /* context.getLogger().debug("Signed message :"+ signedMessage.toString());

            String hexValue = Numeric.toHexString(signedMessage);
            context.getLogger().debug("hex Signed message :"+hexValue);

*/
            EthSendTransaction ethSendTransaction = web3J.ethSendTransaction(transaction).send();
            // ...

            String transactionHash = ethSendTransaction.getTransactionHash();


            return transactionHash;

        }
        else
            return null;
    }
}
