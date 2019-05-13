package com.neotys.ethereumJ.common.utils.Whiteblock.monitoring;

import com.google.common.base.Optional;
import com.neotys.ascode.swagger.client.ApiClient;
import com.neotys.ascode.swagger.client.ApiException;
import com.neotys.ascode.swagger.client.api.ResultsApi;
import com.neotys.ascode.swagger.client.model.CustomMonitor;
import com.neotys.ascode.swagger.client.model.CustomMonitorValues;
import com.neotys.ascode.swagger.client.model.CustomMonitorValuesInner;
import com.neotys.ascode.swagger.client.model.MonitorPostRequest;
import com.neotys.ethereumJ.common.utils.Whiteblock.Constants;
import com.neotys.ethereumJ.common.utils.Whiteblock.data.WhiteblockMonitoringData;
import com.neotys.ethereumJ.common.utils.Whiteblock.management.WhiteBlockConstants;
import com.neotys.ethereumJ.common.utils.Whiteblock.management.WhiteBlockContext;
import com.neotys.ethereumJ.common.utils.Whiteblock.management.WhiteblockConnectionException;
import com.neotys.ethereumJ.common.utils.Whiteblock.management.WhiteblockProcessbuilder;
import com.neotys.extensions.action.engine.Context;
import com.neotys.rest.dataexchange.client.DataExchangeAPIClient;
import com.neotys.rest.dataexchange.model.EntryBuilder;
import com.neotys.rest.error.NeotysAPIException;

import java.io.IOException;
import java.net.URISyntaxException;
import java.security.GeneralSecurityException;
import java.sql.Timestamp;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static com.neotys.ethereumJ.common.utils.Whiteblock.Constants.NLWEB_VERSION;

public class WhiteblockDataToNeoLoad {

    Optional<DataExchangeAPIClient> dataExchangeAPIClient;
    WhiteBlockContext context;
    long startime;
    long endtime;

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
        traceInfo(context,"value :" +String.valueOf(whiteblockData.getValue())+" ts:"+String.valueOf(System.currentTimeMillis()));
        valuesInners.add(customMonitorValuesInner);
       //
        monitor.setValues(valuesInners);
        return monitor;
    }

    public WhiteblockDataToNeoLoad(WhiteBlockContext context, long start, long end, Optional<DataExchangeAPIClient> apiclient)
    {
        dataExchangeAPIClient=apiclient;
        this.context=context;
        startime=start;
        endtime=end;


    }

    public void getMonitoringData() throws URISyntaxException, GeneralSecurityException, NeotysAPIException, IOException, WhiteblockConnectionException, InterruptedException {
        WhiteblockMonitoringData monitoringData=WhiteblockProcessbuilder.getMonitoringData(context,startime,endtime);
       if(dataExchangeAPIClient.isPresent())
            dataExchangeAPIClient.get().addEntries(toEntries(monitoringData));


    }

    private List<com.neotys.rest.dataexchange.model.Entry> toEntries(final WhiteblockMonitoringData whiteblockMetric) {
        List<WhiteblockData> data=whiteblockMetric.getWhiteblockDataTONL();
        return data.stream()
                .map(this::toEntry)
                .collect(Collectors.toList());
    }

    private static void traceInfo(WhiteBlockContext context,String log)
    {
        if(context.getTracemode().isPresent())
        {
            if(context.getTracemode().get().toLowerCase().equals(WhiteBlockConstants.TRUE))
            {
                context.getContext().getLogger().info(log);
            }
        }
    }


    private String getBasePath(final WhiteBlockContext context) {
        final String webPlatformApiUrl = context.getContext().getWebPlatformApiUrl();
        final StringBuilder basePathBuilder = new StringBuilder(webPlatformApiUrl);
        if(!webPlatformApiUrl.endsWith("/")) {
            basePathBuilder.append("/");
        }
        basePathBuilder.append(NLWEB_VERSION + "/");
        return basePathBuilder.toString();
    }
    public void sendToNeoLoadWeb() throws InterruptedException, WhiteblockConnectionException, IOException, ApiException {
        String testid=context.getContext().getTestId();

        ApiClient neoLoadWebApiClient = new ApiClient();
        neoLoadWebApiClient.setApiKey(context.getContext().getAccountToken());
        neoLoadWebApiClient.setBasePath(getBasePath(context));
        WhiteblockMonitoringData monitoringData=WhiteblockProcessbuilder.getMonitoringData(context,startime,endtime);
       // traceInfo(context,String.valueOf(monitoringData.getBlockTime()));
     //    traceInfo(context,monitoringData.generateOutPut());
        if(testid!=null)
        {
            ResultsApi resultsApi=new ResultsApi(neoLoadWebApiClient);
            MonitorPostRequest monitorPostRequest=new MonitorPostRequest();
            monitorPostRequest.monitors(convertWhiteblockMonitoringToCustomMonitor(monitoringData));
            traceInfo(context,generateLogfromMonitorRequest(monitorPostRequest));
            resultsApi.postTestMonitors(monitorPostRequest,testid);
        }
    }


    private String generateLogfromMonitorRequest(MonitorPostRequest request)
    {

        String output= request.getMonitors().stream().map(monitor->{
            StringBuilder builder=new StringBuilder();
            builder.append("Name:");
            builder.append(monitor.getName());
            builder.append("Path");
            builder.append(monitor.getPath().stream().collect(Collectors.joining("/")));
            builder.append("Value:");
            monitor.getValues().stream().forEach(value-> builder.append("v:"+value.getValue().toString()+" ts:"+value.getTimestamp().toString()));
            return builder.toString();
        }).collect(Collectors.joining("\n"));

        return output;
    }
    public List<CustomMonitor> convertWhiteblockMonitoringToCustomMonitor(WhiteblockMonitoringData whiteblockData)
    {
        List<WhiteblockData> data=whiteblockData.getWhiteblockDataTONL();
        traceInfo(context,data.stream().map(d->{
            StringBuilder str =new StringBuilder();
            str.append("metric :"+ d.getMetricName());
            str.append("time :"+d.getTime());
          //  str.append("unit :"+d.getUnit());
            return str.toString();
        }).collect(Collectors.joining("\t")));

        return data.stream()
                .map(this::toCustomMonitor)
                .collect(Collectors.toList());
    }

}
