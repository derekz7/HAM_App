package com.example.ham_app.dialog;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.Gravity;
import android.view.ViewGroup;
import android.widget.TextView;
import com.example.ham_app.R;

public class BottomDialogMessage {
    private Dialog dialog;
    private Context context;

    public BottomDialogMessage(Context context) {
        this.context = context;
    }

    public void show(String message) {
        dialog = new Dialog(context);
        dialog.setContentView(R.layout.bottom_dialog_message);
        dialog.setCancelable(true);
        if (dialog.getWindow() != null) {
            dialog.getWindow().setGravity(Gravity.BOTTOM);
            dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            dialog.getWindow().getAttributes().windowAnimations = R.style.BottomSheetAnimation;
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        }
        final TextView tvMessage = dialog.findViewById(R.id.tvDialogMessage);
        tvMessage.setText(message);
        dialog.show();
    }
}
