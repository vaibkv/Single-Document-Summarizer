/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package edu.research.textsummarization.controller;

import edu.research.textsummarization.util.Constants;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Comparator;

/**
 *
 * @author Administrator
 */
public class TokenManager 
{
private static TokenManager m_SelfRef = null;
private  Hashtable<String,String> m_TokensTable = new Hashtable<String,String>();
private ArrayList<Token> m_ThemeWordList = new ArrayList<Token>();   
private ArrayList<String> originalTokens = new ArrayList<String>();
private TokenManager()
{
    
}

public static TokenManager getInstance()
{
    if(m_SelfRef==null)
    {
        m_SelfRef = new TokenManager();
    }
    return m_SelfRef;
}
    public void StoreTokens(String tokens[]) 
	{
        /*Code for checking this method flow
        System.out.println("Checking StoreTokens....");
        for (String s : tokens)
        {
            System.out.print( s + " ");
        }
        All working well till here*/
        
        for (String token : tokens) 
	{
            
            if (m_TokensTable.get(token) == null) 
            {
                m_TokensTable.put(token, "1");
            } else 
            {
                int frequency = Integer.parseInt(m_TokensTable.get(token));
                frequency++;
                m_TokensTable.put(token, frequency + "");
                /*System.out.print(token + " ");*/
                
            } /* m_TokensTable maps unique tokens(stemmed,stopword removed) to their frequencies*/

        }
                
                
                String key, value;
                Enumeration<String> enume = m_TokensTable.keys();
                System.out.println("\nPrinting values from hashtable of tokens and their frequencies");
                for (;enume.hasMoreElements();)
                {
                    key = enume.nextElement();
                    value = m_TokensTable.get(key);
                    System.out.println(key + " " + value);
                }
        
        int maxFrequency = findMaxFrequency(m_TokensTable);
        System.out.println("maxFrequency = " + maxFrequency);
        //  System.out.println("hashcontent="+tokenHashtable.toString());
       
        
        /*The following code calculates the normalized frequency of each token*/
        Enumeration<String> keys = m_TokensTable.keys();
        while(keys.hasMoreElements())
        {
            try
            {
            String word = keys.nextElement();
            double weight =(double)Integer.parseInt(m_TokensTable.get(word))/maxFrequency;
            BigDecimal b = new BigDecimal(weight).setScale(Constants.SETPRECISIONFORWEIGHT,BigDecimal.ROUND_HALF_UP);
            weight=b.doubleValue();
            m_TokensTable.put(word,weight+"");
            Token t = new Token(word);                                                                                                                                                                                                                                                                                                                                                            Token token = new Token(word);
            t.setWeight(weight);
            m_ThemeWordList.add(t);
             }catch(NumberFormatException ex)
            {
                continue;
            }
        }
                /*checking code again*/
                String newkey, newvalue;
                Enumeration<String> enume2 = m_TokensTable.keys();
                System.out.println("\nPrinting values from hashtable of tokens and their frequencies");
                for (;enume2.hasMoreElements();)
                {
                    newkey = enume2.nextElement();
                    newvalue = m_TokensTable.get(newkey);
                    System.out.println(newkey + " " + newvalue);
                }   
    
     SortThemeWords comp = new SortThemeWords();           
     Collections.sort(m_ThemeWordList, comp); 
     //Token[] themeWordsCheck = m_ThemeWordList.toArray(new Token[m_ThemeWordList.size()]);
     
     //Checking if theme word tokens have been sorted as required
     System.out.println("Printing sorted theme tokens in descending order of weight....");
     for(Token theme : m_ThemeWordList)
     {
         System.out.println(theme.getWord() + " " + theme.getWeight());
     }
     
     System.out.println("tokentable content="+m_TokensTable.toString());
     System.out.println("themeword content="+m_ThemeWordList.toString());
    }
    
    
    private int findMaxFrequency(Hashtable<String, String> tokens) 
	{
        System.out.println("all working well till here in findMaxFrequency");
        Enumeration<String> values = tokens.elements();
        int maxFrequency = 0;
        while (values.hasMoreElements()) 
        {
            try 
            {
                
                /*int i;
                for(i=0;values.hasMoreElements();i++)
                values.nextElement();
                System.out.println(i); */

                int frequency = Integer.parseInt(values.nextElement());
                
                System.out.println(frequency); /*all well till here*/
                if (frequency > maxFrequency) 
                {
                    maxFrequency = frequency;
                }
            } catch (NumberFormatException ex) 
            {
                continue;
            }

        }
        System.out.println("Printing maxFrequency before returning value " + maxFrequency);
        return maxFrequency;
    }
    
  /*public double getThemewordweight(String word)
  {
        if(word==null)
        {
            throw new NullPointerException("word is null in getThemeWordWeight");
        }
        System.out.println("Printing theme words : TokenManager getThemewordweight()...");
        for(int i=0;i<Constants.NO_OF_THEMEWORDS;i++)
        {
            Token token = m_ThemeWordList.get(i);
            
            if(word.equals(token.getWord()))
            {
                System.out.println(token.getWord() + " " + token.getWeight());
                return token.getWeight();
            }
            
        }
        
        return 0.0;
    }*/
  
   public double getTokenWeight(String token)
   {
       if(token==null)
       {
           throw new NullPointerException("token is null");
       }
       String w = m_TokensTable.get(token);
       double weight = Double.parseDouble(w);
       System.out.println("token and its weight before returning frm getTokenWeight " + token + " " + weight);
       return weight;
   }
   
   public void setOriginalTokens(ArrayList<String> originalTokens)
   {
       this.originalTokens = originalTokens ;
       /*checking if tokens have been set right*/
       //String[] tok = this.originalTokens.toArray(new String[this.originalTokens.size()]) ;
       for(String p : this.originalTokens)
       {
           System.out.println(p);
       }
   
   }
   
   public ArrayList<String> getOriginalTokens()
   {
       return originalTokens;
   }
   
   public double getThemeWordRatio(String[] tokens)
   {
       double returnRatio = 0.0;
       int themeWordCount = 0;
       int tokenCount = tokens.length;
       for(String t : tokens)
       {
           if(t==null)
        {
            throw new NullPointerException("word is null in getThemeWordWeight");
        }
           for(int i=0;i<Constants.NO_OF_THEMEWORDS;i++)
           {
               Token tok = m_ThemeWordList.get(i);
               String word = tok.getWord();
               if(t.equals(word))
               {
                   System.out.println(t + " is a theme word");
                   themeWordCount++;break;
               }
           }
           
       }
       returnRatio = (double)themeWordCount/tokenCount;
       System.out.println("Theme word ratio : return = " + returnRatio);
       return returnRatio;
       
   }
   
   public Hashtable<String,String> getTokensTable()
   {
       return m_TokensTable;
   }


}


class SortThemeWords implements Comparator<Token>
{
    public int compare(Token a, Token b)
    {
        double a_weight = a.getWeight();
        double b_weight = b.getWeight();
        Double A_weight = new Double(a_weight);
        Double B_weight = new Double(b_weight);
        return (B_weight.compareTo(A_weight));
    }
}
