package com.ruthvicsai.sivakumar.grocerilist;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.github.amlcurran.showcaseview.ShowcaseView;
import com.ruthvicsai.sivakumar.grocerilist.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.google.zxing.Result;
import com.oguzdev.circularfloatingactionmenu.library.FloatingActionMenu;
import com.oguzdev.circularfloatingactionmenu.library.SubActionButton;
import com.ruthvicsai.sivakumar.grocerilist.activites.DisplayListActivity;
import com.ruthvicsai.sivakumar.grocerilist.activites.SettingsActivity;
import com.ruthvicsai.sivakumar.grocerilist.activites.ViewListsActivity;
import com.ruthvicsai.sivakumar.grocerilist.dialogs.DialogAddNewList;
import com.ruthvicsai.sivakumar.grocerilist.dialogs.DialogAddToList;

import java.lang.reflect.Type;
import java.util.ArrayList;

import com.ruthvicsai.sivakumar.grocerilist.ScannerClass.ZXingScannerView;
import uk.co.deanwild.materialshowcaseview.MaterialShowcaseSequence;
import uk.co.deanwild.materialshowcaseview.MaterialShowcaseView;
import uk.co.deanwild.materialshowcaseview.ShowcaseConfig;

import static android.Manifest.permission.CAMERA;

public class MainActivity extends AppCompatActivity implements  DialogAddNewList.OnInputListener,
        me.dm7.barcodescanner.zxing.ZXingScannerView.ResultHandler,
        DialogAddToList.OnInputListener,
        DialogAddToList.resumeCamera{

    private static final int REQUEST_CAMERA = 1;
    private ZXingScannerView scannerView;
    private ArrayList<String> dataOfLists = new ArrayList<>();
    private final int currentApiVersion = Build.VERSION.SDK_INT;
    private BarcodeItem newBarcodeItem;
    SubActionButton viewListsBtn, viewFavoritesBtn, addNewListBtn, settingsBtn;
    private ArrayList<BarcodeItem> tempBarcodeList;
    private FloatingActionButton actionButton;
    private String rawResultString, newListString;
    private String tips[];
    FloatingActionMenu actionMenu;
    private ShowcaseView.Builder SCVB2;
    private ShowcaseView SCV2;
    private Activity mActivity = this;
    private Boolean firstTipDone = false;
    private static final String SHOWCASE_ID = "1";
    private int tutorialID;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        loadListsData();
        dPrintListData();

        //dataOfLists.remove("");

        //saveListsData();
        //loadListsData();

        Intent mainIntent = getIntent();
        tutorialID = mainIntent.getIntExtra("tutorialId", 0);



        dPrintListData();
        setTipsText();
        verifyPermisson();

        scannerView = (ZXingScannerView) findViewById(R.id.TScannerView);

        setUpFab();
        setUpTipsTextView(tips);

        if(tutorialID == 3)
        {
            tutorial(1);
        }

        //TutorialObject mTutorialObject = new TutorialObject(this);
        //mTutorialObject.homeTutorial();

        tutorial(0);

        FabSubButtonOnCLick();

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

        addNewListBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogAddNewList dialog = new DialogAddNewList();
                dialog.show(getSupportFragmentManager(), "my");
                //dataOfLists.add(newListString);

                saveListsData();
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

    private void tutorial(int Code)
    {
        scannerView.stopCamera();
        // actionMenu.removeView();
        //actionMenu.toggle(true);
        final int COLOR_MASK = getResources().getColor(R.color.primary_dark_transparent);
        final int COLOR_DISMISS = getResources().getColor(R.color.colorPrimaryDark);

        ShowcaseConfig config = new ShowcaseConfig();
        config.setDelay(500); // half second between each showcase view
        MaterialShowcaseSequence sequence;

        if(Code == 0)
        {
            sequence = new MaterialShowcaseSequence(this, SHOWCASE_ID);
            sequence.setConfig(config);
        }
        else {
            sequence = new MaterialShowcaseSequence(this/*, SHOWCASE_ID*/);
            sequence.setConfig(config);
        }

        MaterialShowcaseView.Builder sequenceBuilderFAB,
                sequenceBuilderTips,
                sequenceBuilderFavoriteList,
                sequenceBuilderViewAllLists,
                sequenceBuilderSettings,
                sequenceBuilderPlus;




        sequenceBuilderFAB = new MaterialShowcaseView.Builder(this)
                .setTarget(findViewById(R.id.faButtonMain))
                .renderOverNavigationBar()
                .setDismissOnTargetTouch(true)
                .setTargetTouchable(true)
                .setContentText("Tap this button to view options")//+
                // " - Tap the heart to view your favorite list\n"+
                //" - Tap the check to view all your lists\n"+
                //" - Tap the plus to add a new List")
                .setDelay(100)
                .setTitleText("Main Menu")
                //.setTitleTextColor(R.color.colorPrimaryDark)
                //.setContentTextColor(R.color.colorPrimary)
                .withCircleShape()
                .setMaskColour(COLOR_MASK)
                .setDismissTextColor(COLOR_DISMISS);

        sequenceBuilderTips = new MaterialShowcaseView.Builder(this)
                .setTarget(findViewById(R.id.tipsTextView))
                .renderOverNavigationBar()
                .setDismissOnTargetTouch(true)
                .setTargetTouchable(true)
                .setTitleText("Tips")
                .setContentText("  - Tips will be displayed bellow\n  - Tap tips bar to continue")
                .setDelay(100)
                .withRectangleShape()
                .setMaskColour(COLOR_MASK)
                .setDismissTextColor(COLOR_DISMISS);

        sequenceBuilderPlus = new MaterialShowcaseView.Builder(this)
                .setTarget(addNewListBtn)
                .renderOverNavigationBar()
                .setDismissOnTouch(true)
                .setTitleText("Add New List")
                .setContentText("Tap this button to add a new List")
                .setDelay(100)
                .withCircleShape()
                .setMaskColour(COLOR_MASK)
                .setDismissTextColor(COLOR_DISMISS);

        sequenceBuilderFavoriteList = new MaterialShowcaseView.Builder(this)
                .setTarget(viewFavoritesBtn)
                .renderOverNavigationBar()
                .setDismissOnTouch(true)
                .setTitleText("Favorite List")
                .setContentText("This will take you directly to favorite'd items")
                .setDelay(100)
                .withCircleShape()
                .setMaskColour(COLOR_MASK)
                .setDismissTextColor(COLOR_DISMISS);

        sequenceBuilderSettings = new MaterialShowcaseView.Builder(this)
                .setTarget(settingsBtn)
                .renderOverNavigationBar()
                .setDismissOnTouch(true)
                .setTitleText("Settings")
                .setContentText("Tap this to view more options")
                .setDelay(100)
                .withCircleShape()
                .setMaskColour(COLOR_MASK)
                .setDismissTextColor(COLOR_DISMISS);

        sequenceBuilderViewAllLists = new MaterialShowcaseView.Builder(this)
                .setTarget(viewListsBtn)
                .renderOverNavigationBar()
                .setDismissOnTouch(true)
                .setTitleText("View all your Lists")
                .setContentText("You can view all your lists here")
                .setDelay(100)
                .withCircleShape()
                .setMaskColour(COLOR_MASK)
                .setDismissTextColor(COLOR_DISMISS);


        //sequence.addSequenceItem(sequenceBuilderTips.build());
        sequence.addSequenceItem(sequenceBuilderFAB.build());
        sequence.addSequenceItem(sequenceBuilderFavoriteList.build());
        sequence.addSequenceItem(sequenceBuilderViewAllLists.build());
        sequence.addSequenceItem(sequenceBuilderPlus.build());
        sequence.addSequenceItem(sequenceBuilderSettings.build());
        sequence.start();


    }

    /*private void tutorialFirst()
    {
        scannerView.stopCamera();
        ShowcaseConfig config = new ShowcaseConfig();
        config.setDelay(500); // half second between each showcase view

        MaterialShowcaseSequence sequence = new MaterialShowcaseSequence(this, SHOWCASE_ID);
        sequence.setConfig(config);
        MaterialShowcaseView.Builder sequenceBuilderFAB,
                sequenceBuilderTips,
                sequenceBuilderFavoriteList,
                sequenceBuilderViewAllLists,
                sequenceBuilderSettings,
                sequenceBuilderPlus;

        final int COLOR_MASK = getResources().getColor(R.color.primary_dark_transparent);
        final int COLOR_DISMISS = getResources().getColor(R.color.colorPrimaryDark);

        sequenceBuilderFAB = new MaterialShowcaseView.Builder(this)
                .setTarget(findViewById(R.id.faButtonMain))
                .renderOverNavigationBar()
                .setDismissOnTargetTouch(true)
                .setTargetTouchable(true)
                .setContentText("Tap this button to view more options\n"+
                        " - Tap the heart to view your favorite list\n"+
                        " - Tap the check to view all your lists\n"+
                        " - Tap the plus to add a new List")
                .setDelay(100)
                .setTitleText("Main Menu")
                //.setTitleTextColor(R.color.colorPrimaryDark)
                //.setContentTextColor(R.color.colorPrimary)
                .withCircleShape()
                .setMaskColour(COLOR_MASK)
                .setDismissTextColor(COLOR_DISMISS);

        sequenceBuilderTips = new MaterialShowcaseView.Builder(this)
                .setTarget(findViewById(R.id.tipsTextView))
                .renderOverNavigationBar()
                .setDismissOnTouch(true)
                .setTitleText("Tips")
                .setContentText("Tips will be displayed here")
                .setDelay(100)
                .withRectangleShape()
                .setMaskColour(COLOR_MASK)
                .setDismissTextColor(COLOR_DISMISS);


        sequenceBuilderPlus = new MaterialShowcaseView.Builder(this)
                .setTarget(addNewListBtn)
                .renderOverNavigationBar()
                .setDismissOnTouch(true)
                .setTitleText("Add New List")
                .setContentText("Tap this button to add a new List")
                .setDelay(100)
                .withCircleShape()
                .setMaskColour(COLOR_MASK)
                .setDismissTextColor(COLOR_DISMISS);

        sequenceBuilderFavoriteList = new MaterialShowcaseView.Builder(this)
                .setTarget(viewFavoritesBtn)
                .renderOverNavigationBar()
                .setDismissOnTouch(true)
                .setTitleText("Favorite List")
                .setContentText("This will take you directly to favorite'd items")
                .setDelay(100)
                .withCircleShape()
                .setMaskColour(COLOR_MASK)
                .setDismissTextColor(COLOR_DISMISS);

        sequenceBuilderSettings = new MaterialShowcaseView.Builder(this)
                .setTarget(settingsBtn)
                .renderOverNavigationBar()
                .setDismissOnTouch(true)
                .setTitleText("Settings")
                .setContentText("Tap this to view more options")
                .setDelay(100)
                .withCircleShape()
                .setMaskColour(COLOR_MASK)
                .setDismissTextColor(COLOR_DISMISS);

        sequenceBuilderViewAllLists = new MaterialShowcaseView.Builder(this)
                .setTarget(viewListsBtn)
                .renderOverNavigationBar()
                .setDismissOnTouch(true)
                .setTitleText("View all your Lists")
                .setContentText("You can view all your lists here")
                .setDelay(100)
                .withCircleShape()
                .setMaskColour(COLOR_MASK)
                .setDismissTextColor(COLOR_DISMISS);




        //sequence.addSequenceItem(sequenceBuilderTips.build());
        sequence.addSequenceItem(sequenceBuilderFAB.build());
        sequence.addSequenceItem(sequenceBuilderFavoriteList.build());
        sequence.addSequenceItem(sequenceBuilderViewAllLists.build());
        sequence.addSequenceItem(sequenceBuilderPlus.build());
        sequence.addSequenceItem(sequenceBuilderSettings.build());
        sequence.start();
    }*/


    private void dPrintListData()
    {
        for( int i = 0; i < dataOfLists.size(); i++)
        {
            System.out.println("Position: " + i + "Data: " + dataOfLists.get(i));
        }

    }

    private void setUpFab()
    {
        Drawable addNewListD = getResources().getDrawable( R.drawable.plus_with_border );
        Drawable viewListsIconD = getResources().getDrawable( R.drawable.view_all_lists_icon );
        Drawable favoriteIconD = getResources().getDrawable(R.drawable.favorite_list_icon42);
        Drawable settingsIconD = getResources().getDrawable(R.drawable.settings_icon);

        ImageView icon = new ImageView(this);
        //icon.setImageResource(R.drawable.ic_foreground_192pxxxhdpi);
        icon.setImageResource(R.drawable.grocerilisticon13);



        actionButton = new FloatingActionButton.Builder(this)
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

        viewListsBtn = itemBuilder.setContentView(viewListsIcon).build();
        viewFavoritesBtn = itemBuilder.setContentView(favoriteIcon).build();
        addNewListBtn = itemBuilder.setContentView(addNewListIcon).build();
        settingsBtn = itemBuilder.setContentView(settingsIcon).build();

        actionButton.setId(R.id.faButtonMain);
        viewListsBtn.setId(R.id.faButtonAllList);
        viewFavoritesBtn.setId(R.id.faButtonFavoriteList);
        addNewListBtn.setId(R.id.faButtonPlus);
        settingsBtn.setId(R.id.faSettingsButton);

        actionMenu = new FloatingActionMenu.Builder(this)
                .addSubActionView(settingsBtn)
                .addSubActionView(addNewListBtn)
                .addSubActionView(viewListsBtn)
                .addSubActionView(viewFavoritesBtn)
                .attachTo(actionButton)
                .build();


        //actionButton.detach();
        //actionMenu.removeView();
        //actionMenu.open(true);

    }

    private void loadData(){
        SharedPreferences sharedPreferences = getSharedPreferences("shared", MODE_PRIVATE );
        Gson gson = new Gson();
        String json = sharedPreferences.getString("listOfData",null);
        Type type = new TypeToken<ArrayList<String>>() {}.getType();
        dataOfLists = gson.fromJson(json, type);

        if(dataOfLists == null)
        {
            dataOfLists = new ArrayList<>();
        }
    }

    private void loadBarcodeListData(String s){
        SharedPreferences sharedPreferences = getSharedPreferences("shared", MODE_PRIVATE );
        Gson gson = new Gson();
        String json = sharedPreferences.getString(s,null);
        Type type = new TypeToken<ArrayList<BarcodeItem>>() {}.getType();
        tempBarcodeList = gson.fromJson(json, type);

        if(tempBarcodeList == null)
        {
            tempBarcodeList = new ArrayList<>();
        }
    }

    private void saveBarcodeListData(String s) {
        SharedPreferences sharedPreferences = getSharedPreferences("shared", MODE_PRIVATE );
        SharedPreferences.Editor editor = sharedPreferences.edit();
        Gson gson = new Gson();
        String json = gson.toJson(tempBarcodeList);
        editor.putString(s, json);
        editor.apply();
    }

    private boolean checkPermission()
    {
        return (ContextCompat.checkSelfPermission(getApplicationContext(), CAMERA) == PackageManager.PERMISSION_GRANTED);
    }

    private void requestPermission()
    {
        ActivityCompat.requestPermissions(this, new String[]{CAMERA}, REQUEST_CAMERA);
    }

    private void verifyPermisson()
    {
        if(currentApiVersion >=  Build.VERSION_CODES.M)
        {
            if(checkPermission())
            {
                //Toast.makeText(getApplicationContext(), "Permission already granted!", Toast.LENGTH_LONG).show();
            }
            else
            {
                requestPermission();
            }
        }
    }

    private void loadListsData(){
        SharedPreferences sharedPreferences = getSharedPreferences("shared", MODE_PRIVATE );
        Gson gson = new Gson();
        String json = sharedPreferences.getString("listOfData",null);
        Type type = new TypeToken<ArrayList<String>>() {}.getType();
        dataOfLists = gson.fromJson(json, type);

        if(dataOfLists == null)
        {
            dataOfLists = new ArrayList<>();
        }
    }

    private void setUpTipsTextView(String[] tipsArray)
    {
        TextView tipsTextView = (TextView) findViewById(R.id.tipsTextView);
        ViewFadeAnimator animator = new ViewFadeAnimator(tipsTextView, tipsArray);
        animator.startAnimation();
    }

    private void setTipsText()
    {
        tips = new String[]{getResources().getString(R.string.tip1String),
                getResources().getString(R.string.tip2String),
                getResources().getString(R.string.tip3String),
                "Have a great day!"};
    }

    private void saveListsData() {
        SharedPreferences sharedPreferences = getSharedPreferences("shared", MODE_PRIVATE );
        SharedPreferences.Editor editor = sharedPreferences.edit();
        Gson gson = new Gson();
        String json = gson.toJson(dataOfLists);
        editor.putString("listOfData", json);
        editor.apply();
    }

    //--------------------------------------------------------------------------------------------------

    @Override
    public void sendInput(String input, String listInput) {
        loadBarcodeListData(listInput);
        newBarcodeItem = new BarcodeItem(input, rawResultString);
        tempBarcodeList.add(newBarcodeItem);
        saveBarcodeListData(listInput);
        scannerView.resumeCameraPreview(MainActivity.this);
    }

    @Override
    public void handleResult(Result rawResult) {
        final String myResult = rawResult.getText();
        rawResultString = rawResult.getText();
        Log.d("QRCodeScanner", rawResult.getText());
        Log.d("QRCodeScanner", rawResult.getBarcodeFormat().toString());
        loadData();
        DialogAddToList dialog = new DialogAddToList();
        dialog.DialogSetData(myResult, dataOfLists);
        dialog.show(getSupportFragmentManager(), "my");
    }

    @Override
    public void onResume() {
        super.onResume();

        int currentapiVersion = android.os.Build.VERSION.SDK_INT;
        if (currentapiVersion >= android.os.Build.VERSION_CODES.M) {
            if (checkPermission()) {
                if(scannerView == null) {
                    scannerView = new ZXingScannerView(this);
                    setContentView(scannerView);
                }
                scannerView.setResultHandler(this);
                scannerView.startCamera();
            } else {
                requestPermission();
            }
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        scannerView.stopCamera();
    }

    @Override
    public void cancelPressed(boolean pressed) {
        onResume();
    }

    @Override
    public void sendInput(String input) {
        System.out.println("sendINput " + input);
        if(input == null)
        {
            newListString = "null-value=input";
        }
        else if(input == "")
        {
            newListString = "no-name";
        }
        else {
            newListString = input;
        }
        dataOfLists.add(newListString);
        saveListsData();
    }
}