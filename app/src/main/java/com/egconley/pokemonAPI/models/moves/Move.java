package com.egconley.pokemonAPI.models.moves;

import java.util.ArrayList;

public class Move {

    private ArrayList<EffectEntry> effectEntries;

    public Move(ArrayList<EffectEntry> effectEntries) {
        this.effectEntries = effectEntries;
    }

    public ArrayList<EffectEntry> getEffectEntries() {
        return effectEntries;
    }
}
