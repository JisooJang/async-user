package com.example.user.payload;

import lombok.Builder;

public class GithubAccessTokenRequest {
    private final String client_id;
    private final String client_secret;
    private final String code;
    private final String status;
    private final String redirect_uri;

    @Builder
    public GithubAccessTokenRequest(String client_id, String client_secret, String code, String status, String redirect_uri) {
        this.client_id = client_id;
        this.client_secret = client_secret;
        this.code = code;
        this.status = status;
        this.redirect_uri = redirect_uri;
    }
}
