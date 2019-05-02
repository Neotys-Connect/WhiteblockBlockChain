package com.neotys.ethereumJ.CustomActions;

import com.google.common.base.Optional;
import com.google.common.collect.ImmutableList;
import com.neotys.action.result.ResultFactory;
import com.neotys.ethereumJ.common.utils.Whiteblock.management.WhiteBlockConstants;
import com.neotys.ethereumJ.common.utils.Whiteblock.management.WhiteBlockContext;
import com.neotys.ethereumJ.common.utils.Whiteblock.management.WhiteblockProcessbuilder;
import com.neotys.extensions.action.ActionParameter;
import com.neotys.extensions.action.engine.ActionEngine;
import com.neotys.extensions.action.engine.Context;
import com.neotys.extensions.action.engine.Logger;
import com.neotys.extensions.action.engine.SampleResult;

import java.util.List;
import java.util.Map;

import static com.neotys.action.argument.Arguments.getArgumentLogString;
import static com.neotys.action.argument.Arguments.parseArguments;

public class MinerActionEngine implements ActionEngine {
    private static final String STATUS_CODE_INVALID_PARAMETER = "NL-WB_MINER_ACTION-01";
    private static final String STATUS_CODE_TECHNICAL_ERROR = "NL-WB_MINER_ACTION-02";
    private static final String STATUS_CODE_BAD_CONTEXT = "NL-WB_MINER_ACTION-03";
    @Override
    public SampleResult execute(Context context, List<ActionParameter> list) {
        final SampleResult sampleResult = new SampleResult();
        final StringBuilder requestBuilder = new StringBuilder();
        final StringBuilder responseBuilder = new StringBuilder();


        final Map<String, Optional<String>> parsedArgs;
        try {
            parsedArgs = parseArguments(list, MinerStartOption.values());
        } catch (final IllegalArgumentException iae) {
            return ResultFactory.newErrorResult(context, STATUS_CODE_INVALID_PARAMETER, "Could not parse arguments: ", iae);
        }



        final Logger logger = context.getLogger();
        if (logger.isDebugEnabled()) {
            logger.debug("Executing " + this.getClass().getName() + " with parameters: "
                    + getArgumentLogString(parsedArgs, GetMonitoringDataOption.values()));
        }

        final String whiteBlocMasterHost = parsedArgs.get(MinerStartOption.WhiteBlocMasterHost.getName()).get();
        final String minerMode=parsedArgs.get(MinerStartOption.MinerMode.getName()).get();
        final Optional<String> tracemode=parsedArgs.get((MinerStartOption.TraceMode.getName()));

        if (!validateMinerMode(minerMode))
            return ResultFactory.newErrorResult(context, STATUS_CODE_INVALID_PARAMETER, "AggregationType needs to be define or equal to\"MIN\",\"MAX\",\"SUM\",\"AVG\",\"MEDIAN\",\"COUNT\",\"PERCENTILE\" ");

        try
        {
            String output;
            WhiteBlockContext whiteBlockContext=new WhiteBlockContext(whiteBlocMasterHost, WhiteBlockConstants.PASSWORD,tracemode,context);

            if(minerMode.toUpperCase().equals("ON"))
                output= WhiteblockProcessbuilder.minterStart(whiteBlockContext);
            else
                output=WhiteblockProcessbuilder.minterStop(whiteBlockContext);

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
    private boolean validateMinerMode(String mode)
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
