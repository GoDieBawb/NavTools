/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package navtools.util;

import com.jme3.export.binary.BinaryExporter;
import com.jme3.scene.Node;
import java.io.File;
import java.io.IOException;

/**
 *
 * @author root
 */
public class NodeExporter {
    
    public void export(Node node, String directory, String name) {

        BinaryExporter exporter = BinaryExporter.getInstance();
        String projectPath      = System.getProperty("user.dir");
        String localPath        = "/assets/" + directory + "/" + name + ".j3o";
        String path             = projectPath + localPath;
        File file               = new File(path);
        
        try {
            exporter.save(node, file);
            System.out.println("Node Exported to: " + path);

        } catch (IOException ex) {
            System.out.println("FAILED TO EXPORT");
        } 
    
    }
    
}
