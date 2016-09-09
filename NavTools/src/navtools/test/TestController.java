/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package navtools.test;

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
public class TestController {
    
    private final Finder      finder;
    private SimpleApplication app;
    private Node              goal;
    private boolean           enabled;
    
    public TestController(SimpleApplication app, Node navNode) {
        this.app = app;
        finder = new Finder(app, navNode);
        initGoal();
    }
    
    private void initGoal() {
        
        goal                     = new Node("Goal");
        Sphere   s               = new Sphere(30, 30, .5f);
        Geometry geom            = new Geometry("Sphere", s);
        Material m               = new Material(app.getAssetManager(), "Common/MatDefs/Misc/Unshaded.j3md");
            m.setColor("Color", ColorRGBA.Pink);
        geom.setMaterial(m);
        goal.attachChild(geom);
        
    }
    
    public void click() {
   
        Camera   cam             = app.getCamera();
        Ray      ray             = new Ray(cam.getLocation(), cam.getDirection());
        CollisionResults results = new CollisionResults();        
        
        app.getRootNode().collideWith(ray, results);
        
        if (results.getClosestCollision() != null) {
            app.getRootNode().attachChild(goal);
            goal.setLocalTranslation(results.getClosestCollision().getContactPoint());
        }
        
    }
    
    public void rightClick() {
        
        Camera   cam             = app.getCamera();
        Ray      ray             = new Ray(cam.getLocation(), cam.getDirection());
        CollisionResults results = new CollisionResults();        
        
        app.getRootNode().collideWith(ray, results);
        
        if (results.getClosestCollision() != null) {
            app.getRootNode().attachChild(finder);
            finder.setLocalTranslation(results.getClosestCollision().getContactPoint());
        }
        
    }
    
    public void go() {
        finder.pathTo(goal);
        enabled = true;
    }
    
    public void stop() {
        goal.removeFromParent();
        finder.removeFromParent();
        enabled = false;
    }
    
    public void update(float tpf) {
        
        if (enabled)
            finder.update(tpf);
        
    }
    
}
