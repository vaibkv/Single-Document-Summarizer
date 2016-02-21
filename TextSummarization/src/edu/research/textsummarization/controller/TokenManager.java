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

/**
 *
 * @author Administrator
 */
public class TokenManager {
private static TokenManager m_SelfRef = null;
private  Hashtable<String,String> m_TokensTable = new Hashtable<String,String>();
private ArrayList<Token> m_ThemeWordList = new ArrayList<Token>();   
private TokenManager(){
    
}

public static TokenManager getInstance(){
    if(m_SelfRef==null){
        m_SelfRef = new TokenManager();
    }
    return m_SelfRef;
}
    public void StoreTokens(String tokens[]) 
	{
          
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
            }

        }
        /*Enumeration<String> enume = m_TokensTable.keys();
                System.out.println("\nPrinting values from hashtable of tokens and their frequencies");
                for (;enume.hasMoreElements();)
                {
                    System.out.println(enume.nextElement()+ " " + m_TokensTable.get(enume.nextElement()));
                }*/
        int maxFrequency = findMaxFrequency(m_TokensTable);
      //  System.out.println("hashcontent="+tokenHashtable.toString());
       
        Enumeration<String> keys = m_TokensTable.keys();
        while(keys.hasMoreElements()){
            try{
            String word = keys.nextElement();
            double weight =(double)Integer.parseInt(m_TokensTable.get(word))/maxFrequency;
            BigDecimal b = new BigDecimal(weight).setScale(Constants.SETPRECISIONFORWEIGHT,BigDecimal.ROUND_HALF_UP);
            weight=b.doubleValue();
            m_TokensTable.put(word,weight+"");
            Token token = new Token(word);
            token.setWeight(weight);
            m_ThemeWordList.add(token);
             }catch(NumberFormatException ex){
                continue;
            }
        }
     Collections.sort(m_ThemeWordList); 
     System.out.println("tokentable content="+m_TokensTable.toString());
     System.out.println("themeword content="+m_ThemeWordList.toString());
    }
    
                
    private int findMaxFrequency(Hashtable<String, String> tokens) 
	{
        Enumeration<String> values = tokens.elements();
        int maxFrequency = 0;
        while (values.hasMoreElements()) {
            try {

                int frequency = Integer.parseInt(values.nextElement());
                if (frequency > maxFrequency) {
                    maxFrequency = frequency;
                }
            } catch (NumberFormatException ex) {

                continue;
            }

        }
        return maxFrequency;
    }
    
  public double getThemewordweight(String word){
        if(word==null){
            throw new NullPointerException("word is nulk in getThemeWordWEIGHT");
        }
        for(int i=0;i<Constants.NO_OF_THEMEWORD;i++){
            Token token = m_ThemeWordList.get(i);
            if(word.equals(token.getWord())){
                return token.getWeight();
            }
        }
        return 0.0;
    }
  
   public double getTokenWeight(String token){
       if(token==null){
           throw new NullPointerException("token is null");
       }
       String  w = m_TokensTable.get(token);
       double weight = Double.parseDouble(w);
       return weight;
   }
  
}
