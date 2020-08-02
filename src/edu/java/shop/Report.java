package edu.java.shop;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.Date;

//����� ��� ������ � �������
public final class Report 
{
	//���� � ������
	private static String Path = "e:/Eclipse/Course/Report.txt";
	
	//����� ���������� ���������� � �����
	public void AddRecord(String Line, boolean UserAdded)
	{
		try
		{			
			//���������� ����� �������� ������ ��::��::��
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
	
	//����� ����������� ������ �� ��������� ������
	public void GenerateReport(int Month)
	{
		
	};
}
