package domain;

import javax.persistence.*;

@Entity
@Table(name = "requests") // Nombre de la tabla en la base de datos
public class RideRequest {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id; // Identificador único requerido por JPA

    @ManyToOne
    private Ride ride; // Relación con Ride

    @ManyToOne
    private User user; // Relación con User (usuario solicitante)

    @ManyToOne
    private Driver driver; // Relación con Driver (conductor)

    @ManyToOne
    private String state;
    @ManyToOne
    private Payment payment;

    // Constructor
    public RideRequest(Ride ride, User user, Driver driver) {
        this.ride = ride;
        this.user = user;
        this.driver = driver;
        this.state = "Waiting";
        payment = new Payment(ride.getPrice());
      //  this.payment = "Not done";
    }

    // Getters y Setters (obligatorios para JPA)
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


}
