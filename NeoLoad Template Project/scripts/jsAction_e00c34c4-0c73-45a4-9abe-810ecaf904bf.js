// Javascript skeleton.
// Edit and adapt to your needs.
// The documentation of the NeoLoad Javascript API
// is available in the appendix of the documentation.

// Get variable value from VariableManager
var acountdetail = context.variableManager.getValue("acountdetail");
if (acountdetail==null) {
        context.fail("Variable 'acountdetail' not found");
}

// Do some computation using the methods
// you defined in the JS Library
var data=acountdetail.split("<");

var accountadress=data[0];
var publickey=data[4];
var privatekey=data[3];


// Inject the computed value in a runtime variable
context.variableManager.setValue("accountadress",accountadress);
context.variableManager.setValue("publickey",publickey);
context.variableManager.setValue("privatekey",privatekey);