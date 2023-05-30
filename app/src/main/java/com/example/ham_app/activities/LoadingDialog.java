package com.example.ham_app.activities;

import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.widget.TextView;

import com.example.ham_app.R;

public class LoadingDialog {
    private final Context context;
    private Dialog dialog;

    public LoadingDialog(Context context) {
        this.context = context;
    }

    public void show(){
        dialog = new Dialog(context);
        dialog.setContentView(R.layout.dialog_loading);
        dialog.setCancelable(false);
        if (dialog.getWindow() != null)
        {
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        }
        dialog.show();
    }
    public void dismissDialog(){
        dialog.dismiss();
    }
}