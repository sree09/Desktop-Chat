/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package finalchat;

import static finalchat.client.chatarea;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.Socket;
import static finalchat.client.user;
import java.math.BigInteger;
import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;
/**
 *
 * @author saisa
 */
public class clientThread extends Thread{
    private Socket socket = null;
    int i = 0;
    String buf = "";
    String result;
    String original;
    byte[] textDecrypted;
    

    public clientThread(Socket socket) {

        super("clientThread");
        this.socket = socket;

    }
    
    
    public String decrypt(String response)
    {   
        if(response == "" || response == null)
            return response;

        try {
            String Key2 = client.twinsShare.modPow(client.a,client.p).toString();
            byte[] desKeyData2 = Key2.getBytes("ISO-8859-1");
            DESKeySpec desKeySpec2 = new DESKeySpec(desKeyData2);
            SecretKeyFactory keyFactory2 = SecretKeyFactory.getInstance("DES");
            SecretKey myDesKey2 = keyFactory2.generateSecret(desKeySpec2);
            Cipher desCipher1;
            desCipher1 = Cipher.getInstance("DES/ECB/PKCS5Padding");          
            desCipher1.init(Cipher.DECRYPT_MODE, myDesKey2);
            byte[] text2 = response.getBytes("ISO-8859-1");
            textDecrypted = desCipher1.doFinal(text2);
            original = new String(textDecrypted, "ISO-8859-1");         
        }

        catch(Exception e) {
            System.out.println(e);
        }
        return original;
    }
    
    @Override
    public void run(){
        
        try{
        DataInputStream inp = new DataInputStream(this.socket.getInputStream());
        DataOutputStream out = new DataOutputStream(this.socket.getOutputStream());
        
        String[] divorce = {};
        String buf1 = "";
            while(! buf.equals("Bye")){
                buf = inp.readUTF();
                divorce = buf.split("-");
                if((!divorce[0].equals("Key"))&&(!divorce[0].equals("gp")))
                {              
                    if(divorce[0].equals(user.getText()))
                    {
                        buf1 = "Me:\t"+divorce[1];
                    }
                    else
                    {
                        buf1 = divorce[0]+":\t"+divorce[1];
                        result = decrypt(divorce[1]);
                        chatarea.setText(chatarea.getText().trim()+"\n"+divorce[0]+":\t"+ result);
                    }                    
                    chatarea.setText(chatarea.getText().trim()+"\n"+ buf1);    
                }
                else if(divorce[0].equals("gp"))
                {
                    client.g = new BigInteger(divorce[1]);
                    client.p = new BigInteger(divorce[2]);
                }
                else if(divorce[0].equals("Key"))
                {
                    
                    if(divorce[1].equals(user.getText()))
                    {
                        buf1 = "Me:\tI sent my share..!!";
                    }
                    else
                    {
                        buf1 = divorce[1]+":\tI recieved your share..!!";
                        client.twinsShare = new BigInteger(divorce[3]);
                       //client.KEY = client.twinsShare.modPow(client.a,client.p).toString();
                    }                    
                    chatarea.setText(chatarea.getText().trim()+"\n"+buf1);
                }    
            }
        }
        catch(Exception e){
            
        }
    }

    
}
