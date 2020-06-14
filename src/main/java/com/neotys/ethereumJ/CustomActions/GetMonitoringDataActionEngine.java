package com.neotys.ethereumJ.CustomActions;

import com.google.common.base.Optional;
import com.neotys.action.result.ResultFactory;
import com.neotys.ascode.swagger.client.ApiException;
import com.neotys.ethereumJ.common.utils.Whiteblock.Constants;
import com.neotys.ethereumJ.common.utils.Whiteblock.monitoring.WhiteblockDataToNeoLoad;
import com.neotys.ethereumJ.common.utils.Whiteblock.rest.WhiteblockHttpContext;
import com.neotys.extensions.action.ActionParameter;
import com.neotys.extensions.action.engine.ActionEngine;
import com.neotys.extensions.action.engine.Context;
import com.neotys.extensions.action.engine.Logger;
import com.neotys.extensions.action.engine.SampleResult;
import com.neotys.rest.dataexchange.client.DataExchangeAPIClient;
import com.neotys.rest.dataexchange.client.DataExchangeAPIClientFactory;
import com.neotys.rest.dataexchange.model.ContextBuilder;
import com.neotys.rest.error.NeotysAPIException;
import org.apache.olingo.odata2.api.exception.ODataException;

import java.io.IOException;
import java.net.URISyntaxException;
import java.security.GeneralSecurityException;
import java.util.List;
import java.util.Map;

import static com.google.common.base.Strings.emptyToNull;
import static com.neotys.action.argument.Arguments.getArgumentLogString;
import static com.neotys.action.argument.Arguments.parseArguments;

public class GetMonitoringDataActionEngine implements ActionEngine {
    private static final String STATUS_CODE_INVALID_PARAMETER = "NL-WB_MONITORING_ACTION-01";
    private static final String STATUS_CODE_TECHNICAL_ERROR = "NL-WB_MONITORING_ACTION-02";
    private static final String STATUS_CODE_BAD_CONTEXT = "NL-WB_MONITORING_ACTION-03";

    @Override
    public SampleResult execute(Context context, List<ActionParameter> parameters) {
        final SampleResult sampleResult = new SampleResult();


        final Map<String, Optional<String>> parsedArgs;
        try {
            parsedArgs = parseArguments(parameters, GetMonitoringDataOption.values());
        } catch (final IllegalArgumentException iae) {
            return ResultFactory.newErrorResult(context, STATUS_CODE_INVALID_PARAMETER, "Could not parse arguments: ", iae);
        }


        final Logger logger = context.getLogger();
        if (logger.isDebugEnabled()) {
            logger.debug("Executing " + this.getClass().getName() + " with parameters: "
                    + getArgumentLogString(parsedArgs, GetMonitoringDataOption.values()));
        }
        final String bearerToken=parsedArgs.get(GetMonitoringDataOption.AccessToken.getName()).get();
        final Optional<String> proxyName=parsedArgs.get(GetMonitoringDataOption.ProxyName.getName());

        final Optional<String> dataExchangeApiKey = parsedArgs.get(GetMonitoringDataOption.NeoLoadDataExchangeApiKey.getName());
        final String dataExchangeApiUrl = Optional.fromNullable(emptyToNull(parsedArgs.get(GetMonitoringDataOption.NeoLoadDataExchangeApiUrl.getName()).orNull()))
                .or(() -> getDefaultDataExchangeApiUrl(context));
        final Optional<String> tracemode=parsedArgs.get((GetMonitoringDataOption.TraceMode.getName()));

        if (context.getLogger().isDebugEnabled()) {
            context.getLogger().debug("Data Exchange API URL used: " + dataExchangeApiUrl);
        }
        // We should use the block numbers rather than the time, due to potential time sync issues.
        // It also provides more consistent data.
        Object whiteblockLastBlockNumber = context.getCurrentVirtualUser().get(Constants.WHITEBLOCK_LAST_BLOCK_NUMBER);
        final Integer whiteblockCurrentBlockNumber = (int)whiteblockLastBlockNumber + Constants.WHITEBLOCK_MAX_STATS_BLOCKS;
        try {

            sampleResult.sampleStart();
            // Check last execution time (and fail if called less than 45 seconds ago).
            // TODO: Determine if we still need to enforce this 45 second rule

            WhiteblockHttpContext whiteBlockContext=new WhiteblockHttpContext(bearerToken,tracemode,context,proxyName);
            WhiteblockDataToNeoLoad whiteblockDataToNeoLoad=new WhiteblockDataToNeoLoad(whiteBlockContext,
                    (int)whiteblockLastBlockNumber, whiteblockCurrentBlockNumber, Optional.absent());
            whiteblockDataToNeoLoad.sendToNeoLoadWeb();

            sampleResult.sampleEnd();
        } catch (ApiException e) {
            return ResultFactory.newErrorResult(context, STATUS_CODE_TECHNICAL_ERROR, "API ERROR  : "+e.getCode() + " body : "+ e.getResponseBody(), e);
        }

        catch (Exception e) {
            return ResultFactory.newErrorResult(context, STATUS_CODE_TECHNICAL_ERROR, "Error encountered :", e);
        }

        context.getCurrentVirtualUser().put(Constants.WHITEBLOCK_LAST_BLOCK_NUMBER, whiteblockCurrentBlockNumber);

        sampleResult.setRequestContent("");
        sampleResult.setResponseContent("");
        return sampleResult;
    }

    @Override
    public void stopExecute() {

    }
    private String getDefaultDataExchangeApiUrl(final Context context) {
        return "http://" + context.getControllerIp() + ":7400/DataExchange/v1/Service.svc/";
    }
    private DataExchangeAPIClient getDataExchangeAPIClient(final Context context, final StringBuilder requestBuilder, final String dataExchangeApiUrl, final Optional<String> dataExchangeApiKey) throws GeneralSecurityException, IOException, ODataException, URISyntaxException, NeotysAPIException {
        DataExchangeAPIClient dataExchangeAPIClient = (DataExchangeAPIClient) context.getCurrentVirtualUser().get(Constants.NL_DATA_EXCHANGE_API_CLIENT);
        if (dataExchangeAPIClient == null) {
            final ContextBuilder contextBuilder = new ContextBuilder();
            contextBuilder.hardware(Constants.NEOLOAD_CONTEXT_HARDWARE).location(Constants.NEOLOAD_CONTEXT_LOCATION).software(
                    Constants.NEOLOAD_CONTEXT_SOFTWARE).script("DynatraceMonitoring" + System.currentTimeMillis());
            dataExchangeAPIClient = DataExchangeAPIClientFactory.newClient(dataExchangeApiUrl,
                    contextBuilder.build(),
                    dataExchangeApiKey.orNull());
            context.getCurrentVirtualUser().put(Constants.NL_DATA_EXCHANGE_API_CLIENT, dataExchangeAPIClient);
            requestBuilder.append("DataExchangeAPIClient created.\n");
        } else {
            requestBuilder.append("DataExchangeAPIClient retrieved from User Path Context.\n");
        }
        return dataExchangeAPIClient;
    }
}
