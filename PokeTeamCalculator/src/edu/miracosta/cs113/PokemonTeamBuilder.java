package edu.miracosta.cs113;

/**
 * PokemonTeamBuilder.java
 * 
 * This holds the User Interface that allows the User to
 * interact with the program. 
 * 
 * 
 * @author Joshua Cervantes
 */

import java.awt.EventQueue;
import javax.swing.JFrame;
import java.awt.BorderLayout;
import javax.swing.JPanel;
import javax.swing.ListCellRenderer;
import javax.swing.border.LineBorder;
import java.awt.Color;
import java.awt.Component;
import javax.imageio.ImageIO;
import javax.swing.DefaultComboBoxModel;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.Locale.LanguageRange;

import javax.swing.JButton;

public class PokemonTeamBuilder {

	private JFrame frame;
	private ArrayList<Pokemon> pokelist = new ArrayList<Pokemon>(151);
	private BufferedImage bigImg = null;
	private PokeTeam MyTeam = new PokeTeam();
	private String message= "Please Select a Valid Pokemon";
	
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					PokemonTeamBuilder window = new PokemonTeamBuilder();
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}
	

	/**
	 * Create the application.
	 */
	public PokemonTeamBuilder() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		
		//populates the pokelist with all the pokemon
		pokepop();
		
		frame = new JFrame();
		frame.setBounds(400, 100, 1500, 700);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);
		
		JPanel panel = new JPanel(new BorderLayout());
		panel.setBorder(new LineBorder(new Color(0, 0, 0), 10));
		panel.setBounds(82, 169, 80, 80);
		frame.getContentPane().add(panel);
		
		JLabel label = new JLabel();
		panel.add(label, BorderLayout.CENTER);
		
		JPanel panel_1 = new JPanel();
		panel_1.setBorder(new LineBorder(new Color(0, 0, 0), 10));
		panel_1.setBounds(286, 169, 80, 80);
		frame.getContentPane().add(panel_1);
		panel_1.setLayout(new BorderLayout(0, 0));
		
		JLabel label_1 = new JLabel("");
		panel_1.add(label_1, BorderLayout.EAST);
		
		JPanel panel_2 = new JPanel();
		panel_2.setBorder(new LineBorder(new Color(0, 0, 0), 10));
		panel_2.setBounds(522, 169, 80, 80);
		frame.getContentPane().add(panel_2);
		panel_2.setLayout(new BorderLayout(0, 0));
		
		JLabel label_2 = new JLabel("");
		panel_2.add(label_2);
		
		JPanel panel_3 = new JPanel();
		panel_3.setBorder(new LineBorder(new Color(0, 0, 0), 10));
		panel_3.setBounds(782, 169, 80, 80);
		frame.getContentPane().add(panel_3);
		panel_3.setLayout(new BorderLayout(0, 0));
		
		JLabel label_3 = new JLabel("");
		panel_3.add(label_3);
		
		JPanel panel_4 = new JPanel();
		panel_4.setBorder(new LineBorder(new Color(0, 0, 0), 10));
		panel_4.setBounds(1032, 169, 80, 80);
		frame.getContentPane().add(panel_4);
		panel_4.setLayout(new BorderLayout(0, 0));
		
		JLabel label_4 = new JLabel("");
		panel_4.add(label_4);
		
		
		JPanel panel_5 = new JPanel();
		panel_5.setBorder(new LineBorder(new Color(0, 0, 0), 10));
		panel_5.setBounds(1270, 169, 80, 80);
		frame.getContentPane().add(panel_5);
		panel_5.setLayout(new BorderLayout(0, 0));
		
		JLabel label_5 = new JLabel("");
		panel_5.add(label_5);
		
		JLabel label_6 = new JLabel("");
		label_6.setBounds(82, 350, 92, 26);
		frame.getContentPane().add(label_6);
				
				
				JLabel label_7 = new JLabel("");
				label_7.setBounds(286, 350, 92, 26);
				frame.getContentPane().add(label_7);
				
				JLabel label_8 = new JLabel("");
				label_8.setBounds(522, 350, 92, 26);
				frame.getContentPane().add(label_8);
				
				JLabel label_9 = new JLabel("");
				label_9.setBounds(782, 350, 92, 26);
				frame.getContentPane().add(label_9);
				
				JLabel label_10 = new JLabel("");
				label_10.setBounds(1042, 350, 92, 26);
				frame.getContentPane().add(label_10);
		
				JLabel label_11 = new JLabel("");
				label_11.setBounds(1270, 350, 92, 26);
				frame.getContentPane().add(label_11);
				
		//Title of the Program
				JLabel lblPokemonTeamBuilder = new JLabel("POKEMON TEAM BUILDER");
				lblPokemonTeamBuilder.setFont(new Font("Comic Sans MS", Font.BOLD | Font.ITALIC, 60));
				lblPokemonTeamBuilder.setBackground(Color.BLUE);
				lblPokemonTeamBuilder.setForeground(Color.BLUE);
				lblPokemonTeamBuilder.setBounds(344, 33, 870, 76);
				frame.getContentPane().add(lblPokemonTeamBuilder);
		
		/***************Bringing the comboBoxes to Existence*****************/
		
		
		JComboBox comboBox_0 = new JComboBox();
		comboBox_0.setBounds(47, 297, 141, 32);
		comboBox_0.setModel(populate());
		comboBox_0.setRenderer(new ImagesTextRenderer());
	
		frame.getContentPane().add(comboBox_0);
		
		JComboBox comboBox_1 = new JComboBox();
		comboBox_1.setBounds(263, 297, 141, 32);
		comboBox_1.setModel(populate());
		comboBox_1.setRenderer(new ImagesTextRenderer());
		
		frame.getContentPane().add(comboBox_1);
		
		
		JComboBox comboBox_2 = new JComboBox();
		comboBox_2.setBounds(495, 297, 141, 32);
		comboBox_2.setModel(populate());
		comboBox_2.setRenderer(new ImagesTextRenderer());
		
		frame.getContentPane().add(comboBox_2);
		
		JComboBox comboBox_3 = new JComboBox();
		comboBox_3.setBounds(751, 297, 141, 32);
		comboBox_3.setModel(populate());
		comboBox_3.setRenderer(new ImagesTextRenderer());
		
		frame.getContentPane().add(comboBox_3);
		
		JComboBox comboBox_4 = new JComboBox();
		comboBox_4.setBounds(1010, 297, 141, 32);
		comboBox_4.setModel(populate());
		comboBox_4.setRenderer(new ImagesTextRenderer());
		
		frame.getContentPane().add(comboBox_4);
		
		JComboBox comboBox_5 = new JComboBox();
		comboBox_5.setBounds(1245, 297, 141, 32);
		comboBox_5.setModel(populate());
		comboBox_5.setRenderer(new ImagesTextRenderer());
		
		frame.getContentPane().add(comboBox_5);
		
		
		/*********** Events with actionListers for all the comboBoxes *******************/
		/**
		 * clicking on the options will display the Pokemon's larger in the the black box
		 */
		
		comboBox_0.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				//getting large 
				//System.out.println("TESTING: ACTION PERFORMED!!!");
				int index = ((ImagesNText) comboBox_0.getSelectedItem()).getID();
			
				ImageIcon image = new ImageIcon("img/" +index+ "L.png");
				label.setIcon(image);
				panel.add(label, BorderLayout.CENTER);	
				label_6.setText(pokelist.get(index).getTypes());
				
				
			}
		});
		
		comboBox_1.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				//getting large 
				//System.out.println("TESTING: ACTION PERFORMED!!!");
				int index = ((ImagesNText) comboBox_1.getSelectedItem()).getID();
			
				ImageIcon image = new ImageIcon("img/" +index+ "L.png");
				label_1.setIcon(image);
				panel_1.add(label_1, BorderLayout.CENTER);	
				label_7.setText(pokelist.get(index).getTypes());
				
			}
		});
		
		comboBox_2.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				//getting large 
				//System.out.println("TESTING: ACTION PERFORMED!!!");
				int index = ((ImagesNText) comboBox_2.getSelectedItem()).getID();
				ImageIcon image = new ImageIcon("img/" +index+ "L.png");
				label_2.setIcon(image);
				panel_2.add(label_2, BorderLayout.CENTER);	
				label_8.setText(pokelist.get(index).getTypes());
				
			}
		});
		
		comboBox_3.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				//getting large 
				//System.out.println("TESTING: ACTION PERFORMED!!!");
				int index = ((ImagesNText) comboBox_3.getSelectedItem()).getID();
				ImageIcon image = new ImageIcon("img/" +index+ "L.png");
				label_3.setIcon(image);
				panel_3.add(label_3, BorderLayout.CENTER);
				label_9.setText(pokelist.get(index).getTypes());
				
			}
		});
		
		comboBox_4.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				//getting large 
				//System.out.println("TESTING: ACTION PERFORMED!!!");
				int index = ((ImagesNText) comboBox_4.getSelectedItem()).getID();
				ImageIcon image = new ImageIcon("img/" +index+ "L.png");
				label_4.setIcon(image);
				panel_4.add(label_4, BorderLayout.CENTER);	
				label_10.setText(pokelist.get(index).getTypes());
				
			}
		});
		
		comboBox_5.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				//getting large 
				//System.out.println("TESTING: ACTION PERFORMED!!!");
				int index = ((ImagesNText) comboBox_5.getSelectedItem()).getID();
				ImageIcon image = new ImageIcon("img/" +index+ "L.png");
				label_5.setIcon(image);
				panel_5.add(label_5, BorderLayout.CENTER);	
				label_11.setText(pokelist.get(index).getTypes());
				
			}
		});
		
		
		
		//Button that will make the best team with the inputs already made if any
		
		JButton btnMakeBestTeam = new JButton("Make Best Team");
		btnMakeBestTeam.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				MyTeam.fillTeam();
				comboBox_0.setSelectedIndex(MyTeam.team.get(0).getID());
				comboBox_1.setSelectedIndex(MyTeam.team.get(1).getID());
				comboBox_2.setSelectedIndex(MyTeam.team.get(2).getID());
				comboBox_3.setSelectedIndex(MyTeam.team.get(3).getID());
				comboBox_4.setSelectedIndex(MyTeam.team.get(4).getID());
				comboBox_5.setSelectedIndex(MyTeam.team.get(5).getID());
			}
		});
		btnMakeBestTeam.setBounds(495, 527, 141, 35);
		frame.getContentPane().add(btnMakeBestTeam);
		
		
		
		/*******************************Team Lock**************************/
		/*Makes its impossible to surpass the set limit                   */
		/*that PokeTeam has and can't add "select" as a pokemon           */
		
		JButton lockTeam = new JButton("LOCK IN");
		lockTeam.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				int index = ((ImagesNText) comboBox_0.getSelectedItem()).getID();
				if(index ==0){
					JOptionPane.showMessageDialog(null, message);
					index= 1;
				}
				//System.out.println(index);
				else{
					MyTeam.addPlayer(pokelist.get(index));
					comboBox_0.setEnabled(false);
					lockTeam.setEnabled(false);
				}
			}
			});
		lockTeam.setBounds(47, 443, 141, 35);
		frame.getContentPane().add(lockTeam);
		
		
		JButton lockTeam1 = new JButton("LOCK IN");
		lockTeam1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				int index = ((ImagesNText) comboBox_1.getSelectedItem()).getID();
				if(index ==0){
					JOptionPane.showMessageDialog(null, message);
					index= 1;
				}
				else{
					MyTeam.addPlayer(pokelist.get(index));
					comboBox_1.setEnabled(false);
					lockTeam1.setEnabled(false);
				}
			}
			});
		lockTeam1.setBounds(263, 443, 141, 35);
		frame.getContentPane().add(lockTeam1);
		
		
		JButton lockTeam2 = new JButton("LOCK IN");
		lockTeam2.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				int index = ((ImagesNText) comboBox_2.getSelectedItem()).getID();
				if(index ==0){
					JOptionPane.showMessageDialog(null, message);
					index= 1;
				}
				else{
					MyTeam.addPlayer(pokelist.get(index));
					comboBox_2.setEnabled(false);
					lockTeam2.setEnabled(false);
				}
			}
			});
		lockTeam2.setBounds(495, 443, 141, 35);
		frame.getContentPane().add(lockTeam2);
		
		JButton lockTeam3 = new JButton("LOCK IN");
		lockTeam3.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				int index = ((ImagesNText) comboBox_3.getSelectedItem()).getID();
				if(index ==0){
					JOptionPane.showMessageDialog(null, message);
					index= 1;
				}
				else{
					MyTeam.addPlayer(pokelist.get(index));
					comboBox_3.setEnabled(false);
					lockTeam3.setEnabled(false);
				}
			}
			});
		lockTeam3.setBounds(751, 443, 141, 35);
		frame.getContentPane().add(lockTeam3);
		
		JButton lockTeam4 = new JButton("LOCK IN");
		lockTeam4.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				int index = ((ImagesNText) comboBox_4.getSelectedItem()).getID();
				if(index ==0){
					JOptionPane.showMessageDialog(null, message);
					index= 1;
				}
				else{
					MyTeam.addPlayer(pokelist.get(index));
					comboBox_4.setEnabled(false);
					lockTeam4.setEnabled(false);
				}
			}
			});
		lockTeam4.setBounds(1010, 443, 141, 35);
		frame.getContentPane().add(lockTeam4);
		
		JButton lockTeam5 = new JButton("LOCK IN");
		lockTeam5.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				int index = ((ImagesNText) comboBox_5.getSelectedItem()).getID();
				if(index ==0){
					JOptionPane.showMessageDialog(null, message);
					index= 1;
				}
				else{
					MyTeam.addPlayer(pokelist.get(index));
					comboBox_5.setEnabled(false);
					lockTeam5.setEnabled(false);
				}
			}
			});
		lockTeam5.setBounds(1245, 443, 141, 35);
		frame.getContentPane().add(lockTeam5);
		
		/****************************RESET******************************/
		/* Clears all objects in MyTeam and  */
		/* unlocks all the buttons so user  */
		/*         can make a new team     */
		
		JButton resetButton = new JButton("Reset");
		resetButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				MyTeam.team.clear();
				comboBox_0.setEnabled(true);
				lockTeam.setEnabled(true);
				comboBox_1.setEnabled(true);
				lockTeam1.setEnabled(true);
				comboBox_2.setEnabled(true);
				lockTeam2.setEnabled(true);
				comboBox_3.setEnabled(true);
				lockTeam3.setEnabled(true);
				comboBox_4.setEnabled(true);
				lockTeam4.setEnabled(true);
				comboBox_5.setEnabled(true);
				lockTeam5.setEnabled(true);	
				comboBox_0.setSelectedIndex(0);
				comboBox_1.setSelectedIndex(0);
				comboBox_2.setSelectedIndex(0);
				comboBox_3.setSelectedIndex(0);
				comboBox_4.setSelectedIndex(0);
				comboBox_5.setSelectedIndex(0);
			}
		});
		resetButton.setBounds(751, 527, 141, 35);
		frame.getContentPane().add(resetButton);
	}
			
	//Imports all the Pokemon to the ArrayList
	private void pokepop() {
		try {
			//PrintWriter pw = new PrintWriter("Here.dat");
			//pw.close();		
			Scanner ri = new Scanner(new FileInputStream("PokeData.txt"));
			
			Pokemon mon;
			for(int i =0; i <152; i++){
				mon = new Pokemon();
				mon.setID(ri.nextInt());
				mon.setName(ri.next());
				mon.setTypes(ri.next(), ri.next());
				mon.setStyle(ri.next().charAt(0));
				mon.setTier(ri.next());
				//System.out.println(mon.getID());
				//System.out.println(mon.getName());
				//System.out.println(mon.getTypes()); 
				//System.out.println(mon.getStyle());
				//System.out.println(mon.getTier());
				pokelist.add(mon);
			}
			
		//	System.out.println("all pokemon added to array");
			
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}

	//Populates the ComboBoxes with the Pokemon and sprites
	private DefaultComboBoxModel populate(){
		DefaultComboBoxModel dm = new DefaultComboBoxModel();
		for(int i =0; i <pokelist.size(); i++){
		dm.addElement(new ImagesNText(i, new ImageIcon("img/" +i+ "s.png"), pokelist.get(i).getName()));
		}
		return dm;
	}
	
	//Helps render the Pokemons' sprites and names in the ComboBox
	private class ImagesTextRenderer extends JLabel implements ListCellRenderer
	{
		@Override
		public Component getListCellRendererComponent(JList list, Object val,
				int index, boolean selected, boolean focused){
			
			//get Values
			ImagesNText it = (ImagesNText)val;
			
			//Set values
			setIcon(it.getImg());
			setText(it.getName());
			
			if(selected)
			{
				setBackground(list.getSelectionBackground());
				setForeground(list.getSelectionForeground());
			}else
			{
				setBackground(list.getBackground());
				setForeground(list.getForeground());
			}
			
			
			return this;
		}
		}
	
	//This help contain the pokemons' ID, sprite, and name for the ComboBox
	private class ImagesNText{
		private int id;
		private Icon img;
		private String name;
		
		public ImagesNText(int id, Icon img, String name)
		{
			this.img=img;
			this.name=name;
			this.id=id;
		}

		public Icon getImg() {
			return img;
		}

		public void setImg(Icon img) {
			this.img = img;
		}

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}
		
		public int getID(){
			return id;
		}
		public void setID(int id){
			this.id= id;
		}
		
}
}
