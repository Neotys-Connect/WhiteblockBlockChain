package com.neotys.ethereumJ.common.utils.Whiteblock.data;

import java.beans.ExceptionListener;
import java.beans.XMLEncoder;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.List;
//[
//  {
//    "account": "0x9809584a77fd6a3d433e6dd672896654f9449b76",
//    "balance": "16918874000000000000",
//    "txCount": "458336"
//  },
//  {
//    "account": "0x2c061c062caf8afcc46e4a70c32c4668f453e15d",
//    "balance": "20418373017000000000000",
//    "txCount": "446053"
//  },
//  {
//    "account": "0x178f5b6cf6554eb7d4c240543d0c661f7d5af086",
//    "balance": "11227989359000000000000",
//    "txCount": "450705"
//  }
//]
public class WhiteblockAccountList extends WhiteblockDataModel {
    List<WhiteblockAccount> accountList;

    public WhiteblockAccountList(List<WhiteblockAccount> accountList) {
        this.accountList = accountList;
    }


    public List<WhiteblockAccount> getAccountList() {
        return accountList;
    }


}
