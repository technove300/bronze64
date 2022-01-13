package bronze64;

import org.lwjgl.*;
import org.lwjgl.glfw.*;
import org.lwjgl.system.*;
import org.lwjgl.opengl.*;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.system.MemoryUtil.*;

import java.nio.IntBuffer;
import java.util.Random;

import static org.lwjgl.system.MemoryStack.*;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.glfw.Callbacks.*;






public class Window {

    private int width, height;
    private String title;
    private long glfwWindow;
    private static Window window = null;
    private long frameTimer = 0;
    private long slowTimer = 0;

    private float r, g, b, a;
    
    private Window() {
        
        this.width = 1024;
        this.height = 576;
        this.title = "bronze!";

    }

    public static Window get() {
        
        if (Window.window == null){
            Window.window = new Window();
        }

        return Window.window;
    }

    public void run() {
        System.out.println("lwjgl" + Version.getVersion() + "!" );


        init();
        loop();

        // Free the window callbacks and destroy the window
		glfwFreeCallbacks(glfwWindow);
		glfwDestroyWindow(glfwWindow);

		// Terminate GLFW and free the error callback
		glfwTerminate();
		glfwSetErrorCallback(null).free();

    }

    public void init () {
        //setup error callback
        GLFWErrorCallback.createPrint(System.err).set();
        
        if (!GLFW.glfwInit()){
            throw new IllegalStateException("unable to initialize glfw!");
        }

        glfwDefaultWindowHints();
        glfwWindowHint(GLFW_VISIBLE, GLFW_FALSE);
        glfwWindowHint(GLFW_RESIZABLE, GLFW_TRUE);
        glfwWindowHint(GLFW_MAXIMIZED, GLFW_FALSE);


        //create window

        glfwWindow = glfwCreateWindow(this.width, this.height, this.title, NULL, NULL);
        if (glfwWindow == NULL){
            throw new IllegalStateException("failed to create window!");
        }

        glfwSetCursorPosCallback(glfwWindow, ListenMouse::cursorPosCallback);
        glfwSetMouseButtonCallback(glfwWindow, ListenMouse::mouseButtonCallback);
        glfwSetScrollCallback(glfwWindow, ListenMouse::mousescrollCallback);
        glfwSetKeyCallback(glfwWindow, ListenKey::keyCallback);



        /* Setup a key callback. It will be called every time a key is pressed, repeated or released.
		glfwSetKeyCallback(glfwWindow, (glfwWindow, key, scancode, action, mods) -> {
			if ( key == GLFW_KEY_ESCAPE && action == GLFW_RELEASE )
				glfwSetWindowShouldClose(glfwWindow, true); // We will detect this in the rendering loop
		});
        */

		// Get the thread stack and push a new frame
		try ( MemoryStack stack = stackPush() ) {
			IntBuffer pWidth = stack.mallocInt(1); // int*
			IntBuffer pHeight = stack.mallocInt(1); // int*

			// Get the window size passed to glfwCreateWindow
			glfwGetWindowSize(glfwWindow, pWidth, pHeight);

			// Get the resolution of the primary monitor
			GLFWVidMode vidmode = glfwGetVideoMode(glfwGetPrimaryMonitor());

			// Center the window
			glfwSetWindowPos(
				glfwWindow,
				(vidmode.width() - pWidth.get(0)) / 2,
				(vidmode.height() - pHeight.get(0)) / 2
			);
		} // the stack frame is popped automatically  


        // make the opengl context current
        glfwMakeContextCurrent(glfwWindow);
        // v-sync
        glfwSwapInterval(1);

        //make the window visible
        glfwShowWindow(glfwWindow);



        // This line is critical for LWJGL's interoperation with GLFW's
		// OpenGL context, or any context that is managed externally.
		// LWJGL detects the context that is current in the current thread,
		// creates the GLCapabilities instance and makes the OpenGL
		// bindings available for use.
        GL.createCapabilities();

    }

    public void loop() {
        while (!glfwWindowShouldClose(glfwWindow)) {
            glfwPollEvents();
            
            
            
            glClearColor(r, g, b, a);
            glClear(GL_COLOR_BUFFER_BIT);

            if (ListenKey.iskeyPressed(GLFW_KEY_SPACE)) {
                System.out.println("space key pressed!!");
            }

            if(String.valueOf(frameTimer).contains("0") && slowTimer < 1000) {
                r = new Random().nextFloat();
                g = new Random().nextFloat();
                b = new Random().nextFloat();
                
            }

            System.out.println(frameTimer);





            if(frameTimer >= 9){
                frameTimer = 0;
            } else{
                frameTimer++;
            }
            
            if(String.valueOf(frameTimer).contains("0")){
                slowTimer++;
            }
            glfwSwapBuffers(glfwWindow);
            

        }

    }
}
