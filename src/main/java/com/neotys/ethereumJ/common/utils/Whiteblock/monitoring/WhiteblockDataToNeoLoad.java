package com.neotys.ethereumJ.common.utils.Whiteblock.monitoring;

import com.google.common.base.Optional;
import com.neotys.ascode.swagger.client.ApiClient;
import com.neotys.ascode.swagger.client.api.ResultsApi;
import com.neotys.ascode.swagger.client.model.CustomMonitor;
import com.neotys.ascode.swagger.client.model.CustomMonitorValues;
import com.neotys.ascode.swagger.client.model.CustomMonitorValuesInner;
import com.neotys.ascode.swagger.client.model.MonitorPostRequest;
import com.neotys.ethereumJ.common.utils.Whiteblock.Constants;
import com.neotys.ethereumJ.common.utils.Whiteblock.data.WhiteblockMonitoringData;
import com.neotys.ethereumJ.common.utils.Whiteblock.management.WhiteblockConstants;
import com.neotys.ethereumJ.common.utils.Whiteblock.management.WhiteblockProcessBuilder;
import com.neotys.ethereumJ.common.utils.Whiteblock.rest.WhiteblockHttpContext;
import com.neotys.rest.dataexchange.client.DataExchangeAPIClient;
import com.neotys.rest.dataexchange.model.EntryBuilder;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static com.neotys.ethereumJ.common.utils.Whiteblock.Constants.NLWEB_VERSION;

public class WhiteblockDataToNeoLoad {

    final Optional<DataExchangeAPIClient> dataExchangeAPIClient;
    final WhiteblockHttpContext context;
    final int startBlock;
    final int endBlock;

    private com.neotys.rest.dataexchange.model.Entry toEntry(final WhiteblockData whiteblockMetric) {

        List<String> path = new ArrayList<>();
        path.add(Constants.WHITEBLOCK);
        path.addAll(Arrays.asList(whiteblockMetric.getMetricName().split("_")));

        return new EntryBuilder(path, whiteblockMetric.getTime())
                .unit(whiteblockMetric.getUnit())
                .value(whiteblockMetric.getValue())
                .build();
    }



    private CustomMonitor toCustomMonitor(final WhiteblockData whiteblockData)
    {
        CustomMonitor monitor=new CustomMonitor();
        List<String> path = new ArrayList<>();
        path.add(Constants.WHITEBLOCK);
        path.addAll(Arrays.asList(whiteblockData.getMetricName().split("_")));
        traceInfo(context,path.stream().collect(Collectors.joining("/")));
        monitor.setPath(path);
        monitor.setUnit(whiteblockData.getUnit());
       // traceInfo(context,whiteblockData.getUnit());
        monitor.setName(path.get(path.size()-1));
        traceInfo(context,path.get(path.size()-1));
        CustomMonitorValues valuesInners=new CustomMonitorValues();
       // valuesInners.s
        CustomMonitorValuesInner customMonitorValuesInner=new CustomMonitorValuesInner();
        Instant instant = Instant.now();
        customMonitorValuesInner.setTimestamp(instant.getEpochSecond());
        customMonitorValuesInner.setValue((float)whiteblockData.getValue());
        traceInfo(context,"value :" + whiteblockData.getValue() +" ts:"+ System.currentTimeMillis());
        valuesInners.add(customMonitorValuesInner);
       //
        monitor.setValues(valuesInners);
        return monitor;
    }

    public WhiteblockDataToNeoLoad(WhiteblockHttpContext context, int start, int end, Optional<DataExchangeAPIClient> apiclient)
    {
        dataExchangeAPIClient=apiclient;
        this.context=context;
        startBlock=start;
        endBlock=end;


    }

    public void getMonitoringData(String testID) throws Exception {
        WhiteblockMonitoringData monitoringData= WhiteblockProcessBuilder.getEthMonitoringData(context,testID, startBlock,endBlock);
       if(dataExchangeAPIClient.isPresent())
            dataExchangeAPIClient.get().addEntries(toEntries(monitoringData));


    }

    private List<com.neotys.rest.dataexchange.model.Entry> toEntries(final WhiteblockMonitoringData whiteblockMetric) {
        List<WhiteblockData> data=whiteblockMetric.getWhiteblockDataTONL();
        return data.stream()
                .map(this::toEntry)
                .collect(Collectors.toList());
    }

    private static void traceInfo(WhiteblockHttpContext context,String log)
    {
        if(context.getTracemode().isPresent())
        {
            if(context.getTracemode().get().toLowerCase().equals(WhiteblockConstants.TRUE))
            {
                context.getContext().getLogger().info(log);
            }
        }
    }


    private String getBasePath(final WhiteblockHttpContext context) {
        final String webPlatformApiUrl = context.getContext().getWebPlatformApiUrl();
        final StringBuilder basePathBuilder = new StringBuilder(webPlatformApiUrl);
        if(!webPlatformApiUrl.endsWith("/")) {
            basePathBuilder.append("/");
        }
        basePathBuilder.append(NLWEB_VERSION + "/");
        return basePathBuilder.toString();
    }
    public void sendToNeoLoadWeb() throws Exception {
        String testID = context.getContext().getTestId();

        ApiClient neoLoadWebApiClient = new ApiClient();
        neoLoadWebApiClient.setApiKey(context.getContext().getAccountToken());
        neoLoadWebApiClient.setBasePath(getBasePath(context));
        WhiteblockMonitoringData monitoringData= WhiteblockProcessBuilder.getEthMonitoringData(context, testID,
                startBlock,endBlock);
        traceInfo(context,String.valueOf(monitoringData.getBlockTime()));
        traceInfo(context, monitoringData.generateOutPut());
        if(testID != null)
        {
            ResultsApi resultsApi=new ResultsApi(neoLoadWebApiClient);
            MonitorPostRequest monitorPostRequest=new MonitorPostRequest();
            monitorPostRequest.monitors(convertWhiteblockMonitoringToCustomMonitor(monitoringData));
            traceInfo(context,generateLogFromMonitorRequest(monitorPostRequest));
            resultsApi.postTestMonitors(monitorPostRequest,testID);
        }
    }


    private String generateLogFromMonitorRequest(MonitorPostRequest request)
    {

        String output= request.getMonitors().stream().map(monitor->{
            StringBuilder builder=new StringBuilder();
            builder.append("Name:");
            builder.append(monitor.getName());
            builder.append("Path");
            builder.append(monitor.getPath().stream().collect(Collectors.joining("/")));
            builder.append("Value:");
            monitor.getValues().stream().forEach(value-> builder.append("v:").append(value.getValue().toString()).append(" ts:").append(value.getTimestamp().toString()));
            return builder.toString();
        }).collect(Collectors.joining("\n"));

        return output;
    }
    public List<CustomMonitor> convertWhiteblockMonitoringToCustomMonitor(WhiteblockMonitoringData whiteblockData)
    {
        List<WhiteblockData> data=whiteblockData.getWhiteblockDataTONL();
        traceInfo(context,data.stream().map(d->{
            //  str.append("unit :"+d.getUnit());
            String str = "metric :" + d.getMetricName() +
                    "time :" + d.getTime();
            return str;
        }).collect(Collectors.joining("\t")));

        return data.stream()
                .map(this::toCustomMonitor)
                .collect(Collectors.toList());
    }

}
