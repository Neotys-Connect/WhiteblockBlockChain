package com.neotys.ethereumJ.common.utils.Whiteblock.rest;


import com.google.api.client.auth.oauth2.BearerToken;
import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.auth.oauth2.RefreshTokenRequest;
import com.google.api.client.auth.oauth2.TokenResponse;
import com.google.api.client.http.BasicAuthentication;
import com.google.api.client.http.GenericUrl;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.common.base.Optional;
import com.neotys.extensions.action.engine.Context;

import java.util.ArrayList;
import java.util.Collection;

public class WhiteblockHttpContext {
    private Optional<String> tracemode;
    private Context context;
    private Optional<String> proxy;
    private final Credential credential;
    public static final String DOMAIN = "infra.whiteblock.io";
    public static final String HOST = "https://"+DOMAIN;
    public static final String TOKEN_URL = "https://auth."+DOMAIN+"/auth/realms/wb/protocol/openid-connect/token";
    public static final String AUTH_URL = "https://auth."+DOMAIN+"/auth/realms/wb/protocol/openid-connect/auth";

    public WhiteblockHttpContext(String refreshToken, Optional<String> tracemode, Context context, Optional<String> proxy)
            throws Exception {
        this.tracemode = tracemode;
        this.context = context;
        this.proxy=proxy;
        Collection<String> scopes = new ArrayList<>();
        scopes.add("offline_access");
        RefreshTokenRequest request = new RefreshTokenRequest(
                new NetHttpTransport(),
                new JacksonFactory(),
                new GenericUrl(TOKEN_URL),
                refreshToken).setScopes(scopes).setClientAuthentication(
                        new BasicAuthentication("cli", ""));

        TokenResponse tr = request.execute();
        context.getLogger().info("got the access token, :\n" + tr.getAccessToken() );

        this.credential = new Credential.Builder(BearerToken.authorizationHeaderAccessMethod()).setTransport(
                new NetHttpTransport()).setClientAuthentication(
                new BasicAuthentication("cli", ""))
                .setJsonFactory(new JacksonFactory())
                .setTokenServerUrl(new GenericUrl(TOKEN_URL))
                .build()
                .setFromTokenResponse(tr);
    }


    public Optional<String> getProxy() {
        return proxy;
    }

    public void setProxy(Optional<String> proxy) {
        this.proxy = proxy;
    }

    public String getBearerToken() {
        String token = this.credential.getAccessToken();
        return token;
    }

    public Optional<String> getTracemode() {
        return tracemode;
    }

    public void setTracemode(Optional<String> tracemode) {
        this.tracemode = tracemode;
    }

    public Context getContext() {
        return context;
    }

    public void setContext(Context context) {
        this.context = context;
    }


    public boolean isTraceModeActive() {
        return this.getTracemode().isPresent() && this.tracemode.get().equalsIgnoreCase("TRUE");
    }
}



