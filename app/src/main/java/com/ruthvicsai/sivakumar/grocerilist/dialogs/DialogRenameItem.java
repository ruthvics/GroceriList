package com.ruthvicsai.sivakumar.grocerilist.dialogs;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.ruthvicsai.sivakumar.grocerilist.R;
import com.ruthvicsai.sivakumar.grocerilist.activites.ViewListsActivity;


public class DialogRenameItem extends DialogFragment {

    private EditText nameOfList;
    private TextView mActionOk, mActionCancel;
    private String name;

    public interface OnInputListener{
        void sendInput(String input);
    }
    //public interface notifyAdapter{
    //    void updateAdapter();
   // }

    public OnInputListener mOnInputListener;
    //public notifyAdapter mNotifyAdapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog_rename_item, container,false);
        mActionCancel = view.findViewById(R.id.cancelTextView);
        mActionOk = view.findViewById(R.id.oktextView);
        nameOfList = view.findViewById(R.id.listNameEditText);



        mActionCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
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
                    mOnInputListener.sendInput(input);
                    Intent viewGridList = new Intent(getContext(), ViewListsActivity.class);
                    startActivity(viewGridList);
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
            mOnInputListener = (OnInputListener) getActivity();
            //mNotifyAdapter = (notifyAdapter) getActivity();
        }catch(ClassCastException e)
        {
            Log.e("onAttach", e.getMessage());
        }
    }

}
