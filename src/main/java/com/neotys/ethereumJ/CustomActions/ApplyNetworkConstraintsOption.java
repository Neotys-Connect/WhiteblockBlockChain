package com.neotys.ethereumJ.CustomActions;

import com.neotys.action.argument.ArgumentValidator;
import com.neotys.action.argument.Option;
import com.neotys.extensions.action.ActionParameter;

import java.util.Optional;

import static com.neotys.action.argument.DefaultArgumentValidator.BOOLEAN_VALIDATOR;
import static com.neotys.action.argument.DefaultArgumentValidator.INTEGER_VALIDATOR;
import static com.neotys.action.argument.DefaultArgumentValidator.NON_EMPTY;
import static com.neotys.action.argument.Option.AppearsByDefault.True;
import static com.neotys.action.argument.Option.OptionalRequired.Optional;
import static com.neotys.action.argument.Option.OptionalRequired.Required;
import static com.neotys.extensions.action.ActionParameter.Type.TEXT;

enum  ApplyNetworkConstraintsOption  implements Option {
    WhiteBlocMasterHost("WhiteBlocMasterHost", Required, True, TEXT,
            "Host of the master Node ",
                    "Master Node of your Whiteblock Network",
                        NON_EMPTY),
    NodeNumber("NodeNumber", Optional, True, TEXT,
            "Node number ",
            "Node number to apply the network constraints (0-n)",
            INTEGER_VALIDATOR),
    TypeOfConstraint("TypeOfConstraint", Required, True, TEXT,
            "type of constraint to apply",
            "type of constraint to apply on the node : delay, loss or bandwidth . If a node number is specified the constraint wouldbe applied on the node",
            NON_EMPTY),
    ConstraintsValue("ConstraintsValue", Required, True, TEXT,
            "Value of the constraints",
            "Value of the constraints",
            NON_EMPTY),
    ConstraintsUnit("ConstraintsUnit",Optional, True, TEXT,
            "unit of the bandwidth limitation , default value :mbps",
                    "Value only required for bandwidth limitation. Value possible : kbps,mbps,gbps",
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

    ApplyNetworkConstraintsOption(final String name, final Option.OptionalRequired optionalRequired,
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
