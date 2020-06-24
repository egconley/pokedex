package com.egconley;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.MenuItemCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SearchView;
import android.widget.Toast;

import com.egconley.pokemonAPI.PokemonAPIService;
import com.egconley.pokemonAPI.models.pokemon.Pokemon;
import com.egconley.pokemonAPI.models.pokemon.Type;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static com.egconley.util.RandomNumberSetGenerator.getRandomNumberSet;
import static com.egconley.util.StringArrayGenerator.getTypesString;
import static com.egconley.util.StringArrayGenerator.getTypesStringArray;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "egc.MainActivity";

    private Toolbar toolbar;

    // REQ 5 Connect Pokedex Client App to The open Pokemon API using Retro fit
    private Retrofit retrofit = new Retrofit.Builder()
            .baseUrl("https://pokeapi.co/api/v2/")
            .addConverterFactory(GsonConverterFactory.create())
            .build();
    private PokemonAPIService pokemonAPIService = retrofit.create(PokemonAPIService.class);

    //

    private List<Pokemon> team = new ArrayList<>();
    private boolean newTeamRequested = false;

    private MainRecyclerViewAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Log.i(TAG, "On Create started.");

        // Resource used: https://www.youtube.com/watch?v=e53cf9mglH8
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getTeam();
    }

    // Resource used: https://www.youtube.com/watch?v=LD2zsCAAVXw
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        final MenuItem searchMenu = menu.findItem(R.id.searchBar);

        SearchView searchBar = (SearchView) MenuItemCompat.getActionView(searchMenu);
        searchBar.setQueryHint(getString(R.string.search_placeholder_text));
        if (searchBar != null) {
            // Resource used: https://www.youtube.com/watch?v=OWwOSLfWboY
            searchBar.setOnQueryTextListener(new SearchView.OnQueryTextListener() {

                @Override
                public boolean onQueryTextSubmit(String query) {
                    filter(query.toString());
                    return true;
                }

                @Override
                public boolean onQueryTextChange(String newText) {
                    filter(newText.toString());
                    return true;
                }
            });
        }
        return super.onCreateOptionsMenu(menu);
    }

    // REQ 6 Bonus Feature: Implement a menu with two options.
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.new_team:
                // generate new team
                newTeamRequested=true;
                getTeam();
                LayoutInflater inflater = getLayoutInflater();

                View layout = inflater.inflate(R.layout.toast, (ViewGroup) findViewById(R.id.toast_layout));

                Toast toast = new Toast(getApplicationContext());

                toast.setGravity(Gravity.BOTTOM, 0, 0);

                toast.setView(layout);

                toast.show();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void filter(String text) {
        ArrayList<Pokemon> filteredList = new ArrayList();

        for (Pokemon p : team) {
            ArrayList<Type> types = p.getTypes();
            ArrayList<String> typesStringArray = getTypesStringArray(types);
            String typesString = getTypesString(typesStringArray);

            boolean nameContainsSearchedText = p.getName().toLowerCase().contains(text.toLowerCase());
            boolean typesContainsSearchedText = typesString.toLowerCase().contains(text.toLowerCase());

            if (nameContainsSearchedText || typesContainsSearchedText) {
                filteredList.add(p);
            }
        }

        adapter.filteredList(filteredList);
    }

    private void initRecyclerView() {
        RecyclerView recyclerView = findViewById(R.id.recyclerview);
        adapter = new MainRecyclerViewAdapter(this, team);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

    // REQ 5
    // REQ 5.1 Generate a list of 7 random different numbers between 1-151. These numbers corresponds to pokemon_numbers
    private void getTeam() {
        Set<Integer> set = getRandomNumberSet();
        // clean new team list each time to allow for serial clicks on "Generate New Random Team"
        team.clear();
        for (Integer i : set) {
            getPokemon(i);
        }
    }

    // REQ 5.2 Fetch the pokemon using those pokemon_numbers to call the endpoint https://pokeapi.co/api/v2/pokemon/{pokemon_number}/
    private void getPokemon(int number) {
        Call<Pokemon> call = pokemonAPIService.getByNumber(""+number, "pokemon");
        call.enqueue(new Callback<Pokemon>() {
            @Override
            public void onResponse(Call<Pokemon> call, Response<Pokemon> response) {
                if (!response.isSuccessful()) {
                    Log.d(TAG, "Response Code: " + response.code());
                    return;
                }

                Pokemon pokemon = response.body();
                team.add(pokemon);
                if (newTeamRequested==false) {
                    initRecyclerView();
                } else {
                    adapter.newList((ArrayList<Pokemon>) team);
                }

            }

            @Override
            public void onFailure(Call<Pokemon> call, Throwable t) {
                Log.d(TAG, "Oh no!!! " + t.getMessage());
            }
        });
    }

}
