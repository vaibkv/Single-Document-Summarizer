/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.research.textsummarization.controller;

import edu.research.textsummarization.util.ConfigUtil;
import java.util.ArrayList;
//import java.io.*;
//import java.util.*;

/**
 *
 * @author Administrator
 */

public class Tokenizer 
{

    private static Tokenizer s_SelfRef = null;
    private TokenManager m_TokenManager = null;

    private Tokenizer() 
    {

    }

    private void init() 
    {
        m_TokenManager = TokenManager.getInstance();

    }

    public static Tokenizer getInstance() 
    {

        if (s_SelfRef == null) 
        {
            s_SelfRef = new Tokenizer();
            s_SelfRef.init();
        }
        return s_SelfRef;
    }

    public void Tokenize(String text) 
    {
        boolean isStopWordEnabled = ConfigUtil.isStopWordEnabled();
        boolean isStemmingEnabled = ConfigUtil.isStemmingEnabled();
        System.out.println("isStopWordEnabled=" + isStopWordEnabled);
        System.out.println("isStemmingEnabled=" + isStemmingEnabled);
         
        StopWordRemoval stopWordRemoval = StopWordRemoval.getReference();
        String[] tokens = null;

            ArrayList<String> sWordsList = new ArrayList<String>();
            ArrayList<String> tokensList = new ArrayList<String>();
            tokens = text.split("[-–)+><^=_/%$|( '\",*~`&#@;:\n\t\u0964\u0965\u097D.?!]");
            
            /*CHECKING IF TOKENS ARE BEING EXTRACTED FINE*/
            /*System.out.println("Printing tokens.....");
            for(int i=0;i<tokens.length;i++)
            {
                System.out.print(" " + tokens[i]);
            }*/
            
            
            int tokenLength = tokens.length;
            System.out.println("Tokens Length before removing stopwords = " + tokens.length);
            
            ConfigUtil.setIsStopWordRemovalEnabled(true);
            isStopWordEnabled = ConfigUtil.isStopWordEnabled();
            System.out.println("isStopWordEnabled=" + isStopWordEnabled);
            
            
            
            /*processing tokens for stopword removal*/
            for (int j = 0; j < tokenLength; j++) 
            {

                tokens[j] = tokens[j].trim();
                if (tokens[j].trim().equals("") || tokens[j].length() <= 2 || tokens[j].contains("[") || tokens[j].contains("]") || tokens[j].contains("{") || tokens[j].contains("}") || tokens[j].contains("\\"))
		{
                    continue;
                }
                if (tokens[j].trim().equals("")) 
                {
                    continue;
                }
                
                                
                if (isStopWordEnabled) 
                {
                    if (!stopWordRemoval.isStopWord(tokens[j])) 
                    {
                        tokensList.add(tokens[j]);

                    }
                    if (stopWordRemoval.isStopWord(tokens[j]))
                    {
                        sWordsList.add(tokens[j]);
                    }
                }

            }
            
            /*tokensList updated after stopwords are removed*/
            if (isStopWordEnabled) 
            {

                tokens = tokensList.toArray(new String[tokensList.size()]);
            }
            
            System.out.println("Tokens Length after removing stopwords = " + tokens.length);
            
             
            System.out.println("Printing tokens after stopword removal.....");
            for(int i=0;i<tokens.length;i++)
            {
                System.out.print(" " + tokens[i]);
            }
            
            System.out.println("\nNumber of stopwords in document = " + sWordsList.size());
            System.out.println("\nStopWords in the document...");
            for(int i=0;i<sWordsList.size();i++)
            {
                System.out.println(sWordsList.get(i));
            }
            
            
            /*processing tokens for stemming*/
            ConfigUtil.setIsStemmingEnabled(true);
            isStemmingEnabled = ConfigUtil.isStemmingEnabled();
            System.out.println("\nisStemmingEnabled=" + isStemmingEnabled);
            //String stemmedWord;
            if (isStemmingEnabled) 
            {
                for (int k = 0; k < tokens.length; k++) 
                {
                   tokens[k] = PorterStemmer.stem(tokens[k]);
                }
            }
            
            /*checking if the tokens have been stemmed*/
            System.out.println("Printing tokens after stemming.....");
            for(int i=0;i<tokens.length;i++)
            {
                System.out.print(" " + tokens[i]);
            }
           
            System.out.println("\nLength of tokens after stemming = " + tokens.length);
            
            m_TokenManager.StoreTokens(tokens);
            m_TokenManager.setOriginalTokens(tokensList);
            /*NOTE - Till here the ArrayList contains the tokens after 
             * stopword removal and tokens array contains the tokens after 
             * stopword removal and stemming*/
            /*There is no programmatic problem with stopword removal 
             * and stemming till here*/
    }

  
}
