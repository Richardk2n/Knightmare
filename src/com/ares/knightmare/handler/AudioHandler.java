package com.ares.knightmare.handler;

import java.nio.FloatBuffer;
import java.util.ArrayList;
import java.util.List;

import org.lwjgl.BufferUtils;
import org.lwjgl.openal.AL10;
import org.lwjgl.openal.AL11;
import org.lwjgl.openal.ALContext;
import org.lwjgl.openal.ALDevice;

import com.ares.knightmare.util.WaveData;

public class AudioHandler {

	private ALContext context;
	private ALDevice device;

	private List<Integer> buffers = new ArrayList<>();
	private List<Integer> sources = new ArrayList<>();//TODO maybe add a reference

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
		for (int source : sources) {
			delete(source);
		}
		for (int buffer : buffers) {
			AL10.alDeleteBuffers(buffer);
		}
		device.destroy();
		context.destroy();
	}
	
	public int generateSource(float volume){
		int sourceId = AL10.alGenSources();
		
		AL10.alSourcef(sourceId, AL10.AL_ROLLOFF_FACTOR, 1);//TODO move
		AL10.alSourcef(sourceId, AL10.AL_REFERENCE_DISTANCE, 50);
		AL10.alSourcef(sourceId, AL10.AL_MAX_DISTANCE, 100);
		setVolume(sourceId, volume);
		setPosition(sourceId, 0, 0, 0);//TODO use
		sources.add(sourceId);
		return sourceId;
	}

	public void play(int sourceId, int buffer) {
		stop(sourceId);//TODO change to fade
		AL10.alSourcei(sourceId, AL10.AL_BUFFER, buffer);
		AL10.alSourcePlay(sourceId);
	}
	
	public void pause(int sourceId){
		AL10.alSourcePause(sourceId);
	}
	
	public void continuePlaying(int sourceId){
		AL10.alSourcePlay(sourceId);
	}
	
	public void stop(int sourceId){
		AL10.alSourceStop(sourceId);
	}
	
	public void fade(int sourceId){
		//TODO
	}

	public void setVolume(int sourceId, float volume) {
		AL10.alSourcef(sourceId, AL10.AL_GAIN, volume);
	}

	public void setPitch(int sourceId, float pitch) {
		AL10.alSourcef(sourceId, AL10.AL_PITCH, pitch);
	}

	public void setPosition(int sourceId, float x, float y, float z) {
		AL10.alSource3f(sourceId, AL10.AL_POSITION, x, y, z);
	}

	public void setVelocity(int sourceId, float x, float y, float z) {
		AL10.alSource3f(sourceId, AL10.AL_VELOCITY, x, y, z);
	}

	public void setLooping(int sourceId, boolean loop) {
		AL10.alSourcei(sourceId, AL10.AL_LOOPING, loop ? AL10.AL_TRUE : AL10.AL_FALSE);
	}
	
	public boolean isPlaying(int sourceId){
		return AL10.alGetSourcei(sourceId, AL10.AL_SOURCE_STATE) == AL10.AL_PLAYING;
	}

	public void delete(int sourceId) {
		stop(sourceId);
		AL10.alDeleteSources(sourceId);
	}
}
