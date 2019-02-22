package shopee.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import shopee.task.ITask;

@RestController
public class ExecuteController {
	private final Logger log = LoggerFactory.getLogger( this.getClass() );

	@Autowired
	private ApplicationContext context;

	@GetMapping( value = "/execute/{name}" )
	public void execute( @PathVariable String name ) {
		Object bean = context.getBean( name );

		Assert.isInstanceOf( ITask.class, bean );

		log.info( "Execute task manually: " + name );

		( ( ITask ) bean ).execute();
	}
}