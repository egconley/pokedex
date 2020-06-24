package com.egconley.pokemonAPI.models.pokemon;

public class Sprites {

    private String front_default;
    private String back_default;

    public Sprites(String front_default, String back_default) {
        this.front_default = front_default;
        this.back_default = back_default;
    }

    public String getFront_default() {
        return front_default;
    }

    public String getBack_default() {
        return back_default;
    }
}
