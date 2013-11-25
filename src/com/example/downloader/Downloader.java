package com.example.downloader;

import java.io.BufferedInputStream;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;

import android.os.Bundle;
import android.os.Handler;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

public class Downloader extends Activity {

	private Button 		button_download;
	private ProgressBar progressBar_download;
	private EditText 	editText_url;
	
	protected static ProgressDialog progressBar;
	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_downloader);

		button_download 		= (Button) 		findViewById(R.id.button_download);
		button_download.setOnClickListener(downloadListener);
		progressBar_download 	= (ProgressBar) findViewById(R.id.progressBar_download);
		editText_url 			= (EditText) 	findViewById(R.id.editText_url);
		editText_url.setText(getResources().getString(R.string.standard_url));
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.downloader, menu);
		return true;

	}
	
	OnClickListener downloadListener = new OnClickListener() {
		public void onClick(View v) {
			progressBar = new ProgressDialog(v.getContext());
			progressBar.setCancelable(true);
			progressBar.setMessage(getResources().getString(R.string.progressbar_message));
			progressBar.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
			progressBar.setProgress(0);
			progressBar.setMax(DownloadReceiver.FINISHED);
			progressBar.show();

			Intent intent = new Intent(Downloader.this, DownloadService.class);
			intent.putExtra(DownloadService.URL, editText_url.getText().toString() );
			intent.putExtra(DownloadService.RECEIVER, new DownloadReceiver(new Handler()));
			startService(intent);
		}
	};

	
	
}

