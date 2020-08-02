package edu.java.shop;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;

import edu.java.shop.UI.ThreadRefresh;

//����������� ����� ���������� ����������
public abstract class UI {	
	
	//������ ����������
	//������ �� �������
	static protected class NothingSelectedException extends Exception
	{
		public NothingSelectedException() 
		{
			JOptionPane.showMessageDialog(UIFrame, "�������� ����.");
		};
	};
	
	//��� ����� ����
	static protected class NoAnyCopiesException extends Exception
	{
		public NoAnyCopiesException()
		{
			JOptionPane.showMessageDialog(UIFrame, "����������� ����� ��������� ����.");
		};
	};
	
	//����� �����������, ������ �� �����
	protected void IsAnyGameSelected() throws NothingSelectedException
	{
		if(UIGames.getSelectedRow() == -1)
			throw new NothingSelectedException();
	};
	
	//����� �����������, ���� �� ����� ��������� ����
	protected void DoGameCopiesExist(int NumberOfCopies) throws NoAnyCopiesException, SQLException
	{
		if(NumberOfCopies == 0)
		{
			Connection.close();
			throw new NoAnyCopiesException();
		};
	};
	
	//�����, ����������� �����, ����������� ���� ������
	static protected class ThreadRefresh extends Thread
	{
		//����
		public ThreadRefresh() {};
		
		/** *�������� ����� ������ */
		public void run()
		{
			while(true)
			{
				ReadGameDataBase();
			
				//��������� ������ 15 ������
				try {
					Thread.sleep(15000);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		};
	};
	
	//���������� ������������ ����������
	protected static JFrame UIFrame;
	protected static JButton UIClose;
	protected static JButton UIRegister;
	protected static JButton UIChangeUserData;
	protected static JButton UIEditUser;
	protected static JButton UIDeleteUser;
	protected static JButton UIBuy;
	protected static JButton UIAddGame;
	protected static JButton UIEditGame;
	protected static JButton UIDeleteGame;
	protected static JTable UIGames;
	protected static JLabel UIHelloLabel;
	protected static DefaultTableModel UIModel;
	protected static JScrollPane UIScroll;
	protected static ThreadRefresh Refresh;
	protected static java.sql.Connection Connection;	
	
	//����� ��������� ����
	abstract void Show();
	
	//����� ������ ���� ������ ���
	static void ReadGameDataBase()
	{
		// TODO Auto-generated method stub
		//����������, ����� ������ ��������
		int SelectedRow = UIGames.getSelectedRow();
		
		//������� �������
		int rows = UIModel.getRowCount();
		for(int i = 0; i < rows; i++)
			UIModel.removeRow(0);
				
		try
		{
			Statement Stmt = ConnectToDataBase("localhost/3050:E:/Eclipse/Course/DataBases/GAMES.FDB");
					
			//������ ���� ������
			//SQL-������
			String SQLRequest = "SELECT * FROM GAMES;";
			ResultSet RS = Stmt.executeQuery(SQLRequest);
					
			//��������� �������
			while(RS.next())
			{
				String Name = RS.getString(1);
				String Developer = RS.getString(2);
				int Year = RS.getInt(3);
				String Genre = RS.getString(4);
				int Price = RS.getInt(5);
				boolean Discount = RS.getBoolean(6);
				int NumberOfCopies = RS.getInt(7);
				
				Name = Name.trim();
				Developer = Developer.trim();
				Genre = Genre.trim();
				
				String IsDiscount = "";
				if(Discount)
					IsDiscount = "���������";
				
				UIModel.addRow(new String[] {Name, Developer, Integer.toString(Year), Genre, Integer.toString(Price), IsDiscount, Integer.toString(NumberOfCopies)});
			};
				
			DisconnectFromDataBase(Stmt, RS);
			
		}
		catch (SQLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		};
		
		//�������� ������
		UIGames.getSelectionModel().setSelectionInterval(SelectedRow, SelectedRow);
	};
	
	//����� ����������� � ���� ������
	static Statement ConnectToDataBase(String Path) throws SQLException
	{
		org.firebirdsql.pool.FBSimpleDataSource dataSource = new org.firebirdsql.pool.FBSimpleDataSource();
		
		dataSource.setDatabase(Path);
		dataSource.setDescription("���� ������.");
		dataSource.setType("TYPE4");
		dataSource.setEncoding("win1251");
		dataSource.setLoginTimeout(1000);
				
		//������������� ���������� � ����� ������
		Connection = null;
		Connection = dataSource.getConnection("SYSDBA", "masterkey");
				
		if(Connection == null)
		{
			System.err.println("������ ��� ����������� � ���� ������.");
		};
				
		//������ �����, � ������� �������� ����� ����������� SQL-�������		
		return Connection.createStatement();
	};
	
	//����� ���������� �� ���� ������
	static void DisconnectFromDataBase(Statement Stmt, ResultSet RS) throws SQLException
	{
		RS.close();
		Stmt.close();
		Connection.close();
	};
}
