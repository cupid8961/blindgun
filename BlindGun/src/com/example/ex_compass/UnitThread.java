package com.example.ex_compass;

import java.io.*;

import android.content.*;
import android.media.*;
import android.util.*;
import android.widget.*;

public class UnitThread extends Thread {

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Thread#destroy()
	 */

	private final static int LIVE = 1;
	private final static int DAMEGED = 2;
	private final static int DEAD = 3;
	private final static int FINISH = 4;
	private final static float EFFECTIVE_DEGREE = 10;
	private final static float EFFECTIVE_DEGREE_PERCENT = (float) (0.5 * (90 - EFFECTIVE_DEGREE) / 90);

	// 스레드 관련.
	private Thread timer_thread; // 효과음타이머
	private Thread volume_thread; // 볼륨구현 스레드

	private Enermy enermy;
	private Context context;
	private int state;
	private SoundManager mSoundManager; // DAMEGED, DEAD됏을때 효과음
	private MediaPlayer move_bgm;// 발자국소리,
	private int step;

	// stereo 볼륨
	private float left_frequence;// ************실시간 체크
	private float right_frequence;// ************실시간 체크

	// angle
	private float user_degree;
	private float enm_degree;

	// 디버그용 임시토스트
	private boolean logFlag = false;

	public UnitThread(int i, Context context) {

		enermy = new Enermy(i);
		this.context = context;
		state = LIVE;
		step = 0;

		enm_degree = enermy.getEnm_degree();
		// 소리 init
		init_soundpool();
		bgm_startable();
	}

	@Override
	public void destroy() {
		// TODO Auto-generated method stub
		super.destroy();

	}

	public void move_bgm_stop() {
		move_bgm.stop();
	}

	public void setUser_degree(float user_degree) {
		this.user_degree = user_degree;
	}

	public void bgm_startable() {
		move_bgm = MediaPlayer.create(context, enermy.getMove_bgm_resid());
		try {
			move_bgm.prepare();
		} catch (IllegalStateException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		move_bgm.setLooping(true);
	}

	// 유닛마다의 소리의 상대적 위치를 바꾸어줌
	private void modified_sound() {
		move_bgm.setVolume(this.left_frequence, this.right_frequence);
	}
	// 다가올때마다 소리커지는기능+ 스트레오기능
	private void set_moving_stereo(float lf, float rf) {

		float moving_left_percent = (float) ((step + 1) * 0.1
				* (float) enermy.getSpeed() / enermy.getRange() * lf);
		float moving_right_percent = (float) ((step + 1) * 0.1
				* (float) enermy.getSpeed() / enermy.getRange() * rf);

		if (moving_left_percent <= lf && (moving_right_percent <= rf)) {

			++step;
			this.left_frequence = (float) (lf * (step) * 0.1
					* (float) enermy.getSpeed() / enermy.getRange());
			this.right_frequence = (float) (rf * (step) * 0.1
					* (float) enermy.getSpeed() / enermy.getRange());

		} else {
			this.left_frequence = lf;
			this.right_frequence = rf;
			if (!logFlag) {

				Log.i("check", "range = 0");
				logFlag = true;
			}
		}

		return;
	}

	private void set__stereo(float lf, float rf) {
		this.left_frequence = lf;
		this.right_frequence = rf;
		return;
	}

	private void init_soundpool() {
		// TODO Auto-generated method stub
		mSoundManager = new SoundManager();
		mSoundManager.initSounds(context);

		mSoundManager.addSound(1, enermy.getDmg_sound_resid()); // 1번엔 맞는 소리넣음
		mSoundManager.addSound(2, enermy.getDead_sound_resid()); // 2번엔 죽는 소리넣음
	}

	private void sound_check(float ma, float ea) {// m=my_angle 나의 각도,
													// e=enermy_angle 적의 각도
		float ie;// ie:상대적 적의위치 = 적위치- 나의위치 (나의위치를 0으로 잡았을시)
		float lf, rf;// lf,rf :왼쪽,오른쪽 소리비중 (lf+rf=1.0f)
		ie = ea - ma;

		if (ie >= 270) { // 359~ 270도 사이 ,앞에있을때
			ie -= 270;
			lf = 1.0f - 0.5f * (ie / 90);
			rf = 1.0f - lf;

		} else if (ie >= 180) {// 269~180 ,뒤
			ie -= 180;
			lf = 0.5f + (0.5f * ie / 90);
			rf = 1.0f - lf;
			lf /= 2;
			rf /= 2;

		} else if (ie >= 90) {// 179~90,뒤
			ie -= 90;
			rf = 1.0f - (0.5f * ie / 90);
			lf = 1.0f - rf;
			lf /= 2;
			rf /= 2;

		} else if (ie >= 0) {// 89~ 0, 앞
			rf = 0.5f + 0.5f * ie / 90;
			lf = 1.0f - rf;

		} else if (ie >= -90) {// -1~<=90,앞
			lf = 0.5f + 0.5f * (-ie / 90);
			rf = 1.0f - lf;
		} else if (ie >= -180) { // -91~ <=-180,뒤
			ie += 90;
			lf = 1.0f - 0.5f * (-ie / 90);
			rf = 1.0f - lf;
			lf /= 2;
			rf /= 2;

		} else if (ie >= -270) {// -181~ <=-270 ,뒤
			ie += 180;
			rf = 0.5f + 0.5f * (-ie / 90);
			lf = 1.0f - rf;
			lf /= 2;
			rf /= 2;

		} else {
			ie += 270;
			rf = 1.0f - 0.5f * (-ie / 90);
			lf = 1.0f - rf;

		}
		set_moving_stereo(lf, rf);

		return;

	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		while (state != FINISH) { // 유닛이 상태가 끝났는가?

			if (state == 1) {// 살아있을때, 다가오는소리내기(o점점커지게하기, o위치선정)
				if (!move_bgm.isPlaying()) { // 시작시 한번만 재생.
					Log.i("check","in state1");
					move_bgm.start();
				}
			} else if (state == 2) {// 한대 맞았을때
				Log.i("check","in state2");
				move_bgm.pause();

				// 체력 -1
				enermy.downHp();

				// 죽엇는지 확인
				if (enermy.checkDead()) {
					Log.i("check","in state2, checkDead");
					state = 3;
					continue;

				} else {// 맞았는데도 살아있는경우,1초 대기후 다시 움직임.
					try {
						mSoundManager.playSound(1, left_frequence,
								right_frequence);
						timer_thread.sleep(2000);

						move_bgm.start();

					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}

			} else if (state == 3) { // 죽었을때
				Log.i("check","in state3");
				mSoundManager.playSound(2, left_frequence, right_frequence);

				try {
					timer_thread.sleep(3000); // 효과음이 다나고 스레드 없앰.
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				timer_thread = null;
				state = FINISH;

			}
			
			else {
				Log.i("check", "UnitThread else오류");

			}

			// 0.1초마다 스테레오 갱신.
			sound_check(user_degree, enm_degree); // +set_moving_stereo(lf, rf);
			modified_sound();

			try {
				timer_thread.sleep(100); // 0.1초
			} catch (Exception e) {
			}
		}// while문

		// 스레드 종료

	}

	// 내가 유효범위내에 쏜경우
	public void check_bullet() {
		Log.i("check","in check_bullet");
		// TODO Auto-generated method stub
		// 왼쪽소리도 0.5*80/90 이상, 오른쪽도 0.5*80/90이상 소리가 균형잡히게나는경우 -> 정면이란 말과동일
		if ((left_frequence > EFFECTIVE_DEGREE_PERCENT)
				&& (right_frequence > EFFECTIVE_DEGREE_PERCENT)) {
			state = 2; // 맞은 상태 전환
			enermy.downHp();
		}

		return;

	}

	public float getUser_degree() {
		return user_degree;

	}

	public float getEnm_degree() {
		return enm_degree;

	}
}
