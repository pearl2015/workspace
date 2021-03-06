package com.example.blemasterv3;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
 

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothManager;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class ListAdapter extends ArrayAdapter<BluetoothDevice> {

	// bluetooth
	private final static String TAG = "GetBle";
	private BluetoothManager bluetoothManager;
	private BluetoothAdapter mBluetoothAdapter;

	private boolean mScanning;
	private Handler mHandler;

	private Handler uuidhandler;
	
	private ViewHolder viewHolder;
	// Stops scanning after 10 seconds.
	private static final long SCAN_PERIOD = 10000;

	// view
	private Context context;
	int resource;
	private ArrayList<BluetoothDevice> mLeDevices;
	private final HashMap<BluetoothDevice, Integer> rssiMap = new HashMap<BluetoothDevice, Integer>();

	private List<ViewHolder> holders = new ArrayList<ViewHolder>();

	public ListAdapter(Context _context, int _resource,
			List<BluetoothDevice> objects) {

		super(_context, _resource, objects);
		// TODO Auto-generated constructor stub
		this.context = _context;
		resource = _resource;
		mHandler = new Handler();
	}

	public ListAdapter(Context context, int resource, UUIDCallback uuidcallback) {
		super(context, resource);
		// TODO Auto-generated constructor stub
		this.context = context;
		mLeDevices = new ArrayList<BluetoothDevice>();
		mHandler = new Handler();
		this.uuidcallback = uuidcallback;
	}

	public void addDevice(BluetoothDevice device, int rssi) {
		if (!mLeDevices.contains(device)) {
			mLeDevices.add(device);
		}
		rssiMap.put(device, rssi);
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return mLeDevices.size();
	}

	@Override
	public BluetoothDevice getItem(int position) {
		// TODO Auto-generated method stub
		return mLeDevices.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return super.getItemId(position);
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		LinearLayout newView;

		if (null == convertView) {
			newView = new LinearLayout(context);
			String inflater = Context.LAYOUT_INFLATER_SERVICE;
			LayoutInflater li;
			li = (LayoutInflater) context.getSystemService(inflater);
			convertView = li.inflate(R.layout.device_list_fragment, newView,
					true);

			viewHolder = new ViewHolder();
			viewHolder.devicename_tx = (TextView) convertView
					.findViewById(R.id.device_name_tx);
			viewHolder.deviceadd_tx = (TextView) convertView
					.findViewById(R.id.device_address_tx);
			viewHolder.devicerssi_tx = (TextView) convertView
					.findViewById(R.id.device_rssi_tx);
			viewHolder.conn_btn = (Button) convertView
					.findViewById(R.id.conn_btn);

			convertView.setTag(viewHolder);

		} else {
			// newView = (LinearLayout)convertView;
			viewHolder = (ViewHolder) convertView.getTag();
		}

		final BluetoothDevice device = mLeDevices.get(position);
		final String devicename = device.getName();
		 
		if (devicename != null && devicename.length() > 0)
			viewHolder.devicename_tx.setText(devicename);
		else
			viewHolder.devicename_tx.setText("unknown device");
		viewHolder.deviceadd_tx.setText(device.getAddress());
		viewHolder.devicerssi_tx.setText("" + rssiMap.get(device) + "db");

		viewHolder.conn_btn.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(context, DeviceControlActivity.class);
				intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				//intent.setClass(context, SecondActivity.class);
				intent.putExtra(DeviceControlActivity.EXTRAS_DEVICE_NAME,
						device.getName());
				intent.putExtra(DeviceControlActivity.EXTRAS_DEVICE_ADDRESS,
						device.getAddress());
				context.startActivity(intent);
				Toast.makeText(context, "onclick", Toast.LENGTH_SHORT).show();
			}
		});
		return convertView;
	}

	static class ViewHolder {
		TextView devicename_tx;
		TextView deviceadd_tx;
		TextView devicerssi_tx;
		Button conn_btn;
	}

	// Device scan callback.
	private BluetoothAdapter.LeScanCallback mLeScanCallback = new BluetoothAdapter.LeScanCallback() {
		@Override
		public void onLeScan(final BluetoothDevice device, final int rssi,
				final byte[] scanRecord) {
			 
			String devicename = device.getName();
			
			if (SampleGattAttributes.finddevice(devicename)){
			mHandler.postDelayed(new Runnable() {
				@Override 
				public void run() {
					MainActivity.listAdapter.addDevice(device, rssi);
					MainActivity.listAdapter.notifyDataSetChanged();
					
		            int major = ((scanRecord[25]  & 0xFF) << 8)
		                    | (scanRecord[26]  & 0xFF);
		             
		            //Bytes 27 and 28 of the advertisement packet represent the minor value
		            int minor = ((scanRecord[27] & 0xFF) << 8)
		                    | (scanRecord[28] & 0xFF);
		            
		            String major_hex = Tools.Tools.decTohex(major+"");
		            String minor_hex = Tools.Tools.decTohex(minor+"");
		            
		           uuidcallback.setMajor(major_hex);
		           uuidcallback.setMinor(minor_hex);
				}
			}, SCAN_PERIOD);
           }
		}
	};

	public void scanLeDevice(final boolean enable) {
		if (enable) {
			// Stops scanning after a pre-defined scan period.
			mHandler.postDelayed(new Runnable() {
				@Override
				public void run() {
					mScanning = false;
					mBluetoothAdapter.stopLeScan(mLeScanCallback);
				}
			}, SCAN_PERIOD);

			mScanning = true;
			mBluetoothAdapter.startLeScan(mLeScanCallback);
		} else {
			mScanning = false;
			mBluetoothAdapter.stopLeScan(mLeScanCallback);
		}

	}

	// bluetooth
	public boolean onSetUp() {

		bluetoothManager = (BluetoothManager) context
				.getSystemService(Context.BLUETOOTH_SERVICE);
		mBluetoothAdapter = bluetoothManager.getAdapter();

		if (null == bluetoothManager) {
			Log.e(TAG, "null==bluetoothManager");
		}

		if (mBluetoothAdapter == null) {
			Log.e(TAG, "null == mBluetoothAdapter");
			return false;
		}

		if (!mBluetoothAdapter.isEnabled()) {
			Log.i(TAG, "enable mBluetoothAdapter");
			mBluetoothAdapter.enable();
		}

		return true;
	}
	
	private UUIDCallback uuidcallback;
	
	public interface UUIDCallback{
		public void setMajor(String major);
		public void setMinor(String minor);
	}
	

}
