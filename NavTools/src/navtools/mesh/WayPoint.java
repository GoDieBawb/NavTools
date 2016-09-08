/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package navtools.mesh;

import com.jme3.scene.Node;
import java.util.ArrayList;

/**
 *
 * @author root
 */
public class WayPoint extends Node {
    
    ArrayList<WayPoint> neighbors;
    
    public WayPoint() {
        neighbors = new ArrayList();
        name      = "WayPoint";
    }
    
    public void addNeighbor(WayPoint wp) {
        neighbors.add(wp);
    }
    
    public void removeNeighbor(WayPoint wp) {
        neighbors.remove(wp);
    }
            
    public ArrayList<WayPoint> getNeighbors() {
        return neighbors;
    }
    
}
