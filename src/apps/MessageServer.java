package Apps;

//MultiEchoServer.java
// Multithreaded TCP Server - Echo service

import java.io.*;
import java.net.*;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.logging.Level;
import java.util.logging.Logger;
import myMessagePackage.myMessage;
import serverCommands.ServerCommands;

public class MessageServer {

    private static ServerSocket servSocket;
    private static final int PORT = 1234;
    private static int numClients;
    Hashtable outputStreams = new Hashtable();

    public MessageServer() throws IOException
    {
        System.out.println("Creating Server: ");
        Listen();
        System.out.println("Server Is Listening: ");
    }

    public static void main(String args[]) throws IOException {        
        // Create a Server object, which will automatically begin
        // accepting connections.
        new MessageServer();
    }

    private void Listen() throws IOException
    {
        //1 create server socket, start server
        try {
            servSocket = new ServerSocket(PORT);
            System.out.println("Server started on port : [" + PORT + "]");
        } catch (IOException e) {
            System.out.println("\nUnable to set up port!");
            System.exit(1);
        }

        //2) Listen for a connection from Client and create socket
        do {
            //client has connected,accept connection
            Socket client = servSocket.accept();
            numClients++;
            System.out.println("Client # " + numClients);
            System.out.println("Connection From: " + client.getInetAddress());

            // Create a DataOutputStream for writing data to the other side
            ObjectOutputStream oos = new ObjectOutputStream(client.getOutputStream());

            // Save this stream so we don't need to make it again
            outputStreams.put(client, oos);

            //Create a thread to handle communication with
            //this client and pass the constructor for this
            //thread a reference to the relevant socket...
            ClientHandler handler = new ClientHandler(this,client);
            handler.start();
        } while (true);        
    }

    // Get an enumeration of all the OutputStreams, one for each client
    // connected to us
    Enumeration getOutputStreams() {
        return outputStreams.elements();
    }


    // Send a message to all clients (utility routine)
    void sendToAll(myMessage msgObject) {
        // We synchronize on this because another thread might be
        // calling removeConnection() and this would screw us up
        // as we tried to walk through the list
        synchronized (outputStreams) {
            //iterate throught onlineClients
            // For each client ...
            for (Enumeration e = getOutputStreams(); e.hasMoreElements(); )
            {
                ObjectOutputStream oos = (ObjectOutputStream)e.nextElement();
                try {
                    oos.writeObject(msgObject);
                } catch (IOException ex) {
                    Logger.getLogger(MessageServer.class.getName()).log(Level.SEVERE, null, ex);
                }
            }

            }
    }



    // Remove a socket, and it's corresponding output stream, from our
    // list. This is usually called by a connection thread that has
    // discovered that the connectin to the client is dead.
    void removeConnection(Socket s) {
    // Synchronize so we don't mess up sendToAll() while it walks
    // down the list of all output streamsa
        synchronized (outputStreams) {
            // Tell the world
            System.out.println("Removing connection to " + s);
            // Remove it from our hashtable/list
            outputStreams.remove(s);
            // Make sure it's closed
            try {
                s.close();
            } catch (IOException ie) {
                System.out.println("Error closing " + s);
                ie.printStackTrace();
            }
        }

}
// Client thread class - 1 thread for each client connection
class ClientHandler extends Thread {

    private Socket client;
    // The Server that spawned us
    private MessageServer server;

    private ObjectInputStream ois = null;
    private ObjectOutputStream oos = null;

    private ObjectOutputStream msgToAllClientsOOS;
    private ObjectOutputStream msgToPrivateClientsOOS;

    //constructor
    public ClientHandler(MessageServer myserver,Socket socket) {
    //Set up reference to associated socket...
        client = socket;
        server=myserver;

        try {           
            //serializeation
            ois = new ObjectInputStream(client.getInputStream());
            oos = new ObjectOutputStream(client.getOutputStream());

        } catch (IOException e) {
            e.printStackTrace();
        }

    }


    public void run() {
        myMessage inMessage = null;        
        try {
            // exchange of messages between client and server
            do {
                //Accept message from client on the socket's input
                //Repeat above until 'QUIT' sent by client...

                //read in the object send by client
                //************************************************************************************
                inMessage = (myMessage) ois.readObject();

                System.out.println(">SERVER Recieved Message");


                //************************************************************************************
                //send to all clients
                //************************************************************************************
                if(inMessage.getCOMMAND()==ServerCommands.CMD_MSGTOALL)
                {
                    //send message
                    System.out.println(inMessage.displayMessage());
                    //send it back out echo it
                    System.out.println(">SERVER: Sending Object back: " + inMessage.getsMessage());

                    server.sendToAll(inMessage);

                }
                //************************************************************************************
                //************************************************************************************
                else if(inMessage.getCOMMAND() == ServerCommands.CMD_PRIVATE_MSG)
                {
                    /*String sSendMsgTo=inMessage.getsTo();
                    System.out.println("SERVER:> SEND PRIV MSG TO " + sSendMsgTo);
                    msgToPrivateClientsOOS=currentClients.getUserOOS(sSendMsgTo);
                    
                    if(msgToPrivateClientsOOS!=null)
                    {
                        msgToPrivateClientsOOS.writeObject(inMessage);
                        msgToPrivateClientsOOS.flush();
                        msgToPrivateClientsOOS=null;
                    }*/

                }
                //************************************************************************************
                else if(inMessage.getCOMMAND()==ServerCommands.CMD_LOGIN)
                {

                    System.out.println("SERVER:> Do Login:");
                    System.out.println("SERVER:> Logging in User: " + inMessage.getsUserName());

                    //public static Object sharedLock = new Object();
                    //synchronised (sharedLock)
                    //{
                     //add to hastable
                    //****************************************************
                     //currentClients.addUser(inMessage.getsUserName(), oos);
                    //****************************************************
                    //}
                    
                     //****************************************************
                     //send msg to client to update the list of online users

                     //****************************************************

                    
                }
                //************************************************************************************
                //************************************************************************************
                else if(inMessage.getCOMMAND()==ServerCommands.CMD_PRINTALLCLIENTS)
                {
                    System.out.println("SERVER:> PRINT ALL CLIENTS");
                    //System.out.println(currentClients.getOnlineUserNameList());
                    //currentClients.printList();
                }

                
                //disconnect client
                /*else if(inMessage.getCOMMAND()==ServerCommands.CMD_DISCONNECT)
                {
                    System.out.println("SERVER:> Disconnect");
                }*/



                //************************************************************************************
            } 
            while (inMessage.getCOMMAND()!=ServerCommands.CMD_DISCONNECT);

        } catch (IOException e) {
            e.printStackTrace();
        } catch(ClassNotFoundException e){
            e.printStackTrace();
        }
          finally {
            try {
                if (client != null) {
                    System.out.println("SERVER:> Closing down connection...User=" + inMessage.getsUserName());
                    oos.close();
                    ois.close();
                    client.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


    }
}
