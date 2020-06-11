package com.neotys.ethereumJ.CustomActions;

import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import com.google.common.base.Optional;
import com.neotys.action.result.ResultFactory;
import com.neotys.ethereumJ.common.utils.Whiteblock.data.WhiteblockAccount;
import com.neotys.ethereumJ.common.utils.Whiteblock.data.WhiteblockBuildMeta;
import com.neotys.ethereumJ.common.utils.Whiteblock.management.WhiteblockProcessBuilder;
import com.neotys.ethereumJ.common.utils.Whiteblock.rest.WhiteblockHttpContext;
import com.neotys.ethereumJ.common.utils.Whiteblock.tools.Ethereum;
import com.neotys.extensions.action.ActionParameter;
import com.neotys.extensions.action.engine.ActionEngine;
import com.neotys.extensions.action.engine.Context;
import com.neotys.extensions.action.engine.Logger;
import com.neotys.extensions.action.engine.SampleResult;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

import static com.neotys.action.argument.Arguments.getArgumentLogString;
import static com.neotys.action.argument.Arguments.parseArguments;

public class BuildWhiteblockNetworkActionEngine implements ActionEngine {
    private static final String STATUS_CODE_INVALID_PARAMETER = "NL-WB_BUILD_ACTION-01";
    private static final String STATUS_CODE_TECHNICAL_ERROR = "NL-WB_BUILD_ACTION-02";
    private static final String STATUS_CODE_BAD_CONTEXT = "NL-WB_BUILD_ACTION-03";
    private static final String VALIDATION="completed";

    private boolean isTestReady(WhiteblockHttpContext wbContext, String testID, String phase) throws Exception {
        return WhiteblockProcessBuilder.phasePassed(wbContext, testID, phase);
    }
    @Override
    public SampleResult execute(Context context, List<ActionParameter> list) {
        final SampleResult sampleResult = new SampleResult();
        final StringBuilder requestBuilder = new StringBuilder();
        final StringBuilder responseBuilder = new StringBuilder();


        final Map<String, Optional<String>> parsedArgs;
        try {
            parsedArgs = parseArguments(list, BuildWhiteblockNetworkOption.values());
        } catch (final IllegalArgumentException iae) {
            return ResultFactory.newErrorResult(context, STATUS_CODE_INVALID_PARAMETER, "Could not parse arguments: ", iae);
        }

        final Logger logger = context.getLogger();
        if (logger.isDebugEnabled()) {
            logger.debug("Executing " + this.getClass().getName() + " with parameters: "
                    + getArgumentLogString(parsedArgs, GetMonitoringDataOption.values()));
        }

        final String org = parsedArgs.get(BuildWhiteblockNetworkOption.
            Organization.getName()).get();
        final String defFilePath = parsedArgs.get(BuildWhiteblockNetworkOption.
            TestDefinition.getName()).get();
        final String accessToken = parsedArgs.get(BuildWhiteblockNetworkOption.
            AccessToken.getName()).get();
        final String startPhase = parsedArgs.get(BuildWhiteblockNetworkOption.
            StartPhase.getName()).get();
        final Optional<String> tracemode = parsedArgs.get((BuildWhiteblockNetworkOption.TraceMode.getName()));
        final Optional<String> proxyName=parsedArgs.get(BuildWhiteblockNetworkOption.ProxyName.getName());

        String rawDefinition = "";
        try {
            File fd = new File(defFilePath);
            Scanner fdScanner = new Scanner(fd);
            while (fdScanner.hasNextLine()) {
                rawDefinition += fdScanner.nextLine() + "\n";
            }
            fdScanner.close();
        }catch(FileNotFoundException e) {
            return ResultFactory.newErrorResult(context, STATUS_CODE_BAD_CONTEXT, "failed to find the provide test definition file: ", e);
        }
        try
        {
            WhiteblockHttpContext wbContext = new WhiteblockHttpContext(accessToken, tracemode,context,proxyName);
            List<WhiteblockAccount> accounts = Ethereum.generateAccounts(200);
            // TODO: Add logic for the custom ethereum case, which generates the accounts, and stores it somewhere it
            List<String> testIDs  = WhiteblockProcessBuilder.buildEthereum(wbContext, org,
                    new WhiteblockBuildMeta(rawDefinition), accounts);
            if (testIDs.size() >0) {

                responseBuilder.append("<TestIds>");
                testIDs.forEach(s ->
                {
                    responseBuilder.append("<testid>"+s+"</testid>");
                });
                responseBuilder.append("</TestIds>");

                // TODO: Establish whether we handle the case of multiple tests or just give an error
            }

            ///----generate output of the custom action----
            if(accounts.size()>0)
            {
                XmlMapper xmlMapper = new XmlMapper();
                xmlMapper.enable(SerializationFeature.INDENT_OUTPUT);
                String xml = xmlMapper.writeValueAsString(accounts);
                responseBuilder.append(xml);
            }

            String testID = testIDs.get(0);
            while(!isTestReady(wbContext, testID, startPhase)) {
                 Thread.sleep(500);
            }
        }
        catch (Exception e)
        {
            return ResultFactory.newErrorResult(context, STATUS_CODE_BAD_CONTEXT, "Error encountered :", e);

        }

        sampleResult.setRequestContent(requestBuilder.toString());
        sampleResult.setResponseContent(responseBuilder.toString());
        return sampleResult;
    }



    @Override
    public void stopExecute() {

    }
}
