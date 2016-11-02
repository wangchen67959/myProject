package com.test.controller;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.test.entity.User;
import com.test.excel.ExcelExport;
import com.test.excel.ExcelImport;
import com.test.service.TestService;
import com.test.util.DateUtil;

@Controller
// @RequestMapping(value="/testController")
public class TestController {

	public static final Logger LOGGER = Logger.getLogger(TestController.class);

	@Autowired
	private TestService testService;

	@RequestMapping("/test.do")
	public void test(HttpServletRequest request, HttpServletResponse response) {

		try {
			String result = testService.testQuery();
			response.getWriter().print(result.toString());
		} catch (IOException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}

	}
	/**
	 * 返回结果map导入
	 * @param request
	 * @param response
	 */
	@RequestMapping("/test2.do")
	public void testExcel(HttpServletRequest request,
			HttpServletResponse response) {
		ExcelImport excelImp = new ExcelImport();
		try {
			InputStream is = new FileInputStream("d:\\test2.xls");
			excelImp.createWorkBook(is);
			excelImp.setSheetNum(0); // 设置读取的sheet页数
			excelImp.getCellCount();
			int rowCount = excelImp.getRowCount();
			for (int i = 1; i <= rowCount; i++) {
				Map<String, Object> map = new HashMap<String, Object>();
				String[] cellContent = excelImp.readExcelRow(i);
				if (null == cellContent)
					continue;
				int j = 0;
				map.put("id", cellContent[j++]);
				map.put("name", cellContent[j++]);
				map.put("age", cellContent[j++]);
				map.put("birthdate", cellContent[j++]);
				testService.insertV(map);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	/**
	 * 反射方法导入
	 * @param request
	 * @param response
	 */
	@RequestMapping(value="/test4.do")
	public void testInvokeExcel(HttpServletRequest request, 
			HttpServletResponse response) {
		ExcelImport excelImp = new ExcelImport();
		String[] fields = {"id", "name", "age", "address"};
		try{
			InputStream is = new FileInputStream("d:\\80.xls");
			excelImp.createWorkBook(is);
			excelImp.setSheetNum(0);
			int rowCount = excelImp.getRowCount();
			for(int i = 1; i<= rowCount; i++) {
				Map<String, Object> map = new HashMap<String, Object>();
				User u = excelImp.readExcelRow(User.class, fields, i);
				if(null == u)
					continue;
				map.put("id", u.getId());
				map.put("name", u.getName());
				map.put("age", u.getAge());
				map.put("birthdate", u.getAddress());
				testService.insertV(map);
			}
		}
		catch(Exception e){
			e.printStackTrace();
		}
	}
	
	/**
	 * 导出
	 * @param request
	 * @param response
	 */
	@RequestMapping("/test3.do")
	public void testExcelImpot(HttpServletRequest request,
			HttpServletResponse response) {
		ExcelExport export = new ExcelExport();
		String[] titles = { "学号", "姓名", "年龄", "生日" };
		List<LinkedHashMap<String, Object>> list = new ArrayList<LinkedHashMap<String, Object>>();
		try {
			export.createWorkBook();
			export.createSheet("testImp");
			export.createTitle(titles);
			List<User> l = testService.testQueryList();
			for (User u : l) {
				LinkedHashMap<String, Object> map = new LinkedHashMap<String, Object>();
				map.put("id", u.getId());
				map.put("name", u.getName());
				map.put("age", u.getAge());
				map.put("birthdate", DateUtil.Date2Str(u.getAddress()));
				list.add(map);
			}
			export.doExcelExport(list, true);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
