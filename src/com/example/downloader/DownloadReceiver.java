package com.example.downloader;

import android.os.Bundle;
import android.os.Handler;
import android.os.ResultReceiver;
import android.util.Log;

class DownloadReceiver extends ResultReceiver {
	public static final String PROGRESS = "progress";
	public static final int FINISHED = 100;

	public DownloadReceiver(Handler handler) {
		super(handler);
	}

	@Override
	protected void onReceiveResult(int resultCode, Bundle resultData) {
		super.onReceiveResult(resultCode, resultData);
		if (resultCode == DownloadService.UPDATE_PROGRESS) {
			int progress = resultData.getInt(PROGRESS);
			Log.v("DownloadReceiver", "Status:" + progress);

			Downloader.progressBar.setProgress(progress);
			if (progress == FINISHED) {
				Downloader.progressBar.dismiss();
				
			}
		}
	}
}