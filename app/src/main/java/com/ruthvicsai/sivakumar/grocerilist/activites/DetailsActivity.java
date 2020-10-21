package com.ruthvicsai.sivakumar.grocerilist.activites;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.oguzdev.circularfloatingactionmenu.library.FloatingActionMenu;
import com.oguzdev.circularfloatingactionmenu.library.SubActionButton;
import com.ruthvicsai.sivakumar.grocerilist.FloatingActionButtonOriginal;
import com.ruthvicsai.sivakumar.grocerilist.MainActivity;
import com.ruthvicsai.sivakumar.grocerilist.R;

public class DetailsActivity extends AppCompatActivity {

    SubActionButton viewListsBtn, viewFavoritesBtn, homeBtn, settingsBtn;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_details);

        setUpFab();
        FabSubButtonOnCLick();
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
        //icon.setImageResource(R.drawable.ic_foreground_192pxxxhdpi);
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
        //addNewListBtn = itemBuilder.setContentView(addNewListIcon).build();
        settingsBtn = itemBuilder.setContentView(settingsIcon).build();
        homeBtn = itemBuilder.setContentView(homeIcon).build();

        actionButton.setId(R.id.faButtonMain);
        viewListsBtn.setId(R.id.faButtonAllList);
        viewFavoritesBtn.setId(R.id.faButtonFavoriteList);
        // addNewListBtn.setId(R.id.faButtonPlus);
        settingsBtn.setId(R.id.faSettingsButton);
        homeBtn.setId(R.id.faHomeButton);

        actionMenu = new FloatingActionMenu.Builder(this)
                .addSubActionView(settingsBtn)
                //.addSubActionView(addNewListBtn)
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

        /*addNewListBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogAddNewList dialog = new DialogAddNewList();
                dialog.show(getSupportFragmentManager(), "my");
                //dataOfLists.add(newListString);

                saveListsData();
            }
        });*/

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