package com.ares.knightmare.audio;

import java.util.ArrayList;
import java.util.List;

import org.lwjgl.openal.AL10;
import org.lwjgl.openal.AL11;
import org.lwjgl.openal.ALContext;
import org.lwjgl.openal.ALDevice;

public class AudioMaster {

	private static ALContext context;
	private static ALDevice device;

	private static List<Integer> buffers = new ArrayList<>();
	private static List<Source> sources = new ArrayList<>();

	public static void init() {
		context = ALContext.create();
		device = context.getDevice();
		AL10.alDistanceModel(AL11.AL_LINEAR_DISTANCE_CLAMPED);//TODO more realistic
	}

	public static int loadSound(String file) {
		int buffer = AL10.alGenBuffers();
		buffers.add(buffer);
		WaveData waveFile = WaveData.create(file);
		AL10.alBufferData(buffer, waveFile.format, waveFile.data, waveFile.samplerate);
		waveFile.dispose();
		return buffer;
	}

	public static void setListenerData(float x, float y, float z) {
		AL10.alListener3f(AL10.AL_POSITION, x, y, z);
		AL10.alListener3f(AL10.AL_VELOCITY, 0, 0, 0);//TODO maybe use some day
	}

	public static void cleanUp() {
		for (Source source : sources) {
			source.delete();
		}
		for (int buffer : buffers) {
			AL10.alDeleteBuffers(buffer);
		}
		device.destroy();
		context.destroy();
	}
	
	public static Source generateSource(float volume){
		Source source = new Source();
		source.setVolume(volume);
		source.setPosition(0, 0, 0);
		sources.add(source);
		return source;
	}
}
