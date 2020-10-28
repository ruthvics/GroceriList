package com.ruthvicsai.sivakumar.grocerilist.activites;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
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
import com.ruthvicsai.sivakumar.grocerilist.adapters.GridViewAdapter;

import java.lang.reflect.Type;
import java.util.ArrayList;

import uk.co.deanwild.materialshowcaseview.MaterialShowcaseSequence;
import uk.co.deanwild.materialshowcaseview.MaterialShowcaseView;
import uk.co.deanwild.materialshowcaseview.ShowcaseConfig;

public class ViewListsActivity extends AppCompatActivity {

    SubActionButton viewListsBtn, viewFavoritesBtn, homeBtn, settingsBtn;
    private ArrayList<String> listOfData = new ArrayList<>();
    //private String newName;
    private GridViewAdapter mGridadapter;
    private int tutorialID;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        loadData();
        createFavorite();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_all_lists);

        setUpFab();
        FabSubButtonOnCLick();

        Intent mainIntent = getIntent();
        tutorialID = mainIntent.getIntExtra("tutorialId", 0);

        //setNames();
        final GridView gridView = (GridView)findViewById(R.id.listGridView);
        mGridadapter = new GridViewAdapter(this, listOfData, tutorialID);
        mGridadapter.setMode(Attributes.Mode.Multiple);
        gridView.setAdapter(mGridadapter);
        gridView.setSelected(false);

        //tutorial();

        gridView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                //Log.e("onItemClick","onItemClick:" + position);

                Intent viewList = new Intent(getApplicationContext(), DisplayListActivity.class);
                viewList.putExtra("listName", listOfData.get(position));
                startActivity(viewList);
                return false;
            }
        });
    }

    private void saveData() {
        SharedPreferences sharedPreferences = getSharedPreferences("shared", MODE_PRIVATE );
        SharedPreferences.Editor editor = sharedPreferences.edit();
        Gson gson = new Gson();
        String json = gson.toJson(listOfData);
        editor.putString("listOfData", json);
        editor.apply();
    }

    private void loadData(){
        SharedPreferences sharedPreferences = getSharedPreferences("shared", MODE_PRIVATE );
        Gson gson = new Gson();
        String json = sharedPreferences.getString("listOfData",null);
        Type type = new TypeToken<ArrayList<String>>() {}.getType();
        listOfData = gson.fromJson(json, type);

        if(listOfData == null)
        {
            listOfData = new ArrayList<>();
        }
    }

    void createFavorite()
    {
        if(listOfData.size() == 0) {
            listOfData.add("Favorites");
            addExampleItem("Favorites");
        }
    }

    private void createNewList(String name)
    {
        listOfData.add(name);
        saveData();
    }

    private void tutorial()
    {
        ShowcaseConfig config = new ShowcaseConfig();
        config.setDelay(500); // half second between each showcase view

        MaterialShowcaseSequence sequence = new MaterialShowcaseSequence(this/*, SHOWCASE_ID*/);
        sequence.setConfig(config);
        MaterialShowcaseView.Builder sequenceBuilderGoToList;

        final int COLOR_MASK = getResources().getColor(R.color.colorAccentTransparent);
        final int COLOR_DISMISS = getResources().getColor(R.color.colorPrimaryDark);

        sequenceBuilderGoToList = new MaterialShowcaseView.Builder(this)
                .setTarget(findViewById(R.id.nameOfListTextView))
                .renderOverNavigationBar()
                .setDismissOnTargetTouch(true)
                .setTargetTouchable(true)
                .setTitleText("Tips")
                .setContentText("Tips will be displayed here")
                .setDelay(100)
                .withRectangleShape()
                .setMaskColour(COLOR_MASK)
                .setDismissTextColor(COLOR_DISMISS);

        sequence.addSequenceItem(sequenceBuilderGoToList.build());
        sequence.start();

    }

    private void addExampleItem(String s){
        SharedPreferences sharedPreferences = getSharedPreferences("shared", MODE_PRIVATE );
        Gson gson = new Gson();
        String json = sharedPreferences.getString(s,null);
        Type type = new TypeToken<ArrayList<BarcodeItem>>() {}.getType();
        ArrayList<BarcodeItem> listTemp = gson.fromJson(json, type);

        if(listTemp == null)
        {
            listTemp = new ArrayList<>();
        }

        BarcodeItem addExample = new BarcodeItem("name", "123456789");
        listTemp.add(addExample);

        sharedPreferences = getSharedPreferences("shared", MODE_PRIVATE );
        SharedPreferences.Editor editor = sharedPreferences.edit();
        Gson gson2 = new Gson();
        String json2 = gson2.toJson(listTemp);
        editor.putString(s, json2);
        editor.apply();
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

        actionMenu = new FloatingActionMenu.Builder(this)
                .addSubActionView(settingsBtn)
                .addSubActionView(homeBtn)
                .addSubActionView(viewListsBtn)
                .addSubActionView(viewFavoritesBtn)
                .attachTo(actionButton)
                .build();

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