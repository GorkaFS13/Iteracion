package dataAccess;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.TypedQuery;

import configuration.ConfigXML;
import configuration.UtilDate;
import domain.*;
import exceptions.RideAlreadyExistException;
import exceptions.UserAlreadyExistException;
import exceptions.RideMustBeLaterThanTodayException;
import exceptions.UserDoesntExistException;

/**
 * It implements the data access to the objectDb database
 */
public class DataAccess  {
	private  EntityManager  db;
	private  EntityManagerFactory emf;


	ConfigXML c=ConfigXML.getInstance();

     public DataAccess()  {/*
		if (c.isDatabaseInitialized()) {
			String fileName=c.getDbFilename();

			File fileToDelete= new File(fileName);
			if(fileToDelete.delete()){
				File fileToDeleteTemp= new File(fileName+"$");
				fileToDeleteTemp.delete();

				  System.out.println("File deleted");
				} else {
				  System.out.println("Operation failed");
				}
		}
		*/
		open();
		if  (!c.isDatabaseInitialized())initializeDB();
		
		System.out.println("DataAccess created => isDatabaseLocal: "+c.isDatabaseLocal()+" isDatabaseInitialized: "+c.isDatabaseInitialized());

		close();

	}
     
    public DataAccess(EntityManager db) {
    	this.db=db;
    }

	
	
	/**
	 * This is the data access method that initializes the database with some events and questions.
	 * This method is invoked by the business logic (constructor of BLFacadeImplementation) when the option "initialize" is declared in the tag dataBaseOpenMode of resources/config.xml file
	 */	
	public void initializeDB(){
		
		db.getTransaction().begin();

		try {

		   Calendar today = Calendar.getInstance();
		   
		   int month=today.get(Calendar.MONTH);
		   int year=today.get(Calendar.YEAR);
		   if (month==12) { month=1; year+=1;}  
	    
		   
		    //Create drivers 
			Driver driver1=new Driver(new User("driver1@gmail.com","Aitor Fernandez", "Traveler"));
			Driver driver2=new Driver(new User("driver2@gmail.com","Ane Gaztañaga", "Driver"));
			Driver driver3=new Driver(new User("driver3@gmail.com","Test driver", "Driver"));

			
			//Create rides
			driver1.addRide("Donostia", "Bilbo", UtilDate.newDate(year,month,15), 4, 7);
			driver1.addRide("Donostia", "Gazteiz", UtilDate.newDate(year,month,6), 4, 8);
			driver1.addRide("Bilbo", "Donostia", UtilDate.newDate(year,month,25), 4, 4);

			driver1.addRide("Donostia", "Iruña", UtilDate.newDate(year,month,7), 4, 8);
			
			driver2.addRide("Donostia", "Bilbo", UtilDate.newDate(year,month,15), 3, 3);
			driver2.addRide("Bilbo", "Donostia", UtilDate.newDate(year,month,25), 2, 5);
			driver2.addRide("Eibar", "Gasteiz", UtilDate.newDate(year,month,6), 2, 5);

			driver3.addRide("Bilbo", "Donostia", UtilDate.newDate(year,month,14), 1, 3);

			
						
			db.persist(driver1);
			db.persist(driver2);
			db.persist(driver3);

	
			db.getTransaction().commit();
			System.out.println("Db initialized");
		}
		catch (Exception e){
			e.printStackTrace();
		}
	}
	
	/**
	 * This method returns all the cities where rides depart 
	 * @return collection of cities
	 */
	public List<String> getDepartCities(){
			TypedQuery<String> query = db.createQuery("SELECT DISTINCT r.from FROM Ride r ORDER BY r.from", String.class);
			List<String> cities = query.getResultList();
			return cities;
		
	}

	/**
	 * This method returns all the arrival destinations, from all rides that depart from a given city  
	 * 
	 * @param from the depart location of a ride
	 * @return all the arrival destinations
	 */
	public List<String> getArrivalCities(String from){
		TypedQuery<String> query = db.createQuery("SELECT DISTINCT r.to FROM Ride r WHERE r.from=?1 ORDER BY r.to",String.class);
		query.setParameter(1, from);
		List<String> arrivingCities = query.getResultList(); 
		return arrivingCities;
		
	}
	/**
	 * This method creates a ride for a driver
	 * 
	 * @param from the origin location of a ride
	 * @param to the destination location of a ride
	 * @param date the date of the ride 
	 * @param nPlaces available seats
	 * @param driverEmail to which ride is added
	 * 
	 * @return the created ride, or null, or an exception
	 * @throws RideMustBeLaterThanTodayException if the ride date is before today 
 	 * @throws RideAlreadyExistException if the same ride already exists for the driver
	 */
	public Ride createRide(String from, String to, Date date, int nPlaces, float price, String driverEmail) throws  RideAlreadyExistException, RideMustBeLaterThanTodayException {
		System.out.println(">> DataAccess: createRide=> from= "+from+" to= "+to+" driver="+driverEmail+" date "+date);
		try {
			if(new Date().compareTo(date)>0) {
				throw new RideMustBeLaterThanTodayException(ResourceBundle.getBundle("Etiquetas").getString("CreateRideGUI.ErrorRideMustBeLaterThanToday"));
			}
			db.getTransaction().begin();
			Driver driver = db.find(Driver.class, driverEmail);
			if (driver == null) {
				// Si no existe, buscar el usuario primero
				User user = db.find(User.class, driverEmail);
				if (user == null) {
					db.getTransaction().rollback();
					throw new IllegalArgumentException("User not found with email: " + driverEmail);
				}
				// Convertir usuario a conductor si es necesario
				driver = new Driver(user);
				db.persist(driver); // Solo persistir si es nuevo
			}

			System.out.println("Driver:::::: " + driver);
			if (driver.doesRideExists(from, to, date)) {
				db.getTransaction().commit();
				throw new RideAlreadyExistException(ResourceBundle.getBundle("Etiquetas").getString("DataAccess.RideAlreadyExist"));
			}
			Ride ride = driver.addRide(from, to, date, nPlaces, price);
			//next instruction can be obviated
			db.persist(driver);
			db.getTransaction().commit();

			return ride;
		} catch (NullPointerException e) {
			System.out.println("Error has ocurred, " + e);
			// TODO Auto-generated catch block
			db.getTransaction().commit();
			return null;
		}


	}

	public boolean updateRide(Ride currentRide, String from, String to, Date date) throws  RideAlreadyExistException, RideMustBeLaterThanTodayException {
		db.getTransaction().begin();
		Ride ride = db.find(Ride.class, currentRide.getRideNumber());

		if(new Date().compareTo(date)>0) {
			throw new RideMustBeLaterThanTodayException(ResourceBundle.getBundle("Etiquetas").getString("CreateRideGUI.ErrorRideMustBeLaterThanToday"));
		}
		if (date != null)
			ride.setDate(date);

		if (from != null)
			ride.setFrom(from);

		if (to != null)
			ride.setTo(to);


		db.merge(ride);
		db.getTransaction().commit();
		return true;

	}
	
	/**
	 * This method retrieves the rides from two locations on a given date 
	 * 
	 * @param from the origin location of a ride
	 * @param to the destination location of a ride
	 * @param date the date of the ride 
	 * @return collection of rides
	 */
	public List<Ride> getRides(String from, String to, Date date) {
		System.out.println(">> DataAccess: getRides=> from= "+from+" to= "+to+" date "+date);

		List<Ride> res = new ArrayList<>();
		TypedQuery<Ride> query = db.createQuery("SELECT r FROM Ride r LEFT JOIN FETCH r.driver WHERE r.from = ?1 AND r.to = ?2 AND r.date = ?3", Ride.class);		query.setParameter(1, from);
		query.setParameter(2, to);
		query.setParameter(3, date);
		List<Ride> rides = query.getResultList();
	 	 for (Ride ride:rides){
		   res.add(ride);
		  }
	 	return res;
	}
	/**
	 * This method retrieves from the database the dates a month for which there are events
	 * @param from the origin location of a ride
	 * @param to the destination location of a ride 
	 * @param date of the month for which days with rides want to be retrieved 
	 * @return collection of rides
	 */
	public List<Date> getThisMonthDatesWithRides(String from, String to, Date date) {
		System.out.println(">> DataAccess: getEventsMonth");
		List<Date> res = new ArrayList<>();	
		
		Date firstDayMonthDate= UtilDate.firstDayMonth(date);
		Date lastDayMonthDate= UtilDate.lastDayMonth(date);
				
		
		TypedQuery<Date> query = db.createQuery("SELECT DISTINCT r.date FROM Ride r WHERE r.from=?1 AND r.to=?2 AND r.date BETWEEN ?3 and ?4",Date.class);   
		
		query.setParameter(1, from);
		query.setParameter(2, to);
		query.setParameter(3, firstDayMonthDate);
		query.setParameter(4, lastDayMonthDate);
		List<Date> dates = query.getResultList();
	 	 for (Date d:dates){
		   res.add(d);
		  }
	 	return res;
	}


	/**
	 * This method creates a ride for a driver
	 *
	 * @param username the username of the user
	 * @param password of the user

	 *
	 * @return the created ride, or null, or an exception
	 * @throws UserAlreadyExistException if the same ride already exists for the driver
	 */
	public User createUser(String username, String password, String type) throws UserAlreadyExistException {
		System.out.println(">> DataAccess: createUser > username: "+username+", password: " + password + " , type" + type+  "  ;");
		try {
			db.getTransaction().begin();

			User user = db.find(User.class, username);
			System.out.println("User: " + user);
			if (user != null) { // TODO
				db.getTransaction().commit();
				throw new UserAlreadyExistException(ResourceBundle.getBundle("Etiquetas").getString("DataAccess.UserAlreadyExist"));
			}
			User user1 = new User(username, password, type);
			//next instruction can be obviated
			db.persist(user1);
			db.getTransaction().commit();


			return user1;
		}
		catch (NullPointerException e) {
			// TODO Auto-generated catch block
			db.getTransaction().commit();
			return null;
		}

	}
	public User getUser(String username) throws UserDoesntExistException {
		try {
			db.getTransaction().begin();

			User user = db.find(User.class, username);

			System.out.println("User: " + user);
			if (user == null) { // TODO
				db.getTransaction().commit();
				throw new UserDoesntExistException(ResourceBundle.getBundle("Etiquetas").getString("LoginGUI.UserNotExist"));
			}

			db.getTransaction().commit();

			return user;
		} catch (NullPointerException e) {
			// TODO Auto-generated catch block
			db.getTransaction().commit();
			return null;
		}
	}

public void open(){
		
		String fileName=c.getDbFilename();
		if (c.isDatabaseLocal()) {
			emf = Persistence.createEntityManagerFactory("objectdb:"+fileName);
			db = emf.createEntityManager();
		} else {
			Map<String, String> properties = new HashMap<>();
			  properties.put("javax.persistence.jdbc.user", c.getUser());
			  properties.put("javax.persistence.jdbc.password", c.getPassword());

			  emf = Persistence.createEntityManagerFactory("objectdb://"+c.getDatabaseNode()+":"+c.getDatabasePort()+"/"+fileName, properties);
			  db = emf.createEntityManager();
    	   }
		System.out.println("DataAccess opened => isDatabaseLocal: "+c.isDatabaseLocal());

		
	}

	public void close(){
		db.close();
		System.out.println("DataAcess closed");
	}




	public boolean requestRide(Ride selectedRide, User currentUser, Driver driver) {
		try {
			TypedQuery<RideRequest> query = db.createQuery(
					"SELECT r FROM RideRequest r WHERE r.ride = :ride AND r.driver = :driver AND r.user = :user", RideRequest.class);
			query.setParameter("ride", selectedRide);  // Establece el ride específico
			query.setParameter("driver", driver);  // Establece el driver específico
			query.setParameter("user", currentUser);  // Establece el driver específico

			List<RideRequest> requests = query.getResultList();
			if (requests.isEmpty()) {
				db.getTransaction().begin(); // Inicia la transacción
			/*Driver rideDriver = db.find(Driver.class, driver.getUsername());
			if(rideDriver != null){
				System.out.println("Existe el driver:"+ rideDriver);
			}
			rideDriver.addRequest(currentUser, selectedRide); // Añadir la solicitud al driver
			db.merge(rideDriver);*/
				RideRequest request = new RideRequest(selectedRide, currentUser, driver);
				db.persist(request);
				db.getTransaction().commit(); // Confirmar la transacción
				return true;
			}else{
				return false;
			}
		} catch (Exception e) {
			if (db.getTransaction().isActive()) {
				db.getTransaction().rollback(); // Revierte si hay error
			}
			e.printStackTrace();
			return false;
		}
	}


	public List<RideRequest> getRequestsRide(Ride ride, Driver driver) {
		System.out.println(">> DataAccess: getRequestsRide => Driver: " + driver.getUsername() + ", Ride: " + ride);

		try {
			// Crear la consulta para obtener los usuarios de la tabla intermedia requestRide
			TypedQuery<RideRequest> query = db.createQuery(
					"SELECT r FROM RideRequest r WHERE r.ride = :ride AND r.driver = :driver", RideRequest.class);
			query.setParameter("ride", ride);  // Establece el ride específico
			query.setParameter("driver", driver);  // Establece el driver específico

			// Ejecutar la consulta y obtener los usuarios
			List<RideRequest> users = query.getResultList();
			return users;  // Retorna la lista de usuarios que hicieron la solicitud
		} catch (Exception e) {
			e.printStackTrace();  // Captura cualquier error
			return new ArrayList<>();  // Si ocurre un error, retorna una lista vacía
		}
	}



	public List<Ride> getRidesDriver(Driver driver) {
		System.out.println(">> DataAccess: getRidesDriver=> from= "+driver.getUsername());

		List<Ride> res = new ArrayList<>();
		TypedQuery<Ride> query = db.createQuery("SELECT r FROM Ride r WHERE r.driver = :driver", Ride.class);
		query.setParameter("driver", driver);

		List<Ride> rides = query.getResultList();
		for (Ride ride:rides){
			res.add(ride);
		}
		return res;
	}

	public Driver getDriverRide(Ride ride) {
		System.out.println(">> DataAccess: getDriverRide=> from= "+ride.getDriver());

		List<Driver> res = new ArrayList<>();
		TypedQuery<Driver> query = db.createQuery("SELECT d FROM Driver d WHERE :ride MEMBER OF d.rides", Driver.class);
		query.setParameter("ride", ride);
		List<Driver> drivers = query.getResultList();
        return drivers.get(0);

	}

	public Boolean UpdatePlaces(Ride selectedRide) {
		db.getTransaction().begin();
		Ride ride = db.find(Ride.class, selectedRide.getRideNumber());
		int places = (int) ride.getnPlaces();
		if (places>0){
			ride.setBetMinimum(places-1);
			db.merge(ride);
			db.getTransaction().commit();
			return true;
		}
		else{
			db.getTransaction().commit();
			return false;

		}

	}

	public Boolean updateRequest(RideRequest request) {
		db.getTransaction().begin();
		RideRequest req = db.find(RideRequest.class, request.getId());
		if (req == null){
			return false;
		}else {
			req.setState("Accepted");
			System.out.println(req.getState());
			db.merge(req);
			db.getTransaction().commit();
			return true;
		}
	}

	public Boolean removeRequest(RideRequest request) {
		try{
			db.getTransaction().begin();
			RideRequest request2 = db.find(RideRequest.class, request);
			db.remove(request2);
			db.getTransaction().commit();
			return true;
		}catch (Exception e) {
			e.printStackTrace();  // Captura cualquier error
			return false;
		}

	}

	public Boolean payRequest(RideRequest request) {
		System.out.println(">> DataAccess: payRequest=> " + request);

		db.getTransaction().begin();
		RideRequest req = db.find(RideRequest.class, request.getId());
		if (req == null){
			return false;
		}else {
			Payment payment = req.getPayment();
			payment.setPrice(request.getRide().getPrice());
			payment.setStatus("Done");

			System.out.println("New state for payment: " + req.getPayment());
			db.merge(req);
			db.getTransaction().commit();
			return true;
		}
	}

	public List<RideRequest> getRequestsUser(User currentUser) {

		try {
			// Crear la consulta para obtener los usuarios de la tabla intermedia requestRide
			TypedQuery<RideRequest> query = db.createQuery("SELECT r FROM RideRequest r WHERE r.user = :user", RideRequest.class);
			query.setParameter("user", currentUser);


			List<RideRequest> requests = query.getResultList();
			return requests;
		} catch (Exception e) {
			e.printStackTrace();  // Captura cualquier error
			return new ArrayList<>();  // Si ocurre un error, retorna una lista vacía
		}
	}


}
