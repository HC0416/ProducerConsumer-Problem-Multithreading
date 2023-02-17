/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.rts_assignment_final;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

/**
 *
 * @author Dell
 */
public class Serialization {
    
    public static byte[] getByteArray(Soybean soybean) throws IOException {
        
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        ObjectOutputStream os = new ObjectOutputStream(out);
        os.writeObject(soybean);
        return out.toByteArray();
    }
    
    public static byte[] getByteArray2 (Flour flour) throws IOException {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        ObjectOutputStream os = new ObjectOutputStream(out);
        os.writeObject(flour);
        return out.toByteArray();
    }
    
    public static byte[] getByteArray3(Residue residue) throws IOException {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        ObjectOutputStream os = new ObjectOutputStream(out);
        os.writeObject(residue);
        return out.toByteArray();
    }
    
    public static byte[] getByteArray4(Milk milk) throws IOException {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        ObjectOutputStream os = new ObjectOutputStream(out);
        os.writeObject(milk);
        return out.toByteArray();
    }
    
    public static Object deserialize(byte[] byteArray) throws IOException, ClassNotFoundException{
         
        ByteArrayInputStream in = new ByteArrayInputStream(byteArray);
        ObjectInputStream is = new ObjectInputStream(in);
        return is.readObject();
    }
    
}
