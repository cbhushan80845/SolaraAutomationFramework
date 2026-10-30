
package com.airline.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import com.airline.base.BasePage;

public class standardSearchPage extends BasePage {

	// Constructor
	public standardSearchPage(WebDriver driver, int explicitWait) {
		super(driver, explicitWait);
	}

	// Locators
	private By standardSearchHeader = By.xpath("//h2[contains(normalize-space(.),'1. Standard Search')]");

	private By standardSearchSubHeader = By
			.xpath("//p[contains(normalize-space(.),'Reliable default for most product screens')]");

	private By basicSearchInput = By.id("basicSearchInput");

	private By basicSearchForm = By.id("basicSearchForm");

	private By clearButton = By.cssSelector("button.clear");

	private By basicSearchResults = By.cssSelector("#basicSearchResults li");
	private By basicSearchStatus = By.id("basicSearchStatus");

	// Validate header visibility
	public boolean validateHeaderText() {
		return waitUtil.waitForElementVisible(standardSearchHeader).isDisplayed();
	}

	// Get sub-header text
	public String validateSubHeaderText() {
		return waitUtil.waitForElementVisible(standardSearchSubHeader).getText();
	}

	// Enter search text and press Enter
	public void enterTextInSearchBar(String searchTerm) {

		WebElement element = waitUtil.waitForElementClickable(basicSearchInput);

		element.clear();
		element.sendKeys(searchTerm);
		element.sendKeys(Keys.ENTER);
	}

	// Click clear button
	public void clickOnClear() {
		waitUtil.waitForElementClickable(clearButton).click();
	}

	// Get entered search text
	public String getSearchInputValue() {
		return waitUtil.waitForElementVisible(basicSearchInput).getAttribute("value");
	}

	// Get search status message
	public String getSearchStatus() {
		return waitUtil.waitForElementVisible(basicSearchStatus).getText();
	}

	// Get number of search results
	public int getSearchResultCount() {
		return driver.findElements(basicSearchResults).size();
	}

	// Check whether search results are displayed
	public boolean areSearchResultsDisplayed() {
		return getSearchResultCount()>0;
	}

	// Get all search result elements
	public java.util.List<WebElement> getSearchResults() {
		return driver.findElements(basicSearchResults);
	}
}
