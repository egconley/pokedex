package com.egconley.pokemonAPI.models.pokemon;

public class Move_ {

    private String name;

    private String url;

    public Move_(String name, String url) {
        this.name = name;
        this.url = url;
    }

    public String getName() {
        return name;
    }

    public String getUrl() {
        return url;
    }
}
