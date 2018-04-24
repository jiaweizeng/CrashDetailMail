package cn.hzjk.crashdetailmail;

import java.util.Properties;

import javax.activation.DataHandler;
import javax.activation.FileDataSource;
import javax.mail.Message;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.mail.internet.MimeUtility;

/**
 * Created by zjw on 2018/4/20.
 */

public class EmailUtil {

    class MyAuthenticator extends javax.mail.Authenticator {
        private String strUser;
        private String strPwd;

        public MyAuthenticator(String user, String password) {
            this.strUser = user;
            this.strPwd = password;
        }

        protected PasswordAuthentication getPasswordAuthentication() {
            return new PasswordAuthentication(strUser, strPwd);
        }
    }

    public void sendMail(String toMail, String fromMail, String server,
                         String username, String password, String title, String body,
                         String attachment) throws Exception {

        Properties props = System.getProperties();// Get system properties
        props.setProperty("mail.transport.protocol", "smtp");   // 使用的协议（JavaMail规范要求）
        props.put("mail.smtp.host", server);// Setup mail server

        props.put("mail.smtp.auth", "true");

        MyAuthenticator myauth = new MyAuthenticator(username, password);// Get
        // 发送邮件，和密码

        Session session = Session.getDefaultInstance(props, myauth);
        session.setDebug(true);                                 // 设置为debug模式, 可以查看详细的发送 log
        MimeMessage message = new MimeMessage(session); // Define message

        message.setFrom(new InternetAddress(fromMail)); // Set the from address
        // 接收邮箱

        message.addRecipient(Message.RecipientType.TO, new InternetAddress(
                toMail));// 目标邮箱
        // address
        message.setSubject(title);// Set the subject 标题

        // message.setText(MimeUtility.encodeWord(body));// Set the content

        MimeMultipart allMultipart = new MimeMultipart("mixed");

        MimeBodyPart attachPart = new MimeBodyPart();
        FileDataSource fds = new FileDataSource(attachment);
        attachPart.setDataHandler(new DataHandler(fds));
        attachPart.setFileName(MimeUtility.encodeWord(fds.getName()));

        MimeBodyPart textBodyPart = new MimeBodyPart();
        textBodyPart.setText(body);// 邮件内容

        allMultipart.addBodyPart(attachPart);
        allMultipart.addBodyPart(textBodyPart);
        message.setContent(allMultipart);
        message.saveChanges();
        Transport.send(message);
    }

    /**
     * 设置邮件信息
     *  toMail
     *            接收邮箱
     *  account
     *            发送者邮箱
     *  password
     *            发送者邮箱密码
     *  server
     *            发送者邮箱服务器
     * @param title
     *            邮件标题
     * @param body
     *            邮件内容
     * @param path
     *            附件路径
     */
    public void sendMailS(final String body, final String path,final String title) {
        new Thread(new Runnable() {
            public void run() {
                // EmailUtil emailUtil = new EmailUtil();
                try {
                    String toMail = "@qq.com";
                    String account = "@163.com";
                    String password = "";
                    String server = "smtp.163.com";
                    // emailUtil.
                    sendMail(toMail, account, server, account, password, title,
                            body, path);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

}
