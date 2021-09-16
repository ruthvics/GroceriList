package com.ruthvicsai.sivakumar.grocerilist.dialogs;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;


import com.ruthvicsai.sivakumar.grocerilist.R;

import java.util.ArrayList;

public class DialogAddToList<override> extends DialogFragment{
    private EditText nameOfList;
    private TextView mActionOk, mActionCancel, displayBarcodeValueTV;
    private String mBarcodeValue;
    private LinearLayout optionsView;
    private ArrayList<String> mlistOfData;
    private RadioGroup radioGroupList;
    public OnInputListener mOnInputListener;
    public resumeCamera mResumeCamera;





    public interface OnInputListener{
        void sendInput(String input, String listInput);
    }

    public interface resumeCamera{
        void cancelPressed(boolean pressed);
    }



    public void DialogSetData(String barcodeValue, ArrayList<String> listOfData)
    {
        mBarcodeValue = barcodeValue;
        mlistOfData = listOfData;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog_add_item_to_list, container,false);
        mActionCancel = view.findViewById(R.id.cancelTextView);
        mActionOk = view.findViewById(R.id.oktextView);
        nameOfList = view.findViewById(R.id.listNameEditText);
        displayBarcodeValueTV = view.findViewById(R.id.displayBarcodeValue);

        optionsView = (LinearLayout) view.findViewById(R.id.scrollviewLayout);
        displayBarcodeValueTV = (TextView) view.findViewById(R.id.displayBarcodeValue);


        displayBarcodeValueTV.setText(mBarcodeValue);

        setRadioButtons();
        
        mActionCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mResumeCamera.cancelPressed(true);
                getDialog().dismiss();
            }
        });

        mActionOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String input = nameOfList.getText().toString();
                if(input.equals(""))
                {
                    Toast.makeText(getContext(),"Please enter a name",
                            Toast.LENGTH_SHORT).show();
                }
                else {
                    String listInput;
                    int checkId = radioGroupList.getCheckedRadioButtonId();

                    if (checkId != -1) {
                        RadioButton selectedRadioButton = (RadioButton) radioGroupList.findViewById(checkId);
                        listInput = selectedRadioButton.getText().toString();
                    } else {
                        listInput = "Favorites";
                    }


                    mOnInputListener.sendInput(input, listInput);
                    getDialog().dismiss();
                }
            }
        });
        return view;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        try{
            mOnInputListener = (DialogAddToList.OnInputListener) getActivity();
            mResumeCamera = (DialogAddToList.resumeCamera) getActivity();
        }catch(ClassCastException e)
        {
            //Log.e("onAttach", e.getMessage());
        }
    }


    @Override
    public void onDismiss(@NonNull DialogInterface dialog) {
        super.onDismiss(dialog);
        mResumeCamera.cancelPressed(true);

    }


    private void setRadioButtons()
    {
        //Typeface font = (Typeface) getResources().getFont(R.id.ubuntuFont);
       // Typeface font = Typeface.createFromAsset(getAssets(), "SF_Cartoonist_Hand_Bold.ttf");
        //Typeface typeface = ResourcesCompat.getFont( getContext(), R.font.ubuntu_light);


        radioGroupList = new RadioGroup(this.getContext());
        radioGroupList.setOrientation(RadioGroup.VERTICAL);
        //radioGroupList.set

        //Log.e("setRadioButtons",mlistOfData.size()+"");

        int listSize = mlistOfData.size();
        for(int i = 0; i < listSize; i++)
        {
            RadioButton btn = new RadioButton(this.getContext());
            String mOutput = setFirstUpper(mlistOfData.get(i));
            btn.setText(mOutput);
            radioGroupList.addView(btn);
        }
        optionsView.addView(radioGroupList);
    }

    private String setFirstUpper(String input)
    {
        String output = input.substring(0, 1).toUpperCase() + input.substring(1);
        return output;
    }

    private boolean checkIfNoneSelected()
    {
        return true;
    }




}
