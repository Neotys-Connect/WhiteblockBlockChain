package com.neotys.ethereumJ.common.utils.Whiteblock.monitoring;

public class WhiteblockData {
    private final String unit;
    private final double value;
    private final long time;
    private final String metricName;


    public String getUnit() {
        return unit;
    }

    public double getValue() {
        return value;
    }

    public long getTime() {
        return time;
    }

    public String getMetricName() {
        return metricName;
    }




    public WhiteblockData(String unit, double value, long time, String metricName) {
        super();
        this.unit = unit;
        this.value = value;
        this.time = time;
        this.metricName = metricName;

    }
}
