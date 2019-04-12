package com.neotys.ethereumJ.common.utils.Whiteblock.data;

public class WhiteblockNode {
    //[
    //  {
    //    "cpu": 186.5,
    //    "name": "whiteblock-node0",
    //    "server": 1,
    //    "up": true
    //  },
    //  {
    //    "cpu": 480.50000000000006,
    //    "name": "whiteblock-node1",
    //    "server": 1,
    //    "up": true
    //  },
    //  {
    //    "cpu": 153.7,
    //    "name": "whiteblock-node2",
    //    "server": 1,
    //    "up": true
    //  }
    //]
    double cpu;
    String name;
    String server;
    boolean up;

    public WhiteblockNode(double cpu, String name, String server, boolean up) {
        this.cpu = cpu;
        this.name = name;
        this.server = server;
        this.up = up;
    }

    public double getCpu() {
        return cpu;
    }

    public void setCpu(double cpu) {
        this.cpu = cpu;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getServer() {
        return server;
    }

    public void setServer(String server) {
        this.server = server;
    }

    public boolean isUp() {
        return up;
    }

    public void setUp(boolean up) {
        this.up = up;
    }
}
