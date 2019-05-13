package com.neotys.ethereumJ.CustomActions;

import com.google.common.base.Function;
import com.google.common.base.Optional;
import com.google.common.collect.ImmutableList;
import com.google.common.primitives.Ints;
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

public class ApplyNetworkConstraintsActionEngine implements ActionEngine {
    private static final String STATUS_CODE_INVALID_PARAMETER = "NL-WB_NETWORKCONSTRAINT_ACTION-01";
    private static final String STATUS_CODE_TECHNICAL_ERROR = "NL-WB_NETWORKCONSTRAINT_ACTION-02";
    private static final String STATUS_CODE_BAD_CONTEXT = "NL-WB_NETWORKCONSTRAINT_ACTION-03";
    private static final String BANDWIDTH="bandwidth";
    private static final String DELAY="delay";
    private static final String LOSS ="loss";

    @Override
    public SampleResult execute(Context context, List<ActionParameter> list) {
        final SampleResult sampleResult = new SampleResult();
        final StringBuilder requestBuilder = new StringBuilder();
        final StringBuilder responseBuilder = new StringBuilder();


        final Map<String, Optional<String>> parsedArgs;
        try {
            parsedArgs = parseArguments(list, ApplyNetworkConstraintsOption.values());
        } catch (final IllegalArgumentException iae) {
            return ResultFactory.newErrorResult(context, STATUS_CODE_INVALID_PARAMETER, "Could not parse arguments: ", iae);
        }

        context.getLogger().debug("parseargs "+ parsedArgs.toString());


        final Logger logger = context.getLogger();
        if (logger.isDebugEnabled()) {
            logger.debug("Executing " + this.getClass().getName() + " with parameters: "
                    + getArgumentLogString(parsedArgs, ApplyNetworkConstraintsOption.values()));
        }

        final String whiteBlocMasterHost = parsedArgs.get(ApplyNetworkConstraintsOption.WhiteBlocMasterHost.getName()).get();
        final String typeofconstraints=parsedArgs.get(ApplyNetworkConstraintsOption.TypeOfConstraint.getName()).get();
        final String valueofConstraint =parsedArgs.get(ApplyNetworkConstraintsOption.ConstraintsValue.getName()).get();
        final Optional<String> nodenumber = parsedArgs.get(ApplyNetworkConstraintsOption.NodeNumber.getName());
        final Optional<String> unit = parsedArgs.get(ApplyNetworkConstraintsOption.ConstraintsUnit.getName());
        final Optional<String> tracemode=parsedArgs.get((ApplyNetworkConstraintsOption.TraceMode.getName()));


        if (!validateNetworkMode(typeofconstraints))
            return ResultFactory.newErrorResult(context, STATUS_CODE_INVALID_PARAMETER, "The network constraints available are : delay,loss, bandwidth ");

        if(typeofconstraints.equals(BANDWIDTH))
        {
            if(!unit.isPresent())
                return ResultFactory.newErrorResult(context, STATUS_CODE_INVALID_PARAMETER, "a unit is mandatory if using bandwidth ");
            else
            {
                if(!validateUnit(unit))
                    return ResultFactory.newErrorResult(context, STATUS_CODE_INVALID_PARAMETER, "unit can only have the following values : kbps,mbps,gpps ");

            }
        }

        try
        {
            String output = null;
            WhiteBlockContext whiteBlockContext=new WhiteBlockContext(whiteBlocMasterHost, WhiteBlockConstants.PASSWORD,tracemode,context);

            switch(typeofconstraints)
            {
                case BANDWIDTH:
                    output= WhiteblockProcessbuilder.defineBwOnNodes(whiteBlockContext,convert2OptionalInteger(nodenumber),Integer.parseInt(valueofConstraint),unit);
                    break;
                case LOSS:
                    output=WhiteblockProcessbuilder.defineLossOnNodes(whiteBlockContext,convert2OptionalInteger(nodenumber),Double.parseDouble(valueofConstraint));
                    break;
                case DELAY:
                    output=WhiteblockProcessbuilder.defineDelayOnNodes(whiteBlockContext,convert2OptionalInteger(nodenumber),Integer.parseInt(valueofConstraint));
                    break;
            }
 
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
        ImmutableList<String> minerMode=ImmutableList.of(DELAY,LOSS,BANDWIDTH);
        if(minerMode.contains(mode.toLowerCase()))
            return true;
        else
            return false;
    }

    private boolean validateUnit(Optional<String> unit)
    {
        ImmutableList<String> unitpossible=ImmutableList.of("kbps","mbps","gbps");
        if(unit.isPresent())
        {
            if( unitpossible.contains(unit.get()))
                return true;
            else
                return false;
        }
        else
            return false;
    }
    private Optional<Integer> convert2OptionalInteger(Optional<String> mode)
    {

        return mode.transform(STR_TO_INT_FUNCTION).or(Optional.<Integer>absent());
     }
    private static final Function<String, Optional<Integer>> STR_TO_INT_FUNCTION =
            new Function<String, Optional<Integer>>() {
                @Override
                public Optional<Integer> apply(final String input) {
                    return Optional.fromNullable(Ints.tryParse(input));
                }
            };
    @Override
    public void stopExecute() {

    }
}
