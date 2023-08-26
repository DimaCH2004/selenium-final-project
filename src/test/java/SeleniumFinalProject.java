import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.testng.Assert;
import org.testng.annotations.Test;
import java.util.List;
public class SeleniumFinalProject extends TestSetup {

    private String movieName;
    private String cinemaText;
    private String[] dateText;
    private String[] timeText;

    @Test
    public void testSwoop() throws InterruptedException {
        navigateToWebsite();
        int index = 0;
        boolean validFilm = false;
        while (!validFilm){
            selectFirstMovie(index);
            try{
                selectCinema();
                validFilm = true;
            }catch(NoSuchElementException e){
                index++;
                driver.navigate().back();
            }
        }
        selectDateAndOption();
        validatePopupDetails();
        chooseSeatAndRegister();
        validateErrorMessage();
    }
    private void navigateToWebsite() {
        driver.navigate().to("https://www.swoop.ge");
        WebElement filmButton = driver.findElement(By.linkText("კინო"));
        filmButton.click();
    }
    private void selectFirstMovie(int filmIndex) {
        wait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(By.cssSelector("div.container.cinema_container .movies-deal")));
        List<WebElement> films = driver.findElements(By.cssSelector("div.container.cinema_container .movies-deal"));
        WebElement movieToBuy = films.get(filmIndex);
        movieName = movieToBuy.getText();
        action.moveToElement(movieToBuy).build().perform();
        WebElement linkElement = driver.findElement(By.linkText("ყიდვა"));
        linkElement.click();
    }
    private void selectCinema() {
        WebElement cinema = driver.findElement(By.xpath("//a[text()='კავეა ისთ ფოინთი']"));
        cinemaText = cinema.getText();
        js.executeScript("arguments[0].scrollIntoView(true);", cinema);
        js.executeScript("window.scrollBy(0,-100);");
        cinema.click();
        List<WebElement> validateOptions = driver.findElements(By.xpath("//div[@aria-hidden='false']//a//p[@class=\"cinema-title\"]"));
        validateOptions.removeIf(list -> !list.isEnabled() || !list.isDisplayed());
        for (WebElement option:validateOptions){
            Assert.assertEquals(option.getText(),"კავეა ისთ ფოინთი");
        }
    }
    private void selectDateAndOption() {
        List<WebElement> dateElements = driver.findElements(By.xpath("//ul[@role='tablist']/li/a[@class='ui-tabs-anchor' and @role='presentation' and contains(@href, 'day-choose-')]"));
        dateElements.removeIf(list -> !list.isEnabled() || !list.isDisplayed());
        WebElement date = dateElements.get(dateElements.size() - 1);
        dateText = date.getText().split(" ");
        date.click();
        List<WebElement> optionElements = driver.findElements(By.xpath("//div[@aria-hidden='false']//a"));
        optionElements.removeIf(list -> !list.isEnabled() || !list.isDisplayed());
        WebElement chosenOption = optionElements.get(optionElements.size() - 1);
        timeText = chosenOption.getText().split("\n");
        chosenOption.click();
    }
    private void validatePopupDetails() {
        WebElement contentHeader = wait.until(ExpectedConditions.visibilityOfElementLocated(By.className("content-header")));
        List<WebElement> detailElements = contentHeader.findElements(By.tagName("p"));
        String[] dateAndTime = detailElements.get(2).getText().split(" ");
        Assert.assertEquals(detailElements.get(0).getText(), movieName);
        Assert.assertEquals(detailElements.get(1).getText(), cinemaText);
        Assert.assertEquals(dateAndTime[0], dateText[0]);
        Assert.assertEquals(dateAndTime[2], timeText[0]);
    }
    private void chooseSeatAndRegister() {
        WebElement firstPlace = driver.findElement(By.cssSelector(".seat:not(.occupied)"));
        firstPlace.click();
        WebElement registrationButton = wait.until(ExpectedConditions.elementToBeClickable(By.cssSelector("p.register")));
        registrationButton.click();
        fillForm();
    }
    private void fillForm() {
        driver.findElement(By.id("pFirstName")).sendKeys("დიმა");
        driver.findElement(By.id("pLastName")).sendKeys("ჩუბინიძე");
        driver.findElement(By.id("pEmail")).sendKeys("დიმა@ჩუბინიძე");
        driver.findElement(By.id("pPhone")).sendKeys("579172742");
        driver.findElement(By.id("pDateBirth")).sendKeys("03-24-2004");
        WebElement genderDropDown = driver.findElement(By.id("pGender"));
        genderDropDown.sendKeys(Keys.ARROW_DOWN);
        genderDropDown.sendKeys(Keys.RETURN);
        driver.findElement(By.id("pPassword")).sendKeys("password123");
        driver.findElement(By.id("pConfirmPassword")).sendKeys("password123");
        driver.findElement(By.id("pIsAgreeTerns")).click();
        driver.findElement(By.cssSelector(".dashbord-registration input[type='button']")).click();
    }
    private void validateErrorMessage() {
        String errorMessageElement = wait.until(ExpectedConditions.presenceOfElementLocated(By.id("physicalInfoMassage"))).getText();
        Assert.assertEquals(errorMessageElement, "მეილის ფორმატი არასწორია!");
    }
}

