package com.ruthvicsai.sivakumar.grocerilist.adapters;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.daimajia.swipe.adapters.BaseSwipeAdapter;
import com.google.gson.Gson;
import com.ruthvicsai.sivakumar.grocerilist.R;

import java.util.ArrayList;

import uk.co.deanwild.materialshowcaseview.MaterialShowcaseSequence;
import uk.co.deanwild.materialshowcaseview.MaterialShowcaseView;
import uk.co.deanwild.materialshowcaseview.ShowcaseConfig;

public class GridViewAdapter extends BaseSwipeAdapter {
    private ArrayList<String> mListOfData = new ArrayList<>();
    private Context mContext;
    private View favView;
    private int tutorialID;
    private boolean tutOnce = false;
    private static final String SHOWCASE_ID = "2";




    public GridViewAdapter(Context mContext, ArrayList<String> stringArrayList, int stutorialID/*, RecyclerViewClickListener itemListenerM*/) {
        this.mContext = mContext;
        mListOfData = stringArrayList;
        tutorialID = stutorialID;
    }


    @Override
    public int getSwipeLayoutResourceId(int position) {
        return R.id.swipe;
    }

    @Override
    public View generateView(final int position, ViewGroup parent) {
        View v = LayoutInflater.from(mContext).inflate(R.layout.grid_item, null);
        final GridView Gview = (GridView) v.findViewById(R.id.listGridView);
        LinearLayout deleteBtnGrid = (LinearLayout) v.findViewById(R.id.deleteLL);

        if(mListOfData.get(position).equals("Favorites"))
        {
            favView = v;
        }

        if(tutorialID == 3 && tutOnce == false) {
            tutorial();
            tutOnce = true;
        }

        tutorialFirst();


        deleteBtnGrid.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!(mListOfData.get(position).equals("Favorites") ))
                {
                    dPrintListData();
                    mListOfData.remove(position);
                    notifyDataSetChanged();
                    mItemManger.closeAllItems();
                }
                else
                {
                    notifyDataSetChanged();
                    dPrintListData();
                    Toast.makeText(mContext, "Can not delete Favorite",
                            Toast.LENGTH_LONG).show();
                }
                saveData();

            }
        });
        saveData();
        return v;
    }

    @Override
    public void fillValues(int position, View convertView) {
        TextView t = (TextView)convertView.findViewById(R.id.position);
        t.setText((position + 1 )+".");

        TextView name = (TextView)convertView.findViewById(R.id.nameOfListTextView);
        String output = setFirstUpper(mListOfData.get(position));
        name.setText(output);

    }

    @Override
    public int getCount() {
        return mListOfData.size();
    }

    @Override
    public Object getItem(int position) {
        return mListOfData.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    private void saveData() {
        SharedPreferences sharedPreferences = mContext.getSharedPreferences("shared", mContext.MODE_PRIVATE );
        SharedPreferences.Editor editor = sharedPreferences.edit();
        Gson gson = new Gson();
        String json = gson.toJson(mListOfData);
        editor.putString("listOfData", json);
        editor.apply();
    }

    private String setFirstUpper(String input)
    {
        if(Character.isDigit(input.charAt(0)))
        {
            return input;
        }
        else {
            String output = input.substring(0, 1).toUpperCase() + input.substring(1);
            return output;
        }
    }

    private void dPrintListData()
    {
        for( int i = 0; i < mListOfData.size(); i++)
        {
            System.out.println("Position: " + i + "Data: " + mListOfData.get(i));
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
        MaterialShowcaseView.Builder sequenceBuilderLeftTrash,
                                     sequenceBuilderGoToList;



        sequenceBuilderLeftTrash = new MaterialShowcaseView.Builder((Activity)mContext)
            .setTarget(favView)
            .renderOverNavigationBar()
            .setDismissOnTouch(true)
            .setTitleText("Lists")
            .setContentText("Slide to left for delete option")
            .setDelay(100)
            .withRectangleShape()
            .setMaskColour(COLOR_MASK)
            .setDismissTextColor(COLOR_DISMISS);

        sequenceBuilderGoToList = new MaterialShowcaseView.Builder((Activity)mContext)
                .setTarget(favView)
                .renderOverNavigationBar()
                .setDismissOnTouch(true)
                .setTitleText("Lists")
                .setContentText("Long Press to View Contents")
                .setDelay(100)
                .withRectangleShape()
                .setMaskColour(COLOR_MASK)
                .setDismissTextColor(COLOR_DISMISS);

        sequence.addSequenceItem(sequenceBuilderLeftTrash.build());
        sequence.addSequenceItem(sequenceBuilderGoToList.build());

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
        MaterialShowcaseView.Builder sequenceBuilderLeftTrash,
                sequenceBuilderGoToList;


        sequenceBuilderLeftTrash = new MaterialShowcaseView.Builder((Activity)mContext)
                .setTarget(favView)
                .renderOverNavigationBar()
                .setDismissOnTouch(true)
                .setTitleText("Lists")
                .setContentText("Slide to left for delete option")
                .setDelay(100)
                .withRectangleShape()
                .setMaskColour(COLOR_MASK)
                .setDismissTextColor(COLOR_DISMISS);

        sequenceBuilderGoToList = new MaterialShowcaseView.Builder((Activity)mContext)
                .setTarget(favView)
                .renderOverNavigationBar()
                .setDismissOnTouch(true)
                .setTitleText("Lists")
                .setContentText("Long Press to View Contents")
                .setDelay(100)
                .withRectangleShape()
                .setMaskColour(COLOR_MASK)
                .setDismissTextColor(COLOR_DISMISS);

        sequence.addSequenceItem(sequenceBuilderLeftTrash.build());
        sequence.addSequenceItem(sequenceBuilderGoToList.build());

        sequence.start();
    }


}
