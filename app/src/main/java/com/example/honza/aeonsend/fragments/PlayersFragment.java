package com.example.honza.aeonsend.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.honza.aeonsend.R;

/**
 * Created by honza on 29.9.17.
 */

public class PlayersFragment extends Fragment {

    private TextView textView;
    private ImageView subtractImageView;
    private ImageView addImageView;
    private int numPlayers;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        if (savedInstanceState == null) {
            numPlayers = 4;
        } else {
            numPlayers = savedInstanceState.getInt("numPlayers");
        }

        View view = inflater.inflate(R.layout.players_fragment, container, false);

        textView = view.findViewById(R.id.players_fragment_num_players_value_textview);
        textView.setText(String.valueOf(numPlayers));

        subtractImageView = view.findViewById(R.id.players_fragment_subtract_imageview);
        subtractImageView.setImageResource(R.drawable.ic_remove_black_48dp);
        subtractImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (numPlayers != 1) {
                    numPlayers--;
                }
                textView.setText(String.valueOf(numPlayers));
            }
        });

        addImageView = view.findViewById(R.id.players_fragment_add_imageview);
        addImageView.setImageResource(R.drawable.ic_add_black_48dp);
        addImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (numPlayers != 4) {
                    numPlayers++;
                }
                textView.setText(String.valueOf(numPlayers));
            }
        });

        return view;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt("numPlayers", numPlayers);
    }
}