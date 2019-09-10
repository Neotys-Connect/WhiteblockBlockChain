package com.neotys.ethereumJ.CustomActions;

import com.google.common.base.Optional;
import com.google.common.collect.ImmutableList;
import com.neotys.action.result.ResultFactory;
import com.neotys.ethereumJ.common.utils.Whiteblock.management.WhiteBlockConstants;
import com.neotys.ethereumJ.common.utils.Whiteblock.management.WhiteBlockContext;
import com.neotys.ethereumJ.common.utils.Whiteblock.management.WhiteblockProcessbuilder;
import com.neotys.ethereumJ.common.utils.Whiteblock.rpc.WhiteblockHttpContext;
import com.neotys.extensions.action.ActionParameter;
import com.neotys.extensions.action.engine.ActionEngine;
import com.neotys.extensions.action.engine.Context;
import com.neotys.extensions.action.engine.Logger;
import com.neotys.extensions.action.engine.SampleResult;

import java.util.List;
import java.util.Map;

import static com.neotys.action.argument.Arguments.getArgumentLogString;
import static com.neotys.action.argument.Arguments.parseArguments;

public class ActivateNetworkConstraintsActionEngine implements ActionEngine {
    private static final String STATUS_CODE_INVALID_PARAMETER = "NL-WB_NETWORKACTIVATION_ACTION-01";
    private static final String STATUS_CODE_TECHNICAL_ERROR = "NL-WB_NETWORKACTIVATION_ACTION-02";
    private static final String STATUS_CODE_BAD_CONTEXT = "NL-WB_NETWORKACTIVATION_ACTION-03";
    @Override
    public SampleResult execute(Context context, List<ActionParameter> list) {
        final SampleResult sampleResult = new SampleResult();
        final StringBuilder requestBuilder = new StringBuilder();
        final StringBuilder responseBuilder = new StringBuilder();


        final Map<String, Optional<String>> parsedArgs;
        try {
            parsedArgs = parseArguments(list, ActivateNetworkConstraintsOption.values());
        } catch (final IllegalArgumentException iae) {
            return ResultFactory.newErrorResult(context, STATUS_CODE_INVALID_PARAMETER, "Could not parse arguments: ", iae);
        }



        final Logger logger = context.getLogger();
        if (logger.isDebugEnabled()) {
            logger.debug("Executing " + this.getClass().getName() + " with parameters: "
                    + getArgumentLogString(parsedArgs, GetMonitoringDataOption.values()));
        }

        final String whiteBlocMasterHost = parsedArgs.get(ActivateNetworkConstraintsOption.WhiteBlocMasterHost.getName()).get();
        final String whiteBlockRpcPort=parsedArgs.get(ActivateNetworkConstraintsOption.WhiteBlocRpcPort.getName()).get();
        final String whiteBlockRpctoken=parsedArgs.get(ActivateNetworkConstraintsOption.WhiteBlocRpctoken.getName()).get();
        final Optional<String> proxyName=parsedArgs.get(ActivateNetworkConstraintsOption.ProxyName.getName());

        final String networkMode=parsedArgs.get(ActivateNetworkConstraintsOption.NetworkMode.getName()).get();
        final Optional<String> tracemode=parsedArgs.get((ActivateNetworkConstraintsOption.TraceMode.getName()));

        if (!validateNetworkMode(networkMode))
            return ResultFactory.newErrorResult(context, STATUS_CODE_INVALID_PARAMETER, "the mode can have the following values : ON or OFF ");

        try
        {
            WhiteblockHttpContext whiteBlockContext=new WhiteblockHttpContext(whiteBlocMasterHost,whiteBlockRpctoken,tracemode,context,whiteBlockRpcPort,proxyName);
            String output;
            String testnetid=WhiteblockProcessbuilder.getNetID(whiteBlockContext);
            if(networkMode.toUpperCase().equals("ON"))
                output= WhiteblockProcessbuilder.enableNetConfigModule(whiteBlockContext,testnetid);
            else
                output=WhiteblockProcessbuilder.disableNetConfigModule(whiteBlockContext,testnetid);

            responseBuilder.append(output);
        }
        catch (Exception e)
        {
            return ResultFactory.newErrorResult(context, STATUS_CODE_BAD_CONTEXT, "Error encountered :", e);

        }
        sampleResult.setRequestContent(requestBuilder.toString());
        sampleResult.setResponseContent(responseBuilder.toString());
        return sampleResult;
    }



    private boolean validateNetworkMode(String mode)
    {
        ImmutableList<String> minerMode=ImmutableList.of("ON","OFF");
        if(minerMode.contains(mode.toUpperCase()))
            return true;
        else
            return false;
    }
    @Override
    public void stopExecute() {

    }
}
