package bronze64;

import static org.lwjgl.glfw.GLFW.*;

public class Mouse {
    private static Mouse instance;
    private double xscroll, yscroll;
    private double xpos, ypos, ylast, xlast;
    private boolean mousebuttonPressed[] = new boolean[3];
    private boolean isDrag;


    private Mouse() {
        this.xscroll = 0.0;
        this.yscroll = 0.0;
        this.xpos = 0.0;
        this.ypos = 0.0;
        this.ylast = 0.0;
        this.xlast = 0.0;
    }

    public static Mouse get() {
        if (instance == null){
            instance = new Mouse();

        }
        return instance;
    }

    public static void cursorPosCallback(long window, double xpos, double ypos){
        get().xlast = get().xpos;
        get().ylast = get().ypos;
        get().xpos = xpos;
        get().ypos = ypos;
        get().isDrag = get().mousebuttonPressed[0] || get().mousebuttonPressed[1] || get().mousebuttonPressed[2];

    }

    public static void mouseButtonCallback(long window, int button, int action, int mod){
        if (action == GLFW_PRESS) {
            if (button < get().mousebuttonPressed.length) {
                get().mousebuttonPressed[button] = true;
            }
        } else if (action == GLFW_RELEASE) {
            if (button < get().mousebuttonPressed.length) {
                get().mousebuttonPressed[button] = false;
                get().isDrag = false;
            }
        }
    }


    public static void mousescrollCallback(long window, double xoffset, double yoffset){
        get().xscroll = xoffset;
        get().yscroll = yoffset;  
    }


    public static void endframe() {
        get().xscroll = 0;
        get().yscroll = 0;
        get().xlast = get().xpos;
        get().ylast = get().ypos;

    }


    public static float getX() {
        return (float)get().xpos;
    }

    public static float getY() {
        return (float)get().ypos;
    }
    
    public static float getDX() {
        return (float)(get().xlast - get().xpos);
    }

    public static float getDY() {
        return (float)(get().ylast - get().ypos);
    }

    public static float getScrollY() {
        return (float)get().yscroll;
    }

    public static float getScrollX() {
        return (float)get().xscroll;
    }

    public static boolean mousebuttonDown(int button){
        if (button < get().mousebuttonPressed.length) {
        return get().mousebuttonPressed[button];
        } else{
            return false;
        }
    }

    public static boolean isDragging() {
        return get().isDrag;
    }

}
