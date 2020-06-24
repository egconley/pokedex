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
import com.egconley.pokemonAPI.models.moves.EffectEntry;
import com.egconley.pokemonAPI.models.moves.MoveDetail;
import com.egconley.pokemonAPI.models.pokemon.Move;
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

    // connect to pokemon api
    private Retrofit retrofit = new Retrofit.Builder()
            .baseUrl("https://pokeapi.co/api/v2/")
            .addConverterFactory(GsonConverterFactory.create())
            .build();
    private PokemonAPIService pokemonAPIService = retrofit.create(PokemonAPIService.class);

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

    // menu item generates new team and sends a welcome toast
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

    // filters recycler view by entered text in search bar
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

    // generates a list of 7 random different numbers between 1-151, corresponding to pokemon numbers
    private void getTeam() {
        Set<Integer> set = getRandomNumberSet();
        // clear new team list each time to allow for serial clicks on "Generate New Random Team"
        team.clear();
        for (Integer i : set) {
            getPokemon(i);
        }
    }

    // fetches pokemon using random numbers from getTeam()
    private void getPokemon(int number) {
        Call<Pokemon> call = pokemonAPIService.getPokemonByNameOrNumber(""+number, "pokemon");
        call.enqueue(new Callback<Pokemon>() {
            @Override
            public void onResponse(Call<Pokemon> call, Response<Pokemon> response) {
                if (!response.isSuccessful()) {
                    Log.d(TAG, "Response Code: " + response.code());
                    return;
                }

                Pokemon pokemon = response.body();
                // calls api at move endpoint to populate pokemon moves with move effect details
                populateMoveEffects(pokemon);
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

    // fetches moves from the moves endpoint to get effect info for each pokemon, notifies adapter to update data
    private void populateMoveEffects(final Pokemon poke) {
        ArrayList<Move> moves = poke.getMoves();

        for (final Move move : moves) {
            String name = move.getMove().getName();
            Call<MoveDetail> call = pokemonAPIService.getMoveByNameOrNumber(name, "move");
            call.enqueue(new Callback<MoveDetail>() {
                @Override
                public void onResponse(Call<MoveDetail> call, Response<MoveDetail> response) {
                    if (!response.isSuccessful()) {
                        Log.d(TAG, "Response Code: " + response.code());
                        return;
                    }

                    MoveDetail moveDetail = response.body();
                    ArrayList<EffectEntry> effects = moveDetail.getEffectEntries();
                    for (EffectEntry entry : effects) {
                        move.getMove().setEffect(entry.getEffect());
                        move.getMove().setShortEffect(entry.getShort_effect());
                    }

                    // notifies recycler view adapter that data has changed to avoid null effect values in detail view
                    adapter.newList((ArrayList<Pokemon>) team);
                }

                @Override
                public void onFailure(Call<MoveDetail> call, Throwable t) {
                    Log.d(TAG, "Oh no!!! Moves call didn't work." + t.getMessage());
                }
            });
        }
    }

}
