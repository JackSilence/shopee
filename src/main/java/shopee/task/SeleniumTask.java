package shopee.task;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.IntStream;

import org.apache.commons.lang3.StringUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import io.github.bonigarcia.wdm.WebDriverManager;

@Service
public class SeleniumTask implements ITask {
	private final Logger log = LoggerFactory.getLogger( this.getClass() );

	private static final String URL = "https://shopee.tw/shop/14358222/search?page=0&sortBy=ctime";

	private static final String XPATH = "//*[@id=\"main\"]/div/div[2]/div[2]/div/div[2]/div/div/div[2]/div";

	private static final String HOST = "https://shopee.tw";

	@Value( "${GOOGLE_CHROME_SHIM:}" )
	private String bin;

	@Override
	public void execute() {
		WebDriver driver = init();

		driver.get( URL );

		sleep( 5000 );

		// 蝦皮有些內容滑到下面才會載入
		IntStream.range( 0, 7 ).forEach( i -> {
			( ( JavascriptExecutor ) driver ).executeScript( "window.scrollBy(0, 200)" );

			sleep( 2000 );
		} );

		WebElement element = driver.findElement( By.xpath( XPATH ) );

		Document doc = Jsoup.parse( element.getAttribute( "innerHTML" ) );

		doc.getElementsByClass( "shop-search-result-view__item" ).stream().forEach( i -> {
			Map<String, String> map = new HashMap<>();

			Element link = i.getElementsByTag( "a" ).first(), first = link.child( 0 ), text = first.child( 1 );

			map.put( "link", HOST.concat( link.attr( "href" ) ) );

			String style = first.child( 0 ).child( 0 ).attr( "style" );

			map.put( "image", StringUtils.substringBetween( style, "url(\"", "\");" ) );
			map.put( "name", text.child( 0 ).text() );

			Elements children = text.child( 1 ).children();

			map.put( "price", children.get( children.size() > 1 ? 1 : 0 ).text() );

			log.info( map.toString() );

		} );

		driver.quit();
	}

	private WebDriver init() {
		ChromeOptions options = new ChromeOptions();

		if ( bin.isEmpty() ) {
			WebDriverManager.chromedriver().setup();

		} else {
			System.setProperty( "webdriver.chrome.driver", "/app/.chromedriver/bin/chromedriver" );

			options.setBinary( bin );

		}

		options.addArguments( "--headless", "--disable-gpu", "--window-size=1280,680" );

		return new ChromeDriver( options );
	}

	private void sleep( long millis ) {
		try {
			Thread.sleep( millis );

		} catch ( InterruptedException e ) {
			throw new RuntimeException();

		}
	}
}