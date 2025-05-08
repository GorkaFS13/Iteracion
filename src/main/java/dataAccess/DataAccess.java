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


public class DataAccess  {
	private  EntityManager  db;
	private  EntityManagerFactory emf;


	ConfigXML c=ConfigXML.getInstance();

     public DataAccess()  {
		open();
		if  (!c.isDatabaseInitialized())initializeDB();

		System.out.println("DataAccess created => isDatabaseLocal: "+c.isDatabaseLocal()+" isDatabaseInitialized: "+c.isDatabaseInitialized());

		close();

	}

    public DataAccess(EntityManager db) {
    	this.db=db;
    }



	
	public void initializeDB(){

		db.getTransaction().begin();

		try {

		   Calendar today = Calendar.getInstance();

		   int month=today.get(Calendar.MONTH);
		   int year=today.get(Calendar.YEAR);
		   if (month==12) { month=1; year+=1;}


		    
			Driver driver1=new Driver(new User("driver1@gmail.com","Aitor Fernandez", "Traveler"));
			Driver driver2=new Driver(new User("driver2@gmail.com","Ane Gaztañaga", "Driver"));
			Driver driver3=new Driver(new User("driver3@gmail.com","Test driver", "Driver"));


			
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

	
	public List<String> getDepartCities(){
			TypedQuery<String> query = db.createQuery("SELECT DISTINCT r.from FROM Ride r ORDER BY r.from", String.class);
			List<String> cities = query.getResultList();
			return cities;

	}

	
	public List<String> getArrivalCities(String from){
		TypedQuery<String> query = db.createQuery("SELECT DISTINCT r.to FROM Ride r WHERE r.from=?1 ORDER BY r.to",String.class);
		query.setParameter(1, from);
		List<String> arrivingCities = query.getResultList();
		return arrivingCities;

	}
	
	public Ride createRide(String from, String to, Date date, int nPlaces, float price, String driverUsername) throws  RideAlreadyExistException, RideMustBeLaterThanTodayException {
		System.out.println(">> DataAccess: createRide=> from= "+from+" to= "+to+" driver="+driverUsername+" date "+date);
		try {
			if(new Date().compareTo(date)>0) {
				throw new RideMustBeLaterThanTodayException(ResourceBundle.getBundle("Etiquetas").getString("CreateRideGUI.ErrorRideMustBeLaterThanToday"));
			}
			db.getTransaction().begin();

			
			Driver driver = db.find(Driver.class, driverUsername);

			if (driver == null) {
				
				User user = db.find(User.class, driverUsername);

				if (user == null) {
					db.getTransaction().rollback();
					throw new IllegalArgumentException("User not found with username: " + driverUsername);
				}

				
				driver = new Driver(user);
				db.persist(driver); 
				System.out.println(">> DataAccess: createRide => Created new driver from user: " + user.getUsername());
			}

			System.out.println("Driver:::::: " + driver);
			if (driver.doesRideExists(from, to, date)) {
				db.getTransaction().commit();
				throw new RideAlreadyExistException(ResourceBundle.getBundle("Etiquetas").getString("DataAccess.RideAlreadyExist"));
			}
			Ride ride = driver.addRide(from, to, date, nPlaces, price);
			
			db.persist(driver);
			db.getTransaction().commit();

			return ride;
		} catch (NullPointerException e) {
			System.out.println("Error has ocurred, " + e);
			
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


	
	public User createUser(String username, String password, String type) throws UserAlreadyExistException {
		System.out.println(">> DataAccess: createUser > username: "+username+", password: " + password + " , type" + type+  "  ;");
		try {
			db.getTransaction().begin();

			User user = db.find(User.class, username);
			System.out.println("User: " + user);
			if (user != null) { 
				db.getTransaction().commit();
				throw new UserAlreadyExistException(ResourceBundle.getBundle("Etiquetas").getString("DataAccess.UserAlreadyExist"));
			}
			User user1 = new User(username, password, type);
			
			db.persist(user1);
			db.getTransaction().commit();


			return user1;
		}
		catch (NullPointerException e) {
			
			db.getTransaction().commit();
			return null;
		}

	}
	public User getUser(String username) throws UserDoesntExistException {
		try {
			db.getTransaction().begin();

			User user = db.find(User.class, username);

			System.out.println("User: " + user);
			if (user == null) { 
				db.getTransaction().commit();
				throw new UserDoesntExistException(ResourceBundle.getBundle("Etiquetas").getString("LoginGUI.UserNotExist"));
			}

			db.getTransaction().commit();

			return user;
		} catch (NullPointerException e) {
			
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
		
		return requestRide(selectedRide, currentUser, driver, "");
	}

	public boolean requestRide(Ride selectedRide, User currentUser, Driver driver, String comment) {
		try {
			TypedQuery<RideRequest> query = db.createQuery(
					"SELECT r FROM RideRequest r WHERE r.ride = :ride AND r.driver = :driver AND r.user = :user", RideRequest.class);
			query.setParameter("ride", selectedRide);  
			query.setParameter("driver", driver);  
			query.setParameter("user", currentUser);  

			List<RideRequest> requests = query.getResultList();
			if (requests.isEmpty()) {
				db.getTransaction().begin(); 
			
				RideRequest request = new RideRequest(selectedRide, currentUser, driver, comment);
				db.persist(request);
				db.getTransaction().commit(); 
				return true;
			}else{
				return false;
			}
		} catch (Exception e) {
			if (db.getTransaction().isActive()) {
				db.getTransaction().rollback(); 
			}
			e.printStackTrace();
			return false;
		}
	}


	public List<RideRequest> getRequestsRide(Ride ride, Driver driver) {
		System.out.println(">> DataAccess: getRequestsRide => Driver: " + driver.getUsername() + ", Ride: " + ride);

		try {
			
			TypedQuery<RideRequest> query = db.createQuery(
					"SELECT r FROM RideRequest r WHERE r.ride = :ride AND r.driver = :driver", RideRequest.class);
			query.setParameter("ride", ride);  
			query.setParameter("driver", driver);  

			
			List<RideRequest> users = query.getResultList();
			return users;  
		} catch (Exception e) {
			e.printStackTrace();  
			return new ArrayList<>();  
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
		try {
			RideRequest req = db.find(RideRequest.class, request.getId());
			if (req == null){
				System.out.println(">> DataAccess: updateRequest => Request not found with ID: " + request.getId());
				db.getTransaction().rollback();
				return false;
			}

			
			Ride ride = db.find(Ride.class, req.getRide().getRideNumber());
			if (ride == null) {
				System.out.println(">> DataAccess: updateRequest => Ride not found for request: " + req.getId());
				db.getTransaction().rollback();
				return false;
			}

			
			req.setState("Accepted");
			System.out.println(">> DataAccess: updateRequest => New state: " + req.getState());

			
			float ridePrice = ride.getPrice();
			System.out.println(">> DataAccess: updateRequest => Ride price from database: " + ridePrice);

			if (ridePrice <= 0) {
				System.out.println(">> DataAccess: updateRequest => Invalid ride price: " + ridePrice + ". Using minimum price of 1.0");
				ridePrice = 1.0f; 
				ride.setPrice(ridePrice);
				db.merge(ride);
			}

			
			Payment payment = req.getPayment();
			if (payment == null) {
				System.out.println(">> DataAccess: updateRequest => Payment not found for request: " + req.getId() + ". Creating new payment.");
				payment = new Payment(ridePrice);
				req.setPayment(payment);
			} else {
				payment.setPrice(ridePrice);
			}

			db.merge(req);
			db.getTransaction().commit();
			return true;
		} catch (Exception e) {
			System.out.println(">> DataAccess: updateRequest => Exception: " + e.getMessage());
			e.printStackTrace();
			if (db.getTransaction().isActive()) {
				db.getTransaction().rollback();
			}
			return false;
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
			e.printStackTrace();  
			return false;
		}

	}

	public Boolean payRequest(RideRequest request) {
		System.out.println(">> DataAccess: payRequest=> " + request);

		db.getTransaction().begin();
		try {
			RideRequest req = db.find(RideRequest.class, request.getId());
			if (req == null){
				System.out.println(">> DataAccess: payRequest => Request not found with ID: " + request.getId());
				db.getTransaction().rollback();
				return false;
			}

			
			Ride ride = db.find(Ride.class, req.getRide().getRideNumber());
			if (ride == null) {
				System.out.println(">> DataAccess: payRequest => Ride not found for request: " + req.getId());
				db.getTransaction().rollback();
				return false;
			}

			
			Payment payment = req.getPayment();
			if (payment == null) {
				System.out.println(">> DataAccess: payRequest => Payment not found for request: " + req.getId());
				db.getTransaction().rollback();
				return false;
			}

			
			float ridePrice = ride.getPrice();
			System.out.println(">> DataAccess: payRequest => Ride price from database: " + ridePrice);

			if (ridePrice <= 0) {
				System.out.println(">> DataAccess: payRequest => Invalid ride price: " + ridePrice + ". Using minimum price of 1.0");
				ridePrice = 1.0f; 
				ride.setPrice(ridePrice);
				db.merge(ride);
			}

			
			payment.setPrice(ridePrice);
			payment.setStatus("Done");

			System.out.println(">> DataAccess: payRequest => Updated payment: " + payment + ", Price: " + payment.getPrice());

			
			db.merge(req);
			db.getTransaction().commit();

			
			
			

			return true;
		} catch (Exception e) {
			System.out.println(">> DataAccess: payRequest => Exception: " + e.getMessage());
			e.printStackTrace();
			if (db.getTransaction().isActive()) {
				db.getTransaction().rollback();
			}
			return false;
		}
	}

	public List<RideRequest> getRequestsUser(User currentUser) {

		try {
			
			TypedQuery<RideRequest> query = db.createQuery("SELECT r FROM RideRequest r WHERE r.user = :user", RideRequest.class);
			query.setParameter("user", currentUser);


			List<RideRequest> requests = query.getResultList();
			return requests;
		} catch (Exception e) {
			e.printStackTrace();  
			return new ArrayList<>();  
		}
	}

	
	public boolean updateWalletBalance(String username, float amount) {
		System.out.println(">> DataAccess: updateWalletBalance => username: " + username + ", amount: " + amount);
		try {
			db.getTransaction().begin();
			User user = db.find(User.class, username);

			if (user == null) {
				db.getTransaction().rollback();
				return false;
			}

			user.setWalletBalance(amount);
			db.merge(user);
			db.getTransaction().commit();
			return true;
		} catch (Exception e) {
			if (db.getTransaction().isActive()) {
				db.getTransaction().rollback();
			}
			e.printStackTrace();
			return false;
		}
	}

	
	public boolean addMoneyToWallet(String username, float amount) {
		System.out.println(">> DataAccess: addMoneyToWallet => username: " + username + ", amount: " + amount);

		
		if (amount <= 0) {
			System.out.println(">> DataAccess: addMoneyToWallet => Invalid amount (must be positive): " + amount);
			return false;
		}

		if (username == null || username.trim().isEmpty()) {
			System.out.println(">> DataAccess: addMoneyToWallet => Invalid username");
			return false;
		}

		try {
			
			db.getTransaction().begin();

			
			User user = db.find(User.class, username);

			if (user == null) {
				System.out.println(">> DataAccess: addMoneyToWallet => User not found: " + username);
				db.getTransaction().rollback();
				return false;
			}

			
			float currentBalance = user.getWalletBalance();
			System.out.println(">> DataAccess: addMoneyToWallet => Current balance in DB: " + currentBalance + ", Adding amount: " + amount);

			
			float newBalance = currentBalance + amount;
			System.out.println(">> DataAccess: addMoneyToWallet => New balance will be: " + newBalance);

			
			user.setWalletBalance(newBalance);

			
			db.flush(); 
			db.merge(user);
			db.getTransaction().commit();

			
			db.getTransaction().begin();
			User updatedUser = db.find(User.class, username);
			float updatedBalance = updatedUser.getWalletBalance();
			db.getTransaction().commit();

			System.out.println(">> DataAccess: addMoneyToWallet => Verified new balance: " + updatedBalance);

			
			if (Math.abs(updatedBalance - newBalance) > 0.001) { 
				System.out.println(">> DataAccess: addMoneyToWallet => WARNING: Balance verification failed. Expected: " +
					newBalance + ", Actual: " + updatedBalance);
			}

			return true;
		} catch (Exception e) {
			System.out.println(">> DataAccess: addMoneyToWallet => Exception: " + e.getMessage());
			if (db.getTransaction().isActive()) {
				db.getTransaction().rollback();
			}
			e.printStackTrace();
			return false;
		}
	}

	
	public boolean withdrawMoneyFromWallet(String username, float amount) {
		System.out.println(">> DataAccess: withdrawMoneyFromWallet => username: " + username + ", amount: " + amount);

		
		if (amount <= 0) {
			System.out.println(">> DataAccess: withdrawMoneyFromWallet => Invalid amount (must be positive): " + amount);
			return false;
		}

		if (username == null || username.trim().isEmpty()) {
			System.out.println(">> DataAccess: withdrawMoneyFromWallet => Invalid username");
			return false;
		}

		try {
			
			db.getTransaction().begin();

			
			User user = db.find(User.class, username);

			if (user == null) {
				System.out.println(">> DataAccess: withdrawMoneyFromWallet => User not found: " + username);
				db.getTransaction().rollback();
				return false;
			}

			
			float currentBalance = user.getWalletBalance();
			System.out.println(">> DataAccess: withdrawMoneyFromWallet => Current balance in DB: " + currentBalance + ", Withdrawal amount: " + amount);

			
			if (currentBalance < amount) {
				System.out.println(">> DataAccess: withdrawMoneyFromWallet => Insufficient funds: " + currentBalance + " < " + amount);
				db.getTransaction().rollback();
				return false;
			}

			
			float newBalance = currentBalance - amount;
			System.out.println(">> DataAccess: withdrawMoneyFromWallet => New balance will be: " + newBalance);

			
			user.setWalletBalance(newBalance);

			
			db.flush(); 
			db.merge(user);
			db.getTransaction().commit();

			
			db.getTransaction().begin();
			User updatedUser = db.find(User.class, username);
			float updatedBalance = updatedUser.getWalletBalance();
			db.getTransaction().commit();

			System.out.println(">> DataAccess: withdrawMoneyFromWallet => Verified new balance: " + updatedBalance);

			
			if (Math.abs(updatedBalance - newBalance) > 0.001) { 
				System.out.println(">> DataAccess: withdrawMoneyFromWallet => WARNING: Balance verification failed. Expected: " +
					newBalance + ", Actual: " + updatedBalance);
			}

			return true;
		} catch (Exception e) {
			System.out.println(">> DataAccess: withdrawMoneyFromWallet => Exception: " + e.getMessage());
			if (db.getTransaction().isActive()) {
				db.getTransaction().rollback();
			}
			e.printStackTrace();
			return false;
		}
	}

	
	public boolean rateDriver(Driver driver, User user, RideRequest rideRequest, int stars, String comment) {
		System.out.println(">> DataAccess: rateDriver => driver: " + driver.getUsername() +
				", user: " + user.getUsername() + ", stars: " + stars);

		try {
			db.getTransaction().begin();

			
			Driver dbDriver = db.find(Driver.class, driver.getUsername());
			User dbUser = db.find(User.class, user.getUsername());
			RideRequest dbRideRequest = db.find(RideRequest.class, rideRequest.getId());

			if (dbDriver == null || dbUser == null || dbRideRequest == null) {
				System.out.println(">> DataAccess: rateDriver => One or more entities not found in database");
				db.getTransaction().rollback();
				return false;
			}

			
			if (dbRideRequest.isRated()) {
				System.out.println(">> DataAccess: rateDriver => Ride request already rated");
				db.getTransaction().rollback();
				return false;
			}

			
			Rating rating = new Rating(dbDriver, dbUser, dbRideRequest, stars, comment);

			
			db.persist(rating);

			
			dbRideRequest.setRated(true);
			db.merge(dbRideRequest);

			
			dbDriver.addRating(rating);
			db.merge(dbDriver);

			db.getTransaction().commit();
			System.out.println(">> DataAccess: rateDriver => Rating saved successfully. New driver average: " +
					dbDriver.getAverageRating() + " from " + dbDriver.getTotalRatings() + " ratings");

			return true;
		} catch (Exception e) {
			System.out.println(">> DataAccess: rateDriver => Exception occurred: " + e.getMessage());
			if (db.getTransaction().isActive()) {
				db.getTransaction().rollback();
			}
			e.printStackTrace();
			return false;
		}
	}

	
	public List<Rating> getDriverRatings(Driver driver) {
		System.out.println(">> DataAccess: getDriverRatings => driver: " + driver.getUsername());

		try {
			TypedQuery<Rating> query = db.createQuery(
					"SELECT r FROM Rating r WHERE r.driver = :driver ORDER BY r.ratingDate DESC",
					Rating.class);
			query.setParameter("driver", driver);

			List<Rating> ratings = query.getResultList();
			return ratings;
		} catch (Exception e) {
			System.out.println(">> DataAccess: getDriverRatings => Exception occurred: " + e.getMessage());
			e.printStackTrace();
			return new ArrayList<>();
		}
	}

	
	public boolean updateDriverCarType(Driver driver, CarType carType) {
		System.out.println(">> DataAccess: updateDriverCarType => driver: " + driver.getUsername() +
				", carType: " + carType.getDisplayName());

		try {
			db.getTransaction().begin();

			
			Driver dbDriver = db.find(Driver.class, driver.getUsername());

			if (dbDriver == null) {
				System.out.println(">> DataAccess: updateDriverCarType => Driver not found: " + driver.getUsername());
				db.getTransaction().rollback();
				return false;
			}

			
			dbDriver.setCarType(carType);
			db.merge(dbDriver);
			db.getTransaction().commit();

			System.out.println(">> DataAccess: updateDriverCarType => Car type updated successfully");
			return true;
		} catch (Exception e) {
			System.out.println(">> DataAccess: updateDriverCarType => Exception occurred: " + e.getMessage());
			if (db.getTransaction().isActive()) {
				db.getTransaction().rollback();
			}
			e.printStackTrace();
			return false;
		}
	}

	
	public boolean updateUserEmail(String username, String email) {
		System.out.println(">> DataAccess: updateUserEmail => username: " + username + ", email: " + email);

		
		if (username == null || username.trim().isEmpty()) {
			System.out.println(">> DataAccess: updateUserEmail => Invalid username");
			return false;
		}

		if (email == null || email.trim().isEmpty()) {
			System.out.println(">> DataAccess: updateUserEmail => Invalid email");
			return false;
		}

		try {
			db.getTransaction().begin();

			
			User user = db.find(User.class, username);

			if (user == null) {
				System.out.println(">> DataAccess: updateUserEmail => User not found: " + username);
				db.getTransaction().rollback();
				return false;
			}

			
			boolean success = user.setEmail(email);
			if (!success) {
				System.out.println(">> DataAccess: updateUserEmail => Invalid email format: " + email);
				db.getTransaction().rollback();
				return false;
			}

			db.flush();
			db.merge(user);
			db.getTransaction().commit();

			System.out.println(">> DataAccess: updateUserEmail => Email updated successfully for user: " + username);
			return true;
		} catch (Exception e) {
			System.out.println(">> DataAccess: updateUserEmail => Exception occurred: " + e.getMessage());
			if (db.getTransaction().isActive()) {
				db.getTransaction().rollback();
			}
			e.printStackTrace();
			return false;
		}
	}
}
