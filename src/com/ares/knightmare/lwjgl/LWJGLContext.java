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

import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.glfw.GLFWKeyCallback;
import org.lwjgl.glfw.GLFWVidMode;
import org.lwjgl.opengl.GL;

public class LWJGLContext {

	private GLFWErrorCallback errorCallback;
	private GLFWKeyCallback keyCallback;

	private static LWJGLContext context;

	private long window;

	private static int width, heigth;

	public static void createContext(int width, int height) {
		if (context == null) {
			LWJGLContext.width = width;
			LWJGLContext.heigth = height;
			context = new LWJGLContext();
		}
	}

	private LWJGLContext() {
		try {
			init();
			loop();

			// Release window and window callbacks
			glfwDestroyWindow(window);
			keyCallback.release();
		} finally {
			// Terminate GLFW and release the GLFWErrorCallback
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
		window = glfwCreateWindow(width, heigth, "Knightmare", NULL, NULL);
		if (window == NULL)
			throw new RuntimeException("Failed to create the GLFW window");

		// Setup a key callback. It will be called every time a key is pressed,
		// repeated or released.
		glfwSetKeyCallback(window, keyCallback = new KeyListener());

		// Get the resolution of the primary monitor
		GLFWVidMode vidmode = glfwGetVideoMode(glfwGetPrimaryMonitor());
		// Center our window
		glfwSetWindowPos(window, (vidmode.width() - width) / 2, (vidmode.height() - heigth) / 2);

		// Make the OpenGL context current
		glfwMakeContextCurrent(window);
		// Enable v-sync
		glfwSwapInterval(1);

		glfwShowWindow(window);
		GL.createCapabilities();
		glClearColor(0.0f, 0.0f, 0.0f, 0.0f);
	}

	private void loop() {
		// Run the rendering loop until the user has attempted to close
		// the window or has pressed the ESCAPE key.
		while (glfwWindowShouldClose(window) == GLFW_FALSE) {
			glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT); // clear the
																// framebuffer

			render();
			
			glfwSwapBuffers(window); // swap the color buffers

			// Poll for window events. The key callback above will only be
			// invoked during this call.
			glfwPollEvents();
		}
	}

	private void render() {
		
		//TODO
	}
}
