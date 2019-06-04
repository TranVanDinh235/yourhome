package com.example.homedy.NewPost;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;

import com.example.homedy.R;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class TypeRoomDialog extends DialogFragment {

    @InjectView(R.id.btn_dialog_canho) Button _canhoButton;
    @InjectView(R.id.btn_dialog_phongtro) Button _phongtroButton;
    @InjectView(R.id.btn_dialog_canhomini) Button _canhoMiniButton;
    @InjectView(R.id.btn_dialog_nhanguyencan) Button _nhaNguyenCanButton;
    @InjectView(R.id.btn_dialog_bietthu) Button _bietThuButton;
    @InjectView(R.id.btn_dialog_kitucxa) Button _kiTucXaButton;

    public interface DialogPost3Listener {
        public abstract void setDataFromFragment(String data);
    }

    DialogPost3Listener mListener;

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        super.onCreateDialog(savedInstanceState);
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater layoutInflater = requireActivity().getLayoutInflater();
        View view = layoutInflater.inflate(R.layout.fragment_typeroom, null);
        ButterKnife.inject(this, view);
        _canhoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.setDataFromFragment("Căn hộ");
                dismiss();
            }
        });

        _bietThuButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.setDataFromFragment("Biệt thự");
                dismiss();
            }
        });

        _canhoMiniButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.setDataFromFragment("Căn hộ Mini");
                dismiss();
            }
        });

        _nhaNguyenCanButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.setDataFromFragment("Nhà nguyên căn");
                dismiss();
            }
        });

        _phongtroButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.setDataFromFragment("Phòng trọ");
                dismiss();
            }
        });

        _kiTucXaButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.setDataFromFragment("Ki tuc xa");
                dismiss();
            }
        });

        builder.setView(view);
        return builder.create();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if(context instanceof TypeRoomDialog.DialogPost3Listener) {
            mListener = (TypeRoomDialog.DialogPost3Listener) context;
        } else throw new RuntimeException(context.toString() + "must implement DialogPost3Listener");
    }
}
