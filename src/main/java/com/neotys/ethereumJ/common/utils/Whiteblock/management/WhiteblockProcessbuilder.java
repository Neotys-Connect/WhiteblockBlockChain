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
import com.neotys.ethereumJ.common.utils.Whiteblock.rest.WhiteBlockRestAPI;
import com.neotys.ethereumJ.common.utils.Whiteblock.rest.WhiteBlockHttpContext;
import com.neotys.extensions.action.engine.Context;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.regex.Pattern;

import static com.neotys.ethereumJ.common.utils.Whiteblock.Constants.ERROR;

public class WhiteblockProcessbuilder {
    private static ProcessBuilder processBuilder;

     private static void traceInfo(WhiteblockHttpContext context, String log)
    {
        if(context.getTracemode().isPresent())
        {
            if(context.getTracemode().get().toLowerCase().equals(WhiteBlockConstants.TRUE))
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
     * @param definitionFile The path to the definition file
     * @return The ids of the tests which have been created
     */
    public static List<String> build(WhiteblockHttpContext context, WhiteblockBuildMeta meta)
            throws Exception {

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
        // TODO: Implement me
    }
    
    /**
     * @brief Stop a test and cleanup resources
     * @details Stop a test and cleanup resources
     * 
     * @param context WhiteblockHttpContext
     * @param String The id of the test
     */
    public static void abortTest(WhiteblockHttpContext context, String testID)
            throws Exception {
        // TODO: Implement me
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
        // TODO: Implement me
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
        // TODO: Implement me
    }

}
