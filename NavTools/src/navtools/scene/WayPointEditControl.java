/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package navtools.scene;

import com.jme3.app.SimpleApplication;
import com.jme3.collision.CollisionResults;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Ray;
import com.jme3.renderer.Camera;
import com.jme3.scene.Node;
import navtools.mesh.WayPoint;

/**
 *
 * @author root
 */
public class WayPointEditControl {
    
    private final SimpleApplication app;
    private WayPoint          selectedWayPoint;
    private Node              scene;
    private Material          red, green, yellow;
    
    public WayPointEditControl(SimpleApplication app, Node scene) {
        this.app   = app;
        this.scene = scene;
        initMaterials();
    }
    
    private void initMaterials() {
        
        yellow   = new Material(app.getAssetManager(), "Common/MatDefs/Misc/Unshaded.j3md");
        green    = new Material(app.getAssetManager(), "Common/MatDefs/Misc/Unshaded.j3md");
        red      = new Material(app.getAssetManager(), "Common/MatDefs/Misc/Unshaded.j3md");
        
        yellow.setColor("Color", ColorRGBA.Yellow);
        green.setColor("Color", ColorRGBA.Green);
        red.setColor("Color", ColorRGBA.Red);
        
    }
    
    public void changeMode() {
        selectedWayPoint = null;
        colorNeighbors();
    }
    
    public void setScene(Node newScene) {
        scene = newScene;
    }
    
    public void click() {
        
        Node     navNode         = (Node) scene.getChild("Navigation Node");
        Camera   cam             = app.getCamera();
        Ray      ray             = new Ray(cam.getLocation(), cam.getDirection());
        CollisionResults results = new CollisionResults();
        
        navNode.collideWith(ray, results);
        
        if (results.getClosestCollision() != null) {
            selectedWayPoint = (WayPoint) results.getClosestCollision().getGeometry().getParent();
            colorNeighbors();
        }        
        
    }
    
    public void rightClick() {
        
        Node     navNode         = (Node) scene.getChild("Navigation Node");   
        Camera   cam             = app.getCamera();
        Ray      ray             = new Ray(cam.getLocation(), cam.getDirection());
        CollisionResults results = new CollisionResults();
        
        navNode.collideWith(ray, results);
        
        if (results.getClosestCollision() != null) {
            
            WayPoint wp = (WayPoint) results.getClosestCollision().getGeometry().getParent();
            
            if (selectedWayPoint == null) {
                return;
            }
            
            if (wp == selectedWayPoint)
                return;
            
            if (selectedWayPoint.getNeighbors().contains(wp)) {
                selectedWayPoint.getNeighbors().remove(wp);
                wp.setMaterial(red);
            }
            
            else {
                selectedWayPoint.getNeighbors().add(wp);
                wp.setMaterial(green);
            }
            
        }        
    
    }    
    
    private void colorNeighbors() {

        Node navNode = (Node) scene.getChild("Navigation Node");
        
        for (int i = 0; i < navNode.getChildren().size(); i++) {
            
            WayPoint wp = (WayPoint) navNode.getChild(i);

            if (selectedWayPoint == null) {
                wp.setMaterial(red);
                continue;
            }
            
            if (wp == selectedWayPoint)
                wp.setMaterial(yellow);
            else if (selectedWayPoint.getNeighbors().contains(wp))
                wp.setMaterial(green);
            else
                wp.setMaterial(red);
            
        }
        
    } 
    
}
