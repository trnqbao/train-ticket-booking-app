package com.java.trainticketbookingapp.Animation;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Handler;

public class LoadingActivity {

    private ProgressDialog loadingDialog;

    public LoadingActivity(ProgressDialog loadingDialog) {
        this.loadingDialog = loadingDialog;
    }

    public ProgressDialog getLoadingDialog() {
        return loadingDialog;
    }

    public void setLoadingDialog(ProgressDialog loadingDialog) {
        this.loadingDialog = loadingDialog;
    }

    public void showLoadingDialog(Context context, String string) {
        if (loadingDialog == null) {
            loadingDialog = new ProgressDialog(context);
            loadingDialog.setMessage(string);
            loadingDialog.setCanceledOnTouchOutside(false);
            loadingDialog.setCancelable(false);
        }
        loadingDialog.show();

        new Handler().postDelayed(() -> {
            if (loadingDialog != null && loadingDialog.isShowing()) {
                loadingDialog.dismiss();
            }
        }, 2000);
    }

    public void dismissLoadingDialog() {
        if (loadingDialog != null && loadingDialog.isShowing()) {
            loadingDialog.dismiss();
        }
    }
}
