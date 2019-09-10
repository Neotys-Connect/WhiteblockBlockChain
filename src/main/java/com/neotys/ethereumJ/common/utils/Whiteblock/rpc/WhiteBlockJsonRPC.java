package com.neotys.ethereumJ.common.utils.Whiteblock.rpc;


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


public class WhiteBlockJsonRPC {



    public static JSONObject sendquery(String methods,Map<String,Object> params,WhiteblockHttpContext context) throws Exception {
        final Map<String, String> parameters = new HashMap<>();

        final Map<String, String> headers = new HashMap<>();
        String url="https://"+context.getWhiteblocMasterHost()+":"+context.getRpcport()+"/rpc";
        headers.put("Authorization"," Bearer "+context.getRpctoken());


        ZonedDateTime now = ZonedDateTime.now(ZoneOffset.UTC);

        JSONArray jsonParams = new JSONArray();
        JSONObject json=new JSONObject();
        params.forEach((k,v)->{

            try {
                if(!k.toString().trim().isEmpty())
                {
                    json.put(k,v);

                }
                else
                {
                    jsonParams.put(v);
                    if(context.getTracemode().isPresent())
                        context.getContext().getLogger().debug("add parameter "+v.toString());
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }

        });
        if(json.length()>0)
            jsonParams.put(json);


        if(context.getTracemode().isPresent())
            context.getContext().getLogger().debug("params  "+jsonParams.toString());


        JSONObject payload= new JSONObject();
        payload.put("id", randomUUID().hashCode());
        payload.put("method",methods);

        if(json.length()>0)
            payload.put("params",json);
        else
            payload.put("params",jsonParams);

        payload.put("jsonrpc","2.0");

        final Optional<Proxy> proxy = getProxy(context.getContext(), context.getProxy(), url);

        final HTTPGenerator http = HTTPGenerator.newJsonHttpGenerator(HTTP_POST_METHOD, url,  headers, parameters, proxy, payload.toString());

        try
        {
            if(context.getTracemode().isPresent()&&context.getTracemode().get().equalsIgnoreCase("TRUE"))
            {
                context.getContext().getLogger().info("Whiteblock rpc service, :\n" + http.getRequest() + "\n" + payload.toString());

            }
            HttpResponse response=http.execute();
            if(context.getTracemode().isPresent()&&context.getTracemode().get().equalsIgnoreCase("TRUE"))
            {
                context.getContext().getLogger().info("Whiteblock rpc response, :\n" + response.toString() );

            }
            final int statusCode = response.getStatusLine().getStatusCode();
            if(isSuccessHttpCode(statusCode))
            {
                JSONObject result= HttpResponseUtils.getJsonResponse(response);
                if(result.has("error"))
                    throw new WhiteBlockHttpException("Error sendin rpc :"+payload.toString()+"- getting following error : "+result.getJSONObject("error").toString());
                if(context.getTracemode().isPresent()&&context.getTracemode().get().equalsIgnoreCase("TRUE"))
                {
                    context.getContext().getLogger().info("Whiteblock json response, :\n" + result.toString() );

                }
                return  result;
            }
            else if(statusCode != HttpStatus.SC_BAD_REQUEST && statusCode != HttpStatus.SC_NOT_FOUND) {
                final String stringResponse = HttpResponseUtils.getStringResponse(response);
                throw new WhiteBlockHttpException(response.getStatusLine().getReasonPhrase() + " - " + url + " - " + payload.toString() + " - " + stringResponse);
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
