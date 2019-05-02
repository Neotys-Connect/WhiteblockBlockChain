package com.neotys.ethereumJ.common.utils.Whiteblock.monitoring;

import com.neotys.ethereumJ.common.utils.Whiteblock.Constants;
import com.neotys.ethereumJ.common.utils.Whiteblock.data.WhiteblockMonitoringData;
import com.neotys.ethereumJ.common.utils.Whiteblock.management.WhiteBlockContext;
import com.neotys.ethereumJ.common.utils.Whiteblock.management.WhiteblockConnectionException;
import com.neotys.ethereumJ.common.utils.Whiteblock.management.WhiteblockProcessbuilder;
import com.neotys.rest.dataexchange.client.DataExchangeAPIClient;
import com.neotys.rest.dataexchange.model.EntryBuilder;
import com.neotys.rest.error.NeotysAPIException;

import java.io.IOException;
import java.net.URISyntaxException;
import java.security.GeneralSecurityException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class WhiteblockDataToNeoLoad {

    DataExchangeAPIClient dataExchangeAPIClient;
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


    public WhiteblockDataToNeoLoad(WhiteBlockContext context, long start, long end, DataExchangeAPIClient apiclient)
    {
        dataExchangeAPIClient=apiclient;
        this.context=context;
        startime=start;
        endtime=end;


    }

    public void getMonitoringData() throws URISyntaxException, GeneralSecurityException, NeotysAPIException, IOException, WhiteblockConnectionException, InterruptedException {
        WhiteblockMonitoringData monitoringData=WhiteblockProcessbuilder.getMonitoringData(context,startime,endtime);
        dataExchangeAPIClient.addEntries(toEntries(monitoringData));
    }

    private List<com.neotys.rest.dataexchange.model.Entry> toEntries(final WhiteblockMonitoringData whiteblockMetric) {
        List<WhiteblockData> data=whiteblockMetric.getWhiteblockDataTONL();
        return data.stream()
                .map(this::toEntry)
                .collect(Collectors.toList());
    }
}
