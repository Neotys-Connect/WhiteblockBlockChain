package com.neotys.ethereumJ.common.utils.Whiteblock.data;

import com.neotys.ethereumJ.common.utils.Whiteblock.monitoring.WhiteblockData;

import java.lang.reflect.Field;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class WhiteblockMonitoringData extends  WhiteblockDataModel{
    //{
    //  "blockTime": {
    //    "max": 0,
    //    "mean": 0,
    //    "standardDeviation": 0
    //  },
    //  "blocks": 0,
    //  "difficulty": {
    //    "max": 0,
    //    "mean": 0,
    //    "standardDeviation": 0
    //  },
    //  "gasLimit": {
    //    "max": 0,
    //    "mean": 0,
    //    "standardDeviation": 0
    //  },
    //  "gasUsed": {
    //    "max": 0,
    //    "mean": 0,
    //    "standardDeviation": 0
    //  },
    //  "totalDifficulty": {
    //    "max": 0,
    //    "mean": 0,
    //    "standardDeviation": 0
    //  },
    //  "tps": {
    //    "max": 0,
    //    "mean": 0,
    //    "standblocksardDeviation": 0
    //  },
    //  "transactionPerBlock": {
    //    "max": 0,
    //    "mean": 0,
    //    "standardDeviation": 0
    //  },
    //  "uncleCount": {
    //    "max": 0,
    //    "mean": 0,
    //    "standardDeviation": 0
    //  }
    //}
    WhiteblockStat blockTime;
    int blocks;
    WhiteblockStat difficulty;
    WhiteblockStat gasLimit;
    WhiteblockStat gasUsed;
    WhiteblockStat totalDifficulty;
    WhiteblockStat tps;
    WhiteblockStat transactionPerBlock;
    WhiteblockStat uncleCount;

    public WhiteblockMonitoringData(WhiteblockStat blockTime, int blocks, WhiteblockStat difficulty, WhiteblockStat gasLimit, WhiteblockStat gasUsed, WhiteblockStat totalDifficulty, WhiteblockStat tps, WhiteblockStat transactionPerBlock, WhiteblockStat uncleCount) {
        this.blockTime = blockTime;
        this.blocks = blocks;
        this.difficulty = difficulty;
        this.gasLimit = gasLimit;
        this.gasUsed = gasUsed;
        this.totalDifficulty = totalDifficulty;
        this.tps = tps;
        this.transactionPerBlock = transactionPerBlock;
        this.uncleCount = uncleCount;
    }

    public List<WhiteblockData> getWhiteblockDataTONL()
    {
        List<WhiteblockData> data=new ArrayList<>();
        Field[] listofField=this.getClass().getDeclaredFields();
        Arrays.stream(listofField).forEach(f->{
            if(f.getDeclaringClass().equals(WhiteblockStat.class))
            {
                try {
                    WhiteblockStat stat= (WhiteblockStat) f.get(this);
                    String metricname=f.getName();
                    data.add(new WhiteblockData(null,stat.getMean(), Instant.now().toEpochMilli(),metricname+"_average"));
                    data.add(new WhiteblockData(null,stat.getMax(), Instant.now().toEpochMilli(),metricname+"_max"));
                    data.add(new WhiteblockData(null,stat.getStandardDeviation(), Instant.now().toEpochMilli(),metricname+"_standarddeviation"));

                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
        });
        data.add(new WhiteblockData(null,this.getBlocks(),Instant.now().toEpochMilli(),"blocks"));

        return data;
    }

    public WhiteblockStat getBlockTime() {
        return blockTime;
    }

    public void setBlockTime(WhiteblockStat blockTime) {
        this.blockTime = blockTime;
    }

    public int getBlocks() {
        return blocks;
    }

    public void setBlocks(int blocks) {
        this.blocks = blocks;
    }

    public WhiteblockStat getDifficulty() {
        return difficulty;
    }

    public void setDifficulty(WhiteblockStat difficulty) {
        this.difficulty = difficulty;
    }

    public WhiteblockStat getGasLimit() {
        return gasLimit;
    }

    public void setGasLimit(WhiteblockStat gasLimit) {
        this.gasLimit = gasLimit;
    }

    public WhiteblockStat getGasUsed() {
        return gasUsed;
    }

    public void setGasUsed(WhiteblockStat gasUsed) {
        this.gasUsed = gasUsed;
    }

    public WhiteblockStat getTotalDifficulty() {
        return totalDifficulty;
    }

    public void setTotalDifficulty(WhiteblockStat totalDifficulty) {
        this.totalDifficulty = totalDifficulty;
    }

    public WhiteblockStat getTps() {
        return tps;
    }

    public void setTps(WhiteblockStat tps) {
        this.tps = tps;
    }

    public WhiteblockStat getTransactionPerBlock() {
        return transactionPerBlock;
    }

    public void setTransactionPerBlock(WhiteblockStat transactionPerBlock) {
        this.transactionPerBlock = transactionPerBlock;
    }

    public WhiteblockStat getUncleCount() {
        return uncleCount;
    }

    public void setUncleCount(WhiteblockStat uncleCount) {
        this.uncleCount = uncleCount;
    }
}
