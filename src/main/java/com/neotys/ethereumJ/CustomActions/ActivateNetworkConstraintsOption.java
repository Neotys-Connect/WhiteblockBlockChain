package com.neotys.ethereumJ.CustomActions;

import com.neotys.action.argument.ArgumentValidator;
import com.neotys.action.argument.Option;
import com.neotys.extensions.action.ActionParameter;


import static com.neotys.action.argument.DefaultArgumentValidator.BOOLEAN_VALIDATOR;
import static com.neotys.action.argument.DefaultArgumentValidator.INTEGER_VALIDATOR;
import static com.neotys.action.argument.DefaultArgumentValidator.NON_EMPTY;
import static com.neotys.action.argument.Option.AppearsByDefault.False;
import static com.neotys.action.argument.Option.AppearsByDefault.True;
import static com.neotys.action.argument.Option.OptionalRequired.Optional;
import static com.neotys.action.argument.Option.OptionalRequired.Required;
import static com.neotys.extensions.action.ActionParameter.Type.TEXT;

enum  ActivateNetworkConstraintsOption implements Option {
    WhiteBlocMasterHost("WhiteBlocMasterHost", Required, True, TEXT,
            "Host of the master Node ",
                    "Master Node of your Whiteblock Network",
                        NON_EMPTY),
    WhiteBlocRpcPort("WhiteBlocRpcPort", Required, True, TEXT,
            "RPC port of the whiteblock node",
            "RPC port of the whiteblock node",
            INTEGER_VALIDATOR),
    WhiteBlocRpctoken("WhiteBlocRpctoken", Required, True, TEXT,
            "RPC token of the whiteblock node",
            "RPC token of the whiteblock node",
            NON_EMPTY),
    ProxyName("ProxyName", Optional, False, TEXT,
            "name of the neoload proxy",
            "name of the neoload proxy",
            NON_EMPTY),
    NetworkMode("NetworkMode", Required, True, TEXT,
            "Mode of the network emulation",
                    "Mode of the network emulation : ON: enable ; OFF : Disable",
               NON_EMPTY),
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

    ActivateNetworkConstraintsOption(final String name, final Option.OptionalRequired optionalRequired,
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
