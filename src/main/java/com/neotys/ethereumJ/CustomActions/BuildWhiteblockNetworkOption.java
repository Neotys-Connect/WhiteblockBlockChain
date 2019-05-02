package com.neotys.ethereumJ.CustomActions;

import com.neotys.action.argument.ArgumentValidator;
import com.neotys.action.argument.Option;
import com.neotys.extensions.action.ActionParameter;

import static com.neotys.action.argument.DefaultArgumentValidator.*;
import static com.neotys.action.argument.Option.AppearsByDefault.False;
import static com.neotys.action.argument.Option.AppearsByDefault.True;
import static com.neotys.action.argument.Option.OptionalRequired.Optional;
import static com.neotys.action.argument.Option.OptionalRequired.Required;
import static com.neotys.extensions.action.ActionParameter.Type.TEXT;

enum BuildWhiteblockNetworkOption implements Option {
    WhiteBlocMasterHost("WhiteBlocMasterHost", Required, True, TEXT,
            "Host of the master Node ",
            "Master Node of your Whiteblock Network",
            NON_EMPTY),
    TypeofBlochacin("TypeofBlochacin", Required, True, TEXT,
            "Type of BlockChain Network",
                    " value possible :Ethereum, Syscoin, Rchain, EOS, Monero, NEM, Fabric, Quorum, or Custom",
                NON_EMPTY),

    NumberOfNodes("NumberOfNodes", Required, True, TEXT,
            "Number of Nodes",
                    "Number of nodes to generate in the network",
                    INTEGER_VALIDATOR),
    TraceMode("TraceMode", Optional, True, TEXT,
            "enable logging ",
            "enable loggin details  : true: enable ; false : Disable",
            BOOLEAN_VALIDATOR);

    private final String name;
    private final Option.OptionalRequired optionalRequired;
    private final Option.AppearsByDefault appearsByDefault;
    private final ActionParameter.Type type;
    private final String defaultValue;
    private final String description;
    private final ArgumentValidator argumentValidator;

    BuildWhiteblockNetworkOption(final String name, final Option.OptionalRequired optionalRequired,
                         final Option.AppearsByDefault appearsByDefault,
                         final ActionParameter.Type type, final String defaultValue, final String description,
                         final ArgumentValidator argumentValidator) {
        this.name = name;
        this.optionalRequired = optionalRequired;
        this.appearsByDefault = appearsByDefault;
        this.type = type;
        this.defaultValue = defaultValue;
        this.description = description;
        this.argumentValidator = argumentValidator;
    }
    @Override
    public String getName() {
        return name;
    }

    @Override
    public Option.OptionalRequired getOptionalRequired() {
        return optionalRequired;
    }

    @Override
    public Option.AppearsByDefault getAppearsByDefault() {
        return appearsByDefault;
    }

    @Override
    public ActionParameter.Type getType() {
        return type;
    }

    @Override
    public String getDefaultValue() {
        return defaultValue;
    }

    @Override
    public String getDescription() {
        return description;
    }

    @Override
    public ArgumentValidator getArgumentValidator() {
        return argumentValidator;
    }

}

