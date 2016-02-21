/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package edu.research.textsummarization.controller;

/**
 *
 * @author Administrator
 */
public class Token implements Comparable
{

//private String original_text_token;
private String m_Word; //stemmed version
private double m_WordWeight; //contains normalized frequency

public Token(String Word)
{
    
    m_Word = Word;
    m_WordWeight = 0;
}

public void setWeight(double weight)
{
    m_WordWeight = weight;
}

public double getWeight()
{
    return m_WordWeight;
}

public String getWord()
{
    return m_Word;
}
    public int compareTo(Object object) 
    {
        if(object == null)
        {
            throw new NullPointerException("object is null");
        }
       if(!(object instanceof Token))
       {
           throw new RuntimeException("object is not instance of token");
       }
       Token token = (Token)object;
       if(this.m_WordWeight>token.m_WordWeight) 
       {
           return -1;
       } else if(this.m_WordWeight<token.m_WordWeight) 
       {
           return 1;
       }else
       {
           return 0;
       }     
    }

    @Override
    public boolean equals(Object object) 
    {
             if(object == null)
        {
            throw new NullPointerException("object is null");
        }
       if(!(object instanceof Token))
       {
           throw new RuntimeException("object is not instance of token");
       }
       Token token = (Token)object;
       if(this.m_Word.trim().equals(token.m_Word.trim()))
       {
           return true;
       }else
       {
           return false;
       }
    }

    @Override
    public int hashCode() 
    {
           
      return  m_Word.hashCode();
      
           
    }
    
   
           
           
}
