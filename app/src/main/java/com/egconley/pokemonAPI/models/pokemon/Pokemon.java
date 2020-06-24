package com.egconley.pokemonAPI.models.pokemon;

import java.util.ArrayList;

public class Pokemon {

    private int id;
    private String name;
    private int height;
    private int weight;
    private ArrayList<Ability> abilities;
    private ArrayList<Move> moves;
    private ArrayList<Type> types;
    private Sprites sprites;

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public int getHeight() {
        return height;
    }

    public int getWeight() {
        return weight;
    }

    public ArrayList<Ability> getAbilities() {
        return abilities;
    }

    public ArrayList<Move> getMoves() {
        return moves;
    }

    public ArrayList<Type> getTypes() {
        return types;
    }

    public Sprites getSprites() {
        return sprites;
    }

}
