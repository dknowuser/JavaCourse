package edu.java.shop;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;

import edu.java.shop.UI.ThreadRefresh;

//Абстрактный класс интерфейса приложения
public abstract class UI {	
	
	//Классы исключений
	//Ничего не выбрано
	static protected class NothingSelectedException extends Exception
	{
		public NothingSelectedException() 
		{
			JOptionPane.showMessageDialog(UIFrame, "Выберите игру.");
		};
	};
	
	//Нет копий игры
	static protected class NoAnyCopiesException extends Exception
	{
		public NoAnyCopiesException()
		{
			JOptionPane.showMessageDialog(UIFrame, "Отсутствуют копии выбранной игры.");
		};
	};
	
	//Метод определения, выбран ли товар
	protected void IsAnyGameSelected() throws NothingSelectedException
	{
		if(UIGames.getSelectedRow() == -1)
			throw new NothingSelectedException();
	};
	
	//Метод определения, есть ли копии выбранной игры
	protected void DoGameCopiesExist(int NumberOfCopies) throws NoAnyCopiesException, SQLException
	{
		if(NumberOfCopies == 0)
		{
			Connection.close();
			throw new NoAnyCopiesException();
		};
	};
	
	//Класс, описывающий поток, обновляющий базу данных
	static protected class ThreadRefresh extends Thread
	{
		//Ктор
		public ThreadRefresh() {};
		
		/** *Основной метод потока */
		public void run()
		{
			while(true)
			{
				ReadGameDataBase();
			
				//Обновляем каждые 15 секунд
				try {
					Thread.sleep(15000);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		};
	};
	
	//Компоненты графического интерфейса
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
	
	//Метод отрисовки окна
	abstract void Show();
	
	//Метод чтения базы данных игр
	static void ReadGameDataBase()
	{
		// TODO Auto-generated method stub
		//Запоминаем, какая ячейка выделена
		int SelectedRow = UIGames.getSelectedRow();
		
		//Очищаем таблицу
		int rows = UIModel.getRowCount();
		for(int i = 0; i < rows; i++)
			UIModel.removeRow(0);
				
		try
		{
			Statement Stmt = ConnectToDataBase("localhost/3050:E:/Eclipse/Course/DataBases/GAMES.FDB");
					
			//Читаем базу данных
			//SQL-запрос
			String SQLRequest = "SELECT * FROM GAMES;";
			ResultSet RS = Stmt.executeQuery(SQLRequest);
					
			//Заполняем таблицу
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
					IsDiscount = "Действует";
				
				UIModel.addRow(new String[] {Name, Developer, Integer.toString(Year), Genre, Integer.toString(Price), IsDiscount, Integer.toString(NumberOfCopies)});
			};
				
			DisconnectFromDataBase(Stmt, RS);
			
		}
		catch (SQLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		};
		
		//Выделяем ячейку
		UIGames.getSelectionModel().setSelectionInterval(SelectedRow, SelectedRow);
	};
	
	//Метод подключения к базе данных
	static Statement ConnectToDataBase(String Path) throws SQLException
	{
		org.firebirdsql.pool.FBSimpleDataSource dataSource = new org.firebirdsql.pool.FBSimpleDataSource();
		
		dataSource.setDatabase(Path);
		dataSource.setDescription("База данных.");
		dataSource.setType("TYPE4");
		dataSource.setEncoding("win1251");
		dataSource.setLoginTimeout(1000);
				
		//Устанавливаем соединение с базой данных
		Connection = null;
		Connection = dataSource.getConnection("SYSDBA", "masterkey");
				
		if(Connection == null)
		{
			System.err.println("Ошибка при подключении к базе данных.");
		};
				
		//Создаём класс, с помощью которого будут выполняться SQL-запросы		
		return Connection.createStatement();
	};
	
	//Метод отключения от базы данных
	static void DisconnectFromDataBase(Statement Stmt, ResultSet RS) throws SQLException
	{
		RS.close();
		Stmt.close();
		Connection.close();
	};
}
