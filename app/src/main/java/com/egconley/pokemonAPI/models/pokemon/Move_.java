package com.egconley.pokemonAPI.models.pokemon;

public class Move_ {

    private String name;
    private String url;
    private String effect;
    private String shortEffect;

    public Move_(String name, String url) {
        this.name = name;
        this.url = url;
        this.effect = effect;
        this.shortEffect = shortEffect;
    }

    public String getName() {
        return name;
    }

    public String getUrl() {
        return url;
    }

    public String getEffect() {
        return effect;
    }

    public String getShortEffect() {
        return shortEffect;
    }

    public void setEffect(String effect) {
        this.effect = effect;
    }

    public void setShortEffect(String shortEffect) {
        this.shortEffect = shortEffect;
    }
}
