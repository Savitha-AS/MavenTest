package com.mip.framework.utils;

import static com.mip.application.controllers.UserController.props;

import java.util.Properties;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMessage.RecipientType;
import javax.mail.internet.MimeMultipart;

public class EmailUtil {
	
	public void send_mail(String sender_emailid1, String recvr_emailid1,
            String subject1, String body)
            throws MessagingException
    {
        String bodyArray[] = null;
        bodyArray = body.split("©");

        Message message = new MimeMessage(getSession());
        
        message.addRecipient(RecipientType.TO, new InternetAddress(recvr_emailid1));        	
        message.addFrom(new InternetAddress[] { new InternetAddress(sender_emailid1) });

        message.setSubject(subject1);

        String html = "<html><body style=\"font-family: Verdana; font-size: 12px;\">" 
        		+ bodyArray[0] + "<br><br><br>"
                + bodyArray[1] + "<br><br><a href=\'"
        		+ bodyArray[2] + "\'>"
                + bodyArray[2] + "</a><br><br><br>"                
                + bodyArray[3] + "<br><br>" 
                + bodyArray[4] + "<br>"
                + bodyArray[5] + "<br><br>"
                + bodyArray[6] + "<br></body></html>";
        Multipart mp = new MimeMultipart();
        MimeBodyPart htmlPart = new MimeBodyPart();
        htmlPart.setContent(html, "text/html");
        mp.addBodyPart(htmlPart);

        message.setContent(mp);

        Transport.send(message);      
    }

    private Session getSession()
     {
        Authenticator authenticator = new Authenticator();

        Properties properties = new Properties();
        properties.setProperty("mail.smtp.submitter", authenticator.getPasswordAuthentication().getUserName());
        properties.setProperty("mail.smtp.auth", props.getProperty("mail.smtp.auth"));

        properties.setProperty("mail.smtp.host", props.getProperty("mail.smtp.host"));
        properties.setProperty("mail.smtp.port", props.getProperty("mail.smtp.port"));

        return Session.getInstance(properties, authenticator);
    }

    private class Authenticator extends javax.mail.Authenticator
    {
        private PasswordAuthentication authentication;

        public Authenticator()
         {
        	String username = props.getProperty("mail.account.username");
            String password = props.getProperty("mail.account.password");
            authentication = new PasswordAuthentication(username, password);
        }

        protected PasswordAuthentication getPasswordAuthentication()
        {
            return authentication;
        }
    }

}
