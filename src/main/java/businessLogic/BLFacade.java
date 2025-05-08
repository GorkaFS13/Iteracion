package businessLogic;

import java.util.Date;
import java.util.List;


import domain.*;
import exceptions.*;

import javax.jws.WebMethod;
import javax.jws.WebService;


@WebService
public interface BLFacade  {

	
	@WebMethod public List<String> getDepartCities();

	
	@WebMethod public List<String> getDestinationCities(String from);


	
   @WebMethod
   public Ride createRide( String from, String to, Date date, int nPlaces, float price, String driverUsername) throws RideMustBeLaterThanTodayException, RideAlreadyExistException;

	@WebMethod
	public boolean updateRide(Ride currentRide, String from, String to, Date date) throws RideMustBeLaterThanTodayException, RideAlreadyExistException;

	
	@WebMethod public List<Ride> getRides(String from, String to, Date date);

	
	@WebMethod public List<Date> getThisMonthDatesWithRides(String from, String to, Date date);



	
	@WebMethod
	public User createUser(String username, String password, String type) throws UserAlreadyExistException;

	@WebMethod
	public boolean checkUser(String username, String password) throws UserDoesntExistException;





	
	@WebMethod public void initializeBD();




	public User getUser(String username) throws UserDoesntExistException;

	@WebMethod public boolean requestRide(Ride selectedRide, User currentUser, Driver rideDriver);

	@WebMethod public boolean requestRide(Ride selectedRide, User currentUser, Driver rideDriver, String comment);

	@WebMethod public List<RideRequest> getRequestsRide(Ride ride, Driver driver);

	@WebMethod public List<Ride> getRidesDriver(Driver driver);

	@WebMethod public Driver getDriverRide(Ride ride);

	@WebMethod public boolean UpdatePlaces(Ride selectedRide);

	@WebMethod public boolean updateRequest(RideRequest request);

	@WebMethod public boolean removeRequest(RideRequest request);

	@WebMethod public boolean payRequest(RideRequest request);

	@WebMethod public List<RideRequest> getRequestsUser(User currentUser);

	
	@WebMethod public boolean updateWalletBalance(String username, float amount);

	
	@WebMethod public boolean addMoneyToWallet(String username, float amount);

	
	@WebMethod public boolean withdrawMoneyFromWallet(String username, float amount);

	
	@WebMethod public boolean rateDriver(Driver driver, User user, RideRequest rideRequest, int stars, String comment);

	
	@WebMethod public float getDriverAverageRating(Driver driver);

	
	@WebMethod public List<Rating> getDriverRatings(Driver driver);

	
	@WebMethod public boolean updateUserEmail(String username, String email);

	
	@WebMethod public boolean updateDriverCarType(Driver driver, CarType carType);
}
