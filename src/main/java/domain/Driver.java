package domain;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Vector;


import javax.persistence.*;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlIDREF;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

@XmlAccessorType(XmlAccessType.FIELD)
@Entity
public class Driver extends User implements Serializable{

	
	private static final long serialVersionUID = 1L;

	@XmlIDREF
	@OneToMany(fetch=FetchType.EAGER, cascade=CascadeType.PERSIST)
	private List<Ride> rides=new Vector<>();

	@XmlIDREF
	@OneToMany(fetch=FetchType.EAGER, cascade=CascadeType.PERSIST)
	private List<RideRequest> rideRequests = new ArrayList<>();

	@XmlIDREF
	@OneToMany(fetch=FetchType.EAGER, cascade=CascadeType.PERSIST)
	private List<Rating> ratings = new ArrayList<>();

	private float averageRating = 0.0f;
	private int totalRatings = 0;

	@Enumerated(EnumType.STRING)
	private CarType carType = CarType.SEDAN; 

	public Driver() {
		super();
	}

	public Driver(User user) {
		super();
		this.type = user.getType();
		this.username = user.getUsername();

		
		if (user.getEmail() != null && !user.getEmail().isEmpty()) {
			this.email = user.getEmail();
		} else {
			
			if (user.getUsername().contains("@")) {
				this.email = user.getUsername();
			} else {
				this.email = user.getUsername() + "@gmail.com";
			}
		}

		this.password = user.getPassword();
		this.carType = CarType.SEDAN; 

		System.out.println("Created Driver: " + this);
	}

	
	public CarType getCarType() {
		return carType;
	}

	
	public void setCarType(CarType carType) {
		this.carType = carType;
	}


	public Ride addRide(String from, String to, Date date, int nPlaces, float price)  {
        Ride ride=new Ride(from,to,date,nPlaces,price, this);
        rides.add(ride);
        return ride;
	}

	public List<Ride> getRides()  {
		return rides;
	}



	
	public boolean doesRideExists(String from, String to, Date date)  {
		for (Ride r:rides)
			if ( (java.util.Objects.equals(r.getFrom(),from)) && (java.util.Objects.equals(r.getTo(),to)) && (java.util.Objects.equals(r.getDate(),date)) )
			 return true;

		return false;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Driver other = (Driver) obj;
		if (username == null) {
			if (other.username != null)
				return false;
		} else if (!username.equals(other.username))
			return false;
		return true;
	}

	public Ride removeRide(String from, String to, Date date) {
		boolean found=false;
		int index=0;
		Ride r=null;
		while (!found && index<=rides.size()) {
			r=rides.get(++index);
			if ( (java.util.Objects.equals(r.getFrom(),from)) && (java.util.Objects.equals(r.getTo(),to)) && (java.util.Objects.equals(r.getDate(),date)) )
			found=true;
		}

		if (found) {
			rides.remove(index);
			return r;
		} else return null;
	}


	public void addRequest(User passenger, Ride ride) {
		RideRequest newRequest = new RideRequest(ride, passenger, this);
		rideRequests.add(newRequest);
	}

	public List<RideRequest> getRideRequests(Ride ride){
		List<RideRequest> requests = new ArrayList<>();

		for (RideRequest request : rideRequests) {
			if (request.getRide().equals(ride)) {  
				requests.add(request);
			}
		}

		return requests;
	}

	
	public boolean addRating(Rating rating) {
		if (rating == null || rating.getDriver() != this) {
			return false;
		}

		
		ratings.add(rating);

		
		updateAverageRating();

		return true;
	}

	
	public void updateAverageRating() {
		if (ratings.isEmpty()) {
			averageRating = 0.0f;
			totalRatings = 0;
			return;
		}

		int sum = 0;
		for (Rating rating : ratings) {
			sum += rating.getStars();
		}

		totalRatings = ratings.size();
		averageRating = (float) sum / totalRatings;
	}

	
	public float getAverageRating() {
		return averageRating;
	}

	
	public int getTotalRatings() {
		return totalRatings;
	}

	
	public List<Rating> getRatings() {
		return ratings;
	}
}
