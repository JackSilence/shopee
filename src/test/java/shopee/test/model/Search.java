package shopee.test.model;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

public class Search {
	public static final String XPATH_CLOSE = "//*[@id=\"modal\"]/div/div/div[2]/div";
	
	public static final String XPATH_FIRST = "//*[@id=\"main\"]/div/div[2]/div[2]/div/div/div[2]/div[2]/div[2]/div/div[2]/div[1]/div/a";
	
	@FindBy( xpath = XPATH_CLOSE )
	private WebElement close;

	@FindBy( xpath = "//*[@id=\"main\"]/div/div[2]/div[1]/div/div[2]/div/div[1]/div[1]/button" )
	private WebElement button;

	@FindBy( xpath = "//*[@id=\"main\"]/div/div[2]/div[1]/div/div[2]/div/div[1]/div[1]/div/form/input" )
	private WebElement input;

	@FindBy( xpath = XPATH_FIRST )
	private WebElement first;

	public WebElement getClose() {
		return close;
	}

	public WebElement getButton() {
		return button;
	}

	public WebElement getInput() {
		return input;
	}

	public WebElement getFirst() {
		return first;
	}
}