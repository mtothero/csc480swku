/****************************************************************************************************/
/*
/* Modifier: Matt Tothero
/* Author: Dr. Frye
/* Modification Date: November 20th, 2014
/* Creation Date: Mar 2013 
/* Course: CSC480 010
/* Professor Name: Dr. Frye
/* Filename: KButility.java 
/* Purpose: This file contains utility function for use with an ontology KB.                                   
/* 			API: this program uses the Jena ontology API.
/* 
/******************************************************************************************************/

// imports for Jena API
import com.hp.hpl.jena.update.GraphStore;
import com.hp.hpl.jena.update.GraphStoreFactory;
import com.hp.hpl.jena.update.UpdateAction;
import com.hp.hpl.jena.update.UpdateFactory;
import com.hp.hpl.jena.update.UpdateProcessor;
import com.hp.hpl.jena.update.UpdateRequest;
import com.hp.hpl.jena.update.UpdateExecutionFactory;
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
import java.text.DecimalFormat;


public class KButility {
    
    public static final String URL_PREFIX = 
	"http://bill.kutztown.edu/~mtoth190/CSC480/";
    
    private static DecimalFormat decVal = new DecimalFormat ("#0.0000000");
    
    /**************************************************************************/
    /*****                                                                *****/
    /*****     EXECUTE AN UPDATE QUERY AGAINST THE KNOWLEDGE BASE         *****/
    /*****                                                                *****/
    /**************************************************************************/
	public static void execUpdQuery(String queryStr, PrintStream outputFile, Boolean printQuery) 
	{
		try 
		{
			if (printQuery) 
			{
			  outputFile.println("Query executed: =================>");
			  outputFile.println(queryStr);
			}  // end if

			GraphStore graphStore = GraphStoreFactory.create();
			graphStore.setDefaultGraph(KBconnect.RecipeModel.getGraph());

			//Execution the query
			UpdateRequest updateRequest = UpdateFactory.create(queryStr);
			UpdateProcessor uExecFactory = 
			UpdateExecutionFactory.create(updateRequest, graphStore);

			uExecFactory.execute();

			outputFile.println();
		}  // end initial try
	  
		catch (Exception e) 
		{
			e.printStackTrace();
		}  // end catch
	  
	}   // end function execUpdQuery         

	/**************************************************************************/
	/*****                                                                *****/
	/*****         EXECUTE A QUERY AGAINST THE KNOWLEDGE BASE             *****/
	/*****                                                                *****/
	/**************************************************************************/
	public static double executeQuery(String queryStr, OutputStream outstream, PrintStream outputFile, Boolean printQuery) 
	{
		try 
		{
			if (printQuery) 
			{
				outputFile.println("Query executed: =================>");
				outputFile.println(queryStr);
			}  // end if
			
			Query query = QueryFactory.create(queryStr, Syntax.syntaxARQ);
			
			// Execute the query and obtain results
			QueryExecution queryExec = QueryExecutionFactory.create(query, KBconnect.RecipeModel);    
			
			ResultSet results = queryExec.execSelect();

			// Output query results
			ResultSetFormatter.out(outstream, results, query);

			// Free up the resources used running the query
			queryExec.close();
			
			outputFile.println();
		}  // end initial try

		catch(Exception e) 
		{
			e.printStackTrace();
		}  // end catch

		return 0;
	}   // end function executeQuery
}   // end class KButility

