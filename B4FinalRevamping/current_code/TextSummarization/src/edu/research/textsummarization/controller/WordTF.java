/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package edu.research.textsummarization.controller;

/**
 *
 * @author Administrator
 */
public class WordTF 
{
    private int sentence_Number;
    private String word_string;
    private double norm_tf_score;
    
    public void setSentNum(int num)
    {
        sentence_Number = num;
    }
    public int getSentNum()
    {
        return sentence_Number;
    }
    public void setWordString(String word)
    {
        word_string = word;
    }
    public String getWordString()
    {
        return word_string;
    }
    public void setTFScore(double score)
    {
        norm_tf_score = score;
    }
    public double getTFScore()
    {
        return norm_tf_score;
    }
}
