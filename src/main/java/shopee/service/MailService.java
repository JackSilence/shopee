package shopee.service;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import org.apache.commons.lang3.CharEncoding;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import magic.service.SendGrid;

@Service
public class MailService extends SendGrid {
	@Autowired
	private JavaMailSender sender;

	@Value( "${gmail.mail.to:}" )
	private String mail;

	@Override
	public void send( String subject, String content ) {
		super.send( subject, content );

		try {
			MimeMessage message = sender.createMimeMessage();

			MimeMessageHelper helper = new MimeMessageHelper( message, true, CharEncoding.UTF_8 );

			helper.setSubject( subject );
			helper.setText( content, true );
			helper.setTo( mail );

			sender.send( message );

		} catch ( MessagingException e ) {
			throw new RuntimeException( "Failed to send: " + subject, e );

		}
	}
}