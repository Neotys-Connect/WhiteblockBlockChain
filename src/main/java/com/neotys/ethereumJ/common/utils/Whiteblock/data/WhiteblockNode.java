package com.neotys.ethereumJ.common.utils.Whiteblock.data;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class WhiteblockNode extends  WhiteblockDataModel {
    private final String service;
    private final int instance;
    private final String ip;
    private List<Integer> ports;

    public WhiteblockNode(String service, int instance, String ip, List<Integer> ports){
        this.service = service;
        this.instance = instance;
        this.ip = ip;
        this.ports = ports;
    }

    public WhiteblockNode(JSONObject node) {
        this.service = node.getString("service");
        this.instance = node.getInt("instance");
        this.ip = node.getString("ip");
        this.ports = new ArrayList<>();
        if(!node.isNull("ports")) {
            JSONArray arr = node.getJSONArray("ports");
            for(int i = 0; i < arr.length(); i++) {
               this.ports.add(arr.getInt(i));
            }
        }
    }

    public List<Integer> getPorts() {
        return ports;
    }

    public void setPorts(List<Integer> ports) {
        this.ports = ports;
    }

    public String getIP() {
        return this.ip;
    }
}
