/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package navtools.ai;

import com.jme3.math.Vector3f;
import java.util.ArrayList;

/**
 *
 * @author root
 */
public class PathCalculator {
    
    private Vector3f startPoint, finalPoint;
    private WayPoint a ,b;
    private ArrayList<WayPoint> badPoints  = new ArrayList();
    private ArrayList<WayPoint> goodPoints = new ArrayList();
    private ArrayList<WayPoint> path, allPoints;
    private NavigationNode      navNode;
    private int     maxPathSize;
    private boolean debug;
    
    public PathCalculator(NavigationNode navNode) {
        this.navNode = navNode;
    }
    
    public void setNavNode(NavigationNode navNode) {
        this.navNode = navNode;
    }
    
    private void initPoints() {
        
        allPoints   = new ArrayList();
        char letter = 'A';
        
        
        for (int i = 0; i < navNode.getChildren().size(); i++) {
            
            WayPoint wp = (WayPoint) navNode.getChild(i);
            wp.setName(Character.toString(letter));
            allPoints.add(wp);
            letter++;

        }
        
        //makeTree();
        
    }
    
    public void setDebug(boolean newDebug) {
        debug = newDebug;
    }
    
    private void makeTree() {
    
        if (!debug)
            return;
        
        System.out.println("Making Tree::");
        
        ArrayList<WayPoint> list = new ArrayList(); 
        
        for (int i = 0; i < navNode.getChildren().size(); i++) {
            
            WayPoint wp = (WayPoint) navNode.getChildren().get(i);

            for (int j = 0; j < wp.getNeighbors().size(); j++) {
            
                WayPoint neighbor = (WayPoint) wp.getNeighbors().get(j);
                
                if (!list.contains(neighbor)) {
                    float distance = wp.getLocalTranslation().distance(neighbor.getWorldTranslation());
                    System.out.println(wp.getName() + neighbor.getName() + " " + distance);
                }
                    
            }
            
            list.add(wp);
            
        }
        
        System.out.println("");
        
    }
    
    public ArrayList<WayPoint> calculatePath(Vector3f origin, Vector3f destination) {
        finalPoint = destination;
        startPoint = origin;
        updatePath();
        return path;
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
            
            if (goodPoints.contains(newA)) {
                badPoints.add(prevA);
            }
            
            else {
                a = newA;
                goodPoints.add(a);
            }
            
            if (goodPoints.size() > maxPathSize) {
                System.out.println("Path Error: ");
                System.out.println("Returning Previous Path...");
                return path;
            }
            
            origin = a.getLocalTranslation();
            
            return getPath(origin);
            
        }
        
        //Destination Goes Last in the List
        goodPoints.add(b);
        
        return goodPoints;
        
    }    
    
    private void printPath() {
    
        if (!debug)
            return;
        
        System.out.print("Path: ");
        for (WayPoint wp: path) {
            System.out.print(wp.getName() + " ");
        }
        
        System.out.println("");
        
    }
    
    private void updatePath() {
        initPoints();
        maxPathSize = (int) Math.pow(allPoints.size(), 2);
        a           = null;
        b           = null;
        goodPoints  = new ArrayList();
        badPoints   = new ArrayList();
        path        = getPath(startPoint);
        printPath();
    }
    
}
