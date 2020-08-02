package edu.java.shop;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.swing.*;

	/**
	 * @author Марчук Л.Б. 5307
	 *  Класс Login  - класс, обеспечивающий возможность
	 *  входа в систему как гость, как администратор или как постоянный покупатель.
	 *  
	 *  **/
public final class Login {
	
	//Классы исключений для текущего класса
	//Пользователь с заданным именем не найден в системе
	static private class NoSuchUserException extends Exception
	{
		public NoSuchUserException()
		{
			JOptionPane.showMessageDialog(LoginFrame, "Пользователь с заданным именем не найден в системе.");
		};
	};
	
	//Неверный пароль
	static private class IncorrectPasswordException extends Exception
	{
		public IncorrectPasswordException()
		{
			JOptionPane.showMessageDialog(LoginFrame, "Неверный пароль.");
		};
	};
	
	//Метод проверки пароля
	private static void ComparePasswords(java.sql.Connection Connection, Statement Stmt, ResultSet RS,  String str) throws IncorrectPasswordException, SQLException
	{	
		if(TPassword.getText().compareTo(str) != 0)
		{
			Stmt.close();
			RS.close();
			Connection.close();
			throw new IncorrectPasswordException();
		};
	};
	
	//Метод определения, существует ли пользователь в базе данных
	private static void DoesUserAlreadyExist(java.sql.Connection Connection, Statement Stmt, ResultSet RS) throws NoSuchUserException, SQLException
	{			
		if(!RS.next())
		{
			Stmt.close();
			RS.close();
			Connection.close();
			throw new NoSuchUserException();
		};
	};
	
	
	
	//Компоненты графического интерфейса
	static JFrame LoginFrame;	
	static JLabel Hello;
	static JLabel EnterAs;
	static ButtonGroup RadioButtonGroup;
	static JRadioButton RadioGuest;
	static JRadioButton RadioAdmin;
	static JRadioButton RadioCustomer;
	static JLabel LUserName;
	static JTextField TUserName;
	static JLabel LPassword;
	static JPasswordField TPassword;
	static JButton Enter;
	static JButton Register;
	static JButton Exit;
	
	
	//Метод создания окна
	public static void Show()
	{
		//Создаём компоненты графического интерфейса
		LoginFrame = new JFrame("Вход в систему \"Магазин компьютерных игр\"");
		LoginFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		LoginFrame.setSize(600, 330);
		LoginFrame.setLocation(600, 400);	
		LoginFrame.setLayout(null);
		LoginFrame.setUndecorated(true);
		
		//Приветствие		
		Hello = new JLabel("Вас приветствует система \"Магазин компьютерных игр\"!");
		Hello.setForeground(Color.BLUE);
		Hello.setFont(new Font("Verdana", Font.PLAIN, 18));
		Hello.setHorizontalAlignment(JLabel.CENTER);
		Hello.setVerticalAlignment(JLabel.TOP);
		Hello.setBounds(0, 0, 600, 50);
		
		LoginFrame.add(Hello);
		
		EnterAs = new JLabel("Войти как:");
		EnterAs.setFont(new Font("Verdana", Font.PLAIN, 14));
		EnterAs.setForeground(Color.BLACK);
		EnterAs.setHorizontalAlignment(JLabel.LEFT);
		EnterAs.setVerticalAlignment(JLabel.TOP);
		EnterAs.setBounds(10, 50, 600, 50);
		LoginFrame.add(EnterAs);
		
		//Варианты, как можно войти в систему
		RadioButtonGroup = new ButtonGroup();
		
		//Входим как гость
		RadioGuest = new JRadioButton();
		RadioGuest.setText(" гость;");
		RadioGuest.setSelected(true);
		RadioGuest.setFont(new Font("Verdana", Font.PLAIN, 14));
		RadioGuest.setVerticalAlignment(JRadioButton.TOP);
		RadioGuest.setHorizontalAlignment(JRadioButton.LEFT);
		RadioGuest.setBounds(20, 70, 600, 30);
		
		RadioButtonGroup.add(RadioGuest);
		LoginFrame.add(RadioGuest);
		
		//Входим как администратор
		RadioAdmin = new JRadioButton();
		RadioAdmin.setText(" администратор;");
		RadioAdmin.setSelected(false);
		RadioAdmin.setFont(new Font("Verdana", Font.PLAIN, 14));
		RadioAdmin.setVerticalAlignment(JRadioButton.TOP);
		RadioAdmin.setHorizontalAlignment(JRadioButton.LEFT);
		RadioAdmin.setBounds(20, 100, 600, 30);
		
		RadioButtonGroup.add(RadioAdmin);
		LoginFrame.add(RadioAdmin);
		
		//Входим как постоянный покупатель
		RadioCustomer = new JRadioButton();
		RadioCustomer.setText(" постоянный покупатель.");
		RadioCustomer.setSelected(false);
		RadioCustomer.setFont(new Font("Verdana", Font.PLAIN, 14));
		RadioCustomer.setVerticalAlignment(JRadioButton.TOP);
		RadioCustomer.setHorizontalAlignment(JRadioButton.LEFT);
		RadioCustomer.setBounds(20, 130, 600, 30);
		
		RadioButtonGroup.add(RadioCustomer);
		LoginFrame.add(RadioCustomer);
		
		//Поля, куда нужно вводить идентификатор пользователя и пароль
		LUserName = new JLabel("Имя пользователя:");
		LUserName.setForeground(Color.BLACK);
		LUserName.setFont(new Font("Verdana", Font.PLAIN, 14));
		LUserName.setHorizontalAlignment(JLabel.LEFT);
		LUserName.setVerticalAlignment(JLabel.TOP);
		LUserName.setHorizontalAlignment(JLabel.LEFT);
		LUserName.setBounds(10, 200, 140, 30);
		LoginFrame.add(LUserName);
		
		TUserName = new JTextField();
		TUserName.setText(null);
		TUserName.setFont(new Font("Verdana", Font.PLAIN, 14));
		TUserName.setBounds(150, 195, 430, 30);
		TUserName.setEditable(false);
		LoginFrame.add(TUserName);
		
		LPassword = new JLabel("Пароль:");
		LPassword.setForeground(Color.BLACK);
		LPassword.setFont(new Font("Verdana", Font.PLAIN, 14));
		LPassword.setHorizontalAlignment(JLabel.LEFT);
		LPassword.setVerticalAlignment(JLabel.TOP);
		LPassword.setHorizontalAlignment(JLabel.LEFT);
		LPassword.setBounds(10, 230, 140, 30);
		LoginFrame.add(LPassword);
		
		TPassword = new JPasswordField();
		TPassword.setText(null);
		TPassword.setFont(new Font("Verdana", Font.PLAIN, 14));
		TPassword.setBounds(150, 225, 430, 30);
		TPassword.setEditable(false);
		LoginFrame.add(TPassword);
		
		Enter = new JButton("Войти");
		Enter.setFont(new Font("Verdana", Font.PLAIN, 14));
		Enter.setBounds(10, 280, 80, 40);			
		LoginFrame.add(Enter);
		
		Register = new JButton("Зарегистрироваться");
		Register.setFont(new Font("Verdana", Font.PLAIN, 14));
		Register.setBounds(200, 280, 200, 40);			
		LoginFrame.add(Register);	
		
		Exit = new JButton("Выход");
		Exit.setFont(new Font("Verdana", Font.PLAIN, 14));
		Exit.setBounds(490, 280, 100, 40);			
		LoginFrame.add(Exit);	
		
		Registration Reg = new Registration();
		
		LoginFrame.setVisible(true);
		
		//Добавление слушателей
		RadioGuest.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				// TODO Auto-generated method stub
				TUserName.setEditable(false);
				TPassword.setEditable(false);
			}
			
		});
		
		RadioAdmin.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				TUserName.setEditable(false);
				TPassword.setEditable(true);
			}
			
		});
		
		RadioCustomer.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				TUserName.setEditable(true);
				TPassword.setEditable(true);
			}
			
		});
		
		Register.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				
				//Вызываем метод отрисовки окна
				Reg.Show();
			}
			
		});
		
		Enter.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				if (RadioGuest.isSelected())
				{
					//Вход в систему в режиме гостя
					//Не обращаемся к базе данных пользователей
					
					//Создаём объект интерфейса гостя
					GuestUI UI = new GuestUI();
					UI.Show();
					
					//Прячем текущее окно
					LoginFrame.setVisible(false);
					
					//Очищаем поля в нём
					RadioGuest.setSelected(true);
					RadioAdmin.setSelected(false);
					RadioCustomer.setSelected(false);
					TUserName.setText(null);
					TPassword.setText(null);
				} else
					if(RadioAdmin.isSelected())
					{
						//Вход в систему в режиме администратора
						//Обращаемся к базе данных пользователей
						try
						{
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
							
							//Проверяем только пароль
							//SQL-запрос
							String SQLRequest = "SELECT * FROM USERS WHERE ID = 1;";
							ResultSet RS = Stmt.executeQuery(SQLRequest);
							
							String AdminPassword = null;
							while(RS.next())
							{
								//Считываем пароль администратора
								AdminPassword = RS.getString(3);
							};	
							
							AdminPassword = AdminPassword.trim();
							
							RS.close();
							Stmt.close();
							Connection.close();
							
							ComparePasswords(Connection, Stmt, RS, AdminPassword);
							
							//Создаём объект интерфейса администратора
							AdminUI UI = new AdminUI();
							UI.Show();							
							
							//Прячем текущее окно
							LoginFrame.setVisible(false);
							
							//Очищаем поля в нём
							RadioGuest.setSelected(true);
							RadioAdmin.setSelected(false);
							RadioCustomer.setSelected(false);
							TUserName.setText(null);
							TPassword.setText(null);
						}
						catch (SQLException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						} catch (IncorrectPasswordException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						};				
					} else
					{
						//Вход в систему в режиме постоянного покупателя
						//Обращаемся к базе данных пользователей
						try
						{
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
							
							//Ищем пользователя с заданным именем
							//SQL-запрос
							String SQLRequest = "SELECT * FROM USERS WHERE NAME = \'" + TUserName.getText() + "\' AND ID > 1;";
							ResultSet RS = Stmt.executeQuery(SQLRequest);
							DoesUserAlreadyExist(Connection, Stmt, RS);
							
							//Проверяем пароль пользователя
							String UserPassword = null;
							
							//Считываем пароль постоянного покупателя
							UserPassword = RS.getString(3);
							
							UserPassword = UserPassword.trim();
							
							ComparePasswords(Connection, Stmt, RS, UserPassword);
							
							//Создаём объект интерфейса постоянного покупателя
							CustomerUI UI = new CustomerUI(RS.getInt(1));
							UI.Show();
							
							RS.close();
							Stmt.close();
							Connection.close();
							
							//Прячем текущее окно
							LoginFrame.setVisible(false);
							
							//Очищаем поля в нём
							RadioGuest.setSelected(true);
							RadioAdmin.setSelected(false);
							RadioCustomer.setSelected(false);
							TUserName.setText(null);
							TPassword.setText(null);
						}
						catch (SQLException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						} catch (NoSuchUserException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						} catch (IncorrectPasswordException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						};
					};
			};			
		});
		
		Exit.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				// TODO Auto-generated method stub
				LoginFrame.dispose();
			}
			
		});
	};

	/**Точка входа **/
	public static void main(String[] args) {
		// TODO Auto-generated method stub				
		
		//Отображаем окно
		Show();
	}
}
