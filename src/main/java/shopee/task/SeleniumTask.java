package shopee.task;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.IntStream;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.springframework.stereotype.Service;

import magic.service.Selenium;

@Service
public class SeleniumTask extends Selenium {
	private static final String URL = "https://shopee.tw/shop/14358222/search?page=0&sortBy=ctime";

	private static final String XPATH = "//*[@id=\"main\"]/div/div[2]/div[2]/div/div[2]/div/div[2]/div";

	private static final String HOST = "https://shopee.tw";

	@Override
	public void exec() {
		run( "--window-size=1280,680" );
	}

	@Override
	protected void run( WebDriver driver ) {
		driver.get( URL );

		sleep();

		// 蝦皮有些內容滑到下面才會載入
		IntStream.range( 0, 7 ).forEach( i -> {
			script( driver, "window.scrollBy(0, 200)" );

			sleep( 2000 );
		} );

		Document doc = Jsoup.parse( driver.findElement( By.xpath( XPATH ) ).getAttribute( "innerHTML" ) );

		doc.getElementsByClass( "shop-search-result-view__item" ).stream().forEach( i -> {
			Map<String, String> map = new HashMap<>();

			Element link = i.getElementsByTag( "a" ).first(), first = link.child( 0 ), text = first.child( 1 );

			map.put( "link", HOST.concat( link.attr( "href" ) ) );
			map.put( "image", first.child( 0 ).child( 0 ).attr( "src" ) );
			map.put( "name", text.child( 0 ).text() );

			Elements children = text.child( 1 ).children();

			map.put( "price", children.get( children.size() > 1 ? 1 : 0 ).text() );

			log.info( map.toString() );
		} );
	}
}