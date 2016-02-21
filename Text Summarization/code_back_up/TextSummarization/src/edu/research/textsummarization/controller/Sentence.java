/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package edu.research.textsummarization.controller;

//import java.util.Comparator;

/**
 *
 * @author Administrator
 */
public class Sentence
{
private int m_sentenceNumber;
private String m_Sentence;
private double m_SentenceWeight;
public Sentence(int sentenceNumber,String sentence) 
{
    m_sentenceNumber = sentenceNumber;
    m_Sentence = sentence;
    m_SentenceWeight = 0;
}
public void setWeight(double weight)
{
    m_SentenceWeight = weight;
}
public double getWeight()
{
    return m_SentenceWeight;
}
public String getSentenceText()
{
    return m_Sentence;
}
public int getSentenceNo()
{
    return m_sentenceNumber;
}

   
}
