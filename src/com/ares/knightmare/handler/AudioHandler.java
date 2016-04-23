package com.ares.knightmare.handler;

import java.nio.FloatBuffer;
import java.util.ArrayList;
import java.util.List;

import org.lwjgl.BufferUtils;
import org.lwjgl.openal.AL10;
import org.lwjgl.openal.AL11;
import org.lwjgl.openal.ALContext;
import org.lwjgl.openal.ALDevice;

import com.ares.knightmare.entities.Source;
import com.ares.knightmare.util.WaveData;

public class AudioHandler {

	private ALContext context;
	private ALDevice device;

	private List<Integer> buffers = new ArrayList<>();
	private List<Source> sources = new ArrayList<>();//TODO maybe add a reference

	public AudioHandler() {
		context = ALContext.create();
		device = context.getDevice();
		AL10.alDistanceModel(AL11.AL_LINEAR_DISTANCE_CLAMPED);//TODO more realistic
	}

	public int loadSound(String file) {
		int buffer = AL10.alGenBuffers();
		buffers.add(buffer);
		WaveData waveFile = WaveData.create(file);
		AL10.alBufferData(buffer, waveFile.format, waveFile.data, waveFile.samplerate);
		waveFile.dispose();
		return buffer;
	}

	public void setListenerData(float x, float y, float z, float rotX, float rotY, float rotZ) {
		AL10.alListener3f(AL10.AL_POSITION, x, y, z);
		FloatBuffer listenerOri = (FloatBuffer) BufferUtils.createFloatBuffer(6).put(new float[] { rotY/-90, 0.0f, 1.0f, 0.0f, -1.0f, 0.0f }).rewind();//TODO fix
		AL10.alListenerfv(AL10.AL_ORIENTATION, listenerOri);
		AL10.alListener3f(AL10.AL_VELOCITY, 0, 0, 0);//TODO maybe use some day
	}

	public void cleanUp() {
		for (Source source : sources) {
			source.delete();
		}
		for (int buffer : buffers) {
			AL10.alDeleteBuffers(buffer);
		}
		device.destroy();
		context.destroy();
	}
	
	public Source generateSource(float volume){
		Source source = new Source();
		source.setVolume(volume);
		source.setPosition(0, 0, 0);
		sources.add(source);
		return source;
	}
}
