package shopee.service;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import org.apache.commons.lang3.CharEncoding;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Service
public class MailService implements IMailService {
	private final Logger log = LoggerFactory.getLogger( this.getClass() );

	@Autowired
	private JavaMailSender sender;

	@Override
	public void send( String subject, String content ) {
		try {
			log.info( subject + "發送開始!" );

			MimeMessage message = sender.createMimeMessage();

			MimeMessageHelper helper = new MimeMessageHelper( message, true, CharEncoding.UTF_8 );

			helper.setSubject( subject );
			helper.setText( content, true );
			helper.setTo( "lethington@gmail.com" );

			sender.send( message );

			log.info( subject + "發送結束!" );

		} catch ( MessagingException e ) {
			throw new RuntimeException( "Failed to send: " + subject, e );

		}
	}
}