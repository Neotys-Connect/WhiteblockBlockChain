package com.neotys.ethereumJ.common.utils.Whiteblock.management;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.jcraft.jsch.ChannelExec;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.Session;
import com.neotys.ethereumJ.common.utils.Whiteblock.data.*;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.*;

import com.google.common.base.Optional;
import com.neotys.ethereumJ.common.utils.Whiteblock.http.WhiteBlockHttpException;
import com.neotys.ethereumJ.common.utils.Whiteblock.rpc.WhiteBlockJsonRPC;
import com.neotys.ethereumJ.common.utils.Whiteblock.rpc.WhiteblockHttpContext;
import com.neotys.extensions.action.engine.Context;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.regex.Pattern;

import static com.neotys.ethereumJ.common.utils.Whiteblock.Constants.ERROR;

public class WhiteblockProcessbuilder {
    private static ProcessBuilder processBuilder;
    private static String JOINSEPERATOR=" ";
    private static final Pattern REGEXP_PATERN=Pattern.compile("0x[0-9a-z]+");


    private static String cleanKeystoreOutput(String output)
    {
        String cleanedoutput;
        //"ciphertext":"9178bc2614af2f4975451f23713fd769b9fe0b6442f0cff7058ce53f70759a30"
        String[] splitedoutput=output.split(":");
        String keystore=splitedoutput[splitedoutput.length-1];
        cleanedoutput=keystore.substring(1,keystore.length()-1);
        return cleanedoutput;
    }



    private static void traceInfo(WhiteblockHttpContext context,String log)
    {
        if(context.getTracemode().isPresent())
        {
            if(context.getTracemode().get().toLowerCase().equals(WhiteBlockConstants.TRUE))
            {
                context.getContext().getLogger().info(log);
            }
        }
    }


    public static String sshCommand(WhiteblockHttpContext context,List<String> command) throws WhiteblockConnectionException
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
            //session.setPassword(context.getSshpassword());
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
    public static String sendCommand(WhiteblockHttpContext context,List<String> wbcommand) throws IOException, InterruptedException {
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
    public static double getBuildStatus( WhiteblockHttpContext context, String testnetid) throws Exception {
        double jsonCustomer = 0;
        Gson gson = new Gson();
        String output;
        JSONObject jsonoutput;

        Map<String, Object> params = new HashMap<>();
        //JSONArray testnet=new JSONArray();
        //testnet.put(testnetid);
        params.put("", testnetid);
        jsonoutput = WhiteBlockJsonRPC.sendquery("build_status", params, context);
        if(jsonoutput.has("result"))
        {
            JSONObject resultObject=jsonoutput.getJSONObject("result");
            if(resultObject.has("progress")) {
                traceInfo(context, "progress found ");
                jsonCustomer = resultObject.getDouble("progress");
                traceInfo(context, "Value  " + String.valueOf(jsonCustomer));
            }
            else
                traceInfo(context,"progress not found ");
        }
        else
            throw new WhiteBlockHttpException("NO result object in the rpc response : "+jsonoutput);

        return jsonCustomer;
    }
    public static WhiteblockAccountList getAccountLis( WhiteblockHttpContext context, String testnetid) throws Exception {
        String jsonCustomer;
        Gson gson=new Gson();
        String output;
        JSONObject jsonoutput;

        Map<String ,Object> params=new HashMap<>();
        //JSONArray testnet=new JSONArray();
        //testnet.put(testnetid);
        params.put("",testnetid);
        jsonoutput= WhiteBlockJsonRPC.sendquery("state::info",params, context);
        JSONObject jsonResults;
        if(jsonoutput.has("result"))
        {
            jsonResults=jsonoutput.getJSONObject("result");
        }
        else
            jsonResults=new JSONObject();
        if(context.getTracemode().isPresent())
            context.getContext().getLogger().debug("Jsonoutpup :"+jsonoutput.toString());

        List<WhiteblockAccount> accountList=new ArrayList<>();
        for(Iterator iterator = jsonResults.keys(); iterator.hasNext();) {
            String accountadress = (String) iterator.next();
            if(REGEXP_PATERN.matcher(accountadress).matches()) {
                if (jsonResults.getJSONObject(accountadress) instanceof JSONObject) {
                    if (jsonResults.getJSONObject(accountadress).has("privateKey")) {
                        JSONObject accountobject = jsonResults.getJSONObject(accountadress);
                        WhiteblockAccount account = new WhiteblockAccount(accountadress, "0", "0", accountobject.getString("privateKey"), accountobject.getString("publicKey"));
                        accountList.add(account);
                    }
                }
            }
        }
        WhiteblockAccountList whiteblockAccountList= new WhiteblockAccountList(accountList);

        return whiteblockAccountList;
    }

    public static WhiteblockContractList getContractList(WhiteblockHttpContext context,String testnetid) throws Exception {
        String jsonCustomer;
        Gson gson=new Gson();
        JSONObject jsonoutput;

        Map<String ,Object> params=new HashMap<>();
        params.put("",testnetid);
        jsonoutput= WhiteBlockJsonRPC.sendquery("state::info",params, context);
        JSONObject jsonResults;
        JSONArray jsoncontractarray;
        if(jsonoutput.has("result"))
        {
            jsonResults=jsonoutput.getJSONObject("result");
            if(jsonResults.has("contracts"))
            {
                jsoncontractarray=jsonResults.getJSONArray("contracts");
            }
            else
                jsoncontractarray=new JSONArray();
        }
        else
            jsoncontractarray=new JSONArray();
      //  jsonCustomer=sendCommand(context,Arrays.asList("wb", "get", "contracts","list"));
        jsonCustomer="{ \"whiteblockContractList\":"+jsoncontractarray.toString()+"}";

        return gson.fromJson(jsonCustomer, WhiteblockContractList.class);

    }


    public static WhiteblockNodeList getNodeLis(WhiteblockHttpContext context,String testnetid) throws Exception {
        String jsonCustomer;
        Gson gson=new Gson();
        JSONObject jsonoutput;

        Map<String ,Object> params=new HashMap<>();
        params.put("",testnetid);
        jsonoutput= WhiteBlockJsonRPC.sendquery("status_nodes",params, context);
        JSONArray jsonResults;
        if(jsonoutput.has("result"))
        {
            jsonResults=jsonoutput.getJSONArray("result");
        }
        else
            jsonResults=new JSONArray();
        if(context.getTracemode().isPresent())
            context.getContext().getLogger().debug("Jsonoutpup :"+jsonoutput.toString());

        //jsonCustomer=sendCommand(context,Arrays.asList("wb", "get", "nodes", "info"));
        jsonCustomer="{ \"whiteblockNodeList\":"+jsonResults.toString()+"}";

        return gson.fromJson(jsonCustomer, WhiteblockNodeList.class);

    }


    public static WhiteblockMonitoringData getMonitoringData(WhiteblockHttpContext context,long timestampstart,long timestartend) throws Exception {
        String jsonCustomer;
        Gson gson=new Gson();
        JSONObject jsonoutput;

        Map<String ,Object> params=new HashMap<>();
        params.put("startTime",timestampstart);
        params.put("endTime",timestartend);
        params.put("startBlock",0);
        params.put("endBlock",0);
        //jsonCustomer=sshCommand(context,Arrays.asList("wb", "get", "stats", "time",String.valueOf(timestartend),String.valueOf(timestartend)));
        jsonoutput= WhiteBlockJsonRPC.sendquery("stats",params, context);
        JSONObject jsonResults;
        if(jsonoutput.has("result"))
        {
            jsonResults=jsonoutput.getJSONObject("result");
        }
        else
            jsonResults=new JSONObject();
        if(context.getTracemode().isPresent())
            context.getContext().getLogger().debug("Jsonoutpup :"+jsonoutput.toString());

//        jsonCustomer=sendCommand(context,Arrays.asList("wb", "get", "stats", "past","5"));
        if(jsonoutput.toString().contains(ERROR))
            throw new WhiteblockConnectionException("MONITORING error :" +jsonResults.toString());

        return gson.fromJson(jsonResults.toString(), WhiteblockMonitoringData.class);

    }

    public static String enableNetConfigModule(WhiteblockHttpContext context,String testnetID) throws Exception {
        String output;
        ////find the object on all inteface-
        // -
        JSONObject jsonoutput;
        Map<String ,Object> params=new HashMap<>();
        JSONObject jsonObject;
        jsonObject= (JSONObject) context.getContext().getCurrentVirtualUser().get("Networks_All");
        String method;

        params.put("",testnetID);
        if(jsonObject!=null)
        {
            method="netem_all";
            ///-----case of a single object
            params.put("",jsonObject);
            context.getContext().getCurrentVirtualUser().remove("Networks_All");
        }
        else
        {
            method="netem";
            //---case of several network profile on several nodes
            int i=0;
            String key="";
            jsonObject= (JSONObject) context.getContext().getCurrentVirtualUser().get("Networks_"+String.valueOf(i));
            while(jsonObject!=null)
            {
                key=key+" ";
                params.put(key,jsonObject);
                traceInfo(context,"network object found "+jsonObject.toString());
                context.getContext().getCurrentVirtualUser().remove("Networks_"+String.valueOf(i));
                i++;
                jsonObject= (JSONObject) context.getContext().getCurrentVirtualUser().get("Networks_"+String.valueOf(i));

            }
        }


        jsonoutput= WhiteBlockJsonRPC.sendquery(method,params, context);
        if(context.getTracemode().isPresent())
            context.getContext().getLogger().debug("Jsonoutpup :"+jsonoutput.toString());


        return jsonoutput.toString();
    }
    public  static String disableNetConfigModule(WhiteblockHttpContext context,String testnetid) throws Exception {
        String output;
        JSONObject jsonoutput;

        Map<String ,Object> params=new HashMap<>();

        params.put("",testnetid);
        jsonoutput= WhiteBlockJsonRPC.sendquery("netem_delete",params, context);
        JSONObject jsonResults;
        if(jsonoutput.has("result"))
        {
            jsonResults=jsonoutput.getJSONObject("result");
        }
        else
            jsonResults=new JSONObject();
        if(context.getTracemode().isPresent())
            context.getContext().getLogger().debug("Jsonoutpup :"+jsonoutput.toString());


        return jsonResults.toString();
    }
    public static void defineLossOnNodes(WhiteblockHttpContext context, Optional<Integer> nodeid, double loss) throws IOException, InterruptedException, WhiteblockConnectionException, JSONException {
        if(nodeid.isPresent())
        {
            JSONObject jsonObject= (JSONObject) context.getContext().getCurrentVirtualUser().get("Networks_"+String.valueOf(nodeid.get()));
            if(jsonObject==null)
            {
                traceInfo(context,"network object for node "+nodeid+"not found find");
                jsonObject=new JSONObject();
                jsonObject.put("node",nodeid.get());
                jsonObject.put("loss",loss);
                context.getContext().getCurrentVirtualUser().put("Networks_"+String.valueOf(nodeid.get()),jsonObject);
            }
            else
            {
                traceInfo(context,"network object for node "+nodeid+"find");
                jsonObject.put("loss",loss);
            }

        }else {
            JSONObject jsonObject= (JSONObject) context.getContext().getCurrentVirtualUser().get("Networks_All");
            if(jsonObject==null)
            {
                traceInfo(context,"network object for all nodes not found ");

                jsonObject=new JSONObject();
                jsonObject.put("loss",loss);
                context.getContext().getCurrentVirtualUser().put("Networks_All",jsonObject);
            }
            else
            {
                traceInfo(context,"network object for all nodes  found ");
                jsonObject.put("loss",loss);
            }
        }

    }

    public static String getNetID(WhiteblockHttpContext context) throws Exception {
        String testnetid=(String)context.getContext().getCurrentVirtualUser().get("testnet");
        if(testnetid==null)
        {
            testnetid=getTestnetID(context);
            context.getContext().getCurrentVirtualUser().put("testnet",testnetid);
        }
        return testnetid;

    }
    public static void setTestID(WhiteblockHttpContext context,String testid)
    {
        context.getContext().getCurrentVirtualUser().put("testnet",testid);
    }

    public static String getTestnetID(WhiteblockHttpContext context) throws Exception {
        JSONObject jsonoutput;

        Map<String ,Object> params=new HashMap<>();

        //jsonCustomer=sshCommand(context,Arrays.asList("wb", "get", "stats", "time",String.valueOf(timestartend),String.valueOf(timestartend)));
        jsonoutput= WhiteBlockJsonRPC.sendquery("get_last_build",params, context);
        if(context.getTracemode().isPresent())
            context.getContext().getLogger().debug("Jsonoutpup :"+jsonoutput.toString());

        if(jsonoutput.has("result"))
        {
            JSONObject result=jsonoutput.getJSONObject("result");
            if(result.has("id"))
                return result.getString("id");
            else
                return null;
        }
        else
            return null;
    }

    public static void defineDelayOnNodes(WhiteblockHttpContext context,Optional<Integer> nodeid,int delay) throws IOException, InterruptedException, WhiteblockConnectionException, JSONException {
        if(nodeid.isPresent())
        {
            JSONObject jsonObject= (JSONObject) context.getContext().getCurrentVirtualUser().get("Networks_"+String.valueOf(nodeid.get()));
            if(jsonObject==null)
            {
                traceInfo(context,"network object for node "+nodeid+"not found find");

                jsonObject=new JSONObject();
                jsonObject.put("node",nodeid.get());
                jsonObject.put("delay",delay);
                context.getContext().getCurrentVirtualUser().put("Networks_"+String.valueOf(nodeid.get()),jsonObject);
            }
            else
            {
                traceInfo(context,"network object for node "+nodeid+" found find");

                jsonObject.put("delay",delay);
            }

        }else {
            JSONObject jsonObject= (JSONObject) context.getContext().getCurrentVirtualUser().get("Networks_All");
            if(jsonObject==null)
            {
                traceInfo(context,"network object for all nodes not found find");

                jsonObject=new JSONObject();
                jsonObject.put("delay",delay);
                context.getContext().getCurrentVirtualUser().put("Networks_All",jsonObject);
            }
            else
            {
                traceInfo(context,"network object for all nodes found find");

                jsonObject.put("delay",delay);
            }
        }

    }

    public static void defineLimitOnNodes(WhiteblockHttpContext context,Optional<Integer> nodeid,int delay) throws IOException, InterruptedException, WhiteblockConnectionException, JSONException {
        if(nodeid.isPresent())
        {
            JSONObject jsonObject= (JSONObject) context.getContext().getCurrentVirtualUser().get("Networks_"+String.valueOf(nodeid.get()));
            if(jsonObject==null)
            {
                traceInfo(context,"network object for node "+nodeid+"not found find");

                jsonObject=new JSONObject();
                jsonObject.put("node",nodeid.get());
                jsonObject.put("limit",delay);
                context.getContext().getCurrentVirtualUser().put("Networks_"+String.valueOf(nodeid.get()),jsonObject);
            }
            else
            {
                traceInfo(context,"network object for node "+nodeid+" found find");

                jsonObject.put("limit",delay);
            }

        }else {
            JSONObject jsonObject= (JSONObject) context.getContext().getCurrentVirtualUser().get("Networks_All");
            if(jsonObject==null)
            {
                traceInfo(context,"network object for all nodes not found find");

                jsonObject=new JSONObject();
                jsonObject.put("limit",delay);
                context.getContext().getCurrentVirtualUser().put("Networks_All",jsonObject);
            }
            else
            {
                traceInfo(context,"network object for all nodes  found find");

                jsonObject.put("limit",delay);
            }
        }

    }

    public static void defineBwOnNodes(WhiteblockHttpContext context,Optional<Integer> nodeid,int bw,Optional<String> unit) throws IOException, InterruptedException, WhiteblockConnectionException, JSONException {
        if(nodeid.isPresent())
        {
            JSONObject jsonObject= (JSONObject) context.getContext().getCurrentVirtualUser().get("Networks_"+String.valueOf(nodeid.get()));
            if(jsonObject==null)
            {
                traceInfo(context,"network object for node "+nodeid+"not found find");

                jsonObject=new JSONObject();
                jsonObject.put("node",nodeid.get());
                jsonObject.put("rate",String.valueOf(bw)+unit.get());
                context.getContext().getCurrentVirtualUser().put("Networks_"+String.valueOf(nodeid.get()),jsonObject);
            }
            else
            {
                traceInfo(context,"network object for node "+nodeid+" found find");

                jsonObject.put("rate",String.valueOf(bw)+unit.get());

            }

        }else {
            JSONObject jsonObject= (JSONObject) context.getContext().getCurrentVirtualUser().get("Networks_All");
            if(jsonObject==null)
            {
                traceInfo(context,"network object for all nodes not found find");

                jsonObject=new JSONObject();
                jsonObject.put("rate",String.valueOf(bw)+unit.get());

                context.getContext().getCurrentVirtualUser().put("Networks_All",jsonObject);
            }
            else
            {
                traceInfo(context,"network object for all nodes  found find");
                jsonObject.put("rate",String.valueOf(bw)+unit.get());

            }
        }

    }

    public static String minterStart(WhiteblockHttpContext context) throws Exception {
        String output;
        JSONObject jsonoutput;
        Map<String ,Object> params=new HashMap<>();
        jsonoutput= WhiteBlockJsonRPC.sendquery("start_mining",params, context);
        JSONObject jsonResults;
        if(!jsonoutput.has("result"))
        {
            throw new WhiteBlockHttpException("Cannot find results in the payload");
        }


        if(context.getTracemode().isPresent())
            context.getContext().getLogger().debug("Jsonoutpup :"+jsonoutput.toString());

        return jsonoutput.toString();
    }
    public static String minterStop(WhiteblockHttpContext context) throws Exception {
        String output;
        JSONObject jsonoutput;
        Map<String ,Object> params=new HashMap<>();
        jsonoutput= WhiteBlockJsonRPC.sendquery("stop_mining",params, context);
        JSONObject jsonResults;
        if(!jsonoutput.has("result"))
        {
            throw new WhiteBlockHttpException("Cannot find results in the payload");
        }

        if(context.getTracemode().isPresent())
            context.getContext().getLogger().debug("Jsonoutpup :"+jsonoutput.toString());

        return output=jsonoutput.toString();

    }
    private static JSONArray generateRessouceArray(int nodes)
    {
        // "resources": [
        //    {
        //      "boundCPUs": null,
        //      "cpus": "",
        //      "memory": "",
        //      "ports": [
        //        "8545:8545"
        //      ],
        //      "volumes": null
        //    },
        //    {
        //      "boundCPUs": null,
        //      "cpus": "",
        //      "memory": "",
        //      "ports": [
        //        "8546:8545"
        //      ],
        //      "volumes": null
        //    },
        //    {
        //      "boundCPUs": null,
        //      "cpus": "",
        //      "memory": "",
        //      "ports": [
        //        "8547:8545"
        //      ],
        //      "volumes": null
        //    }
        JSONArray ressJsonArray=new JSONArray();

        for(int i=0;i<nodes;i++)
        {
            JSONObject object=new JSONObject();
            object.put("boundCPUs",JSONObject.NULL);
            object.put("cpus","");
            object.put("memory","");
            JSONArray ports=new JSONArray();
            String port=String.valueOf(WhiteBlockConstants.DEFAULT_START_RPC_PORT+i)+":8545";
            ports.put(port);
            object.put("ports",ports);
            object.put("volumes",JSONObject.NULL);
            ressJsonArray.put(object);
        }
        return ressJsonArray;
    }
    public static String buildEnvironment(WhiteblockHttpContext context, String blockchain , String dockerImage, int numberofnodes) throws Exception {
 ////extraAccounts: 0-> in params

        String output;
        JSONObject jsonoutput;

        Map<String ,Object> params=new HashMap<>();
        JSONArray servers=new JSONArray();
        servers.put(1);
        params.put("servers",servers);

        params.put("blockchain",blockchain);
        JSONArray image_arrauy=new JSONArray();
        image_arrauy.put(dockerImage);
        params.put("images",image_arrauy);

        params.put("nodes",numberofnodes);

        params.put("resources",generateRessouceArray(numberofnodes));
        JSONObject buildparams=new JSONObject();
        buildparams.put("extraAccounts",0);
        params.put("params",buildparams);
        jsonoutput= WhiteBlockJsonRPC.sendquery("build",params, context);
        String jsonResults;
        if(jsonoutput.has("result"))
        {
            jsonResults=jsonoutput.getString("result");
        }
        else
            jsonResults="";

        if(context.getTracemode().isPresent())
            context.getContext().getLogger().debug("Jsonoutpup :"+jsonoutput.toString());

        //output=sendCommand(context,Arrays.asList("wb", "build", "-b", blockchain,"-n",String.valueOf(numberofnodes),"-c","0","-m","0","-y"));
        return jsonResults;
    }


}
