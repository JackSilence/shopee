package shopee.task;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.text.StrSubstitutor;
import org.apache.commons.lang3.time.DateUtils;
import org.apache.http.client.fluent.Request;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.google.gson.Gson;

import shopee.service.IMailService;
import shopee.util.Utils;

@Service
public class BuyMiJiaTask implements ITask {
	private static final String TEMPLATE = "/shopee/template/template.html", ITEMS = "/shopee/template/items.html";

	private static final String SEARCH_URL = "https://shopee.tw/api/v2/search_items/?";

	private static final String ITEM_URL = "https://shopee.tw/api/v2/item/get?itemid=%.0f&shopid=%.0f";

	private static final String QUERY = "by=ctime&limit=10&match_id=14358222&newest=0&order=desc&page_type=shop";

	private static final String LINK = "https://shopee.tw/%s-i.%.0f.%.0f";

	@Autowired
	private IMailService mailService;

	@SuppressWarnings( "unchecked" )
	@Scheduled( cron = "0 0 12,19 * * *" )
	public void execute() {
		String items = Utils.getResourceAsString( ITEMS );

		StringBuilder sb = new StringBuilder();

		Request request = Request.Get( SEARCH_URL + QUERY );

		Gson gson = new Gson();

		Date now = new Date();

		( ( List<Map<String, Object>> ) gson.fromJson( Utils.getEntityAsString( request ), Map.class ).get( "items" ) ).forEach( i -> {
			Double shopId = ( Double ) i.get( "shopid" ), itemId = ( Double ) i.get( "itemid" );

			i = ( Map<String, Object> ) gson.fromJson( Utils.getEntityAsString( Request.Get( String.format( ITEM_URL, itemId, shopId ) ) ), Map.class ).get( "item" );

			Double price = ( Double ) i.get( "price" ), ctime = ( Double ) i.get( "ctime" );

			i.put( "price", price == null ? 0 : ( int ) ( price / 100000 ) );

			i.put( "link", String.format( LINK, ( ( String ) i.get( "name" ) ).replaceAll( "\\s", "-" ), shopId, itemId ) );

			i.put( "color", DateUtils.isSameDay( new Date( ( long ) ( ctime * 1000 ) ), now ) ? "#ffeb3b" : "#ffffff" );

			StrSubstitutor substitutor = new StrSubstitutor( i );

			sb.append( substitutor.replace( items ) );
		} );

		String time = new SimpleDateFormat( "yyyy-MM-dd.HH" ).format( now );

		mailService.send( "百米家新商品通知_" + time, String.format( Utils.getResourceAsString( TEMPLATE ), sb.toString() ) );
	}
}