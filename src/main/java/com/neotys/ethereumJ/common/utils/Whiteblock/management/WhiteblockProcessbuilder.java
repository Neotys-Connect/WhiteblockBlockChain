package com.neotys.ethereumJ.common.utils.Whiteblock.management;

import com.google.gson.Gson;
import com.jcraft.jsch.ChannelExec;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.Session;
import com.neotys.ethereumJ.common.utils.Whiteblock.data.WhiteblockAccountList;
import com.neotys.ethereumJ.common.utils.Whiteblock.data.WhiteblockMonitoringData;
import com.neotys.ethereumJ.common.utils.Whiteblock.data.WhiteblockNodeList;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.List;
import com.google.common.base.Optional;
import java.util.Properties;
import java.util.concurrent.atomic.AtomicInteger;

public class WhiteblockProcessbuilder {
    private static ProcessBuilder processBuilder;
    private static String USER="";
    private static String JOINSEPERATOR=" ";

    private static String getPrivateKeyFromNode(String host,int node) throws WhiteblockConnectionException
    {
        Properties config = new Properties();
        JSch jsch = new JSch();
        // Create a JSch session to connect to the server
        config.put("StrictHostKeyChecking", "no");
        StringBuilder output =new StringBuilder();
        // Create a JSch session to connect to the server
        try
        {

            Session session = jsch.getSession(USER, host, 22);
            session.setConfig(config);
            // Establish the connection
            session.connect();

            ChannelExec channel = (ChannelExec) session.openChannel("exec");
            channel.setCommand("wb ssh "+String.valueOf(node));
            channel.setErrStream(System.err);

            InputStream in = channel.getInputStream();
            channel.connect();
            byte[] tmp = new byte[1024];
            while (true) {
                while (in.available() > 0) {
                    int i = in.read(tmp, 0, 1024);
                    if (i < 0) {
                        break;
                    }

                }
                if (channel.isClosed())
                {
                    break;
                }
                Thread.sleep(100);
            }

            channel.setCommand("cat ../geth/keystore/UTC* | grep -o '\"ciphertext\":\"[[:alnum:]]\\{3,\\}\"'");
            while (true) {
                while (in.available() > 0) {
                    int i = in.read(tmp, 0, 1024);
                    if (i < 0) {
                        break;
                    }
                    output.append(new String(tmp, 0, i));
                }
                if (channel.isClosed())
                {
                    break;
                }
                Thread.sleep(100);
            }
            channel.disconnect();
            session.disconnect();
            return output.toString();
        } catch (Exception e) {
            throw new WhiteblockConnectionException("SSH issue to master "+ host+" Exeption :"+e.getMessage());
        }
    }

    private static String cleanKeystoreOutput(String output)
    {
        String cleanedoutput;
        //"ciphertext":"9178bc2614af2f4975451f23713fd769b9fe0b6442f0cff7058ce53f70759a30"
        String[] splitedoutput=output.split(":");
        String keystore=splitedoutput[splitedoutput.length-1];
        cleanedoutput=keystore.substring(1,keystore.length()-1);
        return cleanedoutput;
    }

    public static String getKeystore(String master,int node ) throws WhiteblockConnectionException {
        String ouput;
        ouput=getPrivateKeyFromNode(master,node);
        return cleanKeystoreOutput(ouput);
    }

    public static String sshCommand(String host,List<String> command) throws WhiteblockConnectionException
    {
        Properties config = new Properties();
        JSch jsch = new JSch();
        // Create a JSch session to connect to the server
        config.put("StrictHostKeyChecking", "no");
        StringBuilder output =new StringBuilder();
        // Create a JSch session to connect to the server
        try
        {

            Session session = jsch.getSession(USER, host, 22);
            session.setConfig(config);
            // Establish the connection
            session.connect();

            ChannelExec channel = (ChannelExec) session.openChannel("exec");
            channel.setCommand(String.join(JOINSEPERATOR,command));
            channel.setErrStream(System.err);

            InputStream in = channel.getInputStream();
            channel.connect();
            byte[] tmp = new byte[1024];
            while (true) {
                while (in.available() > 0) {
                    int i = in.read(tmp, 0, 1024);
                    if (i < 0) {
                        break;
                    }
                    output.append(new String(tmp, 0, i));
                }
                if (channel.isClosed())
                {
                    break;
                }
                Thread.sleep(100);
            }
            channel.disconnect();
            session.disconnect();
            return output.toString();
        } catch (Exception e) {
            throw new WhiteblockConnectionException("SSH issue to master "+ host+" Exeption :"+e.getMessage());
         }
    }
    public static String sendCommand(List<String> wbcommand) throws IOException, InterruptedException {
        processBuilder=new ProcessBuilder();
        StringBuilder output=new StringBuilder();

        processBuilder.command(wbcommand);

        Process process=processBuilder.start();
        BufferedReader reader =
                new BufferedReader(new InputStreamReader(process.getInputStream()));

        String line;
        while ((line = reader.readLine()) != null) {
            output.append(line);
        }

        int exitCode = process.waitFor();
        System.out.println("\nExited with error code : " + exitCode);

        return output.toString();
    }
    public static WhiteblockAccountList getAccountLis(String masternode) throws IOException, InterruptedException, WhiteblockConnectionException {
        String jsonCustomer;
        Gson gson=new Gson();
        jsonCustomer=sshCommand(masternode,Arrays.asList("wb", "get", "account", "info"));
        WhiteblockAccountList whiteblockAccountList= gson.fromJson(jsonCustomer, WhiteblockAccountList.class);
        AtomicInteger counter= new AtomicInteger();
        whiteblockAccountList.getAccountList().stream().forEach(whiteblockAccount -> {
            String keystore= null;
            try {
                keystore = getKeystore(masternode, counter.get());
                counter.getAndIncrement();
                whiteblockAccount.setKeystore(keystore);
            } catch (WhiteblockConnectionException e) {
                e.printStackTrace();
            }

        });
        return whiteblockAccountList;
    }



    public static WhiteblockNodeList getNodeLis(String masternode) throws IOException, InterruptedException, WhiteblockConnectionException {
        String jsonCustomer;
        Gson gson=new Gson();
        jsonCustomer=sshCommand(masternode,Arrays.asList("wb", "get", "nodes", "info"));
        return gson.fromJson(jsonCustomer, WhiteblockNodeList.class);

    }


    public static WhiteblockMonitoringData getMonitoringData(String masternode,long timestampstart,long timestartend) throws IOException, InterruptedException, WhiteblockConnectionException {
        String jsonCustomer;
        Gson gson=new Gson();
        jsonCustomer=sshCommand(masternode,Arrays.asList("wb", "get", "stats", "time",String.valueOf(timestartend),String.valueOf(timestartend)));
        return gson.fromJson(jsonCustomer, WhiteblockMonitoringData.class);

    }

    public static String enableNetConfigModule(String masternode) throws IOException, InterruptedException, WhiteblockConnectionException {
        String output;
        output=sshCommand(masternode,Arrays.asList("wb", "netconfig", "on"));
        return output;
    }
    public  static String disableNetConfigModule(String masternode) throws IOException, InterruptedException, WhiteblockConnectionException {
        String output;
        output=sshCommand(masternode,Arrays.asList("wb", "netconfig", "off"));
        return output;
    }
    public static String defineLossOnNodes(String masternode, Optional<Integer> nodeid, double loss) throws IOException, InterruptedException, WhiteblockConnectionException {
        String output;
        if(nodeid.isPresent())
            output=sshCommand(masternode,Arrays.asList("wb", "netconfig", "set", String.valueOf(nodeid.get()),"loss",String.valueOf(loss)));
        else
            output=sshCommand(masternode,Arrays.asList("wb", "netconfig","loss",String.valueOf(loss)));
        return output;
    }

    public static String defineDelayOnNodes(String masternode,Optional<Integer> nodeid,int delay) throws IOException, InterruptedException, WhiteblockConnectionException {
        String output;
        if(nodeid.isPresent())
            output=sshCommand(masternode,Arrays.asList("wb", "netconfig", "set", String.valueOf(nodeid.get()),"-d",String.valueOf(delay)));
        else
            output=sshCommand(masternode,Arrays.asList("wb", "netconfig", "-d",String.valueOf(delay)));
        return output;
    }
    public static String defineBwOnNodes(String masternode,Optional<Integer> nodeid,int bw,Optional<String> unit) throws IOException, InterruptedException, WhiteblockConnectionException {
        String output;
        if(nodeid.isPresent())
            output=sshCommand(masternode,Arrays.asList("wb", "netconfig", "set", String.valueOf(nodeid.get()),"bw",String.valueOf(bw),unit.get()));
        else
            output=sshCommand(masternode,Arrays.asList("wb", "netconfig", "bw",String.valueOf(bw),unit.get()));
        return output;
    }

    public static String minterStart(String masternode) throws IOException, InterruptedException, WhiteblockConnectionException {
        String output;
        output=sshCommand(masternode,Arrays.asList("wb", "miner", "start"));
        return output;
    }
    public static String minterStop(String masternode) throws IOException, InterruptedException, WhiteblockConnectionException {
        String output;
        output=sshCommand(masternode,Arrays.asList("wb", "miner", "stop"));
        return output;
    }
    public static String buildEnvironment(String masternode,String blockchain , int numberofnodes) throws IOException, InterruptedException, WhiteblockConnectionException {
        String output;
        output=sshCommand(masternode,Arrays.asList("wb", "build", "-b", blockchain,"-n",String.valueOf(numberofnodes),"-c","0","-m","0","-y"));
        return output;
    }


}
