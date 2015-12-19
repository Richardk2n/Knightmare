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
import static org.lwjgl.opengl.GL11.GL_COLOR_BUFFER_BIT;
import static org.lwjgl.opengl.GL11.GL_DEPTH_BUFFER_BIT;
import static org.lwjgl.opengl.GL11.glClear;
import static org.lwjgl.opengl.GL11.glClearColor;
import static org.lwjgl.system.MemoryUtil.NULL;

import org.lwjgl.glfw.GLFW;
import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.glfw.GLFWVidMode;
import org.lwjgl.opengl.GL;
import org.lwjgl.opengl.GL11;
import org.lwjgl.util.vector.Vector3f;

import com.ares.knightmare.Loader;
import com.ares.knightmare.Renderer;
import com.ares.knightmare.entities.Camera;
import com.ares.knightmare.entities.Entity;
import com.ares.knightmare.listener.KeyListener;
import com.ares.knightmare.listener.CursorPosListener;
import com.ares.knightmare.models.RawModel;
import com.ares.knightmare.models.TexturedModel;
import com.ares.knightmare.shaders.Shaders;
import com.ares.knightmare.shaders.StaticShader;
import com.ares.knightmare.textures.ModelTexture;

public class LWJGLContext {

	private GLFWErrorCallback errorCallback;
	private KeyListener keyCallback;
	private CursorPosListener cursorPosCallback;
	private Loader loader = new Loader();

	private static LWJGLContext context;

	private long window;

	private static int width, height;
	
	private Renderer renderer;

	private Camera camera;
	
	
	private Entity entity;
	
	
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
			
			float[] vertices = {			
					-0.5f,0.5f,-0.5f,	
					-0.5f,-0.5f,-0.5f,	
					0.5f,-0.5f,-0.5f,	
					0.5f,0.5f,-0.5f,		
					
					-0.5f,0.5f,0.5f,	
					-0.5f,-0.5f,0.5f,	
					0.5f,-0.5f,0.5f,	
					0.5f,0.5f,0.5f,
					
					0.5f,0.5f,-0.5f,	
					0.5f,-0.5f,-0.5f,	
					0.5f,-0.5f,0.5f,	
					0.5f,0.5f,0.5f,
					
					-0.5f,0.5f,-0.5f,	
					-0.5f,-0.5f,-0.5f,	
					-0.5f,-0.5f,0.5f,	
					-0.5f,0.5f,0.5f,
					
					-0.5f,0.5f,0.5f,
					-0.5f,0.5f,-0.5f,
					0.5f,0.5f,-0.5f,
					0.5f,0.5f,0.5f,
					
					-0.5f,-0.5f,0.5f,
					-0.5f,-0.5f,-0.5f,
					0.5f,-0.5f,-0.5f,
					0.5f,-0.5f,0.5f
					
			};
			
			float[] textureCoords = {
					
					0,0,
					0,1,
					1,1,
					1,0,			
					0,0,
					0,1,
					1,1,
					1,0,			
					0,0,
					0,1,
					1,1,
					1,0,
					0,0,
					0,1,
					1,1,
					1,0,
					0,0,
					0,1,
					1,1,
					1,0,
					0,0,
					0,1,
					1,1,
					1,0

					
			};
			
			int[] indices = {
					0,1,3,	
					3,1,2,	
					4,5,7,
					7,5,6,
					8,9,11,
					11,9,10,
					12,13,15,
					15,13,14,	
					16,17,19,
					19,17,18,
					20,21,23,
					23,21,22

			};

			RawModel model = loader.loadToVAO(vertices, textureCoords, indices);
			TexturedModel texturedModel = new TexturedModel(model, new ModelTexture(loader.loadTexture("baum")));
			
			entity = new Entity(texturedModel, new Vector3f(0, 0, -1), 0, 0, 0, 1);
			
			loop();

			// Release window and window callbacks
			glfwDestroyWindow(window);
			keyCallback.release();
			cursorPosCallback.release();
		} finally {
			// Terminate GLFW and release the GLFWErrorCallback
			Shaders.cleanUp();
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
		if (window == NULL){
			throw new RuntimeException("Failed to create the GLFW window");}
		
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
		glClearColor(0.0f, 0.0f, 0.0f, 0.0f);
		
		Shaders.create();
		renderer = new Renderer((StaticShader) Shaders.staticShader, width, height);
	}

	private void loop() {
		// Run the rendering loop until the user has attempted to close
		// the window or has pressed the ESCAPE key.
		while (glfwWindowShouldClose(window) == GLFW_FALSE) {
			glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT); // clear the framebuffer
			GL11.glEnable(GL11.GL_DEPTH_TEST);	
			
			Shaders.staticShader.start();
			((StaticShader) Shaders.staticShader).loadViewMatrix(camera);
			render();
			Shaders.staticShader.stop();

			glfwSwapBuffers(window); // swap the color buffers

			// Poll for window events. The key callback above will only be
			// invoked during this call.
			glfwPollEvents();
		}
	}

	private void render() {
//		entity.increasePosition(0, 0, -0.01f); TODO
		entity.rotate(0, 2, 0);

		renderer.render(entity, (StaticShader) Shaders.staticShader);
	}
}
