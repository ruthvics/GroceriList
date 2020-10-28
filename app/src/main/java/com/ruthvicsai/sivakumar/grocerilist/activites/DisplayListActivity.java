package com.ruthvicsai.sivakumar.grocerilist.activites;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.daimajia.swipe.util.Attributes;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.oguzdev.circularfloatingactionmenu.library.FloatingActionMenu;
import com.oguzdev.circularfloatingactionmenu.library.SubActionButton;
import com.ruthvicsai.sivakumar.grocerilist.BarcodeItem;
import com.ruthvicsai.sivakumar.grocerilist.FloatingActionButtonOriginal;
import com.ruthvicsai.sivakumar.grocerilist.MainActivity;
import com.ruthvicsai.sivakumar.grocerilist.R;
import com.ruthvicsai.sivakumar.grocerilist.adapters.RecyclerViewAdapter;
import com.ruthvicsai.sivakumar.grocerilist.dialogs.DialogAddNewList;

import java.lang.reflect.Type;
import java.util.ArrayList;

public class DisplayListActivity extends AppCompatActivity implements DialogAddNewList.OnInputListener {


    private RecyclerView recyclerView;
    private RecyclerView.Adapter mAdapter;

    private String mListName;
    private com.google.android.material.floatingactionbutton.FloatingActionButton addNewItem;
    private ArrayList<BarcodeItem> mDataSet = new ArrayList<>();
    private Context mContext  = this;
    private int tutorialId;
    SubActionButton viewListsBtn, viewFavoritesBtn, addNewListBtn, settingsBtn, homeBtn;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recycler_view_display);



        Intent displayList = getIntent();
        mListName = displayList.getStringExtra("listName");

        tutorialId = displayList.getIntExtra("tutorialId", 0);

        setTitle(mListName + " List");

        loadData(mListName);

        setUpFab();
        FabSubButtonOnCLick();

        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {

        }

        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        if(mDataSet.size() == 0)
        {
            BarcodeItem exampleBI = new BarcodeItem("name", "123456789");
            mDataSet.add(exampleBI);
        }

        // Adapter:
        mAdapter = new RecyclerViewAdapter(this, mDataSet, mListName, tutorialId);
        ((RecyclerViewAdapter) mAdapter).setMode(Attributes.Mode.Single);
        recyclerView.setAdapter(mAdapter);

        saveData(mListName);
    }




    private void saveData(String s) {
        SharedPreferences sharedPreferences = getSharedPreferences("shared", MODE_PRIVATE );
        SharedPreferences.Editor editor = sharedPreferences.edit();
        Gson gson = new Gson();
        String json = gson.toJson(mDataSet);
        editor.putString(s, json);
        editor.apply();
    }

    private void loadData(String s){
        SharedPreferences sharedPreferences = getSharedPreferences("shared", MODE_PRIVATE );
        Gson gson = new Gson();
        String json = sharedPreferences.getString(s,null);
        Type type = new TypeToken<ArrayList<BarcodeItem>>() {}.getType();
        mDataSet = gson.fromJson(json, type);

        if(mDataSet == null)
        {
            mDataSet = new ArrayList<>();
        }
    }

    @Override
    public void sendInput(String input) {
        BarcodeItem addToList = new BarcodeItem(input);
        mDataSet.add(addToList);
        saveData(mListName);

    }

    private void setUpFab()
    {
        FloatingActionMenu actionMenu;
        FloatingActionButtonOriginal actionButton;


        Drawable addNewListD = getResources().getDrawable( R.drawable.plus_with_border );
        Drawable viewListsIconD = getResources().getDrawable( R.drawable.view_all_lists_icon );
        Drawable favoriteIconD = getResources().getDrawable(R.drawable.favorite_list_icon42);
        Drawable settingsIconD = getResources().getDrawable(R.drawable.settings_icon);
        Drawable homeIconD = getResources().getDrawable(R.drawable.home_icon2);

        ImageView icon = new ImageView(this);
        icon.setImageResource(R.drawable.grocerilisticon13);



        actionButton = new FloatingActionButtonOriginal.Builder(this)
                .setContentView(icon)
                //.setTheme(0)
                .build();




        SubActionButton.Builder itemBuilder = new SubActionButton.Builder(this);

        ImageView addNewListIcon = new ImageView(this);
        addNewListIcon.setImageDrawable(addNewListD);
        ImageView viewListsIcon = new ImageView(this);
        viewListsIcon.setImageDrawable(viewListsIconD);
        ImageView favoriteIcon = new ImageView(this);
        favoriteIcon.setImageDrawable(favoriteIconD);
        ImageView settingsIcon = new ImageView(this);
        settingsIcon.setImageDrawable(settingsIconD);
        ImageView homeIcon = new ImageView(this);
        homeIcon.setImageDrawable(homeIconD);

        viewListsBtn = itemBuilder.setContentView(viewListsIcon).build();
        viewFavoritesBtn = itemBuilder.setContentView(favoriteIcon).build();
        settingsBtn = itemBuilder.setContentView(settingsIcon).build();
        homeBtn = itemBuilder.setContentView(homeIcon).build();

        actionButton.setId(R.id.faButtonMain);
        viewListsBtn.setId(R.id.faButtonAllList);
        viewFavoritesBtn.setId(R.id.faButtonFavoriteList);
        settingsBtn.setId(R.id.faSettingsButton);
        homeBtn.setId(R.id.faHomeButton);
        if(mListName == null)
        {
            mListName = "example";
        }

        if(mListName.equals("Favorites"))
        {
            actionMenu = new FloatingActionMenu.Builder(this)
                    .addSubActionView(settingsBtn)
                    .addSubActionView(viewListsBtn)
                    .addSubActionView(homeBtn)
                    .attachTo(actionButton)
                    .build();
        }
        else {

            actionMenu = new FloatingActionMenu.Builder(this)
                    .addSubActionView(settingsBtn)
                    .addSubActionView(viewListsBtn)
                    .addSubActionView(homeBtn)
                    .addSubActionView(viewFavoritesBtn)
                    .attachTo(actionButton)
                    .build();
        }

    }

    private void FabSubButtonOnCLick()
    {
        viewListsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent viewGridList = new Intent(getApplicationContext(), ViewListsActivity.class);
                startActivity(viewGridList);
            }
        });

        viewFavoritesBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent goToActivity = new Intent(getApplicationContext(), DisplayListActivity.class);
                goToActivity.putExtra("listName", "Favorites");
                startActivity(goToActivity);
            }
        });


        settingsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent settingsAc = new Intent(getApplicationContext(), SettingsActivity.class);
                startActivity(settingsAc);
            }
        });

        homeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent goToMain = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(goToMain);
            }
        });
    }

}