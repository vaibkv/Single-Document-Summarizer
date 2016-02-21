/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package edu.research.textsummarization.controller;

/**
 *
 * @author Administrator
 */
public class WordScore 
{
    private String word;
    private double tf_isf_dens_score;
    private int sent_num;
    
    public void setSentNum(int num)
    {
        sent_num = num;
    }
    
    public int getSentNum()
    {
        return sent_num;
    }
    
    public void setWordText(String wor)
    {
        word = wor;
    }
    public void setScore(double score)
    {
        tf_isf_dens_score = score;
    }
    public String getWordText()
    {
        return word;
    }
    public double getScore()
    {
        return tf_isf_dens_score;
    }
}
