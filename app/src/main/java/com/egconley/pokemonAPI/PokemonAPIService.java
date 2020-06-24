package com.egconley.pokemonAPI;

import com.egconley.pokemonAPI.models.moves.MoveDetail;
import com.egconley.pokemonAPI.models.pokemon.Pokemon;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

// Resources used: https://www.youtube.com/watch?v=4JGvDUlfk7Y&vl=en
//                https://medium.com/@jackson.mclane20/retrofit-tutorial-pokeapi-e84f800f11dd
public interface PokemonAPIService {
    @GET("{endpoint}/{number}/")
    Call<Pokemon> getPokemonByNameOrNumber(@Path("number") String number, @Path("endpoint") String endpoint);

    @GET("{endpoint}/{number}/")
    Call<MoveDetail> getMoveByNameOrNumber(@Path("number") String number, @Path("endpoint") String endpoint);
}
