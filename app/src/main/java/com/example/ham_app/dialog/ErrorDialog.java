package com.example.ham_app.dialog;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;

import com.example.ham_app.R;

public class ErrorDialog {
    private final Context context;
    private Dialog dialog;

    public ErrorDialog(Context context) {
        this.context = context;
    }

    public void show(String title, String message){
        dialog = new Dialog(context);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
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
