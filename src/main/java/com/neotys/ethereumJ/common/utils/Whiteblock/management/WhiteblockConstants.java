package com.neotys.ethereumJ.common.utils.Whiteblock.management;

public class WhiteblockConstants {
    public static final String API_BASE = "/api/v1";

    public static final String FILE_API = API_BASE+"/api";
    public static final String TEST_EXECUTION_API = API_BASE+"/testexecution";
    public static final String REGISTRAR_API = API_BASE + "/registrar";
    public static final String CONTAINER_API = API_BASE + "/container";

    public static final String RUN_TEST_URI = TEST_EXECUTION_API+"/run/%s/%s";
    public static final String STOP_TEST_URI = TEST_EXECUTION_API+"/stop/test/%s";
    public static final String STATUS_TEST_URI = TEST_EXECUTION_API+"/status/%s";
    public static final String FILE_UPLOAD_URI = FILE_API + "/organizations/%s/definitions";
    public static final String PHASE_PASSED_URI = TEST_EXECUTION_API + "/status/%s/phase/%s/passed"; //test, phase
    public static final String PARSE_FILES_URI = TEST_EXECUTION_API + "/parse/files";
    public static final String NODE_ENDPOINTS_URI = TEST_EXECUTION_API + "/info/tests/%s/endpoints/services"; // test
    public static final String NODE_ENDPOINTS_FILTERED_URI = TEST_EXECUTION_API + "/info/tests/%s/endpoints/services/%s"; // test, service
    public static final String TRUE="true";

}
