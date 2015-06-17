package com.zl.hqd.videomanager;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Surface;
import android.view.SurfaceView;
import android.widget.Toast;

import com.bairuitech.anychat.AnyChatBaseEvent;
import com.bairuitech.anychat.AnyChatCoreSDK;
import com.bairuitech.anychat.AnyChatDefine;
import com.bairuitech.anychat.AnyChatUserInfoEvent;
import com.bairuitech.anychat.AnyChatVideoCallEvent;

public class VideoActivity extends Activity implements AnyChatBaseEvent, AnyChatVideoCallEvent, AnyChatUserInfoEvent{
	private AnyChatCoreSDK mAnychat;
	private SurfaceView mRemoteView;
	private SurfaceView mLocalView;
	private int mTargetUser;
	private int mVideoIndex;
	
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_video);
		Intent intent = getIntent();
		Bundle bundle = intent.getExtras();
		mTargetUser = bundle.getInt("TargetUser");
		mTargetUser = 1;
		initSdk();
		initView();
		mAnychat.VideoCallControl(1, 1, 0, 0, 0, "");
	}
	
	public void onDestroy(){
		super.onDestroy();
		mAnychat.UserCameraControl(-1, 0);
		mAnychat.UserSpeakControl(-1, 0);
		mAnychat.UserSpeakControl(mTargetUser, 0);
		mAnychat.UserCameraControl(mTargetUser, 0);
		mAnychat.LeaveRoom(-1);
	}
	
	private void initSdk() {
		if (mAnychat == null)
			mAnychat = new AnyChatCoreSDK();
		mAnychat.SetBaseEvent(this);
		mAnychat.SetVideoCallEvent(this);
		mAnychat.SetUserInfoEvent(this);
		mAnychat.mSensorHelper.InitSensor(this);
		// 鍒濆鍖朇amera涓婁笅鏂囧彞鏌�
		AnyChatCoreSDK.mCameraHelper.SetContext(this);

	}
	
	private void initView(){
		mRemoteView = (SurfaceView)findViewById(R.id.video_remote);
		mLocalView = (SurfaceView)findViewById(R.id.video_local);
		mLocalView.setZOrderOnTop(true);
		// 濡傛灉鏄噰鐢↗ava瑙嗛閲囬泦锛屽垯璁剧疆Surface鐨凜allBack
		if (AnyChatCoreSDK.GetSDKOptionInt(AnyChatDefine.BRAC_SO_LOCALVIDEO_CAPDRIVER) == AnyChatDefine.VIDEOCAP_DRIVER_JAVA) {
			mLocalView.getHolder().addCallback(AnyChatCoreSDK.mCameraHelper);
			Log.i("ANYCHAT", "VIDEOCAPTRUE---" + "JAVA");
		}

		// 濡傛灉鏄噰鐢↗ava瑙嗛鏄剧ず锛屽垯璁剧疆Surface鐨凜allBack
		if (AnyChatCoreSDK.GetSDKOptionInt(AnyChatDefine.BRAC_SO_VIDEOSHOW_DRIVERCTRL) == AnyChatDefine.VIDEOSHOW_DRIVER_JAVA) {
			mVideoIndex = mAnychat.mVideoHelper.bindVideo(mRemoteView.getHolder());
			mAnychat.mVideoHelper.SetVideoUser(mVideoIndex, mTargetUser);
			mAnychat.UserCameraControl(mTargetUser, 1);
			mAnychat.UserSpeakControl(mTargetUser, 1);
			Log.i("ANYCHAT", "VIDEOSHOW---" + "JAVA");
		}else{
			Surface s = mRemoteView.getHolder().getSurface();
			mAnychat.SetVideoPos(mTargetUser, s, 0, 0, 0, 0); 
		}
		
		if (AnyChatCoreSDK.mCameraHelper.GetCameraNumber() > 1) {
			// 榛樿鎵撳紑鍓嶇疆鎽勫儚澶�
			AnyChatCoreSDK.mCameraHelper.SelectVideoCapture(AnyChatCoreSDK.mCameraHelper.CAMERA_FACING_FRONT);
		}
	}
	

	@Override
	public void OnAnyChatUserInfoUpdate(int dwUserId, int dwType) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void OnAnyChatFriendStatus(int dwUserId, int dwStatus) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void OnAnyChatVideoCallEvent(int dwEventType, int dwUserId,
			int dwErrorCode, int dwFlags, int dwParam, String userStr) {
		Log.e("VideoActivity", dwEventType+"");
		//mAnychat.EnterRoom(dwParam, "");
		switch(dwEventType){
		case AnyChatDefine.BRAC_VIDEOCALL_EVENT_REPLY:
			messageHandle(dwErrorCode, dwParam);
			break;
		case AnyChatDefine.BRAC_VIDEOCALL_EVENT_START:
			Log.e("VideoActivity", dwEventType+"");
			break;
		case AnyChatDefine.BRAC_VIDEOCALL_EVENT_FINISH:
			finish();
			Log.e("VideoActivity", dwEventType+"");
			break;
		}
	}

	@Override
	public void OnAnyChatConnectMessage(boolean bSuccess) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void OnAnyChatLoginMessage(int dwUserId, int dwErrorCode) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void OnAnyChatEnterRoomMessage(int dwRoomId, int dwErrorCode) {
		if(0 == dwErrorCode){
			mVideoIndex = mAnychat.mVideoHelper.bindVideo(mRemoteView.getHolder());
			mAnychat.mVideoHelper.SetVideoUser(mVideoIndex, mTargetUser);
			mAnychat.UserCameraControl(-1, 1);
			mAnychat.UserSpeakControl(-1, 1);
			mAnychat.UserCameraControl(mTargetUser, 1);
			mAnychat.UserSpeakControl(mTargetUser, 1);
		}
		
	}

	@Override
	public void OnAnyChatOnlineUserMessage(int dwUserNum, int dwRoomId) {
		mAnychat.UserCameraControl(mTargetUser, 1);
		mAnychat.UserSpeakControl(mTargetUser, 1);
		
	}

	@Override
	public void OnAnyChatUserAtRoomMessage(int dwUserId, boolean bEnter) {
		mAnychat.UserCameraControl(mTargetUser, 1);
		mAnychat.UserSpeakControl(mTargetUser, 1);
		
	}

	@Override
	public void OnAnyChatLinkCloseMessage(int dwErrorCode) {
		mAnychat.UserCameraControl(mTargetUser, 0);
		mAnychat.UserSpeakControl(mTargetUser, 0);
		mAnychat.UserCameraControl(-1, 0);
		mAnychat.UserSpeakControl(-1, 0);
		finish();
		
	}
	
	private void messageHandle(int errorCode, int roomId){
		switch(errorCode){
		case AnyChatDefine.BRAC_ERRORCODE_SESSION_BUSY:
			Toast.makeText(this, R.string.session_busy, Toast.LENGTH_SHORT).show();
			finish();
			break;
		case AnyChatDefine.BRAC_ERRORCODE_SESSION_DISCONNECT:
			Toast.makeText(this, R.string.session_disconnect, Toast.LENGTH_SHORT).show();
			finish();
			break;
		case AnyChatDefine.BRAC_ERRORCODE_SESSION_OFFLINE:
			Toast.makeText(this, R.string.session_offline, Toast.LENGTH_SHORT).show();
			finish();
			break;
		case AnyChatDefine.BRAC_ERRORCODE_SESSION_QUIT:
			Toast.makeText(this, R.string.session_quit, Toast.LENGTH_SHORT).show();
			finish();
			break;
		case AnyChatDefine.BRAC_ERRORCODE_SESSION_REFUSE:
			Toast.makeText(this, R.string.session_refuse, Toast.LENGTH_SHORT).show();
			finish();
			break;	
		case AnyChatDefine.BRAC_ERRORCODE_SESSION_TIMEOUT:
			Toast.makeText(this, R.string.session_timeout, Toast.LENGTH_SHORT).show();
			finish();
			break;
		case AnyChatDefine.BRAC_ERRORCODE_SUCCESS:
			mAnychat.EnterRoom(roomId, "");
		}
	}

}
