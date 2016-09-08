/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mygame;

import com.jme3.app.SimpleApplication;
import com.jme3.collision.CollisionResults;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Ray;
import com.jme3.renderer.Camera;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.shape.Sphere;

/**
 *
 * @author root
 */
public class MeshEditControl {
    
    private final SimpleApplication app;
    private       Node              scene;
    
    public MeshEditControl(SimpleApplication app, Node scene) {
        this.app = app;
        this.scene = scene;
    }
    
    public void setScene(Node newScene) {
        scene = newScene;
    }
    
    public void click() {
        
        Node     navNode         = (Node) scene.getChild("Navigation Node");
        WayPoint waypoint        = new WayPoint();
        Sphere   s               = new Sphere(30, 30, .2f);
        Geometry geom            = new Geometry("Sphere", s);
        Material m               = new Material(app.getAssetManager(), "Common/MatDefs/Misc/Unshaded.j3md");
        Camera   cam             = app.getCamera();
        Ray      ray             = new Ray(cam.getLocation(), cam.getDirection());
        CollisionResults results = new CollisionResults();
        
        m.setColor("Color", ColorRGBA.Red);
        geom.setMaterial(m);
        waypoint.attachChild(geom);
        
        app.getRootNode().collideWith(ray, results);
        
        if (results.getClosestCollision() != null) {
            navNode.attachChild(waypoint);
            waypoint.setLocalTranslation(results.getClosestCollision().getContactPoint());
        }        
        
    }
    
    public void rightClick() {
        
        Node     navNode         = (Node) scene.getChild("Navigation Node");   
        Camera   cam             = app.getCamera();
        Ray      ray             = new Ray(cam.getLocation(), cam.getDirection());
        CollisionResults results = new CollisionResults();
        
        navNode.collideWith(ray, results);
        
        if (results.getClosestCollision() != null) {
            results.getClosestCollision().getGeometry().getParent().removeFromParent();
        }        
    
    }    
    
}
