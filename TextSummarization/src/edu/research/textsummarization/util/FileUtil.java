/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.research.textsummarization.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
//import java.io.InputStream;
import java.io.InputStreamReader;
//import java.io.PrintWriter;
//import java.io.UnsupportedEncodingException;
//import java.util.ArrayList;
//import java.util.Enumeration;
//import java.util.Hashtable;
//import java.util.Stack;
//import java.util.logging.Level;
//import java.util.logging.Logger;

/**
 *
 * @author Administrator
 */
public class FileUtil 
{
    public static String getContentFromFile(String filePath) {
        FileInputStream fileInputStream = null;
        try {
            File file = new File(filePath);
            fileInputStream = new FileInputStream(file);
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(fileInputStream,Constants.CHAR_ENCODING_UTF8));
            StringBuffer stringBuffer = new StringBuffer();
            String line = "";
            while ((line = bufferedReader.readLine()) != null) {
                stringBuffer.append(line + Constants.NEWLINE);
            }
            return stringBuffer.toString();
        } catch (FileNotFoundException ex) {
            ex.printStackTrace();
            System.out.println("FileNotFoundException " + ex.toString());
            return null;
        } catch (IOException ie) {
            ie.printStackTrace();
            System.out.println("IO Exception in reading file" + ie.toString());
            return null;
        } finally {
            try {
                if (fileInputStream != null) {
                    fileInputStream.close();
                }
            } catch (IOException ex) {
                System.out.println("exception in closing inputstream");
            }
        }

    }//end of function
    public static void main(String[] args) {
     //  String s = getContentFromFile("C:/82_utf.txt");
      //  ArrayList<String> filepath = getAbsolutePathofAllFiles("D:\\TeluguRetrieval");
       // testmemory();
       // System.out.println("conent="+s);
//        appendWordsIntofile("\u096D","bvdf fgjdf jdf"); 
  //     appendWordsIntofile("\u096D","jayesh jethva");

    }
}
        

