/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package navtools.gui;

import com.jme3.app.SimpleApplication;
import com.jme3.font.BitmapFont;
import com.jme3.font.BitmapText;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.shape.Box;
import jme3tools.optimize.GeometryBatchFactory;
import navtools.AppManager;
import org.lwjgl.opengl.Display;

/**
 *
 * @author root
 */
public class Gui {
    
    private final SimpleApplication app;
    private final float screenHeight;
    private final float screenWidth;
    private final float xOrigin;
    private final float yOrigin;
    private Node        meshButton;
    private Node        pointButton;
    private BitmapText infoText;
    private String     mode;
    
    public Gui(SimpleApplication app) {
        
        this.app     = app;
        float h      = Display.getHeight();
        float w      = Display.getWidth();
        xOrigin      = w/50;
        yOrigin      = h/4;
        screenWidth  = w - w/50 - w/50;
        screenHeight = h - h/4 - h/50;
        
        createInfoText();
        createFrame();
        createCrosshair();
        setMode("Mesh");
        
    } 
    
    public String getMode() {
        return mode;
    }
    
    private void setMode(String newMode) {
        mode = newMode;
        infoText.setText(mode + " Edit");
        float xPos = screenWidth/2 - infoText.getLineWidth()/2 + xOrigin;
        float yPos = screenHeight + yOrigin - infoText.getLineHeight();
        infoText.setLocalTranslation(xPos, yPos, 1);
    }
    
    private void createInfoText() {
        
        BitmapFont font = app.getAssetManager().loadFont("Interface/Fonts/Default.fnt");
        infoText            = new BitmapText(font, false);
        infoText.setText("Some Test Text");
        
        app.getGuiNode().attachChild(infoText);
        
        float xPos = screenWidth/2 - infoText.getLineWidth()/2 + xOrigin;
        float yPos = screenHeight + yOrigin - infoText.getLineHeight();
        
        infoText.setLocalTranslation(xPos, yPos, 1);
        
        createWayPointEditButton();
        createMeshButton();
        
    }
    
    private void createWayPointEditButton() {
        
        BitmapFont font = app.getAssetManager().loadFont("Interface/Fonts/Default.fnt");
        BitmapText text = new BitmapText(font, false);
        pointButton     = new Node();
        Box  box        = new Box(Display.getWidth()/10, Display.getHeight()/10, 1);
        Geometry g      = new Geometry("Box", box);
        Material m      = new Material(app.getAssetManager(), "Common/MatDefs/Misc/Unshaded.j3md");
        m.setColor("Color", ColorRGBA.Blue);
        text.setText("Edit Points");
        m.setTexture("ColorMap", app.getAssetManager().loadTexture("Models/well/Rusted Metal.png"));
        pointButton.attachChild(g);
        pointButton.attachChild(text);
        app.getGuiNode().attachChild(pointButton);
        g.setMaterial(m);
        
        
        float yPos = Display.getHeight()/8;
        float xPos = Display.getWidth() - Display.getWidth()/5;   
             
        text.setLocalTranslation(Display.getWidth()/20-text.getLineWidth(), Display.getHeight()/20 - text.getLineHeight(), 1);        
        pointButton.setLocalTranslation(xPos, yPos, 1);        
        
    }
    
    private void createMeshButton() {
        
        BitmapFont font = app.getAssetManager().loadFont("Interface/Fonts/Default.fnt");
        BitmapText text = new BitmapText(font, false);
        meshButton      = new Node();
        Box  box        = new Box(Display.getWidth()/10, Display.getHeight()/10, 1);
        Geometry g      = new Geometry("Box", box);
        Material m      = new Material(app.getAssetManager(), "Common/MatDefs/Misc/Unshaded.j3md");
        m.setTexture("ColorMap", app.getAssetManager().loadTexture("Models/well/Rusted Metal.png"));
        m.setColor("Color", ColorRGBA.Red);
        text.setText("Edit Mesh");
        meshButton.attachChild(g);
        meshButton.attachChild(text);
        app.getGuiNode().attachChild(meshButton);
        g.setMaterial(m);
        
        float yPos = Display.getHeight()/8;
        float xPos = 0 + Display.getWidth()/5;
        
        text.setLocalTranslation(Display.getWidth()/20-text.getLineWidth(), Display.getHeight()/20-text.getLineHeight(), 1);
        meshButton.setLocalTranslation(xPos, yPos, 1);
        
    }
    
    private void createCrosshair() {
        
        BitmapFont font  = app.getAssetManager().loadFont("Interface/Fonts/Default.fnt");
        BitmapText cross = new BitmapText(font, false);
        cross.setText("+");
        
        app.getGuiNode().attachChild(cross);
        
        float xPos = screenWidth/2  + xOrigin - cross.getLineWidth();
        float yPos = Display.getHeight()/2 + cross.getLineHeight()/2;
        
        cross.setLocalTranslation(xPos, yPos, 1);
        
    }
    
    private void createFrame() {
        
        Node     frame  = new Node();
        Box      horz   =  new Box(Display.getWidth(), Display.getHeight()/4, 1);
        Box      hBar   =  new Box(Display.getWidth(), Display.getHeight()/50, 1);
        Box      vBar   =  new Box(Display.getWidth()/50, Display.getHeight(), 1);
        
        Geometry vGeom  = new Geometry("Box", vBar);
        Geometry hGeom  = new Geometry("Box", horz);
        Geometry hbGeom = new Geometry("Box", hBar);
        Geometry vbGeom = new Geometry("Box", vBar);
        
        Material m      = new Material(app.getAssetManager(), "Common/MatDefs/Misc/Unshaded.j3md");
        m.setTexture("ColorMap", app.getAssetManager().loadTexture("Models/well/Rusted Metal.png"));
        
        vGeom.setMaterial(m);
        hGeom.setMaterial(m);
        hbGeom.setMaterial(m);
        vbGeom.setMaterial(m);
        
        frame.attachChild(hGeom);
        frame.attachChild(vGeom);
        frame.attachChild(hbGeom);
        frame.attachChild(vbGeom);

        vbGeom.setLocalTranslation(Display.getWidth(),Display.getHeight()/2,0);
        hbGeom.setLocalTranslation(Display.getWidth()/2,Display.getHeight(),0);
        vGeom.setLocalTranslation(0, Display.getHeight()/2, 0);
        hGeom.setLocalTranslation(Display.getWidth()/2, 0, 0);  
        
        app.getGuiNode().attachChild(GeometryBatchFactory.optimize(frame));
        
    }
    
    public void click() {
    
        Material red      = new Material(app.getAssetManager(), "Common/MatDefs/Misc/Unshaded.j3md");
        Material blue   = new Material(app.getAssetManager(), "Common/MatDefs/Misc/Unshaded.j3md");
        red.setColor("Color", ColorRGBA.Red);
        blue.setColor("Color", ColorRGBA.Blue);  
        
        red.setTexture("ColorMap", app.getAssetManager().loadTexture("Models/well/Rusted Metal.png"));
        blue.setTexture("ColorMap", app.getAssetManager().loadTexture("Models/well/Rusted Metal.png"));
        
        if (meshButtonCheck()) {
            
            if (mode.equals("Mesh"))
                return;
            
            setMode("Mesh");
            meshButton.getChild(0).setMaterial(red);
            pointButton.getChild(0).setMaterial(blue);
            app.getStateManager().getState(AppManager.class).getSceneManager().changeMode();
            
        }
        
        else if (pointButtonCheck()) {
            
            if (mode.equals("WayPoint"))
                return;            
            
            setMode("WayPoint");
            
            meshButton.getChild(0).setMaterial(blue);
            pointButton.getChild(0).setMaterial(red);
            app.getStateManager().getState(AppManager.class).getSceneManager().changeMode();
            app.getStateManager().getState(AppManager.class).getSceneManager().changeMode();
            
        }
        
    }
    
    private boolean meshButtonCheck() {
        
        float x    = app.getInputManager().getCursorPosition().x;
        float y    = app.getInputManager().getCursorPosition().y;
        
        float yMax = meshButton.getLocalTranslation().y + Display.getHeight()/10;
        float yMin = meshButton.getLocalTranslation().y - Display.getHeight()/10;
        
        float xMax = meshButton.getLocalTranslation().x + Display.getHeight()/10;
        float xMin = meshButton.getLocalTranslation().x - Display.getHeight()/10;
        
        if (x >= xMin && x <= xMax && y >= yMin && y <= yMax) {
            return true;
        }
        
        return false;
        
    }
    
    private boolean pointButtonCheck() {
        
        float x    = app.getInputManager().getCursorPosition().x;
        float y    = app.getInputManager().getCursorPosition().y;
        
        float yMax = pointButton.getLocalTranslation().y + Display.getHeight()/10;
        float yMin = pointButton.getLocalTranslation().y - Display.getHeight()/10;
        
        float xMax = pointButton.getLocalTranslation().x + Display.getHeight()/10;
        float xMin = pointButton.getLocalTranslation().x - Display.getHeight()/10;
        
        return x >= xMin && x <= xMax && y >= yMin && y <= yMax;
        
    }    
    
    public void update(float tpf) {
    
    }
    
}
