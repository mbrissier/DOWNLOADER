package com.example.downloader;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Environment;
import android.os.ResultReceiver;
import android.util.Log;
import android.widget.Toast;

public class DownloadService extends IntentService {
        public static final int UPDATE_PROGRESS = 8044;
        public static final String URL = "url";
        public static final String RECEIVER = "receiver";
        public static final String DES = "/mnt/sdcard/test/test.iso";
        private static final int BUFFERSIZE = 1024;
        public static final String TOAST = "Download abgeschlossen";
        

        public DownloadService() {
                super("DownloadService");
        }


        
        @Override
        protected void onHandleIntent(Intent intent) {
                String urlToDownload = intent.getStringExtra(URL);
                ResultReceiver receiver = (ResultReceiver) intent
                                .getParcelableExtra(RECEIVER);

                try {
                        URL url = new URL(urlToDownload);
                        URLConnection connection = url.openConnection();
                        connection.connect();
                        // this will be useful so that you can show a typical 0-100%
                        // progress bar
                        int fileLength = connection.getContentLength();

                        // download the file
                        InputStream input = new BufferedInputStream(url.openStream());
                        // save file
                        OutputStream output = new FileOutputStream(DES);
                        
                        //get status of download and pass it to Downloader.progressBar via DownloadReceiver
                        byte data[] = new byte[BUFFERSIZE];
                        long total = 0;
                        int count;
                        while ((count = input.read(data)) != -1) {
                                total += count;
                                // publishing the progress....
                                Bundle resultData = new Bundle();
                                resultData.putInt(DownloadReceiver.PROGRESS, (int) (total
                                                * DownloadReceiver.FINISHED / fileLength));
                                receiver.send(UPDATE_PROGRESS, resultData);
                                output.write(data, 0, count);
                        }

                        output.flush();
                        output.close();
                        input.close();
                } catch (IOException e) {
                        Log.v("DownloadService", "Exception:"+e);
                } catch (RuntimeException e) {
                        Log.v("DownloadService", "Exception:"+e);
                }

                Bundle resultData = new Bundle();
                resultData.putInt(DownloadReceiver.PROGRESS, DownloadReceiver.FINISHED);
                makeToast();
                receiver.send(UPDATE_PROGRESS, resultData);
        }
        
        public void makeToast() {
                
		Toast.makeText(this, TOAST, Toast.LENGTH_LONG).show();
        }
        
        
}