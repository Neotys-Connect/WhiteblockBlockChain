// Javascript skeleton.
// Edit and adapt to your needs.
// The documentation of the NeoLoad Javascript API
// is available in the appendix of the documentation.

// Get variable value from VariableManager
var acountdetail = context.variableManager.getValue("nodedetail");
if (acountdetail==null) {
        context.fail("Variable 'nodedetail' not found");
}

// Do some computation using the methods
// you defined in the JS Library
var data=acountdetail.split("_");

var ip=data[0];
var port=data[1];


// Inject the computed value in a runtime variable
context.variableManager.setValue("nodeip",ip);
context.variableManager.setValue("nodeport",port);
