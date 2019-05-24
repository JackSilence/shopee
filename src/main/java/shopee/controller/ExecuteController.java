package shopee.controller;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.scheduling.annotation.Async;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import magic.service.IService;
import magic.service.Slack;

@RestController
public class ExecuteController {
	private final Logger log = LoggerFactory.getLogger( this.getClass() );

	@Autowired
	private ApplicationContext context;

	@Autowired
	private Slack slack;

	@PostMapping( value = "/execute/{name}" )
	@ApiOperation( value = "手動執行排程任務", notes = "請輸入實作IService的任務Bean名稱" )
	@Async
	// @ApiImplicitParam( name = "name", value = "Bean名稱", allowableValues = "buyMiJiaTask", required = true, dataType = "String" )
	public Map<String, String> execute( @ApiParam( value = "Bean名稱", allowableValues = "buyMiJiaTask,seleniumTask", required = true ) @PathVariable String name ) {
		try {
			Object bean = context.getBean( name );

			Assert.isInstanceOf( IService.class, bean );

			log.error( "Execute task manually: " + name );

			( ( IService ) bean ).exec();

			return slack.text( "OK!" );

		} catch ( Exception e ) {
			log.error( "", e );

			return slack.text( "Oops!" );
		}
	}
}