package com.egconley.pokemonAPI.models.moves;

import java.util.ArrayList;

public class MoveDetail {

    private int id;
    private String name;
    private ArrayList<EffectEntry> effect_entries;

    public MoveDetail(int id, String name, ArrayList<EffectEntry> effect_entries) {
        this.id = id;
        this.name = name;
        this.effect_entries = effect_entries;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public ArrayList<EffectEntry> getEffectEntries() {
        return effect_entries;
    }

}
