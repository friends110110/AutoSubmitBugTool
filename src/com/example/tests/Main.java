package com.example.tests;

import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Scanner;

import org.junit.Test;

import com.example.tests.bean.FieldBean;
import com.example.tests.bean.FieldSets;
import com.example.tests.configuration.ConfigParams;
import com.example.tests.model.CreateExcelformUrlModel;
import com.example.tests.service.FieldServiceImpl;
import com.example.tests.model.CommitDataSetToUrlModel;
import com.example.tests.tool.ConstantValue;

import rx.Observable;
import rx.functions.Action1;

public class Main {

	@Test
	public void testsCreateExcel(){
		CreateExcelformUrlModel fileTool=new CreateExcelformUrlModel(); 
		ArrayList<ArrayList<String>> data =new ArrayList<ArrayList<String>> ();
		for(int i=0;i<5;i++){
			ArrayList<String> str=new ArrayList<String>();
			for(int j=0;j<7;j++){
				str.add(i+"  "+String.valueOf(j)+"安抚安抚安抚安抚安抚萨芬的安抚安抚暗室逢灯安抚暗室逢灯暗室逢灯安抚安抚安抚阿斯蒂芬萨芬的昂视发");
			}
			data.add(str);
		}
		FieldSets fieldSet=new FieldSets();
		String []titles={"problem_id","description","solution","comment","5","6","7"};
		ArrayList<String> titleList=new ArrayList(Arrays.asList(titles));
		fieldSet.titleList=titleList;
		fieldSet.contentList=data;
		fileTool.createExcel("e:\\44.xlsx",fieldSet);
		
	}
	@Test
	public void testWriteFileAgain(){
		CreateExcelformUrlModel fileTool=new CreateExcelformUrlModel(); 
		final ArrayList<ArrayList<String>> data =new ArrayList<ArrayList<String>> ();
		for(int i=0;i<5;i++){
			ArrayList<String> str=new ArrayList<String>();
			for(int j=0;j<3;j++){
				str.add(i+"  "+String.valueOf(j));
			}
			data.add(str);
		}
		Observable.from(data).subscribe(new Action1<ArrayList<String>>(){

			@Override
			public void call(ArrayList<String> list) {
				// TODO Auto-generated method stub
				data.remove(list);
			}
			
		});
		FieldSets fieldSet=new FieldSets();
		String []titles={"problem_id","description","solution","comment"};
		ArrayList<String> titleList=new ArrayList(Arrays.asList(titles));
		fieldSet.titleList=titleList;
		fieldSet.contentList=data;
		fileTool.createExcel(ConstantValue.BACKUP_FILE_PATH, fieldSet);
	}
	
	@SuppressWarnings("resource")
	public static void main(String[] args) throws Exception{
		System.out.println("将资源文件中的geckodriver.exe拿出来和AutoSubmitTool.jar 放同级");
		Scanner scanner=new Scanner(System.in);
		String params;
		ConfigParams paramsMap=ConfigParams.getInstance();
		if(!paramsMap.isExistConf()){
			while(scanner.hasNext()){
				params=scanner.nextLine(); 
				if(true==paramsMap.createConfFile(params)){
					break;
				}
			}
		}else{
			paramsMap.readConfFile();
		}
		FieldServiceImpl fieldServiceImpl=new FieldServiceImpl();
		if(!fieldServiceImpl.isExistenceFile(ConstantValue.BACKUP_FILE_PATH)){
			System.out.println("type in the website, n for the historical lastest record");
			while(scanner.hasNext()){
				params=scanner.nextLine(); 
				if(true==paramsMap.setWebsiteUrl(params)){
					System.out.println("success to save file");
					break;
				}
				System.out.println("type in the website, n for the historical lastest record");
			}
			if(true==fieldServiceImpl.createExcelFromUrl(paramsMap.getValue(ConstantValue.KEY_WEBSITE_URL))){
				 System.out.println("please modify the information at  "+ConstantValue.BACKUP_FILE_PATH);
				 System.out.println("请修改文件，修改完毕后，务必要保存哦，最好关闭文件！然后输入 y 继续...");
			}else{
				 System.out.println("fail to create excel");
				 throw new Exception("fail to create excel");
			}
		}else{
			System.out.println(ConstantValue.BACKUP_FILE_PATH +"  文件已经存在了,并且会打开");
			fieldServiceImpl.resetLogin(paramsMap.getValue(ConstantValue.KEY_WEBSITE_URL));
		    System.out.println("success to create bugs.xlsx,and open  bugs.xlsx file");  
            Runtime.getRuntime().exec("cmd  /c  start  "+ConstantValue.BACKUP_FILE_PATH);  			
			System.out.println("请修改文件，修改完毕后，务必要保存哦，最好关闭文件！然后输入 y 继续...");
		}
		while(scanner.hasNext()){
			String isContinueFlag=scanner.nextLine();
			if("y".equals(isContinueFlag)){
				//此时可能 session已经失效了，重新登录下
				fieldServiceImpl.resetLogin(paramsMap.getValue(ConstantValue.KEY_WEBSITE_URL));
				break;
			}
		}
		while(true){
			FieldSets fieldSets=fieldServiceImpl.parseExcelToDataSet();
			//fieldServiceImpl.removeCells(fieldSets, ConstantValue.DELETE_CELLS_NUMBER);
			boolean isAllDataSubmit=fieldServiceImpl.commitDataSetToUrl(fieldSets);
			if(true==isAllDataSubmit){
				break;
			}
			System.out.println(" type in y to continue .....");
			while(scanner.hasNext()){
				String isContinueFlag=scanner.nextLine();
				if("y".equals(isContinueFlag)){
					if(true==fieldServiceImpl.restoreExcelFile(fieldSets)){
						break;
					}
				}
			}
		}
		scanner.close();
		fieldServiceImpl.deleteExcelFile();
		System.out.println("thank you ,good bye! ");
	}
	@Test
	public void myTest() throws Exception{
		Scanner scanner=new Scanner(System.in);
		FieldServiceImpl fieldServiceImpl=new FieldServiceImpl();

		while(true){
			FieldSets fieldSets=fieldServiceImpl.parseExcelToDataSet();
			//fieldServiceImpl.removeCells(fieldSets, ConstantValue.DELETE_CELLS_NUMBER);
			fieldServiceImpl.commitDataSetToUrl(fieldSets);
			if(fieldSets.contentList.size()==0){
				break;
			}
			System.out.println(" type in y to continue .....");
			while(scanner.hasNext()){
				String isContinueFlag=scanner.nextLine();
				if("y".equals(isContinueFlag)){
					if(true==fieldServiceImpl.restoreExcelFile(fieldSets)){
						break;
					}
				}
			}
		}
		System.out.println("thank you ,good bye! ");
	}
	@Test
	public void testData(){
		CreateExcelformUrlModel fileTool=new CreateExcelformUrlModel(); 
		ConfigParams paramsMap=ConfigParams.getInstance();
		paramsMap.setWebsiteUrl("http://se.hundsun.com/dm/secure/IssueNavigator.jspa?reset=true&mode=hide&pid=20110908&assigneeSelect=issue_current_user&resolution=6&component=13356");
		try {
			paramsMap.readConfFile();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		//		FieldServiceImpl fieldServiceImpl=new FieldServiceImpl();
//		FieldSets fieldSets=fieldServiceImpl.parseExcelToDataSet();
//		fieldServiceImpl.removeCells(fieldSets, ConstantValue.deleteCellsNumber);
		//FieldServiceImpl fieldServiceImpl=new FieldServiceImpl();
//		fieldServiceImpl.createExcelFromUrl(ConstantValue.createExcelURL);
//		Scanner scanner=new Scanner(System.in);
//		while(scanner.hasNext()){
//			String isContinue=scanner.nextLine();
//			if("y".equals(isContinue)){
//				break;
//			}
//		}
		
		
//		FieldSets fieldSets=fileTool.parseExcelToDataSets(ConstantValue.BACKUP_FILE_PATH);
//		fileTool.deleteExcelFile();
//		boolean is=fileTool.createExcel(ConstantValue.BACKUP_FILE_PATH, fieldSets);
//		System.out.println(is);
	}

}
