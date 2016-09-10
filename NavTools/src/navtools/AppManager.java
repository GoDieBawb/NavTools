/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package navtools;

import navtools.scene.SceneManager;
import navtools.util.ControlListener;
import navtools.util.InteractionManager;
import navtools.gui.Gui;
import com.jme3.app.Application;
import com.jme3.app.SimpleApplication;
import com.jme3.app.state.AbstractAppState;
import com.jme3.app.state.AppStateManager;

/**
 *
 * @author root
 */
public class AppManager extends AbstractAppState {
    
    private SimpleApplication  app;
    private Gui                gui;
    private ControlListener    cl;
    private InteractionManager im;    
    private SceneManager       sm;
    
    @Override
    public void initialize(AppStateManager stateManager, Application app) {
        super.initialize(stateManager, app);
        this.app = (SimpleApplication) app; 
        sm       = new SceneManager(app);
        gui      = new Gui(this.app);
        im       = new InteractionManager(this.app);
        cl       = new ControlListener(this.app, im);  
    }
    
    public SceneManager getSceneManager() {
        return sm;
    }
    
    public Gui getGui() {
        return gui;
    }
    
    @Override
    public void update(float tpf) {
        cl.update();
        sm.update(tpf);
        gui.update(tpf);
    }
    
}
