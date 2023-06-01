package com.example.ham_app.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import com.example.ham_app.R;


public class AlertDialog {
    private final Context context;
    private Dialog dialog;


    public AlertDialog(Context context) {
        this.context = context;
    }

    public void show(String message){
        dialog = new Dialog(context);
        dialog.setContentView(R.layout.custom_alert_dialog);
        dialog.setCancelable(false);
        final Button btn_tryAgain = dialog.findViewById(R.id.btnAlert_tryAgain);
        final TextView tv_Message = dialog.findViewById(R.id.tvMessage);
        if (dialog.getWindow() != null)
        {
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        }
        tv_Message.setText(message);
        btn_tryAgain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        dialog.show();
    }
    public void dismissDialog(){
        dialog.dismiss();
    }
}
