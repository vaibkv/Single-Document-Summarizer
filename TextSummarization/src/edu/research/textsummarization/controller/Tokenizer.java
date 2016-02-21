/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.research.textsummarization.controller;

import edu.research.textsummarization.util.ConfigUtil;
import java.io.*;
import java.util.*;
import java.util.ArrayList;

/**
 *
 * @author Administrator
 */
public class Tokenizer {

    private static Tokenizer s_SelfRef = null;
    private TokenManager m_TokenManager = null;

    private Tokenizer() {

    }

    private void init() {
        m_TokenManager = TokenManager.getInstance();

    }

    public static Tokenizer getInstance() {

        if (s_SelfRef == null) {
            s_SelfRef = new Tokenizer();
            s_SelfRef.init();
        }
        return s_SelfRef;
    }

    public void Tokenize(String text) {
        boolean isStopWordEnabled = ConfigUtil.isStopWordEnabled();
        boolean isStemingEnabled = ConfigUtil.isStemingEnabled();
        System.out.println("isstopwordenabled=" + isStopWordEnabled);
        System.out.println("isstemingenabled=" + isStemingEnabled);

        StopWordRemoval stopWordRemoval = StopWordRemoval.getReference();
        String[] tokens = null;

            ArrayList<String> tokensList = new ArrayList<String>();
            tokens = text.split("[-–)+><^=_/%$|( '\",*~`&#@;:\n\t\u0964\u0965\u097D.?!]");
            int tokenLength = tokens.length;


            for (int j = 0; j < tokenLength; j++) {

                tokens[j] = tokens[j].trim();
                if (tokens[j].trim().equals("") || tokens[j].length() <= 2 || tokens[j].contains("[") || tokens[j].contains("]") || tokens[j].contains("{") || tokens[j].contains("}") || tokens[j].contains("\\"))
				{
                    continue;
                }
                if (tokens[j].trim().equals("")) {
                    continue;
                }
                if (isStopWordEnabled) {
                    if (!stopWordRemoval.isStopWord(tokens[j])) {
                        tokensList.add(tokens[j]);

                    }
                }

            }
            if (isStopWordEnabled) {

                tokens = tokensList.toArray(new String[tokensList.size()]);
            }
            if (isStemingEnabled) {
                for (int k = 0; k < tokens.length; k++) {
                    //tokens[k] = HindiStemmer.getReference().getHindiStemedWord(tokens[k]);
                }
            }
            m_TokenManager.StoreTokens(tokens);
        

    }

  
}
