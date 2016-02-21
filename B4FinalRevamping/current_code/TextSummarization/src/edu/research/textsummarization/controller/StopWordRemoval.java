/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.research.textsummarization.controller;


import edu.research.textsummarization.util.Constants;
import java.io.*;
import java.util.*;

/**
 *
 * @author Administrator
 */
public class StopWordRemoval 
{

    private static Hashtable<String, Integer> sStopwords = new Hashtable<String, Integer>();
    
    private static StopWordRemoval sSelfRef=null;
    
    static 
	{
         BufferedReader br =null;
        System.out.println("StopWordRemoval STATIC BLOCK CALLED");
        try 
		{
            String record = null;
             br = new BufferedReader(new InputStreamReader(new FileInputStream("stopwords.txt"),Constants.CHAR_ENCODING_UTF8));
            while ((record = br.readLine()) != null) 
			{
                sStopwords.put(record.trim(), 1);
            }
        } catch (IOException io) 
		{
            io.printStackTrace();
            System.out.println("IO Exception in stopword removal:" + io.toString());
        }
		finally
		{
            try
			{
            br.close();
            }catch(IOException io)
			{
                System.out.println("exception in closing reader");
            }
        }
          System.out.println("stopword loading complete");
          System.out.println("No of stop word:"+ sStopwords.size());
      
    }
    
    private StopWordRemoval ()
    {           
    }
    
    public static StopWordRemoval getReference()
	{
        if (sSelfRef==null)
		{
            sSelfRef=new StopWordRemoval();
                }
        return sSelfRef;        
    }
    
     

    public  boolean isStopWord(String token) 
	{
            //System.out.println("Inside stop word removal:"+ token);
        if (sStopwords.containsKey(token)) 
		{
            return true;
        } else 
		{
            return false;
        }

    }
}
