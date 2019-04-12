package com.neotys.ethereumJ.common.utils.Whiteblock;

import javax.swing.*;
import java.net.URL;

public class WhiteblockUtils {
    private static final ImageIcon WHITEBLOCK_ICON;
    static {

        final URL iconURL = WhiteblockUtils.class.getResource("wb.png");
        if (iconURL != null) {
            WHITEBLOCK_ICON = new ImageIcon(iconURL);
        } else {
            WHITEBLOCK_ICON = null;
        }
    }

    public WhiteblockUtils() {
    }
    public static ImageIcon getWhiteblockIcon() {
        return WHITEBLOCK_ICON;
    }

}
