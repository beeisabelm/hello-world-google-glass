package com.isabel.helloworld;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.view.Menu;
import android.view.MenuItem;

/**
 * Based on Google Glass Sample - Compass and StopWatch
 * @author IsabelM
 * 
 */
public class MainActivity extends Activity {

	private boolean mResumed;
	private MainService.MainBinder mService;
	private ServiceConnection mConnection = new ServiceConnection() {

		@Override
		public void onServiceConnected(ComponentName arg0, IBinder binderService) {
			if(binderService instanceof MainService.MainBinder) {
				mService = (MainService.MainBinder) binderService;
				openOptionsMenu();
			}
		}

		@Override
		public void onServiceDisconnected(ComponentName arg0) {
			// TODO Auto-generated method stub
		}
	};

	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		if(mService == null) {
			bindService(new Intent(this, MainService.class), mConnection, 0);
		}
		
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		mResumed = true;
	}
	
	@Override
    protected void onPause() {
        super.onPause();
        mResumed = false;
    }
	
	@Override
    public void openOptionsMenu() {
        if (mResumed && mService != null) {
            super.openOptionsMenu();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public void onDestroy() {
    	super.onDestroy();
    	finish();
    }
    
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.read_aloud:
            	mService.sayHiLoudly();
            	finish();
                return true;
            case R.id.stop:
                stopService(new Intent(this, MainService.class));
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onOptionsMenuClosed(Menu menu) {
        super.onOptionsMenuClosed(menu);

        unbindService(mConnection);
    }
}
