package com.test.excel;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;

public class ExcelExport {

	private static HSSFWorkbook wb;
	private static HSSFSheet sheet;

	/**
	 * 创建工作簿
	 */
	public void createWorkBook() {
		wb = new HSSFWorkbook();
	}

	/**
	 * 创建工作区
	 * 
	 * @param sheetName
	 * @throws Exception 
	 */
	public HSSFSheet createSheet(String sheetName) throws Exception {
		if(null == wb) {
			throw new Exception("wb is null");
		}
		sheet = wb.createSheet(sheetName);
		sheet.setDefaultColumnWidth(12);
		sheet.setGridsPrinted(true);
		sheet.setDisplayGridlines(true);
		return sheet;
	}

	/**
	 * 创建单元格样式
	 * 
	 * @param backgroundColor 背景颜色
	 * @param foregroundColor
	 * @param halign 对齐方式
	 * @param font 字体样式Font
	 * @return
	 * @throws Exception 
	 */
	public static HSSFCellStyle createCellStyle(int backgroundColor, int foregroundColor,
			int halign, Font font) throws Exception {
		if(null == wb) {
			throw new Exception("wb is null");
		}
		HSSFCellStyle cs = wb.createCellStyle();
		cs.setAlignment((short) halign);
		cs.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
		cs.setFillBackgroundColor((short) backgroundColor);
		cs.setFillForegroundColor((short) foregroundColor);
		cs.setFillPattern(CellStyle.SOLID_FOREGROUND);
		cs.setFont(font);
		return cs;
	}
	
	public static CellStyle createCellStyle(Font font) throws Exception {
		if(null == wb) {
			throw new Exception("wb is null");
		}
		CellStyle cs = wb.createCellStyle();
		cs.setBorderBottom(HSSFCellStyle.BORDER_THIN); // 下边框  
		cs.setBorderLeft(HSSFCellStyle.BORDER_THIN);// 左边框  
		cs.setBorderTop(HSSFCellStyle.BORDER_THIN);// 上边框  
		cs.setBorderRight(HSSFCellStyle.BORDER_THIN);// 右边框  
		cs.setFont(font);
		return cs;
	}

	/**
	 * 功能：创建字体
	 * 
	 * @param wb
	 * @param boldweight short
	 * @param color short
	 * @return Font
	 * @throws Exception 
	 */
	public static Font createFont(short boldweight, short color, int size) throws Exception {
		if(null == wb) {
			throw new Exception("wb is null");
		}
		Font font = wb.createFont();
		font.setBoldweight(boldweight);
		font.setColor(color);
		font.setFontHeightInPoints((short) size);
		return font;
	}

	/**
	 * 功能：创建CELL
	 * 
	 * @param row  HSSFRow
	 * @param cellNum int
	 * @param style HSSFStyle
	 * @return HSSFCell
	 */
	public static HSSFCell createCell(HSSFRow row, int cellNum, CellStyle style) {
		HSSFCell cell = row.createCell(cellNum);
		cell.setCellStyle(style);
		return cell;
	}

	/**
	 * 功能：创建HSSFRow
	 * 
	 * @param sheet  HSSFSheet
	 * @param rowNum int
	 * @param height int
	 * @return HSSFRow
	 */
	public static HSSFRow createRow(int rowNum, int height) {
		HSSFRow row = sheet.createRow(rowNum);
		row.setHeight((short) height);
		return row;
	}

	/**
	 * 创建标题，存于第一行
	 * 
	 * @param titles 头部数组
	 * @throws Exception
	 */
	public void createTitle(String[] titles) throws Exception {
		if (titles.length <= 0) {
			throw new Exception("标题数组不能为空");
		}
		HSSFRow row = sheet.createRow(0);
		for (int i = 0; i < titles.length; i++) {
			HSSFCell cell = row.createCell(i);
			sheet.setColumnWidth(i+1, 20 * 256);
			cell.setCellValue(titles[i]);
		}
	}

	/**
	 * 执行导出
	 * @param list 数据集合
	 * @param isTitle 是否存在头部
	 * @throws FileNotFoundException
	 */
	public void doExcelExport(List<LinkedHashMap<String, Object>> list, Boolean isTitle)
			throws Exception {
		int rowNum = 0;
		// 根据是否有头部 设置内容行开始位置
		if (null != isTitle && isTitle) {
			rowNum = 1;
		}
		// 创建一个字体
		for (Map<String, Object> temp : list) {
			HSSFRow titleRow1 = sheet.createRow(rowNum);
			Set<String> set = temp.keySet();
			Iterator<String> it = set.iterator();
			int indexOfColumn = 0;
			while (it.hasNext()) {
				String key = it.next();
				String value = "";
				if (null != temp.get(key)) {
					value = temp.get(key).toString();
				}
				HSSFCell cell = titleRow1.createCell(indexOfColumn);
				cell.setCellValue(value);
				indexOfColumn++;
			}
			rowNum++;
		}
		try {
			FileOutputStream fos = new FileOutputStream(new File("D:\\"+new Random().nextInt(100) + ".xls"));
			wb.write(fos);
			fos.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	
	/*******************************以下内容可删除********************************************/
	
	public static void main(String[] args) throws Exception {
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("id", "1");
		map.put("name", "王朝");
		map.put("age", "2");
		map.put("birthdate", "2015-12-02 12:32:42");
		list.add(map);
		ExcelExport export = new ExcelExport();
		try {
			export.crateTempFile(list);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 将数据导出到excel中
	 * 
	 * @param list
	 *            将要被导入到excel中的数据
	 * @throws IOException
	 */
	public void crateTempFile(List<Map<String, Object>> list)
			throws Exception {
		// 建立一个excel工作簿
		HSSFWorkbook wb = new HSSFWorkbook();
		// 建立一个sheet
		HSSFSheet sheet = wb.createSheet("test");
		// 建立一个单元格样式
		Map<String, Object> titleMap = list.get(0);
		Set<String> keySet = titleMap.keySet();
		Iterator<String> it = keySet.iterator();
		// sheet中行索引值
		int indexOfRow = 0;
		// 设置sheet第二行
		HSSFRow dataRow = sheet.createRow(indexOfRow);
		while (it.hasNext()) {
			// 遍历建立第二行单元格格式
			HSSFCell cell1 = dataRow.createCell(indexOfRow);
			String key1 = it.next();
			// 将数据库表中的字段名改为修改成excel中的需要的标题名称
			key1 = dbColumnNameToExcelColumnName(key1);
			sheet.setColumnWidth(indexOfRow, 20 * 256);
			cell1.setCellValue(key1);
			indexOfRow++;
		}
		// 设置第三行
		indexOfRow = 1;
		for (Map<String, Object> temp : list) {
			HSSFRow titleRow1 = sheet.createRow(indexOfRow);
			Set<String> set = temp.keySet();
			Iterator<String> it1 = set.iterator();
			int indexOfColumn = 0;
			while (it1.hasNext()) {
				String key = it1.next();
				String value = "";
				if (null != temp.get(key)) {
					value = temp.get(key).toString();
				}
				HSSFCell cell = titleRow1.createCell(indexOfColumn);
				cell.setCellValue(value);
				indexOfColumn++;
			}
			indexOfRow++;
		}
		FileOutputStream fos = new FileOutputStream(new File("D:\\"
				+ new Random().nextInt(100) + ".xls"));
		wb.write(fos);
		fos.close();
	}

	public static String dbColumnNameToExcelColumnName(String key) {
		Map<String, String> map = new HashMap<String, String>();
		map.put("id", "学号");
		map.put("name", "姓名");
		map.put("age", "年龄");
		map.put("birthdate", "生日");
		return map.get(key);
	}
}
