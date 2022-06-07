
package com.qa.utilities;

import java.util.ArrayList;

public class TestUtility {
	
	static Xls_Reader reader;
	
	
	public static ArrayList<Object[]> getDataFromExcel(){
		
		
		ArrayList<Object[]> myData = new ArrayList<Object[]>();
		
		try {
			reader = new Xls_Reader ("C:\\Users\\prani\\Projects\\DataDriven\\ExcelSheets\\Data.xlsx");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		for(int rowNum = 2 ; rowNum <= reader.getRowCount("TestData"); rowNum++)
		{
			
			String firstname = reader.getCellData("TestData", "Firstname", rowNum);
			String lastname = reader.getCellData("TestData", "Lastname", rowNum);
			String email = reader.getCellData("TestData", "Email", rowNum);
			String number = reader.getCellData("TestData", "Number", rowNum);
			String address= reader.getCellData("TestData", "Address", rowNum);
			String dob = reader.getCellData("TestData", "DOB", rowNum);
			String hobbies = reader.getCellData("TestData", "Hobbies", rowNum);

			
			Object ob[] = {firstname,lastname,email,number,address,dob,hobbies};
			myData.add(ob);
		}
		
		return myData;
	
		
	}
}


