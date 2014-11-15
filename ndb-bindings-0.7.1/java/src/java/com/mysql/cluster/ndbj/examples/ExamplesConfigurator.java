package com.mysql.cluster.ndbj.examples;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.net.Authenticator;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.PasswordAuthentication;
import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;

import com.mysql.cluster.ndbj.Ndb;
import com.mysql.cluster.ndbj.NdbApiException;
import com.mysql.cluster.ndbj.NdbClusterConnection;
import com.mysql.cluster.ndbj.NdbTransaction;

public class ExamplesConfigurator 
{
	protected static String CONFIG_FILENAME="/home/jdowling/ndbj-java/ndbapi/test/ndbj.props";
	public static String MYSQLD_HOST="";
	public static String CONNECTSTRING="";
	public static String DB_NAME="test";
	public static String TABLE_NAME="";
	protected static String LIB_PATH = ""; 
	public static String USERNAME  = "root";
	public static String PASSWORD = "";
	protected static String CLUSTER_LOG="";
	protected static final int MAX_NUM_TRANSACTIONS=4;
	public static Connection MYSQL_CONN=null;
	
	/**
	 * HTTP Proxy Settings 
	 */
    protected static String proxyHost = null; 
    protected static int port         = 8080;
	protected static String proxyUsername=null;
	protected static String proxyPassword=null;

	protected static NdbClusterConnection conn=null;
	protected static Ndb ndb=null;
	protected static NdbTransaction trans=null;

	public static void DropAndCreateTable(String tableName, String schemaDefinition)
		throws NdbApiException {
		TABLE_NAME=tableName;

    	      /**
	       *  Connect to mysql server
	       */
      String dBaseName = "jdbc:mysql://"+MYSQLD_HOST+"/" +DB_NAME;

      
      try {
        	Class.forName("org.gjt.mm.mysql.Driver").newInstance();
        }
        catch (Exception e) {
          	System.err.println("Error loading driver: " + e.toString());
          	System.exit(-1);
          }
      
      try {
      	MYSQL_CONN = DriverManager.getConnection(dBaseName, USERNAME, PASSWORD);
          Statement stmt = MYSQL_CONN.createStatement();
          String dropTable = "DROP TABLE IF EXISTS " + TABLE_NAME;
          String schemaDefinitionStr ="CREATE TABLE IF NOT EXISTS `"
        	  + TABLE_NAME + "` " + schemaDefinition;
          System.out.println(schemaDefinitionStr );
          stmt.execute(dropTable);
          stmt.execute(schemaDefinitionStr);
          stmt.close();
      } catch (SQLException e) {
          System.err.println(e.toString());
          System.exit(-1);
      }
      catch (Exception e) {
        	System.err.println("Couldn't connect to mysql server with DB_NAME="+dBaseName+", uname="+USERNAME+", pwd="+PASSWORD);
        	System.exit(-1);
        }
      finally {
    	  try {
    		  if (MYSQL_CONN != null)
    			  MYSQL_CONN.close();
    	  }
	      catch (SQLException e) {
	          System.err.println(e.toString());
	          System.exit(-1);
	      }
      }

	}
	
	
	static public void loadParamsFromPropertiesFile(String configFileName) 
	throws NdbApiException 
	{
		
		CONFIG_FILENAME=configFileName;

		/**
		 * Set environment variables
		 */

		File f =null;
		try {
			InputStream in=null;
			f = new File(CONFIG_FILENAME);
			if (f.exists()){
				if (f.canRead()){
					in = new FileInputStream(f);
					}
				else {
					System.err.println("Could not open " + CONFIG_FILENAME);
					System.exit(-1);
					}
				}
			else {
				System.err.println("Could not find " + CONFIG_FILENAME);
				System.exit(-1);
				}
		    loadProperties(in);

		}
		catch (java.io.FileNotFoundException e) {
			System.err.println(e.getMessage());
		}
		catch (NdbApiException e) {
			System.out.print(e.getStackTrace());
			System.err.println(e.getMessage());		
		}

	}

	
	/**
	 * Loads parameters from a local file ("config/crl.props") 
	 * Can be overriden - e.g., getting the parameters from a central server	
	 * @throws CRLParamsException
	 */
	public void loadParamsFromURL(String url) throws NdbApiException {
		try {
			URL server = new URL(url);	
			
			if (proxyUsername != null && proxyPassword != null) {
				Authenticator.setDefault(new SimpleAuthenticator(
						proxyUsername,proxyPassword));
//				if (proxy != null) {
//					dnsWorkAround();
//				}
			}
			
			Properties systemProperties = System.getProperties();
			if (proxyHost != null) {
				systemProperties.setProperty("http.proxyHost",proxyHost);
				systemProperties.setProperty("http.proxyPort",Integer.toString(port));
			}			
			HttpURLConnection connection = (
				    HttpURLConnection)server.openConnection();
			connection.connect();
			
			InputStream in = connection.getInputStream();
			// set up new properties object from file "myProperties.txt"
						
			BufferedInputStream bufIn= new BufferedInputStream(in);
					    
		    Properties p = new Properties(System.getProperties());
		    p.load(bufIn);

	        System.setProperties(p);	 
	        System.getProperties().list(System.out);
	        
		} 
		catch (MalformedURLException e) {
			throw new NdbApiException(e.toString());
		}
		catch (IOException e) {
			throw new NdbApiException(e.toString());
		}
		catch (NullPointerException e) {
			throw new NdbApiException(e.toString());
		}
		catch (IllegalArgumentException e) {
			throw new NdbApiException(e.toString());
		}
		catch (SecurityException e) {
			throw new NdbApiException(e.toString());
		}
	}	

	
	public void setUp() throws Exception {
	      /**
	       *  Connect to cluster, done once for each Application
	       */

        try {

        	conn = NdbClusterConnection.create(CONNECTSTRING);
		    conn.connect(5,3,true);
		    conn.waitUntilReady(30,0);
		    /**
	         *  Get a connection to a NDB object that is used to create transactions.
	         *  We're going to initialise the ndb object with more than the default
	         *  number of transactions, 12. 
	         */
		    ndb = conn.createNdb(ExamplesConfigurator.DB_NAME,12);		    

        }
        catch (NdbApiException e) {
			System.err.println("failed initialisation");
			System.exit(-1);
        }
	}

	public void close() throws Exception {

	      String dBaseName = "jdbc:mysql://"+MYSQLD_HOST+"/" +DB_NAME;
		
	      try {
	        	Class.forName("org.gjt.mm.mysql.Driver").newInstance();
	        }
	        catch (Exception e) {
	          	System.err.println("Error loading driver: " + e.toString());
	          	System.exit(-1);
	          }
	      
	      try {
	        	MYSQL_CONN = DriverManager.getConnection(dBaseName, USERNAME, PASSWORD);
	            Statement stmt = MYSQL_CONN.createStatement();
	            String dropTable = "DROP TABLE IF EXISTS " + TABLE_NAME;
	            stmt.execute(dropTable);
	            stmt.close();
	        } catch (SQLException e) {
	            System.err.println(e.toString());
	            System.exit(-1);
	        }
	        catch (Exception e) {
	          	System.err.println("Couldn't connect to mysql server with DB_NAME="+dBaseName+", uname="+USERNAME+", pwd="+PASSWORD);
	          	System.exit(-1);
	          }
	        finally {
	      	  try {
	      		  if (MYSQL_CONN != null)
	      			  MYSQL_CONN.close();
	      	  }
	  	      catch (SQLException e) {
	  	          System.err.println(e.toString());
	  	          System.exit(-1);
	  	      }		
	        }

	        if (ndb != null)
				ndb.close();
			if (conn != null)
				conn.close();
			

	}
	
	public static void setHttpProxy(String hostname, int portNumber, String username, String password) {
		proxyHost=hostname;
		port=portNumber;
		proxyUsername=username;
		proxyPassword=password;
	}

	protected static void loadProperties(InputStream in) 
	throws NdbApiException {

		boolean libPathSet=true;
		Properties p = new Properties(System.getProperties());

		try {
			p.load(in);
	
			// set the system properties
	        System.setProperties(p);
	
	        ExamplesConfigurator.MYSQLD_HOST = System.getProperty("ndbj.mysqld");
			if (ExamplesConfigurator.MYSQLD_HOST == null) {
				System.out.println("ndbj.mysql not defined in configuration file: + " + CONFIG_FILENAME);		
			}
	
			ExamplesConfigurator.CONNECTSTRING= System.getProperty("ndbj.connectstring");
			if (ExamplesConfigurator.CONNECTSTRING == null) {
				System.out.println("ndbj.connectstring not defined in configuration file: + " + CONFIG_FILENAME);
			}
	
			ExamplesConfigurator.DB_NAME= System.getProperty("ndbj.testDatabase");
			if (ExamplesConfigurator.DB_NAME == null) {
				System.out.println("ndbj.testDatabase was empty. Setting it to 'test' db.");
			}

			ExamplesConfigurator.LIB_PATH= System.getProperty("ndbj.libpath");
			if (ExamplesConfigurator.LIB_PATH == null) {
				libPathSet=false;				
			}
	
			ExamplesConfigurator.USERNAME = System.getProperty("ndbj.username");
			if (ExamplesConfigurator.USERNAME == null) {
				System.out.println("ndbj.username was empty. Setting it to 'root'.");
			}
	
			ExamplesConfigurator.PASSWORD = System.getProperty("ndbj.password");
			if (ExamplesConfigurator.PASSWORD == null) {
				System.out.println("ndbj.password was empty. Setting it to empty string ''");
			}
	
			ExamplesConfigurator.CLUSTER_LOG= System.getProperty("ndbj.clusterLog");
		}
		catch (java.io.IOException e) {
			throw new NdbApiException(e.getMessage());
			}
		catch (NullPointerException e) {
			throw new NdbApiException(e.getMessage());			
			}
		catch (IllegalArgumentException e) {
			throw new NdbApiException(e.getMessage());
		}
		catch (SecurityException e) {
			throw new NdbApiException(e.getMessage());	
		}

		if (libPathSet == true) {
			Class<ClassLoader> clazz = ClassLoader.class;
		    Field field;
		    boolean accessible;
		    try {
			    field = clazz.getDeclaredField("sys_paths");
			    accessible = field.isAccessible();
			    if (!accessible) {
			    	field.setAccessible(true);
			    }
			    field.set(clazz, null);
			    System.setProperty("java.library.path", LIB_PATH);
		    }
		    catch (NoSuchFieldException e) {
		        throw new NdbApiException(e.getMessage());
		    }
		    catch (IllegalAccessException e) {
		    	throw new NdbApiException(e.getMessage());
		    }
		}
	}
	private class SimpleAuthenticator
	   extends Authenticator
	{
	   private String username,
	                  password;
	                     
	   public SimpleAuthenticator(String username,String password)
	   {
	      this.username = username;
	      this.password = password;
	   }
	   
	   @Override
	   protected PasswordAuthentication getPasswordAuthentication()
	   {
	      return new PasswordAuthentication(
	             username,password.toCharArray());
	   }

	}	
}
