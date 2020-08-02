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


//���� � ��� �� Connection ��� ������������� � ���!!!
public class AdminUI extends UI
{
	//������ ����������
	//�� ������ ������������ �������
	static private class UserNotSelectedException extends Exception
	{
		public UserNotSelectedException()
		{
			JOptionPane.showMessageDialog(UIFrame, "�������� ������������.");
		};
	};
	
	//�� ����� ��������
	static private class NoParameterException extends Exception
	{
		public NoParameterException()
		{
			JOptionPane.showMessageDialog(UIFrame, "�� ��� ����� ��������.");
		};
	};
	
	//����� �����������, ������ �� ������������
	private void IsAnyUserSelected() throws UserNotSelectedException
	{
		if(UIUsers.getSelectedRow() == -1)
			throw new UserNotSelectedException();
	};
	
	//����� �����������, ����� �� �� ���-������ � ���� ����� ���������
	private void NoParameter(String str) throws  NoParameterException
	{
		if(str.length() == 0)
		{
			throw new NoParameterException();
		};
	};
	
	
	//����� ����������� � ���� ������
	static Statement ConnectToUsersDataBase(String Path) throws SQLException
	{
		org.firebirdsql.pool.FBSimpleDataSource dataSource = new org.firebirdsql.pool.FBSimpleDataSource();
			
		dataSource.setDatabase(Path);
		dataSource.setDescription("���� ������.");
		dataSource.setType("TYPE4");
		dataSource.setEncoding("win1251");
		dataSource.setLoginTimeout(1000);
					
		//������������� ���������� � ����� ������
		UsersDataBaseConnection = null;
		UsersDataBaseConnection = dataSource.getConnection("SYSDBA", "masterkey");
					
		if(UsersDataBaseConnection == null)
		{
			System.err.println("������ ��� ����������� � ���� ������.");
		};
					
		//������ �����, � ������� �������� ����� ����������� SQL-�������		
		return UsersDataBaseConnection.createStatement();
	};
		
	//����� ���������� �� ���� ������
	static void DisconnectFromUsersDataBase(Statement Stmt, ResultSet RS) throws SQLException
	{
		RS.close();
		Stmt.close();
		UsersDataBaseConnection.close();
	};
	
	
	//����� ������, ������������ ���� ������ �������������
	static protected class ThreadUsers extends Thread
	{
		//����
		public ThreadUsers() {};
		
		/** *�������� ����� ������ */
		public void run()
		{
			while(true)
			{
				ReadUsersDataBase();
				
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
	
	//����� ������ ���� ������ �������������
	static void ReadUsersDataBase()
	{
		// TODO Auto-generated method stub
		//����������, ����� ������ ��������
		int SelectedRow = UIUsers.getSelectedRow();
		
		//������� �������
		int rows = UIModelUsers.getRowCount();
		for(int i = 0; i < rows; i++)
			UIModelUsers.removeRow(0);
				
		try
		{
			Statement Stmt = ConnectToUsersDataBase("localhost/3050:E:/Eclipse/Course/DataBases/USERS.FDB");
					
			//������ ���� ������
			//SQL-������
			String SQLRequest = "SELECT * FROM USERS;";
			ResultSet RS = Stmt.executeQuery(SQLRequest);
					
			//��������� �������
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
		
		//�������� ������
		UIUsers.getSelectionModel().setSelectionInterval(SelectedRow, SelectedRow);
	};	
	
	//�������������� ���������� ������������ ����������
	protected static JTable UIUsers;
	protected static DefaultTableModel UIModelUsers;
	protected static JScrollPane UIScrollUsers;
	protected static JButton UIReport;
	protected static java.sql.Connection UsersDataBaseConnection;
	
	@Override
	void Show() {
		// TODO Auto-generated method stub
		//������ ���������� ������������ ����������
		UIFrame = new JFrame("������� \"������� ������������ ���\"::�������������");
		UIFrame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		UIFrame.setSize(1785, 800);
		UIFrame.setLocation(60, 100);	
		UIFrame.setLayout(null);
		UIFrame.setUndecorated(true);
		
		UIHelloLabel = new JLabel("������� \"������� ������������ ���\"::�������������");
		UIHelloLabel.setForeground(Color.BLACK);
		UIHelloLabel.setFont(new Font("Verdana", Font.PLAIN, 14));
		UIHelloLabel.setHorizontalAlignment(JLabel.LEFT);
		UIHelloLabel.setVerticalAlignment(JLabel.TOP);
		UIHelloLabel.setBounds(690, 5, 700, 30);
		UIFrame.add(UIHelloLabel);
		
		UIClose = new JButton("�����");
		UIClose.setFont(new Font("Verdana", Font.PLAIN, 14));
		UIClose.setBounds(1685, 35, 90, 40);			
		UIFrame.add(UIClose);
		
		String[] Columns = {"�������� ����", "����������", "���", "����", "����", "50% ������*", "���-�� �����������"};
		String[][] Data = {};
		UIModel = new DefaultTableModel(Data, Columns);		
		UIGames = new JTable(UIModel);
		UIGames.setFont(new Font("Verdana", Font.PLAIN, 14));
		UIGames.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
				
		UIScroll = new JScrollPane(UIGames);		
		UIScroll.setFont(new Font("Verdana", Font.PLAIN, 14));
		UIScroll.setBounds(10, 135, 1180, 655);		
		UIFrame.add(UIScroll);
		
		String[] ColumnsUsers = {"ID", "���", "������"};
		UIModelUsers = new DefaultTableModel(Data, ColumnsUsers);		
		UIUsers = new JTable(UIModelUsers);
		UIUsers.setFont(new Font("Verdana", Font.PLAIN, 14));
		UIUsers.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
				
		UIScrollUsers = new JScrollPane(UIUsers);		
		UIScrollUsers.setFont(new Font("Verdana", Font.PLAIN, 14));
		UIScrollUsers.setBounds(1200, 135, 575, 655);		
		UIFrame.add(UIScrollUsers);
		
		UIEditUser = new JButton("�������� ������ ������������");
		UIEditUser.setFont(new Font("Verdana", Font.PLAIN, 14));
		UIEditUser.setBounds(1200, 35, 300, 40);
		UIFrame.add(UIEditUser);
		
		UIDeleteUser = new JButton("������� ������������ �� �������");
		UIDeleteUser.setFont(new Font("Verdana", Font.PLAIN, 14));
		UIDeleteUser.setBounds(1200, 85, 300, 40);
		UIFrame.add(UIDeleteUser);
		
		UIAddGame = new JButton("�������� ����");
		UIAddGame.setFont(new Font("Verdana", Font.PLAIN, 14));
		UIAddGame.setBounds(10, 35, 150, 40);
		UIFrame.add(UIAddGame);
		
		UIDeleteGame = new JButton("������� ����");
		UIDeleteGame.setFont(new Font("Verdana", Font.PLAIN, 14));
		UIDeleteGame.setBounds(10, 85, 150, 40);
		UIFrame.add(UIDeleteGame);
		
		UIEditGame = new JButton("�������� ������ �� ����");
		UIEditGame.setFont(new Font("Verdana", Font.PLAIN, 14));
		UIEditGame.setBounds(170, 35, 250, 40);
		UIFrame.add(UIEditGame);
		
		UIReport = new JButton("�������� �����");
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
					
					//������������ � ���� ������
					Statement Stmt = ConnectToDataBase("localhost/3050:E:/Eclipse/Course/DataBases/USERS.FDB");
					
					//SQL-������
					String SQLRequest = "DELETE FROM USERS WHERE ID = " + UIModelUsers.getValueAt(UIUsers.getSelectedRow(), 0).toString() + ";";
					Stmt.executeUpdate(SQLRequest);
					
					//����������� �� ���� ������
					Stmt.close();
					Connection.close();
					
					//��������� ����� ���� ������ �������������
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
					
					//���������� �����, � ������� ������������� ������ ����� ������
					String Name = JOptionPane.showInputDialog(UIFrame, "������� ����� ��� ������������:");
					if(Name == null)
					{
						return;
					};
					
					NoParameter(Name);
					
					String Password = JOptionPane.showInputDialog(UIFrame, "������� ������:");
					if(Password == null)
					{
						return;
					};
					
					NoParameter(Password);
					
					//������������ � ���� ������
					Statement Stmt = ConnectToDataBase("localhost/3050:E:/Eclipse/Course/DataBases/USERS.FDB");
					
					//SQL-������
					String SQLRequest = "UPDATE USERS SET NAME = \'" + Name + "\', PASSWORD = \'" + Password + "\' WHERE ID = " + ID + ";";
					Stmt.executeUpdate(SQLRequest);
					
					//����������� �� ���� ������
					Stmt.close();
					Connection.close();
					
					//��������� ����� ���� ������ �������������
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
					
					//������������ � ���� ������
					Statement Stmt = ConnectToDataBase("localhost/3050:E:/Eclipse/Course/DataBases/GAMES.FDB");
					
					//SQL-������
					String SQLRequest = "DELETE FROM GAMES WHERE NAME = \'" + UIModel.getValueAt(UIGames.getSelectedRow(), 0).toString() + "\' AND DEVELOPER = \'" + UIModel.getValueAt(UIGames.getSelectedRow(), 1).toString() + "\';";
					Stmt.executeUpdate(SQLRequest);
					
					//����������� �� ���� ������
					Stmt.close();
					Connection.close();
					
					//��������� ����� ���� ������ ���
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
					
					//����� ������ ����, ������ � ����������
					String Name = UIModel.getValueAt(UIGames.getSelectedRow(), 0).toString();
					String Developer = UIModel.getValueAt(UIGames.getSelectedRow(), 1).toString();
					
					Name = Name.trim();
					Developer = Developer.trim();
					
					//���������� �����, � ������� ������������� ������ ����� ����
					String Price = JOptionPane.showInputDialog(UIFrame, "������� ����� ����:");
					if(Price == null)
					{
						return;
					};
					
					NoParameter(Price);
					
					int PriceInt = Integer.parseInt(Price);
					
					//���������� �����, � ������� ������������� ������������� ������
					boolean Discount = false;
					if(JOptionPane.showConfirmDialog(UIFrame, "������ �� �� ���������� ������ �� ��������� ����?", "����������� ��������", JOptionPane.YES_NO_OPTION) == 0)
						Discount = true;
					
					//���������� �����, � ������� ������������� ������ ����� ����
					String Count = JOptionPane.showInputDialog(UIFrame, "������� ����� ���������� �����������:");
					if(Count == null)
					{
						return;
					};
					
					NoParameter(Count);
					
					int NumberOfCopies = Integer.parseInt(Count);
					
					//������������ � ���� ������
					Statement Stmt = ConnectToDataBase("localhost/3050:E:/Eclipse/Course/DataBases/GAMES.FDB");
					
					//SQL-������
					String SQLRequest = "";
					if(Discount)
						SQLRequest = "UPDATE GAMES SET PRICE = \'" + Integer.toString(PriceInt) + "\', DISCOUNT = \'TRUE\', NUMBER_OF_COPIES = " + Integer.toString(NumberOfCopies) + " WHERE NAME = \'" + Name + "\' AND DEVELOPER = \'" + Developer + "\';";
					else
						SQLRequest = "UPDATE GAMES SET PRICE = \'" + Integer.toString(PriceInt) + "\', DISCOUNT = \'FALSE\', NUMBER_OF_COPIES = " + Integer.toString(NumberOfCopies) + " WHERE NAME = \'" + Name + "\' AND DEVELOPER = \'" + Developer + "\';";
					
					Stmt.executeUpdate(SQLRequest);
					
					//����������� �� ���� ������
					Stmt.close();
					Connection.close();
					
					//��������� ����� ���� ������ ���
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
					String Name = JOptionPane.showInputDialog(UIFrame, "������� �������� ����:");
					if(Name == null)
					{
						return;
					};
					
					NoParameter(Name);
					Name = Name.trim();
					
					String Developer = JOptionPane.showInputDialog(UIFrame, "������� ������������ ����:");
					if(Developer == null)
					{
						return;
					};
					
					NoParameter(Developer);
					Developer = Developer.trim();
					
					String SYear = JOptionPane.showInputDialog(UIFrame, "������� ��� ������ ����:");
					if(SYear == null)
					{
						return;
					};
					
					NoParameter(SYear);
					SYear = SYear.trim();
					int Year = Integer.parseInt(SYear);
					
					String Genre = JOptionPane.showInputDialog(UIFrame, "������� ���� ����:");
					if(Genre == null)
					{
						return;
					};
					
					NoParameter(Genre);
					Genre = Genre.trim();
					
					//���������� �����, � ������� ������������� ������ ����� ����
					String Price = JOptionPane.showInputDialog(UIFrame, "������� ����� ����:");
					if(Price == null)
					{
						return;
					};
					
					NoParameter(Price);
					Price = Price.trim();
					
					int PriceInt = Integer.parseInt(Price);
					
					//���������� �����, � ������� ������������� ������������� ������
					boolean Discount = false;
					if(JOptionPane.showConfirmDialog(UIFrame, "������ �� �� ���������� ������ �� ��������� ����?", "����������� ��������", JOptionPane.YES_NO_OPTION) == 0)
						Discount = true;
					
					//���������� �����, � ������� ������������� ������ ����� ����
					String Count = JOptionPane.showInputDialog(UIFrame, "������� ����� ���������� �����������:");
					if(Count == null)
					{
						return;
					};
					
					NoParameter(Count);
					Count = Count.trim();
					
					int NumberOfCopies = Integer.parseInt(Count);
					
					//������������ � ���� ������
					Statement Stmt = ConnectToDataBase("localhost/3050:E:/Eclipse/Course/DataBases/GAMES.FDB");
					
					//SQL-������
					String SQLRequest = "";
					
					if(Discount)
						SQLRequest = "INSERT INTO GAMES VALUES (\'" + Name + "\', \'" + Developer + "\', " + SYear + ", \'" + Genre + "\', " + Price + ", TRUE, " + Count + ");";
					else
						SQLRequest = "INSERT INTO GAMES VALUES (\'" + Name + "\', \'" + Developer + "\', " + SYear + ", \'" + Genre + "\', " + Price + ", FALSE, " + Count + ");";
					
					Stmt.executeUpdate(SQLRequest);
					
					//����������� �� ���� ������
					Stmt.close();
					Connection.close();
					
					//��������� ����� ���� ������ ���
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
		
		//��������� �����, ����������� ����� ���� ������ �������
		Refresh = new ThreadRefresh();
		Refresh.start();
		
		//��������� �����, ����������� ����� ���� ������ �������������
		ThreadUsers RefreshUsers = new ThreadUsers();
		RefreshUsers.start();
	}

}
