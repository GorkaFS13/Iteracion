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

@XmlAccessorType(XmlAccessType.FIELD)
@Entity
public class Driver extends User implements Serializable{

	/**
	 *
	 */
	private static final long serialVersionUID = 1L;




	@XmlIDREF
	@OneToMany(fetch=FetchType.EAGER, cascade=CascadeType.PERSIST)
	private List<Ride> rides=new Vector<>();
	@XmlIDREF
	@OneToMany(fetch=FetchType.EAGER, cascade=CascadeType.PERSIST)
	private List<RideRequest> rideRequests = new ArrayList<>();

	public Driver() {
		super();
	}

	public Driver(User user) {
		super();
		this.type = user.getType();
		this.email = user.getUsername();
		this.username = user.getUsername();
		this.password = user.getPassword();

		System.out.println("THis: " + this);
	}


	public Ride addRide(String from, String to, Date date, int nPlaces, float price)  {
        Ride ride=new Ride(from,to,date,nPlaces,price, this);
        rides.add(ride);
        return ride;
	}

	public List<Ride> getRides()  {
		return rides;
	}



	/**
	 * This method checks if the ride already exists for that driver
	 *
	 * @param from the origin location
	 * @param to the destination location
	 * @param date the date of the ride
	 * @return true if the ride exists and false in other case
	 */
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
		if (email != other.email)
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
			if (request.getRide().equals(ride)) {  // Filtra solo las solicitudes de ese Ride
				requests.add(request);
			}
		}

		return requests;
	}



}
