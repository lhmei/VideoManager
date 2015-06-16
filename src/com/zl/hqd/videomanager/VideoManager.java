package com.zl.hqd.videomanager;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.bairuitech.anychat.AnyChatBaseEvent;
import com.bairuitech.anychat.AnyChatCoreSDK;

public class VideoManager implements AnyChatBaseEvent{
	private final String IP = "10.75.201.121"; //wait for ip address;
	private final int PORT = 8906; //wait for port;
	private final int PERIOD = 5 * 1000;
	private final int DELAY = 1000;
	private AnyChatCoreSDK mAnychat;
	private String mId;
	private TextView mTextView;
	private Button mButton;
	private Context mContext;
	public VideoManager(String id, TextView textView, Button button, Context context){
		mId = id;
		mTextView = textView;
		mButton = button;
		mContext = context;
	}
	public void initSDK(){
		if (mAnychat == null) {
			mAnychat = new AnyChatCoreSDK();
			mAnychat.SetBaseEvent(this);
			mAnychat.InitSDK(android.os.Build.VERSION.SDK_INT, 0);
		}
	}
	public void login(){
		mAnychat.Connect(IP, PORT);
		mAnychat.Login(mId, "123");
	}
	public void logout(){
		mAnychat.Logout();
	}
	@Override
	public void OnAnyChatConnectMessage(boolean bSuccess) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void OnAnyChatLoginMessage(int dwUserId, int dwErrorCode) {
		if(0 == dwErrorCode){
			mTimer.start();
		}
		
	}
	@Override
	public void OnAnyChatEnterRoomMessage(int dwRoomId, int dwErrorCode) {
		
		
	}
	@Override
	public void OnAnyChatOnlineUserMessage(int dwUserNum, int dwRoomId) {
		
	}
	@Override
	public void OnAnyChatUserAtRoomMessage(int dwUserId, boolean bEnter) {
	}
	@Override
	public void OnAnyChatLinkCloseMessage(int dwErrorCode) {
		// TODO Auto-generated method stub
		
	}
	
	private CountDownTimer mTimer = new CountDownTimer(PERIOD, DELAY){

		@Override
		public void onTick(long millisUntilFinished) {
			StringBuilder builder = new StringBuilder(mContext.getString(R.string.count_down));
			builder.replace(0, 1, millisUntilFinished/1000+"");
			mTextView.setText(builder.toString());			
		}

		@Override
		public void onFinish() {
			mAnychat.VideoCallControl(1, 1, 0, 0, 0, "");
			Intent intent = new Intent();
			Bundle bundle = new Bundle();
			bundle.putInt("TargetUser", 17719);
			intent.putExtras(bundle);
			intent.setClass(mContext, VideoActivity.class);
			mContext.startActivity(intent);
			mButton.setVisibility(View.VISIBLE);
			mTextView.setVisibility(View.GONE);
		}
		
	};

}
