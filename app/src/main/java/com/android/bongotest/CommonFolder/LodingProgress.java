package com.android.bongotest.CommonFolder;

import android.app.Activity;
import android.app.Dialog;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;

import com.android.bongotest.R;
import com.github.ybq.android.spinkit.sprite.Sprite;
import com.github.ybq.android.spinkit.style.ThreeBounce;

public class LodingProgress {
    public static Dialog mProgressDlg;
    public static void showLoadingDialog(Activity activity){
        Sprite doubleBounce = new ThreeBounce();


        if (mProgressDlg != null && mProgressDlg.isShowing()) return;
        mProgressDlg = new Dialog(activity);
        mProgressDlg.getWindow().setBackgroundDrawable(new ColorDrawable(activity.getResources().getColor(R.color.transparent_color)));

        View contentView = LayoutInflater.from(activity).inflate(R.layout.ui_loading_dialog, null);

//        ProgressBar progressBar = contentView.findViewById(R.id.progress);
//        progressBar.setIndeterminateDrawable(doubleBounce);

        mProgressDlg.setContentView(contentView);
        mProgressDlg.setCancelable(true);
        mProgressDlg.setTitle(null);
        mProgressDlg.show();
    }
    public static void hideLoadingDialog(){
        if (mProgressDlg.isShowing()){
            mProgressDlg.dismiss();
        }

    }
}
