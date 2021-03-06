package com.example.androidtest;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
 

import android.annotation.SuppressLint;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

@SuppressLint("NewApi") public class MainActivity extends ActionBarActivity {
	JSONArray jArray;
	String result = null;
	InputStream is = null;
	StringBuilder sb = null;

	private EditText tv = null;
	private Button b1 = null;
	

	private EditText send_tv = null;
	private Button send_btn = null;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		 b1 = (Button) findViewById(R.id.button1);

		tv = (EditText) findViewById(R.id.editView);
		
		b1.setOnClickListener(new Button.OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
		        Intent wampConnIntent = new Intent(getApplicationContext(), WampService.class);
		        bindService(wampConnIntent, mServiceConnection, BIND_AUTO_CREATE);
			}
	});
		
		send_tv = (EditText)findViewById(R.id.send_content);
		send_btn = (Button)findViewById(R.id.send_btn);
		
		send_btn.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				runOnUiThread(new Runnable() {
					
					@Override
					public void run() {
						// TODO Auto-generated method stub

						mWampService.makePostRequest();
					}
				});
			}
		});
		
		 
	}
	  
	

	private WampService mWampService;
//	 Code to manage Service lifecycle.
	private ServiceConnection mServiceConnection = new ServiceConnection() {

		@Override
		public void onServiceConnected(ComponentName componentName,
				IBinder service) {
			mWampService = ((WampService.LocalBinder) service)
					.getService();
			mWampService.connnectWamp(myhandler);
			
		}

		@Override
		public void onServiceDisconnected(ComponentName componentName) {
			mWampService = null;
		}
	};
 

	private final Handler myhandler = new Handler(){
		public void handleMessage(Message msg){
			if(msg.what == 0x123){
				System.out.println("------->" + msg.obj);
				tv.append(msg.obj + "\n");
			}
		}
	};
	
	
	
}
