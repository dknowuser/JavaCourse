package edu.java.shop;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;

//����� ���������� ���������� �����
public final class GuestUI extends UI
{
	//�������������� ���������� ������������ ����������
	private static JLabel LForUsersOnly;
	
	@Override
	void Show() {
		// TODO Auto-generated method stub
		//������ ���������� ������������ ����������
		UIFrame = new JFrame("������� \"������� ������������ ���\"::�����");
		UIFrame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		UIFrame.setSize(1200, 800);
		UIFrame.setLocation(300, 100);	
		UIFrame.setLayout(null);
		UIFrame.setUndecorated(true);
		
		UIHelloLabel = new JLabel("������� \"������� ������������ ���\"::�����");
		UIHelloLabel.setForeground(Color.BLACK);
		UIHelloLabel.setFont(new Font("Verdana", Font.PLAIN, 14));
		UIHelloLabel.setHorizontalAlignment(JLabel.LEFT);
		UIHelloLabel.setVerticalAlignment(JLabel.TOP);
		UIHelloLabel.setBounds(425, 5, 350, 30);
		UIFrame.add(UIHelloLabel);
		
		UIClose = new JButton("�����");
		UIClose.setFont(new Font("Verdana", Font.PLAIN, 14));
		UIClose.setBounds(1100, 35, 90, 40);			
		UIFrame.add(UIClose);
		
		UIBuy = new JButton("������");
		UIBuy.setFont(new Font("Verdana", Font.PLAIN, 14));
		UIBuy.setBounds(10, 35, 90, 40);			
		UIFrame.add(UIBuy);
		
		LForUsersOnly = new JLabel("* ������ ��� ������������������ ���������� �����������.");
		LForUsersOnly.setForeground(Color.BLACK);
		LForUsersOnly.setFont(new Font("Verdana", Font.PLAIN, 14));
		LForUsersOnly.setHorizontalAlignment(JLabel.LEFT);
		LForUsersOnly.setVerticalAlignment(JLabel.TOP);
		LForUsersOnly.setBounds(5, 775, 500, 30);
		UIFrame.add(LForUsersOnly);
		
		
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
					if(JOptionPane.showConfirmDialog(UIFrame, "������������� �� �� ������ ���������� ���� \'" + Name + "\' ������������ \'" + Developer + "\' �� ���� " + Integer.toString(Price) + " ���.?", "����������� ��������", JOptionPane.YES_NO_OPTION) == 0)
					{
						//���������� ��������� � ���� ������
						NumberOfCopies -= 1;
						SQLRequest = "UPDATE GAMES SET NUMBER_OF_COPIES = " + Integer.toString(NumberOfCopies) + " WHERE NAME = \'" + UIModel.getValueAt(UIGames.getSelectedRow(), 0).toString() + "\' AND DEVELOPER = \'" + UIModel.getValueAt(UIGames.getSelectedRow(), 1).toString() + "\';";
						Stmt.executeUpdate(SQLRequest);
					};
					
					//����������� �� ���� ������
					DisconnectFromDataBase(Stmt, RS);					
					
					//��������� ����� ���� ������ �������
					ReadGameDataBase();
					
					//������� �������� � �����
					Report Rep = new Report();
					Rep.AddRecord("���� \'" + Name + "\' ������������ \'" + Developer + "\' ���� ������� ������ �� ���� " + Integer.toString(Price) + ".", false);					
					
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
		
		UIFrame.setVisible(true);
		
		//��������� �����, ����������� ����� ���� ������
		Refresh = new ThreadRefresh();
		Refresh.start();
	}
};
