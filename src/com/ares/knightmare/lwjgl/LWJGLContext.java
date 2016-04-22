package com.ares.knightmare.lwjgl;

import static org.lwjgl.glfw.GLFW.GLFW_FALSE;
import static org.lwjgl.glfw.GLFW.GLFW_RESIZABLE;
import static org.lwjgl.glfw.GLFW.GLFW_TRUE;
import static org.lwjgl.glfw.GLFW.GLFW_VISIBLE;
import static org.lwjgl.glfw.GLFW.glfwCreateWindow;
import static org.lwjgl.glfw.GLFW.glfwDefaultWindowHints;
import static org.lwjgl.glfw.GLFW.glfwDestroyWindow;
import static org.lwjgl.glfw.GLFW.glfwGetPrimaryMonitor;
import static org.lwjgl.glfw.GLFW.glfwGetVideoMode;
import static org.lwjgl.glfw.GLFW.glfwInit;
import static org.lwjgl.glfw.GLFW.glfwMakeContextCurrent;
import static org.lwjgl.glfw.GLFW.glfwPollEvents;
import static org.lwjgl.glfw.GLFW.glfwSetErrorCallback;
import static org.lwjgl.glfw.GLFW.glfwSetKeyCallback;
import static org.lwjgl.glfw.GLFW.glfwSetWindowPos;
import static org.lwjgl.glfw.GLFW.glfwShowWindow;
import static org.lwjgl.glfw.GLFW.glfwSwapBuffers;
import static org.lwjgl.glfw.GLFW.glfwSwapInterval;
import static org.lwjgl.glfw.GLFW.glfwTerminate;
import static org.lwjgl.glfw.GLFW.glfwWindowHint;
import static org.lwjgl.glfw.GLFW.glfwWindowShouldClose;
import static org.lwjgl.system.MemoryUtil.NULL;
import static org.lwjgl.opengl.GL11.*;

import org.lwjgl.Version;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.glfw.GLFWVidMode;
import org.lwjgl.opengl.GL;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;
import org.lwjgl.opengl.GL30;
import org.lwjgl.util.vector.Vector3f;
import org.lwjgl.util.vector.Vector4f;

import com.ares.knightmare.audio.AudioMaster;
import com.ares.knightmare.entities.Camera;
import com.ares.knightmare.entities.Overseher;
import com.ares.knightmare.fontRendering.TextMaster;
import com.ares.knightmare.handler.CameraHandler;
import com.ares.knightmare.listener.KeyListener;
import com.ares.knightmare.listener.MouseButtonListener;
import com.ares.knightmare.listener.MousePicker;
import com.ares.knightmare.listener.ScrollListener;
import com.ares.knightmare.postProcessing.Fbo;
import com.ares.knightmare.postProcessing.PostProcessing;
import com.ares.knightmare.listener.CursorPosListener;
import com.ares.knightmare.rendering.Loader;
import com.ares.knightmare.rendering.MasterRenderer;
import com.ares.knightmare.util.Level;
import com.ares.knightmare.util.WaterFrameBuffers;

import javafx.scene.image.PixelFormat;

public class LWJGLContext {

	private GLFWErrorCallback errorCallback;
	private KeyListener keyCallback;
	private CursorPosListener cursorPosCallback;
	private ScrollListener scrollCallback;
	private MouseButtonListener mouseButtonCallback;
	private long window;
	private int width, height;
	private CameraHandler cameraHandler;
	private Overseher overseher;
	private MasterRenderer renderer;
	private Loader loader = new Loader();
	private WaterFrameBuffers fbos;
	private Fbo fbo;

	public LWJGLContext(int width, int height) {
		this.width = width;
		this.height = height;
		try {
			init();

			loop();

			fbos.cleanUp();

			// Release window and window callbacks
			glfwDestroyWindow(window);
			keyCallback.release();
			cursorPosCallback.release();
			scrollCallback.release();
			mouseButtonCallback.release();
		} catch (Exception e) {
			e.printStackTrace(System.err);
		} finally {
			// Terminate GLFW and release the GLFWErrorCallback
			AudioMaster.cleanUp();
			PostProcessing.cleanUp();
			fbo.cleanUp();
			TextMaster.cleanUp();
			renderer.cleanUp();
			loader.cleanUp();
			glfwTerminate();
			errorCallback.release();
		}
	}

	private void init() {
		// Setup an error callback. The default implementation
		// will print the error message in System.err.
		glfwSetErrorCallback(errorCallback = GLFWErrorCallback.createPrint(System.err));// TODO
																						// maybe
																						// own

		// Initialize GLFW. Most GLFW functions will not work before doing this.
		if (glfwInit() != GLFW_TRUE)
			throw new IllegalStateException("Unable to initialize GLFW");

		// Configure our window
		glfwDefaultWindowHints(); // optional, the current window hints are
									// already the default
		glfwWindowHint(GLFW_VISIBLE, GLFW_FALSE); // the window will stay hidden
													// after creation
		glfwWindowHint(GLFW_RESIZABLE, GLFW_TRUE); // the window will be
													// resizable
		glfwWindowHint(GLFW.GLFW_SAMPLES, 16);// TODO count samples
		// Create the window
		window = glfwCreateWindow(width, height, "Knightmare", NULL, NULL);
		if (window == NULL) {
			throw new RuntimeException("Failed to create the GLFW window");
		}

		overseher = new Overseher(width, height);
		overseher.move(0, 20, 0);
		cameraHandler = new CameraHandler(overseher);

		// Get the resolution of the primary monitor
		GLFWVidMode vidmode = glfwGetVideoMode(glfwGetPrimaryMonitor());
		// Center our window
		glfwSetWindowPos(window, (vidmode.width() - width) / 2, (vidmode.height() - height) / 2);

		// Make the OpenGL context current
		glfwMakeContextCurrent(window);
		// Enable v-sync
		glfwSwapInterval(1);

		glfwShowWindow(window);
		GL.createCapabilities();
		GL11.glEnable(GL13.GL_MULTISAMPLE);

		System.out.println("OS name " + System.getProperty("os.name"));
		System.out.println("OS version " + System.getProperty("os.version"));
		System.out.println("LWJGL version " + Version.getVersion());
		System.out.println("OpenGL version " + glGetString(GL_VERSION));

		fbos = new WaterFrameBuffers(width, height);
		fbo = new Fbo(width, height, Fbo.DEPTH_RENDER_BUFFER);
		PostProcessing.init(loader);
		renderer = new MasterRenderer(width, height, loader, fbos, cameraHandler);
		TextMaster.init(loader);
		Level level = new Level(renderer, loader, fbos);

		// Setup a key callback. It will be called every time a key is pressed,
		// repeated or released.
		glfwSetKeyCallback(window, keyCallback = new KeyListener(cameraHandler, level));
		GLFW.glfwSetCursorPosCallback(window, cursorPosCallback = new CursorPosListener(cameraHandler));
		GLFW.glfwSetScrollCallback(window, scrollCallback = new ScrollListener(cameraHandler));
		GLFW.glfwSetMouseButtonCallback(window,
				mouseButtonCallback = new MouseButtonListener(cameraHandler, new MousePicker(renderer.getProjectionMatrix(), width, height), level));
	}

	private void loop() {
		// Run the rendering loop until the user has attempted to close
		// the window or has pressed the ESCAPE key.
		while (glfwWindowShouldClose(window) == GLFW_FALSE) {
			GL11.glEnable(GL30.GL_CLIP_DISTANCE0);

			Camera camera = cameraHandler.getCamera();
			Vector3f camP = camera.getPosition();
			AudioMaster.setListenerData(camP.x, camP.y, camP.z);//TODO try to move
			Camera reflectionCam = camera.clone();

			// Shadow Map
			renderer.renderShadowMap(camera);

			// Reflection
			fbos.bindReflectionFrameBuffer();
			float distance = 2 * (camera.getPosition().y - 0);// TODO water
																// hight = 0
			reflectionCam.getPosition().y -= distance;
			reflectionCam.invertPitch();
			renderer.prepare();
			renderer.renderScene(reflectionCam, new Vector4f(0, 1, 0, 0.2f));

			// Refraction
			fbos.bindRefractionFrameBuffer();
			renderer.prepare();
			renderer.renderScene(camera, new Vector4f(0, -1, 0, 0.5f));

			// Post processing fbo
			fbos.unbindCurrentFrameBuffer();
			fbo.bindFrameBuffer();
			GL11.glDisable(GL30.GL_CLIP_DISTANCE0);
			renderer.prepare();
			renderer.renderScene(camera, new Vector4f(0, -1, 0, 10000));
			renderer.renderWaterAndParticles(camera);
			fbo.unbindFrameBuffer();
			PostProcessing.doPostProcessing(fbo.getColourTexture());
			
			//Display
			renderer.renderGui();
			TextMaster.render();

			glfwSwapBuffers(window); // swap the color buffers

			// Poll for window events. The key callback above will only be
			// invoked during this call.
			glfwPollEvents();
		}
	}
}
