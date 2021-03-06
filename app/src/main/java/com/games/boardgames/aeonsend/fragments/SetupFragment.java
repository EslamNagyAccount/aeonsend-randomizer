package com.games.boardgames.aeonsend.fragments;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewStub;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ListView;

import com.games.boardgames.aeonsend.GeneratedSetupActivity;
import com.games.boardgames.aeonsend.R;
import com.games.boardgames.aeonsend.adapter.MarketGridViewAdapter;
import com.games.boardgames.aeonsend.adapter.MarketListViewAdapter;
import com.games.boardgames.aeonsend.cards.MarketSetupCard;
import com.games.boardgames.aeonsend.database.MarketSetupCardList;
import com.games.boardgames.aeonsend.utils.Constants;
import com.games.boardgames.aeonsend.listeners.OnDataPass;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by honza on 29.9.17.
 */

public class SetupFragment extends Fragment {
    private ViewStub stubGrid;
    private ViewStub stubList;
    private ListView listView;
    private GridView gridView;
    private MarketGridViewAdapter marketGridViewAdapter;
    private MarketListViewAdapter marketListViewAdapter;
    private View view;
    private OnDataPass dataPasser;

    private int currentViewMode = 0;

    static final int VIEW_MODE_LISTVIEW = 0;
    static final int VIEW_MODE_GRIDVIEW = 1;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        dataPasser = (OnDataPass) context;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.setup_fragment, container, false);

        stubGrid = (ViewStub) view.findViewById(R.id.setup_fragment_stub_grid);
        stubList = (ViewStub) view.findViewById(R.id.setup_fragment_stub_list);

        stubGrid.inflate();
        stubList.inflate();

        SharedPreferences sharedPreferences = getContext().getSharedPreferences("ViewMode", view.getContext().MODE_PRIVATE);
        // Default view is ListView
        currentViewMode = sharedPreferences.getInt("currentViewMode", VIEW_MODE_LISTVIEW);

        gridView = (GridView) view.findViewById(R.id.marketGridView);
        listView = (ListView) view.findViewById(R.id.marketListView);

        switchView();

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                // Get MarketSetupCard from adapterView and pass it to Activity
                passData((MarketSetupCard) adapterView.getItemAtPosition(i));
                // Fetch data from activity into bundle object and pass it to second activity via intent
                Bundle bundle = dataPasser.getFragmentValuesBundle();
                // Start new activity
                Intent intent = new Intent(view.getContext(), GeneratedSetupActivity.class);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                // Get MarketSetupCard from adapterView and pass it to Activity
                passData((MarketSetupCard) adapterView.getItemAtPosition(i));
                // Fetch data from activity into bundle object and pass it to second activity via intent
                Bundle bundle = dataPasser.getFragmentValuesBundle();
                // Start new activity
                Intent intent = new Intent(view.getContext(), GeneratedSetupActivity.class);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });

        return view;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        // Inflate the menu; this adds items to the action bar if it is present.
//        inflater.inflate(R.menu.menu_main, menu);

        // Find button for layout switching and set it to correct icon correlated to currentViewMode and make it visible
        MenuItem switchLayoutButton = menu.findItem(R.id.main_menu_action_switch_layout);
        switchLayoutButton.setVisible(true);

        SharedPreferences sharedPreferences = view.getContext().getSharedPreferences("ViewMode", MODE_PRIVATE);
        currentViewMode = sharedPreferences.getInt("currentViewMode", VIEW_MODE_LISTVIEW);

        switch (currentViewMode) {
            case VIEW_MODE_LISTVIEW:
                switchLayoutButton.setIcon(R.mipmap.ic_view_module_white_24dp);
                break;
            case VIEW_MODE_GRIDVIEW:
                switchLayoutButton.setIcon(R.mipmap.ic_view_list_white_24dp);
                break;
        }
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.main_menu_action_switch_layout) {

            switch (currentViewMode) {
                case VIEW_MODE_LISTVIEW:
                    // Change icon of layout switching button to list icon
                    item.setIcon(R.mipmap.ic_view_list_white_24dp);
                    currentViewMode = VIEW_MODE_GRIDVIEW;
                    break;
                case VIEW_MODE_GRIDVIEW:
                    // Change icon of layout switching button to grid icon
                    item.setIcon(R.mipmap.ic_view_module_white_24dp);
                    currentViewMode = VIEW_MODE_LISTVIEW;
                    break;
            }
            switchView();

            SharedPreferences sharedPreferences = view.getContext().getSharedPreferences("ViewMode", view.getContext().MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putInt("currentViewMode", currentViewMode);
            editor.apply();

            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void switchView() {
        switch (currentViewMode) {
            case VIEW_MODE_LISTVIEW:
                // Hide GridView
                stubGrid.setVisibility(View.GONE);
                // Display ListView
                stubList.setVisibility(View.VISIBLE);
                break;

            case VIEW_MODE_GRIDVIEW:
                // Display GridView
                stubGrid.setVisibility(View.VISIBLE);
                // Hide ListView
                stubList.setVisibility(View.GONE);
                break;
        }
        setAdapter();

    }

    private void setAdapter() {
        switch (currentViewMode) {
            case VIEW_MODE_LISTVIEW:
                marketListViewAdapter = new MarketListViewAdapter(view.getContext(), MarketSetupCardList.getMarketSetupCards());
                listView.setAdapter(marketListViewAdapter);
                break;
            case VIEW_MODE_GRIDVIEW:
                marketGridViewAdapter = new MarketGridViewAdapter(view.getContext(), MarketSetupCardList.getMarketSetupCards());
                gridView.setAdapter(marketGridViewAdapter);
                break;
        }
    }

    private void passData(MarketSetupCard card) {
        dataPasser.onDataPass(Constants.EXTRASCHOSENSETUP, card);
    }
}
