/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package edu.research.textsummarization.util;

/**
 *
 * @author Administrator
 */
public class ConfigUtil 
{
    private static boolean sIsStemmingEnabled;
    private static boolean sIsStopWordRemovalEnabled ;

public static boolean isStopWordEnabled()
{
    return sIsStopWordRemovalEnabled;
}

public static boolean isStemmingEnabled()
{
    return sIsStemmingEnabled;
}

public static void setIsStopWordRemovalEnabled(boolean isStopWordRemovalEnabled)
{
    sIsStopWordRemovalEnabled = isStopWordRemovalEnabled;
}

public static void setIsStemmingEnabled(boolean isStemmingEnabled)
{
    sIsStemmingEnabled = isStemmingEnabled;
}
}
