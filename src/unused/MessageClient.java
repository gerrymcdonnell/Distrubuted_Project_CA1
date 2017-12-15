package unused;

//MultiEchoClient.java
// TCP Client - Echo service
import java.io.*;
import java.net.*;
import myMessagePackage.myMessage;

public class MessageClient {

    private static InetAddress host;
    private static final int PORT = 1234;
    private static Socket link;
    private static BufferedReader keyboard;

    private static ObjectOutputStream oos = null;
    private static ObjectInputStream ois = null;

    public static void main(String args[]) throws IOException {
        try {
            host = InetAddress.getByName("localhost");//InetAddress.getLocalHost();
            link = new Socket(host, PORT);
            keyboard = new BufferedReader(new InputStreamReader(System.in));

            //serial
            //*******************************************************************************
            // open I/O streams for objects - serialization streams
            oos = new ObjectOutputStream(link.getOutputStream());
            ois = new ObjectInputStream(link.getInputStream());         
            
            // create a custom serialized objects parans: String sMessage,String sTo,String sUserName)
            myMessage myOutMessage = new myMessage();
            myMessage myInMessage = new myMessage();
            //*******************************************************************************


            String sMessage, response,sUserName;

            System.out.print("Enter UserName: ");
            sUserName=keyboard.readLine();

            do {
                System.out.print("Enter message ('QUIT' to exit): ");
                sMessage = keyboard.readLine();



                //Send message to server on
                //the socket's output stream...
                //out.println(message);

                //send it the message object serialiazation
                //******************************************************
                myOutMessage.setsMessage(sMessage);
                //set username
                myOutMessage.setsUserName(sUserName);

                // write the objects to the server
                System.out.println("CLIENT> Sending Message Object.");
                oos.writeObject(myOutMessage);
                oos.flush();
                //******************************************************

                //Accept response from server on
                //the socket's input stream...
                //response = in.readLine();
                //Display server's response to user...
                //System.out.println(response);


                //******************************************************
                //recieve message from server
                myInMessage = (myMessage) ois.readObject();  //this line needs to have a catch for classnotfoundexc
                System.out.println("CLIENT:> Recieved Message " + myInMessage.displayMessage());
                //******************************************************


            } while (!sMessage.equals("QUIT"));
        } catch (UnknownHostException uhEx) {
            System.out.println("\nHost ID not found!\n");
        } catch (IOException ioEx) {
            ioEx.printStackTrace();
        } catch (ClassNotFoundException e){
            e.printStackTrace();
        }
          finally {
            try {
                if (link != null) {
                    System.out.println("Closing down connection...");
                    link.close();
                }
            } catch (IOException ioEx) {
                System.out.println("Error IO Exception!");
                ioEx.printStackTrace();
            }
        }
    }
}
