package com.example.ble_linker;



import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity {
	/** Called when the activity is first created. */
	public static List<Map<String, Object>> mData;
	private int flag;
	public static String title[]=new String[]{"����0","����1","����2","����3","����4","����5","����6","����7","����8","����9"};
	public static String info[]=new String[]{ "����28","����28","����28","����28","����28","����28","����28","����28","����28","����28",};
	

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		mData = getData();
		ListView listView = (ListView) findViewById(R.id.devices_list);
		MyAdapter adapter = new MyAdapter(this);
		listView.setAdapter(adapter);
		
		
	}



	//��ȡ��̬��������  �����������ط�����(json��)
	private List<Map<String, Object>> getData() {
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
for(int i=0;i<title.length;i++){
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("title", title[i]);
		map.put("info", info[i]);
		list.add(map);
}

		return list;
	}

//	public void showInfo(int position){
//		  
//		new AlertDialog.Builder(this)
//		.setTitle("����"+position)
//		.setMessage("������")
//		.setPositiveButton("ȷ��", new DialogInterface.OnClickListener() {
//			@Override
//			public void onClick(DialogInterface dialog, int which) {
//			}
//		})
//		.show();
//	}
	
	
}