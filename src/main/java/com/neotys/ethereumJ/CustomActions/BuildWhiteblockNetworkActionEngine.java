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

import static com.google.common.base.Strings.emptyToNull;
import static com.neotys.action.argument.Arguments.getArgumentLogString;
import static com.neotys.action.argument.Arguments.parseArguments;

public class BuildWhiteblockNetworkActionEngine implements ActionEngine {
    private static final String STATUS_CODE_INVALID_PARAMETER = "NL-WB_BUILD_ACTION-01";
    private static final String STATUS_CODE_TECHNICAL_ERROR = "NL-WB_BUILD_ACTION-02";
    private static final String STATUS_CODE_BAD_CONTEXT = "NL-WB_BUILD_ACTION-03";
    private static final String VALIDATION="completed";
    @Override
    public SampleResult execute(Context context, List<ActionParameter> list) {
        final SampleResult sampleResult = new SampleResult();
        final StringBuilder requestBuilder = new StringBuilder();
        final StringBuilder responseBuilder = new StringBuilder();


        final Map<String, Optional<String>> parsedArgs;
        try {
            parsedArgs = parseArguments(list, BuildWhiteblockNetworkOption.values());
        } catch (final IllegalArgumentException iae) {
            return ResultFactory.newErrorResult(context, STATUS_CODE_INVALID_PARAMETER, "Could not parse arguments: ", iae);
        }


        final Logger logger = context.getLogger();
        if (logger.isDebugEnabled()) {
            logger.debug("Executing " + this.getClass().getName() + " with parameters: "
                    + getArgumentLogString(parsedArgs, GetMonitoringDataOption.values()));
        }

        final String whiteBlocMasterHost = parsedArgs.get(BuildWhiteblockNetworkOption.WhiteBlocMasterHost.getName()).get();
        final String whiteBlockRpcPort=parsedArgs.get(BuildWhiteblockNetworkOption.WhiteBlocRpcPort.getName()).get();
        final String whiteBlockRpctoken=parsedArgs.get(BuildWhiteblockNetworkOption.WhiteBlocRpctoken.getName()).get();
        final Optional<String> proxyName=parsedArgs.get(BuildWhiteblockNetworkOption.ProxyName.getName());
        final String dockerimage=parsedArgs.get(BuildWhiteblockNetworkOption.DockerImage.getName()).get();

        final String numberofnodes = parsedArgs.get(BuildWhiteblockNetworkOption.NumberOfNodes.getName()).get();
        final String typeofBlochacin = parsedArgs.get(BuildWhiteblockNetworkOption.TypeofBlochacin.getName()).get();
        final Optional<String> tracemode=parsedArgs.get((BuildWhiteblockNetworkOption.TraceMode.getName()));


        if(!validatetypeofBlockChain(typeofBlochacin))
            return ResultFactory.newErrorResult(context, STATUS_CODE_INVALID_PARAMETER, "INVALID parameter : "+ typeofBlochacin,null);


        try
        {
            WhiteblockHttpContext whiteBlockContext=new WhiteblockHttpContext(whiteBlocMasterHost,whiteBlockRpctoken, tracemode,context,whiteBlockRpcPort,proxyName);

            String output= WhiteblockProcessbuilder.buildEnvironment(whiteBlockContext,typeofBlochacin,dockerimage,Integer.parseInt(numberofnodes));
            if(output!=null)
            {
                double status;
                WhiteblockProcessbuilder.setTestID(whiteBlockContext,output);
                do{
                    Thread.sleep(500);
                    status=WhiteblockProcessbuilder.getBuildStatus(whiteBlockContext,output);
                    if(tracemode.isPresent())
                        context.getLogger().debug("current status "+String.valueOf(status));

                }while(status<100);

                responseBuilder.append("Network  created");
            }
            else
                return ResultFactory.newErrorResult(context, STATUS_CODE_TECHNICAL_ERROR, "unable to create the network  :"+output, null);

        }
        catch (Exception e)
        {
            return ResultFactory.newErrorResult(context, STATUS_CODE_BAD_CONTEXT, "Error encountered :", e);

        }

        sampleResult.setRequestContent(requestBuilder.toString());
        sampleResult.setResponseContent(responseBuilder.toString());
        return sampleResult;
    }


    private boolean validatetypeofBlockChain(String mode)
    {
        ImmutableList<String> minerMode=ImmutableList.of("ethereum", "parity","pantheon","syscoin", "rchain", "eos", "artemis", "beam", "cosmos");
        if(minerMode.contains(mode.toLowerCase()))
            return true;
        else
            return false;
    }
    @Override
    public void stopExecute() {

    }
}
