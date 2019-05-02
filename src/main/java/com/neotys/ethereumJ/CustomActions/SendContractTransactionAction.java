package com.neotys.ethereumJ.CustomActions;

import com.google.common.base.Optional;
import com.neotys.action.argument.Arguments;
import com.neotys.action.argument.Option;
import com.neotys.action.result.ResultFactory;
import com.neotys.ethereumJ.Web3J.Web3UtilsWhiteblock;
import com.neotys.ethereumJ.common.utils.Whiteblock.WhiteblockUtils;
import com.neotys.extensions.action.Action;
import com.neotys.extensions.action.ActionParameter;
import com.neotys.extensions.action.engine.ActionEngine;
import com.neotys.extensions.action.engine.Context;
import com.neotys.extensions.action.engine.Logger;
import com.neotys.extensions.action.engine.SampleResult;

import javax.swing.*;
import java.util.*;

import static com.neotys.action.argument.Arguments.getArgumentLogString;
import static com.neotys.action.argument.Arguments.parseArguments;

public class SendContractTransactionAction implements Action {
    private static final String BUNDLE_NAME = "com.neotys.ethereumJ.SendContractTransaction.bundle";
    private static final String DISPLAY_NAME = ResourceBundle.getBundle(BUNDLE_NAME, Locale.getDefault()).getString("displayName");
    private static final String DISPLAY_PATH = ResourceBundle.getBundle(BUNDLE_NAME, Locale.getDefault()).getString("displayPath");

    @Override
    public String getType() {
        return "SendContractTransaction";
    }

    @Override
    public List<ActionParameter> getDefaultActionParameters() {
        final ArrayList<ActionParameter> parameters = new ArrayList<>();

        for (final SendContractTransactionOption option : SendContractTransactionOption.values()) {
            if (Option.AppearsByDefault.True.equals(option.getAppearsByDefault())) {
                parameters.add(new ActionParameter(option.getName(), option.getDefaultValue(),
                        option.getType()));
            }
        }

        return parameters;
    }

    @Override
    public Class<? extends ActionEngine> getEngineClass() {
        return SendContractTransactionActionEngine.class;
    }

    @Override
    public Icon getIcon() {
        // TODO Add an icon
        return WhiteblockUtils.getWhiteblockIcon();
    }

    @Override
    public boolean getDefaultIsHit(){
        return true;
    }

    @Override
    public String getDescription() {
        return "Send BlockChain signed Transaction in the network .\n\n" + Arguments.getArgumentDescriptions(SendContractTransactionOption.values());

    }

    @Override
    public String getDisplayName() {
        return DISPLAY_NAME;
    }

    @Override
    public String getDisplayPath() {
        return DISPLAY_PATH;
    }

    @Override
    public Optional<String> getMinimumNeoLoadVersion() {
        return Optional.of("6.7");
    }

    @Override
    public Optional<String> getMaximumNeoLoadVersion() {
        return Optional.absent();
    }

}
