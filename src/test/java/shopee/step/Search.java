package shopee.step;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import cucumber.api.java8.En;
import io.github.bonigarcia.wdm.WebDriverManager;
import magic.util.Utils;

public class Search implements En {
	private static final WebDriver driver;

	static {
		WebDriverManager.chromedriver().setup();

		driver = new ChromeDriver();
	}

	public Search() {
		shopee.test.model.Search search = PageFactory.initElements( driver, shopee.test.model.Search.class );

		Given( "^I enter url \"([^\"]*)\"$", ( String url ) -> {
			driver.get( url );

			wait( shopee.test.model.Search.XPATH_CLOSE );

			search.getClose().click();
		} );

		When( "^I enter \"([^\"]*)\" in the input field$", ( String keyword ) -> {
			search.getInput().sendKeys( keyword );
		} );

		And( "^I click the search button$", () -> {
			search.getButton().click();
		} );

		Then( "^I click the first result of search$", () -> {
			wait( shopee.test.model.Search.XPATH_FIRST );

			search.getFirst().click();

			Utils.sleep( 5000 );

			driver.quit();
		} );
	}

	private void wait( String xpath ) {
		WebDriverWait wait = new WebDriverWait( driver, 20 );

		// 也可以用css selector
		wait.until( ExpectedConditions.presenceOfElementLocated( By.xpath( xpath ) ) );
	}
}