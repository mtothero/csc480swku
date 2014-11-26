/****************************************************************************************************/
/*
/* Modifier: Matt Tothero
/* Author: Dr. Frye
/* Modification Date: November 20th, 2014
/* Creation Date: Mar 2013 
/* Course: CSC480 010
/* Professor Name: Dr. Frye
/* Filename: main.java
/* Purpose: The following is the interface for creating a knowledge 
/*			base off of the Recipe Ontology, adding instances, 
/*			executing SPARQL queries, creating SPARQL queries, and
/*			printing the Knowledge base. 
/*
/*			It's an adapter that connects the Recipe GUI to the other ontology files.
/*
/* Output Files:                                             
/*			outputFile (*.output) - general output file           
/*			KBoutputFile (*.kboutput) - knowledge base classes and instances                                
/*			KBpropsoutputFile (*.kbpropsoutput) - model's triples, in Subject - Property - Object format
/* 
/******************************************************************************************************/

import java.io.*;
import java.util.*;

public class Main 
{	
    OutputStream outstream = null;
	PrintStream outputFile = null;
	OutputStream KBoutstream = null; 
	PrintStream KBoutputFile = null;
	OutputStream KBpropsoutstream = null; 
	PrintStream KBpropsoutputFile = null;
	
	String filePrefix = "recipe";
	
	public Main() 
    {		
		try 
		{
			/************************OPEN FILES***************************/
			outstream = new FileOutputStream(filePrefix + ".output");
			outputFile = new PrintStream(outstream);
			KBoutstream = new FileOutputStream(filePrefix + ".kboutput");
			KBoutputFile = new PrintStream(KBoutstream);
			KBpropsoutstream = new FileOutputStream(filePrefix + ".kbpropsoutput");
			KBpropsoutputFile = new PrintStream(KBpropsoutstream);

			if (outputFile == null || KBoutputFile == null || KBpropsoutputFile == null) 
			{
				System.out.println("Error opening output files");
				System.exit(-1);
			}  	
		}  
		catch (Exception e)  
		{
			e.printStackTrace();
		} 
    }

	public void createModel()
	{
		/************************************************************/
		/************** INITIALIZE MODEL AND ONTOLOGIES *************/
		/************************************************************/
		
		/**********************CREATE MODEL**************************/
		KBconnect.createModel();
	}
	
	public void readModel()
	{
		/********************** READ ONTOLOGIES **********************/
		KBconnect.readOntology(KBconnect.ONT_PREFIX);
		KBconnect.readOntology(KBconnect.INST_PREFIX);
	}
	
	public void loadInstances()
	{
		/************* ADD INSTANCES TO KNNOWLEDGE BASE **************/
		KBinterface.addInstances();
		MainGUI.printInfo("---> Instances have been dynamically added.\n--->\n");
		MainGUI.loadButton.setEnabled(false);
	}
	
	public void dumpKnowledgeBase()
	{
		/******************* DUMP KNOWLEDGE BASE *********************/
		KBprint.dumpKB(KBoutstream, KBoutputFile, KBpropsoutstream, KBpropsoutputFile);
		KBprint.dumpModel(filePrefix);
		KBprint.listModelStatements(filePrefix);
		MainGUI.createButton.setEnabled(false);
		MainGUI.loadButton.setEnabled(false);
		MainGUI.executeButton.setEnabled(false);
		MainGUI.dumpButton.setEnabled(false);
		MainGUI.hungryButton.setEnabled(false);
		MainGUI.submitButton.setEnabled(false);
		MainGUI.printInfo("---> Knowledge base has been closed.\n--->\n");
		MainGUI.printInfo("---> To recreate the knowledge base, please restart the application.\n");
	}
	
	public void submitQueries()
	{
		/**************** QUERY KB with test queries *****************/
		KBqueries.queryKB();
	}
	
	public void findUserRecipes()
	{
		/**************** QUERY KB using Jena methods and user input *****************/
		KBlookup.findRecipes();
	}
} 
