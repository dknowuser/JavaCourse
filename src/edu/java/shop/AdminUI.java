package edu.java.shop;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.table.DefaultTableModel;

import edu.java.shop.UI.ThreadRefresh;


//Один и тот же Connection для пользователей и игр!!!
public class AdminUI extends UI
{
	//Классы исключений
	//Не выбран пользователь системы
	static private class UserNotSelectedException extends Exception
	{
		public UserNotSelectedException()
		{
			JOptionPane.showMessageDialog(UIFrame, "Выберите пользователя.");
		};
	};
	
	//Не введён параметр
	static private class NoParameterException extends Exception
	{
		public NoParameterException()
		{
			JOptionPane.showMessageDialog(UIFrame, "Не был введён параметр.");
		};
	};
	
	//Метод определения, выбран ли пользователь
	private void IsAnyUserSelected() throws UserNotSelectedException
	{
		if(UIUsers.getSelectedRow() == -1)
			throw new UserNotSelectedException();
	};
	
	//Метод определения, ввели ли мы что-нибудь в окно ввода параметра
	private void NoParameter(String str) throws  NoParameterException
	{
		if(str.length() == 0)
		{
			throw new NoParameterException();
		};
	};
	
	
	//Метод подключения к базе данных
	static Statement ConnectToUsersDataBase(String Path) throws SQLException
	{
		org.firebirdsql.pool.FBSimpleDataSource dataSource = new org.firebirdsql.pool.FBSimpleDataSource();
			
		dataSource.setDatabase(Path);
		dataSource.setDescription("База данных.");
		dataSource.setType("TYPE4");
		dataSource.setEncoding("win1251");
		dataSource.setLoginTimeout(1000);
					
		//Устанавливаем соединение с базой данных
		UsersDataBaseConnection = null;
		UsersDataBaseConnection = dataSource.getConnection("SYSDBA", "masterkey");
					
		if(UsersDataBaseConnection == null)
		{
			System.err.println("Ошибка при подключении к базе данных.");
		};
					
		//Создаём класс, с помощью которого будут выполняться SQL-запросы		
		return UsersDataBaseConnection.createStatement();
	};
		
	//Метод отключения от базы данных
	static void DisconnectFromUsersDataBase(Statement Stmt, ResultSet RS) throws SQLException
	{
		RS.close();
		Stmt.close();
		UsersDataBaseConnection.close();
	};
	
	
	//Класс потока, обновляющего базу данных пользователей
	static protected class ThreadUsers extends Thread
	{
		//Ктор
		public ThreadUsers() {};
		
		/** *Основной метод потока */
		public void run()
		{
			while(true)
			{
				ReadUsersDataBase();
				
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
	
	//Метод чтения базы данных пользователей
	static void ReadUsersDataBase()
	{
		// TODO Auto-generated method stub
		//Запоминаем, какая ячейка выделена
		int SelectedRow = UIUsers.getSelectedRow();
		
		//Очищаем таблицу
		int rows = UIModelUsers.getRowCount();
		for(int i = 0; i < rows; i++)
			UIModelUsers.removeRow(0);
				
		try
		{
			Statement Stmt = ConnectToUsersDataBase("localhost/3050:E:/Eclipse/Course/DataBases/USERS.FDB");
					
			//Читаем базу данных
			//SQL-запрос
			String SQLRequest = "SELECT * FROM USERS;";
			ResultSet RS = Stmt.executeQuery(SQLRequest);
					
			//Заполняем таблицу
			while(RS.next())
			{
				int ID = RS.getInt(1);
				String Name = RS.getString(2);
				String Password = RS.getString(3);
				
				Name = Name.trim();
				Password = Password.trim();
				
				UIModelUsers.addRow(new String[] {Integer.toString(ID), Name, Password});
			};
				
			DisconnectFromUsersDataBase(Stmt, RS);
			
		}
		catch (SQLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		};
		
		//Выделяем ячейку
		UIUsers.getSelectionModel().setSelectionInterval(SelectedRow, SelectedRow);
	};	
	
	//Дополнительные компоненты графического интерфейса
	protected static JTable UIUsers;
	protected static DefaultTableModel UIModelUsers;
	protected static JScrollPane UIScrollUsers;
	protected static JButton UIReport;
	protected static java.sql.Connection UsersDataBaseConnection;
	
	@Override
	void Show() {
		// TODO Auto-generated method stub
		//Создаём компоненты графического интерфейса
		UIFrame = new JFrame("Система \"Магазин компьютерных игр\"::Администратор");
		UIFrame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		UIFrame.setSize(1785, 800);
		UIFrame.setLocation(60, 100);	
		UIFrame.setLayout(null);
		UIFrame.setUndecorated(true);
		
		UIHelloLabel = new JLabel("Система \"Магазин компьютерных игр\"::Администратор");
		UIHelloLabel.setForeground(Color.BLACK);
		UIHelloLabel.setFont(new Font("Verdana", Font.PLAIN, 14));
		UIHelloLabel.setHorizontalAlignment(JLabel.LEFT);
		UIHelloLabel.setVerticalAlignment(JLabel.TOP);
		UIHelloLabel.setBounds(690, 5, 700, 30);
		UIFrame.add(UIHelloLabel);
		
		UIClose = new JButton("Выйти");
		UIClose.setFont(new Font("Verdana", Font.PLAIN, 14));
		UIClose.setBounds(1685, 35, 90, 40);			
		UIFrame.add(UIClose);
		
		String[] Columns = {"Название игры", "Разрабочик", "Год", "Жанр", "Цена", "50% скидка*", "Кол-во экземпляров"};
		String[][] Data = {};
		UIModel = new DefaultTableModel(Data, Columns);		
		UIGames = new JTable(UIModel);
		UIGames.setFont(new Font("Verdana", Font.PLAIN, 14));
		UIGames.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
				
		UIScroll = new JScrollPane(UIGames);		
		UIScroll.setFont(new Font("Verdana", Font.PLAIN, 14));
		UIScroll.setBounds(10, 135, 1180, 655);		
		UIFrame.add(UIScroll);
		
		String[] ColumnsUsers = {"ID", "Имя", "Пароль"};
		UIModelUsers = new DefaultTableModel(Data, ColumnsUsers);		
		UIUsers = new JTable(UIModelUsers);
		UIUsers.setFont(new Font("Verdana", Font.PLAIN, 14));
		UIUsers.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
				
		UIScrollUsers = new JScrollPane(UIUsers);		
		UIScrollUsers.setFont(new Font("Verdana", Font.PLAIN, 14));
		UIScrollUsers.setBounds(1200, 135, 575, 655);		
		UIFrame.add(UIScrollUsers);
		
		UIEditUser = new JButton("Изменить данные пользователя");
		UIEditUser.setFont(new Font("Verdana", Font.PLAIN, 14));
		UIEditUser.setBounds(1200, 35, 300, 40);
		UIFrame.add(UIEditUser);
		
		UIDeleteUser = new JButton("Удалить пользователя из системы");
		UIDeleteUser.setFont(new Font("Verdana", Font.PLAIN, 14));
		UIDeleteUser.setBounds(1200, 85, 300, 40);
		UIFrame.add(UIDeleteUser);
		
		UIAddGame = new JButton("Добавить игру");
		UIAddGame.setFont(new Font("Verdana", Font.PLAIN, 14));
		UIAddGame.setBounds(10, 35, 150, 40);
		UIFrame.add(UIAddGame);
		
		UIDeleteGame = new JButton("Удалить игру");
		UIDeleteGame.setFont(new Font("Verdana", Font.PLAIN, 14));
		UIDeleteGame.setBounds(10, 85, 150, 40);
		UIFrame.add(UIDeleteGame);
		
		UIEditGame = new JButton("Изменить данные об игре");
		UIEditGame.setFont(new Font("Verdana", Font.PLAIN, 14));
		UIEditGame.setBounds(170, 35, 250, 40);
		UIFrame.add(UIEditGame);
		
		UIReport = new JButton("Показать отчёт");
		UIReport.setFont(new Font("Verdana", Font.PLAIN, 14));
		UIReport.setBounds(1040, 35, 150, 40);
		UIFrame.add(UIReport);
		
		UIClose.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				// TODO Auto-generated method stub
				try 
				{
					Connection.close();
					UsersDataBaseConnection.close();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				UIFrame.dispose();
			}			
		});
		
		UIDeleteUser.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				try 
				{
					IsAnyUserSelected();
					
					//Подключаемся к базе данных
					Statement Stmt = ConnectToDataBase("localhost/3050:E:/Eclipse/Course/DataBases/USERS.FDB");
					
					//SQL-запрос
					String SQLRequest = "DELETE FROM USERS WHERE ID = " + UIModelUsers.getValueAt(UIUsers.getSelectedRow(), 0).toString() + ";";
					Stmt.executeUpdate(SQLRequest);
					
					//Отключаемся от базы данных
					Stmt.close();
					Connection.close();
					
					//Обновляем вывод базы данных пользователей
					ReadUsersDataBase();
					
				} catch (UserNotSelectedException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				} catch (SQLException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
			
		});
		
		UIEditUser.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				try 
				{
					IsAnyUserSelected();
					
					String ID = UIModelUsers.getValueAt(UIUsers.getSelectedRow(), 0).toString();
					
					//Показываем форму, в которой администратор вводит новые данные
					String Name = JOptionPane.showInputDialog(UIFrame, "Введите новое имя пользователя:");
					if(Name == null)
					{
						return;
					};
					
					NoParameter(Name);
					
					String Password = JOptionPane.showInputDialog(UIFrame, "Введите пароль:");
					if(Password == null)
					{
						return;
					};
					
					NoParameter(Password);
					
					//Подключаемся к базе данных
					Statement Stmt = ConnectToDataBase("localhost/3050:E:/Eclipse/Course/DataBases/USERS.FDB");
					
					//SQL-запрос
					String SQLRequest = "UPDATE USERS SET NAME = \'" + Name + "\', PASSWORD = \'" + Password + "\' WHERE ID = " + ID + ";";
					Stmt.executeUpdate(SQLRequest);
					
					//Отключаемся от базы данных
					Stmt.close();
					Connection.close();
					
					//Обновляем вывод базы данных пользователей
					ReadUsersDataBase();
					
					
				} catch (UserNotSelectedException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				} catch (SQLException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				} catch (NoParameterException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
			
		});
		
		UIDeleteGame.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				// TODO Auto-generated method stub
				try 
				{
					IsAnyGameSelected();
					
					//Подключаемся к базе данных
					Statement Stmt = ConnectToDataBase("localhost/3050:E:/Eclipse/Course/DataBases/GAMES.FDB");
					
					//SQL-запрос
					String SQLRequest = "DELETE FROM GAMES WHERE NAME = \'" + UIModel.getValueAt(UIGames.getSelectedRow(), 0).toString() + "\' AND DEVELOPER = \'" + UIModel.getValueAt(UIGames.getSelectedRow(), 1).toString() + "\';";
					Stmt.executeUpdate(SQLRequest);
					
					//Отключаемся от базы данных
					Stmt.close();
					Connection.close();
					
					//Обновляем вывод базы данных игр
					ReadGameDataBase();
					
				} catch (NothingSelectedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		
		});
		
		UIEditGame.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				try 
				{
					IsAnyGameSelected();
					
					//Можем менять цену, скидку и количество
					String Name = UIModel.getValueAt(UIGames.getSelectedRow(), 0).toString();
					String Developer = UIModel.getValueAt(UIGames.getSelectedRow(), 1).toString();
					
					Name = Name.trim();
					Developer = Developer.trim();
					
					//Показываем форму, в которой администратор вводит новую цену
					String Price = JOptionPane.showInputDialog(UIFrame, "Введите новую цену:");
					if(Price == null)
					{
						return;
					};
					
					NoParameter(Price);
					
					int PriceInt = Integer.parseInt(Price);
					
					//Показываем форму, в которой администратор устанавливает скидку
					boolean Discount = false;
					if(JOptionPane.showConfirmDialog(UIFrame, "Хотите ли Вы установить скидку на выбранную игру?", "Подтвердите действие", JOptionPane.YES_NO_OPTION) == 0)
						Discount = true;
					
					//Показываем форму, в которой администратор вводит новую цену
					String Count = JOptionPane.showInputDialog(UIFrame, "Введите новое количество экземпляров:");
					if(Count == null)
					{
						return;
					};
					
					NoParameter(Count);
					
					int NumberOfCopies = Integer.parseInt(Count);
					
					//Подключаемся к базе данных
					Statement Stmt = ConnectToDataBase("localhost/3050:E:/Eclipse/Course/DataBases/GAMES.FDB");
					
					//SQL-запрос
					String SQLRequest = "";
					if(Discount)
						SQLRequest = "UPDATE GAMES SET PRICE = \'" + Integer.toString(PriceInt) + "\', DISCOUNT = \'TRUE\', NUMBER_OF_COPIES = " + Integer.toString(NumberOfCopies) + " WHERE NAME = \'" + Name + "\' AND DEVELOPER = \'" + Developer + "\';";
					else
						SQLRequest = "UPDATE GAMES SET PRICE = \'" + Integer.toString(PriceInt) + "\', DISCOUNT = \'FALSE\', NUMBER_OF_COPIES = " + Integer.toString(NumberOfCopies) + " WHERE NAME = \'" + Name + "\' AND DEVELOPER = \'" + Developer + "\';";
					
					Stmt.executeUpdate(SQLRequest);
					
					//Отключаемся от базы данных
					Stmt.close();
					Connection.close();
					
					//Обновляем вывод базы данных игр
					ReadGameDataBase();
					
					
				} catch (NothingSelectedException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				} catch (SQLException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				} catch (NoParameterException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
			
		});
		
		UIAddGame.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				// TODO Auto-generated method stub
				try 
				{					
					String Name = JOptionPane.showInputDialog(UIFrame, "Введите название игры:");
					if(Name == null)
					{
						return;
					};
					
					NoParameter(Name);
					Name = Name.trim();
					
					String Developer = JOptionPane.showInputDialog(UIFrame, "Введите разработчика игры:");
					if(Developer == null)
					{
						return;
					};
					
					NoParameter(Developer);
					Developer = Developer.trim();
					
					String SYear = JOptionPane.showInputDialog(UIFrame, "Введите год релиза игры:");
					if(SYear == null)
					{
						return;
					};
					
					NoParameter(SYear);
					SYear = SYear.trim();
					int Year = Integer.parseInt(SYear);
					
					String Genre = JOptionPane.showInputDialog(UIFrame, "Введите жанр игры:");
					if(Genre == null)
					{
						return;
					};
					
					NoParameter(Genre);
					Genre = Genre.trim();
					
					//Показываем форму, в которой администратор вводит новую цену
					String Price = JOptionPane.showInputDialog(UIFrame, "Введите новую цену:");
					if(Price == null)
					{
						return;
					};
					
					NoParameter(Price);
					Price = Price.trim();
					
					int PriceInt = Integer.parseInt(Price);
					
					//Показываем форму, в которой администратор устанавливает скидку
					boolean Discount = false;
					if(JOptionPane.showConfirmDialog(UIFrame, "Хотите ли Вы установить скидку на выбранную игру?", "Подтвердите действие", JOptionPane.YES_NO_OPTION) == 0)
						Discount = true;
					
					//Показываем форму, в которой администратор вводит новую цену
					String Count = JOptionPane.showInputDialog(UIFrame, "Введите новое количество экземпляров:");
					if(Count == null)
					{
						return;
					};
					
					NoParameter(Count);
					Count = Count.trim();
					
					int NumberOfCopies = Integer.parseInt(Count);
					
					//Подключаемся к базе данных
					Statement Stmt = ConnectToDataBase("localhost/3050:E:/Eclipse/Course/DataBases/GAMES.FDB");
					
					//SQL-запрос
					String SQLRequest = "";
					
					if(Discount)
						SQLRequest = "INSERT INTO GAMES VALUES (\'" + Name + "\', \'" + Developer + "\', " + SYear + ", \'" + Genre + "\', " + Price + ", TRUE, " + Count + ");";
					else
						SQLRequest = "INSERT INTO GAMES VALUES (\'" + Name + "\', \'" + Developer + "\', " + SYear + ", \'" + Genre + "\', " + Price + ", FALSE, " + Count + ");";
					
					Stmt.executeUpdate(SQLRequest);
					
					//Отключаемся от базы данных
					Stmt.close();
					Connection.close();
					
					//Обновляем вывод базы данных игр
					ReadGameDataBase();
					
					
				} catch (NoParameterException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			
		});
		
		UIReport.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				// TODO Auto-generated method stub
				ReportUI Rep = new ReportUI();
				Rep.Show();
			}
			
		});
		
		
		UIFrame.setVisible(true);
		
		//Запускаем поток, обновляющий вывод базы данных товаров
		Refresh = new ThreadRefresh();
		Refresh.start();
		
		//Запускаем поток, обновляющий вывод базы данных пользователей
		ThreadUsers RefreshUsers = new ThreadUsers();
		RefreshUsers.start();
	}

}
