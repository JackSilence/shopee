package shopee;

import java.util.Properties;

import org.apache.tomcat.util.codec.binary.Base64;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class App {
	@Value( "${email.username}" )
	private String username;

	@Value( "${email.password}" )
	private String password;

	public static void main( String[] args ) {
		SpringApplication.run( App.class, args );
	}

	@Bean
	public JavaMailSender sender() {
		JavaMailSenderImpl sender = new JavaMailSenderImpl();

		sender.setHost( "smtp.gmail.com" );
		sender.setPort( 465 );

		sender.setUsername( new String( Base64.decodeBase64( username ) ) );
		sender.setPassword( new String( Base64.decodeBase64( password ) ) );

		Properties props = sender.getJavaMailProperties();

		props.put( "mail.transport.protocol", "smtp" );
		props.put( "mail.smtp.auth", "true" );
		props.put( "mail.smtp.starttls.enable", "true" );
		props.put( "mail.debug", "true" );
		props.put( "mail.smtp.ssl.enable", "true" );

		return sender;
	}
}