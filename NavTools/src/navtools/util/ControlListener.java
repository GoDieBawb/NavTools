/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package navtools.util;

import navtools.util.InteractionManager;
import com.jme3.app.SimpleApplication;
import navtools.AppManager;
import org.lwjgl.opengl.Display;

/**
 *
 * @author root
 */
public class ControlListener {
    
    private boolean click = false, rightClick = false, cursor = false;
    private final InteractionManager im;
    private final SimpleApplication app;
    private String sceneName;
    
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
        
    }
    
    private void clickPress() {
        
        if (!app.getInputManager().isCursorVisible()) {
            app.getStateManager().getState(AppManager.class).getSceneManager().onClick();
        }
        
        else {
            app.getStateManager().getState(AppManager.class).getGui().click();
        }
        
    }
    
    private void clickRelease() {
    
    }
    
    private void rightClickPress() {
        
        if (!app.getInputManager().isCursorVisible()) {
            app.getStateManager().getState(AppManager.class).getSceneManager().onRightClick();
            return;
        }
        
        float y = app.getInputManager().getCursorPosition().y;
        
        if (y > Display.getHeight()/4) {
            app.getStateManager().getState(AppManager.class).getSceneManager().onRightClick();
        }
        
        else {
        
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
