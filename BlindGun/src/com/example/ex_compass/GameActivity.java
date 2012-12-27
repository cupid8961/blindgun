package com.example.ex_compass;

import java.io.*;
import java.util.*;

import android.app.*;
import android.content.*;
import android.hardware.*;
import android.media.*;
import android.os.*;
import android.widget.*;

public class GameActivity extends Activity implements SensorEventListener {

	private SoundManager mSoundManager;// soundpool_효과음 깔기.
	private MediaPlayer foot_bgm;// mediaPalyer_bgm깔기

	private SensorManager sensorManager;
	private Sensor accelerormeterSensor;
	CompassView mView;

	private long lastTime;
	private float speed;
	private float lastX;
	private float lastY;
	private float lastZ;
	private float enm_angle;

	// stereo 볼륨
	private float left_frequence;
	private float right_frequence;

	private float x, y, z;
	private static final int SHAKE_THRESHOLD = 800;

	private static final int DATA_X = SensorManager.DATA_X;
	private static final int DATA_Y = SensorManager.DATA_Y;
	private static final int DATA_Z = SensorManager.DATA_Z;

	// 스레드관련 멤버변수.
	UnitThread unit01;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// 센서 등록
		sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
		accelerormeterSensor = sensorManager
				.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);

		// 효과음init
		init_soundpool();

		// 나침판등록
		mView = new CompassView(this);
		setContentView(mView);
		
		// 1~4 각기다른 유닛,소리게임 스레드시작
		unit01 = new UnitThread(1, getBaseContext());
		unit01.start();

	}

	@Override
	public void onStart() {
		super.onStart();

		if (accelerormeterSensor != null)
			sensorManager.registerListener(this, accelerormeterSensor,
					SensorManager.SENSOR_DELAY_GAME);

		// ***********시작음

	}

	@Override
	protected void onResume() {
		super.onResume();
		sensorManager.registerListener(mView,
				sensorManager.getDefaultSensor(Sensor.TYPE_ORIENTATION),
				SensorManager.SENSOR_DELAY_UI);
	}

	@Override
	protected void onPause() {
		super.onPause();
		sensorManager.unregisterListener(mView);
	}

	@Override
	public void onStop() {
		super.onStop();

		if (sensorManager != null)
			sensorManager.unregisterListener(this);
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		unit01.move_bgm_stop();

	}

	// 센서가 변할때마다 실행(+흔들림감지,총소리)

	@Override
	public void onSensorChanged(SensorEvent event) {
		if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
			long currentTime = System.currentTimeMillis();
			long gabOfTime = (currentTime - lastTime);
			float gamer_degree = mView.getazimuth();

			
			modified_unitThread(); //유닛 객체에게 현재 내가보는 방향을 알려줌.
			
			// 0.1초 마다 폰이 흔들렸는지 체크함.
			if (gabOfTime > 100) {
				lastTime = currentTime;

				x = event.values[SensorManager.DATA_X];
				y = event.values[SensorManager.DATA_Y];
				z = event.values[SensorManager.DATA_Z];

				speed = Math.abs(x + y + z - lastX - lastY - lastZ) / gabOfTime
						* 10000;

				if (speed > SHAKE_THRESHOLD) { // 총쐇을때
					// 총소리 효과음 재생
					mSoundManager.playSound(1);

					// 총에 맞았는지 check & 자체 효과음 재생.
					unit01.check_bullet();
					Toast.makeText(this, "enm_dgree:" + unit01.getEnm_degree(), Toast.LENGTH_SHORT).show();


				}
				lastX = event.values[DATA_X];
				lastY = event.values[DATA_Y];
				lastZ = event.values[DATA_Z];

			}
		}
	}
	
	//유닛 객체에게 현재내가보는 방향을 알려줌.
		public void modified_unitThread(){
			unit01.setUser_degree(mView.getazimuth());
		}
		
	private void init_soundpool() {
		// TODO Auto-generated method stub
		mSoundManager = new SoundManager();
		mSoundManager.initSounds(getBaseContext());

		mSoundManager.addSound(1, R.raw.shot);

	}

	@Override
	public void onAccuracyChanged(Sensor sensor, int accuracy) {
	}

	@Override
	// 뒤로가기 이벤트
	public void onBackPressed() {

		super.onBackPressed();

		Intent intent = new Intent(GameActivity.this, MainActivity.class);
		startActivity(intent);
		finish();
	}

}
