package com.neotys.ethereumJ.common.utils.Whiteblock.data;


import java.util.List;
//[
//  {
//    "account": "0x059be7a77fa9bd2d6f38c3c2bc19eb7a163eeb4e",
//    "balance": "100000000000000000000",
//    "privateKey": "3623fc5b714d1b81dfb118c39ee999b93a9a1f120b35a4ac86eeab67dfb09930",
//    "publicKey": "0439f0832ef1b1e7dd3345040f3ef83bfe00198c34df9815d227b2f6506e57c3984ad0352fcee5843df4aee0ca69fc4f392c41fc2b3198b468713a87d1c6ed23f7",
//    "txCount": "0"
//  },
//  {
//    "account": "0x187dc833fcbce9b8a4b3bd28dc254a15087dd171",
//    "balance": "100000000000000000000",
//    "privateKey": "7fa0f0c8e1a73fd29b4c65a2c6c63933f7a7956bc03bcdab6335330ab7a84a72",
//    "publicKey": "04d4849d35814cc81b78609e6ef9208d9d4fc258ecc243193c915d2f3589241e7a2127b2ad53e1cab6df6785e357ea15626461c08d890fafb7d00bd3365155b5e7",
//    "txCount": "0"
//  },
//  {
//    "account": "0x45a71b7139371de0f064d6d017c4f2367e7cbf23",
//    "balance": "100000000000000000000",
//    "privateKey": "70eebadfe361be7f2601507cbca1393fb573d543d664f25cfb7575741313d100",
//    "publicKey": "040346300d77322dcf7c981c9bdfdad46f117d889800dce77ddfda26fcb72b7e080daf60515ca5f0294c1ce754f22f65998164f5ed01ea764434e1c2956b9aecc1",
//    "txCount": "0"
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

    public void setAccountList(List<WhiteblockAccount> accountList) {
        this.accountList = accountList;
    }
}
