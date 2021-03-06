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
import com.example.tests.tool.ConstantValue;
import com.example.tests.tool.WebDriverUtils;

import rx.Observable;
import rx.functions.Action1;

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
  
  public void callProgram(FieldSets fieldSet) throws Exception {
	  final ArrayList<ArrayList<String>> contentLists=fieldSet.contentList;
	  if(contentLists.size()<=0){
		  throw new Exception("数据数太少 明显不对");
	  } 
	  Observable.from(contentLists).subscribe(new Action1<ArrayList<String>>() {
		@Override
		public void call(ArrayList<String> list) {
			try {
				callProgram(list);
			} catch (Exception e) {
				e.printStackTrace();
				//throw e;
			}
			System.out.println(list.toString() +"  has successfully been submited ");
			contentLists.remove(list);
		}
	});
//	  WebDriverUtils.closeWebDriver();
  }
  public void callProgramToSubmit(ArrayList<ArrayList<String>> lists )throws Exception{
	  for (int i=0;i<lists.size();i++){
		  ArrayList<String> list=lists.get(i);
		  callProgram(list);
		  lists.remove(0);
		  i--;
	  }
  }
  
  public void callProgram(ArrayList<String> list)throws Exception{
	  String problemUrl=list.get(0);
	  //  list.get(1)   description  is just message
	  String result=list.get(2);
	  String solution=list.get(3);
	  String comment=list.get(4);
	  String assignUser=list.get(5);
	  String assignComment=list.get(6);
	  callProgram(problemUrl,result,solution,comment,assignUser,assignComment);
	  Thread.sleep(1000);
  }
  //solve one problem 
/**
 * 
 * @param problemUrl
 * @param result	未解决(尚未处理)、未解决(延后解决)、未解决(无法解决)、未解决(不需解决)、未解决(无法重现)、解决(修复成功)、解决(不是缺陷)、解决(重复缺陷)"
 * @param solution
 * @param comment
 */
  public void callProgram(String problemUrl,String result,String solution,String comment,
		  String assignUser,String assignComment) throws Exception{
	    //it is the Chinese name
	    String regex = "[\u4E00-\u9FA5]+";
		WebDriverUtils.setImplicitlyWaitSeconds(ConstantValue.TIMEOUT_SECONDS);	//set outTime
	    driver.get(problemUrl);
	    System.out.println("now solve the website:---->"+problemUrl);
	    try{	//if the problem has been assigned, (ps: 分配 button has disappeared,and next page is decide to solve problem or reject)
    		if(null!=assignUser&&!"".equals(assignUser)&&assignUser.matches(regex)){
    			driver.findElement(By.id("assign_issue")).click();
	    	    Thread.sleep(1000);
		    	driver.findElement(By.id("comment")).clear();
		    	driver.findElement(By.id("comment")).sendKeys(assignComment);
		    	try{
		    		new Select(driver.findElement(By.id("assignee"))).selectByVisibleText(assignUser);
		    	    //System.out.println("firstly select person :----->"+assignUser);

		    		
		    	}catch(Exception e){
			    	System.out.println("please correct the assignUser, cant not find the assignUser" +assignUser);
			    	e.printStackTrace();
			    	throw new Exception("please correct the assignUser");
		    	}	
		    	driver.findElement(By.id("分配")).click();	
	    	    System.out.println(" assigne to :----->"+assignUser);
			    return;
    		}
    	}catch(Exception e){
    		System.out.println("分配了人，但是操作无效");
    		return;
    	}
	    if(null==result||"".equals(result)){
	    	result="解决(修复成功)";
	    }	
	    //先分配给自己 再解决缺陷
	    try{
	    	driver.findElement(By.id("action_id_721")).click();
	    	driver.findElement(By.id("分配任务")).click();	
	    }catch(Exception e){
	    	System.out.println("已经分配给自己了的，下面解决问题");
	    }
	    //mock operation click to solve bug
    	//解决缺陷
	    Thread.sleep(1000);
	    driver.findElement(By.id("action_id_751")).click();
	    Thread.sleep(1000);
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
