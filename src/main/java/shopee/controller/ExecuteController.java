package shopee.controller;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

@RestController
public class ExecuteController extends magic.controller.ExecuteController {
	@ApiOperation( value = "手動執行排程任務", notes = "請輸入實作IService的任務Bean名稱" )
	// @ApiImplicitParam( name = "name", value = "Bean名稱", allowableValues = "buyMiJiaTask", required = true, dataType = "String" )
	@Override
	public String execute( @ApiParam( value = "Bean名稱", allowableValues = "buyMiJiaTask,seleniumTask", required = true ) @PathVariable String name, @ApiParam( hidden = true ) String command, @ApiParam( hidden = true ) String text ) {
		return super.execute( name, command, text );
	}
}