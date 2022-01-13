package bronze64;

import static org.lwjgl.glfw.GLFW.*;

public class ListenKey {
    private static ListenKey instance;
    private boolean keyPressed[] = new boolean[350];

    private ListenKey(){

    }

    public static ListenKey get() {
        if(ListenKey.instance == null) {
            ListenKey.instance = new ListenKey();
        }
        return ListenKey.instance;
    }


    public static void keyCallback(long window, int key, int scancode, int action, int mods) {
        if (action == GLFW_PRESS){
            get().keyPressed[key] = true;
        } else if(action == GLFW_RELEASE) {
            get().keyPressed[key] = false;
        }
    }
    

    public static boolean iskeyPressed(int scancode){
        if(scancode < get().keyPressed.length) {
            return get().keyPressed[scancode];
        } else {
            System.err.println("scancode out of bounds!!");
            return false;
        }
    } 
}
