package domain;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "requests") 
public class RideRequest implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id; 

    @ManyToOne
    private Ride ride; 

    @ManyToOne
    private User user; 

    @ManyToOne
    private Driver driver; 

    private String state;

    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinColumn(name = "payment_id")
    private Payment payment;

    @Column(length = 500)
    private String comment; 

    private boolean rated = false; 

    
    public RideRequest() {
        this.state = "Waiting";
        this.comment = "";
        this.payment = new Payment(1.0f); 
    }

    
    public RideRequest(Ride ride, User user, Driver driver) {
        this.ride = ride;
        this.user = user;
        this.driver = driver;
        this.state = "Waiting";
        this.comment = ""; 

        
        float ridePrice = ride.getPrice();
        if (ridePrice <= 0) {
            System.out.println("Warning: Creating RideRequest with invalid price: " + ridePrice + ". Using minimum price of 1.0");
            ridePrice = 1.0f; 
        }

        
        this.payment = new Payment(ridePrice);
        System.out.println("Created RideRequest with price: " + ridePrice + ", Payment: " + this.payment);
    }

    
    public RideRequest(Ride ride, User user, Driver driver, String comment) {
        this.ride = ride;
        this.user = user;
        this.driver = driver;
        this.state = "Waiting";
        this.comment = comment;

        
        float ridePrice = ride.getPrice();
        if (ridePrice <= 0) {
            System.out.println("Warning: Creating RideRequest with invalid price: " + ridePrice + ". Using minimum price of 1.0");
            ridePrice = 1.0f; 
        }

        
        this.payment = new Payment(ridePrice);
        System.out.println("Created RideRequest with price: " + ridePrice + ", Payment: " + this.payment);
    }

    
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Ride getRide() {
        return ride;
    }

    public void setRide(Ride ride) {
        this.ride = ride;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Driver getDriver() {
        return driver;
    }

    public void setDriver(Driver driver) {
        this.driver = driver;
    }

    public void setState(String accepted) {
        this.state = accepted;
    }

    public String getState() { return this.state;
    }

    public Payment getPayment() {
        return payment;
    }

    public void setPayment(Payment payment) {
        this.payment = payment;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public boolean isRated() {
        return rated;
    }

    public void setRated(boolean rated) {
        this.rated = rated;
    }
}
