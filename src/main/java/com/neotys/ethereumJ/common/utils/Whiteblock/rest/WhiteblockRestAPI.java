package com.neotys.ethereumJ.common.utils.Whiteblock.rest;
import com.google.common.base.Optional;

import com.google.gson.JsonObject;
import com.neotys.ethereumJ.common.utils.Whiteblock.http.HTTPGenerator;
import com.neotys.ethereumJ.common.utils.Whiteblock.http.HttpResponseUtils;
import com.neotys.ethereumJ.common.utils.Whiteblock.http.WhiteBlockHttpException;
import com.neotys.extensions.action.engine.Context;
import com.neotys.extensions.action.engine.Proxy;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import static com.neotys.ethereumJ.common.utils.Whiteblock.http.HTTPGenerator.HTTP_POST_METHOD;
import static com.neotys.ethereumJ.common.utils.Whiteblock.http.HttpResponseUtils.isSuccessHttpCode;
import static java.util.UUID.randomUUID;

public class WhiteBlockRestAPI {

	public static String MultipartRequest(String method, String path, filePaths String[]) throws Exception {
		throw new Exception("nyi");
	}

    public static String Request(String method, String path, String payload, WhiteblockHttpContext context) throws Exception {
        final Map<String, String> parameters = new HashMap<>();

        final Map<String, String> headers = new HashMap<>();
        String url = context.getWhiteblockMasterHost() + path;
        headers.put("Authorization"," Bearer "+context.getRpctoken());

        final Optional<Proxy> proxy = getProxy(context.getContext(), context.getProxy(), url);

        final HTTPGenerator http = HTTPGenerator.newJsonHttpGenerator(HTTP_POST_METHOD, url,  headers, parameters, proxy, payload);

        try
        {
            if(context.getTracemode().isPresent()&&context.getTracemode().get().equalsIgnoreCase("TRUE"))
            {
                context.getContext().getLogger().info("Whiteblock rpc service, :\n" + http.getRequest() + "\n" + payload);

            }
            HttpResponse response=http.execute();
            if(context.getTracemode().isPresent()&&context.getTracemode().get().equalsIgnoreCase("TRUE"))
            {
                context.getContext().getLogger().info("Whiteblock rpc response, :\n" + response.toString() );

            }
            final int statusCode = response.getStatusLine().getStatusCode();
            if(isSuccessHttpCode(statusCode))
            {
                if(context.getTracemode().isPresent()&&context.getTracemode().get().equalsIgnoreCase("TRUE"))
                {
                    context.getContext().getLogger().info("Whiteblock API response, :\n" + response );
                }
                return response;
            }
            else if(statusCode != HttpStatus.SC_BAD_REQUEST && statusCode != HttpStatus.SC_NOT_FOUND) {
                throw new WhiteBlockHttpException(response.getStatusLine().getReasonPhrase() + " - " + url + " - " + payload] + " - " + response);
            }
            return null;
        }
        catch (Exception e)
        {
            throw new WhiteBlockHttpException(e.getMessage());
        }
    }


    public static Optional<Proxy> getProxy(final Context context, final Optional<String> proxyName, final String url) throws MalformedURLException {
        if (proxyName.isPresent()) {
            return Optional.fromNullable(context.getProxyByName(proxyName.get(), new URL(url)));
        }
        return Optional.absent();
    }

    public static boolean isSuccessHttpCode(final int httpCode) {
        return httpCode >= HttpStatus.SC_OK
                && httpCode <= HttpStatus.SC_MULTI_STATUS;
    }
}
