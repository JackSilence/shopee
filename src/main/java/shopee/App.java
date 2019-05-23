package shopee;

import java.util.Properties;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.scheduling.annotation.EnableScheduling;

import magic.util.Utils;

@SpringBootApplication
@EnableScheduling
public class App {
	@Value( "${email.username:}" )
	private String username;

	@Value( "${email.password:}" )
	private String password;

	public static void main( String[] args ) {
		SpringApplication.run( App.class, args );
	}

	@Bean
	public JavaMailSender sender() {
		JavaMailSenderImpl sender = new JavaMailSenderImpl();

		sender.setHost( "smtp.gmail.com" );
		sender.setPort( 465 );

		sender.setUsername( Utils.decode( username ) );
		sender.setPassword( Utils.decode( password ) );

		Properties props = sender.getJavaMailProperties();

		props.put( "mail.transport.protocol", "smtp" );
		props.put( "mail.smtp.auth", "true" );
		props.put( "mail.smtp.starttls.enable", "true" );
		props.put( "mail.debug", "false" );
		props.put( "mail.smtp.ssl.enable", "true" );

		return sender;
	}
}