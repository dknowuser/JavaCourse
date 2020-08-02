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
	 * @author ������ �.�. 5307
	 *  ����� Login  - �����, �������������� �����������
	 *  ����� � ������� ��� �����, ��� ������������� ��� ��� ���������� ����������.
	 *  
	 *  **/
public final class Login {
	
	//������ ���������� ��� �������� ������
	//������������ � �������� ������ �� ������ � �������
	static private class NoSuchUserException extends Exception
	{
		public NoSuchUserException()
		{
			JOptionPane.showMessageDialog(LoginFrame, "������������ � �������� ������ �� ������ � �������.");
		};
	};
	
	//�������� ������
	static private class IncorrectPasswordException extends Exception
	{
		public IncorrectPasswordException()
		{
			JOptionPane.showMessageDialog(LoginFrame, "�������� ������.");
		};
	};
	
	//����� �������� ������
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
	
	//����� �����������, ���������� �� ������������ � ���� ������
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
	
	
	
	//���������� ������������ ����������
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
	
	
	//����� �������� ����
	public static void Show()
	{
		//������ ���������� ������������ ����������
		LoginFrame = new JFrame("���� � ������� \"������� ������������ ���\"");
		LoginFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		LoginFrame.setSize(600, 330);
		LoginFrame.setLocation(600, 400);	
		LoginFrame.setLayout(null);
		LoginFrame.setUndecorated(true);
		
		//�����������		
		Hello = new JLabel("��� ������������ ������� \"������� ������������ ���\"!");
		Hello.setForeground(Color.BLUE);
		Hello.setFont(new Font("Verdana", Font.PLAIN, 18));
		Hello.setHorizontalAlignment(JLabel.CENTER);
		Hello.setVerticalAlignment(JLabel.TOP);
		Hello.setBounds(0, 0, 600, 50);
		
		LoginFrame.add(Hello);
		
		EnterAs = new JLabel("����� ���:");
		EnterAs.setFont(new Font("Verdana", Font.PLAIN, 14));
		EnterAs.setForeground(Color.BLACK);
		EnterAs.setHorizontalAlignment(JLabel.LEFT);
		EnterAs.setVerticalAlignment(JLabel.TOP);
		EnterAs.setBounds(10, 50, 600, 50);
		LoginFrame.add(EnterAs);
		
		//��������, ��� ����� ����� � �������
		RadioButtonGroup = new ButtonGroup();
		
		//������ ��� �����
		RadioGuest = new JRadioButton();
		RadioGuest.setText(" �����;");
		RadioGuest.setSelected(true);
		RadioGuest.setFont(new Font("Verdana", Font.PLAIN, 14));
		RadioGuest.setVerticalAlignment(JRadioButton.TOP);
		RadioGuest.setHorizontalAlignment(JRadioButton.LEFT);
		RadioGuest.setBounds(20, 70, 600, 30);
		
		RadioButtonGroup.add(RadioGuest);
		LoginFrame.add(RadioGuest);
		
		//������ ��� �������������
		RadioAdmin = new JRadioButton();
		RadioAdmin.setText(" �������������;");
		RadioAdmin.setSelected(false);
		RadioAdmin.setFont(new Font("Verdana", Font.PLAIN, 14));
		RadioAdmin.setVerticalAlignment(JRadioButton.TOP);
		RadioAdmin.setHorizontalAlignment(JRadioButton.LEFT);
		RadioAdmin.setBounds(20, 100, 600, 30);
		
		RadioButtonGroup.add(RadioAdmin);
		LoginFrame.add(RadioAdmin);
		
		//������ ��� ���������� ����������
		RadioCustomer = new JRadioButton();
		RadioCustomer.setText(" ���������� ����������.");
		RadioCustomer.setSelected(false);
		RadioCustomer.setFont(new Font("Verdana", Font.PLAIN, 14));
		RadioCustomer.setVerticalAlignment(JRadioButton.TOP);
		RadioCustomer.setHorizontalAlignment(JRadioButton.LEFT);
		RadioCustomer.setBounds(20, 130, 600, 30);
		
		RadioButtonGroup.add(RadioCustomer);
		LoginFrame.add(RadioCustomer);
		
		//����, ���� ����� ������� ������������� ������������ � ������
		LUserName = new JLabel("��� ������������:");
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
		
		LPassword = new JLabel("������:");
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
		
		Enter = new JButton("�����");
		Enter.setFont(new Font("Verdana", Font.PLAIN, 14));
		Enter.setBounds(10, 280, 80, 40);			
		LoginFrame.add(Enter);
		
		Register = new JButton("������������������");
		Register.setFont(new Font("Verdana", Font.PLAIN, 14));
		Register.setBounds(200, 280, 200, 40);			
		LoginFrame.add(Register);	
		
		Exit = new JButton("�����");
		Exit.setFont(new Font("Verdana", Font.PLAIN, 14));
		Exit.setBounds(490, 280, 100, 40);			
		LoginFrame.add(Exit);	
		
		Registration Reg = new Registration();
		
		LoginFrame.setVisible(true);
		
		//���������� ����������
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
				
				//�������� ����� ��������� ����
				Reg.Show();
			}
			
		});
		
		Enter.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				if (RadioGuest.isSelected())
				{
					//���� � ������� � ������ �����
					//�� ���������� � ���� ������ �������������
					
					//������ ������ ���������� �����
					GuestUI UI = new GuestUI();
					UI.Show();
					
					//������ ������� ����
					LoginFrame.setVisible(false);
					
					//������� ���� � ��
					RadioGuest.setSelected(true);
					RadioAdmin.setSelected(false);
					RadioCustomer.setSelected(false);
					TUserName.setText(null);
					TPassword.setText(null);
				} else
					if(RadioAdmin.isSelected())
					{
						//���� � ������� � ������ ��������������
						//���������� � ���� ������ �������������
						try
						{
							org.firebirdsql.pool.FBSimpleDataSource dataSource = new org.firebirdsql.pool.FBSimpleDataSource();
							
							dataSource.setDatabase("localhost/3050:E:/Eclipse/Course/DataBases/USERS.FDB");
							dataSource.setDescription("���� ������ ������������� �������.");
							dataSource.setType("TYPE4");
							dataSource.setEncoding("win1251");
							dataSource.setLoginTimeout(1000);
							
							//������������� ���������� � ����� ������
							java.sql.Connection Connection = null;
							Connection = dataSource.getConnection("SYSDBA", "masterkey");
							
							if(Connection == null)
							{
								System.err.println("������ ��� ����������� � ���� ������ �������������.");
							};
							
							//������ �����, � ������� �������� ����� ����������� SQL-�������
							Statement Stmt = Connection.createStatement();
							
							//��������� ������ ������
							//SQL-������
							String SQLRequest = "SELECT * FROM USERS WHERE ID = 1;";
							ResultSet RS = Stmt.executeQuery(SQLRequest);
							
							String AdminPassword = null;
							while(RS.next())
							{
								//��������� ������ ��������������
								AdminPassword = RS.getString(3);
							};	
							
							AdminPassword = AdminPassword.trim();
							
							RS.close();
							Stmt.close();
							Connection.close();
							
							ComparePasswords(Connection, Stmt, RS, AdminPassword);
							
							//������ ������ ���������� ��������������
							AdminUI UI = new AdminUI();
							UI.Show();							
							
							//������ ������� ����
							LoginFrame.setVisible(false);
							
							//������� ���� � ��
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
						//���� � ������� � ������ ����������� ����������
						//���������� � ���� ������ �������������
						try
						{
							org.firebirdsql.pool.FBSimpleDataSource dataSource = new org.firebirdsql.pool.FBSimpleDataSource();
							
							dataSource.setDatabase("localhost/3050:E:/Eclipse/Course/DataBases/USERS.FDB");
							dataSource.setDescription("���� ������ ������������� �������.");
							dataSource.setType("TYPE4");
							dataSource.setEncoding("win1251");
							dataSource.setLoginTimeout(1000);
							
							//������������� ���������� � ����� ������
							java.sql.Connection Connection = null;
							Connection = dataSource.getConnection("SYSDBA", "masterkey");
							
							if(Connection == null)
							{
								System.err.println("������ ��� ����������� � ���� ������ �������������.");
							};
							
							//������ �����, � ������� �������� ����� ����������� SQL-�������
							Statement Stmt = Connection.createStatement();
							
							//���� ������������ � �������� ������
							//SQL-������
							String SQLRequest = "SELECT * FROM USERS WHERE NAME = \'" + TUserName.getText() + "\' AND ID > 1;";
							ResultSet RS = Stmt.executeQuery(SQLRequest);
							DoesUserAlreadyExist(Connection, Stmt, RS);
							
							//��������� ������ ������������
							String UserPassword = null;
							
							//��������� ������ ����������� ����������
							UserPassword = RS.getString(3);
							
							UserPassword = UserPassword.trim();
							
							ComparePasswords(Connection, Stmt, RS, UserPassword);
							
							//������ ������ ���������� ����������� ����������
							CustomerUI UI = new CustomerUI(RS.getInt(1));
							UI.Show();
							
							RS.close();
							Stmt.close();
							Connection.close();
							
							//������ ������� ����
							LoginFrame.setVisible(false);
							
							//������� ���� � ��
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

	/**����� ����� **/
	public static void main(String[] args) {
		// TODO Auto-generated method stub				
		
		//���������� ����
		Show();
	}
}
