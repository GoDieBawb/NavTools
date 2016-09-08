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
    
    private Vector3f moveDir;
    private ArrayList<WayPoint> badPoints  = new ArrayList();
    private ArrayList<WayPoint> goodPoints = new ArrayList();
    private ArrayList<WayPoint> path, allPoints;
    private WayPoint a ,b;
    
    public Finder(SimpleApplication app, Node navNode) {
        
        Box b      = new Box(1,1,1);
        Geometry g = new Geometry("Box", b);
        Material m = new Material(app.getAssetManager(), "Common/MatDefs/Misc/Unshaded.j3md");
        
        initPoints(navNode);
        m.setColor("Color", ColorRGBA.Cyan);
        g.setMaterial(m);
        attachChild(g);
        
    }
    
    private void initPoints(Node navNode) {
    
        for (int i = 0; i < navNode.getChildren().size(); i++) {
            WayPoint wp = (WayPoint) navNode.getChild(i);
            allPoints.add(wp);
        }
        
    }
    
    public void pathTo(Vector3f destination) {
        updatePath(destination);
    }
    
    private ArrayList<WayPoint> getPath(ArrayList<WayPoint> points, Vector3f origin, Vector3f destination) {
     
        if (a == null && b == null) {
            
            a = new WayPoint();
            b = new WayPoint();
            
            a.setLocalTranslation(origin);
            b.setLocalTranslation(destination);
        
            a.getNeighbors().set(0, b);
            b.getNeighbors().set(0, a);
            
            for (WayPoint point : points) {

                float and     = a.getLocalTranslation().distance(a.getNeighbors().get(0).getLocalTranslation());
                float bnd     = b.getLocalTranslation().distance(b.getNeighbors().get(0).getLocalTranslation());
                
                float adist = a.getLocalTranslation().distance(point.getLocalTranslation());
                float bdist = destination.distance(point.getLocalTranslation());
                
                if (and > adist) {
                    a.getNeighbors().set(0, point);
                }
                
                if (bnd > bdist) {
                    b.getNeighbors().set(0, point);
                }

            }
            
        }
        
        while (!a.getNeighbors().contains(b)) {
        
            WayPoint prevA = a;
            WayPoint prevB = b;
            WayPoint newA = new WayPoint();
            WayPoint newB = new WayPoint();
            
            float dist = a.getLocalTranslation().distance(b.getLocalTranslation());
            
            for (WayPoint point : a.getNeighbors()) {

                if (badPoints.contains(point))
                    continue;
                
                float newdist = a.getLocalTranslation().distance(point.getLocalTranslation());

                if (dist > newdist) {
                    newA  = point;
                    dist  = newdist;
                }
                
            }
            
            a = newA;
            
            dist = a.getLocalTranslation().distance(b.getLocalTranslation());
            
            for (WayPoint point : b.getNeighbors()) {

                if (badPoints.contains(point))
                    continue;
                
                float newdist = b.getLocalTranslation().distance(point.getLocalTranslation());
                
                if (dist > newdist) {
                    newB  = point;
                    dist  = newdist;
                }
                
            }            
            
            b = newB;
            
            if (goodPoints.contains(a)) {
                badPoints.add(prevA);
            }
            
            else {
                goodPoints.add(a);
            }
            
            if (goodPoints.contains(b)) {
                badPoints.add(prevB);
            }            
            
            else {
                goodPoints.add(b);
            }
            
            origin      = a.getLocalTranslation();
            destination = b.getLocalTranslation();
            
            return getPath(points, origin, destination);
            
        }
        
        return goodPoints;
        
    }
    
    private void updatePath(Vector3f dest) {
        a          = null;
        b          = null;
        goodPoints = null;
        badPoints  = null;
        path       = getPath(allPoints, this.getLocalTranslation(), dest);
    }
    
    public void update(float tpf) {
    
        WayPoint wp = path.get(0);
        moveDir     = wp.getLocalTranslation().subtract(getLocalTranslation());
        
        if (getLocalTranslation().distance(wp.getLocalTranslation()) < 3) {
            path.remove(0);
        }
        
        move(moveDir.normalize().mult(3).mult(tpf));
        
    }
    
}
