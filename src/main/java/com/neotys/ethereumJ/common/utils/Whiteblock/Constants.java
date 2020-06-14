package com.neotys.ethereumJ.common.utils.Whiteblock;

public class Constants {

    /*** Dynatrace ***/
    public static final String WHITEBLOCK = "WHITEBLOCK";

    /*** NeoLoad context (Data Exchange API) ***/
    public static final String NEOLOAD_CONTEXT_HARDWARE = WHITEBLOCK;
    public static final String NEOLOAD_CONTEXT_LOCATION = WHITEBLOCK;
    public static final String NEOLOAD_CONTEXT_SOFTWARE = WHITEBLOCK;

    /*** NeoLoad Current Virtual user context (Keep object in cache cross iterations) ***/
    public static final String NL_DATA_EXCHANGE_API_CLIENT = "NLDataExchangeAPIClient";
	public static final String WHITEBLOCK_LAST_EXECUTION_TIME = "WhiteblockLastExecutionTime";
	public static final String WHITEBLOCK_LAST_BLOCK_NUMBER = "WhiteblockLastBlockNumber";
	public static final int WHITEBLOCK_MAX_STATS_BLOCKS=10;
    public static final long WHITEBLOCK_MONITORING_PACE=5000;
	public static final int WHITEBLOCK_MAX_DELAY=2;
	public static final String WHITEBLOCK_DEFAULT_PASSWORD="password";
	public static final String WHITEBLOCK_RPC_PORT="8545";
    public static final String NLWEB_VERSION="v1";
    public static final String ERROR="Error:";
}
