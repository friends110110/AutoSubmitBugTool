package com.example.tests.model;

import java.util.regex.Pattern;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import org.junit.*;
import static org.junit.Assert.*;
import static org.hamcrest.CoreMatchers.*;
import org.openqa.selenium.*;
import org.openqa.selenium.firefox.FirefoxBinary;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxProfile;
import org.openqa.selenium.support.ui.Select;

import com.example.tests.bean.FieldSets;
import com.example.tests.tool.WebDriverUtils;

public class CommitDataSetToUrlModel {
  private WebDriver driver;
  private boolean acceptNextAlert = true;
  private StringBuffer verificationErrors = new StringBuffer();

  public CommitDataSetToUrlModel(){
	  driver=WebDriverUtils.getWebDriver();
  }
  @Before
  public void setUp(String firefoxPath) throws Exception {

  }
  
  public void callProgram(FieldSets fieldSet) throws Exception{
	  ArrayList<ArrayList<String>> contentLists=fieldSet.contentList;
	  if(contentLists.size()<=0){
		  throw new Exception("数据数太少 明显不对");
	  } 
	  for(int i=0;i<contentLists.size();i++){
		  ArrayList<String> list=contentLists.get(i);
		  callProgram(list);
	  }
	  WebDriverUtils.closeWebDriver();
  }
  public void callProgram(ArrayList<String> list)throws Exception{
	  if(list.size()<4){
		  throw new Exception("输入参数不够");
	  }
	  String problemUrl=list.get(0);
	  String result=list.get(1);
	  String solution=list.get(2);
	  String comment=list.get(3);
	  callProgram(problemUrl,result,solution,comment);
  }
  //solve one problem 
/**
 * 
 * @param problemUrl
 * @param result	未解决(尚未处理)、未解决(延后解决)、未解决(无法解决)、未解决(不需解决)、未解决(无法重现)、解决(修复成功)、解决(不是缺陷)、解决(重复缺陷)"
 * @param solution
 * @param comment
 */
  public void callProgram(String problemUrl,String result,String solution,String comment)throws Exception{
	    if(null==result||"".equals(result)){
	    	result="解决(修复成功)";
	    }	
	    driver.get(problemUrl);
	    driver.findElement(By.id("action_id_721")).click();
	    driver.findElement(By.id("分配任务")).click();
	    driver.findElement(By.id("action_id_751")).click();
	    new Select(driver.findElement(By.id("resolution"))).selectByVisibleText(result);
	    new Select(driver.findElement(By.id("customfield_10330"))).selectByVisibleText("其他");
	    driver.findElement(By.id("customfield_10007")).clear();
	    driver.findElement(By.id("customfield_10007")).sendKeys(""+solution);
	    new Select(driver.findElement(By.id("customfield_10392"))).selectByVisibleText("否");
	    driver.findElement(By.id("comment")).clear();
	    driver.findElement(By.id("comment")).sendKeys(""+comment);
	    driver.findElement(By.id("解决缺陷")).click();
	  
  }

  @Test
  public void testFullAuto() throws Exception {
	  
//	  callProgram("20854","放大了图标","","添加备注");
	  
  }

  @After
  public void tearDown() throws Exception {
    driver.quit();
    String verificationErrorString = verificationErrors.toString();
    if (!"".equals(verificationErrorString)) {
      fail(verificationErrorString);
    }
  }

  private boolean isElementPresent(By by) {
    try {
      driver.findElement(by);
      return true;
    } catch (NoSuchElementException e) {
      return false;
    }
  }

  private boolean isAlertPresent() {
    try {
      driver.switchTo().alert();
      return true;
    } catch (NoAlertPresentException e) {
      return false;
    }
  }

  private String closeAlertAndGetItsText() {
    try {
      Alert alert = driver.switchTo().alert();
      String alertText = alert.getText();
      if (acceptNextAlert) {
        alert.accept();
      } else {
        alert.dismiss();
      }
      return alertText;
    } finally {
      acceptNextAlert = true;
    }
  }
}