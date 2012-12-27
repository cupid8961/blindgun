package com.example.ex_compass;

import java.io.*;
import java.util.*;

import android.content.*;
import android.media.*;
import android.util.*;

public class BGMPlayer {
/*
	private ArrayList<Integer> BGMAdd_List;
	private ArrayList<MediaPlayer> BGMList;
	private Context prev_context;
	
	public BGMPlayer(Context context){
		BGMList= new ArrayList<MediaPlayer>();
		prev_context=context;
		
	}
	private void set(int wavefile){
		MediaPlayer tempMP = new MediaPlayer();
		tempMP.create(prev_context,wavefile);
		BGMList.add(tempMP);
		BGMAdd_List.add(wavefile);
	}
	
	private void init_MediaPlayer() {
		// TODO Auto-generated method stub
		main_bgm = MediaPlayer.create(getBaseContext(), R.raw.main_bgm);
		select_bgm = MediaPlayer.create(getBaseContext(), R.raw.select_bgm);
		clock_bgm = MediaPlayer.create(getBaseContext(), R.raw.clock_bgm);
		fast_clock_bgm = MediaPlayer.create(getBaseContext(),
				R.raw.fast_clock_bgm);
		
		for(int i : BGMList.){
			i.create(prev_context, BGMAdd_List.get(i));
			
		}

	}
	private void bgm_seektozero() {
		// TODO Auto-generated method stub
		main_bgm.seekTo(0); 
		select_bgm.seekTo(0);
		clock_bgm.seekTo(0);
		fast_clock_bgm.seekTo(0);
		
	}
	private void bgm_looping() {
		// TODO Auto-generated method stub
		main_bgm.setLooping(true); // 諛섎났�ㅼ젙
		select_bgm.setLooping(true);
		clock_bgm.setLooping(true);
		fast_clock_bgm.setLooping(true);
	}

	private void bgm_prepare() {
		// TODO Auto-generated method stub
		try {
			main_bgm.prepare();
			select_bgm.prepare();
			clock_bgm.prepare();
			fast_clock_bgm.prepare();

		} catch (IllegalStateException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		

	}

	public void bgm_pause() {
		main_bgm.pause(); 
		select_bgm.pause();
		clock_bgm.pause();
		fast_clock_bgm.pause();

	}

	public void bgm_stop() {
		main_bgm.stop(); 
		select_bgm.stop();
		clock_bgm.stop();
		fast_clock_bgm.stop();

	}
	public void bgm_startable(){
		init_MediaPlayer();
		bgm_prepare();
		bgm_looping();
		
	
	}    

*/

}
