// Javascript skeleton.
// Edit and adapt to your needs.
// The documentation of the NeoLoad Javascript API
// is available in the appendix of the documentation.

// Get variable value from VariableManager

var accountList = context.variableManager.peekSharedValue("ALL_accounts");
if (accountList==null) {
        context.fail("Variable 'nodedetail' not found");
}
var testid = context.variableManager.peekSharedValue("testid");
if (testid==null) {
        context.fail("Variable 'testid' not found");
}

logger.debug("testid="+testid);
// Inject the computed value in a runtime variable

context.variableManager.setValue("accountsJSON",accountList);
context.variableManager.setValue("testid",testid);