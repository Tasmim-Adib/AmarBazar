package com.example.amarmarket;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;

public class PopUP extends DialogFragment {

    int position = 0;
    public interface SingleChoiceListener{
        void onPositiveButtonClick(String[] list, int position);
        void onNegativeButtonClick();

    }

    SingleChoiceListener singleChoiceListener;
    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        try {
            singleChoiceListener = (SingleChoiceListener)context;
        }
        catch (Exception e){
            throw new ClassCastException(getActivity().toString() + "Singlecast listener must implement");
        }
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        final String[] choiceList = getActivity().getResources().getStringArray(R.array.choice);

        builder.setTitle("Account Type")
                .setSingleChoiceItems(choiceList, position, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        position = which;
                    }
                }).setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                singleChoiceListener.onPositiveButtonClick(choiceList,position);

            }
        }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                singleChoiceListener.onNegativeButtonClick();
            }
        });

        return builder.create();
    }
}
