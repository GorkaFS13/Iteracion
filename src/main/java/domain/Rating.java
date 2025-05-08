package domain;

import javax.persistence.*;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import java.io.Serializable;
import java.util.Date;


@XmlAccessorType(XmlAccessType.FIELD)
@Entity
@Table(name = "ratings")
public class Rating implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @ManyToOne
    private Driver driver;

    @ManyToOne
    private User user;

    @ManyToOne
    private RideRequest rideRequest;

    private int stars; 
    private Date ratingDate;
    private String comment;

    
    public Rating() {
        this.ratingDate = new Date(); 
    }

    
    public Rating(Driver driver, User user, RideRequest rideRequest, int stars) {
        this.driver = driver;
        this.user = user;
        this.rideRequest = rideRequest;
        this.stars = validateStars(stars);
        this.ratingDate = new Date();
        this.comment = "";
    }

    
    public Rating(Driver driver, User user, RideRequest rideRequest, int stars, String comment) {
        this.driver = driver;
        this.user = user;
        this.rideRequest = rideRequest;
        this.stars = validateStars(stars);
        this.ratingDate = new Date();
        this.comment = comment;
    }

    
    private int validateStars(int stars) {
        if (stars < 1) {
            System.out.println("Warning: Rating value too low: " + stars + ". Setting to minimum of 1 star.");
            return 1;
        } else if (stars > 5) {
            System.out.println("Warning: Rating value too high: " + stars + ". Setting to maximum of 5 stars.");
            return 5;
        }
        return stars;
    }

    

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Driver getDriver() {
        return driver;
    }

    public void setDriver(Driver driver) {
        this.driver = driver;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public RideRequest getRideRequest() {
        return rideRequest;
    }

    public void setRideRequest(RideRequest rideRequest) {
        this.rideRequest = rideRequest;
    }

    public int getStars() {
        return stars;
    }

    public void setStars(int stars) {
        this.stars = validateStars(stars);
    }

    public Date getRatingDate() {
        return ratingDate;
    }

    public void setRatingDate(Date ratingDate) {
        this.ratingDate = ratingDate;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }
}
