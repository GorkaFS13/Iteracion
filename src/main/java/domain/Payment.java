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

	/**
	 *
	 */


	@XmlID
	@Id
	private String status;
	private float price;

	public Payment() {
		status = "Waiting";
	}

	public Payment(float price) {
		status = "Waiting";
		this.price = price;
	}

	public void setStatus(String newStatus) {
		this.status = newStatus;
	}
	public void setPrice(float newPrice) {
		this.price = newPrice;
	}

	public float getPrice() {
		return this.price;
	}
	public String getStatus() {
		return this.status;
	}


}
