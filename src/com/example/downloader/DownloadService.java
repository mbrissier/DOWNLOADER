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
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.os.ResultReceiver;
import android.widget.Toast;

public class DownloadService extends IntentService {
    public 	static final int UPDATE_PROGRESS = 8344;
    public 	static final String URL
    	= "url";
    public 	static final String RECEIVER
		= "receiver";
    private static final int	BUFFERSIZE
		= 1024;
    private static final String STORAGENOTWRITABLE
		= "external storage is not accessable or not writable";
    
    
    public DownloadService() {
        super("DownloadService");
    }
    
    /* Checks if external storage is available for read and write */
    public boolean isExternalStorageWritable() {
        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state)) {
            return true;
        }
        return false;
    }
    
    @Override
    protected void onHandleIntent(Intent intent) {
        String	 		urlToDownload 	= intent.getStringExtra(URL);
        ResultReceiver	receiver 		= (ResultReceiver) intent.getParcelableExtra(RECEIVER);
        
        try {
            URL url = new URL(urlToDownload);
            URLConnection connection = url.openConnection();
            connection.connect();
            // this will be useful so that you can show a typical 0-100% progress bar
            int fileLength = connection.getContentLength();

            // download the file
            InputStream input = new BufferedInputStream(url.openStream());
            if(!isExternalStorageWritable())	throw new RuntimeException(STORAGENOTWRITABLE);
            OutputStream output = new FileOutputStream
            					 (new File(Environment.getExternalStoragePublicDirectory
            					 (Environment.DIRECTORY_PICTURES), url.toString()));

            byte data[] = new byte[BUFFERSIZE];
            long total = 0;
            int count;
            while ((count = input.read(data)) != -1) {
                total += count;
                // publishing the progress....
                Bundle resultData = new Bundle();
                resultData.putInt(DownloadReceiver.PROGRESS ,(int) (total * DownloadReceiver.FINISHED / fileLength));
                receiver.send(UPDATE_PROGRESS, resultData);
                output.write(data, 0, count);
            }

            output.flush();
            output.close();
            input.close();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (RuntimeException e) {
        	e.printStackTrace();
        }

        Bundle resultData = new Bundle();
        resultData.putInt(DownloadReceiver.PROGRESS, DownloadReceiver.FINISHED);
        Toast.makeText(this, getResources().getString(R.string.finished_toast), Toast.LENGTH_LONG).show();
        receiver.send(UPDATE_PROGRESS, resultData);
    }
}