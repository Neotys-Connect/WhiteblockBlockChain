package com.neotys.ethereumJ.common.utils.Whiteblock.data;

import java.util.List;

public class WhiteblockNode extends  WhiteblockDataModel {
    private String service;
    private int instance;
    private String ip;
    private List<Integer> ports;

    public WhiteblockNode(String service, int instance, String ip, List<Integer> ports){
        this.service = service;
        this.instance = instance;
        this.ip = ip;
        this.ports = ports;
    }

    public List<Integer> getPorts() {
        return ports;
    }

    public void setPorts(List<Integer> ports) {
        this.ports = ports;
    }
}
