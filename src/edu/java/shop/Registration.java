package edu.java.shop;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.lang.Math;

import javax.swing.*;

public final class Registration {
	//Классы исключений для текущего класса
	//Какое-либо из полей оказалось пустым
	static private class EmptyFieldException extends Exception
	{
		public EmptyFieldException() 
		{
			super("Все поля должны быть заполнены.");
		};
	};
	
	//Пароль и повторённый пароль не совпадают
	static private class NotEqualPasswordsException extends Exception
	{
		public NotEqualPasswordsException()
		{
			super("Пароли не совпадают.");
		};
	};
	
	//Пользователь с таким именем уже существует в системе
	static private class UserAlreadyExistsException extends Exception
	{
		public UserAlreadyExistsException() 
		{
			super("Пользователь с таким именем уже существует.");
		};
	};	
	
	//Компоненты графического интерфейса
	static JFrame RegistrationFrame;
	static JLabel LUserName;
	static JTextField TUserName;
	static JLabel LPassword;
	static JPasswordField TPassword;
	static JLabel LPasswordRepeat;
	static JPasswordField TPasswordRepeat;
	static JButton Cancel;
	static JButton Register;
	
	//Метод определения, является ли какое-либо поле пустым
	private static void IsAnyFieldEmpty() throws EmptyFieldException
	{
		//Удаляем лишние пробелы в начале и в конце строки
		TUserName.setText(TUserName.getText().trim());
		
		if(TUserName.getText().isEmpty())
			throw new EmptyFieldException();
		if(TPassword.getText().isEmpty())
			throw new EmptyFieldException();
		if(TPasswordRepeat.getText().isEmpty())
			throw new EmptyFieldException();
	};
	
	//Метод определения, совпадают ли введённые пароли
	private static void ArePasswordsEqual() throws NotEqualPasswordsException
	{
		if(!TPassword.getText().equals(TPasswordRepeat.getText()))
			throw new NotEqualPasswordsException();
	};
	
	//Метод определения, существует ли пользователь в базе данных
	private static void DoesUserAlreadyExist(Statement Stmt, java.sql.Connection Connection, String User) throws UserAlreadyExistsException, SQLException
	{
		//SQL-запрос
		String SQLRequest = "SELECT * FROM USERS WHERE NAME = \'" + User + "\';";
		ResultSet RS = Stmt.executeQuery(SQLRequest);		
		
		if(RS.next())
		{
			Stmt.close();
			RS.close();
			Connection.close();
			throw new UserAlreadyExistsException();
		};
	};
	
	
	//Метод создания окна
	public static void Show()
	{
		RegistrationFrame = new JFrame("Регистрация в системе \"Магазин компьютерных игр\"");
		RegistrationFrame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		RegistrationFrame.setSize(600, 155);
		RegistrationFrame.setLocation(1200, 400);	
		RegistrationFrame.setLayout(null);
		RegistrationFrame.setUndecorated(true);
		
		LUserName = new JLabel("Имя пользователя:");
		LUserName.setForeground(Color.BLACK);
		LUserName.setFont(new Font("Verdana", Font.PLAIN, 14));
		LUserName.setHorizontalAlignment(JLabel.LEFT);
		LUserName.setVerticalAlignment(JLabel.TOP);
		LUserName.setBounds(10, 10, 140, 30);
		RegistrationFrame.add(LUserName);
		
		TUserName = new JTextField();
		TUserName.setText(null);
		TUserName.setFont(new Font("Verdana", Font.PLAIN, 14));
		TUserName.setBounds(150, 5, 430, 30);
		TUserName.setEditable(true);
		RegistrationFrame.add(TUserName);
		
		LPassword = new JLabel("Пароль:");
		LPassword.setForeground(Color.BLACK);
		LPassword.setFont(new Font("Verdana", Font.PLAIN, 14));
		LPassword.setHorizontalAlignment(JLabel.LEFT);
		LPassword.setVerticalAlignment(JLabel.TOP);
		LPassword.setHorizontalAlignment(JLabel.LEFT);
		LPassword.setBounds(10, 40, 140, 30);
		RegistrationFrame.add(LPassword);
		
		TPassword = new JPasswordField();
		TPassword.setText(null);
		TPassword.setFont(new Font("Verdana", Font.PLAIN, 14));
		TPassword.setBounds(150, 35, 430, 30);
		TPassword.setEditable(true);
		RegistrationFrame.add(TPassword);
		
		LPasswordRepeat = new JLabel("Повтор пароля:");
		LPasswordRepeat.setForeground(Color.BLACK);
		LPasswordRepeat.setFont(new Font("Verdana", Font.PLAIN, 14));
		LPasswordRepeat.setHorizontalAlignment(JLabel.LEFT);
		LPasswordRepeat.setVerticalAlignment(JLabel.TOP);
		LPasswordRepeat.setHorizontalAlignment(JLabel.LEFT);
		LPasswordRepeat.setBounds(10, 70, 140, 30);
		RegistrationFrame.add(LPasswordRepeat);
		
		TPasswordRepeat = new JPasswordField();
		TPasswordRepeat.setText(null);
		TPasswordRepeat.setFont(new Font("Verdana", Font.PLAIN, 14));
		TPasswordRepeat.setBounds(150, 65, 430, 30);
		TPasswordRepeat.setEditable(true);
		RegistrationFrame.add(TPasswordRepeat);
		
		Cancel = new JButton("Отмена");
		Cancel.setFont(new Font("Verdana", Font.PLAIN, 14));
		Cancel.setBounds(10, 105, 90, 40);			
		RegistrationFrame.add(Cancel);
		
		Register = new JButton("Зарегистрироваться");
		Register.setFont(new Font("Verdana", Font.PLAIN, 14));
		Register.setBounds(220, 105, 200, 40);			
		RegistrationFrame.add(Register);		
		
		RegistrationFrame.setVisible(true);
		
		Cancel.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				// TODO Auto-generated method stub
				RegistrationFrame.dispose();
			}
			
		});
		
		Register.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				try 
				{
					IsAnyFieldEmpty();
					ArePasswordsEqual();
					
					org.firebirdsql.pool.FBSimpleDataSource dataSource = new org.firebirdsql.pool.FBSimpleDataSource();
					
					dataSource.setDatabase("localhost/3050:E:/Eclipse/Course/DataBases/USERS.FDB");
					dataSource.setDescription("База данных пользователей системы.");
					dataSource.setType("TYPE4");
					dataSource.setEncoding("win1251");
					dataSource.setLoginTimeout(1000);
					
					//Устанавливаем соединение с базой данных
					java.sql.Connection Connection = null;
					Connection = dataSource.getConnection("SYSDBA", "masterkey");
					
					if(Connection == null)
					{
						System.err.println("Ошибка при подключении к базе данных пользователей.");
					};
					
					//Создаём класс, с помощью которого будут выполняться SQL-запросы
					Statement Stmt = Connection.createStatement();
					
					DoesUserAlreadyExist(Stmt, Connection, TUserName.getText().trim());					
					
					//Добавляем нового пользователя
					//SQL-запрос
					int ID = 2 + (int)(Math.random() * 2147483644);
					String SQLRequest = "INSERT INTO USERS VALUES (" + Integer.toString(ID) + ", " + '\'' + TUserName.getText().trim() + "\', \'" + TPassword.getText().trim() + "\');";
					Stmt.executeUpdate(SQLRequest);	
					
					Stmt.close();
					Connection.close();
					
					JOptionPane.showMessageDialog(RegistrationFrame, "Пользователь с именем " + TUserName.getText().trim() + " успешно зарегистрирован в системе.");
					
					//Заносим сведения в отчёт
					Report Rep = new Report();
					Rep.AddRecord("Пользователь с именем \'" + TUserName.getText().trim() + "\' и ID = " + Integer.toString(ID) + " успешно зарегистрирован в системе.", true);
					
					RegistrationFrame.dispose();
				}
				catch(EmptyFieldException e1)
				{
					// TODO Auto-generated catch block
					JOptionPane.showMessageDialog(RegistrationFrame, "Заполните все поля.");
					e1.printStackTrace();
				} 
				catch (NotEqualPasswordsException e1) {
					// TODO Auto-generated catch block
					JOptionPane.showMessageDialog(RegistrationFrame, "Введённые пароли не совпадают.");
					e1.printStackTrace();
				} 
				catch (UserAlreadyExistsException e1) {
					// TODO Auto-generated catch block
					JOptionPane.showMessageDialog(RegistrationFrame, "Пользователь с именем " + TUserName.getText() + " уже зарегистрирован в системе.");
					e1.printStackTrace();
				} catch (SQLException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				};	
			}			
		});		
	};
};
