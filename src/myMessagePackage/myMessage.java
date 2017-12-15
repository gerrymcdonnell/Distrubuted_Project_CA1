/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package myMessagePackage;

// Serializing Custom Classes Over Sockets - Custom Object


import java.io.*;
import java.util.*;

public class myMessage implements Serializable {
    static final long serialVersionUID = 5541662647609574496L;

    private String sMessage=" ";
    private String sTo="";
    private String sUserName="";
    private int COMMAND=0;


    /*private final int CMD_MSGTOALL=0;
    private final int CMD_PRIVATE_MSG=1;

    private final int CMD_LOGIN=3;
    private final int CMD_DISCONNECT=4;*/

    public myMessage(String sMessage,String sTo,String sUserName) {
        this.sMessage=sMessage;
        this.sTo=sTo;
        this.sUserName=sUserName;
        this.COMMAND=0;
    }

    public myMessage() {
    }

    public myMessage(String sUserName) {
        this.sUserName=sUserName;
    }

    public String getsMessage() {
        return sMessage;
    }

    public void setsMessage(String sMessage) {
        this.sMessage = sMessage;
    }

    public String getsTo() {
        return sTo;
    }

    public void setsTo(String sTo) {
        this.sTo = sTo;
    }

    public String getsUserName() {
        return sUserName;
    }

    public void setsUserName(String sUserName) {
        this.sUserName = sUserName;
    }

    public int getCOMMAND() {
        return COMMAND;
    }

    public void setCOMMAND(int COMMAND) {
        this.COMMAND = COMMAND;
    }

    @Override
    public String toString() {
        return "myMessage{" + "sMessage=" + sMessage + "sTo=" + sTo + "sUserName=" + sUserName + "COMMAND=" + COMMAND + '}';
    }

    public String displayMessage()
    {
        return "["+ this.sUserName + "] " + this.sMessage;
    }

    public void display()
    {
        System.out.println(toString());
    }










}
