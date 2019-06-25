package com.neotys.ethereumJ.common.utils.Whiteblock.management;

import com.google.gson.Gson;
import com.jcraft.jsch.ChannelExec;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.Session;
import com.neotys.ethereumJ.common.utils.Whiteblock.data.WhiteblockAccountList;
import com.neotys.ethereumJ.common.utils.Whiteblock.data.WhiteblockContractList;
import com.neotys.ethereumJ.common.utils.Whiteblock.data.WhiteblockMonitoringData;
import com.neotys.ethereumJ.common.utils.Whiteblock.data.WhiteblockNodeList;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.List;
import com.google.common.base.Optional;
import com.neotys.extensions.action.engine.Context;

import java.util.Properties;
import java.util.concurrent.atomic.AtomicInteger;

import static com.neotys.ethereumJ.common.utils.Whiteblock.Constants.ERROR;

public class WhiteblockProcessbuilder {
    private static ProcessBuilder processBuilder;
    private static String JOINSEPERATOR=" ";



    private static String cleanKeystoreOutput(String output)
    {
        String cleanedoutput;
        //"ciphertext":"9178bc2614af2f4975451f23713fd769b9fe0b6442f0cff7058ce53f70759a30"
        String[] splitedoutput=output.split(":");
        String keystore=splitedoutput[splitedoutput.length-1];
        cleanedoutput=keystore.substring(1,keystore.length()-1);
        return cleanedoutput;
    }



    private static void traceInfo(WhiteBlockContext context,String log)
    {
        if(context.getTracemode().isPresent())
        {
            if(context.getTracemode().get().toLowerCase().equals(WhiteBlockConstants.TRUE))
            {
                context.getContext().getLogger().info(log);
            }
        }
    }

    public static String sshCommand(WhiteBlockContext context,List<String> command) throws WhiteblockConnectionException
    {
        Properties config = new Properties();
        JSch jsch = new JSch();
        String host=context.getWhiteblocMasterHost();
        // Create a JSch session to connect to the server
        config.put("StrictHostKeyChecking", "no");
        StringBuilder output =new StringBuilder();
        // Create a JSch session to connect to the server
        try
        {

            Session session = jsch.getSession(WhiteBlockConstants.USER, host, 22);
            session.setPassword(context.getSshpassword());
            session.setConfig(config);
            // Establish the connection
            session.connect();

            ChannelExec channel = (ChannelExec) session.openChannel("exec");
            channel.setCommand(String.join(JOINSEPERATOR,command));
            channel.setErrStream(System.err);

            InputStream in = channel.getInputStream();
            channel.connect();
            byte[] tmp = new byte[1024];
            String line;
            while (true) {
                while (in.available() > 0) {
                    int i = in.read(tmp, 0, 1024);
                    if (i < 0) {
                        break;
                    }
                    line=new String(tmp, 0, i);
                    output.append(line);
                    traceInfo(context,"sshcommand - received : "+line);
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
    public static String sendCommand(WhiteBlockContext context,List<String> wbcommand) throws IOException, InterruptedException {
        processBuilder=new ProcessBuilder();
        StringBuilder output=new StringBuilder();

        processBuilder.command(wbcommand);
        traceInfo(context,"sendcommand - send  : "+wbcommand);
        Process process=processBuilder.start();
        BufferedReader reader =
                new BufferedReader(new InputStreamReader(process.getInputStream()));

        String line;
        while ((line = reader.readLine()) != null) {
            traceInfo(context,"sendcommand - received : "+line);
            output.append(line);
        }

        int exitCode = process.waitFor();
        traceInfo(context,"Exited with error code : " + exitCode);

        return output.toString();
    }
    public static WhiteblockAccountList getAccountLis( WhiteBlockContext context) throws IOException, InterruptedException, WhiteblockConnectionException {
        String jsonCustomer;
        Gson gson=new Gson();
        jsonCustomer=sendCommand(context,Arrays.asList("wb", "get", "account", "info"));
        jsonCustomer="{ \"accountList\":"+jsonCustomer+"}";
        WhiteblockAccountList whiteblockAccountList= gson.fromJson(jsonCustomer, WhiteblockAccountList.class);

        return whiteblockAccountList;
    }

    public static WhiteblockContractList getContractList(WhiteBlockContext context) throws IOException, InterruptedException, WhiteblockConnectionException {
        String jsonCustomer;
        Gson gson=new Gson();
        jsonCustomer=sendCommand(context,Arrays.asList("wb", "get", "contracts","list"));
        jsonCustomer="{ \"whiteblockContractList\":"+jsonCustomer+"}";

        return gson.fromJson(jsonCustomer, WhiteblockContractList.class);

    }


    public static WhiteblockNodeList getNodeLis(WhiteBlockContext context) throws IOException, InterruptedException, WhiteblockConnectionException {
        String jsonCustomer;
        Gson gson=new Gson();
        jsonCustomer=sendCommand(context,Arrays.asList("wb", "get", "nodes", "info"));
        jsonCustomer="{ \"whiteblockNodeList\":"+jsonCustomer+"}";

        return gson.fromJson(jsonCustomer, WhiteblockNodeList.class);

    }


    public static WhiteblockMonitoringData getMonitoringData(WhiteBlockContext context,long timestampstart,long timestartend) throws IOException, InterruptedException, WhiteblockConnectionException {
        String jsonCustomer;
        Gson gson=new Gson();
        //jsonCustomer=sshCommand(context,Arrays.asList("wb", "get", "stats", "time",String.valueOf(timestartend),String.valueOf(timestartend)));
        jsonCustomer=sendCommand(context,Arrays.asList("wb", "get", "stats", "past","5"));
        if(jsonCustomer.contains(ERROR))
            throw new WhiteblockConnectionException("MONITORING error :" +jsonCustomer);

        return gson.fromJson(jsonCustomer, WhiteblockMonitoringData.class);

    }

    public static String enableNetConfigModule(WhiteBlockContext context) throws IOException, InterruptedException, WhiteblockConnectionException {
        String output;
        output=sendCommand(context,Arrays.asList("wb", "netconfig",  "all"));
        return output;
    }
    public  static String disableNetConfigModule(WhiteBlockContext context) throws IOException, InterruptedException, WhiteblockConnectionException {
        String output;
        output=sendCommand(context,Arrays.asList("wb", "netconfig", "clear"));
        return output;
    }
    public static String defineLossOnNodes(WhiteBlockContext context, Optional<Integer> nodeid, double loss) throws IOException, InterruptedException, WhiteblockConnectionException {
        String output;
        if(nodeid.isPresent())
            output=sendCommand(context,Arrays.asList("wb", "netconfig", "set", String.valueOf(nodeid.get()),"loss",String.valueOf(loss)));
        else
            output=sendCommand(context,Arrays.asList("wb", "netconfig","loss",String.valueOf(loss)));
        return output;
    }

    public static String defineDelayOnNodes(WhiteBlockContext context,Optional<Integer> nodeid,int delay) throws IOException, InterruptedException, WhiteblockConnectionException {
        String output;
        if(nodeid.isPresent())
            output=sendCommand(context,Arrays.asList("wb", "netconfig", "set", String.valueOf(nodeid.get()),"-d",String.valueOf(delay)));
        else
            output=sendCommand(context,Arrays.asList("wb", "netconfig", "-d",String.valueOf(delay)));
        return output;
    }
    public static String defineBwOnNodes(WhiteBlockContext context,Optional<Integer> nodeid,int bw,Optional<String> unit) throws IOException, InterruptedException, WhiteblockConnectionException {
        String output;
        if(nodeid.isPresent())
            output=sendCommand(context,Arrays.asList("wb", "netconfig", "set", String.valueOf(nodeid.get()),"bw",String.valueOf(bw),unit.get()));
        else
            output=sendCommand(context,Arrays.asList("wb", "netconfig", "bw",String.valueOf(bw),unit.get()));
        return output;
    }

    public static String minterStart(WhiteBlockContext context) throws IOException, InterruptedException, WhiteblockConnectionException {
        String output;
        output=sendCommand(context,Arrays.asList("wb", "miner", "start"));
        return output;
    }
    public static String minterStop(WhiteBlockContext contextt) throws IOException, InterruptedException, WhiteblockConnectionException {
        String output;
        output=sendCommand(contextt,Arrays.asList("wb", "miner", "stop"));
        return output;
    }
    public static String buildEnvironment(WhiteBlockContext context,String blockchain , int numberofnodes) throws IOException, InterruptedException, WhiteblockConnectionException {
        String output;
        output=sendCommand(context,Arrays.asList("wb", "build", "-b", blockchain,"-n",String.valueOf(numberofnodes),"-c","0","-m","0","-y"));
        return output;
    }


}
