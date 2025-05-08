package configuration;

import java.io.File;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;


public class ConfigXML {

	private String configFile = "src/main/resources/config.xml";

	private String businessLogicNode;

	private String businessLogicPort;

	private String businessLogicName;

	private static String dbFilename;

	
	private boolean isDatabaseInitialized;

	
	private boolean businessLogicLocal;

	
	private boolean databaseLocal;

	private String databaseNode;

	private int databasePort;



	private String user;

	private String password;

	private String locale;

	
	private String smtpHost;
	private int smtpPort;
	private boolean smtpAuth;
	private boolean smtpStartTLS;
	private String senderEmail;
	private String senderName;
	private String emailUsername;
	private String emailPassword;

	public String getLocale() {
		return locale;
	}

	public String getSmtpHost() {
		return smtpHost;
	}

	public int getSmtpPort() {
		return smtpPort;
	}

	public boolean isSmtpAuth() {
		return smtpAuth;
	}

	public boolean isSmtpStartTLS() {
		return smtpStartTLS;
	}

	public String getSenderEmail() {
		return senderEmail;
	}

	public String getSenderName() {
		return senderName;
	}

	public String getEmailUsername() {
		return emailUsername;
	}

	public String getEmailPassword() {
		return emailPassword;
	}

	public int getDatabasePort() {
		return databasePort;
	}

	public String getUser() {
		return user;
	}

	public String getPassword() {
		return password;
	}

	public boolean isDatabaseLocal() {
		return databaseLocal;
	}

	public boolean isBusinessLogicLocal() {
		return businessLogicLocal;
	}
	private static ConfigXML theInstance = new ConfigXML();

	private ConfigXML(){

		  try {
			  DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
			  DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
			  Document doc = dBuilder.parse(new File(configFile));
			  doc.getDocumentElement().normalize();

			  NodeList list = doc.getElementsByTagName("config");
			  Element config = (Element) list.item(0); 


				
			  String value= ((Element)config.getElementsByTagName("businessLogic").item(0)).getAttribute("local");
			  businessLogicLocal=value.equals("true");

			  businessLogicNode = getTagValue("businessLogicNode", config);

			  businessLogicPort = getTagValue("businessLogicPort", config);

			  businessLogicName = getTagValue("businessLogicName", config);

			  locale = getTagValue("locale", config);





			  dbFilename = getTagValue("dbFilename", config);

				
			  value= ((Element)config.getElementsByTagName("database").item(0)).getAttribute("local");
			  databaseLocal=value.equals("true");


				
			  String dbOpenValue= ((Element)config.getElementsByTagName("database").item(0)).getAttribute("initialize");
			  isDatabaseInitialized= dbOpenValue.equals("true");;


			  databaseNode = getTagValue("databaseNode", config);

			  databasePort=Integer.parseInt(getTagValue("databasePort", config));

			  user=getTagValue("user", config);

			  password=getTagValue("password", config);

			  
			  try {
				  Element emailElement = (Element) config.getElementsByTagName("email").item(0);
				  if (emailElement != null) {
					  smtpHost = getTagValue("smtpHost", emailElement);
					  smtpPort = Integer.parseInt(getTagValue("smtpPort", emailElement));
					  smtpAuth = Boolean.parseBoolean(getTagValue("smtpAuth", emailElement));
					  smtpStartTLS = Boolean.parseBoolean(getTagValue("smtpStartTLS", emailElement));
					  senderEmail = getTagValue("senderEmail", emailElement);
					  senderName = getTagValue("senderName", emailElement);

					  
					  NodeList usernameNodes = emailElement.getElementsByTagName("username");
					  if (usernameNodes.getLength() > 0) {
						  Node usernameNode = usernameNodes.item(0);
						  if (usernameNode.getFirstChild() != null) {
							  emailUsername = usernameNode.getFirstChild().getNodeValue();
						  } else {
							  emailUsername = "";
						  }
					  }

					  NodeList passwordNodes = emailElement.getElementsByTagName("password");
					  if (passwordNodes.getLength() > 0) {
						  Node passwordNode = passwordNodes.item(0);
						  if (passwordNode.getFirstChild() != null) {
							  emailPassword = passwordNode.getFirstChild().getNodeValue();
						  } else {
							  emailPassword = "";
						  }
					  }
				  }
			  } catch (Exception e) {
				  System.out.println("Error reading email configuration: " + e.getMessage());
				  
				  smtpHost = "smtp.ehu.es";
				  smtpPort = 587;
				  smtpAuth = false;
				  smtpStartTLS = false;
				  senderEmail = "rides24@example.com";
				  senderName = "Rides24 Team";
				  emailUsername = "";
				  emailPassword = "";
			  }

			  System.out.print("Read from config.xml: ");
			  System.out.print("\t businessLogicLocal="+businessLogicLocal);
			  System.out.print("\t databaseLocal="+databaseLocal);
			  System.out.println("\t dataBaseInitialized="+isDatabaseInitialized);
			  System.out.println("\t Email configuration: smtpHost="+smtpHost+", smtpPort="+smtpPort);

		  } catch (Exception e) {
			System.out.println("Error in ConfigXML.java: problems with "+ configFile);
		    e.printStackTrace();
		  }

	}

	private static String getTagValue(String sTag, Element eElement)
	 {
		  Node node = eElement.getElementsByTagName(sTag).item(0);
		  if (node == null) {
			  return null;
		  }
		  NodeList nlList = node.getChildNodes();
		  if (nlList.getLength() == 0) {
			  return null;
		  }
		  Node nValue = nlList.item(0);
		  if (nValue == null) {
			  return null;
		  }
		  return nValue.getNodeValue();
	 }

	public static ConfigXML getInstance() {
		return theInstance;
	}

	public String getBusinessLogicNode() {
		return businessLogicNode;
	}

	public String getBusinessLogicPort() {
		return businessLogicPort;
	}

	public String getBusinessLogicName() {
		return businessLogicName;
	}

	public String getDbFilename(){
		return dbFilename;
	}

	public boolean isDatabaseInitialized(){
		return isDatabaseInitialized;
	}

	public String getDatabaseNode() {
		return databaseNode;
	}

}
