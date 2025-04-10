package businessLogic;

import java.util.Date;
import java.util.List;

//import domain.Booking;
import domain.Driver;
import domain.Ride;
import domain.User;
import domain.RideRequest;
import exceptions.*;

import javax.jws.WebMethod;
import javax.jws.WebService;
 
/**
 * Interface that specifies the business logic.
 */
@WebService
public interface BLFacade  {
	  
	/**
	 * This method returns all the cities where rides depart 
	 * @return collection of cities
	 */
	@WebMethod public List<String> getDepartCities();
	
	/**
	 * This method returns all the arrival destinations, from all rides that depart from a given city  
	 * 
	 * @param from the depart location of a ride
	 * @return all the arrival destinations
	 */
	@WebMethod public List<String> getDestinationCities(String from);


	/**
	 * This method creates a ride for a driver
	 * 
	 * @param from the origin location of a ride
	 * @param to the destination location of a ride
	 * @param date the date of the ride 
	 * @param nPlaces available seats
	 * @param driver to which ride is added
	 * 
	 * @return the created ride, or null, or an exception
	 * @throws RideMustBeLaterThanTodayException if the ride date is before today 
 	 * @throws RideAlreadyExistException if the same ride already exists for the driver
	 */
   @WebMethod
   public Ride createRide( String from, String to, Date date, int nPlaces, float price, String driverEmail) throws RideMustBeLaterThanTodayException, RideAlreadyExistException;

	@WebMethod
	public boolean updateRide(Ride currentRide, String from, String to, Date date) throws RideMustBeLaterThanTodayException, RideAlreadyExistException;

	/**
	 * This method retrieves the rides from two locations on a given date 
	 * 
	 * @param from the origin location of a ride
	 * @param to the destination location of a ride
	 * @param date the date of the ride 
	 * @return collection of rides
	 */
	@WebMethod public List<Ride> getRides(String from, String to, Date date);
	
	/**
	 * This method retrieves from the database the dates a month for which there are events
	 * @param from the origin location of a ride
	 * @param to the destination location of a ride 
	 * @param date of the month for which days with rides want to be retrieved 
	 * @return collection of rides
	 */
	@WebMethod public List<Date> getThisMonthDatesWithRides(String from, String to, Date date);



	/**
	 * This method creates a ride for a driver
	 *
	 * @param from the origin location of a ride
	 * @param to the destination location of a ride
	 * @param date the date of the ride
	 * @param nPlaces available seats
	 * @param driver to which ride is added
	 *
	 * @return the created ride, or null, or an exception
	 * @throws RideMustBeLaterThanTodayException if the ride date is before today
	 * @throws RideAlreadyExistException if the same ride already exists for the driver
	 */
	@WebMethod
	public User createUser(String username, String password, String type) throws UserAlreadyExistException;

	@WebMethod
	public boolean checkUser(String username, String password) throws UserDoesntExistException;





	/**
	 * This method calls the data access to initialize the database with some events and questions.
	 * It is invoked only when the option "initialize" is declared in the tag dataBaseOpenMode of resources/config.xml file
	 */	
	@WebMethod public void initializeBD();




	public User getUser(String username) throws UserDoesntExistException;

	@WebMethod public boolean requestRide(Ride selectedRide, User currentUser, Driver rideDriver);

	@WebMethod public List<RideRequest> getRequestsRide(Ride ride, Driver driver);

	@WebMethod public List<Ride> getRidesDriver(Driver driver);

	@WebMethod public Driver getDriverRide(Ride ride);

	@WebMethod public boolean UpdatePlaces(Ride selectedRide);

	@WebMethod public boolean updateRequest(RideRequest request);

	@WebMethod public boolean removeRequest(RideRequest request);

	@WebMethod public boolean payRequest(RideRequest request);

	@WebMethod public List<RideRequest> getRequestsUser(User currentUser);


}
