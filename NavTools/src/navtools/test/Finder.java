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
import navtools.mesh.WayPoint;

/**
 *
 * @author root
 */
public class Finder extends Node {
    
    private Vector3f moveDir, finalPoint;
    private ArrayList<WayPoint> badPoints  = new ArrayList();
    private ArrayList<WayPoint> goodPoints = new ArrayList();
    private ArrayList<WayPoint> path, allPoints;
    private WayPoint a ,b;
    private boolean isFinding;
    private Long    lastUpdate;
    private Node    navNode;
    
    public Finder(SimpleApplication app, Node navNode) {
        
        Box b        = new Box(1,1,1);
        Geometry g   = new Geometry("Box", b);
        Material m   = new Material(app.getAssetManager(), "Common/MatDefs/Misc/Unshaded.j3md");
        lastUpdate   = System.currentTimeMillis();
        this.navNode = navNode;
        
        m.setColor("Color", ColorRGBA.Cyan);
        g.setMaterial(m);
        attachChild(g);
        
    }
    
    private void initPoints() {
        
        allPoints = new ArrayList();
        
        for (int i = 0; i < navNode.getChildren().size(); i++) {
            WayPoint wp = (WayPoint) navNode.getChild(i);
            allPoints.add(wp);
        }
        
    }
    
    public void pathTo(Vector3f destination) {
        finalPoint = destination;
        isFinding  = true;
        updatePath();
    }
    
    private WayPoint getClosest(WayPoint wp) {
    
        WayPoint closest  = wp;
        float    distance = 9999;
        
        for (WayPoint point: allPoints) {
            
            if (point.getLocalTranslation().distance(wp.getLocalTranslation()) < distance) {
                
                if (point == wp)
                    continue;
                
                distance = point.getLocalTranslation().distance(wp.getWorldTranslation());
                closest  = point;
                
            }
            
        }
        
        return closest;
        
    }
    
    private ArrayList<WayPoint> getPath(Vector3f origin) {

        if (a == null && b == null) {
            
            a = new WayPoint();
            b = new WayPoint();
            
            a.setName("Start");
            b.setName("Finish");
            
            a.setLocalTranslation(origin);
            b.setLocalTranslation(finalPoint);
        
            allPoints.add(a);
            allPoints.add(b);
            
            a.getNeighbors().add(getClosest(a));
            b.getNeighbors().add(getClosest(b));
            
            //Original Destination is a Point
            goodPoints.add(a);
            
        }
        
        WayPoint prevA = a;
        WayPoint newA  = new WayPoint();         
        
        while (!b.getNeighbors().contains(a)) {
            
            float dist  = origin.distance(finalPoint);
            
            for (WayPoint point : a.getNeighbors()) {
                
                if (badPoints.contains(point))
                    continue;
                
                float checkdist = finalPoint.distance(point.getLocalTranslation());

                if (dist > checkdist) {
                    newA  = point;
                    dist  = checkdist;
                }
                
            }
            
            a = newA;
            
            if (goodPoints.contains(a)) {
                badPoints.add(prevA);
            }
            
            else {
                goodPoints.add(a);
            }
            
            origin      = a.getLocalTranslation();
            
            return getPath(origin);
            
        }
        
        //Destination Goes Last in the List
        goodPoints.add(b);
        
        return goodPoints;
        
    }
    
    private void updatePath() {
        initPoints();
        a          = null;
        b          = null;
        goodPoints = new ArrayList();
        badPoints  = new ArrayList();
        path       = getPath(this.getLocalTranslation());
        lastUpdate = System.currentTimeMillis();
    }
    
    public void update(float tpf) {
        
        if (!isFinding)
            return;
        
        WayPoint wp = path.get(0);
        moveDir     = wp.getLocalTranslation().subtract(getLocalTranslation());        
        
        if (System.currentTimeMillis() - lastUpdate > 3000) {
            pathTo(finalPoint);
        }
        
        if (getLocalTranslation().distance(finalPoint) < 3) {
            System.out.println("At Destination");
            isFinding = false;
        }
        
        if (getLocalTranslation().distance(wp.getLocalTranslation()) < 3) {
            path.remove(0);
        }        
        
        move(moveDir.normalize().mult(3).mult(tpf));
        
    }
    
}
