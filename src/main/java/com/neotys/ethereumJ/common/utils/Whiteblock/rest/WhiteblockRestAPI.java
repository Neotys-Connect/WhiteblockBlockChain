package com.neotys.ethereumJ.common.utils.Whiteblock.rest;

import com.google.common.base.Optional;
import com.neotys.ethereumJ.common.utils.Whiteblock.data.WhiteblockPseudoFile;
import com.neotys.ethereumJ.common.utils.Whiteblock.http.HTTPGenerator;
import com.neotys.ethereumJ.common.utils.Whiteblock.http.HttpResponseUtils;
import com.neotys.ethereumJ.common.utils.Whiteblock.http.WhiteBlockHttpException;
import com.neotys.extensions.action.engine.Context;
import com.neotys.extensions.action.engine.Proxy;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.json.JSONArray;
import org.json.JSONObject;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class WhiteblockRestAPI {

	public static String multipartRequest(WhiteblockHttpContext context,
                                          String path, List<String> filePaths,
                                          List<WhiteblockPseudoFile> fileOverides) throws Exception {
        final Map<String, String> parameters = new HashMap<>();

        final Map<String, String> headers = new HashMap<>();
        String url = context.HOST + path;
        headers.put("Authorization"," Bearer "+context.getBearerToken());
        final Optional<Proxy> proxy = getProxy(context.getContext(), context.getProxy(), url);
        final HTTPGenerator http = HTTPGenerator.newMultiPartRequest(url,  headers, parameters, proxy, filePaths, fileOverides);

        try
        {
            if(context.isTraceModeActive())
            {
                context.getContext().getLogger().info("Whiteblock multipart request, :\n" + http.getRequest() + "\n" +
                        filePaths.toString());
            }
            HttpResponse response=http.execute();
            if(context.isTraceModeActive())
            {
                context.getContext().getLogger().info("Whiteblock multipart response, :\n" + response.toString() );
            }
            final int statusCode = response.getStatusLine().getStatusCode();
            if(isSuccessHttpCode(statusCode))
            {
                if(context.isTraceModeActive())
                {
                    context.getContext().getLogger().info("Whiteblock multipart success response, :\n" + response );
                }
                return response.toString();
            }
            else if(statusCode != HttpStatus.SC_BAD_REQUEST && statusCode != HttpStatus.SC_NOT_FOUND) {
                throw new WhiteBlockHttpException(response.getStatusLine().getReasonPhrase() + " - " + url + " - " +
                        filePaths.toString() + " - " + response);
            }
            return null;
        }
        catch (Exception e)
        {
            throw new WhiteBlockHttpException(e.getMessage());
        }
	}

    public static String request(String method, String path, String payload, WhiteblockHttpContext context) throws Exception {
        final Map<String, String> parameters = new HashMap<>();

        final Map<String, String> headers = new HashMap<>();
        String url = context.HOST + path;
        headers.put("Authorization"," Bearer "+context.getBearerToken());

        final Optional<Proxy> proxy = getProxy(context.getContext(), context.getProxy(), url);

        final HTTPGenerator http = new HTTPGenerator(method, url,  headers, parameters, proxy);

        try
        {
            if(context.isTraceModeActive())
            {
                context.getContext().getLogger().info("Whiteblock rpc service, :\n" + http.getRequest() + "\n" + payload);

            }
            HttpResponse response=http.execute();
            if(context.isTraceModeActive())
            {
                context.getContext().getLogger().info("Whiteblock rpc response, :\n" + response.toString() );

            }
            final int statusCode = response.getStatusLine().getStatusCode();
            if(isSuccessHttpCode(statusCode))
            {
                if(context.isTraceModeActive())
                {
                    context.getContext().getLogger().info("Whiteblock API response, :\n" + response );
                }
                return response.toString();
            }
            else if(statusCode != HttpStatus.SC_BAD_REQUEST && statusCode != HttpStatus.SC_NOT_FOUND) {
                throw new WhiteBlockHttpException(response.getStatusLine().getReasonPhrase() + " - " + url + " - " +
                        payload + " - " + response);
            }
            return null;
        }
        catch (Exception e)
        {
            throw new WhiteBlockHttpException(e.getMessage());
        }
    }

    public static JSONObject jRequest(String method, String url, String payload, WhiteblockHttpContext context)
            throws Exception {
        final Map<String, String> parameters = new HashMap<>();

        final Map<String, String> headers = new HashMap<>();

        headers.put("Authorization"," Bearer "+context.getBearerToken());

        final Optional<Proxy> proxy = getProxy(context.getContext(), context.getProxy(), url);

        final HTTPGenerator http = new HTTPGenerator(method, url,  headers, parameters, proxy);
        try
        {
            if(context.getTracemode().isPresent()&&context.getTracemode().get().equalsIgnoreCase("TRUE"))
            {
                context.getContext().getLogger().info("Whiteblock rpc service, :\n" + http.getRequest() +
                        "\n" + payload);

            }
            HttpResponse response=http.execute();
            if(context.getTracemode().isPresent()&&context.getTracemode().get().equalsIgnoreCase("TRUE"))
            {
                context.getContext().getLogger().info("Whiteblock rpc response, :\n" + response.toString() );

            }
            final int statusCode = response.getStatusLine().getStatusCode();
            if(isSuccessHttpCode(statusCode))
            {
                return HttpResponseUtils.getJsonResponse(response);
            }
            final String stringResponse = HttpResponseUtils.getStringResponse(response);
            throw new WhiteBlockHttpException(response.getStatusLine().getReasonPhrase() + " - " + url +
                    " - " + payload + " - " + stringResponse);

        }
        catch (Exception e)
        {
            throw new WhiteBlockHttpException(e.getMessage());
        }
    }
    public static JSONObject jsonRequest(String method, String path, String payload, WhiteblockHttpContext context)
            throws Exception {
	    return jRequest(method, context.HOST + path, payload, context);
    }


    public static JSONArray jsonArrRequest(String method, String path, String payload, WhiteblockHttpContext context)
            throws Exception {
        final Map<String, String> parameters = new HashMap<>();

        final Map<String, String> headers = new HashMap<>();
        String url = context.HOST + path;
        headers.put("Authorization"," Bearer "+context.getBearerToken());

        final Optional<Proxy> proxy = getProxy(context.getContext(), context.getProxy(), url);

        final HTTPGenerator http = new HTTPGenerator(method, url,  headers, parameters, proxy);
        try
        {
            if(context.getTracemode().isPresent()&&context.getTracemode().get().equalsIgnoreCase("TRUE"))
            {
                context.getContext().getLogger().info("Whiteblock rpc service, :\n" + http.getRequest() +
                        "\n" + payload);

            }
            HttpResponse response=http.execute();
            if(context.getTracemode().isPresent()&&context.getTracemode().get().equalsIgnoreCase("TRUE"))
            {
                context.getContext().getLogger().info("Whiteblock rpc response, :\n" + response.toString() );

            }
            final int statusCode = response.getStatusLine().getStatusCode();
            if(isSuccessHttpCode(statusCode))
            {
                return HttpResponseUtils.getJsonArrayResponse(response);
            }
            final String stringResponse = HttpResponseUtils.getStringResponse(response);
            throw new WhiteBlockHttpException(response.getStatusLine().getReasonPhrase() + " - " + url +
                    " - " + payload + " - " + stringResponse);

        }
        catch (Exception e)
        {
            throw new WhiteBlockHttpException(e.getMessage());
        }
    }


    public static Optional<Proxy> getProxy(final Context context, final Optional<String> proxyName, final String url)
            throws MalformedURLException {
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
