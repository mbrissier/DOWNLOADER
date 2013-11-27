package com.example.downloader;

import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

public class Downloader extends Activity {

	private 			Button 		button_download;
	private 			EditText 	editText_url;

	protected static 	ProgressBar progressBar;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_downloader);

		button_download = (Button) findViewById(R.id.button_download);
		button_download.setOnClickListener(downloadListener);
		
		editText_url = (EditText) findViewById(R.id.editText_url);
		editText_url.setText(getResources().getString(R.string.standard_url));
		
		progressBar = (ProgressBar) findViewById(R.id.progressBar_download);
	}

	OnClickListener downloadListener = new OnClickListener() {
		public void onClick(View v) {

			if (isOnline()) {

				progressBar.setProgress(0);
				progressBar.setMax(DownloadReceiver.FINISHED);

				Intent intent = new Intent(Downloader.this,
						DownloadService.class);
				intent.putExtra(DownloadService.URL, editText_url.getText()
						.toString());
				intent.putExtra(DownloadService.RECEIVER, new DownloadReceiver(
						new Handler()));
				startService(intent);
			} else {

				progressBar.setProgress(0);
				Toast.makeText(Downloader.this, getResources().getString(R.string.inet_toast),
						Toast.LENGTH_LONG).show();

			}
		}
	}

	;

	public boolean isOnline() {
		ConnectivityManager cm 		= (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo 		netInfo = cm.getActiveNetworkInfo();
		if (netInfo != null && netInfo.isConnectedOrConnecting()) {
			return true;
		}
		return false;
	}

}
