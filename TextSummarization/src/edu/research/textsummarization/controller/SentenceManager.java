 /*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package edu.research.textsummarization.controller;

import edu.research.textsummarization.util.Constants;
import edu.research.textsummarization.util.StringUtils;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Iterator;

/**
 *
 * @author Administrator
 */
public class SentenceManager
{
private static SentenceManager m_SelfRef=null;
private  Hashtable<Integer,Sentence> m_SentenceTable = new Hashtable<Integer, Sentence>();
private ArrayList<Sentence> m_SentencesList = new ArrayList<Sentence>();
private ArrayList<Sentence> m_TopWeightedSentenceList = new ArrayList<Sentence>();
private TokenManager m_tokenManager = null;
private String m_DocTitleSentence;
private String m_refererText = "He he she She it It";
private SentenceManager()
{
   
}
private void init()
{
    m_tokenManager = TokenManager.getInstance(); 
}
public static SentenceManager getInstance()
{
    if(m_SelfRef==null)
	{
        m_SelfRef = new SentenceManager();
        m_SelfRef.init();
    }
    return m_SelfRef;
}

public void extractSentences(String text)
{
    //String[] sentences = text.split(".");
    String[] sentences = StringUtils.split(text, '.');
    System.out.println("no. of sentences = "+sentences.length);
    m_DocTitleSentence = sentences[0];
    int sentenceNumber = 1;
    for (String sentence : sentences) 
	{
        Sentence sentenceObject = new Sentence(sentenceNumber, sentence);
        m_SentenceTable.put(new Integer(sentenceNumber),sentenceObject);
        m_SentencesList.add(sentenceObject);
        sentenceNumber++;
        
    }

}
public void calculateAllSentencesWeight()
{
    Iterator<Sentence> sentences = m_SentencesList.iterator();
    while(sentences.hasNext())
	{
        Sentence sentence = sentences.next();
        double weight = getSentenceWeight(sentence);
        sentence.setWeight(weight);
    }
}
public ArrayList<Sentence> extractTopWeightedSentanceAndArrange()
{
    
    int summarySize = ((m_SentencesList.size()*40)/100);
    System.out.println("summarysize="+summarySize);
    
    Collections.sort(m_SentencesList, new Comparator(){

            public int compare(Object o1, Object o2) {
                if(o1==null || o2==null){
                    throw new RuntimeException("o1 or o2 can not be null");
                }
                if((o1 instanceof Sentence)&&(o2 instanceof Sentence)){
                    Sentence sentence1 = (Sentence)o1;
                    Sentence sentence2 = (Sentence)o2;
                    if(sentence1.getWeight()>sentence2.getWeight()){
                        return -1;
                    }
                    else if(sentence1.getWeight()==sentence2.getWeight()){
                        return 0;
                    }else{
                        return 1;
                    }
                    
                }else{
                    throw new ClassCastException("wrong object type");
                }
            }
        
    });
    
     for(int i=0;i<summarySize;i++)
    {
        m_TopWeightedSentenceList.add(m_SentencesList.get(i));
    }
   
    Collections.sort(m_TopWeightedSentenceList, new Comparator(){

            public int compare(Object o1, Object o2) {
                if(o1==null || o2==null){
                    throw new RuntimeException("o1 or o2 can not be null");
                }
                if((o1 instanceof Sentence)&&(o2 instanceof Sentence)){
                    Sentence sentence1 = (Sentence)o1;
                    Sentence sentence2 = (Sentence)o2;
                    if(sentence1.getSentenceNo()>sentence2.getSentenceNo()){
                        return 1;
                    }
                    else if(sentence1.getSentenceNo()==sentence2.getSentenceNo()){
                        return 0;
                    }else{
                        return -1;
                    }
                    
                }else{
                    throw new ClassCastException("wrong object type");
                }
            }
        
    });
   
   return m_TopWeightedSentenceList; 
}
private double getSentenceWeight(Sentence sentence){
System.out.println("Entry SentenceManager:getSentenceWeight(): Perameters: sentnce=object of sentence");
double weight = getIndividualWordWeight(sentence)+getThemeWordWeight(sentence)+getTitleWordWeight(sentence)+getWeightDuetoNextSentence(sentence);
System.out.println("Return SentenceManager:getSentenceWeight(): Perameters: weight="+weight);

return weight;
}

private double getIndividualWordWeight(Sentence sentence)
{
 System.out.println("Entry SentenceManager:getIndividualWordWeight(): Perameters: sentnce=object of sentence");
    
    Double sentenceWeight = 0.0;
    if(sentence==null){
        throw new NullPointerException("sentence can not be null");
    }
  
    String sentenceText = sentence.getSentenceText();
   System.out.println("sentenceText="+sentenceText);
    String[] tokens =  sentenceText.split("[-–)+><^=_/%$|( '\",*~`&#@;:\n\t\u0964\u0965\u097D.?!]");
    for (String token : tokens) {
      sentenceWeight+=m_tokenManager.getTokenWeight(token);
    }
    
System.out.println("sentenceWeight="+sentenceWeight);  
return sentenceWeight;
}
private double getThemeWordWeight(Sentence sentence)
{
 System.out.println("Entry SentenceManager:getThemeWordWeight(): Perameters: sentnce=object of sentence");
    
    Double sentenceWeight = 0.0;
    if(sentence==null){
        throw new NullPointerException("sentence can not be null");
    }
    String sentenceText = sentence.getSentenceText();
    System.out.println("senteceText="+sentenceText);
    String[] tokens =  sentenceText.split("[-–)+><^=_/%$|( '\",*~`&#@;:\n\t\u0964\u0965\u097D.?!]");
    for (String token : tokens) {
      sentenceWeight+=m_tokenManager.getThemewordweight(token);
    }
System.out.println("Return SentenceManager:getThemeWordWeight(): Return="+sentenceWeight);
return sentenceWeight;
}
private double getTitleWordWeight(Sentence sentence)
{
 System.out.println("Entry SentenceManager:getTitleWordWeight(): Perameters: sentnce=object of sentence");
 
    Double sentenceWeight = 0.0;
  
    if(sentence==null){
        throw new NullPointerException("sentence can not be null");
    }
    String sentenceText = sentence.getSentenceText();
    System.out.println("sentenceText="+sentenceText);
    String[] tokens =  sentenceText.split("[-–)+><^=_/%$|( '\",*~`&#@;:\n\t\u0964\u0965\u097D.?!]");
   
      for (String token : tokens) {
      if(m_DocTitleSentence.contains(token)){
          sentenceWeight += m_tokenManager.getTokenWeight(token);
      }
    }
  System.out.println("Return SentenceManager:getTitleWordWeight() Return="+sentenceWeight);
  
    return sentenceWeight;
    
}
private double getWeightDuetoNextSentence(Sentence sentence)
{
    System.out.println("Entry SentenceManager:getWeightDuetoNextSentence(): Perameters: sentnce=object of sentence");
    int sentenceNo = sentence.getSentenceNo();
    String sentenceText = sentence.getSentenceText();
    System.out.println("sentenceNo="+sentenceNo+" sentenceText="+sentenceText);
    Sentence nextSentence = m_SentenceTable.get(new Integer(sentenceNo+1));
    if(nextSentence==null){
        System.out.println("no next sentence.");
    System.out.println("Return SentenceManager:getWeightDuetoNextSentence() Return=0.0");
   
        return 0.0;
    }
    String nextSentenceText = nextSentence.getSentenceText();
    System.out.println("nextsentenceText="+nextSentenceText);
    if(nextSentenceText.length()<2)return 0.0;
    String nextSentenceFirstWord =  sentenceText.split("[-–)+><^=_/%$|( '\",*~`&#@;:\n\t\u0964\u0965\u097D.?!]")[0];
  System.out.println("nextSentenceFirstWord="+nextSentenceFirstWord);
    if(m_refererText.contains(nextSentenceFirstWord)){
           System.out.println("Return SentenceManager:getWeightDuetoNextSentence() Return="+Constants.WEIGHT_DUE_TO_HE);
 
       return Constants.WEIGHT_DUE_TO_HE;
   }
     System.out.println("Return SentenceManager:getWeightDuetoNextSentence() Return=0.0");
 
  return 0.0;
}
}
