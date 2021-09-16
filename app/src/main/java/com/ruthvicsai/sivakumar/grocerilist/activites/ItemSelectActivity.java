package com.ruthvicsai.sivakumar.grocerilist.activites;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.ecloud.pulltozoomview.PullToZoomScrollViewEx;
import com.oguzdev.circularfloatingactionmenu.library.FloatingActionMenu;
import com.oguzdev.circularfloatingactionmenu.library.SubActionButton;
import com.ruthvicsai.sivakumar.grocerilist.BarcodeItem;
import com.ruthvicsai.sivakumar.grocerilist.FloatingActionButtonOriginal;
import com.ruthvicsai.sivakumar.grocerilist.MainActivity;
import com.ruthvicsai.sivakumar.grocerilist.R;
import com.ruthvicsai.sivakumar.grocerilist.dialogs.DialogRenameItem;

import uk.co.deanwild.materialshowcaseview.MaterialShowcaseSequence;
import uk.co.deanwild.materialshowcaseview.MaterialShowcaseView;
import uk.co.deanwild.materialshowcaseview.ShowcaseConfig;

public class ItemSelectActivity extends AppCompatActivity {
    private PullToZoomScrollViewEx scrollView;
    private BarcodeItem barcodeToDisplay = new BarcodeItem();
    private TextView barcodeNameTextView;
    private TextView barcodeValueTextView;
    private Button searchOnAmazonBtn;
    private Button searchOnGoogleBtn;
    private Button searchOnTargetBtn;
    private final String amazonLinkFinal = "https://www.amazon.com/s?k=";
    private final String googleLinkFinal = "https://www.google.com/search?q=";
    private final String targetLinkFinal = "https://www.target.com/s?searchTerm=";
    private String newName;
    private Context mContext = this;
    private int tutorialID;
    private static final String SHOWCASE_ID = "4";
    SubActionButton viewListsBtn, viewFavoritesBtn, addNewListBtn, settingsBtn, homeBtn;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_select);
        loadViewForCode();
        scrollView = (PullToZoomScrollViewEx) findViewById(R.id.scroll_view);
        setUpFab();
        FabSubButtonOnCLick();



        Intent intent = getIntent();
        barcodeToDisplay = intent.getParcelableExtra("BarcodeItem");

        if(barcodeToDisplay == null)
        {
            barcodeToDisplay = new BarcodeItem("name", "123456789");
        }


        setall();

        barcodeNameTextView.setText(barcodeToDisplay.getBarcodeName());
        barcodeValueTextView.setText(barcodeToDisplay.getBarcodeValue());

        searchOnAmazonBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String link = amazonLinkFinal + barcodeToDisplay.getBarcodeValue();
                searchByButtons(link);
            }
        });

        searchOnGoogleBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String link = googleLinkFinal + barcodeToDisplay.getBarcodeValue();
                searchByButtons(link);
            }
        });

        searchOnTargetBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String link = targetLinkFinal + barcodeToDisplay.getBarcodeValue();
                searchByButtons(link);
            }
        });



        TextView changeNameTextView = (TextView) findViewById(R.id.tv_item_name);
        changeNameTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogRenameItem dialog = new DialogRenameItem();
                dialog.show(getSupportFragmentManager(), "my");
            }
        });

        Intent thisIntent = getIntent();
        tutorialID = thisIntent.getIntExtra("tutorialId", 0);
        if(tutorialID == 3) {
            tutorial();
        }

        tutorialFirst();

    }

    private void loadViewForCode() {
        PullToZoomScrollViewEx scrollView = (PullToZoomScrollViewEx) findViewById(R.id.scroll_view);
        View headView = LayoutInflater.from(this).inflate(R.layout.profile_head_view, null, false);
        View zoomView = LayoutInflater.from(this).inflate(R.layout.profile_zoom_view, null, false);
        View contentView = LayoutInflater.from(this).inflate(R.layout.item_content_view, null, false);
        scrollView.setHeaderView(headView);
        scrollView.setZoomView(zoomView);
        scrollView.setScrollContentView(contentView);
        scrollView.setZoomEnabled(false);
    }

    private void setall()
    {
        barcodeNameTextView = (TextView)findViewById(R.id.tv_item_name);
        barcodeValueTextView = (TextView) findViewById(R.id.tv_barcode_value);
        searchOnAmazonBtn = (Button) findViewById(R.id.viewItemAmznBtn);
        searchOnGoogleBtn = (Button) findViewById(R.id.viewItemGgleBtn);
        searchOnTargetBtn = (Button) findViewById(R.id.viewItemTrgtBtn);
    }

    private void searchByButtons(String searchLink)
    {
        System.out.println(searchLink);
        Uri webaddress = Uri.parse(searchLink);

        Intent siteSearch = new Intent(Intent.ACTION_VIEW, webaddress);

        if(siteSearch.resolveActivity(getPackageManager()) != null)
        {
            startActivity(siteSearch);
        }
        else
        {
            Toast.makeText(getApplicationContext(), "This Item can not be searched",
                    Toast.LENGTH_LONG).show();
        }
    }

    private void tutorial()
    {
        ShowcaseConfig config = new ShowcaseConfig();
        config.setDelay(500); // half second between each showcase view

        final int COLOR_MASK = mContext.getResources().getColor(R.color.primary_dark_transparent);
        final int COLOR_DISMISS = mContext.getResources().getColor(R.color.colorPrimaryDark);

        MaterialShowcaseSequence sequence = new MaterialShowcaseSequence((Activity) mContext/*, SHOWCASE_ID*/);
        sequence.setConfig(config);
        MaterialShowcaseView.Builder sequenceBuilderName, sequenceBuilderGoToWebsite,
                sequenceBuilderBarcodeValue;


        sequenceBuilderName = new MaterialShowcaseView.Builder((Activity)mContext)
                .setTarget(findViewById(R.id.tv_item_name))
                .renderOverNavigationBar()
                .setTargetTouchable(false)
                .setDismissOnTouch(true)
                .setTitleText("Name")
                .setContentText("The name if the item will be displayed here, tap the name to edit it")
                .setDelay(100)
                .withRectangleShape()
                .setMaskColour(COLOR_MASK)
                .setDismissTextColor(COLOR_DISMISS);

        sequenceBuilderBarcodeValue = new MaterialShowcaseView.Builder((Activity)mContext)
                .setTarget(findViewById(R.id.tv_barcode_value))
                .renderOverNavigationBar()
                .setDismissOnTouch(true)
                .setTargetTouchable(true)
                .setTitleText("Barcode")
                .setContentText("The content of the barcode is diaplyed here")
                .setDelay(100)
                .withRectangleShape()
                .setMaskColour(COLOR_MASK)
                .setDismissTextColor(COLOR_DISMISS);

        sequenceBuilderGoToWebsite = new MaterialShowcaseView.Builder((Activity)mContext)
                .setTarget(findViewById(R.id.searchItemBtn))
                .renderOverNavigationBar()
                //.setDismissOnTargetTouch(true)
                .setDismissOnTouch(true)
                .setTargetTouchable(false)
                .setTitleText("Websites")
                .setContentText("These are the websites you can search item on")
                .setDelay(100)
                .withRectangleShape()
                .setMaskColour(COLOR_MASK)
                .setDismissTextColor(COLOR_DISMISS);

        sequence.addSequenceItem(sequenceBuilderName.build());
        sequence.addSequenceItem(sequenceBuilderBarcodeValue.build());
        sequence.addSequenceItem(sequenceBuilderGoToWebsite.build());


        sequence.start();

    }

    private void tutorialFirst()
    {
        ShowcaseConfig config = new ShowcaseConfig();
        config.setDelay(500); // half second between each showcase view

        final int COLOR_MASK = mContext.getResources().getColor(R.color.primary_dark_transparent);
        final int COLOR_DISMISS = mContext.getResources().getColor(R.color.colorPrimaryDark);

        MaterialShowcaseSequence sequence = new MaterialShowcaseSequence((Activity) mContext, SHOWCASE_ID);
        sequence.setConfig(config);
        MaterialShowcaseView.Builder sequenceBuilderName, sequenceBuilderGoToWebsite,
                sequenceBuilderBarcodeValue;


        sequenceBuilderName = new MaterialShowcaseView.Builder((Activity)mContext)
                .setTarget(findViewById(R.id.tv_item_name))
                .renderOverNavigationBar()
                .setTargetTouchable(false)
                .setDismissOnTouch(true)
                .setTitleText("Name")
                .setContentText("The name if the item will be displayed here, tap the name to edit it")
                .setDelay(100)
                .withRectangleShape()
                .setMaskColour(COLOR_MASK)
                .setDismissTextColor(COLOR_DISMISS);

        sequenceBuilderBarcodeValue = new MaterialShowcaseView.Builder((Activity)mContext)
                .setTarget(findViewById(R.id.tv_barcode_value))
                .renderOverNavigationBar()
                .setDismissOnTouch(true)
                .setTargetTouchable(true)
                .setTitleText("Barcode")
                .setContentText("The content of the barcode is diaplyed here")
                .setDelay(100)
                .withRectangleShape()
                .setMaskColour(COLOR_MASK)
                .setDismissTextColor(COLOR_DISMISS);

        sequenceBuilderGoToWebsite = new MaterialShowcaseView.Builder((Activity)mContext)
                .setTarget(findViewById(R.id.searchItemBtn))
                .renderOverNavigationBar()
                .setDismissOnTouch(true)
                .setTargetTouchable(false)
                .setTitleText("Websites")
                .setContentText("These are the websites you can search item on")
                .setDelay(100)
                .withRectangleShape()
                .setMaskColour(COLOR_MASK)
                .setDismissTextColor(COLOR_DISMISS);

        sequence.addSequenceItem(sequenceBuilderName.build());
        sequence.addSequenceItem(sequenceBuilderBarcodeValue.build());
        sequence.addSequenceItem(sequenceBuilderGoToWebsite.build());


        sequence.start();
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

        homeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent goToMain = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(goToMain);
            }
        });

        settingsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent settingsAc = new Intent(getApplicationContext(), SettingsActivity.class);
                startActivity(settingsAc);
            }
        });
    }

}