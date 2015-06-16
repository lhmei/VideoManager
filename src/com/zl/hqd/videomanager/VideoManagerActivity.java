package com.zl.hqd.videomanager;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;


public class VideoManagerActivity extends ActionBarActivity {
	TextView mTextView;
	Button mButton;
	String userId = "chenqihong";
	VideoManager mVideoManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_manager);
        mTextView = (TextView)findViewById(R.id.video_manager_text);
        mButton = (Button)findViewById(R.id.video_manager_button);
        mButton.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View arg0) {
				finish();
			}
        	
        });
        mVideoManager = new VideoManager(userId, mTextView, mButton, this);
        mVideoManager.initSDK();
        mVideoManager.login();
    }
    
    @Override
    public void onDestroy(){
    	super.onDestroy();
    	mVideoManager.logout();
    }
}
