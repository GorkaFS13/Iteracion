package businessLogic;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.jws.WebMethod;
import javax.jws.WebService;

import configuration.ConfigXML;
import dataAccess.DataAccess;
import domain.*;
import exceptions.*;


@WebService(endpointInterface = "businessLogic.BLFacade")
public class BLFacadeImplementation  implements BLFacade {
	DataAccess dbManager;

	public BLFacadeImplementation()  {
		System.out.println("Creating BLFacadeImplementation instance");


		    dbManager=new DataAccess();

		


	}

    public BLFacadeImplementation(DataAccess da)  {

		System.out.println("Creating BLFacadeImplementation instance with DataAccess parameter");
		ConfigXML c=ConfigXML.getInstance();

		dbManager=da;
	}


    
    @WebMethod public List<String> getDepartCities(){
    	dbManager.open();

		 List<String> departLocations=dbManager.getDepartCities();

		dbManager.close();

		return departLocations;

    }
    
	@WebMethod public List<String> getDestinationCities(String from){
		dbManager.open();

		 List<String> targetCities=dbManager.getArrivalCities(from);

		dbManager.close();

		return targetCities;
	}

	
	@WebMethod
	public Ride createRide( String from, String to, Date date, int nPlaces, float price, String driverUsername ) throws RideMustBeLaterThanTodayException, RideAlreadyExistException{

		dbManager.open();
		Ride ride=dbManager.createRide(from, to, date, nPlaces, price, driverUsername);
		System.out.println("Ride created: " + ride);
		dbManager.close();
		return ride;
	};



	
	@WebMethod
	public boolean updateRide( Ride currentRide, String from, String to, Date date) throws RideMustBeLaterThanTodayException, RideAlreadyExistException{

		dbManager.open();
		boolean success =dbManager.updateRide(currentRide, from, to, date);
		dbManager.close();
		return success;
	};

   
	@WebMethod
	public List<Ride> getRides(String from, String to, Date date){
		dbManager.open();
		List<Ride>  rides=dbManager.getRides(from, to, date);
		dbManager.close();
		return rides;
	}
	@WebMethod

	public List<Ride> getRidesDriver(Driver driver){
		dbManager.open();
		List<Ride> rides=dbManager.getRidesDriver(driver);
		dbManager.close();
		return rides;
	}


	
	@WebMethod
	public List<Date> getThisMonthDatesWithRides(String from, String to, Date date){
		dbManager.open();
		List<Date>  dates=dbManager.getThisMonthDatesWithRides(from, to, date);
		dbManager.close();
		return dates;
	}


	public void close() {
		DataAccess dB4oManager=new DataAccess();

		dB4oManager.close();

	}

	
	@WebMethod
	public User createUser(String username, String password, String type) throws UserAlreadyExistException {
		dbManager.open();
		User user=dbManager.createUser(username, password, type);
		dbManager.close();
		return user;
	}

	@WebMethod
	public boolean checkUser(String username, String password) throws UserDoesntExistException {

		User user = getUser(username);
		if (user ==null){
			throw new UserDoesntExistException();
		}
		System.out.println("Pasword: " + password);
		System.out.println("Pasword good: " + user.getPassword());


		System.out.println("Return" + user.getPassword().equals(password));
		return(user.getPassword().equals(password));
	}


	public User getUser(String username) throws UserDoesntExistException {
		dbManager.open();
		User user = dbManager.getUser(username);
		dbManager.close();
		return user;
	}



	@Override
	public boolean requestRide(Ride selectedRide, User currentUser, Driver driver) {
		dbManager.open();
		boolean done = dbManager.requestRide(selectedRide, currentUser, driver);
		dbManager.close();
		return done;
	}

	@Override
	public boolean requestRide(Ride selectedRide, User currentUser, Driver driver, String comment) {
		dbManager.open();
		boolean done = dbManager.requestRide(selectedRide, currentUser, driver, comment);
		dbManager.close();
		return done;
	}

	@WebMethod
	 public List<RideRequest> getRequestsRide(Ride ride, Driver driver){
		dbManager.open();
		List<RideRequest>  requestUsers=dbManager.getRequestsRide(ride, driver);
		dbManager.close();
		return requestUsers;
	 }



	
    @WebMethod
	 public void initializeBD(){
    	dbManager.open();
		dbManager.initializeDB();
		dbManager.close();
	}


	public Driver getDriverRide(Ride ride){
		dbManager.open();
		Driver driver=dbManager.getDriverRide(ride);
		dbManager.close();
		return driver;
	}
	public boolean UpdatePlaces(Ride selectedRide){
		dbManager.open();
		Boolean places = dbManager.UpdatePlaces(selectedRide);
		dbManager.close();
		return places;
	}
	public boolean updateRequest(RideRequest request){
		dbManager.open();
		Boolean t = dbManager.updateRequest(request);
		dbManager.close();
		return t;
	}

	public boolean removeRequest(RideRequest request){
		dbManager.open();
		Boolean x = dbManager.removeRequest(request);
		dbManager.close();
		return x;
	}

	public boolean payRequest(RideRequest request){
		dbManager.open();
		Boolean y = dbManager.payRequest(request);
		dbManager.close();
		return y;
	}

	public List<RideRequest> getRequestsUser(User currentUser){
		dbManager.open();
		List<RideRequest>  requestUser=dbManager.getRequestsUser(currentUser);
		dbManager.close();
		return requestUser;
	}

	@WebMethod
	public boolean updateWalletBalance(String username, float amount) {
		dbManager.open();
		boolean success = dbManager.updateWalletBalance(username, amount);
		dbManager.close();
		return success;
	}

	@WebMethod
	public boolean addMoneyToWallet(String username, float amount) {
		dbManager.open();
		boolean success = dbManager.addMoneyToWallet(username, amount);
		dbManager.close();
		return success;
	}

	@WebMethod
	public boolean withdrawMoneyFromWallet(String username, float amount) {
		System.out.println("BLFacadeImplementation.withdrawMoneyFromWallet: username=" + username + ", amount=" + amount);

		try {
			
			User user = null;
			try {
				user = getUser(username);
			} catch (UserDoesntExistException e) {
				System.out.println("BLFacadeImplementation.withdrawMoneyFromWallet: User doesn't exist: " + username);
				return false;
			}

			if (user == null) {
				System.out.println("BLFacadeImplementation.withdrawMoneyFromWallet: User is null after getUser call");
				return false;
			}

			float currentBalance = user.getWalletBalance();
			System.out.println("BLFacadeImplementation.withdrawMoneyFromWallet: Current balance: " + currentBalance);

			if (currentBalance < amount) {
				System.out.println("BLFacadeImplementation.withdrawMoneyFromWallet: Insufficient funds: " + currentBalance + " < " + amount);
				return false;
			}

			
			dbManager.open();
			boolean success = dbManager.withdrawMoneyFromWallet(username, amount);
			dbManager.close();

			System.out.println("BLFacadeImplementation.withdrawMoneyFromWallet: Result: " + success);
			return success;
		} catch (Exception e) {
			System.out.println("BLFacadeImplementation.withdrawMoneyFromWallet: Exception: " + e.getMessage());
			e.printStackTrace();
			return false;
		}
	}

	@WebMethod
	public boolean rateDriver(Driver driver, User user, RideRequest rideRequest, int stars, String comment) {
		System.out.println("BLFacadeImplementation.rateDriver: driver=" + driver.getUsername() +
				", user=" + user.getUsername() + ", stars=" + stars);

		try {
			
			if (driver == null || user == null || rideRequest == null || stars < 1 || stars > 5) {
				System.out.println("BLFacadeImplementation.rateDriver: Invalid input parameters");
				return false;
			}

			
			if (rideRequest.isRated()) {
				System.out.println("BLFacadeImplementation.rateDriver: Ride request already rated");
				return false;
			}

			
			if (!"Done".equals(rideRequest.getPayment().getStatus())) {
				System.out.println("BLFacadeImplementation.rateDriver: Payment not completed");
				return false;
			}

			dbManager.open();
			boolean success = dbManager.rateDriver(driver, user, rideRequest, stars, comment);
			dbManager.close();

			return success;
		} catch (Exception e) {
			System.out.println("BLFacadeImplementation.rateDriver: Exception: " + e.getMessage());
			e.printStackTrace();
			return false;
		}
	}

	@WebMethod
	public float getDriverAverageRating(Driver driver) {
		if (driver == null) {
			return 0.0f;
		}

		return driver.getAverageRating();
	}

	@WebMethod
	public List<Rating> getDriverRatings(Driver driver) {
		if (driver == null) {
			return new ArrayList<>();
		}

		dbManager.open();
		List<Rating> ratings = dbManager.getDriverRatings(driver);
		dbManager.close();

		return ratings;
	}

	@WebMethod
	public boolean updateUserEmail(String username, String email) {
		System.out.println("BLFacadeImplementation.updateUserEmail: username=" + username + ", email=" + email);

		if (username == null || username.trim().isEmpty() || email == null || email.trim().isEmpty()) {
			return false;
		}

		dbManager.open();
		boolean success = dbManager.updateUserEmail(username, email);
		dbManager.close();

		return success;
	}

	@WebMethod
	public boolean updateDriverCarType(Driver driver, CarType carType) {
		if (driver == null || carType == null) {
			return false;
		}

		try {
			dbManager.open();
			boolean success = dbManager.updateDriverCarType(driver, carType);
			dbManager.close();
			return success;
		} catch (Exception e) {
			System.out.println("BLFacadeImplementation.updateDriverCarType: Exception: " + e.getMessage());
			e.printStackTrace();
			return false;
		}
	}
}

