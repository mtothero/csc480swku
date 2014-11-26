/****************************************************************************************************/
/*
/* Author: Matt Tothero
/* Modification Date: November 26th, 2014
/* Creation Date: November 26th, 2014 
/* Course: CSC480 010
/* Professor Name: Dr. Frye
/* Filename: KBlookup.java
/* Purpose: The following uses Jena methods to query the recipe ontology and display what the user
/* 			can make based off of his/her preferences which are set through the mainGUI application.
/*		
/*			To make sure you are entering in the right input, please go over the Readme first. A future
/*			enhancement would be to replace the textboxes in the Recipe's GUI and use Radio buttons 
/*			instead. 
/******************************************************************************************************/

// imports for Jena API
import com.hp.hpl.jena.ontology.OntModel;
import com.hp.hpl.jena.ontology.OntModelSpec;
import com.hp.hpl.jena.ontology.OntClass;
import com.hp.hpl.jena.ontology.OntProperty;
import com.hp.hpl.jena.ontology.OntResource;
import com.hp.hpl.jena.ontology.Individual;
import com.hp.hpl.jena.ontology.DatatypeProperty;
import com.hp.hpl.jena.datatypes.xsd.XSDDatatype;
import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.InfModel;
import com.hp.hpl.jena.rdf.model.ModelFactory;
import com.hp.hpl.jena.rdf.model.ModelFactoryBase;
import com.hp.hpl.jena.rdf.model.Property;
import com.hp.hpl.jena.rdf.model.Resource;
import com.hp.hpl.jena.rdf.model.ModelCon;
import com.hp.hpl.jena.rdf.model.Statement;
import com.hp.hpl.jena.rdf.model.StmtIterator;
import com.hp.hpl.jena.rdf.model.Literal;
import com.hp.hpl.jena.rdf.model.RDFNode;
import com.hp.hpl.jena.reasoner.*;
import com.hp.hpl.jena.reasoner.rulesys.Util;
import com.hp.hpl.jena.util.iterator.ExtendedIterator;
import com.hp.hpl.jena.query.Query;
import com.hp.hpl.jena.query.Syntax;
import com.hp.hpl.jena.query.QueryExecution;
import com.hp.hpl.jena.query.QueryFactory;
import com.hp.hpl.jena.query.QueryExecutionFactory;
import com.hp.hpl.jena.query.QuerySolution;
import com.hp.hpl.jena.query.ResultSet;
import com.hp.hpl.jena.query.ResultSetFormatter;
import com.clarkparsia.pellet.sparqldl.jena.SparqlDLExecutionFactory;
import com.hp.hpl.jena.shared.CannotEncodeCharacterException;

// general imports
import java.lang.*;
import java.io.*;
import java.util.*;
import java.net.*;
import java.util.Iterator;
import java.util.Collection;
import java.util.ArrayList;
import java.text.DecimalFormat;
 
public class KBlookup 
{
    public static void findRecipes() 
	{        
		try 
		{	
			//we first get the Recipe Ontology Class and all of its properties
			OntClass recipeClass = KBconnect.RecipeModel.getOntClass( KBconnect.ONT_PREFIX + "Recipe" );
			OntProperty name = KBconnect.RecipeModel.getOntProperty( KBconnect.ONT_PREFIX + "name" );
			OntProperty hasTemp = KBconnect.RecipeModel.getOntProperty( KBconnect.ONT_PREFIX + "hasTemp" );
			OntProperty hasSize = KBconnect.RecipeModel.getOntProperty( KBconnect.ONT_PREFIX + "hasSize" );
			OntProperty isHealthy = KBconnect.RecipeModel.getOntProperty( KBconnect.ONT_PREFIX + "isHealthy" );
			OntProperty isClassified = KBconnect.RecipeModel.getOntProperty( KBconnect.ONT_PREFIX + "isClassifiedAs" );
			OntProperty hasTime = KBconnect.RecipeModel.getOntProperty( KBconnect.ONT_PREFIX + "takes" );
			OntProperty hasDifficulty = KBconnect.RecipeModel.getOntProperty( KBconnect.ONT_PREFIX + "hasDifficulty" );
			OntProperty FoodAllergy = KBconnect.RecipeModel.getOntProperty( KBconnect.ONT_PREFIX + "hasAllergy" );
			
			//this iterates though all of the recipe instances that exist in the knowledge base
			for ( ExtendedIterator<? extends OntResource> recipes = recipeClass.listInstances(); recipes.hasNext(); ) 
			{
				//let's pretty these strings up so they are easier to compare
				OntResource recipeInstance = recipes.next();
				String nameValue = recipeInstance.getPropertyValue( name ).toString();
				nameValue = nameValue.replace("^^http://www.w3.org/2001/XMLSchema#string", "");
				
				String hasTempValue = recipeInstance.getPropertyValue( hasTemp ).toString();
				hasTempValue = hasTempValue.replace(KBconnect.ONT_PREFIX, "");
				
				String hasSizeValue = recipeInstance.getPropertyValue( hasSize ).toString();
				hasSizeValue = hasSizeValue.replace(KBconnect.ONT_PREFIX, "");
				
				String isHealthyValue = recipeInstance.getPropertyValue( isHealthy ).toString();
				isHealthyValue = isHealthyValue.replace(KBconnect.ONT_PREFIX, "");
				
				String isClassifiedValue = recipeInstance.getPropertyValue( isClassified ).toString();
				isClassifiedValue = isClassifiedValue.replace(KBconnect.ONT_PREFIX, "");
				
				String hasTimeValue = recipeInstance.getPropertyValue( hasTime ).toString();
				hasTimeValue = hasTimeValue.replace(KBconnect.ONT_PREFIX, "");
				hasTimeValue = hasTimeValue.replace("^^http://www.w3.org/2001/XMLSchema#string", "");
				
				String hasDifficultyValue = recipeInstance.getPropertyValue( hasDifficulty ).toString();
				hasDifficultyValue = hasDifficultyValue.replace(KBconnect.ONT_PREFIX, "");
				
				//since a recipe doesn't have to have an allergy, we have to first check if each instance does
				//before obtaining its text value
				String FoodAllergyValue;
				if(recipeInstance.getPropertyValue( FoodAllergy ) != null)
				{
					FoodAllergyValue = recipeInstance.getPropertyValue( FoodAllergy ).toString();
					FoodAllergyValue = FoodAllergyValue.replace(KBconnect.ONT_PREFIX, "");
				}
				else
				{
					FoodAllergyValue = "N/A";
				}
				
				//the following compares each user preference to the current recipe
				boolean hasPassed = true;
				
				//check and see if any recipes match the temp text box
				if(hasTempValue.equals(MainGUI.tempText.getText()) || MainGUI.tempText.getText().equals(""))
					hasPassed = hasPassed;
				else
					hasPassed = false;
				
				//check and see if any recipes match the size text box
				if(hasSizeValue.equals(MainGUI.sizeText.getText()) || MainGUI.sizeText.getText().equals(""))
					hasPassed = hasPassed;
				else
					hasPassed = false;
					
				//check and see if any recipes match the nutritional text box
				if(isHealthyValue.equals(MainGUI.NutritionalText.getText()) || MainGUI.NutritionalText.getText().equals(""))
					hasPassed = hasPassed;
				else
					hasPassed = false;
				
				//check and see if any recipes match the cuisine text box
				if(isClassifiedValue.equals(MainGUI.CuisineText.getText()) || MainGUI.CuisineText.getText().equals(""))
					hasPassed = hasPassed;
				else
					hasPassed = false;
					
				//check and see if any recipes are less than the time text box
				if(MainGUI.timeText.getText().equals(""))
					hasPassed = hasPassed;
				else if((Integer.parseInt(hasTimeValue) < Integer.parseInt(MainGUI.timeText.getText())))
					hasPassed = hasPassed;
				else
					hasPassed = false;
					
				//check and see if any recipes match the difficulty text box
				if(hasDifficultyValue.equals(MainGUI.difficultyText.getText()) || MainGUI.difficultyText.getText().equals(""))
					hasPassed = hasPassed;
				else
					hasPassed = false;
					
				//check and see if any recipes do NOT match the allergy text box
				if (MainGUI.AllergyText.getText().equals(""))
					hasPassed = hasPassed;
				else if(FoodAllergyValue.equals(MainGUI.AllergyText.getText()))
					hasPassed = false;
				else
					hasPassed = hasPassed;
					
				
				if(hasPassed)
					MainGUI.printInfo("---> " + nameValue + "\n");
					//Debugging purposes
					//System.out.println( nameValue + " - " + hasTempValue + " - " + hasSizeValue + " - " + isHealthyValue + " - " + isClassifiedValue + " - " + hasTimeValue + " - " + //hasDifficultyValue + " - " + FoodAllergyValue);
			}
		}
		catch (Exception e) 
		{
			e.printStackTrace();
		} 
		finally 
		{	
			MainGUI.printInfo("--->\n");
		}   
	}
}
