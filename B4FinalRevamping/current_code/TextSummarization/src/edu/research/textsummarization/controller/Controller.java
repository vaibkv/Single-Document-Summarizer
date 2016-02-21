/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package edu.research.textsummarization.controller;

import edu.research.textsummarization.gui.TextSummarizerGUI;
import java.util.ArrayList;
/**
 *
 * @author Administrator
 */
public class Controller 
{
private static Controller m_SelfRef=null;
private SentenceManager m_SentenceManager=null;
private Tokenizer m_Tokenizer = null;
private ArrayList<Sentence> m_TopWeightedSentenceList = new ArrayList<Sentence>();
private String summary;
private String inputDoc;

public Controller()
{
    m_SentenceManager = SentenceManager.getInstance();
}

public static Controller getInstance()
{
    if(m_SelfRef==null)
    {
        m_SelfRef = new Controller();
    }
    return m_SelfRef;
}

public void onSummarizeButtonClick(String inputText)
{
    setInputDoc(inputText);
    m_SentenceManager.extractSentences(inputText);
    m_Tokenizer = Tokenizer.getInstance();
    m_Tokenizer.Tokenize(inputText);
    m_SentenceManager.calculateAllSentencesWeight();
    m_TopWeightedSentenceList =  m_SentenceManager.extractTopWeightedSentanceAndArrange();
    //System.out.println("....................summary.....................");
    //printSummary();
}
public String printSummary()
{
    summary = "";
    for(int i=0;i<m_TopWeightedSentenceList.size();i++)
    {
        Sentence s = m_TopWeightedSentenceList.get(i);
        summary = summary + s.getSentenceText(); 
        /*System.out.println(""+s.getSentenceText());*/
        
    }
    return summary;
}

public void setInputDoc(String input)
{
    inputDoc = input;
}

public String getInputDoc()
{
    return inputDoc;
}

    public static void main(String[] args) 
	{
      TextSummarizerGUI.main(args); 
     //Controller c = Controller.getInstance();
     //String text = TextSummarizerGUI.getTxt();
     //c.onSummarizeButtonClick(text);
        }

}
