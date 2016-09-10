/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package navtools.editor;

import navtools.ai.WayPoint;
import com.jme3.app.SimpleApplication;
import com.jme3.collision.CollisionResults;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Ray;
import com.jme3.renderer.Camera;
import com.jme3.scene.Geometry;
import com.jme3.scene.Mesh;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import com.jme3.scene.VertexBuffer;
import java.util.ArrayList;
import navtools.AppManager;
import navtools.ai.WayPoint;
import navtools.scene.SceneManager;

/**
 *
 * @author root
 */
public class WayPointEditControl {
    
    private final SimpleApplication app;
    private WayPoint          selectedWayPoint;
    private Node              scene;
    private Material          red, green, yellow;
    private final Node        lineNode;
    private boolean           showAllLines;
    private SceneManager      sm;
    
    public WayPointEditControl(SimpleApplication app, SceneManager sm) {
        this.app   = app;
        scene      = sm.getScene();
        lineNode   = new Node();
        this.sm    = sm;
        app.getRootNode().attachChild(lineNode);
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
        clearColors();
        clearLines();
    }
    
    public void setShowLines(boolean show) {
        
        showAllLines = show;
        
        if (show) {
            drawAllLines();
        }
        
        else {
            colorNeighbors();
        }
        
    }
    
    public void setScene(Node newScene) {
        scene = newScene;
    }
    
    public void click() {
        
        Node     navNode         = sm.getNavNode();
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
        
        Node     navNode         = sm.getNavNode();   
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
                wp.getNeighbors().remove(selectedWayPoint);
                colorNeighbors();
            }
            
            else {
                wp.getNeighbors().add(selectedWayPoint);
                selectedWayPoint.getNeighbors().add(wp);
                colorNeighbors();
            }
            
        }        
    
    }    
    
    public void clearColors() {
        
        Node navNode = sm.getNavNode();
        
        for (int i = 0; i < navNode.getChildren().size(); i++) {
            WayPoint wp = (WayPoint) navNode.getChildren().get(i);
            wp.setMaterial(red);
        }
        
    }
    
    private void clearLines() {
        lineNode.detachAllChildren();
    }
    
    private void colorNeighbors() {

        clearColors();
        clearLines();
        
        if (selectedWayPoint == null) {
            return;
        }
        
        selectedWayPoint.setMaterial(yellow);
        
        for (int i = 0; i < selectedWayPoint.getNeighbors().size(); i++) {
            
            WayPoint wp = (WayPoint) selectedWayPoint.getNeighbors().get(i);
            wp.setMaterial(green);
            
            if (showAllLines) {
                drawAllLines();
            }
            
            else {
                Spatial line = drawLine(selectedWayPoint, wp);
                line.setMaterial(green);
                lineNode.attachChild(line);
            }
            
        }
        
    }
    
    private void drawAllLines() {
    
        clearLines();

        Node navNode = sm.getNavNode();
        
        ArrayList<WayPoint> list = new ArrayList(); 
        
        for (int i = 0; i < navNode.getChildren().size(); i++) {
            
            WayPoint wp = (WayPoint) navNode.getChildren().get(i);

            for (int j = 0; j < wp.getNeighbors().size(); j++) {
            
                WayPoint neighbor = (WayPoint) wp.getNeighbors().get(j);
                
                if (!list.contains(neighbor)) {
                    
                    Spatial line = drawLine(wp, neighbor);
                    lineNode.attachChild(line);
                    
                    if (wp == selectedWayPoint || neighbor == selectedWayPoint) {
                        line.setMaterial(green);
                    }
                    
                }
                    
                
            }
            
            list.add(wp);
            
        }
        
    }
    
    private Spatial drawLine(Spatial origin, Spatial destination) {
        
        float ox = origin.getLocalTranslation().x;
        float oy = origin.getLocalTranslation().y;
        float oz = origin.getLocalTranslation().z;
        
        float dx = destination.getLocalTranslation().x;
        float dy = destination.getLocalTranslation().y;
        float dz = destination.getLocalTranslation().z;        
        
        Mesh lineMesh = new Mesh();

        lineMesh.setMode(Mesh.Mode.Lines);

        lineMesh.setBuffer(VertexBuffer.Type.Position, 3, new float[]{ ox, oy, oz, dx, dy, dz});

        lineMesh.setBuffer(VertexBuffer.Type.Index, 2, new short[]{ 0, 1 });

        lineMesh.updateBound();

        lineMesh.updateCounts();

        Geometry lineGeometry = new Geometry("line", lineMesh);

        Material lineMaterial = red;

        lineGeometry.setMaterial(lineMaterial);

        return lineGeometry;
        
    }
    
}
