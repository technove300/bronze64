package bronze64.util;

public class Time {
    public static float timeStarted = System.nanoTime();

    public static float getTime() {
        return (float)((System.nanoTime() - timeStarted) * 1e-9);
    }
    
}
