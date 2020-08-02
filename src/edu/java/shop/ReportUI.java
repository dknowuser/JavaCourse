package edu.java.shop;

import java.awt.Color;
import java.awt.Font;

import javax.swing.*;

public final class ReportUI {
	
	//���������� ������������ ����������
	protected static JFrame UIRepFrame;
	protected static JLabel UIRepWelcome;
	protected static JComboBox UIRepMonth;
	protected static JLabel UIRepUsersLabel;
	protected static JLabel UIRepGamesLabel;
	
	public void Show()
	{
		//������ ���������� ������������ ����������
		UIRepFrame = new JFrame("����������� ������ �� ��������");
		UIRepFrame.setSize(900, 800);
		UIRepFrame.setLocation(60, 100);	
		UIRepFrame.setLayout(null);
		
		UIRepWelcome = new JLabel("�������� �����, ��� �������� ����� ���������� ������:");
		UIRepWelcome.setForeground(Color.BLACK);
		UIRepWelcome.setFont(new Font("Verdana", Font.PLAIN, 14));
		UIRepWelcome.setHorizontalAlignment(JLabel.LEFT);
		UIRepWelcome.setVerticalAlignment(JLabel.TOP);
		UIRepWelcome.setBounds(10, 10, 450, 30);
		UIRepFrame.add(UIRepWelcome);
		
		String Items[] = {"������", "�������", "����", "������", "���", "����", "����", "������",
				"��������", "�������", "������", "�������"};
		UIRepMonth = new JComboBox(Items);
		UIRepMonth.setBounds(10, 40, 200, 30);
		UIRepMonth.setSelectedIndex(-1);
		UIRepFrame.add(UIRepMonth);
		
		UIRepUsersLabel = new JLabel("����� �� ���������� �����������:");
		UIRepUsersLabel.setForeground(Color.BLACK);
		UIRepUsersLabel.setFont(new Font("Verdana", Font.PLAIN, 14));
		UIRepUsersLabel.setHorizontalAlignment(JLabel.LEFT);
		UIRepUsersLabel.setVerticalAlignment(JLabel.TOP);
		UIRepUsersLabel.setBounds(10, 80, 350, 30);
		UIRepFrame.add(UIRepUsersLabel);
		
		UIRepGamesLabel = new JLabel("����� �� ��������:");
		UIRepGamesLabel.setForeground(Color.BLACK);
		UIRepGamesLabel.setFont(new Font("Verdana", Font.PLAIN, 14));
		UIRepGamesLabel.setHorizontalAlignment(JLabel.LEFT);
		UIRepGamesLabel.setVerticalAlignment(JLabel.TOP);
		UIRepGamesLabel.setBounds(600, 80, 350, 30);
		UIRepFrame.add(UIRepGamesLabel);
		
		
		
		
		
		
		UIRepFrame.setVisible(true);
	};
}
