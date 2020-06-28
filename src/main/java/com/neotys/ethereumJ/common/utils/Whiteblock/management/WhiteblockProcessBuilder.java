package com.neotys.ethereumJ.common.utils.Whiteblock.management;

import com.neotys.ethereumJ.common.utils.Whiteblock.data.*;
import com.neotys.ethereumJ.common.utils.Whiteblock.http.WhiteBlockHttpException;
import com.neotys.ethereumJ.common.utils.Whiteblock.rest.WhiteblockHttpContext;
import com.neotys.ethereumJ.common.utils.Whiteblock.rest.WhiteblockRestAPI;
import com.neotys.ethereumJ.common.utils.Whiteblock.tools.Ethereum;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import static com.neotys.ethereumJ.common.utils.Whiteblock.management.WhiteblockConstants.*;

public class WhiteblockProcessBuilder {

     private static ProcessBuilder processBuilder;

     private static void traceInfo(WhiteblockHttpContext context, String log)
    {
        if(context.getTracemode().isPresent())
        {
            if(context.getTracemode().get().toLowerCase().equals(WhiteblockConstants.TRUE))
            {
                context.getContext().getLogger().info(log);
            }
        }
    }

    /**
     * @brief Build the testnets according to the give YAML
     * @details Build the testnets according to the give YAML
     * 
     * @param context WhiteblockHttpContext
     * @param meta The build meta data
     * @return The ids of the tests which have been created
     */
    public static List<String> build(WhiteblockHttpContext context, String orgID, WhiteblockBuildMeta meta)
            throws Exception {
        // Parse
        JSONArray rawFiles = WhiteblockRestAPI.jsonArrRequest("POST", PARSE_FILES_URI,
                meta.getDefinitionRaw(), context);
        List<String> files = new ArrayList<>();
        for(int i = 0; i < rawFiles.length(); i++) {
            files.add(rawFiles.getString(i));
        }
        // Upload the files
        String rawResp = WhiteblockRestAPI.multipartRequest(context, String.format(FILE_UPLOAD_URI,orgID),
                meta.getFolderPath(), files, null);
        JSONObject resp = new JSONObject(rawResp);
        String defID = resp.getJSONObject("data").getString("definitionID");
        JSONObject payload = meta.marshalJSON();
        JSONArray rawTestIDs = WhiteblockRestAPI.jsonArrRequest("POST",
                String.format(RUN_TEST_URI,orgID, defID),payload.toString(),context);
        List<String> testIDs = new ArrayList<>();
        for(int i = 0; i < rawTestIDs.length(); i++) {
            testIDs.add(rawTestIDs.getString(i));
        }
        return testIDs;
    }

    /**
     * @brief Build the testnets according to the give YAML
     * @details Build the testnets according to the give YAML
     *
     * @param context WhiteblockHttpContext
     * @param meta The build meta data
     * @return The ids of the tests which have been created
     */
    public static List<String> buildEthereum(WhiteblockHttpContext context, String orgID, WhiteblockBuildMeta meta,
                                             List<WhiteblockAccount> accounts)
            throws Exception {
        // Parse
        JSONObject genesis = Ethereum.createGenesis(accounts);
        JSONArray rawFiles = WhiteblockRestAPI.jsonArrRequest("POST", PARSE_FILES_URI,
                meta.getDefinitionRaw(), context);
        List<String> files = new ArrayList<>();
        if(rawFiles == null) {
            throw new WhiteblockLogicException("got back null for raw files");
        }

        for(int i = 0; i < rawFiles.length(); i++) {
            files.add(rawFiles.getString(i));
        }
        files.add("definition");
        List<WhiteblockPseudoFile> overrides =  new ArrayList<>();
        overrides.add(new WhiteblockPseudoFile("genesis.json",genesis.toString()));
        overrides.add(new WhiteblockPseudoFile("definition",meta.getDefinitionRaw()));
        // Upload the files
        String rawResp = WhiteblockRestAPI.multipartRequest(context, String.format(FILE_UPLOAD_URI, orgID),
                meta.getFolderPath(), files, overrides);
        if(rawResp!=null) {
            JSONObject resp = new JSONObject(rawResp);
            String defID = resp.getJSONObject("data").getString("definitionID");
            meta.addDomain(defID.replaceAll("-",""));//Use the definition ID as the domain base
            JSONObject payload = meta.marshalJSON();
            JSONArray rawTestIDs = WhiteblockRestAPI.jsonArrRequest("POST",
                    String.format(RUN_TEST_URI, orgID, defID), payload.toString(), context);
            List<String> testIDs = new ArrayList<>();
            for (int i = 0; i < rawTestIDs.length(); i++) {
                testIDs.add(rawTestIDs.getString(i));
            }
            return testIDs;
        }
        else
            throw new WhiteBlockHttpException("Getting issue when sending multipart request");
    }
    /**
     * @brief Get the build status of a test
     * @details Get the build status of a test
     * 
     * @param context WhiteblockHttpContext
     * @param testID The id of the test
     * 
     * @return An object describing the status of the test
     */
    public static WhiteblockStatus status(WhiteblockHttpContext context, String testID)
            throws Exception {

        JSONObject jResult  = WhiteblockRestAPI.jsonRequest("GET",String.format(STATUS_TEST_URI,testID),
                null, context);
        return new WhiteblockStatus(jResult);
    }

    public static boolean phasePassed(WhiteblockHttpContext context, String testID, String phase)
            throws Exception {
        String result = WhiteblockRestAPI.request("GET",
                String.format(PHASE_PASSED_URI,testID, phase),null,context);
        return result.contains("true");
    }
    
    /**
     * @brief Stop a test and cleanup resources
     * @details Stop a test and cleanup resources
     * 
     * @param context WhiteblockHttpContext
     * @param testID The id of the test
     */
    public static void abortTest(WhiteblockHttpContext context, String testID)
            throws Exception {
        WhiteblockRestAPI.request("POST",
                String.format(STOP_TEST_URI,testID),null,context);
    }

    /**
     * @brief Get all of the exposed tcp sockets on a test
     * @details Get all of the exposed tcp sockets on a test
     * 
     * @param context WhiteblockHttpContext
     * @param testID The id of the test
     * 
     * @return The endpoints in the form ip:port
     */
    public static List<String> tcpEndpoints(WhiteblockHttpContext context, String testID)
            throws Exception {
        WhiteblockNodeList allNodes = listAllNodes(context, testID);

        List<WhiteblockNode> lst = allNodes.getWhiteblockNodeList();
        List<String> out = new ArrayList<>();
        for (WhiteblockNode node : lst) {
            List<Integer> ports = node.getPorts();
            for (Integer port : ports) {
                out.add(node.getIP() + ":" + port.toString());
            }
        }
        return out;
    }


     /**
     * @brief Get all of the exposed tcp sockets on a test by service
     * @details Get all of the exposed tcp sockets on a test by service
     * 
     * @param context WhiteblockHttpContext
     * @param testID The id of the test
     * @param service The service type which is being targeted
     * 
     * @return The endpoints in the form ip:port
     */
    public static List<String> tcpEndpointsByService(WhiteblockHttpContext context, 
        String testID, String service) throws Exception {
        WhiteblockNodeList allNodes = listNodes(context, testID, service);

        List<WhiteblockNode> lst = allNodes.getWhiteblockNodeList();
        List<String> out = new ArrayList<>();
        for (WhiteblockNode node : lst) {
            List<Integer> ports = node.getPorts();
            for (Integer port : ports) {
                out.add(node.getIP() + ":" + port.toString());
            }
        }
        return out;
    }

    public static WhiteblockNodeList listAllNodes(WhiteblockHttpContext context, String testID)
        throws Exception {
        JSONArray result = WhiteblockRestAPI.jsonArrRequest("GET",
                String.format(NODE_ENDPOINTS_URI,testID),null,context);
        return new WhiteblockNodeList(result);
    }

    public static WhiteblockNodeList listNodes(WhiteblockHttpContext context, String testID, String service)
        throws Exception {
        JSONArray result = WhiteblockRestAPI.jsonArrRequest("GET",
                String.format(NODE_ENDPOINTS_FILTERED_URI,testID, service),null,context);
        return new WhiteblockNodeList(result);
    }

    /**
     * @brief This fetches the monitoring data from a helper. It makes a call to the Whiteblock API to determine the
     * location of this helper, if it exists at all
     * @param context WhiteblockHttpContext
     * @param testID The id of the test
     * @param startBlock The block to get the monitoring data starting at
     * @param endBlock The last block to include in the monitoring data calculations
     * @return
     */
    public static WhiteblockMonitoringData getEthMonitoringData(WhiteblockHttpContext context, String testID,
                                                                int startBlock, int endBlock)
        throws Exception {
        WhiteblockNodeList nodeList = listNodes(context, testID, "record");
        List<WhiteblockNode> nodes = nodeList.getWhiteblockNodeList();
        if (nodes.size() == 0 ){
            throw new WhiteblockLogicException("cannot get metrics, missing the record service helper");
        }
        WhiteblockNode record = nodes.get(0);
        if (record.getPorts().size() != 1) {
            throw new WhiteblockLogicException("the record service helper should have exactly one port binding."+
                    " Note: record listens on :8080 by default");
        }
        String url = "https://" + record.getIP() + ":" + record.getPorts().get(0).toString();
        url += String.format("/stats/block/%d/%d",startBlock, endBlock);
        return new WhiteblockMonitoringData(WhiteblockRestAPI.jRequest("GET",url,null,context));

    }

}
