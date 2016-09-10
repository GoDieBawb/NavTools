/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package navtools.editor;

import com.jme3.app.SimpleApplication;
import navtools.AppManager;
import navtools.scene.SceneManager;
import navtools.test.TestController;
import navtools.util.NodeExporter;

/**
 *
 * @author root
 */
public class EditControlManager {
    
    private final MeshEditControl     mec;
    private final WayPointEditControl wec;
    private final TestController      tc;
    private final SimpleApplication   app;
    private final SceneManager        sm;
    
    
    public EditControlManager(SimpleApplication app, SceneManager sm) {
        this.app = app;
        this.sm  = sm;
        mec      = new MeshEditControl(app, sm);
        wec      = new WayPointEditControl(app, sm);
        tc       = new TestController(app, sm);
    }
    
    public void onClick() {
        
        String mode = app.getStateManager().getState(AppManager.class).getGui().getMode();
        
        switch(mode) {
        
            case "Mesh":
                mec.click();
                break;
            
            case "WayPoint":
                wec.click();
                break;
                
            case "Test":
                tc.click();
                break;  
                
        }
        
    }
    
    public void onRightClick() {
        
        String mode = app.getStateManager().getState(AppManager.class).getGui().getMode();
        
        switch(mode) {
        
            case "Mesh":
                mec.rightClick();
                break;
                
            case "WayPoint":
                wec.rightClick();
                break;  
                
            case "Test":
                tc.rightClick();
                break;                  
            
        }
        
    } 
    
    public void onClickRelease() {
        
        String mode = app.getStateManager().getState(AppManager.class).getGui().getMode();
        switch(mode) {
        
            case "Test":
                tc.clickRelease();
            
        }
        
    }
    
    public void onExportRelease() {
        wec.setShowLines(true);
        wec.clearColors();
        sm.sortWayPoints();
        sm.addNeighborData();
        new NodeExporter().export(sm.getNavNode(), "Mesh", sm.getSceneName() + "_Mesh");
    }
    
    public void onSortRelease() {
        sm.sortWayPoints();
    }
    
    public TestController getTestController() {
        return tc;
    }
    
    public void setShowLines(boolean show) {
        wec.setShowLines(show);
    }
    
    public void changeMode() {
        wec.changeMode();
    }    
    
    public void update(float tpf) {
        tc.update(tpf);
    }
    
}
