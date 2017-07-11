/*
 * Copyright 1999-2004 Carnegie Mellon University.
 * Portions Copyright 2004 Sun Microsystems, Inc.
 * Portions Copyright 2004 Mitsubishi Electric Research Laboratories.
 * All Rights Reserved.  Use is subject to license terms.
 *
 * See the file "license.terms" for information on usage and
 * redistribution of this file, and for a DISCLAIMER OF ALL
 * WARRANTIES.
 *
 */

package demo.sphinx.helloworld;

import edu.cmu.sphinx.frontend.util.Microphone;
import edu.cmu.sphinx.recognizer.Recognizer;
import edu.cmu.sphinx.result.Result;
import edu.cmu.sphinx.util.props.ConfigurationManager;
import edu.cmu.sphinx.util.props.PropertyException;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Scanner;

import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextPane;

import org.omg.SendingContext.RunTime;


/**
 * A simple HelloWorld demo showing a simple speech application 
 * built using Sphinx-4. This application uses the Sphinx-4 endpointer,
 * which automatically segments incoming audio into utterances and silences.
 */
public class HelloWorld {

    /**
     * Main method for running the HelloWorld demo.
     */
    public static void main(String[] args) {
        try 
        {
            URL url;
            if (args.length > 0) 
            {
                url = new File(args[0]).toURI().toURL();
            } 
            else 
            {
                url = HelloWorld.class.getResource("helloworld.config.xml");
            }

            System.out.println("Loading...");

            ConfigurationManager cm = new ConfigurationManager(url);

            Recognizer recognizer = (Recognizer) cm.lookup("recognizer");
	    	Microphone microphone = (Microphone) cm.lookup("microphone");
	    	Process p;
	    	JFrame f= new JFrame();
	    	JTextArea area=new JTextArea(null,10,50);
        	recognizer.allocate();

            
        	if (microphone.startRecording()) 
        	{

        		
        		System.out.println("Start speaking. Say terminate to quit.\n");
        		
        		
        		while (true) 
        		{
                    
        			Result result = recognizer.recognize();
        			String resultText = result.getBestFinalResultNoFiller();
        			
        			/*if(resultText.equalsIgnoreCase("lock"))
        			{
        				p = Runtime.getRuntime().exec("rundll32.exe user32.dll,LockWorkStation");
        				System.out.println("Terminated.");
        				System.exit(0);
        			}*/
        			
        			if(resultText.equalsIgnoreCase("commandprompt"))
        			{
        				p = Runtime.getRuntime().exec("cmd /c start");
        			}
        			
        			
        			else if(resultText.equalsIgnoreCase("search"))
        			{
        				
        				String search = JOptionPane.showInputDialog("Enter the file/folder name you want to search");
        				try{
        					
        					if(search.length()>0)
            				{
        						
            					System.out.println("cmd /c C: & cd Users/Rajat & dir /s *"+search+"* > path.txt");
            					
            					p = Runtime.getRuntime().exec("cmd /c C: & cd Users/Rajat & dir /s *"+search+"* > path.txt");
            					
            					String path="C:/Users/Rajat/path.txt";
            					String contents = new String(Files.readAllBytes(Paths.get(path)));
            					System.out.println(contents);
            					System.out.println("------------");
            					
            					
            					area.setText(contents);
            					JScrollPane scrollPane = new JScrollPane(area);
            					scrollPane.setBorder(BorderFactory.createTitledBorder("Messages"));
            					scrollPane.setViewportView(area);
            					
            					 area.setBounds(10,60, 500,500);  
            				        f.add(area);  
            				        f.add(scrollPane);
            				        f.setSize(600,600);  
            				        f.setLayout(null);  
            				        f.setVisible(true); 
            				      
            				}
        				}
        				catch(Exception e)
        				{
        					e.printStackTrace();
        				}
        			
     	
        				
        			}
        			
        			else if(resultText.equalsIgnoreCase("closewindow"))
        			{
        				System.out.println("closing window..");
        				area.setText(null);
        				f.setVisible(false);
        			}
        			
        			/*else if(resultText.equalsIgnoreCase("erase"))
        			{
        				System.out.println("Erasing..");
        				Process p1 = Runtime.getRuntime().exec("cmd /c C: & cd Users/Rajat & break>path.txt");
        			}*/
        			
        			else if(resultText.equalsIgnoreCase("terminate"))
        			{
        				System.out.println("Terminated.");
        				System.exit(0);
        			}
        			
        			
        			
        			else 
        			{
        				System.out.println("I can't hear what you said.\n");
        			}
        		}
        	} 
	    
        	else 
        	{
        		System.out.println("Cannot start microphone.");
        		recognizer.deallocate();
        		System.exit(1);
        	}
        } 
        
        catch (IOException e) {
            System.err.println("Problem when loading HelloWorld: " + e);
            e.printStackTrace();
        } catch (PropertyException e) {
            System.err.println("Problem configuring HelloWorld: " + e);
            e.printStackTrace();
        } catch (InstantiationException e) {
            System.err.println("Problem creating HelloWorld: " + e);
            e.printStackTrace();
        }
        
    }
}
