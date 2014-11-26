/****************************************************************************************************/
/*
/* Modifier: Matt Tothero
/* Author: Dr. Frye
/* Modification Date: November 20th, 2014
/* Creation Date: Mar 2013 
/* Course: CSC480 010
/* Professor Name: Dr. Frye
/* Filename: KBconnect.java 
/* Purpose: This program will connect to an ontology reasoner   
/*          and create the appropriate models to access the     
/*          knowledge base.                                     
/* 			API: this program uses the Jena ontology API.
/* 
/*			The Recipe Ontology uses some constructs that aren't available in Lite. Full is not needed.
/*			DL will suffice for this project. 
/******************************************************************************************************/

// imports for Jena API
import com.hp.hpl.jena.ontology.OntModel;
import com.hp.hpl.jena.ontology.OntModelSpec;
import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.InfModel;
import com.hp.hpl.jena.rdf.model.ModelFactory;
import com.hp.hpl.jena.rdf.model.ModelFactoryBase;
import com.hp.hpl.jena.rdf.model.Resource;
import com.hp.hpl.jena.rdf.model.ModelCon;
import com.hp.hpl.jena.reasoner.Reasoner;
import com.hp.hpl.jena.reasoner.ReasonerRegistry;
import com.hp.hpl.jena.reasoner.ValidityReport;
import com.hp.hpl.jena.update.GraphStore;
import com.hp.hpl.jena.update.GraphStoreFactory;
import org.mindswap.pellet.jena.PelletInfGraph;
import org.mindswap.pellet.jena.PelletReasoner;
import org.mindswap.pellet.jena.PelletReasonerFactory;

// general imports
import java.lang.*;
import java.io.*;
import java.util.*;

public class KBconnect 
{

    public static final String ONTOLOGY = KButility.URL_PREFIX + "RecipeOntology";
	public static final String INSTANCES = KButility.URL_PREFIX + "RecipeInstances";
    public static final String ONT_URL = ONTOLOGY + ".owl";
	public static final String INST_URL = INSTANCES + ".owl";
    public static final String ONT_PREFIX = ONT_URL + "#";
	public static final String INST_PREFIX = INST_URL + "#";
    
    public static OntModel RecipeModel;
    private static Reasoner owlReasoner;
  		
    /**************************************************************************/
    /*****                                                                *****/
    /*****                     CREATE MODEL                               *****/
    /*****                                                                *****/
    /**************************************************************************/
    public static void createModel() 
	{
	
		try 
		{
			
			// MODELS INFORMATION
			// Model contains set of statements
			// InfModel extends Model with inference
			// OntModel extends InfModel with ontology API
			
			// OWL Full with an optimised rule-based reasoner with OWL rules
			// Create model using OWL Reasoner
			RecipeModel = ModelFactory.createOntologyModel(OntModelSpec.OWL_DL_MEM_RULE_INF, null);

		}  // end initial try
		
		catch(Exception e) 
		{
			e.printStackTrace();
		}  // end catch
		
		}   // end function createModel	
    
		/**************************************************************************/
		/*****                                                                *****/
		/*****                 READ ONTOLOGY INTO MODEL                       *****/
		/*****                                                                *****/
		/**************************************************************************/
		public static void readOntology(String ontFile) {
		
		try 
		{	
			RecipeModel.read(ontFile);
		}  // end initial try

		catch(Exception e) 
		{
			e.printStackTrace();
		}  // end catch
		
	  }   // end function readOntology

    /**************************************************************************/
    /*****                                                                *****/
    /*****                 CONNECT TO REASONER                            *****/
    /*****                                                                *****/
    /**************************************************************************/
    /**************************************************************************/
    /*                                                                        */
    /* NOTE: from http://jena.sourceforge.net/inference/index.html            */
    /*     If working with the Ontology API it is not always necessary to     */
    /*     explicitly locate a reasoner. The prebuilt instances of            */
    /*     OntModelSpec provide easy access to the appropriate reasoners to   */
    /*     use for different Ontology configurations.                         */
    /*                                                                        */
    /**************************************************************************/
    public static void connectReasoner() 
	{
		try 
		{
			System.out.println("Connecting to reasoner...");

			// Create an OWL reasoner 
			// does same as the create model code
			owlReasoner = ReasonerRegistry.getOWLReasoner();
			
		}  // end initial try

		catch(Exception e) 
		{
			e.printStackTrace();
		}  
    }   
}  
