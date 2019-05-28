package shopee.task;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.text.StrSubstitutor;
import org.apache.commons.lang3.time.DateUtils;
import org.apache.http.client.fluent.Request;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.google.gson.Gson;

import magic.service.IMailService;
import magic.service.IService;
import magic.service.Slack;
import magic.util.Utils;

@Service
public class BuyMiJiaTask implements IService {
	private static final String TEMPLATE = "/shopee/template/template.html", ITEMS = "/shopee/template/items.html";

	private static final String SEARCH_URL = "https://shopee.tw/api/v2/search_items/?";

	private static final String ITEM_URL = "https://shopee.tw/api/v2/item/get?itemid=%.0f&shopid=%.0f";

	private static final String QUERY = "by=ctime&limit=20&match_id=14358222&newest=0&order=desc&page_type=shop";

	private static final String LINK = "https://shopee.tw/%s-i.%.0f.%.0f", IMAGE = "https://cf.shopee.tw/file/%s_tn";

	@Autowired
	private IMailService mailService;

	@Autowired
	private Slack slack;

	@SuppressWarnings( "unchecked" )
	@Scheduled( cron = "0 0 12,19 * * *" )
	public void exec() {
		String items = Utils.getResourceAsString( ITEMS );

		StringBuilder sb = new StringBuilder();

		List<Map<String, Object>> attachments = new ArrayList<>();

		Request request = Request.Get( SEARCH_URL + QUERY );

		Gson gson = new Gson();

		Date now = new Date();

		( ( List<Map<String, Object>> ) gson.fromJson( Utils.getEntityAsString( request ), Map.class ).get( "items" ) ).forEach( i -> {
			Double shopId = ( Double ) i.get( "shopid" ), itemId = ( Double ) i.get( "itemid" );

			i = ( Map<String, Object> ) gson.fromJson( Utils.getEntityAsString( Request.Get( String.format( ITEM_URL, itemId, shopId ) ) ), Map.class ).get( "item" );

			int min = price( i.get( "price_min" ) ), max = price( i.get( "price_max" ) );

			String name = ( String ) i.get( "name" ), price, link, color, title;

			i.put( "price", price = min == max ? String.valueOf( min ) : min + "<br>" + max );

			i.put( "link", link = String.format( LINK, name.replaceAll( "\\s", "-" ), shopId, itemId ) );

			Date c = new Date( ( long ) ( ( Double ) i.get( "ctime" ) * 1000 ) ), ytd = DateUtils.addDays( now, -1 );

			boolean isNow = DateUtils.isSameDay( c, now ), isYtd = DateUtils.isSameDay( c, ytd );

			i.put( "color", color = isNow ? "#ffeb3b" : isYtd ? "#EEEEE0" : "#ffffff" );

			sb.append( new StrSubstitutor( i ).replace( items ) );

			if ( isNow || isYtd || attachments.isEmpty() ) {
				Map<String, Object> attachment = new HashMap<>();

				attachment.put( "fallback", title = String.format( "%s $%s", name, price.replace( "<br>", " - $" ) ) );
				attachment.put( "title", title );
				attachment.put( "title_link", link );
				attachment.put( "color", color );
				attachment.put( "image_url", String.format( IMAGE, i.get( "image" ) ) );

				attachments.add( attachment );
			}
		} );

		slack.post( gson.toJson( Collections.singletonMap( "attachments", attachments ) ) );

		String time = new SimpleDateFormat( "yyyy-MM-dd.HH" ).format( now );

		mailService.send( "百米家新商品通知_" + time, String.format( Utils.getResourceAsString( TEMPLATE ), sb.toString() ) );
	}

	private int price( Object price ) {
		return price == null ? 0 : ( int ) ( ( Double ) price / 100000 );
	}
}