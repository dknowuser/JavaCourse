package edu.java.shop;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.Date;

//Класс для работы с отчётом
public final class Report 
{
	//Путь к отчёту
	private static String Path = "e:/Eclipse/Course/Report.txt";
	
	//Метод добавления информации в отчёт
	public void AddRecord(String Line, boolean UserAdded)
	{
		try
		{			
			//Записываем время создания записи дд::мм::гг
			Date ReportDate = new Date();
			SimpleDateFormat FormatForReport = new SimpleDateFormat("dd::MM::");
			
			FileWriter Writer = new FileWriter(Path, true);
			
			Writer.write(FormatForReport.format(ReportDate));
			
			if(UserAdded)
				Writer.write("UserAdded - ");
			else
				Writer.write("GameSold - ");
			
			Writer.write(Line);
			Writer.append('\r');
			Writer.append('\n');			
			Writer.flush();	
			Writer.close();
			
		} catch(IOException e) {
			e.printStackTrace();
		}
	};
	
	//Метод составления отчёта по заданному месяцу
	public void GenerateReport(int Month)
	{
		
	};
}
