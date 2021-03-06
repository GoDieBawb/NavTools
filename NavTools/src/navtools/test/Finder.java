/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package navtools.test;

import com.jme3.app.SimpleApplication;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector3f;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.shape.Box;
import java.util.ArrayList;
import navtools.ai.WayPoint;
import navtools.ai.PathCalculator;

/**
 *
 * @author root
 */
public class Finder extends Node {
    
    private Vector3f             moveDir;
    private ArrayList<WayPoint>  path;
    private boolean              isFinding;
    private Long                 lastUpdate;
    private final Node           navNode;
    private final PathCalculator calc;  
    private Node                 target;
    
    public Finder(SimpleApplication app, Node navNode, PathCalculator calc) {
        
        Box b        = new Box(1,1,1);
        Geometry g   = new Geometry("Box", b);
        Material m   = new Material(app.getAssetManager(), "Common/MatDefs/Misc/Unshaded.j3md");
        lastUpdate   = System.currentTimeMillis();
        this.navNode = navNode;
        this.calc    = calc;
        m.setColor("Color", ColorRGBA.Cyan);
        g.setMaterial(m);
        attachChild(g);
        
    }
    
    private void updatePath() {
        path = calc.calculatePath(getLocalTranslation(), target.getLocalTranslation());
    }
    
    public void pathTo(Node target) {
        this.target = target;
        isFinding   = true;
        updatePath();
    }

    public void stop() {
        isFinding = false;
    }

    public boolean isFinding() {
        return isFinding;
    }
    
    public void update(float tpf) {
        
        if (!isFinding)
            return;
        
        WayPoint wp;
        
        //If path is Empty Goal is Target
        if (path.isEmpty()) {
            wp = new WayPoint();
            wp.setLocalTranslation(target.getLocalTranslation());
        }
        
        else {
            wp = path.get(0);
        }
        
        moveDir = wp.getLocalTranslation().subtract(getLocalTranslation());        
        
        if (System.currentTimeMillis() - lastUpdate > 1000) {
            pathTo(target);
            lastUpdate = System.currentTimeMillis();
        }
        
        if (getLocalTranslation().distance(target.getLocalTranslation()) < 1) {
            System.out.println("At Destination");
            isFinding = false;
        }
        
        if (getLocalTranslation().distance(wp.getLocalTranslation()) < 1) {
            path.remove(0);
        }        
        
        move(moveDir.normalize().mult(3).mult(tpf));
        
    }
    
}
