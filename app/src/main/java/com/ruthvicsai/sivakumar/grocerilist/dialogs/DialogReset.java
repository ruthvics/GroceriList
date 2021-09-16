package com.ruthvicsai.sivakumar.grocerilist.dialogs;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.ruthvicsai.sivakumar.grocerilist.R;


public class DialogReset extends DialogFragment {

    private TextView mActionOk, mActionCancel, mMainText;
    private Context mContext;
    private OnInputListener mOnInputListener;

    public interface OnInputListener{
        void getContext();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog_reset_app, container,false);
        mActionCancel = view.findViewById(R.id.cancelTextView);
        mActionOk = view.findViewById(R.id.okTextView);
        mMainText = view.findViewById(R.id.textTv);

        mActionCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getDialog().dismiss();
            }
        });

        mActionOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                mOnInputListener.getContext();
                getDialog().dismiss();

            }
        });

        return view;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        try{
            mOnInputListener = (DialogReset.OnInputListener) getActivity();
            //mNotifyAdapter = (notifyAdapter) getActivity();
        }catch(ClassCastException e)
        {
            //Log.e("onAttach", e.getMessage());
        }
    }
}
