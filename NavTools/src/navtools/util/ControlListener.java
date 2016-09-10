/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package navtools.util;

import com.jme3.app.SimpleApplication;
import navtools.AppManager;

/**
 *
 * @author root
 */
public class ControlListener {
    
    private boolean click = false, rightClick = false, cursor = false, export = false, sort = false;
    private final InteractionManager im;
    private final SimpleApplication app;
    
    public ControlListener(SimpleApplication app, InteractionManager im) {
        this.app = app;
        this.im  = im;
    }
    
    private void updateKeys() {
    
        if (im.getIsPressed("Click")) {
            
            if (!click) {
                clickPress();
            }
            
            click = true;  
        }
        
        else if (click) {
            clickRelease();
            click = false;
        }
        
        if (im.getIsPressed("RightClick")) {
            
            if (!rightClick) {
                rightClickPress();
            }
            
            rightClick = true;
        }
        
        else if (rightClick) {
            rightClickRelease();
            rightClick = false;
        } 
        
        if (im.getIsPressed("Cursor")) {
            
            if (!cursor) {
                cursorPress();
            }
            
            cursor = true;
            
        }
        
        else if (cursor) {
            cursorRelease();
            cursor = false;
        }
        
        if (im.getIsPressed("Export")) {
            export = true;
        }
        
        else if (export) {
            exportRelease();
            export = false;
        }  
        
        if (im.getIsPressed("Sort")) {
            sort = true;
        }
        
        else if (sort) {
            sortRelease();
            sort = false;
        }            
        
    }
    
    private void exportRelease() {
        app.getStateManager().getState(AppManager.class).getSceneManager().getControlManager().onExportRelease();
    }
    
    private void sortRelease() {
        app.getStateManager().getState(AppManager.class).getSceneManager().getControlManager().onSortRelease();
    }    
    
    private void clickPress() {
        
        if (!app.getInputManager().isCursorVisible()) {
            app.getStateManager().getState(AppManager.class).getSceneManager().getControlManager().onClick();
        }
        
        else {
            app.getStateManager().getState(AppManager.class).getGui().click();
        }
        
    }
    
    private void clickRelease() {
        
        if (!app.getInputManager().isCursorVisible()) {
            app.getStateManager().getState(AppManager.class).getSceneManager().getControlManager().onClickRelease();
        }
    }
    
    private void rightClickPress() {
        
        if (!app.getInputManager().isCursorVisible()) {
            app.getStateManager().getState(AppManager.class).getSceneManager().getControlManager().onRightClick();
        }
        
        else {
            app.getStateManager().getState(AppManager.class).getSceneManager().getControlManager().onRightClick();
        }
    }
    
    private void rightClickRelease() {
    
    }
    
    private void cursorPress() {
        app.getFlyByCamera().setEnabled(false);
        app.getInputManager().setCursorVisible(true);
    }
    
    private void cursorRelease() {
        app.getFlyByCamera().setEnabled(true);
        app.getInputManager().setCursorVisible(false);
    }
    
    public void update() {
        updateKeys();
    }
    
}
