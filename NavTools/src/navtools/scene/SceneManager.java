/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package navtools.scene;

import com.jme3.app.Application;
import com.jme3.app.SimpleApplication;
import com.jme3.light.AmbientLight;
import com.jme3.math.ColorRGBA;
import com.jme3.scene.Node;
import navtools.AppManager;
import navtools.util.FileWalker;

/**
 *
 * @author root
 */
public class SceneManager  {
    
    private final SimpleApplication   app;
    private final FileWalker          fw;
    private final MeshEditControl     mec;
    private final WayPointEditControl wec;
    private Node                      scene;
    private Node                      navNode;
    
    public SceneManager(Application app) {
        this.app = (SimpleApplication) app;
        fw       = new FileWalker();
        createLight();
        loadScene("Town");
        initNavNode();
        mec = new MeshEditControl(this.app, scene);
        wec = new WayPointEditControl(this.app, scene);
    }
    
    private void loadScene(String path) {
        
        String[] p = path.split("/");
        path       = p[p.length-1];
        path       = path.split("\\.")[0];
        path       = fw.walk("assets/Scenes", path);
        path       = path.split("assets/")[1];
        path       = path.replace("data", "");
        scene      = (Node) app.getAssetManager().loadModel(path);
        app.getRootNode().attachChild(scene);
        
    }
    
    private void createLight() {
        AmbientLight light = new AmbientLight();
        light.setColor(ColorRGBA.White);
        app.getRootNode().addLight(light);
        
    }
    
    private void initNavNode() {
        
        if (scene.getChild("Navigation Node") == null) {
            navNode = new Node();
            navNode.setName("Navigation Node");
            scene.attachChild(navNode);
        }
        
        else {
            navNode = (Node) scene.getChild("Navigation Node");
        }
        
    }
    
    public Node getNavNode() {
        return navNode;
    }
    
    public void onClick() {
        
        String mode = app.getStateManager().getState(AppManager.class).getGui().getMode();
        
        switch(mode) {
        
            case "Mesh":
                mec.click();
                break;
            
            case "WayPoint":
                wec.click();
                break;
                
        }
        
    }
    
    public void onRightClick() {
        
        String mode = app.getStateManager().getState(AppManager.class).getGui().getMode();
        
        switch(mode) {
        
            case "Mesh":
                mec.rightClick();
                break;
                
            case "WayPoint":
                wec.rightClick();
                break;                
            
        }
        
    } 
    
    public void changeMode() {
        wec.changeMode();
    }
    
    public void update(float tpf) {
    
    }
    
}
