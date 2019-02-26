@search_flow
Feature: This is a simple test using Cucumber.
  Scenario:
    Given I enter url "https://shopee.tw/"
    # Search Button Feature:
    When I enter "米家" in the input field
    And I click the search button
    Then I click the first result of search