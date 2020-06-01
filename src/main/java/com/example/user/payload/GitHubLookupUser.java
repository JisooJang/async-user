package com.example.user.payload;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class GitHubLookupUser {
    private String name;
    private String avatar_url;
    private String html_url;
    private String blog;

    @Override
    public String toString() {
        return "User [name=" + name + ", " + html_url + "]";
    }
 }
