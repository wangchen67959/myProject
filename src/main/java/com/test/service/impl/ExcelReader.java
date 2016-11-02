package com.test.service.impl;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFDateUtil;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.cglib.core.ReflectUtils;

import com.test.entity.User;


//返回针对Excel中存储的字段的map结果

/**
 * 操作Excel表格的功能类
 * @param <T>
 */
public class ExcelReader {
	private POIFSFileSystem fs;
	private Workbook wb;
	private Sheet sheet;
	private Row row;

	/**
	 * 读取Excel表格表头的内容
	 * 
	 * @param InputStream
	 * @return String 表头内容的数组
	 */
	public String[] readExcelTitle(InputStream is) {
		try {
			fs = new POIFSFileSystem(is);
			wb = new HSSFWorkbook(fs);
		} catch (IOException e) {
			e.printStackTrace();
		}
		sheet = wb.getSheetAt(0);
		row = sheet.getRow(0);
		// 标题总列数
		int colNum = row.getPhysicalNumberOfCells();
		System.out.println("colNum:" + colNum);
		String[] title = new String[colNum];
		for (int i = 0; i < colNum; i++) {
			// title[i] = getStringCellValue(row.getCell((short) i));
			title[i] = getCellFormatValue(row.getCell((short) i));
		}
		return title;
	}

	/**
	 * 读取Excel数据内容
	 * @param <T>
	 * 
	 * @param InputStream
	 * @return Map 包含单元格数据内容的Map对象
	 * @throws Exception
	 */
	public <T> List<String[]> readExcelContent(InputStream is, String Exceldit, Class<T> clazz)
			throws Exception {
		Map<Integer, String> content = new HashMap<Integer, String>();
		String str = "";
		
//		fs = new POIFSFileSystem(is);
		if ("xls".equals(Exceldit)) {
			wb = new HSSFWorkbook(is);
		} else if ("xlsx".equals(Exceldit)) {
			wb = new XSSFWorkbook(is);
		} else {
			throw new Exception("当前文件不是excel文件");
		}
		
		// 创建返回对象，把每行中的值作为一个数组，所有行作为一个集合返回
		List<String[]> list = new ArrayList<String[]>();
		if (wb != null) {
		      Sheet sheet1 = wb.getSheetAt(0);    
//		        for (Row row : sheet1) {    
//		            for (Cell cell : row) {
//		                System.out.print(getCellValue(cell)+"  ");    
//		            }    
//		            System.out.println();    
//		        }
//		  	for (int sheetNum = 0; sheetNum < wb.getNumberOfSheets(); sheetNum++) {
				// 获得当前sheet工作表
		      Sheet sheet = wb.getSheetAt(0);
		      Row _title = sheet.getRow(0);
		      int colNum = _title.getPhysicalNumberOfCells();
				System.out.println("colNum:" + colNum);
				String[] title = new String[colNum];
				for (int i = 0; i < colNum; i++) {
					// title[i] = getStringCellValue(row.getCell((short) i));
					title[i] = _title.getCell(i).getStringCellValue();
				}
		      
//		
				
//				if (sheet == null) {
//					continue;
//				}
				// 获得当前sheet的开始行
				int firstRowNum = sheet.getFirstRowNum();
				// 获得当前sheet的结束行
				int lastRowNum = sheet.getLastRowNum();
				sheet.getPhysicalNumberOfRows(); // 总行数
				System.out.println(sheet.getPhysicalNumberOfRows()+"fgfghfghfhhhhhhhg");
				int contColumnNum = title.length; //2
				// 循环除了第一行的所有行
				for (int rowNum = firstRowNum + 1; rowNum <= lastRowNum; rowNum++) {
					// 获得当前行
					Row row = sheet.getRow(rowNum);
					if (row == null) {
						continue;
					}
//					// 获得当前行的开始列   每次都要获取一遍数值 1
//					int firstCellNum = row.getFirstCellNum();
//					// 获得当前行的列数
//					int lastCellNum = row.getPhysicalNumberOfCells();
					String[] cells = new String[row.getPhysicalNumberOfCells()];
					// 循环当前行
					T user = clazz.newInstance(); 
//					for (int cellNum = firstCellNum; cellNum < lastCellNum; cellNum++) { // 1
					for(int cellNum =0;cellNum<contColumnNum;cellNum++){ //2 
						Cell cell = row.getCell(cellNum);
						// 反射机制
						cells[cellNum] = getCellValue(cell);
					}
//					ReflectUtils.getMethodInfo(null);
					System.out.println();
					list.add(cells);
				}
//			}
//			wb.close();
		}
		return list;
	}

	public static String getCellValue(Cell cell) {
		String cellValue = "";
		if (cell == null) {
			return cellValue;
		}
		// 把数字当成String来读，避免出现1读成1.0的情况
		if (cell.getCellType() == Cell.CELL_TYPE_NUMERIC) {
			cell.setCellType(Cell.CELL_TYPE_STRING);
		}
		// 判断数据的类型
		switch (cell.getCellType()) {
		case Cell.CELL_TYPE_NUMERIC: // 数字
			cellValue = String.valueOf(cell.getNumericCellValue());
			break;
		case Cell.CELL_TYPE_STRING: // 字符串
			cellValue = String.valueOf(cell.getStringCellValue());
			break;
		case Cell.CELL_TYPE_BOOLEAN: // Boolean
			cellValue = String.valueOf(cell.getBooleanCellValue());
			break;
		case Cell.CELL_TYPE_FORMULA: // 公式
			{
				if(DateUtil.isCellDateFormatted(cell)){
					// 方法1：这样子的data格式是带时分秒的：2011-10-12 0:00:00
					// cellvalue = cell.getDateCellValue().toLocaleString();

					// 方法2：这样子的data格式是不带带时分秒的：2011-10-12
					Date date = cell.getDateCellValue();
					SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
					cellValue = sdf.format(date);
				}else {
					cellValue = String.valueOf(cell.getCellFormula());
				}
				break;
			}
		case Cell.CELL_TYPE_BLANK: // 空值
			cellValue = "";
			break;
		case Cell.CELL_TYPE_ERROR: // 故障
			cellValue = "非法字符";
			break;
		default:
			cellValue = "";
			break;
		}
		return cellValue;
	}

	/**
	 * 获取单元格数据内容为字符串类型的数据
	 * 
	 * @param cell
	 *            Excel单元格
	 * @return String 单元格数据内容
	 */
	@SuppressWarnings("deprecation")
	private String getStringCellValue(HSSFCell cell) {
		String strCell = "";
		switch (cell.getCellType()) {
		case HSSFCell.CELL_TYPE_STRING:
			strCell = cell.getStringCellValue();
			break;
		case HSSFCell.CELL_TYPE_NUMERIC:
			strCell = String.valueOf(cell.getNumericCellValue());
			break;
		case HSSFCell.CELL_TYPE_BOOLEAN:
			strCell = String.valueOf(cell.getBooleanCellValue());
			break;
		case HSSFCell.CELL_TYPE_BLANK:
			strCell = "";
			break;
		default:
			strCell = "";
			break;
		}
		if (strCell.equals("") || strCell == null) {
			return "";
		}
		if (cell == null) {
			return "";
		}
		return strCell;
	}

	/**
	 * 获取单元格数据内容为日期类型的数据
	 * 
	 * @param cell
	 *            Excel单元格
	 * @return String 单元格数据内容
	 */
	private String getDateCellValue(HSSFCell cell) {
		String result = "";
		try {
			int cellType = cell.getCellType();
			if (cellType == HSSFCell.CELL_TYPE_NUMERIC) {
				Date date = cell.getDateCellValue();
				result = (date.getYear() + 1900) + "-" + (date.getMonth() + 1)
						+ "-" + date.getDate();
			} else if (cellType == HSSFCell.CELL_TYPE_STRING) {
				String date = getStringCellValue(cell);
				result = date.replaceAll("[年月]", "-").replace("日", "").trim();
			} else if (cellType == HSSFCell.CELL_TYPE_BLANK) {
				result = "";
			}
		} catch (Exception e) {
			System.out.println("日期格式不正确!");
			e.printStackTrace();
		}
		return result;
	}

	/**
	 * 根据HSSFCell类型设置数据
	 * 
	 * @param cell
	 * @return
	 */
	private String getCellFormatValue(Cell cell) {
		String cellvalue = "";
		if (cell != null) {
			// 判断当前Cell的Type
			switch (cell.getCellType()) {
			// 如果当前Cell的Type为NUMERIC
			case HSSFCell.CELL_TYPE_NUMERIC:
			case HSSFCell.CELL_TYPE_FORMULA: {
				// 判断当前的cell是否为Date
				if (HSSFDateUtil.isCellDateFormatted(cell)) {
					// 如果是Date类型则，转化为Data格式
					// 方法1：这样子的data格式是带时分秒的：2011-10-12 0:00:00
					// cellvalue = cell.getDateCellValue().toLocaleString();

					// 方法2：这样子的data格式是不带带时分秒的：2011-10-12
					Date date = cell.getDateCellValue();
					SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
					cellvalue = sdf.format(date);
				}
				// 如果是纯数字
				else {
					// 取得当前Cell的数值
					cellvalue = String.valueOf(cell.getNumericCellValue());
				}
				break;
			}
			// 如果当前Cell的Type为STRIN
			case HSSFCell.CELL_TYPE_STRING:
				// 取得当前的Cell字符串
				cellvalue = cell.getRichStringCellValue().getString();
				break;
			// 默认的Cell值
			default:
				cellvalue = " ";
			}
		} else {
			cellvalue = "";
		}
		return cellvalue;

	}

	public static void main(String[] args) {
		try {
			ExcelReader excelReader = new ExcelReader();

			// 对读取Excel表格标题测试
//			InputStream is = new FileInputStream("d:\\test2.xlsx");
//			String[] title = excelReader.readExcelTitle(is);
//			System.out.println("获得Excel表格的标题:");
//			for (String s : title) {
//				System.out.print(s + " ");
//			}

			// 对读取Excel表格内容测试
			InputStream is2 = new FileInputStream("d:\\test2.xlsx");
			List<String[]> map = excelReader.readExcelContent(is2, "xlsx", User.class);
			System.out.println("获得Excel表格的内容:");
			for (int i = 0; i <map.size(); i++) {
				
				for(int j=0;j<map.get(i).length;j++) {
					System.out.println(map.get(i)[j]);
				}
				
			}

		} catch (FileNotFoundException e) {
			System.out.println("未找到指定路径的文件!");
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
