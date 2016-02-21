 /*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package edu.research.textsummarization.controller;

//import edu.research.textsummarization.util.Constants;
import edu.research.textsummarization.gui.TextSummarizerGUI;
import edu.research.textsummarization.util.*;
//import edu.research.textsummarization.gui.TextSummarizerGUI;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Hashtable;
import java.util.Iterator;
import java.math.BigDecimal;
import java.util.regex.Pattern;
import java.util.regex.Matcher;
import java.util.Enumeration;
//import java.util.Collection;


/**
 *
 * @author Administrator
 */
public class SentenceManager
{
private TextSummarizerGUI gui = null;
private static SentenceManager m_SelfRef=null;
private Hashtable<Integer,Sentence> m_SentenceTable = new Hashtable<Integer, Sentence>();
private ArrayList<Sentence> m_SentencesList = new ArrayList<Sentence>();
private ArrayList<Sentence> m_TopWeightedSentenceList = new ArrayList<Sentence>();
private TokenManager m_tokenManager = null;
private String m_DocTitleSentence;
private String m_refererText = "He She It Her His We Our These This They Those That Their Ours Theirs ";
private ArrayList<String> originalTokens = new ArrayList<String>();
private ArrayList<String> docTitleStemmedTokens = new ArrayList<String>();
private int totalNumberOfSentences ;
private ArrayList<String> topicWords = new ArrayList<String>();
private Hashtable<Integer,ArrayList<String>> sentence_tokens = new Hashtable<Integer,ArrayList<String>>();
//private ArrayList<WordScore> word_tfisf_dens_list = new ArrayList<WordScore>();
private Hashtable<String,ArrayList<WordTF>> word_norm_sent_tf = new Hashtable<String,ArrayList<WordTF>>();
private Hashtable<String,Double> word_isf = new Hashtable<String,Double>();
private Hashtable<String,Double> word_dens = new Hashtable<String,Double>();
private ArrayList<WordScore> word_tf_isf_dens_list = new ArrayList<WordScore>();


private SentenceManager()
{
   
}
private void init()
{
    m_tokenManager = TokenManager.getInstance(); 
    gui = TextSummarizerGUI.getInstance();
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
    System.out.println("NUMBER OF SENTENCES = "+sentences.length);
    m_DocTitleSentence = sentences[0];
    int sentenceNumber = 1;
    for (String sentence : sentences) 
    {
        Sentence sentenceObject = new Sentence(sentenceNumber, sentence);
        m_SentenceTable.put(new Integer(sentenceNumber),sentenceObject);
        m_SentencesList.add(sentenceObject);
        sentenceNumber++;
        
    }
    totalNumberOfSentences = sentences.length;

}
public void calculateAllSentencesWeight()
{
    setOriginalTokens();
    Iterator<Sentence> sentences = m_SentencesList.iterator();
    while(sentences.hasNext())
    {
        Sentence sentence = sentences.next();
        double weight = getSentenceWeight(sentence);
        sentence.setWeight(weight);
        System.out.println("Sentence " + sentence.getSentenceNo() + " has a weight of " + sentence.getWeight());
    }
}
public ArrayList<Sentence> extractTopWeightedSentanceAndArrange()
{
    
    int summarySize = ((m_SentencesList.size()*Constants.SUMMARY_SIZE_PERCENTAGE)/100);
    System.out.println("summarysize="+summarySize);
    
    Collections.sort(m_SentencesList, new Comparator()
    {

            public int compare(Object o1, Object o2) {
                if(o1==null || o2==null){
                    throw new RuntimeException("o1 or o2 can not be null");
                }
                if((o1 instanceof Sentence)&&(o2 instanceof Sentence))
                {
                    Sentence sentence1 = (Sentence)o1;
                    Sentence sentence2 = (Sentence)o2;
                    if(sentence1.getWeight()>sentence2.getWeight())
                    {
                        return -1;
                    }
                    else if(sentence1.getWeight()==sentence2.getWeight())
                    {
                        return 0;
                    }else
                    {
                        return 1;
                    }
                    
                }else
                {
                    throw new ClassCastException("wrong object type");
                }
            }
        
    });
    
     for(int i=0;i<summarySize;i++)
    {
        m_TopWeightedSentenceList.add(m_SentencesList.get(i));
    }
   
    Collections.sort(m_TopWeightedSentenceList, new Comparator()
    {

            public int compare(Object o1, Object o2) 
            {
                if(o1==null || o2==null)
                {
                    throw new RuntimeException("o1 or o2 can not be null");
                }
                if((o1 instanceof Sentence)&&(o2 instanceof Sentence))
                {
                    Sentence sentence1 = (Sentence)o1;
                    Sentence sentence2 = (Sentence)o2;
                    if(sentence1.getSentenceNo()>sentence2.getSentenceNo())
                    {
                        return 1;
                    }
                    else if(sentence1.getSentenceNo()==sentence2.getSentenceNo())
                    {
                        return 0;
                    }else
                    {
                        return -1;
                    }
                    
                }else
                {
                    throw new ClassCastException("wrong object type");
                }
            }
        
    });
   
   return m_TopWeightedSentenceList; 
}
private double getSentenceWeight(Sentence sentence)
{
    
System.out.println("Entry SentenceManager:getSentenceWeight()");
double weight = Constants.ALPHA*getSentenceLocWeight(sentence) + Constants.BETA*getWeightDuetoNextSentence(sentence) + Constants.GAMMA*getTitleWordWeight(sentence) + Constants.DELTA*getIndividualWordWeight(sentence)+ Constants.EPSILON*getThemeWordWeight(sentence) + Constants.ZETA*getProperNounsWeight(sentence) + Constants.ETA*getCueWordsWeight(sentence) + Constants.THETA*getTopicWordsWeight(sentence) + Constants.IOTA*getSentenceLengthWeight(sentence) + Constants.KAPPA*getPunctuationWeight(sentence) + Constants.LAMBDA*getNumericDataWeight(sentence);
System.out.println("Return SentenceManager:getSentenceWeight() weight="+weight);

return weight;
}

private double getIndividualWordWeight(Sentence sentence)
{
 System.out.println("Entry SentenceManager:getIndividualWordWeight()");
 //all well till here   
 Double sentenceWeight = 0.0;
 if(sentence==null)
 {
      throw new NullPointerException("sentence can not be null");
 }
  
   String sentenceText = sentence.getSentenceText();
   System.out.println("sentenceText="+sentenceText);
   String[] tokens =  sentenceText.split("[-–)+><^=_/%$|( '\",*~`&#@;:\n\t\u0964\u0965\u097D.?!]");
   ArrayList<String> usableTokens = getUsableTokens(tokens);
   String[] stemmedSentenceTokens = usableTokens.toArray(new String[usableTokens.size()]);
   System.out.println("Printing stemmed tokens for this sentence : getIndividualWordWeight()");
   
   for (String t : stemmedSentenceTokens)
   {
       System.out.println(t);
   }
   /*working well till here*/
   for (String token : stemmedSentenceTokens) 
   {
   //We have to send the stemmed version of token here    
   sentenceWeight+=m_tokenManager.getTokenWeight(token);
   System.out.println("Weight from getTokenWeight after returning = " + m_tokenManager.getTokenWeight(token));
   }
    
System.out.println("sentenceWeight from Individual word weight="+sentenceWeight);  
return sentenceWeight;
}

private double getThemeWordWeight(Sentence sentence)
{
 System.out.println("Entry SentenceManager:getThemeWordWeight()");
    
    Double sentenceWeight = 0.0;
    if(sentence==null)
    {
        throw new NullPointerException("sentence can not be null");
    }
    String sentenceText = sentence.getSentenceText();
    System.out.println("senteceText="+sentenceText);
    String[] tokens =  sentenceText.split("[-–)+><^=_/%$|( '\",*~`&#@;:\n\t\u0964\u0965\u097D.?!]");
    ArrayList<String> usableThemeTokens = getUsableTokens(tokens);
    String[] stemmedThemeTokens = usableThemeTokens.toArray(new String[usableThemeTokens.size()]);
    System.out.println("Printing stemmed words for this sentence : getThemeWordWeight()");
   
    for (String t : stemmedThemeTokens)
    {
        System.out.println(t);
    }
    /*for (String token : stemmedThemeTokens) 
    {
        sentenceWeight+=m_tokenManager.getThemewordweight(token);
    }*/
    sentenceWeight = m_tokenManager.getThemeWordRatio(stemmedThemeTokens);
System.out.println("Return SentenceManager:getThemeWordWeight(): Return="+sentenceWeight);
return sentenceWeight;
}

private double getTitleWordWeight(Sentence sentence)
{
 System.out.println("Entry SentenceManager:getTitleWordWeight()");
 
    
 setDocTitleStemmedTokens(m_DocTitleSentence);
 Double sentenceWeight = 0.0;
 int sentenceTokenCount = 0;   
    if(sentence==null)
    {
        throw new NullPointerException("sentence can not be null");
    }
    String sentenceText = sentence.getSentenceText();
    System.out.println("sentenceText="+sentenceText);
    String[] tokens =  sentenceText.split("[-–)+><^=_/%$|( '\",*~`&#@;:\n\t\u0964\u0965\u097D.?!]");
    ArrayList<String> usableTitleTokens = getUsableTokens(tokens);
    String[] stemmedTitleTokens = usableTitleTokens.toArray(new String[usableTitleTokens.size()]);
    int docTitleCount = docTitleStemmedTokens.size();
    System.out.println("Number of tokens in docTitle = " + docTitleCount);
    System.out.println("Printing stemmed tokens for this sentence : getIndividualWordWeight()");
    
     for (String t : stemmedTitleTokens)
    {
        System.out.println(t);
        if(docTitleStemmedTokens.contains(t)) 
        { 
            System.out.println(t + " is a title word");sentenceTokenCount++; 
        }
    }
     sentenceWeight = (double)sentenceTokenCount/stemmedTitleTokens.length;
     
     // for (String token : tokens) 
      //{
      //if(m_DocTitleSentence.contains(token))
      //{
       //   sentenceWeight += 0.15;/*m_tokenManager.getTokenWeight(token);*/
      //}
    //}
  System.out.println("Return SentenceManager:getTitleWordWeight() Return="+sentenceWeight);
  
    return sentenceWeight;
    
}

private double getWeightDuetoNextSentence(Sentence sentence)
{
    double weight = 0.0;
    System.out.println("Entry SentenceManager:getWeightDuetoNextSentence()");
    int sentenceNo = sentence.getSentenceNo();
    String sentenceText = sentence.getSentenceText();
    System.out.println("sentenceNo="+sentenceNo+" sentenceText="+sentenceText);
    if(sentenceNo >= totalNumberOfSentences) { return weight; }
    for(int i =  m_SentenceTable.get(new Integer(sentenceNo+1)).getSentenceNo();i<=totalNumberOfSentences;i++)
    {
     Sentence nextSentence = m_SentenceTable.get(new Integer(i));
    if(nextSentence==null)
    {
    System.out.println("no next sentence.");
    System.out.println("Return SentenceManager:getWeightDuetoNextSentence() Return=0.0");
   
        weight+=0.0 ; return weight;
    }
    String nextSentenceText = nextSentence.getSentenceText();
    
    System.out.println("nextsentenceText= "+nextSentenceText);
    Pattern p = Pattern.compile("[a-zA-Z0-9]+");
    Matcher m = p.matcher(nextSentenceText);
    int k=0;
    while(m.find())
    {
        k++;
    }
    if(k<2){continue;}
    String nextSentenceFirstWord =  nextSentenceText.split("[-–)+><^=_/%$|( '\",*~`&#@;:\n\t\u0964\u0965\u097D.?!]")[1];
    System.out.println("nextSentenceFirstWord="+nextSentenceFirstWord);
    if(m_refererText.contains(nextSentenceFirstWord))
    {
         weight += Constants.WEIGHT_DUE_TO_HE;
    }
    }
     System.out.println("Return SentenceManager:getWeightDuetoNextSentence() Return= " + weight);
 
  return weight;
}

public void setOriginalTokens()
{
    originalTokens = m_tokenManager.getOriginalTokens();
    System.out.println("\nEntering SentenceManager : setOriginalTokens()");
    String[] a = originalTokens.toArray(new String[originalTokens.size()]);
    for(String b : a)
    {
        System.out.println(b);
    }
}

public ArrayList<String> getUsableTokens(String[] tokens)
{
    ArrayList<String> sentenceTokens = new ArrayList<String>();
    
    for(String token : tokens)
    {
        if(originalTokens.contains(token))
        {
            String t = getStemmedVersion(token);
            sentenceTokens.add(t);
            
        }
        else continue;
    }
    return sentenceTokens;
}

public String getStemmedVersion(String s)
{
    String t = PorterStemmer.stem(s);
    return t;
}

public double getSentenceLocWeight(Sentence sentence)
{
    System.out.println("Entry SentenceManager : getSentenceLocWeight()");
    double weight = 0.0;
    int s_num = sentence.getSentenceNo();
    double normalized_num = (double)s_num/totalNumberOfSentences;
    BigDecimal b = new BigDecimal(normalized_num).setScale(2, BigDecimal.ROUND_HALF_UP);
    normalized_num = b.doubleValue();
    
    if(normalized_num > 0.0 && normalized_num <= 0.1)
    {
        weight = 0.17;
    }
    else if(normalized_num > 0.1 && normalized_num <= 0.2)
    {
        weight = 0.23;
    }
    else if(normalized_num > 0.2 && normalized_num <= 0.3)
    {
        weight = 0.14;
    }
    else if(normalized_num > 0.3 && normalized_num <= 0.4)
    {
        weight = 0.08;
    }
    else if(normalized_num > 0.4 && normalized_num <= 0.5)
    {
        weight = 0.05;
    }
    else if(normalized_num > 0.5 && normalized_num <= 0.6)
    {
        weight = 0.04;
    }
    else if(normalized_num > 0.6 && normalized_num <= 0.7)
    {
        weight = 0.06;
    }
    else if(normalized_num > 0.7 && normalized_num <= 0.8)
    {
        weight = 0.04;
    }
    else if(normalized_num > 0.8 && normalized_num <= 0.9)
    {
        weight = 0.04;
    }
    else if(normalized_num > 0.9 && normalized_num <= 1.0)
    {
        weight = 0.15;
    }
    else
    { weight = 0.0;}
    System.out.println("sentence location weight of sentence number " + s_num + " is " + weight);
    System.out.println("normalized sentence number = "+ normalized_num);
    return weight;
}

public double getProperNounsWeight(Sentence sentence)
{
   System.out.println("Entry SentenceManager:getProperNounsWeight()");
 
 double sentenceWeight = 0.0;
 int flag = 0,properNounCount = 0;
 if(sentence==null)
 {
      throw new NullPointerException("sentence can not be null");
 }
  
   String sentenceText = sentence.getSentenceText();
   System.out.println("sentenceText="+sentenceText);
   String[] tokens =  sentenceText.split("[-–)+><^=_/%$|( '\",*~`&#@;:\n\t\u0964\u0965\u097D.?!]");
   ArrayList<String> usableTokens = getUsableTokens(tokens);
   String[] stemmedSentenceTokens = usableTokens.toArray(new String[usableTokens.size()]);
   System.out.println("Printing stemmed tokens for this sentence : getProperNounsWeight()");
   
   for (String t : stemmedSentenceTokens)
   {
       System.out.println(t);
   }
    System.out.println("Trying to identify proper nouns..");
    System.out.println("number of stemmed elements in this sentence =" + stemmedSentenceTokens.length);
        for(int i=0;i<stemmedSentenceTokens.length;i++)
        {
            if((flag==0) && (stemmedSentenceTokens[i]!=null) && (properNounCount==0)) {i++;flag=1;}
            if(i >= stemmedSentenceTokens.length) break;
            if(Character.isUpperCase(stemmedSentenceTokens[i].trim().toCharArray()[0]))
            {
                properNounCount++;
                System.out.println(stemmedSentenceTokens[i] + " is a proper noun");
            }
            
        }
        System.out.println("number of proper nouns = " + properNounCount);
        sentenceWeight = (double)properNounCount/stemmedSentenceTokens.length;
        System.out.println("proper nouns ratio:return = " + sentenceWeight);
        return sentenceWeight;

}


/*we have to ignore case here*/
public double getCueWordsWeight(Sentence sentence)
{
    int numOfCueToks = 0;
    System.out.println("Entry SentenceManager:getCueWordsWeight()");
    Double sentenceWeight = 0.0;
 if(sentence==null)
 {
      throw new NullPointerException("sentence can not be null");
 }
  
   String sentenceText = sentence.getSentenceText();
   System.out.println("sentenceText="+sentenceText);
   String[] tokens =  sentenceText.split("[-–)+><^=_/%$|( '\",*~`&#@;:\n\t\u0964\u0965\u097D.?!]");
   //ArrayList<String> cue_toks = new ArrayList<String>();
   /*System.out.println("Printing raw tokens(no stemming or stopword removal)");
   for(String token : tokens)
   {
       System.out.print(token + " ");
       //toks.add(token);
       
   }
   System.out.println();*/
   /*for(int i=0;i<Constants.CUE_PHRASE.length;i++)
   {
       cue_toks.add(Constants.CUE_PHRASE[i]);
   }
   for(int i=0;i<tokens.length;i++)
   {
       if(cue_toks.contains(token))
       {
           numOfCueToks++;
           System.out.println(token + " is a cue word");
       }
   }*/
   
   //if u face some problem comparing this way then it can also be done by not tokensizing but searching the full sentence text using contains
   //for(int i=0;i<tokens.length;i++)
   //{
       for(int j=0;j<Constants.CUE_PHRASE.length;j++)
       {
           if(sentenceText.toLowerCase().contains(Constants.CUE_PHRASE[j]))
           {
               System.out.println( Constants.CUE_PHRASE[j]+ " is a cue word");
               numOfCueToks++;
               //break;
           }
       }
       
   //}
   System.out.println("Number of cue tokens in the sentence = " + numOfCueToks);
   sentenceWeight = (double)numOfCueToks/tokens.length;
   System.out.println("Cue words return ratio = " + sentenceWeight);
   
    
   return sentenceWeight;
}

public double getTopicWordsWeight(Sentence sentence)
{
    System.out.println("Entry SentenceManager:getTopicWordsWeight()");
    double weight = 0.0;
    int num_of_topic_words = 0;
    topicWords = getTopicWords();
    if(sentence==null)
 {
      throw new NullPointerException("sentence can not be null");
 }
  
   String sentenceText = sentence.getSentenceText();
   System.out.println("sentenceText="+sentenceText);
   String[] tokens =  sentenceText.split("[-–)+><^=_/%$|( '\",*~`&#@;:\n\t\u0964\u0965\u097D.?!]");
   ArrayList<String> usableTokens = getUsableTokens(tokens);
   String[] stemmedSentenceTokens = usableTokens.toArray(new String[usableTokens.size()]);
   for(String tok : stemmedSentenceTokens)
   {
       if(topicWords.contains(tok))
       {
           num_of_topic_words++;
       }
   }
   weight = (double)num_of_topic_words/stemmedSentenceTokens.length;
   System.out.println("Topic words weight: return = " + weight);
    
    return weight;
}

public double getSentenceLengthWeight(Sentence sentence)
{
    System.out.println("Entry SentenceManager:getSentenceLengthWeight()");
    double weight = 0.0;
    int maxSentenceLength = findMaxSentenceLength();
    String s_text = sentence.getSentenceText();
    String[] tok =  s_text.split("[-–)+><^=_/%$|( '\",*~`&#@;:\n\t\u0964\u0965\u097D.?!]");
    int s_length = tok.length;    
    System.out.println("Present sentence length = " + s_length);
    weight = (double)s_length/maxSentenceLength;
    System.out.println("Weight due to sentence length:return = " + weight);
    return weight;
}

public double getPunctuationWeight(Sentence sentence)
{
    /*counting punctuation */
    System.out.println("Entry SentenceManager:getPunctuationWeight()");
    double weight = 0.0;
    int num_of_words = 0;
    int num_of_punctuation = 0;
    int num_of_interro_exclam = 0;
    int num_of_inverted = 0;
    int right_paren = 0;
    int left_paren = 0;
    int paren_count = 0;
    Pattern pat_punc = Pattern.compile("[!@:;,?-]");
    Matcher mat_punc = pat_punc.matcher(sentence.getSentenceText());
    Pattern pat_words = Pattern.compile("[a-zA-Z0-9]+");
    Matcher mat_words = pat_words.matcher(sentence.getSentenceText());
    System.out.println("Attempting to print words and punctuation matched by matchers");
    System.out.println("Words from sentence:");
    while(mat_words.find())
    {
        System.out.println(mat_words.group());
        num_of_words++;
    }
    System.out.println("Punctuation from sentence:");
    while(mat_punc.find())
    {
        System.out.println(mat_punc.group());
        if((mat_punc.group().equals("!")) || (mat_punc.group().equals("?")))
        {
            num_of_interro_exclam++;num_of_punctuation++;
        }
        else if(mat_punc.group().equals("\""))
        {
            num_of_inverted++;
        }
        else if(mat_punc.group().equals("("))
        {
            left_paren++;
        }
        else if(mat_punc.group().equals(")"))
        {
            right_paren++;
        }
        else {num_of_punctuation++;}
    }
    /*checking for consistency*/
    paren_count = (right_paren<=left_paren)?right_paren:left_paren;
    Double temp = (double)num_of_inverted/2;
    num_of_inverted = temp.intValue();
    num_of_punctuation+=(num_of_inverted+paren_count);
    
    System.out.println("number of countable inverted commas = " + num_of_inverted);
    System.out.println("number of countable parenthesis = " + paren_count);
    System.out.println("Total number of countable punctuation = " + num_of_punctuation);
    System.out.println("Total number of words = " + num_of_words);
    double temp_wt = (double)num_of_punctuation/num_of_words;
    double intero_exclam_wt = num_of_interro_exclam*Constants.INTERRO_EXCLAM_WT;
    weight = temp_wt + intero_exclam_wt;
    System.out.println("Punctuation weight:return = " + weight);
    return weight;
}

public double getNumericDataWeight(Sentence sentence)
{
    System.out.println("Entry SentenceManager:getNumericDataWeight()");
    double weight = 0.0;
    int numeric_count = 0;
    int words_count = 0;
    Pattern pat_words = Pattern.compile("[a-zA-Z0-9]+");
    Pattern pat_numericData = Pattern.compile("[0-9]+");
    Matcher mat_words = pat_words.matcher(sentence.getSentenceText());
    Matcher mat_numericData = pat_numericData.matcher(sentence.getSentenceText());
    System.out.println("Printing the numeric data present");
    while(mat_numericData.find())
    {
        System.out.println(mat_numericData.group());
        numeric_count++;
    }
    while(mat_words.find())
    {
        words_count++;
    }
    weight = (double)numeric_count/words_count;
    System.out.println("Numeric data weight:return = " + weight);
    return weight;
}

public void setDocTitleStemmedTokens(String sentence)
{
    System.out.println("Entry Sentencemanager:getDocTitleStemmedTokens()");
    String[] tok =  sentence.split("[-–)+><^=_/%$|( '\",*~`&#@;:\n\t\u0964\u0965\u097D.?!]");
    docTitleStemmedTokens = getUsableTokens(tok);
    System.out.println("printing usable (stemmed) tokens from doc title");
    for(String s : docTitleStemmedTokens)
    {
        System.out.println(s);
    }


}

public int findMaxSentenceLength()
{
    int maxSentenceLength = 0;
    
    for(Sentence s : m_SentencesList)
    {
        String s_text = s.getSentenceText();
        String[] tokens =  s_text.split("[-–)+><^=_/%$|( '\",*~`&#@;:\n\t\u0964\u0965\u097D.?!]");
        int s_length = tokens.length;
        if(s_length >= maxSentenceLength)
        {
            maxSentenceLength = s_length;
        }
        
    }
    System.out.println("Maximum sentence length = " + maxSentenceLength + " from findMaxsentenceLength");
    return maxSentenceLength;
}

public ArrayList<String> getTopicWords()
{
    Integer key;
    double tf_isf_dens_score = 0.0;
    
    int token_tf = 0;
    double tokenNormTF = 0.0;
    int token_in_num_of_sent = 0;
    //double isf = 0.0;
    double isf_score = 0.0;
    double dens_score = 0.0;
    ArrayList<String> value;
    String tok;
    System.out.println("Entry getTopicWords()");
    for(Sentence sent : m_SentencesList)
   {
         if(sent==null)
 {
      throw new NullPointerException("sentence can not be null");
 }
  
   String sentenceText = sent.getSentenceText();
   System.out.println("sentenceText="+sentenceText);
   String[] tokens =  sentenceText.split("[-–)+><^=_/%$|( '\",*~`&#@;:\n\t\u0964\u0965\u097D.?!]");
   ArrayList<String> usableTokens = getUsableTokens(tokens);
   //String[] stemmedSentenceTokens = usableTokens.toArray(new String[usableTokens.size()]);
   Integer s_num = new Integer(sent.getSentenceNo());
   sentence_tokens.put(s_num, usableTokens);
      
   }
   System.out.println("Printing sentence numbers and their stemmed words");
   Enumeration<Integer> s_numbers = sentence_tokens.keys();
   for(;s_numbers.hasMoreElements();)
   {
       key = s_numbers.nextElement();
       value = sentence_tokens.get(key);
       System.out.println("For sentence number" + key + " tokens are\n");
       for(String v : value)
       {
           System.out.print(v+ " ");
       }
   }
   
  
    /*Now we calculate tf.isf score for each stemmed token in each sentence
    * which will help us in finding out the topic segmentation words*/
    Hashtable<String,String> token_table = m_tokenManager.getTokensTable();
    Enumeration<String> uniq_tokens = token_table.keys();
    int sent_num = 0;
    ArrayList<String> use_toks = new ArrayList<String>();
    for(;uniq_tokens.hasMoreElements();)
    {
        ArrayList<WordTF> sent_tf_list = new ArrayList<WordTF>();
        tok = uniq_tokens.nextElement();//1st unique token is tok
        for(Sentence sentence : m_SentencesList)
        {
            sent_num = sentence.getSentenceNo();
            use_toks = sentence_tokens.get(sent_num);
            if(use_toks.contains(tok)) 
            { token_in_num_of_sent++;
            for(String sent_toks : use_toks)
            {
                if(tok.equals(sent_toks))
                {
                    token_tf++;
                }
            }
            WordTF wordTF = new WordTF();
            tokenNormTF = (double)token_tf/use_toks.size();
            wordTF.setTFScore(tokenNormTF);
            wordTF.setSentNum(sent_num);
            wordTF.setWordString(tok);
            sent_tf_list.add(wordTF);
            
            
                        
        }
            
            
    }
            word_norm_sent_tf.put(tok, sent_tf_list);    
            double isf_ratio = (double)totalNumberOfSentences/token_in_num_of_sent;
            isf_score = Math.log(isf_ratio)/Math.log(2.0);
            Double isf = new Double(isf_score);
            word_isf.put(tok,isf);
            dens_score = findWordDensity(tok);
            Double dens = new Double(dens_score);
            //tf_isf_dens_score = isf_score * dens_score;//incorrect
            word_dens.put(tok,dens);
            //Double tfIsfDensScore = new Double(tf_isf_dens_score);
            //WordScore wordScore = new WordScore();
            //wordScore.setScore(tf_isf_dens_score);
            //wordScore.setWordText(tok);
            //word_tf_isf_dens_list.add(wordScore);
            //word score is yet to be calculated
            
            
            
}
        //word_tf_list_comp comp = new word_tf_list_comp();
        //Collections.sort(word_tf_isf_dens_list,comp);
        //now we have to find 1st 4 words and make their ArrayList and send them
        ArrayList<String> foundWords = findWords();
         
        
        return foundWords;

}

public double findWordDensity(String token)
{
    System.out.println("Entry findWordDensity()");
    double density = 0.0;
    int distance = 0;
    double dist = 0.0;
    //double log_dist = 0.0;
    int prev = 0;
    int i = 0;
    Controller control = Controller.getInstance();
    String inputTex = control.getInputDoc();
    Pattern pat = Pattern.compile(token);
    Matcher mat = pat.matcher(inputTex);
    while(mat.find())
    {
        if(i==0)
        {
            distance = distance;
            prev = mat.start();i++;
            System.out.println("Token " + token + " appears for the first time at "+ prev);
        }
        else
        {
            distance = mat.start() - prev;i++;
            prev = mat.start();
            System.out.println("Token " + token + " appears next at "+mat.start());
        }
        dist = (double)(distance+Math.E);
        density += 1/(Math.log(dist));
        
    }
    System.out.println("Density of token "+token+" is:return = "+density);
    
    return density;
}


//private Hashtable<String,ArrayList<WordTF>> word_norm_sent_tf = new Hashtable<String,ArrayList<WordTF>>();
//private Hashtable<String,Double> word_isf = new Hashtable<String,Double>();
//private Hashtable<String,Double> word_dens = new Hashtable<String,Double>();

public ArrayList<String> findWords()
{
    ArrayList<String> words = new ArrayList<String>();
    Enumeration<String> tokens =  word_norm_sent_tf.keys();
    for(;tokens.hasMoreElements();)
    {
        String token = tokens.nextElement();
        ArrayList<WordTF> wordTF = word_norm_sent_tf.get(token);
        for(WordTF wordtf : wordTF)
        {
            double normal_tfScore = wordtf.getTFScore();
            double isf = (double)word_isf.get(token);
            double dens = (double)word_dens.get(token);
            double final_score = isf*dens*normal_tfScore;
            WordScore wordScore = new WordScore();
            wordScore.setScore(final_score);
            wordScore.setWordText(token);
            wordScore.setSentNum(wordtf.getSentNum());
            word_tf_isf_dens_list.add(wordScore);

        }
    }
    word_tf_list_comp comp = new word_tf_list_comp();
    Collections.sort(word_tf_isf_dens_list,comp);
    //print the sorted list here
    
    /*finding the exact 5 words*/
    int i=0;
    for(WordScore ws : word_tf_isf_dens_list)
    {
        if(i==0)
        {
            words.add(ws.getWordText());
            i++;
        }
        else
        {
            if(i<5)
            {
                if(!words.contains(ws.getWordText()))
                {
                    words.add(ws.getWordText());i++;
                }
            }
        }
    }
    //printing the 5 special words
    System.out.println("Printing the topic words");
    for(String w : words)
    {
        System.out.println(w);
    }
    return words;
}

}

class word_tf_list_comp implements Comparator<WordScore>
{
    public int compare(WordScore a, WordScore b)
    {
        double score_a = a.getScore();
        double score_b = b.getScore();
        Double score_A = new Double(score_a);
        Double score_B = new Double(score_b);
        return(score_B.compareTo(score_A)); 
    }
}
