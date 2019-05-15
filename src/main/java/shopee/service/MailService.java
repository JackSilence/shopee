package shopee.service;

import java.io.IOException;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import org.apache.commons.lang3.CharEncoding;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import com.sendgrid.Content;
import com.sendgrid.Email;
import com.sendgrid.Mail;
import com.sendgrid.Method;
import com.sendgrid.Request;
import com.sendgrid.Response;
import com.sendgrid.SendGrid;

@Service
public class MailService implements IMailService {
	private final Logger log = LoggerFactory.getLogger( this.getClass() );

	@Autowired
	private JavaMailSender sender;

	@Value( "${sendgrid.api.key:}" )
	private String key;

	@Value( "${gmail.mail.to:}" )
	private String mail1;

	@Value( "${sendgrid.mail.to:}" )
	private String mail2;

	@Override
	public void send( String subject, String content ) {
		try {
			MimeMessage message = sender.createMimeMessage();

			MimeMessageHelper helper = new MimeMessageHelper( message, true, CharEncoding.UTF_8 );

			helper.setSubject( subject );
			helper.setText( content, true );
			helper.setTo( mail1 );

			sender.send( message );

		} catch ( MessagingException e ) {
			throw new RuntimeException( "Failed to send: " + subject, e );

		}

		try {
			Email from = new Email( "shopee@heroku.com" ), to = new Email( mail2 );

			Request request = new Request();

			request.setMethod( Method.POST );
			request.setEndpoint( "mail/send" );
			request.setBody( new Mail( from, subject, to, new Content( "text/html", content ) ).build() );

			Response response = new SendGrid( key ).api( request );

			log.info( "Status: {}", response.getStatusCode() );

		} catch ( IOException e ) {
			throw new RuntimeException( "Failed to send (SendGrid): " + subject, e );

		}
	}
}