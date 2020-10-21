package com.ruthvicsai.sivakumar.grocerilist.activites;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.ruthvicsai.sivakumar.grocerilist.MainActivity;
import com.ruthvicsai.sivakumar.grocerilist.R;

public class TutorialActivity extends AppCompatActivity {

    Button homeTutBtn, listTutBtn, itemTutBtn, itemDetailTutBtn;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_tutorial);

        homeTutBtn = findViewById(R.id.HomeTutBtn);
        listTutBtn = findViewById(R.id.ListTutBtn);
        itemTutBtn = findViewById(R.id.ItemTutBtn);
        itemDetailTutBtn = findViewById(R.id.DetailTutBtn);

        homeTutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent mainActivity = new Intent(getApplicationContext(), MainActivity.class);
                mainActivity.putExtra("tutorialId", 3);
                startActivity(mainActivity);
            }
        });

        listTutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent listActivity = new Intent(getApplicationContext(), ViewListsActivity.class);
                listActivity.putExtra("tutorialId", 3);
                startActivity(listActivity);
            }
        });

        itemTutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent itemActivity = new Intent(getApplicationContext(), DisplayListActivity.class);
                itemActivity.putExtra("tutorialId", 3);
                startActivity(itemActivity);
            }
        });

        itemDetailTutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent itemDetialTutActivity = new Intent(getApplicationContext(), ItemSelectActivity.class);
                itemDetialTutActivity.putExtra("tutorialId", 3);
                startActivity(itemDetialTutActivity);
            }
        });
    }
}
