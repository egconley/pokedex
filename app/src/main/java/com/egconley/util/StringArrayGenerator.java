package com.egconley.util;

import com.egconley.pokemonAPI.models.pokemon.Ability;
import com.egconley.pokemonAPI.models.pokemon.Move;
import com.egconley.pokemonAPI.models.pokemon.Type;

import java.util.ArrayList;

public class StringArrayGenerator {

    private static final String TAG = "StringArrayGenerator";

    public static ArrayList<String> getAbilitiesStringArray(ArrayList<Ability> abilityArray) {
        ArrayList<String> stringArray = new ArrayList<>();
        for (Ability a : abilityArray) {
            stringArray.add(a.getAbility().getName());
        }
        return stringArray;
    }

    public static ArrayList<String> getMovesStringArray(ArrayList<Move> moveArray) {
        ArrayList<String> stringArray = new ArrayList<>();
        for (Move m : moveArray) {
            stringArray.add(m.getMove().getName());
        }
        return stringArray;
    }

    public static ArrayList<String> getTypesStringArray(ArrayList<Type> typeArray) {
        ArrayList<String> stringArray = new ArrayList<>();
        for (Type t : typeArray) {
            stringArray.add(t.getType().getName());
        }
        return stringArray;
    }

    public static String getTypesString(ArrayList<String> types) {
        if (types.size()==1) {
            return types.get(0);
        } else {
            StringBuilder builder = new StringBuilder();
            builder.append(types.get(0));
            for (int i=1; i<types.size(); i++) {
                builder.append(", " + types.get(i));
            }
            return builder.toString();
        }
    }
}
