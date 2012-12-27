/*
 * 총거리 : 100
 * 
 * # 적 구성요서
 * -고유넘버&종류, 스피드, 체력, 위치, 움직이는소리, 죽는소리
 * 
 * -종류
 * 	1.사람 	 (1, 5, 1, ran, 똑똑똑, 으악)
 * 	2.개   	 (2, 10, 2, ran, 왈왈, 으악)
 *  3.쇠사슬병사(3, 6, 3, ran, 쓰르릉, 으악)
 * 	4.전기톱병사(4, 8, 5, ran, 덜덜덜, 으악)
 *
 *  
 */

package com.example.ex_compass;

import java.util.*;

import android.util.*;

public class Enermy {
	private int id;
	private String name;
	private int speed;
	private int hp;
	private float enm_degree;
	private int move_bgm_resid;
	private int dmg_sound_resid;
	private int dead_sound_resid;
	private int range;
	Random rand;

	public Enermy(int id) {
		this.id = id;

		switch (id) {
		case 1:
			name = "Human";
			speed = 1;
			hp = 1;
			rand = new Random(System.currentTimeMillis()); // seed값을 배정하여 생성
//			enm_degree = Math.abs(rand.nextInt(359)); // 0~359 ,
														// Math.abs(rand.nextInt(9)+1)
														// //1~10
			
			enm_degree =100;
			move_bgm_resid = R.raw.foot;
			dmg_sound_resid = R.raw.enm_dmg;
			dead_sound_resid = R.raw.enm_dmg;
			range=100;
			break;

		case 2:// 2.개 (2, 10, 2, ran, 왈왈, 으악)
			name = "Dog";
			speed = 10;
			hp = 2;
			rand = new Random(System.currentTimeMillis()); // seed값을 배정하여 생성
			enm_degree = Math.abs(rand.nextInt(359)); // 0~359 ,
														// Math.abs(rand.nextInt(9)+1)
														// //1~10
			move_bgm_resid = R.raw.foot;
			dmg_sound_resid = R.raw.enm_dmg;
			dead_sound_resid = R.raw.enm_dmg;
			range=80;
			break;

		case 3: // 3.쇠사슬병사(3, 6, 3, ran, 쓰르릉, 으악)
			name = "Chainman";
			speed = 5;
			hp = 3;
			rand = new Random(System.currentTimeMillis()); // seed값을 배정하여 생성
			enm_degree = Math.abs(rand.nextInt(359)); // 0~359 ,
														// Math.abs(rand.nextInt(9)+1)
														// //1~10
			move_bgm_resid = R.raw.foot;
			dmg_sound_resid = R.raw.enm_dmg;
			dead_sound_resid = R.raw.enm_dmg;
			range=70;
			break;

		case 4: // 4.전기톱병사(4, 8, 5, ran, 덜덜덜, 으악)
			name = "Sawman";
			speed = 8;
			hp = 5;
			rand = new Random(System.currentTimeMillis()); // seed값을 배정하여 생성
			enm_degree = Math.abs(rand.nextInt(359)); // 0~359 ,
														// Math.abs(rand.nextInt(9)+1)
														// //1~10
			move_bgm_resid = R.raw.foot;
			dmg_sound_resid = R.raw.enm_dmg;
			dead_sound_resid = R.raw.enm_dmg;
			range=60;
			break;

		default:
			Log.i("error", "error : Enermy 객체 만들때 1~4아닌수 들어옴.");
			return;
		}
	}// Enermy 생성자
	
	public int getMove_bgm_resid(){
		return this.move_bgm_resid;
	}

	public int getDmg_sound_resid(){
		return this.dmg_sound_resid;
	}
	public int getDead_sound_resid(){
		return this.dead_sound_resid;
	}
	public void downHp(){
		this.hp = --hp;
	}
	public boolean checkDead(){
		if (hp <=0)
			return true;
		else return false;
	}
	public float getEnm_degree(){
		return this.enm_degree;
	}
	public int getSpeed(){
		return this.speed;
	}
	public int getRange(){
		return this.range;
	}
	
}
