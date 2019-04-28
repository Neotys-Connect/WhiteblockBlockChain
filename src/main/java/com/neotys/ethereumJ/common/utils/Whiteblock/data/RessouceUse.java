package com.neotys.ethereumJ.common.utils.Whiteblock.data;

public class RessouceUse {
    //    "resourceUse": {
    //      "cpu": 9.5,
    //      "residentSetSize": 1078752,
    //      "virtualMemorySize": 4752256
    //    },

    double cpu;
    int residentSetSize;
    int virtualMemorySize;

    public RessouceUse(double cpu, int residentSetSize, int virtualMemorySize) {
        this.cpu = cpu;
        this.residentSetSize = residentSetSize;
        this.virtualMemorySize = virtualMemorySize;
    }

    public double getCpu() {
        return cpu;
    }

    public void setCpu(double cpu) {
        this.cpu = cpu;
    }

    public int getResidentSetSize() {
        return residentSetSize;
    }

    public void setResidentSetSize(int residentSetSize) {
        this.residentSetSize = residentSetSize;
    }

    public int getVirtualMemorySize() {
        return virtualMemorySize;
    }

    public void setVirtualMemorySize(int virtualMemorySize) {
        this.virtualMemorySize = virtualMemorySize;
    }
}
