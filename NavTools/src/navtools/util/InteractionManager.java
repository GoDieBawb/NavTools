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
    private boolean  click=false, rightClick=false, cursor=false;
    
    public InteractionManager(SimpleApplication app) {
        this.app = app;
        setUpKeys();
    }
    
    private void setUpKeys() {
        
        InputManager inputManager = app.getInputManager();
        
        inputManager.addListener(this, "Click");
        inputManager.addListener(this, "RightClick");
        inputManager.addListener(this, "Cursor");
        inputManager.addMapping("Click",      new MouseButtonTrigger(MouseInput.BUTTON_LEFT));
        inputManager.addMapping("RightClick", new MouseButtonTrigger(MouseInput.BUTTON_RIGHT)); 
        inputManager.addMapping("Cursor", new KeyTrigger(KeyInput.KEY_E)); 
        
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
                
            default:
                return false;
                
        }
    
    }
    
}
