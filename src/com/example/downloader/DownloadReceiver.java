package com.example.downloader;

import android.os.Bundle;
import android.os.Handler;
import android.os.ResultReceiver;

class DownloadReceiver extends ResultReceiver{
    public DownloadReceiver(Handler handler) {
        super(handler);
    }

    @Override
    protected void onReceiveResult(int resultCode, Bundle resultData) {
        super.onReceiveResult(resultCode, resultData);
        if (resultCode == DownloadService.UPDATE_PROGRESS) {
            int progress = resultData.getInt("progress");
            
            Downloader.progressBar.setProgress(progress);
            if (progress == 100) {
               Downloader.progressBar.dismiss();
               
            }
        }
    }
}