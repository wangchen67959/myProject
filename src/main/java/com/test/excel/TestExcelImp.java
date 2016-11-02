package com.test.excel;

import java.io.FileInputStream;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;

import com.test.service.TestService;

public class TestExcelImp {
	@Autowired
	private  TestService testService;

	public TestService getTestService() {
		return testService;
	}
	public void setTestService(TestService testService) {
		this.testService = testService;
	}
	public static void main(String[] args) {
		ExcelImport excelImp = new ExcelImport();
		try {
			InputStream is = new FileInputStream("d:\\test2.xls");
			excelImp.createWorkBook(is);
			excelImp.setSheetNum(0); // 设置读取的sheet页数
			excelImp.getCellCount();
			int rowCount = excelImp.getRowCount();
			for (int i = 1; i <= rowCount; i++) {
				Map<String, String> map = new HashMap<String, String>();
				String[] cellContent = excelImp.readExcelRow(i);
				if (null == cellContent)
					continue;
				int j =0;
				map.put("id", cellContent[j++]);
				map.put("name",  cellContent[j++]);
				map.put("age",  cellContent[j++]);
				map.put("birthdate",  cellContent[j++]);
				
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
