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

import edu.java.shop.UI.NoAnyCopiesException;
import edu.java.shop.UI.NothingSelectedException;
import edu.java.shop.UI.ThreadRefresh;

public class CustomerUI extends UI
{
	//Классы исключений
	//Пароль и повторённый пароль не совпадают
	static private class NotEqualPasswordsException extends Exception
	{
		public NotEqualPasswordsException()
		{
			JOptionPane.showMessageDialog(UIFrame, "Пароли не совпадают.");
		};
	};
	
	//Пользователь с таким именем уже существует в системе
	static private class UserAlreadyExistsException extends Exception
	{
		public UserAlreadyExistsException() 
		{
			JOptionPane.showMessageDialog(UIFrame, "Пользователь с таким именем уже существует.");
		};
	};	
	
	//Метод определения, совпадают ли введённые пароли
	private static void ArePasswordsEqual(String Str1, String Str2) throws NotEqualPasswordsException
	{
		if(!Str1.equals(Str2))
			throw new NotEqualPasswordsException();
	};
	
	//Метод определения, существует ли пользователь в базе данных
	private void DoesUserAlreadyExist(Statement Stmt, ResultSet RS) throws UserAlreadyExistsException, SQLException
	{
		if(RS.next())
		{
			Stmt.close();
			RS.close();
			Connection.close();
			throw new UserAlreadyExistsException();
		};
	};
	
	//ID постоянного покупателя
	private int ID = 2;

	//Ктор
	public CustomerUI(int UserID)
	{
		ID = UserID;
	};
	
	
	@Override
	void Show() {
		// TODO Auto-generated method stub
		//Создаём компоненты графического интерфейса
		UIFrame = new JFrame("Система \"Магазин компьютерных игр\"::Постоянный покупатель");
		UIFrame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		UIFrame.setSize(1200, 800);
		UIFrame.setLocation(300, 100);	
		UIFrame.setLayout(null);
		UIFrame.setUndecorated(true);
				
		UIHelloLabel = new JLabel("Система \"Магазин компьютерных игр\"::Постоянный покупатель (ID = " + Integer.toString(ID) + ")");
		UIHelloLabel.setForeground(Color.BLACK);
		UIHelloLabel.setFont(new Font("Verdana", Font.PLAIN, 14));
		UIHelloLabel.setHorizontalAlignment(JLabel.LEFT);
		UIHelloLabel.setVerticalAlignment(JLabel.TOP);
		UIHelloLabel.setBounds(250, 5, 700, 30);
		UIFrame.add(UIHelloLabel);
				
		UIClose = new JButton("Выйти");
		UIClose.setFont(new Font("Verdana", Font.PLAIN, 14));
		UIClose.setBounds(1100, 35, 90, 40);			
		UIFrame.add(UIClose);
				
		UIBuy = new JButton("Купить");
		UIBuy.setFont(new Font("Verdana", Font.PLAIN, 14));
		UIBuy.setBounds(10, 35, 90, 40);			
		UIFrame.add(UIBuy);
		
		UIEditUser = new JButton("Изменить данные пользователя");
		UIEditUser.setFont(new Font("Verdana", Font.PLAIN, 14));
		UIEditUser.setBounds(110, 35, 300, 40);
		UIFrame.add(UIEditUser);
		
		UIDeleteUser = new JButton("Удалить пользователя из системы");
		UIDeleteUser.setFont(new Font("Verdana", Font.PLAIN, 14));
		UIDeleteUser.setBounds(420, 35, 300, 40);
		UIFrame.add(UIDeleteUser);		
				
		String[] Columns = {"Название игры", "Разрабочик", "Год", "Жанр", "Цена", "50% скидка*", "Кол-во экземпляров"};
		String[][] Data = {};
		UIModel = new DefaultTableModel(Data, Columns);		
		UIGames = new JTable(UIModel);
		UIGames.setFont(new Font("Verdana", Font.PLAIN, 14));
		UIGames.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
				
		UIScroll = new JScrollPane(UIGames);		
		UIScroll.setFont(new Font("Verdana", Font.PLAIN, 14));
		UIScroll.setBounds(10, 85, 1180, 685);		
		UIFrame.add(UIScroll);
		
		
		
		
		UIClose.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				// TODO Auto-generated method stub	
				try {
					Connection.close();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				UIFrame.dispose();
			}			
		});
		
		UIBuy.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				try {
					IsAnyGameSelected();
					
					//Подключаемся к базе данных
					Statement Stmt = ConnectToDataBase("localhost/3050:E:/Eclipse/Course/DataBases/GAMES.FDB");
					
					//SQL-запрос
					String SQLRequest = "SELECT * FROM GAMES WHERE NAME = \'" + UIModel.getValueAt(UIGames.getSelectedRow(), 0).toString() + "\' AND DEVELOPER = \'" + UIModel.getValueAt(UIGames.getSelectedRow(), 1).toString() + "\';";
					ResultSet RS = Stmt.executeQuery(SQLRequest);
					
					String Name = null;
					String Developer = null;
					int Year = 0;
					String Genre = null;
					int Price = 0;
					boolean Discount = false;
					int NumberOfCopies = 0;
					
					while(RS.next())
					{
						Name = RS.getString(1);
						Developer = RS.getString(2);
						Year = RS.getInt(3);
						Genre = RS.getString(4);
						Price = RS.getInt(5);
						Discount = RS.getBoolean(6);
						NumberOfCopies = RS.getInt(7);
					};
					
					Name = Name.trim();
					Developer = Developer.trim();
					Genre = Genre.trim();					
					
					//Проверяем число копий после покупки
					DoGameCopiesExist(NumberOfCopies);
					
					//Спрашиваем пользователя, хочет ли он купить игру по цене (без скидки)
					//Избавиться от этого эффекта
					if(UIGames.getSelectedRow() != -1)
					{
						if(Discount)
						{
							if(JOptionPane.showConfirmDialog(UIFrame, "Действительно ли Вы хотите приобрести игру \'" + Name + "\' разработчика \'" + Developer + "\' по цене " + Integer.toString(Price/2) + " руб.?", "Подтвердите действие", JOptionPane.YES_NO_OPTION) == 0)
							{
								//Записываем изменения в базу данных
								NumberOfCopies -= 1;
								SQLRequest = "UPDATE GAMES SET NUMBER_OF_COPIES = " + Integer.toString(NumberOfCopies) + " WHERE NAME = \'" + UIModel.getValueAt(UIGames.getSelectedRow(), 0).toString() + "\' AND DEVELOPER = \'" + UIModel.getValueAt(UIGames.getSelectedRow(), 1).toString() + "\';";
								Stmt.executeUpdate(SQLRequest);
							};
						}
						else
						{
							if(JOptionPane.showConfirmDialog(UIFrame, "Действительно ли Вы хотите приобрести игру \'" + Name + "\' разработчика \'" + Developer + "\' по цене " + Integer.toString(Price) + " руб.?", "Подтвердите действие", JOptionPane.YES_NO_OPTION) == 0)
							{
								//Записываем изменения в базу данных
								NumberOfCopies -= 1;
								SQLRequest = "UPDATE GAMES SET NUMBER_OF_COPIES = " + Integer.toString(NumberOfCopies) + " WHERE NAME = \'" + UIModel.getValueAt(UIGames.getSelectedRow(), 0).toString() + "\' AND DEVELOPER = \'" + UIModel.getValueAt(UIGames.getSelectedRow(), 1).toString() + "\';";
								Stmt.executeUpdate(SQLRequest);
							};
						};	
					}
					else
						JOptionPane.showMessageDialog(UIFrame, "Выберите элемент для покупки.");
					
					//Отключаемся от базы данных
					DisconnectFromDataBase(Stmt, RS);					
					
					//Обновляем вывод базы данных товаров
					ReadGameDataBase();
					
					//Заносим сведения в отчёт
					Report Rep = new Report();
					if(Discount)
						Rep.AddRecord("Игра \'" + Name + "\' разработчика \'" + Developer + "\' была куплена постоянным покупателем по цене " + Integer.toString(Price/2) + ".", false);
					else
						Rep.AddRecord("Игра \'" + Name + "\' разработчика \'" + Developer + "\' была куплена постоянным покупателем по цене " + Integer.toString(Price) + ".", false);
				} catch (NothingSelectedException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				} catch (SQLException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				} catch (NoAnyCopiesException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
			
		});
		
		UIDeleteUser.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				// TODO Auto-generated method stub
				if(JOptionPane.showConfirmDialog(UIFrame, "Действительно ли Вы хотите удалить пользователя с ID = " + Integer.toString(ID) + "?", "Подтвердите действие", JOptionPane.YES_NO_OPTION) == 0)
				{
					try 
					{
						//Подключаемся к базе данных пользователей
						Statement Stmt = ConnectToDataBase("localhost/3050:E:/Eclipse/Course/DataBases/USERS.FDB");
						
						//SQL-запрос
						String SQLRequest = "DELETE FROM USERS WHERE ID = " + Integer.toString(ID) + ";";	
						Stmt.executeUpdate(SQLRequest);
						
						//Отключаемся от базы данных
						Stmt.close();
					} catch (SQLException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					
					Report Rep = new Report();
					Rep.AddRecord("Пользователь с ID = " + Integer.toString(ID) + " успешно удалён.", true);
					
					//Выводим сообщение об удалении пользователя
					JOptionPane.showMessageDialog(UIFrame, "Пользователь с ID = " + Integer.toString(ID) + " успешно удалён. Будет произведён выход из системы.");					
					
					//Выходим из системы после удаления пользователя					
					UIFrame.dispose();
				};
			}
			
		});
		
		UIEditUser.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				// TODO Auto-generated method stub
				
				//Показываем форму, в которой пользователь вводит новые данные
				String Name = JOptionPane.showInputDialog(UIFrame, "Введите новое имя пользователя:");
				if(Name == null)
				{
					return;
				};
				
				if(Name.length() == 0)
				{
					JOptionPane.showMessageDialog(UIFrame, "Имя пользователя не может быть пустым.");
					return;
				};
				
				//Проверяем, есть ли пользователи с таким же именем
				//Подключаемся к базе данных
				Statement Stmt;
				try {
					Stmt = ConnectToDataBase("localhost/3050:E:/Eclipse/Course/DataBases/USERS.FDB");
					
					//SQL-запрос
					String SQLRequest = "SELECT * FROM USERS WHERE NAME = \'" + Name + "\' AND ID != " + Integer.toString(ID) + ";";
					ResultSet RS = Stmt.executeQuery(SQLRequest);
					
					DoesUserAlreadyExist(Stmt, RS);
					
					//Отключаемся от базы данных
					DisconnectFromDataBase(Stmt, RS);
					
					String Password = JOptionPane.showInputDialog(UIFrame, "Введите пароль:");
					if(Password == null)
					{
						return;
					};
					
					if(Password.length() == 0)
					{
						JOptionPane.showMessageDialog(UIFrame, "Пароль не может быть пустым.");
						return;
					};
					
					String RepeatPassword = JOptionPane.showInputDialog(UIFrame, "Повторите пароль:");
					if(RepeatPassword == null)
					{
						return;
					};
					
					ArePasswordsEqual(Password, RepeatPassword);
					
					//Подключаемся к базе данных
					Statement Stmt1 = ConnectToDataBase("localhost/3050:E:/Eclipse/Course/DataBases/USERS.FDB");
					
					//SQL-запрос
					String SQLRequest1 = "UPDATE USERS SET NAME = \'" + Name + "\', PASSWORD = \'" + Password + "\' WHERE ID = " + Integer.toString(ID) + ";";
					Stmt1.executeUpdate(SQLRequest1);
					
					//Отключаемся от базы данных
					Stmt1.close();
					
					Report Rep = new Report();
					Rep.AddRecord("Пользователь с ID = " + Integer.toString(ID) + " изменил свои данные.", true);
					
					//Выводим сообщение об изменении данных пользователя
					JOptionPane.showMessageDialog(UIFrame, "Данные пользователя успешно изменены.");
					
				} catch (SQLException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				} catch (UserAlreadyExistsException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (NotEqualPasswordsException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			
		});
		
		
		UIFrame.setVisible(true);
		
		//Запускаем поток, обновляющий вывод базы данных
		Refresh = new ThreadRefresh();
		Refresh.start();
	}

}
