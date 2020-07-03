package com.neotys.ethereumJ.common.utils.Whiteblock.data;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.List;

public class WhiteblockNodeList extends WhiteblockDataModel {
    List<WhiteblockNode> whiteblockNodeList;

    public WhiteblockNodeList(JSONArray list) {
        this.whiteblockNodeList = new ArrayList<>();
        if(list !=null) {
            for (int i = 0; i < list.length(); i++) {
                this.whiteblockNodeList.add(new WhiteblockNode(list.getJSONObject(i)));
            }
        }
    }
    public List<WhiteblockNode> getWhiteblockNodeList() {
        return whiteblockNodeList;
    }

}
