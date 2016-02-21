/*********************************************************************************
 * Type Revision History:
 * VERSION       DATE            AUTHOR/MODIFIER     COMMENT
 * -------------------------------------------------------------------------
 * 0.1           May 23, 2009    Jayesh Jethva       Initial Create
 *
 **********************************************************************************/
package edu.research.textsummarization.util;

import java.util.Enumeration;
import java.util.Vector;

/** This class provides the split function for string as String class in j2me does not support this method.
 */
public class StringUtils {

    /** @param vector
     * @param args
     */
    private static String[] vectorToStringArray(Vector vector) {
        Enumeration elements = vector.elements();
        String[] returnStrings = new String[vector.size()];
        for (int i = 0; elements.hasMoreElements(); i++) {
            returnStrings[i] = (String) elements.nextElement();
        }

        return returnStrings;
    }

    /** split string into array based on particular seperator.
     *
     * @param str
     * @param sep
     */
    public static String[] split(String str, char sep) {
        if (str == null || str.length() == 0) {
            return new String[0];
        }
        Vector results = new Vector();
        StringBuffer buf = new StringBuffer();
        for (int i = 0; i < str.length(); i++) {
            char c = str.charAt(i);
            if (c == sep) {
                results.addElement(buf.toString());
                buf.setLength(0);
            } else {
                buf.append(c);
            }

        }
        if (buf.length() > 0) {
            results.addElement(buf.toString());
        }

        return vectorToStringArray(results);
    }
   
}
