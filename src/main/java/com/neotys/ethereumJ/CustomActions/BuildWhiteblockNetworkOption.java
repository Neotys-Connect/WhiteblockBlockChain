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
    Organization("Organization", Required, True, TEXT,
            "The name or id of the organization to deploy one",
            "The name or id of the organization to deploy one",
            NON_EMPTY),
    TestDefinition("TestDefinition", Required, True, TEXT,
            "${NL-CustomResources}/youFile.yaml",
            "The path to the Whiteblock Genesis test definition yaml file",
            NON_EMPTY),
    AccessToken("AccessToken", Required, True, TEXT,
            "A Whiteblock Genesis access token",
            "You can get this by running the command genesis auth print-access-token",
            NON_EMPTY),
    StartPhase("StartPhase", Required, True, TEXT,
            "The phase at which to start the load test",
            "Choose a phase in your test during which you want to start the load test",
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

