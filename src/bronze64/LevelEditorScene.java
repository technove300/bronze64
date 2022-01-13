package bronze64;

import java.awt.event.KeyEvent;

public class LevelEditorScene extends Scene {

    private boolean changingScene = false;
    private float timetochangeScene = 2.0f;

    public LevelEditorScene() {
        System.out.println("inside level editor scene!");
        Window.get().r = 1;
        Window.get().g = 1;
        Window.get().b = 1;
    }
    
    @Override
    public void update(float dt) {

        System.out.println("" + (1.0f / dt) + " FPS");

        if(!changingScene && Keyboard.iskeyPressed(KeyEvent.VK_SPACE)) {
            
            changingScene = true;

        }
        
        if(changingScene && timetochangeScene > 0){

            timetochangeScene -= dt;
            Window.get().r -= dt * 5.0f;
            Window.get().g -= dt * 5.0f;
            Window.get().b -= dt * 5.0f;

        }   else if (changingScene){
            Window.changeScene(1);
        }
    }

}
