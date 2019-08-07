package shopee.test;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import io.github.bonigarcia.wdm.WebDriverManager;
import magic.util.Utils;
import shopee.test.model.Search;

@RunWith( SpringJUnit4ClassRunner.class )
@SpringBootTest( classes = SeleniumTest.class )
public class SeleniumTest {
	private final Logger log = LoggerFactory.getLogger( this.getClass() );

	private static final WebDriver driver;

	static {
		// System.setProperty( "webdriver.chrome.driver", "C:\\chromedriver_win32\\chromedriver.exe" );
		WebDriverManager.chromedriver().setup();

		ChromeOptions options = new ChromeOptions();

		options.addArguments( "--headless", "--disable-gpu" ); // --disable-gpu應該是非必要設定

		driver = new ChromeDriver( options );
	}

	@Test
	public void search() throws UnsupportedEncodingException {
		driver.get( "https://shopee.tw/" );

		Search search = PageFactory.initElements( driver, Search.class );

		wait( Search.XPATH_CLOSE );

		search.getClose().click();

		log.info( "URL before search: " + url() );
		log.info( "Title before search: " + driver.getTitle() );

		search.getInput().sendKeys( "夜燈" );

		search.getButton().click();

		wait( Search.XPATH_FIRST );

		log.info( "URL after search: " + url() );
		log.info( "Title after search: " + driver.getTitle() );

		search.getFirst().click();

		Utils.sleep( 5000 );

		log.info( "URL after click first: " + url() );
		log.info( "Title after click first: " + driver.getTitle() ); // 可能會因為頁面還沒完全載入拿到預設的, 所以放在後面

		log.info( "Quit: " + driver.getClass().getSimpleName() );

		driver.quit();
	}

	private String url() throws UnsupportedEncodingException {
		return URLDecoder.decode( driver.getCurrentUrl(), StandardCharsets.UTF_8.name() );
	}

	private void wait( String xpath ) {
		// 也可以用css selector
		new WebDriverWait( driver, 20 ).until( ExpectedConditions.presenceOfElementLocated( By.xpath( xpath ) ) );
	}
}