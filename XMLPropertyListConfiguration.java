package com.paperlit.reader.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Stack;

import org.xmlpull.v1.XmlPullParser;

    /**
    * This class will parse a plist xml file and store the contents in a
    * hierarchical HashMap of <String, Object> tuples, where the Object value
    * could be another HashMap, an ArrayList, String or Integer value.
    *
    * Use the getConfiguration methods to retrieve the values from the parsed plist file.
    *
    * The key names for nested dictionary references must be of the form :
    *       Dict1KeyName.Dict2KeyName.ElementKeyName
    *
    * @author akoscz
    *
    */
    public class XMLPropertyListConfiguration {
       
    	public static String[] xmlHeaders = new String[] {
    		 "<?xml version=\"1.0\" encoding=\"UTF-8\"?>"
        	,"<!DOCTYPE plist PUBLIC \"-//Apple//DTD PLIST 1.0//EN\" \"http://www.apple.com/DTDs/PropertyList-1.0.dtd\">"
        	,"<plist version=\"1.0\">"
    	};
    	public static String[] xmlFooters = new String[] {
        	"</plist>"
    	};
    	
       /**
        * The nested (hierarchical) HashMap which holds our key-value pairs of our plist file.
        */
       protected HashMap<String, Object> mPlistHashMap;

       /**
        * Constructor.
        * Parse a plist file from the given InputStream.
        *
        * @param inputStream The InputStream which has the bytes of the plist file we need to parse.
        */
       public XMLPropertyListConfiguration(InputStream inputStream) {
          mPlistHashMap = new HashMap<String, Object>();
          parse(inputStream);
       }

       /**
        * Get an String configuration value for the given key.
        *
        * @param keyName The name of the key to look up in the configuration dictionary.
        * @return The String value of the specified key.
        */
       public String getConfiguration(String keyName) {
          return (String)getConfigurationObject(keyName);
       }

       /**
        * Get a String configuration value for the given key.
        * If there is no value for the given key, then return the default value.
        * @param keyName The name of the key to look up in the configuration dictionary.
        * @param defaultValue The default value to return if they key has no associated value.
        * @return The String value of the specified key, or defaultValue if the value for keyName is null.
        */
       public String getConfigurationWithDefault(String keyName, String defaultValue) {
          String value = getConfiguration(keyName);
          if(value == null) {
             return defaultValue;
          }
          
          return value;
       }

       /**
        * Get an Integer configuration value for the given key.
        *
        * @param keyName The name of the key to look up in the configuration dictionary.
        * @return The Integer value of the specified key.
        */
       public Integer getConfigurationInteger(String keyName) {
          return (Integer)getConfigurationObject(keyName);
       }

       /**
        * Get an Integer configuration value for the given key.
        * If there is no value for the given key, then return the default value.
        * @param keyName The name of the key to look up in the configuration dictionary.
        * @param defaultValue The default value to return if they key has no associated value.
        * @return The Integer value of the specified key, or defaultValue if the value for keyName is null.
        */
       public Integer getConfigurationIntegerWithDefault(String keyName, Integer defaultValue) {
          Integer value = getConfigurationInteger(keyName);
          if(value == null) {
             return defaultValue;
          }
          
          return value;
       }
       
       public boolean parsingArray( Stack<Object> stack )
       {
    	   if ( stack == null )
    		   return false;
    	   else if ( stack.isEmpty() )
    		   return false;
    	   else 
    		   return stack.peek() instanceof ArrayList;
       }
       
       /**
        * Utility method which uses a XmlPullParser to iterate through the XML elements
        * and build up a hierarchical HashMap representing the key-value pairs of the
        * plist configuration file.
        *
        * @param inputStream The InputStream which contains the plist XML file.
        */
       @SuppressWarnings("unchecked")
       public void parse(InputStream inputStream) {
          //XmlPullParser parser = Xml.newPullParser();
    	   org.kxml2.io.KXmlParser parser = new org.kxml2.io.KXmlParser();
          try {
            parser.setInput(inputStream, null);
        	
             int eventType = parser.getEventType();
             
             boolean done = false;
              
             String name = null;
             String key = null;
             
             Stack<Object> 				stack = new Stack<Object>();
             HashMap<String, Object> 	dict = null;
             ArrayList<Object> 			array = null;
             
             while (eventType != XmlPullParser.END_DOCUMENT && !done) {
                switch(eventType) {
                   case XmlPullParser.START_DOCUMENT:
                      break;
                      
                   case XmlPullParser.START_TAG:
                      name = parser.getName();
//                      System.out.println( "Name is " + name );
                      if(name.equalsIgnoreCase("dict")) 
                      {
                         // root dict element
                         if(key == null) 
                         {
                            mPlistHashMap.clear();
                            dict = mPlistHashMap;
                         }
                         else 
                         {
                             HashMap<String, Object> childDict = new HashMap<String, Object>();
                        	 if( parsingArray(stack) ) 
                                 array.add(childDict);
                        	 else
 	                             dict.put(key, childDict);

                             dict = childDict;
                         }
                         stack.push(dict);
                      }
                      else if(name.equalsIgnoreCase("key")) 
                         key = parser.nextText();
                      else if(name.equalsIgnoreCase("integer")) 
                      {
                          if( parsingArray( stack ) )
                             array.add(new Integer(parser.nextText()));
                           else
                             dict.put(key, new Integer(parser.nextText()));
                       }
                      else if(name.equalsIgnoreCase("true") || name.equalsIgnoreCase("false")) 
                      {
                          if( parsingArray( stack ) )
                             array.add(new Boolean(name));
                          else
                             dict.put(key, new Boolean(name));
                       }
                      else if(name.equalsIgnoreCase("string")) 
                      {
                         if( parsingArray( stack ) )
                            array.add(parser.nextText());
                         else
                            dict.put(key, parser.nextText());
                      }
                      else if(name.equalsIgnoreCase("array")) 
                      {
                         ArrayList<Object> childArray = new ArrayList<Object>();
                         
                         if( parsingArray( stack ) )
                        	 array.add( childArray );
                         else
                        	 dict.put(key, childArray);
                         
                         stack.push(childArray);
                         array = childArray;
                      }
                      break;
                      
                   case XmlPullParser.END_TAG:
                      name = parser.getName();       
                      done = name.equalsIgnoreCase("plist");
                      if(name.equalsIgnoreCase("dict") || name.equalsIgnoreCase("array") ) 
                      {
                    	  if ( ! stack.isEmpty() )
                    	  {
							 stack.pop();
							 if( parsingArray( stack ) )
							    array = (ArrayList<Object>) stack.peek();
							 else if ( ! stack.isEmpty() )
								dict = (HashMap<String,Object>) stack.peek();
                		  }
                      }                         
                      break;
                      
                   case XmlPullParser.END_DOCUMENT:
                      //Log.d(TAG, "END_DOCUMENT");
                      break;
       
                }
                eventType = parser.next();
             }
          }catch (Exception ex){
             ex.printStackTrace();
          }      
       }


       /**
        * Utility method which tokenizes the given keyName using
        * the "." delimiter and then looks up each token in the
        * configuration dictionary.  If the token key points to a
        * dictionary then it proceeds to the next token key and
        * looks up value of the token key in the dictionary it found
        * from the previous token key.
        *
        * @param keyName The fully qualified key name.
        * @return The Object value associated with the given key, or null if the key does not exist.
        */
       @SuppressWarnings("unchecked")
       public Object getConfigurationObject(String keyName) {
          String[] tokens = keyName.split("\\.");
       
          if(tokens.length > 1) {
             HashMap<String,Object> dict = mPlistHashMap;
             Object obj;
             for(int i = 0; i < tokens.length; i++) {
                obj = dict.get(tokens[i]);
                if(obj instanceof HashMap<?, ?>) {
                   dict = (HashMap<String, Object>) obj;
                   continue;
                }
                return obj;
             }
          }
          
          return mPlistHashMap.get(keyName);
       }
       
       public HashMap<String,Object> getPlist()
       {
    	   return mPlistHashMap;
       }
              
       private String makeIndent( int indentLevel )
       {
    	   String s = "";
    	   for ( int i = 0; i < indentLevel; i++ )
    		   s += "\t";
    	   return s;
       }
       
       @SuppressWarnings("unchecked")
       private void saveObject( PrintStream fos, Object obj, int indentLevel  )
       {
    	  	if ( obj instanceof Boolean )
    	  		saveBoolean( fos, (Boolean) obj, indentLevel );
     	  	else if ( obj instanceof String )
     	  		saveString( fos, (String) obj, indentLevel );
     	  	else if ( obj instanceof Integer )
     	  		saveInteger( fos, (Integer) obj, indentLevel );
     	  	else if ( obj instanceof ArrayList )
     	  		saveArray( fos, (ArrayList) obj, indentLevel );
     	  	else if ( obj instanceof HashMap )
     	  		saveDictionary( fos, (HashMap) obj, indentLevel );
       }
       
       private void saveString( PrintStream fos, String obj, int indentLevel )
       {
    	   fos.println( makeIndent(indentLevel) + "<string>" + obj + "</string>" );  
       }
       
       private void saveBoolean( PrintStream fos, Boolean obj, int indentLevel )
       {
    	   fos.println( makeIndent(indentLevel) + "<" + obj + "/>" );  
       }
       
       private void saveInteger( PrintStream fos, Integer obj, int indentLevel )
       {
    	   fos.println( makeIndent(indentLevel) + "<integer>" + obj + "</integer>" );  
       }
       
       @SuppressWarnings("unchecked")
       private void saveArray( PrintStream fos, ArrayList array, int indentLevel )
       {
    	   String indent = makeIndent(indentLevel);
    	  fos.println( indent + "<array>" );
    	  Iterator it = array.iterator();
    	  while( it.hasNext() ) 
    	  {
    		  Object obj = (Object) it.next();
    		  saveObject( fos, obj, indentLevel + 1 );
    	  }
    	  fos.println( indent + "</array>"); 
       }
       
       
       private void saveKeyValuePair( PrintStream fos, String key, Object obj, int indentLevel )
       {
    	   String indent = makeIndent(indentLevel);
     	  	fos.println( indent + "<key>" + key + "</key>" );
     	  	saveObject( fos, obj, indentLevel );    	   
       }
       
       @SuppressWarnings("unchecked")
       private void saveDictionary( PrintStream fos, HashMap dict, int indentLevel )
       {
    	   String indent = makeIndent(indentLevel);
    	  fos.println( indent + "<dict>" );
    	  Iterator it = dict.entrySet().iterator();
    	  while( it.hasNext() ) 
    	  {
    		  Map.Entry entry = (Map.Entry) it.next();
    		  String key = (String) entry.getKey();
    		  Object obj =  entry.getValue();
    		  saveKeyValuePair( fos, key, obj, indentLevel + 1 );
    	  }
    	  fos.println( indent + "</dict>"); 
       }
       
       public boolean save( String name )
       {
    	  PrintStream fos = null;
    	  try {
			File plistFile = new File( name );  
			fos = new PrintStream(new FileOutputStream( plistFile ));
			for ( int i = 0; i < xmlHeaders.length; i++ )
				fos.println( xmlHeaders[i] );
			saveDictionary( fos, mPlistHashMap, 0 );
			for ( int i = 0; i < xmlFooters.length; i++ )
				fos.println( xmlFooters[i] );
			
			return true;
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			return false;
		}
		finally {
			fos.flush();
			fos.close();
		}
       }

    @SuppressWarnings("unchecked")
	public static void main( String args[] )
	{
		try {
//			System.out.println( System.getProperty( "user.dir" ) );
			File plistFile = new File( "Content.plist");
			InputStream fis = new FileInputStream( plistFile );
			
			XMLPropertyListConfiguration conf = new XMLPropertyListConfiguration( fis );
			HashMap plist = conf.getPlist();
//            System.out.println( "plist has " + plist.size() + " elements" );

			Iterator it = plist.keySet().iterator();
			while( it.hasNext() )
			{
				String key = (String) it.next();
				Object hm = plist.get( key );
				// System.out.println( "Key " + key + " object is:" + hm.getClass().getName());
			}			
			
			Boolean b = (Boolean) conf.getConfigurationObject( "customSort" );
//			System.out.println( "customsort is : " + b );
			
			b = (Boolean) conf.getConfigurationObject( "defaultIngredients.aglio" );
//			System.out.println( "defaultIngredients.aglio is : " + b );
			
			conf.save( "prefs1.plist");
			
		} catch (Exception e) {
			e.printStackTrace();
		}	
	}
}

    