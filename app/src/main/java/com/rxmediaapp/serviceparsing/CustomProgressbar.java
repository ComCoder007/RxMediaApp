package com.rxmediaapp.serviceparsing;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.View;
import android.view.Window;
import android.widget.LinearLayout;

import com.github.ybq.android.spinkit.SpinKitView;
import com.rxmediaapp.R;


public class CustomProgressbar {

	public static Dialog dialog;

	public static void Progressbarshow(final Context context) {

				dialog = new Dialog(context);
				dialog.getWindow();
				dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
				dialog.setContentView(R.layout.loadingprogress);
				dialog.getWindow().setBackgroundDrawable(new ColorDrawable( Color.TRANSPARENT));
				dialog.getWindow().setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);

				LinearLayout giflayout = (LinearLayout)dialog.findViewById(R.id.giflayout);
				SpinKitView spin_kit = dialog.findViewById(R.id.spin_kit);
				spin_kit.setVisibility(View.VISIBLE);


				dialog.show();
			}

	public static void Progressbarcancel(Context context) {

		if (dialog != null) {
			dialog.dismiss();
		}
	}

	public static ProgressDialog GpsprogressDialog;

	public static void GPSProgressbarshow(Activity context) {
		GpsprogressDialog = new ProgressDialog(context);
		GpsprogressDialog.setMessage("Uploading....");
		GpsprogressDialog.setCancelable(true);
		GpsprogressDialog.setIndeterminate(true);
		GpsprogressDialog.show();
	}

	public static void GPSProgressbarcancel(Activity context) {
		if (GpsprogressDialog != null) {
			GpsprogressDialog.dismiss();
		}
	}


}
