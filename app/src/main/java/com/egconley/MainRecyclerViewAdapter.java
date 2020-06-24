package com.egconley;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.egconley.pokemonAPI.models.pokemon.Ability;
import com.egconley.pokemonAPI.models.pokemon.Move;
import com.egconley.pokemonAPI.models.pokemon.Sprites;
import com.egconley.pokemonAPI.models.pokemon.Type;
import com.egconley.pokemonAPI.models.pokemon.Pokemon;

import java.util.ArrayList;
import java.util.List;

import static com.egconley.util.FirstLetterCapitalizer.capitalizeFirstLetter;
import static com.egconley.util.FirstLetterCapitalizer.capitalizeStringsInArray;
import static com.egconley.util.StringArrayGenerator.getAbilitiesStringArray;
import static com.egconley.util.StringArrayGenerator.getMovesStringArray;
import static com.egconley.util.StringArrayGenerator.getTypesString;
import static com.egconley.util.StringArrayGenerator.getTypesStringArray;

// REQ 2: Render the list of 7 pokemon in a RecyclerView and use ConstraintLayout to display the ui of each pokemon.
// Resource used: https://www.youtube.com/watch?v=Vyqz_-sJGFk
public class MainRecyclerViewAdapter extends RecyclerView.Adapter<MainRecyclerViewAdapter.ViewHolder>{

    private static final String TAG = "egc.MainRVAdapter";

    private Context mContext;
    private List<Pokemon> mPokemon = new ArrayList();

    // default constructor
    public MainRecyclerViewAdapter(Context mContext, List<Pokemon> mPokemon) {
        this.mPokemon = mPokemon;
        this.mContext = mContext;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_pokemonlist, parent, false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
        Log.d(TAG, "onBindViewHolder called.");

        // get data in String/ArrayList<String> form, formatted with first letter capitalized
        ArrayList<Ability> abilities = mPokemon.get(position).getAbilities();
        final ArrayList<String> abilitiesStringArray = capitalizeStringsInArray(getAbilitiesStringArray(abilities));

        ArrayList<Move> moves = mPokemon.get(position).getMoves();
        final ArrayList<String> movesStringArray = capitalizeStringsInArray(getMovesStringArray(moves));

        ArrayList<Type> types = mPokemon.get(position).getTypes();
        ArrayList<String> typesStringArray = capitalizeStringsInArray(getTypesStringArray(types));
        final String typesString = getTypesString(typesStringArray);

        Sprites sprites = mPokemon.get(position).getSprites();
        final String frontPic = sprites.getFront_default();
        final String backPic = sprites.getBack_default();

        // populate data for recycler view in main activity
        Glide.with(mContext)
                .load(frontPic)
                .centerCrop()
                .placeholder(R.drawable.ic_launcher_foreground)
                .into(holder.pokemonPic);
        holder.pokemonName.setText("Name: " + capitalizeFirstLetter(mPokemon.get(position).getName()));
        holder.level.setText("Species Number: "+ mPokemon.get(position).getId());
        holder.pokemonTypes.setText("Types: " + typesString);

        // set onclick listener to start detail view and send data for clicked pokemon
        holder.listItemLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "Clicked on: " + mPokemon.get(position).getName());
                Intent goToDetailView = new Intent(mContext, DetailViewActivity.class);

                goToDetailView.putExtra("kName", capitalizeFirstLetter(mPokemon.get(position).getName()));
                goToDetailView.putExtra("kFrontPic", frontPic);
                goToDetailView.putExtra("kBackPic", backPic);
                goToDetailView.putExtra("kWeight", ""+mPokemon.get(position).getWeight());
                goToDetailView.putExtra("kTypes", typesString);
                goToDetailView.putExtra("kNumber", ""+mPokemon.get(position).getId());
                goToDetailView.putExtra("kHeight", ""+mPokemon.get(position).getHeight());
                goToDetailView.putStringArrayListExtra("moves", movesStringArray);
                goToDetailView.putStringArrayListExtra("abilities", abilitiesStringArray);

                mContext.startActivity(goToDetailView);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mPokemon.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        ImageView pokemonPic;
        TextView pokemonName;
        TextView level;
        TextView pokemonTypes;
        ConstraintLayout listItemLayout;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            pokemonPic = itemView.findViewById(R.id.main_imageview);
            pokemonName = itemView.findViewById(R.id.name_textview);
            level = itemView.findViewById(R.id.number_textview);
            pokemonTypes = itemView.findViewById(R.id.types_textview);
            listItemLayout = itemView.findViewById(R.id.pokemonlist_layout);
        }
    }

    public void filteredList(ArrayList<Pokemon> filteredList) {
        mPokemon = filteredList;
        notifyDataSetChanged();
    }

    public void newList(ArrayList<Pokemon> newList) {
        mPokemon = newList;
        notifyDataSetChanged();
    }
}
