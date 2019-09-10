package com.neotys.ethereumJ.common.utils.Whiteblock.data;

import java.util.HashMap;

public class WhiteblockNode {
    // [
    //  {
    //    "ip": "10.1.0.2",
    //    "name": "whiteblock-node0",
    //    "resourceUse": {
    //      "cpu": 6.8,
    //      "residentSetSize": 1095612,
    //      "virtualMemorySize": 4758028
    //    },
    //    "server": 1,
    //    "up": true
    //  },
    //  {
    //    "ip": "10.1.0.6",
    //    "name": "whiteblock-node1",
    //    "resourceUse": {
    //      "cpu": 6.1000000000000005,
    //      "residentSetSize": 1094216,
    //      "virtualMemorySize": 4832592
    //    },
    //    "server": 1,
    //    "up": true
    //  },
    //  {
    //    "ip": "10.1.0.10",
    //    "name": "whiteblock-node2",
    //    "resourceUse": {
    //      "cpu": 9.5,
    //      "residentSetSize": 1078752,
    //      "virtualMemorySize": 4752256
    //    },
    //    "server": 1,
    //    "up": true
    //  }
    //]

    RessouceUse resourceUse;
    String name;
    String image;
    String protocol;
    String server;
    boolean up;
    String ip;
    String id;
    HashMap<String,String> portMappings;

    public WhiteblockNode(RessouceUse resourceUse, String name, String image, String protocol, String server, boolean up, String ip, String id, HashMap<String, String> portMappings) {
        this.resourceUse = resourceUse;
        this.name = name;
        this.image = image;
        this.protocol = protocol;
        this.server = server;
        this.up = up;
        this.ip = ip;
        this.id = id;
        this.portMappings = portMappings;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getProtocol() {
        return protocol;
    }

    public void setProtocol(String protocol) {
        this.protocol = protocol;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public HashMap<String, String> getPortMappings() {
        return portMappings;
    }

    public void setPortMappings(HashMap<String, String> portMappings) {
        this.portMappings = portMappings;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public RessouceUse getResourceUse() {
        return resourceUse;
    }

    public void setResourceUse(RessouceUse resourceUse) {
        this.resourceUse = resourceUse;
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
