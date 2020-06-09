package com.neotys.ethereumJ.common.utils.Whiteblock.management;

import com.neotys.ethereumJ.common.utils.Whiteblock.data.*;
import com.neotys.ethereumJ.common.utils.Whiteblock.rest.*;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.*;
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
        String rawResp = WhiteblockRestAPI.multipartRequest(context, String.format(FILE_UPLOAD_URI,orgID), files);
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
        // TODO: Implement me
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
        return null;
        // TODO: Implement me
    }

    public static boolean phasePassed(WhiteblockHttpContext context, String testID, String phase)
            throws Exception {
        String result = WhiteblockRestAPI.request("GET",
                String.format(PHASE_PASSED_URI,testID, phase),null,context);
        return result == "true";
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
        WhiteblockRestAPI.request("GET",
                String.format(STOP_TEST_URI,testID),null,context);
        return;
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
        for(int i = 0; i < lst.size(); i++) {
            WhiteblockNode node = lst.get(i);
            List<Integer> ports = node.getPorts();
            for(int j = 0; j < ports.size(); j++) {
                out.add(node.getIP()+":"+ports.get(j).toString());
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
        for(int i = 0; i < lst.size(); i++) {
            WhiteblockNode node = lst.get(i);
            List<Integer> ports = node.getPorts();
            for(int j = 0; j < ports.size(); j++) {
                out.add(node.getIP()+":"+ports.get(j).toString());
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

}