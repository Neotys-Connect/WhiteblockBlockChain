package com.neotys.ethereumJ.common.utils.Whiteblock.data;

import java.util.List;

public class WhiteblockNodeList extends WhiteblockDataModel {
    List<WhiteblockNode> whiteblockNodeList;

    public WhiteblockNodeList(List<WhiteblockNode> whiteblockNodeList) {
        this.whiteblockNodeList = whiteblockNodeList;
    }

    public List<WhiteblockNode> getWhiteblockNodeList() {
        return whiteblockNodeList;
    }

    public void setWhiteblockNodeList(List<WhiteblockNode> whiteblockNodeList) {
        this.whiteblockNodeList = whiteblockNodeList;
    }
}
