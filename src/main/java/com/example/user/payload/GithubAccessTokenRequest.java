package com.example.user.payload;

import lombok.Builder;

import java.io.Serializable;

public class GithubAccessTokenRequest implements Serializable {
    private static final long serialVersionUID = -3504586520124861349L;
    private final String client_id;
    private final String client_secret;
    private final String code;
    private final String state;

    @Builder
    public GithubAccessTokenRequest(String client_id, String client_secret, String code, String state, String redirect_uri) {
        this.client_id = client_id;
        this.client_secret = client_secret;
        this.code = code;
        this.state = state;
    }
}
