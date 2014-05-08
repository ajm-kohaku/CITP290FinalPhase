/*
* Programmer:   Amber Murphy
* TUID:         MURPH32
* Course:       CITP 290: Spring 2014
* Main Program: PopApp.java
* Assignment:   Phase 3
* Summary:      Create GUI for phase 2 content.
*
*/
package murph32.core;

/**
 * Created by Dukat on 4/8/2014.
 */
public class StringUtils {
    public static String padWithSpaces(String s, int length) {
        if (s.length() < length) {
            StringBuilder sb = new StringBuilder(s);
            while (sb.length() < length) {
                sb.append(" ");
            }
            return sb.toString();
        } else {
            return s.substring(0, length);
        }
    }
}
