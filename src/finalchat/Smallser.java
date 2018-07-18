/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package finalchat;

import static finalchat.server.clients;
import static finalchat.server.soc;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.math.BigInteger;
import java.net.Socket;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Iterator;

/**
 *
 * @author Dasaradhi Jetti
 */
public class Smallser extends Thread{

    private Socket socket = null;
    
    
    String buf;
    String divorce[];
    int i = 0;
    BigInteger a1;
    BigInteger a2;
   
    
    String keyEst = "";
    String gp = "";

    public Smallser(Socket socket) {

        super("SmallSer");
        this.socket = socket;

    }

    @Override
    public void run(){
        
    BigInteger g = BigInteger.probablePrime(56, new SecureRandom());
    BigInteger p = BigInteger.probablePrime(56, new SecureRandom());
        try{
        DataInputStream inp = new DataInputStream(soc.getInputStream());
        DataOutputStream out = new DataOutputStream(soc.getOutputStream());
        clients.add(out);
        
        for(DataOutputStream oss : clients) {
        try{
        gp = "gp-"+g.toString()+"-"+p.toString();
        oss.writeUTF(gp);
        }
        catch(Exception e)
        {
            
        }
        }
        
          while (true) { //reading
           //Iterator<DataOutputStream> it = clients.iterator();
           buf = inp.readUTF();  
            
            for(DataOutputStream oss : clients) {
            try {
                //DataOutputStream oss = it.next();
                oss.writeUTF(buf);//writing
                //oss.flush();
                }
            catch(Exception e){
            
            }
            }
          }
        
        
        
        }
        catch(Exception e){
            
        }
            //implement your methods here
    }
}
    

