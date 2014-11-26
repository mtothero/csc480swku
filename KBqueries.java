/********************************************************************/
/*                                                                  */
/* Author: Lisa Frye                                                */
/* Date: Jan 2011                                                   */
/* Filename: KBqueries.java                                         */
/*                                                                  */
/* Description: This program will query the ontology KB using Jena  */
/*              and SPARQL.                                         */
/* API: this program uses the Jena ontology API.                    */
/*                                                                  */
/********************************************************************/

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
//import org.mindswap.pellet.jena.PelletQueryExecution;
import com.clarkparsia.pellet.sparqldl.jena.SparqlDLExecutionFactory;

// general imports
import java.lang.*;
import java.io.*;
import java.util.*;
import java.net.*;
import java.text.DecimalFormat;


public class KBqueries {

           
  /**************************************************************************/
  /*****                                                                *****/
  /*****                 QUERY THE KNOWLEDGE BASE                       *****/
  /*****                                                                *****/
  /**************************************************************************/
  public static void queryKB() {
      
      try {      
	  
	  String queryStr;
	  
	  
	  // find all Teams
	  queryStr = 
	      "PREFIX Recipe: " +
	      "<" + KButility.URL_PREFIX + "Recipe.owl#> " +
	      "PREFIX xsd: <http://www.w3.org/2001/XMLSchema#> " +
	      "PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#> " +
	      "SELECT * { " +
	      "  ?team rdf:type Recipe:Team " +
	      "}";
	  
	  /************ Execute the query ***********/
	  System.out.println("******* Find all Team instances");
	  KButility.executeQuery(queryStr, System.out, System.out, false);


	  // find all Recipe Teams
	  queryStr = 
	      "PREFIX Recipe: " +
	      "<" + KButility.URL_PREFIX + "Recipe.owl#> " +
	      "PREFIX xsd: <http://www.w3.org/2001/XMLSchema#> " +
	      "PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#> " +
	      "SELECT * { " +
	      "  ?team rdf:type Recipe:RecipeTeam " +
	      "}";
	  
	  /************ Execute the query ***********/
	  System.out.println("******* Find all RecipeTeam instances");
	  KButility.executeQuery(queryStr, System.out, System.out, false);


	  // find all Major League Recipe Teams
	  queryStr = 
	      "PREFIX Recipe: " +
	      "<" + KButility.URL_PREFIX + "Recipe.owl#> " +
	      "PREFIX xsd: <http://www.w3.org/2001/XMLSchema#> " +
	      "PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#> " +
	      "SELECT * { " +
	      "  ?team rdf:type Recipe:MajorLeagueRecipeTeam " +
	      "}";
	  
	  /************ Execute the query ***********/
	  System.out.println("******* Find all MajorLeagueRecipeTeam instances");
	  KButility.executeQuery(queryStr, System.out, System.out, false);


	  // find all Players
	  queryStr = 
	      "PREFIX Recipe: " +
	      "<" + KButility.URL_PREFIX + "Recipe.owl#> " +
	      "PREFIX xsd: <http://www.w3.org/2001/XMLSchema#> " +
	      "PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#> " +
	      "SELECT * { " +
	      "  ?player rdf:type Recipe:Player " +
	      "}";
	  
	  /************ Execute the query ***********/
	  System.out.println("******* Find all Player instances");
	  KButility.executeQuery(queryStr, System.out, System.out, false);


	  // find all Recipe Players
	  queryStr = 
	      "PREFIX Recipe: " +
	      "<" + KButility.URL_PREFIX + "Recipe.owl#> " +
	      "PREFIX xsd: <http://www.w3.org/2001/XMLSchema#> " +
	      "PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#> " +
	      "SELECT * { " +
	      "  ?player rdf:type Recipe:RecipePlayer " +
	      "}";
	  
	  /************ Execute the query ***********/
	  System.out.println("******* Find all RecipePlayer instances");
	  KButility.executeQuery(queryStr, System.out, System.out, false);


	  // find all Major League Recipe Players
	  queryStr = 
	      "PREFIX Recipe: " +
	      "<" + KButility.URL_PREFIX + "Recipe.owl#> " +
	      "PREFIX xsd: <http://www.w3.org/2001/XMLSchema#> " +
	      "PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#> " +
	      "SELECT * { " +
	      "  ?player rdf:type Recipe:MajorLeagueRecipePlayer " +
	      "}";
	  
	  /************ Execute the query ***********/
	  System.out.println("******* Find all MajorLeagueRecipePlayer instances");
	  KButility.executeQuery(queryStr, System.out, System.out, false);
	  
     	
      }  // end initial try
      
      catch(Exception e) {
	  e.printStackTrace();
      }  // end catch
      
  }   // end function queryKB
    
    
}   // end class KBqueries

