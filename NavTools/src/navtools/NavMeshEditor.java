package navtools;

import com.jme3.app.SimpleApplication;
import com.jme3.math.Vector3f;
import com.jme3.renderer.RenderManager;
import com.simsilica.lemur.GuiGlobals;
import com.simsilica.lemur.style.BaseStyles;

public class NavMeshEditor extends SimpleApplication {
    
    public static void main(String[] args) {
        NavMeshEditor app = new NavMeshEditor();
        //AppSettings settings = new AppSettings(true);
        //settings.setFullscreen(true);
        //app.setSettings(settings);
        app.start();
    }

    // Game Starts with Attaching the Game Manager
    @Override
    public void simpleInitApp() {
        setDisplayStatView(false);
        setShowSettings(false);
        setDisplayFps(false);
        flyCam.setMoveSpeed(10);
        cam.setLocation(new Vector3f(0,5,0));
        stateManager.attach(new AppManager());
        GuiGlobals.initialize(this);
        GuiGlobals.getInstance().getStyles().setDefaultStyle("glass");
        BaseStyles.loadGlassStyle();
    }

    @Override
    public void simpleUpdate(float tpf) {
    }

    @Override
    public void simpleRender(RenderManager rm) {
        //TODO: add render code
    }

}
