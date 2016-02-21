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
    private static boolean sIsStemingEnabled;
    private static boolean sIsStopWordRemovalEnabled;

	public static boolean isStopWordEnabled()
{
    return sIsStopWordRemovalEnabled;
}

public static boolean isStemingEnabled()
{
    return sIsStemingEnabled;
}

public static void setIsStopWordRemovalEnabled(boolean isStopWordRemovalEnabled)
{
    sIsStopWordRemovalEnabled = isStopWordRemovalEnabled;
}

public static void setIsStemingEnabled(boolean isStemingEnabled)
{
    sIsStemingEnabled = isStemingEnabled;
}
}
