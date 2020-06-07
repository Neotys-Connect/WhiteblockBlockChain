package com.neotys.ethereumJ.CustomActions;

import com.google.common.base.Optional;
import com.neotys.action.result.ResultFactory;
import com.neotys.ethereumJ.Web3J.Web3JContext;
import com.neotys.ethereumJ.Web3J.Web3JUtils;
import com.neotys.extensions.action.ActionParameter;
import com.neotys.extensions.action.engine.ActionEngine;
import com.neotys.extensions.action.engine.Context;
import com.neotys.extensions.action.engine.Logger;
import com.neotys.extensions.action.engine.SampleResult;

import java.util.List;
import java.util.Map;

import static com.neotys.action.argument.Arguments.getArgumentLogString;
import static com.neotys.action.argument.Arguments.parseArguments;

public class GetBalanceActionEngine implements ActionEngine {
    private static final String STATUS_CODE_INVALID_PARAMETER = "NL-WB_GETBALANCEACTION_ACTION-01";
    private static final String STATUS_CODE_TECHNICAL_ERROR = "NL-WB_GETBALANCEACTION_ACTION-02";
    private static final String STATUS_CODE_BAD_CONTEXT = "NL-WB_GETBALANCEACTION_ACTION-03";

    @Override
    public SampleResult execute(Context context, List<ActionParameter> parameters) {
        final SampleResult sampleResult = new SampleResult();
        final StringBuilder requestBuilder = new StringBuilder();
        final StringBuilder responseBuilder = new StringBuilder();
        final Map<String, Optional<String>> parsedArgs;
        try {
            parsedArgs = parseArguments(parameters, GetBalanceOption.values());
        } catch (final IllegalArgumentException iae) {
            return ResultFactory.newErrorResult(context, STATUS_CODE_INVALID_PARAMETER, "Could not parse arguments: ", iae);
        }


        final Logger logger = context.getLogger();
        if (logger.isDebugEnabled()) {
            logger.debug("Executing " + this.getClass().getName() + " with parameters: "
                    + getArgumentLogString(parsedArgs, GetBalanceOption.values()));
        }
        final String nodeIP = parsedArgs.get(GetBalanceOption.nodeIP.getName()).get();
        final String nodePort = parsedArgs.get(GetBalanceOption.nodePort.getName()).get();

        final String address = parsedArgs.get(GetBalanceOption.address.getName()).get();
        //final Optional<String> traceMode = parsedArgs.get(GetBalanceOption.TraceMode.getName());

        sampleResult.sampleStart();
        try {
            Web3JContext ctx = new Web3JContext(nodeIP,nodePort,context);
            Web3JUtils w3 = new Web3JUtils(ctx);
            String balance = w3.getBalanceEther(address).toPlainString();

            appendLineToStringBuilder(responseBuilder, "Balance of the account in Ether  : " + balance);
        } catch (Exception e) {
            return ResultFactory.newErrorResult(context, STATUS_CODE_BAD_CONTEXT, "Error encountered :", e);

        }


        sampleResult.sampleEnd();

        sampleResult.setRequestContent(requestBuilder.toString());
        sampleResult.setResponseContent(responseBuilder.toString());
        return sampleResult;
    }

    private void appendLineToStringBuilder(final StringBuilder sb, final String line) {
        sb.append(line).append("\n");
    }

    /**
     * This method allows to easily create an error result and log exception.
     */
    private static SampleResult getErrorResult(final Context context, final SampleResult result, final String errorMessage, final Exception exception) {
        result.setError(true);
        result.setStatusCode("NL-SendContractTransaction_ERROR");
        result.setResponseContent(errorMessage);
        if (exception != null) {
            context.getLogger().error(errorMessage, exception);
        } else {
            context.getLogger().error(errorMessage);
        }
        return result;
    }


    @Override
    public void stopExecute() {
        // TODO add code executed when the test have to stop.
    }
}

