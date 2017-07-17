package de.noobits.secretsantamanager;


import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Properties;

/**
 * Created by Thomas on 16.07.2017.
 */

public class EmailController {
    private Socket mailSocket;
    private PrintWriter out;
    private BufferedReader in;
    private String sRt, smtpServer, fromAdress, toAdress;
    private String user, password;
    private Properties props;

    EmailController(String smtpServer, int smtpPort, String fromAdress, String toAdress, String password)throws Exception{
        sRt = "";
        out = null;
        in = null;
        props = new Properties();
        props.put("mail.transport.protocol", "smtp");
        props.put("mail.smtp.host", smtpServer);
        props.put("mail.smtp.auth", true);
        props.put("mail.smtp.port", smtpPort);



        this.smtpServer = smtpServer;
        this.fromAdress = fromAdress;
        this.toAdress = toAdress;
        this.user = fromAdress;
        this.password = password;

        try {
            mailSocket = new Socket(smtpServer, smtpPort);
            out = new PrintWriter(mailSocket.getOutputStream());
            in = new BufferedReader(new InputStreamReader(mailSocket.getInputStream()));
            mailSocket.setSoTimeout(10000);
        }catch(Exception e){
            System.out.println("Error: cant connect to server: " + e.toString());
        }
    }

    /**
     * Sends an email via smtp protocol to given address.
     * @param subject
     * @param mailText
     * @return
     */
    public synchronized boolean sendEmail( String subject, String mailText) throws Exception{
        //TODO: connect to noreply@noobits.de and send the notification message to a santa
        Boolean paramOk = checkParameters(smtpServer, fromAdress, toAdress, subject, mailText);
        System.out.println("parameter are ok: " + paramOk);

        try {
            in.readLine();
            out.println("HELO user");
            out.println("MAIL FROM: <" + fromAdress + ">");
            out.println("RCPT TO: <" + toAdress + ">");
            out.println("DATA");
            out.println("Subject: " + subject);
            out.println(mailText);
            out.println(".");


            //close all
            if (in != null) try {
                in.close();
            } catch (Exception ex) {
            }
            if (out != null) try {
                out.close();
            } catch (Exception ex) {
            }
            if (mailSocket != null) try {
                mailSocket.close();
            } catch (Exception ex) {
            }
        }catch(Exception e){

        }
        return true;
    }

    private boolean checkParameters(String smtpServer, String fromAdress, String toAdress,
                                           String subject, String mailText) throws Exception {
        if( null == smtpServer  || 0 >= smtpServer.length() ||
                null == fromAdress     || 0 >= fromAdress.length()    ||
                null == toAdress       || 0 >= toAdress.length()      ||
                (  (null == subject || 0 >= subject.length())
                        && (null == mailText    || 0 >= mailText.length())  )   ) {
            throw new Exception("Invalid Parameters for EmailController.sendEmail().");
        }
        return true;
    }


}
