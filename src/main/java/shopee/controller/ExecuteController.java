package shopee.controller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import magic.service.AsyncExecutor;
import magic.service.Slack;

@RestController
public class ExecuteController {
	@Autowired
	private AsyncExecutor executor;

	@Autowired
	private Slack slack;

	@PostMapping( value = "/execute/{name}" )
	@ApiOperation( value = "手動執行排程任務", notes = "請輸入實作IService的任務Bean名稱" )
	// @ApiImplicitParam( name = "name", value = "Bean名稱", allowableValues = "buyMiJiaTask", required = true, dataType = "String" )
	public Map<String, String> execute( @ApiParam( value = "Bean名稱", allowableValues = "buyMiJiaTask,seleniumTask", required = true ) @PathVariable String name ) {
		executor.exec( name );

		return slack.text( "OK!" );
	}
}