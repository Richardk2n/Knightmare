package com.ares.knightmare.handler;

import java.util.Timer;
import java.util.TimerTask;

public class TimeHandler {

	private static final int TIME_BETWEEN_TICKS = 10;
	
	public TimeHandler(EntityHandler chunkHandler){//TODO
		new Timer(true).scheduleAtFixedRate(new TimerTask() {
			
			@Override
			public void run() {
				chunkHandler.tick();
			}
		}, 0, TIME_BETWEEN_TICKS);
	}
}
