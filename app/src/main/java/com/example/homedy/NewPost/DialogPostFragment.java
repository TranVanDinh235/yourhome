package com.example.homedy.NewPost;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.NumberPicker;

import com.example.homedy.Address;
import com.example.homedy.R;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class DialogPostFragment extends DialogFragment {

    private static final String DATA = "data";
    private static final String BUTTON_ID = "id";

    @InjectView(R.id.numberpicker) NumberPicker _numberPicker;

    private String[] strings = Address.citys;
    private int id;

    public interface DialogPostFragmetListener{
        public abstract void setDataFromFragment(String data, int button_id);
    }

    DialogPostFragmetListener mListenter;

    public void setNumberPickerData(String[] data, NumberPicker numberPicker) {
        numberPicker.setMinValue(0);
        numberPicker.setMaxValue(data.length - 1);
        numberPicker.setDisplayedValues(data);
    }

    public static DialogPostFragment newInstance(String[] data, int id){
        DialogPostFragment fragment = new DialogPostFragment();
        Bundle args = new Bundle();
        args.putStringArray(DATA, data);
        args.putInt(BUTTON_ID, id);
        fragment.setArguments(args);
        return fragment;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = requireActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.fragment_dialog, null);
        ButterKnife.inject(this, view);
        if (getArguments() != null) {
            strings = getArguments().getStringArray(DATA);
            id = getArguments().getInt(BUTTON_ID);
        }
        setNumberPickerData(strings,_numberPicker);
        builder.setView(view)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        mListenter.setDataFromFragment(strings[_numberPicker.getValue()], id);
                    }
                })
                .setNegativeButton("Huỷ", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dismiss();
                    }
                });
        return builder.create();
    }

    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof DialogPostFragment.DialogPostFragmetListener) {
            mListenter = (DialogPostFragment.DialogPostFragmetListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement DialogPostFragmentListener");
        }
    }
}
