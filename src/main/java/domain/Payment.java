package domain;

import javax.persistence.*;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlID;
import javax.xml.bind.annotation.XmlIDREF;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Vector;

@XmlAccessorType(XmlAccessType.FIELD)
@Entity
public class Payment {

	


	@XmlID
	@Id
	private String status;
	private float price;

	public Payment() {
		status = "Waiting";
	}

	public Payment(float price) {
		status = "Waiting";
		
		this.price = (price > 0) ? price : 1.0f; 
		if (price <= 0) {
			System.out.println("Warning: Attempted to create Payment with invalid price: " + price + ". Using default price: 1.0");
		}
	}

	public void setStatus(String newStatus) {
		this.status = newStatus;
	}

	public void setPrice(float newPrice) {
		
		if (newPrice > 0) {
			this.price = newPrice;
		} else {
			System.out.println("Warning: Attempted to set invalid price: " + newPrice + ". Price not updated.");
		}
	}

	public float getPrice() {
		return this.price;
	}
	public String getStatus() {
		return this.status;
	}


}
