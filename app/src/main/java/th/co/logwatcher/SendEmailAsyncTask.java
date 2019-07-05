package th.co.logwatcher;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import javax.activation.DataHandler;
import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.mail.util.ByteArrayDataSource;

public class SendEmailAsyncTask extends AsyncTask<String, Void, Void> {

    private ProgressDialog progressDialog;
    Context context;
    DataHandler dataHandler ;

    SendEmailAsyncTask(Context context) {
        this.context = context;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();

        progressDialog = ProgressDialog.show(context, context.getString(R.string.toast_title_wait), context.getString(R.string.toast_reading_device_log));
        // prepare archive file to send email
        try {
            String zipFile = context.getExternalFilesDir(Constants.filepath) + "/ZIP_" + Constants.timeStamp + ".zip" ;
            InputStream inputStream = new FileInputStream(zipFile);
            dataHandler = new DataHandler( new ByteArrayDataSource( inputStream, "application/octet-stream" ) );

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    protected Void doInBackground(String... urls) {

        sendEmail();
        return null;
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);

        if (progressDialog != null)
            progressDialog.dismiss();
        Log.v(Constants.LOG_TAG , "Sent message successfully....");

    }

    //Method send email
    public void sendEmail(){
        //email test
        final String username = "wd.kku.event@gmail.com";
        final String password = "cy9yMcIL";
        final String sendto = "boonyaporn.eye@gmail.com";

        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.port", "587");

        Session session = Session.getInstance(props,
                new javax.mail.Authenticator() {
                    protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication(username, password);
                    }
                });
        try {
            // Create a default MimeMessage object.
            Message message = new MimeMessage(session);

            // Set From: header field of the header.
            message.setFrom(new InternetAddress(username));

            // Set To: header field of the header.
            message.setRecipients(Message.RecipientType.TO,
                    InternetAddress.parse(sendto));

            // Set Subject: header field
            message.setSubject("ZIP log file per week");

            // This mail has 2 part, the BODY and the embedded zip
            MimeMultipart multipart = new MimeMultipart("related");

            // first part (the html)
            BodyPart messageBodyPart = new MimeBodyPart();
            String htmlText = "<p> Test send log email </p>";

            messageBodyPart.setContent(htmlText,"text/html");
            multipart.addBodyPart(messageBodyPart);

            //Second part (archive file)
            messageBodyPart = new MimeBodyPart();
            messageBodyPart.setDataHandler(dataHandler);
            messageBodyPart.setFileName(Constants.timeStamp + ".zip");
            multipart.addBodyPart(messageBodyPart);

            // put everything together
            message.setContent(multipart);

            // Send message
            Transport.send(message);


        } catch (MessagingException e) {
            throw new RuntimeException(e);
        }
    }
}
