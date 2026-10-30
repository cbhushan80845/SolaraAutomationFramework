
package com.airline.tests;

import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import com.airline.base.BaseTest;
import com.airline.pages.standardSearchPage;

public class StandardSearchTest extends BaseTest {

    private standardSearchPage searchPage;

    @BeforeMethod
    public void setUpSearchPage() {
        searchPage = new standardSearchPage(getDriver(), 10);
    }

    // Scenario 1: Verify Standard Search heading
    @Test
    public void verifyStandardSearchHeader() {

        Assert.assertTrue(
                searchPage.validateHeaderText(),
                "Standard Search heading is not displayed"
        );
    }

    // Scenario 2: Verify Standard Search subheading
    @Test
    public void verifyStandardSearchSubHeader() {

        String actualText = searchPage.validateSubHeaderText();

        Assert.assertEquals(
                actualText,
                "Reliable default for most product screens.",
                "Subheading text does not match"
        );
    }

    // Scenario 3: Search with valid airline
    @Test
    public void verifyValidSearch() {

        searchPage.enterTextInSearchBar("Indigo");

        Assert.assertTrue(
                searchPage.getSearchStatus().contains("flights found"),
                "Expected flight results were not displayed"
        );

        Assert.assertTrue(
                searchPage.areSearchResultsDisplayed(),
                "Search results are empty"
        );
    }

    // Scenario 4: Search with invalid airline
    @Test
    public void verifyInvalidSearch() {

        searchPage.enterTextInSearchBar("XYZAirline123");

        Assert.assertTrue(
                searchPage.getSearchStatus().contains("No flights found"),
                "Expected no-results message was not displayed"
        );

        Assert.assertEquals(
                searchPage.getSearchResultCount(),
                0,
                "Unexpected search results were displayed"
        );
    }

    // Scenario 5: Verify clear button
    @Test
    public void verifyClearSearch() {

        searchPage.enterTextInSearchBar("delhi");

        Assert.assertTrue(
                searchPage.areSearchResultsDisplayed(),
                "Search results were not displayed before clearing"
        );

        searchPage.clickOnClear();

        Assert.assertEquals(
                searchPage.getSearchInputValue(),
                "",
                "Search input was not cleared"
        );

        Assert.assertTrue(
                searchPage.getSearchResultCount() == 0,
                "Search results were not cleared"
        );
    }
}
