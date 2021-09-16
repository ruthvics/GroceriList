package com.ruthvicsai.sivakumar.grocerilist.adapters;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.daimajia.swipe.SimpleSwipeListener;
import com.daimajia.swipe.SwipeLayout;
import com.daimajia.swipe.adapters.RecyclerSwipeAdapter;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.ruthvicsai.sivakumar.grocerilist.BarcodeItem;
import com.ruthvicsai.sivakumar.grocerilist.R;
import com.ruthvicsai.sivakumar.grocerilist.activites.ItemSelectActivity;

import java.lang.reflect.Type;
import java.util.ArrayList;

import uk.co.deanwild.materialshowcaseview.MaterialShowcaseSequence;
import uk.co.deanwild.materialshowcaseview.MaterialShowcaseView;
import uk.co.deanwild.materialshowcaseview.ShowcaseConfig;

public class RecyclerViewAdapter extends RecyclerSwipeAdapter<RecyclerViewAdapter.SimpleViewHolder> {
    private Context mContext;
    private ArrayList<BarcodeItem> mDataset;
    private ArrayList<BarcodeItem> favoriteList;
    private String nameOfList;
    private static final String SHOWCASE_ID = "3";
    private View favView;
    private int tutorialID;


    public RecyclerViewAdapter(Context context, ArrayList<BarcodeItem> DataSet, String nOL, int tutID) {
        this.mContext = context;
        this.mDataset = DataSet;
        this.nameOfList = nOL;
        tutorialID = tutID;
    }

    public static class SimpleViewHolder extends RecyclerView.ViewHolder{
        SwipeLayout swipeLayout;
        TextView textViewPos;
        TextView textViewData;
        Button buttonDelete;
        Button favoriteBtn;

        public SimpleViewHolder(View itemView) {
            super(itemView);
            swipeLayout = (SwipeLayout) itemView.findViewById(R.id.swipe);
            textViewPos = (TextView) itemView.findViewById(R.id.position);
            textViewData = (TextView) itemView.findViewById(R.id.text_data);
            buttonDelete = (Button) itemView.findViewById(R.id.delete);
            favoriteBtn = (Button) itemView.findViewById(R.id.favorite);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Log.d(getClass().getSimpleName(), "onItemSelected: " + textViewData.getText().toString());
                    Toast.makeText(view.getContext(), "onItemSelected: " + textViewData.getText().toString(), Toast.LENGTH_SHORT).show();
                }
            });
        }
    }


    @Override
    public SimpleViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recyclerview_item, parent, false);
        favView = view;
        return new SimpleViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final SimpleViewHolder viewHolder, final int position) {
        BarcodeItem item = mDataset.get(position);
        viewHolder.swipeLayout.setShowMode(SwipeLayout.ShowMode.LayDown);
        viewHolder.swipeLayout.addSwipeListener(new SimpleSwipeListener() {
            @Override
            public void onOpen(SwipeLayout layout) {
                YoYo.with(Techniques.Tada).duration(500).delay(100).playOn(layout.findViewById(R.id.trash));
            }
        });
        viewHolder.swipeLayout.setOnDoubleClickListener(new SwipeLayout.DoubleClickListener() {
            @Override
            public void onDoubleClick(SwipeLayout layout, boolean surface) {
                Toast.makeText(mContext, "DoubleClick", Toast.LENGTH_SHORT).show();
            }
        });
        viewHolder.buttonDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mItemManger.removeShownLayouts(viewHolder.swipeLayout);
                mDataset.remove(position);
                notifyItemRemoved(position);
                notifyItemRangeChanged(position, mDataset.size());
                mItemManger.closeAllItems();
                Toast.makeText(view.getContext(), "Deleted " + viewHolder.textViewData.getText().toString() + "!", Toast.LENGTH_SHORT).show();
                saveData();
            }
        });
        viewHolder.favoriteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loadFavData();
                if(checkFav(mDataset.get(position)))
                {
                    Toast.makeText(view.getContext(), mDataset.get(position).getBarcodeName() + " is already favorited", Toast.LENGTH_SHORT).show();
                }
                else {
                    favoriteList.add(mDataset.get(position));
                    Log.e("favoriteBtn", position + ":-- " + mDataset.get(position).getBarcodeName());
                }

                saveFavData();
                saveData();
            }
        });
        viewHolder.swipeLayout.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                Log.d(getClass().getSimpleName(), "onItemSelected: ");
                Intent intent = new Intent(mContext, ItemSelectActivity.class);
                intent.putExtra("BarcodeItem", mDataset.get(position));
                mContext.startActivity(intent);
                return false;
            }
        });
        viewHolder.textViewPos.setText((position + 1) + ".");
        viewHolder.textViewData.setText(item.getBarcodeName());


        mItemManger.bindView(viewHolder.itemView, position);

        if(tutorialID == 3) {
            tutorial();
        }

        tutorialFirst();
    }

    @Override
    public int getItemCount() {
         return mDataset.size();
    }

    @Override
    public int getSwipeLayoutResourceId(int position) {
        return R.id.swipe;
    }

    private void saveData() {
        SharedPreferences sharedPreferences = mContext.getSharedPreferences("shared", mContext.MODE_PRIVATE);//getSharedPreferences("shared", MODE_PRIVATE );
        SharedPreferences.Editor editor = sharedPreferences.edit();
        Gson gson = new Gson();
        String json = gson.toJson(mDataset);
        editor.putString(nameOfList, json);
        editor.apply();
    }

    private void saveFavData() {
        SharedPreferences sharedPreferences = mContext.getSharedPreferences("shared", mContext.MODE_PRIVATE);//getSharedPreferences("shared", MODE_PRIVATE );
        SharedPreferences.Editor editor = sharedPreferences.edit();
        Gson gson = new Gson();
        String json = gson.toJson(favoriteList);
        editor.putString("Favorites", json);
        editor.apply();
    }

    private void loadFavData(){
        SharedPreferences sharedPreferences = mContext.getSharedPreferences("shared", mContext.MODE_PRIVATE );
        Gson gson = new Gson();
        String json = sharedPreferences.getString("Favorites",null);
        Type type = new TypeToken<ArrayList<BarcodeItem>>() {}.getType();
        favoriteList = gson.fromJson(json, type);

        if(favoriteList == null)
        {
            favoriteList = new ArrayList<>();
        }
    }

    private boolean checkFav(BarcodeItem check)
    {
        boolean idSame;
        int size = favoriteList.size();
        for(int i = 0; i < size;i++)
        {
            if(favoriteList.get(i).getBarcodeValue().equals(check.getBarcodeValue()))
            {
                return true;
            }
        }
        return false;
    }

    private void tutorial()
    {
        ShowcaseConfig config = new ShowcaseConfig();
        config.setDelay(500); // half second between each showcase view

        final int COLOR_MASK = mContext.getResources().getColor(R.color.primary_dark_transparent);
        final int COLOR_DISMISS = mContext.getResources().getColor(R.color.colorPrimaryDark);

        MaterialShowcaseSequence sequence = new MaterialShowcaseSequence((Activity) mContext/*, SHOWCASE_ID*/);
        sequence.setConfig(config);
        MaterialShowcaseView.Builder sequenceBuilderLeftButtons,
                sequenceBuilderLongPressItem;


        sequenceBuilderLeftButtons = new MaterialShowcaseView.Builder((Activity)mContext)
                .setTarget((View)favView)
                .renderOverNavigationBar()
                .setDismissOnTouch(true)
                .setTitleText("Items")
                .setContentText("Slide item to left to view more options")
                .setDelay(100)
                .withRectangleShape()
                .setMaskColour(COLOR_MASK)
                .setDismissTextColor(COLOR_DISMISS);

        sequenceBuilderLongPressItem = new MaterialShowcaseView.Builder((Activity)mContext)
                .setTarget((View)favView)
                .renderOverNavigationBar()
                .setDismissOnTouch(true)
                .setTitleText("Items")
                .setContentText("Long Press to see item details")
                .setDelay(100)
                .withRectangleShape()
                .setMaskColour(COLOR_MASK)
                .setDismissTextColor(COLOR_DISMISS);

        sequence.addSequenceItem(sequenceBuilderLeftButtons.build());
        sequence.addSequenceItem(sequenceBuilderLongPressItem.build());

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
        MaterialShowcaseView.Builder sequenceBuilderLeftButtons,
                sequenceBuilderLongPressItem;


        sequenceBuilderLeftButtons = new MaterialShowcaseView.Builder((Activity)mContext)
                .setTarget((View)favView)
                .renderOverNavigationBar()
                .setDismissOnTouch(true)
                .setTitleText("Items")
                .setContentText("Slide item to left to view more options")
                .setDelay(100)
                .withRectangleShape()
                .setMaskColour(COLOR_MASK)
                .setDismissTextColor(COLOR_DISMISS);

        sequenceBuilderLongPressItem = new MaterialShowcaseView.Builder((Activity)mContext)
                .setTarget((View)favView)
                .renderOverNavigationBar()
                .setDismissOnTouch(true)
                .setTitleText("Items")
                .setContentText("Long Press to see item details")
                .setDelay(100)
                .withRectangleShape()
                .setMaskColour(COLOR_MASK)
                .setDismissTextColor(COLOR_DISMISS);

        sequence.addSequenceItem(sequenceBuilderLeftButtons.build());
        sequence.addSequenceItem(sequenceBuilderLongPressItem.build());

        sequence.start();
    }
}
