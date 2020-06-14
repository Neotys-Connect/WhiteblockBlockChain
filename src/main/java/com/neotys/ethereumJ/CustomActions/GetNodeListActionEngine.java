package com.neotys.ethereumJ.CustomActions;

import com.google.common.base.Optional;
import com.neotys.action.result.ResultFactory;
import com.neotys.ethereumJ.common.utils.Whiteblock.management.WhiteblockProcessBuilder;
import com.neotys.ethereumJ.common.utils.Whiteblock.rest.WhiteblockHttpContext;
import com.neotys.extensions.action.ActionParameter;
import com.neotys.extensions.action.engine.ActionEngine;
import com.neotys.extensions.action.engine.Context;
import com.neotys.extensions.action.engine.Logger;
import com.neotys.extensions.action.engine.SampleResult;

import java.util.List;
import java.util.Map;

import static com.neotys.action.argument.Arguments.getArgumentLogString;
import static com.neotys.action.argument.Arguments.parseArguments;

public class GetNodeListActionEngine implements ActionEngine {
    private static final String STATUS_CODE_INVALID_PARAMETER = "NL-WB_NODE_ACTION-01";
    private static final String STATUS_CODE_TECHNICAL_ERROR = "NL-WB_NODE_ACTION-02";
    private static final String STATUS_CODE_BAD_CONTEXT = "NL-WB_NODE_ACTION-03";
    @Override
    public SampleResult execute(Context context, List<ActionParameter> list) {
        final SampleResult sampleResult = new SampleResult();
        final StringBuilder responseBuilder = new StringBuilder();


        final Map<String, Optional<String>> parsedArgs;
        try {
            parsedArgs = parseArguments(list, GetNodesListOption.values());
        } catch (final IllegalArgumentException iae) {
            return ResultFactory.newErrorResult(context, STATUS_CODE_INVALID_PARAMETER, "Could not parse arguments: ", iae);
        }


        final Logger logger = context.getLogger();
        if (logger.isDebugEnabled()) {
            logger.debug("Executing " + this.getClass().getName() + " with parameters: "
                    + getArgumentLogString(parsedArgs, GetMonitoringDataOption.values()));
        }
        final String testID = parsedArgs.get(GetNodesListOption.TestID.getName()).get();
        final Optional<String> service = parsedArgs.get(GetNodesListOption.Service.getName());
        final Optional<String> proxyName=parsedArgs.get(GetNodesListOption.ProxyName.getName());
        final String accessToken = parsedArgs.get(BuildWhiteblockNetworkOption.
                AccessToken.getName()).get();
        final Optional<String> tracemode=parsedArgs.get((GetNodesListOption.TraceMode.getName()));

        try
        {
            WhiteblockHttpContext whiteBlockContext=new WhiteblockHttpContext(accessToken,tracemode,context,proxyName);
            String output;
            if(service.isPresent()) {
                output = WhiteblockProcessBuilder.listNodes(whiteBlockContext,testID, service.get()).generateOutPut();
            }else{
                output = WhiteblockProcessBuilder.listAllNodes(whiteBlockContext,testID).generateOutPut();
            }

            responseBuilder.append(output);
        }
        catch (Exception e)
        {
            return ResultFactory.newErrorResult(context, STATUS_CODE_BAD_CONTEXT, "Error encountered :", e);

        }
        sampleResult.setRequestContent("");
        sampleResult.setResponseContent(responseBuilder.toString());
        return sampleResult;
    }

    @Override
    public void stopExecute() {

    }
}
