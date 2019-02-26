package shopee.test;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import io.github.bonigarcia.wdm.WebDriverManager;
import shopee.test.model.Search;

@RunWith( SpringJUnit4ClassRunner.class )
@SpringBootTest( classes = SeleniumTest.class )
public class SeleniumTest {
	private static final WebDriver driver;

	static {
		// System.setProperty( "webdriver.chrome.driver", "C:\\chromedriver_win32\\chromedriver.exe" );
		WebDriverManager.chromedriver().setup();

		driver = new ChromeDriver();
	}

	@Test
	public void search() throws InterruptedException {
		driver.get( "https://shopee.tw/" );

		Search search = PageFactory.initElements( driver, Search.class );

		wait( Search.XPATH_CLOSE );

		search.getClose().click();

		search.getInput().sendKeys( "夜燈" );

		search.getButton().click();

		wait( Search.XPATH_FIRST );

		search.getFirst().click();

		Thread.sleep( 5000 );

		driver.quit();
	}

	private void wait( String xpath ) {
		WebDriverWait wait = new WebDriverWait( driver, 20 );

		// 也可以用css selector
		wait.until( ExpectedConditions.presenceOfElementLocated( By.xpath( xpath ) ) );
	}
}