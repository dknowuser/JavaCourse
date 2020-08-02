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
	//������ ����������
	//������ � ���������� ������ �� ���������
	static private class NotEqualPasswordsException extends Exception
	{
		public NotEqualPasswordsException()
		{
			JOptionPane.showMessageDialog(UIFrame, "������ �� ���������.");
		};
	};
	
	//������������ � ����� ������ ��� ���������� � �������
	static private class UserAlreadyExistsException extends Exception
	{
		public UserAlreadyExistsException() 
		{
			JOptionPane.showMessageDialog(UIFrame, "������������ � ����� ������ ��� ����������.");
		};
	};	
	
	//����� �����������, ��������� �� �������� ������
	private static void ArePasswordsEqual(String Str1, String Str2) throws NotEqualPasswordsException
	{
		if(!Str1.equals(Str2))
			throw new NotEqualPasswordsException();
	};
	
	//����� �����������, ���������� �� ������������ � ���� ������
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
	
	//ID ����������� ����������
	private int ID = 2;

	//����
	public CustomerUI(int UserID)
	{
		ID = UserID;
	};
	
	
	@Override
	void Show() {
		// TODO Auto-generated method stub
		//������ ���������� ������������ ����������
		UIFrame = new JFrame("������� \"������� ������������ ���\"::���������� ����������");
		UIFrame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		UIFrame.setSize(1200, 800);
		UIFrame.setLocation(300, 100);	
		UIFrame.setLayout(null);
		UIFrame.setUndecorated(true);
				
		UIHelloLabel = new JLabel("������� \"������� ������������ ���\"::���������� ���������� (ID = " + Integer.toString(ID) + ")");
		UIHelloLabel.setForeground(Color.BLACK);
		UIHelloLabel.setFont(new Font("Verdana", Font.PLAIN, 14));
		UIHelloLabel.setHorizontalAlignment(JLabel.LEFT);
		UIHelloLabel.setVerticalAlignment(JLabel.TOP);
		UIHelloLabel.setBounds(250, 5, 700, 30);
		UIFrame.add(UIHelloLabel);
				
		UIClose = new JButton("�����");
		UIClose.setFont(new Font("Verdana", Font.PLAIN, 14));
		UIClose.setBounds(1100, 35, 90, 40);			
		UIFrame.add(UIClose);
				
		UIBuy = new JButton("������");
		UIBuy.setFont(new Font("Verdana", Font.PLAIN, 14));
		UIBuy.setBounds(10, 35, 90, 40);			
		UIFrame.add(UIBuy);
		
		UIEditUser = new JButton("�������� ������ ������������");
		UIEditUser.setFont(new Font("Verdana", Font.PLAIN, 14));
		UIEditUser.setBounds(110, 35, 300, 40);
		UIFrame.add(UIEditUser);
		
		UIDeleteUser = new JButton("������� ������������ �� �������");
		UIDeleteUser.setFont(new Font("Verdana", Font.PLAIN, 14));
		UIDeleteUser.setBounds(420, 35, 300, 40);
		UIFrame.add(UIDeleteUser);		
				
		String[] Columns = {"�������� ����", "����������", "���", "����", "����", "50% ������*", "���-�� �����������"};
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
					
					//������������ � ���� ������
					Statement Stmt = ConnectToDataBase("localhost/3050:E:/Eclipse/Course/DataBases/GAMES.FDB");
					
					//SQL-������
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
					
					//��������� ����� ����� ����� �������
					DoGameCopiesExist(NumberOfCopies);
					
					//���������� ������������, ����� �� �� ������ ���� �� ���� (��� ������)
					//���������� �� ����� �������
					if(UIGames.getSelectedRow() != -1)
					{
						if(Discount)
						{
							if(JOptionPane.showConfirmDialog(UIFrame, "������������� �� �� ������ ���������� ���� \'" + Name + "\' ������������ \'" + Developer + "\' �� ���� " + Integer.toString(Price/2) + " ���.?", "����������� ��������", JOptionPane.YES_NO_OPTION) == 0)
							{
								//���������� ��������� � ���� ������
								NumberOfCopies -= 1;
								SQLRequest = "UPDATE GAMES SET NUMBER_OF_COPIES = " + Integer.toString(NumberOfCopies) + " WHERE NAME = \'" + UIModel.getValueAt(UIGames.getSelectedRow(), 0).toString() + "\' AND DEVELOPER = \'" + UIModel.getValueAt(UIGames.getSelectedRow(), 1).toString() + "\';";
								Stmt.executeUpdate(SQLRequest);
							};
						}
						else
						{
							if(JOptionPane.showConfirmDialog(UIFrame, "������������� �� �� ������ ���������� ���� \'" + Name + "\' ������������ \'" + Developer + "\' �� ���� " + Integer.toString(Price) + " ���.?", "����������� ��������", JOptionPane.YES_NO_OPTION) == 0)
							{
								//���������� ��������� � ���� ������
								NumberOfCopies -= 1;
								SQLRequest = "UPDATE GAMES SET NUMBER_OF_COPIES = " + Integer.toString(NumberOfCopies) + " WHERE NAME = \'" + UIModel.getValueAt(UIGames.getSelectedRow(), 0).toString() + "\' AND DEVELOPER = \'" + UIModel.getValueAt(UIGames.getSelectedRow(), 1).toString() + "\';";
								Stmt.executeUpdate(SQLRequest);
							};
						};	
					}
					else
						JOptionPane.showMessageDialog(UIFrame, "�������� ������� ��� �������.");
					
					//����������� �� ���� ������
					DisconnectFromDataBase(Stmt, RS);					
					
					//��������� ����� ���� ������ �������
					ReadGameDataBase();
					
					//������� �������� � �����
					Report Rep = new Report();
					if(Discount)
						Rep.AddRecord("���� \'" + Name + "\' ������������ \'" + Developer + "\' ���� ������� ���������� ����������� �� ���� " + Integer.toString(Price/2) + ".", false);
					else
						Rep.AddRecord("���� \'" + Name + "\' ������������ \'" + Developer + "\' ���� ������� ���������� ����������� �� ���� " + Integer.toString(Price) + ".", false);
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
				if(JOptionPane.showConfirmDialog(UIFrame, "������������� �� �� ������ ������� ������������ � ID = " + Integer.toString(ID) + "?", "����������� ��������", JOptionPane.YES_NO_OPTION) == 0)
				{
					try 
					{
						//������������ � ���� ������ �������������
						Statement Stmt = ConnectToDataBase("localhost/3050:E:/Eclipse/Course/DataBases/USERS.FDB");
						
						//SQL-������
						String SQLRequest = "DELETE FROM USERS WHERE ID = " + Integer.toString(ID) + ";";	
						Stmt.executeUpdate(SQLRequest);
						
						//����������� �� ���� ������
						Stmt.close();
					} catch (SQLException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					
					Report Rep = new Report();
					Rep.AddRecord("������������ � ID = " + Integer.toString(ID) + " ������� �����.", true);
					
					//������� ��������� �� �������� ������������
					JOptionPane.showMessageDialog(UIFrame, "������������ � ID = " + Integer.toString(ID) + " ������� �����. ����� ��������� ����� �� �������.");					
					
					//������� �� ������� ����� �������� ������������					
					UIFrame.dispose();
				};
			}
			
		});
		
		UIEditUser.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				// TODO Auto-generated method stub
				
				//���������� �����, � ������� ������������ ������ ����� ������
				String Name = JOptionPane.showInputDialog(UIFrame, "������� ����� ��� ������������:");
				if(Name == null)
				{
					return;
				};
				
				if(Name.length() == 0)
				{
					JOptionPane.showMessageDialog(UIFrame, "��� ������������ �� ����� ���� ������.");
					return;
				};
				
				//���������, ���� �� ������������ � ����� �� ������
				//������������ � ���� ������
				Statement Stmt;
				try {
					Stmt = ConnectToDataBase("localhost/3050:E:/Eclipse/Course/DataBases/USERS.FDB");
					
					//SQL-������
					String SQLRequest = "SELECT * FROM USERS WHERE NAME = \'" + Name + "\' AND ID != " + Integer.toString(ID) + ";";
					ResultSet RS = Stmt.executeQuery(SQLRequest);
					
					DoesUserAlreadyExist(Stmt, RS);
					
					//����������� �� ���� ������
					DisconnectFromDataBase(Stmt, RS);
					
					String Password = JOptionPane.showInputDialog(UIFrame, "������� ������:");
					if(Password == null)
					{
						return;
					};
					
					if(Password.length() == 0)
					{
						JOptionPane.showMessageDialog(UIFrame, "������ �� ����� ���� ������.");
						return;
					};
					
					String RepeatPassword = JOptionPane.showInputDialog(UIFrame, "��������� ������:");
					if(RepeatPassword == null)
					{
						return;
					};
					
					ArePasswordsEqual(Password, RepeatPassword);
					
					//������������ � ���� ������
					Statement Stmt1 = ConnectToDataBase("localhost/3050:E:/Eclipse/Course/DataBases/USERS.FDB");
					
					//SQL-������
					String SQLRequest1 = "UPDATE USERS SET NAME = \'" + Name + "\', PASSWORD = \'" + Password + "\' WHERE ID = " + Integer.toString(ID) + ";";
					Stmt1.executeUpdate(SQLRequest1);
					
					//����������� �� ���� ������
					Stmt1.close();
					
					Report Rep = new Report();
					Rep.AddRecord("������������ � ID = " + Integer.toString(ID) + " ������� ���� ������.", true);
					
					//������� ��������� �� ��������� ������ ������������
					JOptionPane.showMessageDialog(UIFrame, "������ ������������ ������� ��������.");
					
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
		
		//��������� �����, ����������� ����� ���� ������
		Refresh = new ThreadRefresh();
		Refresh.start();
	}

}
