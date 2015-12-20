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

import org.lwjgl.glfw.GLFW;
import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.glfw.GLFWVidMode;
import org.lwjgl.opengl.GL;
import org.lwjgl.util.vector.Vector3f;

import com.ares.knightmare.entities.Camera;
import com.ares.knightmare.entities.Entity;
import com.ares.knightmare.entities.Light;
import com.ares.knightmare.graphics.Loader;
import com.ares.knightmare.graphics.MasterRenderer;
import com.ares.knightmare.graphics.OBJLoader;
import com.ares.knightmare.listener.KeyListener;
import com.ares.knightmare.listener.CursorPosListener;
import com.ares.knightmare.models.RawModel;
import com.ares.knightmare.models.TexturedModel;
import com.ares.knightmare.terrain.Terrain;
import com.ares.knightmare.textures.ModelTexture;

public class LWJGLContext {

	private GLFWErrorCallback errorCallback;
	private KeyListener keyCallback;
	private CursorPosListener cursorPosCallback;
	private Loader loader = new Loader();
	private static LWJGLContext context;
	private long window;
	private static int width, height;
	private Camera camera;
	private Entity entity, gras;
	private Light light;
	private Terrain terrain, terrain2;
	private MasterRenderer renderer;

	public static void createContext(int width, int height) {
		if (context == null) {
			LWJGLContext.width = width;
			LWJGLContext.height = height;
			context = new LWJGLContext();
		}
	}

	private LWJGLContext() {
		try {
			init();

			RawModel model = OBJLoader.loadObjModel("stall", loader);
			TexturedModel texturedModel = new TexturedModel(model, new ModelTexture(loader.loadTexture("stallTexture")));
			ModelTexture texture = texturedModel.getTexture();
			texture.setShineDamper(0);
			texture.setReflectivity(0);
			entity = new Entity(texturedModel, new Vector3f(0, 0, -25), 0, 160, 0, 1);

			RawModel modelg = OBJLoader.loadObjModel("fern", loader);
			TexturedModel texturedModelg = new TexturedModel(modelg, new ModelTexture(loader.loadTexture("fern")));
			ModelTexture textureg = texturedModelg.getTexture();
			textureg.setShineDamper(0);
			textureg.setReflectivity(0);
			textureg.setHasTransparency(true);
			gras = new Entity(texturedModelg, new Vector3f(0, 0, -50), 0, 160, 0, 1);
			
			light = new Light(new Vector3f(3000, 2000, 2000), new Vector3f(1, 1, 1));//TODO
			
			terrain = new Terrain(0, -1, loader, new ModelTexture(loader.loadTexture("gras")));
			terrain2 = new Terrain(1, -1, loader, new ModelTexture(loader.loadTexture("gras")));

			loop();

			// Release window and window callbacks
			glfwDestroyWindow(window);
			keyCallback.release();
			cursorPosCallback.release();
		} finally {
			// Terminate GLFW and release the GLFWErrorCallback
			renderer.cleanUp();
			loader.cleanUp();
			glfwTerminate();
			errorCallback.release();
		}
	}

	private void init() {
		// Setup an error callback. The default implementation
		// will print the error message in System.err.
		glfwSetErrorCallback(errorCallback = GLFWErrorCallback.createPrint(System.err));

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

		// Create the window
		window = glfwCreateWindow(width, height, "Knightmare", NULL, NULL);
		if (window == NULL) {
			throw new RuntimeException("Failed to create the GLFW window");
		}

		camera = new Camera(width, height);

		// Setup a key callback. It will be called every time a key is pressed,
		// repeated or released.
		glfwSetKeyCallback(window, keyCallback = new KeyListener(camera));
		GLFW.glfwSetCursorPosCallback(window, cursorPosCallback = new CursorPosListener(camera));

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

		renderer = new MasterRenderer(width, height);
	}

	private void loop() {
		// Run the rendering loop until the user has attempted to close
		// the window or has pressed the ESCAPE key.
		while (glfwWindowShouldClose(window) == GLFW_FALSE) {
			renderer.prepare();
			render();

			glfwSwapBuffers(window); // swap the color buffers

			// Poll for window events. The key callback above will only be
			// invoked during this call.
			glfwPollEvents();
		}
	}

	private void render() {
		// entity.increasePosition(0, 0, -0.01f); TODO
		// entity.rotate(0, 2, 0);
		renderer.processEntity(entity);
		renderer.processEntity(gras);
		renderer.processTerrain(terrain);
		renderer.processTerrain(terrain2);
		
		renderer.render(light, camera);
	}
}
