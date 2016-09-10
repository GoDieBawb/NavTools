/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package navtools.util;

import com.jme3.app.SimpleApplication;
import com.jme3.input.MouseInput;
import com.jme3.input.controls.ActionListener;
import com.jme3.input.controls.MouseButtonTrigger;
import com.jme3.input.InputManager;
import com.jme3.input.KeyInput;
import com.jme3.input.controls.KeyTrigger;

/**
 *
 * @author root
 */
public class InteractionManager implements ActionListener {

    private SimpleApplication app;    
    private boolean  click=false, rightClick=false, cursor=false, export=false, sort=false;
    
    public InteractionManager(SimpleApplication app) {
        this.app = app;
        setUpKeys();
    }
    
    private void setUpKeys() {
        
        InputManager inputManager = app.getInputManager();
        
        inputManager.addListener(this, "Click");
        inputManager.addListener(this, "RightClick");
        inputManager.addListener(this, "Cursor");
        inputManager.addListener(this, "Export");
        inputManager.addListener(this, "Sort");
        inputManager.addMapping("Click",      new MouseButtonTrigger(MouseInput.BUTTON_LEFT));
        inputManager.addMapping("RightClick", new MouseButtonTrigger(MouseInput.BUTTON_RIGHT)); 
        inputManager.addMapping("Cursor", new KeyTrigger(KeyInput.KEY_E)); 
        inputManager.addMapping("Export", new KeyTrigger(KeyInput.KEY_F9));
        inputManager.addMapping("Sort", new KeyTrigger(KeyInput.KEY_F1));
        
    }
    
    @Override
    public void onAction(String name, boolean isPressed, float tpf) {
        
        switch (name) {
        
            case "Click":
                click = isPressed;
                break;
            case "RightClick":
                rightClick = isPressed;
                break;
            case "Cursor":
                cursor = isPressed;
                break;
            case "Export":
                export = isPressed;
                break;      
            case "Sort":
                sort = isPressed;
                break;                    
            default:
                break;       
        
        }
        
    }
    
    //Ability to Check Buttons Pressed This is For the Camera Manager
    public boolean getIsPressed(String triggerName) {
        
        switch (triggerName) {
            
            case "Click":
                return click ;
                
            case "RightClick":
                return rightClick ;
                
            case "Cursor":
                return cursor;
                
            case "Export":
                return export;                
                
            case "Sort":
                return sort;                  
                
            default:
                return false;
                
        }
    
    }
    
}
