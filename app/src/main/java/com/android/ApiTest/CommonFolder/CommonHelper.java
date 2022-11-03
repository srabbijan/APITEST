package com.android.ApiTest.CommonFolder;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.ApiTest.R;


public class CommonHelper {

    private TextView textView;
    private ImageView imageView;
    private final Activity activity;
    private String typeInitial = "inc";
    private String typeTest = "uviom";
    private int TIME;
    static boolean isDialogOpen = false;

    public CommonHelper(Activity activity) {
        this.activity = activity;
    }



    public static Boolean isConnected(Activity activity) {
        boolean check = false;
        try {
            ConnectivityManager ConnectionManager = (ConnectivityManager) activity.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo networkInfo = ConnectionManager.getActiveNetworkInfo();
            if (networkInfo != null && networkInfo.isConnected()) {
                check = true;
            } else {
                try {
                    if (!isDialogOpen) {
                        @SuppressLint("InflateParams")
                        View sheetView = activity.getLayoutInflater().inflate(R.layout.dialog_no_internet, null);
                        ViewGroup parentViewGroup = (ViewGroup) sheetView.getParent();
                        if (parentViewGroup != null) {
                            parentViewGroup.removeAllViews();
                        }

                        final Dialog mBottomSheetDialog = new Dialog(activity);
                        mBottomSheetDialog.setContentView(sheetView);
                        mBottomSheetDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                        mBottomSheetDialog.show();
                        isDialogOpen = true;
                        Button btnRetry = sheetView.findViewById(R.id.btnRetry);
                        mBottomSheetDialog.setCancelable(false);

                        btnRetry.setOnClickListener(view -> {
                            if (isConnected(activity)) {
                                isDialogOpen = false;
                                mBottomSheetDialog.dismiss();
                                isConnected(activity);
                            }
                        });
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return check;
    }

}
