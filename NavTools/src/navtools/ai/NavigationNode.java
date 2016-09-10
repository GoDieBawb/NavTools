/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package navtools.ai;

import com.jme3.app.SimpleApplication;
import com.jme3.app.state.AppStateManager;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.shape.Sphere;
import java.util.HashMap;

/**
 *
 * @author root
 */
public class NavigationNode extends Node {
    
    private Node                  loadedNode;
    private HashMap<String, Node> sceneNodes;
    private boolean debug;
    
    public NavigationNode() {
        name = "Navigation Node";
    }
    
    public NavigationNode(Node navNode) {
        name = "Navigation Node";
        this.loadedNode = navNode;
        initialize();
    }
    
    public void setDebug(boolean newVal, AppStateManager stateManager) {
        
        debug = newVal;
        
        if (debug) {
            showBalls(stateManager);
        }
        
        else {
            hideBalls();
        }
        
    }
    
    private void showBalls(AppStateManager stateManager) {
        
        for (int i = 0; i < getChildren().size(); i++) {
        
            WayPoint  wp       = (WayPoint) getChild(i);
            Sphere   s         = new Sphere(30, 30, .2f);
            Geometry geom      = new Geometry("Sphere", s);
            Material m         = new Material(stateManager.getApplication().getAssetManager(), "Common/MatDefs/Misc/Unshaded.j3md");
            geom.setMaterial(m);
            m.setColor("Color", ColorRGBA.Red);
            wp.attachChild(geom);
        }
        
    }
    
    private void hideBalls() {
        
        for (int i = 0; i < getChildren().size(); i++) {
            WayPoint  wp       = (WayPoint) getChild(i);
            wp.detachAllChildren();
            
        }
    }    
    
    private void initialize() {
        
        sceneNodes = new HashMap();
        
        for (int i = 0; i < loadedNode.getChildren().size(); i++) {
            
            WayPoint wayPoint  = new WayPoint();
            Node currentNode   = (WayPoint) loadedNode.getChild(i);
            String    data     = currentNode.getUserData("Neighbors");
            
            sceneNodes.put(currentNode.getName(), currentNode);
            
            wayPoint.setName(currentNode.getName());
            wayPoint.setUserData("Neighbors", data);
            wayPoint.setLocalTranslation(currentNode.getLocalTranslation());
            attachChild(wayPoint);
            
        }
        
        //attachModels();
        initNeighbors();
        loadedNode.removeFromParent();
        
    }
    
    private void attachModels() {
    
        for (int i = 0; i < getChildren().size(); i++) {
            
            WayPoint wp   = (WayPoint) getChild(i);
            Node     node = sceneNodes.get(wp.getName());
            wp.attachChild(node);
            node.setLocalTranslation(0,0,0);
            
        }
        
    }
    
    private void initNeighbors() {
        
        for (int i = 0; i < getChildren().size(); i++) {
        
            WayPoint  wp       = (WayPoint) getChild(i);
            String    data     = wp.getUserData("Neighbors");
            
            for (int j = 0; j < getChildren().size(); j++) {

                WayPoint neighbor   = (WayPoint) getChild(j);

                if (data.contains(neighbor.getName())) {
                    wp.getNeighbors().add(neighbor);
                }

            }              
        
        }
            
    }
    
    public void debugNeighbors() {
    
        System.out.println("Debugging Neighbors...");
        
        for (int i = 0; i < getChildren().size(); i++) {
            WayPoint  wp       = (WayPoint) getChild(i);
            System.out.println(wp.getName() + ": " + wp.getNeighbors());
        }
        
    }
    
}
