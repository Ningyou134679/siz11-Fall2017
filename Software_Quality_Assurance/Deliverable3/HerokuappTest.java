import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.*;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.htmlunit.HtmlUnitDriver;
import java.util.logging.*;
import java.util.List;

/**
 * As a user,
 * I would like to see herokuapp links in all sorts of ways,
 * So that I can  calculate factorial, fibonacci, and see pictures of the cathedral
 * @author simingZ
 *
 */

/**
*note that the tests below are expected to fail because of defects on the web app
*testFibonacciEmpty()
*testFactorialEmpty()
*testFibonacciString()
*testLeadText2()
*testFactorialString()
*/

public class HerokuappTest{
  static WebDriver driver;

  //start up home page for https://cs1632ex.herokuapp.com/
  //the setLevel and setProperty is for disabling logs, which can be found in https://github.com/laboon/CS1632_Fall2017/blob/master/sample_code/selenium_example/RedditTestRunnerNoLogs.java
  @Before
  public void setUp() throws Exception{
    java.util.logging.Logger.getLogger("com.gargoylesoftware").setLevel(Level.OFF); 
    System.setProperty("org.apache.commons.logging.Log", "org.apache.commons.logging.impl.NoOpLog");
    driver = new HtmlUnitDriver(); 
    driver.get("https://cs1632ex.herokuapp.com/");
  }
  // Given that I am on the main page
  // When I view the text in jumbotron
  // Then I see that it contains the string "Welcome, friend, to a land of pure calculation"
  @Test
  public void testLeadText() {
    try {
      //getText() method converts <br> into \n, instead of the " " in the requirement, so I replace "\n" with " "
      WebElement e = driver.findElement(By.className("jumbotron"));
      String elementText = e.getText().replace("\n"," ");
      assertTrue(elementText.contains("Welcome, friend, to a land of pure calculation"));
    } catch (NoSuchElementException nseex) {
      fail();
    }
  }
  // Given that I am on the main page
  // When I view the text in jumbotron
  // Then I see that it contains the string "Used for CS1632 Software Quality Assurance, taught by Bill Laboon"
  @Test
  public void testLeadText2() {
    try {
      WebElement e = driver.findElement(By.className("jumbotron"));
      String elementText = e.getText().replace("\n"," ");
      assertTrue(elementText.contains("Used for CS1632 Software Quality Assurance, taught by Bill Laboon"));
    } catch (NoSuchElementException nseex) {
      fail();
    }
  }

  // Given that I am on the main page
  // When I view the links in the navigation bar
  // Then I see "CS1632 D3 Home", "Factorial", "Fibonacci", "Hello", and "Cathedral Pics"
  @Test
  public void testHasCorrectHeaderLinks() {
    //checks all five elements
    //if one is not found, fail the test
    try {
      driver.findElement(By.linkText("CS1632 D3 Home"));
      driver.findElement(By.linkText("Factorial"));
      driver.findElement(By.linkText("Fibonacci"));
      driver.findElement(By.linkText("Hello"));
      driver.findElement(By.linkText("Cathedral Pics"));
    } catch (NoSuchElementException nseex) {
      fail();
    }
  }

  // Given that I am on the main page
  // When I click on the "CS1632 D3 Home" option
  // Then it should navigate me to "https://cs1632ex.herokuapp.com/"
  @Test
  public void testCorrectHomeHref(){
    try{
      WebElement homeLink = driver.findElement(By.linkText("CS1632 D3 Home"));
      assertTrue(homeLink.getAttribute("href").equals("https://cs1632ex.herokuapp.com/"));
    }catch(NoSuchElementException nseex){
      fail();
    }
  }
  // Given that I am on the main page
  // When I click on the "Factorial" option
  // Then it should navigate me to "https://cs1632ex.herokuapp.com/fact"
  @Test
  public void testCorrectFactorialHref(){
    try{
      WebElement factLink = driver.findElement(By.linkText("Factorial"));
      assertTrue(factLink.getAttribute("href").equals("https://cs1632ex.herokuapp.com/fact"));
    }catch(NoSuchElementException nseex){
      fail();
    }
  }
  // Given that I am on the main page
  // When I click on the "Fibonacci" option
  // Then it should navigate me to "https://cs1632ex.herokuapp.com/fib"
  @Test
  public void testCorrectFibonacciHref(){
    try{
      WebElement fibLink = driver.findElement(By.linkText("Fibonacci"));
      assertTrue(fibLink.getAttribute("href").equals("https://cs1632ex.herokuapp.com/fib"));
    }catch(NoSuchElementException nseex){
      fail();
    }
  }
  // Given that I am on the main page
  // When I click on the "Hello" option
  // Then it should navigate me to "https://cs1632ex.herokuapp.com/hello"
  @Test
  public void testCorrectHelloHref(){
    try{
      WebElement helloLink = driver.findElement(By.linkText("Hello"));
      assertTrue(helloLink.getAttribute("href").equals("https://cs1632ex.herokuapp.com/hello"));
    }catch(NoSuchElementException nseex){
      fail();
    }
  }
  // Given that I am on the main page
  // When I click on the "Hello" option
  // Then it should navigate me to "https://cs1632ex.herokuapp.com/hello"
  @Test
  public void testCorrectCathedralHref(){
    try{
      WebElement cathedralLink = driver.findElement(By.linkText("Cathedral Pics"));
      assertTrue(cathedralLink.getAttribute("href").equals("https://cs1632ex.herokuapp.com/cathy"));
    }catch(NoSuchElementException nseex){
      fail();
    }
  }
  // Given that I am on the factorial page
  // When I enter 5 and submit
  // Then I should see "Factorial of 5 is 120!"
  @Test
  public void testFactorial(){ 
    //on home page, click on "Factorial" in the navigation bar
    //find the text area of the form by the name "value"
    //set the text field to 5
    //then, find the submit button and click it
    //the new page shall display "Factorial of 5 is 120!"
    try {
      driver.findElement(By.linkText("Factorial")).click();
      WebElement value = driver.findElement(By.name("value"));
      value.sendKeys("5");
      WebElement submitBtn = driver.findElement(By.xpath("//input[@type='submit']"));
      submitBtn.click();
      WebElement output = driver.findElement(By.className("jumbotron"));
      String outputText = output.getText().replace("\n"," ");
      assertTrue(outputText.equals("Factorial of 5 is 120!"));
    } catch (NoSuchElementException nseex) {
      fail();
    }
  }
  // Given that I am on the factorial page
  // When I enter -100 and submit
  // Then I should see "Factorial of -100 is 1!"
  @Test
  public void testFactorialNegative(){ 
    //on home page, click on "Factorial" in the navigation bar
    //find the text area of the form by the name "value"
    //set the text field to -100
    //then, find the submit button and click it
    //the new page shall display "Factorial of -100 is 1!"
    try {
      driver.findElement(By.linkText("Factorial")).click();
      WebElement value = driver.findElement(By.name("value"));
      value.sendKeys("-100");
      WebElement submitBtn = driver.findElement(By.xpath("//input[@type='submit']"));
      submitBtn.click();
      WebElement output = driver.findElement(By.className("jumbotron"));
      String outputText = output.getText().replace("\n"," ");
      assertTrue(outputText.equals("Factorial of -100 is 1!"));
    } catch (NoSuchElementException nseex) {
      fail();
    }
  }
  // Given that I am on the factorial page
  // When I enter 101 and submit
  // Then I should see "Factorial of 101 is 1!"
  @Test
  public void testFactorialExceed(){ 
    //on home page, click on "Factorial" in the navigation bar
    //find the text area of the form by the name "value"
    //set the text field to 101
    //then, find the submit button and click it
    //the new page shall display "Factorial of 101 is 120!"
    try {
      driver.findElement(By.linkText("Factorial")).click();
      WebElement value = driver.findElement(By.name("value"));
      value.sendKeys("101");
      WebElement submitBtn = driver.findElement(By.xpath("//input[@type='submit']"));
      submitBtn.click();
      WebElement output = driver.findElement(By.className("jumbotron"));
      String outputText = output.getText().replace("\n"," ");
      assertTrue(outputText.equals("Factorial of 101 is 1!"));
    } catch (NoSuchElementException nseex) {
      fail();
    }
  }
  // Given that I am on the factorial page
  // When I enter "a" and submit
  // Then I should see "Factorial of a is 1!"
  @Test
  public void testFactorialString(){ 
    //on home page, click on "Factorial" in the navigation bar
    //find the text area of the form by the name "value"
    //set the text field to "a"
    //then, find the submit button and click it
    //the new page shall display "Factorial of a is 1!"
    try {
      driver.findElement(By.linkText("Factorial")).click();
      WebElement value = driver.findElement(By.name("value"));
      value.sendKeys("a");
      WebElement submitBtn = driver.findElement(By.xpath("//input[@type='submit']"));
      submitBtn.click();
      WebElement output = driver.findElement(By.className("jumbotron"));
      String outputText = output.getText().replace("\n"," ");
      assertTrue(outputText.equals("Factorial of a is 1!"));
    } catch (NoSuchElementException nseex) {
      fail();
    }
  }
  // Given that I am on the factorial page
  // When I do not enter anything and submit
  // Then I should see "Factorial of is 1!"
  @Test
  public void testFactorialEmpty(){ 
    //on home page, click on "Factorial" in the navigation bar
    //find the text area of the form by the name "value"
    //set the text field to empty
    //then, find the submit button and click it
    //the new page shall display "Factorial of is 120!"
    try {
      driver.findElement(By.linkText("Factorial")).click();
      WebElement value = driver.findElement(By.name("value"));
      value.sendKeys("");
      WebElement submitBtn = driver.findElement(By.xpath("//input[@type='submit']"));
      submitBtn.click();
      WebElement output = driver.findElement(By.className("jumbotron"));
      String outputText = output.getText().replace("\n"," ");
      assertTrue(outputText.equals("Factorial of is 1!"));
    } catch (NoSuchElementException nseex) {
      fail();
    }
  }
  // Given that I am on the fabonacci page
  // When I enter 5 and submit
  // Then I should see "Fibonacci of 5 is 8!"
  @Test
  public void testFibonacci(){ 
    //on home page, click on "Fibonacci" in the navigation bar
    //find the text area of the form by the name "value"
    //set the text field to "5"
    //then, find the submit button and click it
    //the new page shall display "Fibonacci of 5 is 8!"
    try {
      driver.findElement(By.linkText("Fibonacci")).click();
      WebElement value = driver.findElement(By.name("value"));
      value.sendKeys("5");
      WebElement submitBtn = driver.findElement(By.xpath("//input[@type='submit']"));
      submitBtn.click();
      WebElement output = driver.findElement(By.className("jumbotron"));
      String outputText = output.getText().replace("\n"," ");
      assertTrue(outputText.equals("Fibonacci of 5 is 8!"));
    } catch (NoSuchElementException nseex) {
      fail();
    }
  }
  // Given that I am on the fabonacci page
  // When I enter -100 and submit
  // Then I should see "Fibonacci of -100 is 1!"
  @Test
  public void testFibonacciNegative(){ 
    //on home page, click on "Fibonacci" in the navigation bar
    //find the text area of the form by the name "value"
    //set the text field to "-100"
    //then, find the submit button and click it
    //the new page shall display "Fibonacci of -100 is 1!"
    try {
      driver.findElement(By.linkText("Fibonacci")).click();
      WebElement value = driver.findElement(By.name("value"));
      value.sendKeys("-100");
      WebElement submitBtn = driver.findElement(By.xpath("//input[@type='submit']"));
      submitBtn.click();
      WebElement output = driver.findElement(By.className("jumbotron"));
      String outputText = output.getText().replace("\n"," ");
      assertTrue(outputText.equals("Fibonacci of -100 is 1!"));
    } catch (NoSuchElementException nseex) {   
      fail();
    }
  }
  // Given that I am on the fabonacci page
  // When I enter 101 and submit
  // Then I should see "Fibonacci of 101 is 1!"
  @Test
  public void testFibonacciExceed(){ 
    //on home page, click on "Fibonacci" in the navigation bar
    //find the text area of the form by the name "value"
    //set the text field to "101"
    //then, find the submit button and click it
    //the new page shall display "Fibonacci of 101 is 1!"
    try {
      driver.findElement(By.linkText("Fibonacci")).click();
      WebElement value = driver.findElement(By.name("value"));
      value.sendKeys("101");
      WebElement submitBtn = driver.findElement(By.xpath("//input[@type='submit']"));
      submitBtn.click();
      WebElement output = driver.findElement(By.className("jumbotron"));
      String outputText = output.getText().replace("\n"," ");
      assertTrue(outputText.equals("Fibonacci of 101 is 1!"));
    } catch (NoSuchElementException nseex) {   
      fail();
    }
  }
  // Given that I am on the fabonacci page
  // When I enter a and submit
  // Then I should see "Fibonacci of a is 1!"
  @Test
  public void testFibonacciString(){ 
    try {
      driver.findElement(By.linkText("Fibonacci")).click();
      WebElement value = driver.findElement(By.name("value"));
      value.sendKeys("a");
      WebElement submitBtn = driver.findElement(By.xpath("//input[@type='submit']"));
      submitBtn.click();
      WebElement output = driver.findElement(By.className("jumbotron"));
      String outputText = output.getText().replace("\n"," ");
      assertTrue(outputText.equals("Fibonacci of a is 1!"));
    } catch (NoSuchElementException nseex) { 
      fail();
    }
  }
  // Given that I am on the fabonacci page
  // When I do not enter anything and submit
  // Then I should see "Fibonacci of is 1!"
  @Test
  public void testFibonacciEmpty(){ 
    //on home page, click on "Fibonacci" in the navigation bar
    //find the text area of the form by the name "value"
    //set the text field to empty
    //then, find the submit button and click it
    //the new page shall display "Fibonacci of is 1!"
    try {
      driver.findElement(By.linkText("Fibonacci")).click();
      WebElement value = driver.findElement(By.name("value"));
      value.sendKeys("");
      WebElement submitBtn = driver.findElement(By.xpath("//input[@type='submit']"));
      submitBtn.click();
      WebElement output = driver.findElement(By.className("jumbotron"));
      String outputText = output.getText().replace("\n"," ");
      assertTrue(outputText.equals("Fibonacci of is 1!"));
    } catch (NoSuchElementException nseex) { 
      fail();
    }
  }
  // Given that I am on the hello page
  // When I observed the text displayed
  // Then I should see "Hello CS1632, from Prof. Laboon!"
  @Test
  public void testHello(){
    try {
      //on home page, click on "Hello" in the navigation bar
      //find the text from the page by the class name "jumbotron"
      //the text should be "Hello CS1632, from Prof. Laboon!"
      driver.findElement(By.linkText("Hello")).click();
      WebElement textE = driver.findElement(By.className("jumbotron"));
      String helloText = textE.getText().replace("\n"," ");
      assertTrue(helloText.equals("Hello CS1632, from Prof. Laboon!"));
    } catch (NoSuchElementException nseex) {   
      fail();
    }
  }
  // Given that I am on the hello page, with a traling of "/Jazzy"
  // When I observed the text displayed
  // Then I should see "Hello CS1632, from Jazzy"
  @Test
  public void testHelloTrailing(){
    try {
      //on home page, navigate to "https://cs1632ex.herokuapp.com/hello/Jazzy"
      //find the text from the page by the class name "jumbotron"
      //the text should be "Hello CS1632, from Jazzy""
      driver.navigate().to("https://cs1632ex.herokuapp.com/hello/Jazzy");
      WebElement textE = driver.findElement(By.className("jumbotron"));
      String helloText = textE.getText().replace("\n"," ");
      assertTrue(helloText.equals("Hello CS1632, from Jazzy!"));
    } catch (NoSuchElementException nseex) {
      fail();
    }
  }
  // Given that I am on the Cathedral Pics page
  // When I observed the page
  // Then I should see 3 images in an ordered list
  @Test
  public void testCathy(){
    //on homepage, click on the "Cathedral Pics" in the navigation bar
    //get the ordered list element
    //get the list of elements under the ordered list
    //the number of items in the list should be 3
    try {
      driver.findElement(By.linkText("Cathedral Pics")).click();
      WebElement ol = driver.findElement(By.tagName("ol"));
      List<WebElement> list = ol.findElements(By.tagName("img"));
      assertTrue(list.size()==3);
    } catch (NoSuchElementException nseex) {   
      fail();
    }
  }
  
}

