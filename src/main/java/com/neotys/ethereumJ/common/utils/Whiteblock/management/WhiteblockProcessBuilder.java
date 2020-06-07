package com.neotys.ethereumJ.common.utils.Whiteblock.management;

import com.neotys.ethereumJ.common.utils.Whiteblock.data.*;
import com.neotys.ethereumJ.common.utils.Whiteblock.rest.*;
import java.util.*;

public class WhiteblockProcessBuilder {
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
     * @return The ids of the tests which have been created
     */
    public static List<String> build(WhiteblockHttpContext context, WhiteblockBuildMeta meta)
            throws Exception {
        return null;
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
    
    /**
     * @brief Stop a test and cleanup resources
     * @details Stop a test and cleanup resources
     * 
     * @param context WhiteblockHttpContext
     * @param testID The id of the test
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
        return null;
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
        return null;
        // TODO: Implement me
    }

    public static WhiteblockNodeList listAllNodes(WhiteblockHttpContext context, String testID)
        throws Exception {
        // TODO: Implement me
        return null;
    }

    public static WhiteblockNodeList listNodes(WhiteblockHttpContext context, String testID, String service)
        throws Exception {
        // TODO: Implement me
        return null;
    }

}
