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

	// 유닛수
	private final static int UNIT_NUMBER = 10;

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

	// 여러 유닛스레드 객체
	private UnitThread[] unitThreadArry;
	private Thread timeThread;
	private UnitThread timeThread2;
	

	// 스레드관련 멤버변수.
	// UnitThread unit01;

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

		// 10개 스레드 초기화
		unitThreadArry = new UnitThread[UNIT_NUMBER];
		for (int i = 0; i < UNIT_NUMBER; i++) {
			if (i < 2) {
				unitThreadArry[i] = new UnitThread(1, getBaseContext());
				unitThreadArry[i].setDaemon(true);
			} else if (i < 4) {
				unitThreadArry[i] = new UnitThread(2, getBaseContext());
				unitThreadArry[i].setDaemon(true);
			} else if (i < 6) {
				unitThreadArry[i] = new UnitThread(3, getBaseContext());
				unitThreadArry[i].setDaemon(true);
			} else {
				unitThreadArry[i] = new UnitThread(4, getBaseContext());
				unitThreadArry[i].setDaemon(true);
			}
		}//10개유닛스레드 초기화
		
		timeThread2 = new UnitThread(1,getBaseContext());
		
		// 1~4 각기다른 유닛,소리게임 스레드시작
		// unit01 = new UnitThread(1, getBaseContext());
		// unit01.start();

	}

	@Override
	public void onStart() {
		super.onStart();

		if (accelerormeterSensor != null)
			sensorManager.registerListener(this, accelerormeterSensor,
					SensorManager.SENSOR_DELAY_GAME);

		// ***********시작음
		start_game();
	}

	public void start_game() {

		// 20초 19초 18초 순으로 유닛등장.
		for (int i = 0; i < UNIT_NUMBER; i++) {
			unitThreadArry[i].start();
			TimerThread timerThread = new TimerThread((120 - i) * 1000);
			timerThread.start();
		
			
		}
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

		for (int i = 0; i < UNIT_NUMBER; i++) {

			unitThreadArry[i].move_bgm_stop();
		}
	}

	// unit01.move_bgm_stop();

	
	// 센서가 변할때마다 실행(+흔들림감지,총소리)
	@Override
	public void onSensorChanged(SensorEvent event) {
		//view가 모두 그려지고 난다음 게임스레드를 작동시키기위하여 어쩔수없이 이곳에 다중스레드시작문을 넣었음.
	
		
		if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
			long currentTime = System.currentTimeMillis();
			long gabOfTime = (currentTime - lastTime);
			float gamer_degree = mView.getazimuth();

			modified_unitThread(); // 유닛 객체에게 현재 내가보는 방향을 알려줌.

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

					// 총에 맞았는지 check & 자체 효과음 재생 & 죽었는지확인
					for (int i = 0; i < UNIT_NUMBER; i++) {
						unitThreadArry[i].check_bullet();
					}
					// unit01.check_bullet();
					// Toast.makeText(this, "enm_dgree:" +
					// unit01.getEnm_degree(),
					// Toast.LENGTH_SHORT).show();

				}
				lastX = event.values[DATA_X];
				lastY = event.values[DATA_Y];
				lastZ = event.values[DATA_Z];

			}
		}
	}

	// 유닛 객체에게 현재내가보는 방향을 알려줌.
	public void modified_unitThread() {
		for (int i = 0; i < UNIT_NUMBER; i++) {
			unitThreadArry[i].setUser_degree(mView.getazimuth());
		}
		// unit01.setUser_degree(mView.getazimuth());
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
