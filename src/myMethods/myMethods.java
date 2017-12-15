/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package myMethods;

import javax.swing.JOptionPane;

/**
 *
 * @author gerry
 */
public class myMethods {

    public static void msgBox(String s)
    {
        JOptionPane.showMessageDialog(null,s);
    }

    public static String inputBox(String sMsg)
    {
         //Pop up an input box with text ( What is your name ? )
        return JOptionPane.showInputDialog(null,sMsg);
    }

}
