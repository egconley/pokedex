package com.egconley.pokemonAPI.models.moves;

public class EffectEntry {

    private String effect;
    private String short_effect;

    public EffectEntry(String effect, String short_effect) {
        this.effect = effect;
        this.short_effect = short_effect;
    }

    public String getEffect() {
        return effect;
    }

    public String getShort_effect() {
        return short_effect;
    }
}
