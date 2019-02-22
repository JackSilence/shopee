package shopee.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class MailService implements IMailService {
	@Autowired
	private JavaMailSender sender;

	@Override
	public void send( String subject, String content ) {
		SimpleMailMessage message = new SimpleMailMessage();

		message.setTo( "lethington@gmail.com" );
		message.setSubject( subject );
		message.setText( content );

		sender.send( message );
	}
}