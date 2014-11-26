/****************************************************************************************************/
/*
/* Modifier: Matt Tothero
/* Author: Dr. Frye
/* Modification Date: November 26th, 2014
/* Creation Date: Nov 2014 
/* Course: CSC480 010
/* Professor Name: Dr. Frye
/* Filename: KBinterface.java 
/* Purpose: This program will read the ASCII-formatted instance file and create an instance in the Recipe         
/*     		ontology.                                                   
/* 			API: this program uses the Jena ontology API.
/* 
/******************************************************************************************************/

// imports for Jena API
import com.hp.hpl.jena.ontology.OntModel;
import com.hp.hpl.jena.ontology.OntModelSpec;
import com.hp.hpl.jena.ontology.OntClass;
import com.hp.hpl.jena.ontology.Individual;
import com.hp.hpl.jena.ontology.DatatypeProperty;
import com.hp.hpl.jena.ontology.ObjectProperty;
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

// general imports
import java.lang.*;
import java.io.*;
import java.util.*;
import java.net.*;
import java.util.Iterator;
import java.util.Collection;
import java.util.ArrayList;

public class KBinterface 
{
	static OntClass recipe;
	static String propStr, propValue;
	static Individual individual;
	static Statement statement;
	static Literal propLiteral;
	static Resource res;
	static DatatypeProperty prop;
	static ObjectProperty oprop;
	static String classSt;
	
    public static void addInstances()  
	{                         
		try 
		{
			Scanner s = null;
			String line;
			String[] lineSeparated;
		
			/**********************************************************************/
			/**********************************************************************/
			/*****  Create instances for Recipe Ontology from static data     *****/
			/**********************************************************************/
			/**********************************************************************/
								   
			MainGUI.printInfo("---> Adding Recipe instances to KB\n");
			
			//the following reads in data from input.txt and then inserts it into the knowledge base
			try 
			{
				s = new Scanner(new BufferedReader(new FileReader("input.txt")));

				while(s.hasNextLine()) 
				{
					line = s.nextLine();
					lineSeparated=line.split(",");
					//System.out.println(lineSeparated[0]);
					//System.out.println(lineSeparated[1]);
					inputValue(lineSeparated[0],lineSeparated[1]);
				}
			} 
			finally 
			{
				if (s != null) 
				{
					s.close();
				}
			}
		}  
		catch(Exception e) 
		{
			e.printStackTrace();
		}  
	}

	public static void inputValue(String property, String value)
	{
		//switch statements for strings are only supported in Java 7
		//a loop would go great here, too much repetitive code
		if(property.equals("--BEGIN"))
		{
			// Get the class for the individual (instance)
			classSt = KBconnect.ONT_PREFIX + "Recipe";
			recipe = KBconnect.RecipeModel.getOntClass(classSt);
			if (recipe == null)   
			{
				MainGUI.printInfo("---> No class found --> recipe!\n");
				return;
			}  // end if
		}
		
		else if(property.equals("--END"))
		{
			//nothing needs to be placed here yet
		}
		
		else if(property.equals("ID"))
		{
			// Create a Recipe recipes
			individual = KBconnect.RecipeModel.createIndividual(KBconnect.ONT_PREFIX + value, recipe);
		}
		
		else if(property.equals("NAME"))
		{
			// add name (datatype property) for recipe
			propStr = KBconnect.ONT_PREFIX + "name";
			//System.out.println(propStr);
			prop = KBconnect.RecipeModel.getDatatypeProperty(propStr);
			//System.out.println(prop);
			
			if (prop != null)  
			{
				propLiteral = KBconnect.RecipeModel.createTypedLiteral(value, XSDDatatype.XSDstring);
				statement = KBconnect.RecipeModel.createLiteralStatement(individual, prop, propLiteral);
				KBconnect.RecipeModel.add(statement);
			}  // end if
		}
		
		else if(property.equals("INGREDIENT"))
		{
			propStr = KBconnect.ONT_PREFIX + "hasIngredients";
			propValue = KBconnect.ONT_PREFIX + value;
			
			res = KBconnect.RecipeModel.getResource(propValue);
			if (res == null)
				MainGUI.printInfo("---> No ingredient found!\n");
			else 
			{
				//System.out.println(res);
				oprop = KBconnect.RecipeModel.getObjectProperty(propStr);
				if (oprop != null) 
				{
					statement = KBconnect.RecipeModel.createStatement(individual, oprop, res);
					KBconnect.RecipeModel.add(statement);
				}  // end if
			}  // end else
		}
		
		else if(property.equals("CLASSIFICATION"))
		{
			propStr = KBconnect.ONT_PREFIX + "isClassifiedAs";
			propValue = KBconnect.ONT_PREFIX + value;
			
			res = KBconnect.RecipeModel.getResource(propValue);
			if (res == null)
				MainGUI.printInfo("---> No cuisine found!\n");
			else 
			{
				//System.out.println(res);
				oprop = KBconnect.RecipeModel.getObjectProperty(propStr);
				if (oprop != null) 
				{
					statement = KBconnect.RecipeModel.createStatement(individual, oprop, res);
					KBconnect.RecipeModel.add(statement);
				}  // end if
			}  // end else 
		}
		
		else if(property.equals("TEMP"))
		{
			propStr = KBconnect.ONT_PREFIX + "hasTemp";
			propValue = KBconnect.ONT_PREFIX + value;
			
			res = KBconnect.RecipeModel.getResource(propValue);
			//System.out.println(res);
			if (res == null)
				MainGUI.printInfo("---> No temp setting found!\n");
			else 
			{
				//System.out.println(res);
				oprop = KBconnect.RecipeModel.getObjectProperty(propStr);
				if (oprop != null) 
				{
					statement = KBconnect.RecipeModel.createStatement(individual, oprop, res);
					KBconnect.RecipeModel.add(statement);
				}  // end if
			}  // end else 
		}
		
		else if(property.equals("SIZE"))
		{
			propStr = KBconnect.ONT_PREFIX + "hasSize";
			propValue = KBconnect.ONT_PREFIX + value;
			
			res = KBconnect.RecipeModel.getResource(propValue);
			if (res == null)
				MainGUI.printInfo("---> No size setting found!\n");
			else 
			{
				//System.out.println(res);
				oprop = KBconnect.RecipeModel.getObjectProperty(propStr);
				if (oprop != null) 
				{
					statement = KBconnect.RecipeModel.createStatement(individual, oprop, res);
					KBconnect.RecipeModel.add(statement);
				}  // end if
			}  // end else 
		}
		
		else if(property.equals("NUTRITIONAL"))
		{
			propStr = KBconnect.ONT_PREFIX + "isHealthy";
			propValue = KBconnect.ONT_PREFIX + value;
			
			res = KBconnect.RecipeModel.getResource(propValue);
			if (res == null)
				MainGUI.printInfo("---> No nutritional status found!\n");
			else 
			{
				//System.out.println(res);
				oprop = KBconnect.RecipeModel.getObjectProperty(propStr);
				if (oprop != null) 
				{
					statement = KBconnect.RecipeModel.createStatement(individual, oprop, res);
					KBconnect.RecipeModel.add(statement);
				}  // end if
			}  // end else 
		}
		
		else if(property.equals("ALLERGY"))
		{
			propStr = KBconnect.ONT_PREFIX + "hasAllergy";
			propValue = KBconnect.ONT_PREFIX + value;
			
			res = KBconnect.RecipeModel.getResource(propValue);
			if (res == null)
				MainGUI.printInfo("---> No allergy setting found!\n");
			else 
			{
				//System.out.println(res);
				oprop = KBconnect.RecipeModel.getObjectProperty(propStr);
				if (oprop != null) 
				{
					statement = KBconnect.RecipeModel.createStatement(individual, oprop, res);
					KBconnect.RecipeModel.add(statement);
				}  // end if
			}  // end else 
		}
		
		else if(property.equals("DIFFICULTY"))
		{
			propStr = KBconnect.ONT_PREFIX + "hasDifficulty";
			propValue = KBconnect.ONT_PREFIX + value;
			
			res = KBconnect.RecipeModel.getResource(propValue);
			if (res == null)
				MainGUI.printInfo("---> No difficulty setting found!\n");
			else 
			{
				//System.out.println(res);
				oprop = KBconnect.RecipeModel.getObjectProperty(propStr);
				if (oprop != null) 
				{
					statement = KBconnect.RecipeModel.createStatement(individual, oprop, res);
					KBconnect.RecipeModel.add(statement);
				}  // end if
			}  // end else 
		}
		
		else if(property.equals("USES"))
		{
			propStr = KBconnect.ONT_PREFIX + "uses";
			propValue = KBconnect.ONT_PREFIX + value;
			
			res = KBconnect.RecipeModel.getResource(propValue);
			if (res == null)
				MainGUI.printInfo("---> No appliances setting found!\n");
			else 
			{
				//System.out.println(res);
				oprop = KBconnect.RecipeModel.getObjectProperty(propStr);
				if (oprop != null) 
				{
					statement = KBconnect.RecipeModel.createStatement(individual, oprop, res);
					KBconnect.RecipeModel.add(statement);
				}  // end if
			}  // end else 
		}
		
		else if(property.equals("TAKES"))
		{
			// add name (datatype property) for recipe
			propStr = KBconnect.ONT_PREFIX + "takes";
			//System.out.println(propStr);
			prop = KBconnect.RecipeModel.getDatatypeProperty(propStr);
			//System.out.println(prop);
			
			if (prop != null)  
			{
				propLiteral = KBconnect.RecipeModel.createTypedLiteral(value, XSDDatatype.XSDstring);
				//System.out.println(propLiteral);
				statement = KBconnect.RecipeModel.createLiteralStatement(individual, prop, propLiteral);
				KBconnect.RecipeModel.add(statement);
			}  // end if
		}
		
		else if(property.equals("OTHERNAME"))
		{
			// add name (datatype property) for recipe
			propStr = KBconnect.ONT_PREFIX + "otherName";
			//System.out.println(propStr);
			prop = KBconnect.RecipeModel.getDatatypeProperty(propStr);
			//System.out.println(prop);
			
			if (prop != null)  
			{
				propLiteral = KBconnect.RecipeModel.createTypedLiteral(value, XSDDatatype.XSDstring);
				//System.out.println(propLiteral);
				statement = KBconnect.RecipeModel.createLiteralStatement(individual, prop, propLiteral);
				KBconnect.RecipeModel.add(statement);
			}  // end if
		}
		
		else if(property.equals("TOTALCALORIES"))
		{
			// add name (datatype property) for recipe
			propStr = KBconnect.ONT_PREFIX + "totalCalories";
			//System.out.println(propStr);
			prop = KBconnect.RecipeModel.getDatatypeProperty(propStr);
			//System.out.println(prop);
			
			if (prop != null)  
			{
				propLiteral = KBconnect.RecipeModel.createTypedLiteral(value, XSDDatatype.XSDstring);
				//System.out.println(propLiteral);
				statement = KBconnect.RecipeModel.createLiteralStatement(individual, prop, propLiteral);
				KBconnect.RecipeModel.add(statement);
			}  // end if
		}
		
		else if(property.equals("OCCASION"))
		{
			// add name (datatype property) for recipe
			propStr = KBconnect.ONT_PREFIX + "occasion";
			//System.out.println(propStr);
			prop = KBconnect.RecipeModel.getDatatypeProperty(propStr);
			//System.out.println(prop);
			
			if (prop != null)  
			{
				propLiteral = KBconnect.RecipeModel.createTypedLiteral(value, XSDDatatype.XSDstring);
				//System.out.println(propLiteral);
				statement = KBconnect.RecipeModel.createLiteralStatement(individual, prop, propLiteral);
				KBconnect.RecipeModel.add(statement);
			}  // end if
		}
		
		else
		{
			MainGUI.printInfo("---> Invalid command found in input file.\n");
		}
	}
}   // end class KBinterface

