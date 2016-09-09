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
    
    private final Node        finderNode;
    private final Node        navNode;
    private final Node        scene;
    private SimpleApplication app;
    private Node              goal;
    private boolean           enabled;
    private boolean           isClick;
    
    public TestController(SimpleApplication app, Node navNode) {
        this.app     = app;
        scene        = navNode.getParent();
        finderNode   = new Node();
        this.navNode = navNode;
        app.getRootNode().attachChild(finderNode);
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
        isClick                  = true;
        
        scene.collideWith(ray, results);
        
        if (results.getClosestCollision() != null) {
            app.getRootNode().attachChild(goal);
            goal.setLocalTranslation(results.getClosestCollision().getContactPoint());
        }
        
    }
    
    public void clickRelease() {
    
        isClick = false;
        
    }
    
    public void rightClick() {
        
        Camera   cam             = app.getCamera();
        Ray      ray             = new Ray(cam.getLocation(), cam.getDirection());
        CollisionResults results = new CollisionResults();        
        
        app.getRootNode().collideWith(ray, results);
        
        if (results.getClosestCollision() != null) {
            
            if (results.getClosestCollision().getGeometry().getParent() instanceof Finder)
                results.getClosestCollision().getGeometry().getParent().removeFromParent();
            
            Finder finder   = new Finder(app, navNode);
            
            finderNode.attachChild(finder);
            finder.setLocalTranslation(results.getClosestCollision().getContactPoint());
            
            if (enabled)
                finder.pathTo(goal);
            
        }
        
    }
    
    public void go() {
        enabled = true;
        startFinders();
    }
    
    public void stop() {
        goal.removeFromParent();
        finderNode.detachAllChildren();
        enabled = false;
    }
    
    private void clickHold() {
        
        Camera   cam             = app.getCamera();
        Ray      ray             = new Ray(cam.getLocation(), cam.getDirection());
        CollisionResults results = new CollisionResults();    
        isClick                  = true;
        
        scene.collideWith(ray, results);
        
        if (results.getClosestCollision() != null) {
            
            if (results.getClosestCollision().getGeometry().getParent() == goal)
                return;
            
            app.getRootNode().attachChild(goal);
            goal.setLocalTranslation(results.getClosestCollision().getContactPoint());
        }
    }
    
    private void startFinders() {
        
        for (int i = 0; i < finderNode.getChildren().size(); i++) {
            
            Finder finder = (Finder) finderNode.getChild(i);
            finder.pathTo(goal);
            
        }
        
    }
    
    private void updateFinders(float tpf) {
    
        for (int i = 0; i < finderNode.getChildren().size(); i++) {
            Finder finder = (Finder) finderNode.getChild(i);
            finder.update(tpf);
        }

    }
    
    public void update(float tpf) {
        
        if (isClick)
            clickHold();
        
        if (enabled)
            updateFinders(tpf);
        
    }
    
}
