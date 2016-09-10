/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package navtools.ai;

import com.jme3.app.state.AppStateManager;
import com.jme3.scene.Node;
import java.util.ArrayList;
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
    
    }
    
    public NavigationNode(Node navNode) {
        this.loadedNode = navNode;
        initialize();
    }
    
    public void setDebug(boolean newVal, AppStateManager stateManager) {
        
        debug = true;
        
        if (debug) {
            //((SimpleApplication) stateManager.getApplication()).getRootNode().attachChild(loadedNode);
        }
        
        else {
            //loadedNode.removeFromParent();
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
        
        attachModels();
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
    
        for (int i = 0; i < getChildren().size(); i++) {
            WayPoint  wp       = (WayPoint) getChild(i);
            System.out.println(wp.getName() + ": " + wp.getNeighbors());
        }
        
    }
    
}
