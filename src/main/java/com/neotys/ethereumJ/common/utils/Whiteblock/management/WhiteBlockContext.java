package com.neotys.ethereumJ.common.utils.Whiteblock.management;

import com.google.common.base.Optional;
import com.neotys.extensions.action.engine.Context;

public class WhiteBlockContext {
    private String whiteblocMasterHost;
    private String sshpassword;
    private Optional<String> tracemode;
    private Context context;

    public WhiteBlockContext(String whiteblocMasterHost, String sshpassword,Optional<String> tracemode,Context context) {
        this.whiteblocMasterHost = whiteblocMasterHost;
        this.sshpassword = sshpassword;
        this.tracemode=tracemode;
        this.context=context;
    }

    public Context getContext() {
        return context;
    }

    public void setContext(Context context) {
        this.context = context;
    }

    public Optional<String> getTracemode() {
        return tracemode;
    }

    public void setTracemode(Optional<String> tracemode) {
        this.tracemode = tracemode;
    }

    public String getWhiteblocMasterHost() {
        return whiteblocMasterHost;
    }

    public void setWhiteblocMasterHost(String whiteblocMasterHost) {
        this.whiteblocMasterHost = whiteblocMasterHost;
    }

    public String getSshpassword() {
        return sshpassword;
    }

    public void setSshpassword(String sshpassword) {
        this.sshpassword = sshpassword;
    }
}
