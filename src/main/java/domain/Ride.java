package domain;

import java.io.*;
import java.util.Date;

import javax.persistence.*;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlID;
import javax.xml.bind.annotation.XmlIDREF;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;


@SuppressWarnings("serial")
@XmlAccessorType(XmlAccessType.FIELD)
@Entity
public class Ride implements Serializable {
	@XmlID
	@Id 
	@XmlJavaTypeAdapter(IntegerAdapter.class)
	@GeneratedValue
	private Integer rideNumber;
	private String from;
	private String to;
	private int nPlaces;
	private Date date;
	private float price;


	private Driver driver;  

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "user_email") 
	private User user;


	public Ride(){
		super();
	}
	
	public Ride(Integer rideNumber, String from, String to, Date date, int nPlaces, float price, Driver driver) {
		super();
		this.rideNumber = rideNumber;
		this.from = from;
		this.to = to;
		this.nPlaces = nPlaces;
		this.date=date;
		this.price=price;
		this.driver = driver;
	}
	public Ride(Integer rideNumber, String from, String to, Date date, int nPlaces, float price, Driver driver, User user) {
		super();
		this.rideNumber = rideNumber;
		this.from = from;
		this.to = to;
		this.nPlaces = nPlaces;
		this.date=date;
		this.price=price;
		this.driver = driver;
		this.user = user;
	}

	

	public Ride(String from, String to,  Date date, int nPlaces, float price, Driver driver) {
		super();
		this.from = from;
		this.to = to;
		this.nPlaces = nPlaces;
		this.date=date;
		this.price=price;
		this.driver = driver;
	}
	
	
	public Integer getRideNumber() {
		return rideNumber;
	}

	
	
	
	public void setRideNumber(Integer rideNumber) {
		this.rideNumber = rideNumber;
	}


	

	public String getFrom() {
		return from;
	}


		
	
	public void setFrom(String origin) {
		this.from = origin;
	}

	

	public String getTo() {
		return to;
	}


		
	public void setTo(String destination) {
		this.to = destination;
	}

	
	
	
	public Date getDate() {
		return date;
	}
		
	public void setDate(Date date) {
		this.date = date;
	}

	
	public float getnPlaces() {
		return nPlaces;
	}

	

	public void setBetMinimum(int nPlaces) {
		this.nPlaces = nPlaces;
	}

	
	public Driver getDriver() {
		return driver;
	}

	
	public void setDriver(Driver driver) {
		this.driver = driver;
	}

	public float getPrice() {
		return price;
	}

	public void setPrice(float price) {
		this.price = price;
	}



	public String toString(){
		return rideNumber+";"+";"+from+";"+to+";"+date;  
	}




	
}
