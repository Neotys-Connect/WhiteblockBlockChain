package com.neotys.ethereumJ.common.utils.Whiteblock.data;

public class WhiteblockPseudoFile {
    private final String name;
    private final String data;

    public WhiteblockPseudoFile(String name, String data) {
        this.name = name;
        this.data = data;
    }

    public String getName() {
        return name;
    }

    public String getData() {
        return data;
    }


}
