/****************************************************************************************************/
/*
/* Author: Matt Tothero
/* Modification Date: November 26th, 2014
/* Creation Date: November 21st, 2014 
/* Course: CSC480 010
/* Professor Name: Dr. Frye
/* Filename: mainGUI.java
/* Purpose: The following is a GUI that assists the user in creating a knowledge 
/*			base off of the Recipe Ontology, adding instances, 
/*			executing SPARQL queries, creating SPARQL queries, and
/*			printing the Knowledge base. 
/* 
/******************************************************************************************************/

import java.awt.* ;
import javax.swing.* ;
import java.awt.event.* ;
import javax.swing.text.*;
 
public class MainGUI extends JFrame
{
	static private JLayeredPane controlLayer = new JLayeredPane();
	static private JPanel queryPanel = new JPanel(new GridBagLayout());
    static private JPanel controlPanel = new JPanel();
	static private JPanel parentPanel = new JPanel(new GridLayout(2,1));
	static public JButton createButton = new JButton("Create");
	static public JButton loadButton = new JButton("Load");
	static public JButton executeButton = new JButton("Execute");
	static public JButton hungryButton = new JButton("Hungry?");
	static public JButton submitButton = new JButton("Submit");
	static public JButton dumpButton = new JButton("Dump");
	static private JButton quitButton = new JButton("Quit");
	static private JTextArea outputConsole = new JTextArea();
	static public Main mainApi;
	static public JTextField CuisineText = new JTextField(10);
	static public JTextField AllergyText = new JTextField(10);
	static public JTextField NutritionalText = new JTextField(10);
	static public JTextField difficultyText = new JTextField(10);
	static public JTextField timeText = new JTextField(10);
	static public JTextField sizeText = new JTextField(10);
	static public JTextField tempText = new JTextField(10);
	
    public MainGUI() 
	{
		//the following add's frame properties
        setPreferredSize(new Dimension(600, 525));
        setLayout(new BorderLayout());
		
		//the control layer will be used to place the controls on top of the background
        add(controlLayer, BorderLayout.CENTER);
		getContentPane().setBackground(new Color(245,245,245));
        controlLayer.setBounds(10, 10, 580, 505);
		 
		 //the text area acts as a console
		Font font = new Font("Lucida Sans", Font.PLAIN, 12);
		outputConsole.setBorder(BorderFactory.createLineBorder(Color.black));
		outputConsole.setFont(font);
		DefaultCaret caret = (DefaultCaret)outputConsole.getCaret();
		caret.setUpdatePolicy(DefaultCaret.ALWAYS_UPDATE);
		outputConsole.setLineWrap (true);
		outputConsole.setEditable(false);
		
		//the scroller allows the user to scroll through the text area
		JScrollPane scroller = new JScrollPane(outputConsole);
        scroller.setPreferredSize(new Dimension(520, 225));
        scroller.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        scroller.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
        controlPanel.add(scroller);
		
		//the create button allows the user to create the knowledge base
		createButton.setEnabled(true);
		createButton.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				outputConsole.append("---> Output files have been created.\n");
				mainApi.createModel();
				outputConsole.append("---> Knowledge base has been created.\n");
				mainApi.readModel();
				outputConsole.append("---> Knowledge base has been populated.\n--->\n");
				loadButton.setEnabled(true);
				executeButton.setEnabled(true);
				dumpButton.setEnabled(true);
				hungryButton.setEnabled(true);
				createButton.setEnabled(false);
				outputConsole.append("---> You now have access to the following functionality:\n");
				outputConsole.append("---> \t - Load New Instances Into Knowledge Base\n");
				outputConsole.append("---> \t - Execute Static Queries\n");
				outputConsole.append("---> \t - Find Out What Recipe You Can Make\n");
				outputConsole.append("---> \t - Close the Knowledge Base and Print Its Results\n--->\n");
			}
		});
        controlPanel.add(createButton);
		
		//the load button allows the user to load the instances created in the input.txt file
		loadButton.setEnabled(false);
		loadButton.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				mainApi.loadInstances();
			}
		});
        controlPanel.add(loadButton);
		
		//the execute button allows the user to execute the predefined queries
		executeButton.setEnabled(false);
		executeButton.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				
			}
		});
		controlPanel.add(executeButton);
		
		//the hungry button allows the user to ask the knowledge for recipe recommendations based off of the user's input
		hungryButton.setEnabled(false);
		hungryButton.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				outputConsole.append("---> Wondering what you can make this evening? Fill in the text boxes below, then\n --->click submit.\n");
				outputConsole.append("---> Your results will then be displayed here.\n");
				outputConsole.append("---> To make sure you are entering in the right values, please read the README.\n");
				outputConsole.append("---> \n");
				submitButton.setEnabled(true);
				hungryButton.setEnabled(false);
				CuisineText.setEnabled(true); 
				AllergyText.setEnabled(true); 
				NutritionalText.setEnabled(true); 
				difficultyText.setEnabled(true); 
				timeText.setEnabled(true); 
				sizeText.setEnabled(true); 
				tempText.setEnabled(true);
			}
		});
        controlPanel.add(hungryButton);
		
		//the submit button queries the model based off the input
		submitButton.setEnabled(false);
		submitButton.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				mainApi.findUserRecipes();
				submitButton.setEnabled(false);
				hungryButton.setEnabled(true);
				CuisineText.setEnabled(false); 
				AllergyText.setEnabled(false); 
				NutritionalText.setEnabled(false); 
				difficultyText.setEnabled(false); 
				timeText.setEnabled(false); 
				sizeText.setEnabled(false); 
				tempText.setEnabled(false);
				CuisineText.setText(""); 
				AllergyText.setText(""); 
				NutritionalText.setText(""); 
				difficultyText.setText(""); 
				timeText.setText(""); 
				sizeText.setText(""); 
				tempText.setText(""); 
			}
		});
        controlPanel.add(submitButton);
		
		//the dump button allows the user to close the knowledge base and create the output files
		dumpButton.setEnabled(false);
		dumpButton.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				mainApi.dumpKnowledgeBase();
			}
		});
        controlPanel.add(dumpButton);
	
		//the quit button allows the user to quit
		quitButton.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				System.exit(0);
			}
		});
		controlPanel.add(quitButton);
		parentPanel.add(controlPanel);
		createRecipeInputGUI();
		parentPanel.add(queryPanel);
		
		//the control panel groups these buttons together
        controlPanel.setBounds(0, 0, 600, 525);
        controlPanel.setOpaque(false);
		queryPanel.setOpaque(false);
		queryPanel.setBounds(0, 0, 600, 525);
		parentPanel.setBounds(0, 0, 600, 525);
        parentPanel.setOpaque(false);
        controlLayer.add(parentPanel, new Integer(0), 0);
 
		//finalize the application frame
        pack();
		setVisible(true);
		setTitle("Recipe Ontology");
        setLocationRelativeTo(null);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		requestFocusInWindow();
    }
 
	//first thing that gets called
    public static void main(String[] args) 
	{
        MainGUI frame = new MainGUI();
		outputConsole.append("---> GUI has now been loaded.\n");
		mainApi = new Main();
    }
	
	//other classes call this function, acts as a set function
	public static void printInfo(String output)
	{
		outputConsole.append(output);
	}
	
	//creates the different possible recipe inputs
	public static void createRecipeInputGUI()
	{
		//let's make this pretty
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.gridx = 0;
		gbc.gridy = 0;
		gbc.fill = GridBagConstraints.HORIZONTAL;
		gbc.ipadx = 10;
		gbc.ipady = 10;
		gbc.insets = new Insets(2,2,2,2);  //top padding
		
		//1st box
		JLabel tempLabel = new JLabel("Temp:");
		queryPanel.add(tempLabel, gbc);
		gbc.gridx++;
		
		//2nd box
		queryPanel.add(tempText, gbc);
		gbc.gridx++;
		JLabel timeLabel = new JLabel("Takes less than:");
		queryPanel.add(timeLabel, gbc);
		gbc.gridx++;
		
		//3rd box
		queryPanel.add(timeText, gbc);
		gbc.gridx--;
		gbc.gridx--;
		gbc.gridx--;
		gbc.gridy++;
		JLabel sizeLabel = new JLabel("Size:");
		queryPanel.add(sizeLabel, gbc);
		gbc.gridx++;
		
		//4th box
		queryPanel.add(sizeText, gbc);
		gbc.gridx++;
		JLabel difficultyLabel = new JLabel("Difficulty:");
		queryPanel.add(difficultyLabel, gbc);
		gbc.gridx++;

		//5th
		queryPanel.add(difficultyText, gbc);
		gbc.gridx--;
		gbc.gridx--;
		gbc.gridx--;
		gbc.gridy++;
		JLabel NutritionalLabel = new JLabel("Nutritional Value:");
		queryPanel.add(NutritionalLabel, gbc);
		gbc.gridx++;
		queryPanel.add(NutritionalText, gbc);
		gbc.gridx++;
		
		//6th box
		JLabel AllergyLabel = new JLabel("Allergy:");
		queryPanel.add(AllergyLabel, gbc);
		gbc.gridx++;
		queryPanel.add(AllergyText, gbc);
		gbc.gridx--;
		gbc.gridx--;
		gbc.gridx--;
		gbc.gridy++;
		
		//7th box
		JLabel CuisineLabel = new JLabel("Cuisine:");
		queryPanel.add(CuisineLabel, gbc);
		gbc.gridx++;
		queryPanel.add(CuisineText, gbc);
		
		//disable them at startup
		CuisineText.setEnabled(false); 
		AllergyText.setEnabled(false); 
		NutritionalText.setEnabled(false); 
		difficultyText.setEnabled(false); 
		timeText.setEnabled(false); 
		sizeText.setEnabled(false); 
		tempText.setEnabled(false); 
	}
}
