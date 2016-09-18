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
import com.simsilica.lemur.TextField;
import java.util.ArrayList;
import navtools.ai.NavigationNode;
import navtools.ai.WayPoint;
import navtools.editor.EditControlManager;
import navtools.util.FileWalker;
import org.lwjgl.opengl.Display;

/**
 *
 * @author root
 */
public class SceneManager  {
    
    private final SimpleApplication   app;
    private final FileWalker          fw;
    private final EditControlManager  ecm;
    private Node                      scene;
    private NavigationNode            navNode;
    private String                    sceneName;
    
    public SceneManager(Application app) {
        this.app = (SimpleApplication) app;
        fw       = new FileWalker();
        ecm      = new EditControlManager(this.app, this);
        createLight();
    }
    
    public void loadScene(String path) throws Exception {
        
        scene      = (Node) app.getAssetManager().loadModel(path);
        String[] p = path.split("/");
        path       = p[p.length-1];
        path       = path.split("\\.")[0];
        path       = fw.walk("assets/", path);
        path       = path.split("assets/")[1];
        path       = path.replace("data", "");
        sceneName  = path;
        app.getRootNode().attachChild(scene);
        initNavNode();
        
    }
    
    private void createLight() {
        AmbientLight light = new AmbientLight();
        light.setColor(ColorRGBA.White);
        app.getRootNode().addLight(light);
        
    }
    
    private void initNavNode() {
        
        if (scene.getChild("Navigation Node") == null) {
            navNode = new NavigationNode();
            navNode.setName("Navigation Node");
            scene.attachChild(navNode);
        }
        
        else {
           Node sceneNavNode = (Node) scene.getChild("Navigation Node");
           navNode           = new NavigationNode(sceneNavNode);
           navNode.setDebug(true, app.getStateManager());
           app.getRootNode().attachChild(navNode);
        }
        
    }
    
    public EditControlManager getControlManager() {
        return ecm;
    }
    
    public NavigationNode getNavNode() {
        return navNode;
    }
    
    public String getSceneName() {
        return sceneName;
    }
    
    public Node getScene() {
        return scene;
    }
    
    public void update(float tpf) {
        ecm.update(tpf);
    }
    
    public void sortWayPoints() {
        char    letter             = 'A';
        WayPoint startPoint        = (WayPoint) navNode.getChild(0);
        ArrayList<WayPoint> sorted = new ArrayList();
        nameNeighbors(startPoint, letter, sorted);
        addNeighborData();
    }    
    
    private void nameNeighbors(WayPoint point, char letter, ArrayList<WayPoint> sorted) {

        for (WayPoint neighbor: point.getNeighbors()) {
            
            if (!sorted.contains(neighbor)) {
                sorted.add(neighbor);
                neighbor.setName(Character.toString(letter));
                letter++;
                nameNeighbors(neighbor, letter,sorted);
            }
            
        }
        
    }
    
    public void addNeighborData() {
        
        for (int i = 0; i < navNode.getChildren().size(); i++) {
            
            String data = "";
            WayPoint wp = (WayPoint) navNode.getChildren().get(i);
            
            for (WayPoint neighbor: wp.getNeighbors()) {
                data = data + neighbor.getName() + " ";
            }
            
            wp.setUserData("Neighbors", data);
            
        }
        
    } 
    
}
