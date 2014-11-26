/****************************************************************************************************/
/*
/* Modifier: Matt Tothero
/* Author: Dr. Frye
/* Modification Date: November 26th, 2014
/* Creation Date: Jan 2011  
/* Course: CSC480 010
/* Professor Name: Dr. Frye
/* Filename: KBprint.java
/* Purpose: This program will query the ontology KB and dump the contents of various classes to a file.       
/*          Used for debugging purposes.                   
/* 			API: this program uses the Jena ontology API. 
/* 
/******************************************************************************************************/

// imports for Jena API
import com.hp.hpl.jena.ontology.OntModel;
import com.hp.hpl.jena.ontology.OntModelSpec;
import com.hp.hpl.jena.ontology.OntClass;
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
                            
public class KBprint 
{
    /**************************************************************************/
    /*****                                                                *****/
    /*****                  DUMP THE KNOWLEDGE BASE                       *****/
    /*****                                                                *****/
    /**************************************************************************/
    public static void dumpKB(OutputStream KBoutstream, PrintStream outputFile,OutputStream KBpropsoutstream,PrintStream KBpropsoutputFile) 
	{
		try 
		{   
			MainGUI.printInfo("---> Dumping the KB......\n");
			
			KBpropsoutputFile.println("***********   OBJECT PROPERTIES   ***********");
			dumpObjectProps(KBpropsoutstream, KBpropsoutputFile);
			KBpropsoutputFile.println("******************************************");
			KBpropsoutputFile.println();
			
			outputFile.println("***********************************************");
			outputFile.println("***********   DUMP KNOWLEDGE BASE   ***********");
			outputFile.println("***********************************************");
			
			outputFile.println();
			outputFile.println();
		}  // end initial try
		
		catch(Exception e) 
		{
			e.printStackTrace();
		}  // end catch
    }   // end function dumpKB
    
    /**************************************************************************/
    /*****                                                                *****/
    /*****                    DUMP THE ONTOLOGY MODEL                     *****/
    /*****                                                                *****/
    /**************************************************************************/
    public static void dumpModel(String filePrefix) 
	{        
		OutputStream modelOut = null;
	  
		try 
		{	
			MainGUI.printInfo("---> Writing the ontology model to a file...\n");
			// OutputStream to write output from writeAll
			modelOut = new FileOutputStream(filePrefix + ".modeloutput");
			//modelOut = new ByteArrayOutputStream(filePrefix + ".modeloutput");
			
			// set prefixes
			KBconnect.RecipeModel.setNsPrefix("Recipe", KBconnect.ONT_PREFIX);
			//KBconnect.RecipeModel.writeAll(modelOut, "RDF/XML-ABBREV", null);
			KBconnect.RecipeModel.writeAll(modelOut, "RDF/XML", null);
			//KBconnect.RecipeModel.writeAll(modelOut, "N3", null);
		}
		
		catch (CannotEncodeCharacterException e) 
		{ 
			try 
			{ 
				modelOut.flush();
			} 
			catch (IOException f) 
			{
				MainGUI.printInfo("---> ERROR: flushing model output file.\n");
			}
		}
		catch(IOException e)
		{
			MainGUI.printInfo("---> ERROR: creating modelOut output stream.\n");
		} 
		finally 
		{
			try
			{
				if(modelOut != null) modelOut.close();
			} 
			catch(IOException e)
			{
				MainGUI.printInfo("---> ERROR: closing modelOut output stream.\n");
			}   // end catch
		}   // end finally
	}   // end function dumpModel
  
    /**************************************************************************/
    /*****                                                                *****/
    /*****                    LIST STATEMENTS IN MODEL                    *****/
    /*****                                                                *****/
    /**************************************************************************/
    public static void listModelStatements(String filePrefix) 
	{        
		PrintStream stmtsOut = null;
	
		try 
		{	
			MainGUI.printInfo("---> Sending ontology statements to file...\n");
			// OutputStream to write output from lstStatements
			stmtsOut = new PrintStream(filePrefix + ".stmtsoutput");
			
			// set prefixes
			KBconnect.RecipeModel.setNsPrefix("Recipe", KBconnect.ONT_PREFIX);
			
			// list the statements in the Model
			StmtIterator iter = KBconnect.RecipeModel.listStatements();
			
			// print out the predicate, subject and object of each statement
			while (iter.hasNext()) 
			{
				Statement stmt      = iter.nextStatement();  // get next statement
				Resource  subject   = stmt.getSubject();     // get the subject
				Property  predicate = stmt.getPredicate();   // get the predicate
				RDFNode   object    = stmt.getObject();      // get the object
				
				stmtsOut.print(subject.toString());
				stmtsOut.print(" " + predicate.toString() + " ");
				if (object instanceof Resource) 
				{
					stmtsOut.print(object.toString());
				} 
				else 
				{
					// object is a literal
					stmtsOut.print(" \"" + object.toString() + "\"");
				}
				stmtsOut.println(" .");
			}   // end while 
		}
		catch (Exception e) 
		{
			e.printStackTrace();
		} 
		finally 
		{
			try
			{
				if(stmtsOut != null) stmtsOut.close();
			} 
			catch(Exception e)
			{
				MainGUI.printInfo("---> ERROR: closing stmtsOut output stream\n");
			}   // end catch
		}   // end finally
    }   // end function dumpModel
    
    /**************************************************************************/
    /*****                                                                *****/
    /*****                  DUMP ALL OF THE TRIPLES HERE                  *****/
    /*****                                                                *****/
    /**************************************************************************/
    public static void dumpObjectProps(OutputStream outstream, PrintStream outputFile) 
	{
		try 
		{
			String queryStr;
			
			outputFile.println();
			outputFile.println("***********   Object Properties   ***********");
			outputFile.println("   Prints the Subject - Property - Object");
			queryStr = 
			"PREFIX Recip: " +
			"<" + KButility.URL_PREFIX + "RecipeOntology.owl#> " +
			"PREFIX RecipInst: " +
			"<" + KButility.URL_PREFIX + "RecipeInstances.owl#> " +
			"PREFIX xsd: <http://www.w3.org/2001/XMLSchema#> " +
			"PREFIX rdf: <http://www.w3.org/2000/01/rdf-schema#> " +
			"PREFIX RDF: <http://www.w3.org/1999/02/22-rdf-syntax-ns#> " +
			"PREFIX owl: <http://www.w3.org/2002/07/owl#> " +
			"SELECT ?subject ?predicate ?value " +
			"WHERE{?subject ?predicate ?value  " +
			"}";
			
			/************ Execute the query ***********/
			KButility.executeQuery(queryStr, outstream, outputFile, true);
			
		}  // end initial try
		catch(Exception e) 
		{
			e.printStackTrace();
		} 
    }   
}   // end class KBprint

