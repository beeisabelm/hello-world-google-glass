package com.isabel.helloworld;

import com.google.android.glass.timeline.LiveCard;
import com.google.android.glass.timeline.TimelineManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.speech.tts.TextToSpeech;
import android.widget.RemoteViews;

/**
 * Based on Google Glass Sample - Compass and StopWatch
 * @author IsabelM
 * 
 */
public class MainService extends Service {

	private static final String LIVE_CARD_ID = "Hello";
	private static final CharSequence INTRO = "Hi to the World";
	private TimelineManager mTimelineManager;
	private LiveCard mLiveCard;
	private TextToSpeech mSpeech;
	private final IBinder mBinder = new MainBinder();

	public class MainBinder extends Binder {
		public void sayHiLoudly() {
			mSpeech.speak(getString(R.string.hello), TextToSpeech.QUEUE_FLUSH, null);
		}
	}

	@Override
    public void onCreate() {
        super.onCreate();
	        mTimelineManager = TimelineManager.from(this);
	        mSpeech = new TextToSpeech(this, new TextToSpeech.OnInitListener() {
	            @Override
	            public void onInit(int status) {
	            	//do nothing
	            }
	        });
	}
	
	@Override
    public int onStartCommand(Intent intent, int flags, int startId) {
		RemoteViews aRV = new RemoteViews(this.getPackageName(),
                R.layout.card_text);
        if (mLiveCard == null) {
            mLiveCard = mTimelineManager.createLiveCard(LIVE_CARD_ID);
            aRV.setTextViewText(R.id.main_text, INTRO);
            mLiveCard.setViews(aRV);
            Intent mIntent = new Intent(this, MainActivity.class);
            mIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            mLiveCard.setAction(PendingIntent.getActivity(this, 0, mIntent, 0));
            mLiveCard.publish(LiveCard.PublishMode.REVEAL);
        } 
        return START_STICKY;
    }
	
	@Override
	public void onDestroy() {
	    if (mLiveCard != null && mLiveCard.isPublished()) {
	        mLiveCard.unpublish();
	        mLiveCard = null;
	    }
	    mSpeech.shutdown();

        mSpeech = null;
        super.onDestroy();
	}

	@Override
	public IBinder onBind(Intent intent) {
		return mBinder ;
	}
}
