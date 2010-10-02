package org.droidkit.util;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;

public class DKIOTricks {

    // "http://example.com/icons/icon.jpg" => "http-example.com-icons-icon.jpg"
    // Used when caching images to local storage
    public static String sanitizeFileName(String url) {
        String fileName = url.replaceAll("[:\\/\\?\\&\\|]+", "-");
        if (fileName.length() > 128) {
            fileName = DKTextTricks.md5Hash(fileName);
        }
        return fileName;
    }
    
    public static void saveObject(Object object, File fileName) throws IOException {
        File tempFileName = new File(fileName.toString() + "-tmp");

        FileOutputStream fileOut = new FileOutputStream(tempFileName);
        BufferedOutputStream buffdOut = new BufferedOutputStream(fileOut, 32 * 1024);
        ObjectOutputStream objectOut = new ObjectOutputStream(buffdOut);
        
        objectOut.writeObject(object);
        
        objectOut.close();
        buffdOut.close();
        fileOut.close();
        
        fileName.delete();
        tempFileName.renameTo(fileName);
    }
    
    public static Object loadObject(File fileName) throws IOException, ClassNotFoundException {
        FileInputStream fileIn = new FileInputStream(fileName);
        BufferedInputStream buffdIn = new BufferedInputStream(fileIn, 32 * 1024);
        ObjectInputStream objectIn = new ObjectInputStream(buffdIn);
        
        Object object = objectIn.readObject();
        
        objectIn.close();
        buffdIn.close();
        fileIn.close();
        
        return object;
    }
    
    public static int copyStreamToStream(InputStream in, OutputStream out, int bufferSize) throws IOException {
        byte[] buffer = new byte[bufferSize];
        int total = 0;
        int len;
        while ((len = in.read(buffer)) > 0) {
            out.write(buffer, 0, len);
            total += len;
        }
        return total;
    }
}
