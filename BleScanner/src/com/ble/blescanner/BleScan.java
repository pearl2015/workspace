package com.ble.blescanner;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothManager;
import android.content.Context;
import android.os.Handler;
import android.util.Log;

public class BleScan extends Activity {

	private final static String TAG = "GetBle";
	private BluetoothManager bluetoothManager;
	private BluetoothAdapter mBluetoothAdapter;

	private boolean mScanning;
	private Handler mHandler;
	
	private Context context;

	// Stops scanning after 10 seconds.
	private static final long SCAN_PERIOD = 10000;
	
	public BleScan(Context context){
		
		this.context = context;
		mHandler = new Handler();
	}
	
	public BleScan(){
		
		//this.context = context;
		mHandler = new Handler();
	}
	

	

	public boolean onSetUp() {

		bluetoothManager = (BluetoothManager)context.getSystemService(Context.BLUETOOTH_SERVICE);
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

	// Device scan callback.
	private BluetoothAdapter.LeScanCallback mLeScanCallback = new BluetoothAdapter.LeScanCallback() {
		@Override
		public void onLeScan(final BluetoothDevice device, int rssi,
				byte[] scanRecord) {
			mHandler.postDelayed(new Runnable() {
				@Override
				public void run() {
					// mLeDeviceListAdapter.addDevice(device);
					// mLeDeviceListAdapter.notifyDataSetChanged();
//					String a = device.getName();
//					if(!(null==a))
//					MainActivity.devicename.setText(a);
					
					String a = device.getName();
					boolean repeat = false;
					if(null !=  MainActivity.devices)
					for(String b : MainActivity.devices)
					{
						if(b==a)
							repeat = true;
							//return; 
					}
					if(!repeat)
						ScanDeviceFragment.onNewDeviceAddedListener.onNewDeviceAdded(a);
				}
			}, SCAN_PERIOD);
		}
	};
 
}
