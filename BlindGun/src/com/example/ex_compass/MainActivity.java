package com.example.ex_compass;

import java.io.*;

import android.app.*;
import android.content.*;
import android.graphics.*;
import android.media.*;
import android.os.*;
import android.util.*;
import android.view.*;
import android.view.View.OnClickListener;
import android.widget.*;

public class MainActivity extends Activity {

	/* (non-Javadoc)
	 * @see android.app.Activity#onDestroy()
	 */

	private MediaPlayer title_bgm;
	private View howto_view;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFormat(PixelFormat.TRANSLUCENT);

		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		// view init
		howto_view = (View) findViewById(R.id.howto_view);

		// bgm시작
		bgm_startable();
//		title_bgm.start();

		

		// start
		ImageButton btn01 = (ImageButton) findViewById(R.id.imageButton1);

		btn01.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {

				title_bgm.stop();

				Intent intent = new Intent(MainActivity.this,
						GameActivity.class);
				startActivity(intent);
				finish();
			}
		});

		// vivration on/off
		ImageButton btn02 = (ImageButton) findViewById(R.id.imageButton2);
		btn02.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				Toast.makeText(getBaseContext(), "Vivration ON/OFF",
						Toast.LENGTH_SHORT).show();

			}
		});

		// howto
		ImageButton btn03 = (ImageButton) findViewById(R.id.imageButton3);
		btn03.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				howto_view.setVisibility(View.VISIBLE);

			}
		});
		ImageButton btn04 = (ImageButton) findViewById(R.id.imageButton4);
		btn04.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				howto_view.setVisibility(View.INVISIBLE);

			}
		});
	
	}// oncreate
	
	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();

		title_bgm.stop();
	}

	private void init_MediaPlayer() {
		// TODO Auto-generated method stub
		title_bgm = MediaPlayer.create(getBaseContext(), R.raw.main_bgm);

	}

	private void bgm_seektozero() {
		// TODO Auto-generated method stub
		title_bgm.seekTo(0);

	}

	private void bgm_looping() {
		// TODO Auto-generated method stub
		title_bgm.setLooping(true); // 반복설정
	}

	private void bgm_prepare() {
		// TODO Auto-generated method stub
		try {
			title_bgm.prepare();

		} catch (IllegalStateException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public void bgm_pause() {
		title_bgm.pause();

	}

	public void bgm_stop() {
		title_bgm.stop();

	}

	public void bgm_startable() {
		init_MediaPlayer();
		bgm_prepare();
		bgm_looping();

	}

}
