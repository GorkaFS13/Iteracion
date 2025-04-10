package domain;

import javax.persistence.*;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlID;
import javax.xml.bind.annotation.XmlIDREF;
import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.Vector;

@XmlAccessorType(XmlAccessType.FIELD)
@Entity
public class User implements Serializable {

	/**
	 *
	 */
	private static final long serialVersionUID = 1L;
	@XmlID
	@Id
	String username;
	String email;


	String type;
	public String getType() {
		return type;
	}

	String password;

	public String getEmail() {
		return email;
	}

	@XmlIDREF
	@OneToMany(fetch=FetchType.EAGER, cascade=CascadeType.PERSIST)
	private List<Ride> rides=new Vector<Ride>();

	public User() {
		super();
	}

	public User(String email, String name, String type) {
		this.username = email;
		this.password = name;
		this.type = type;
	}
	
	
	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	
	
	public String toString(){
		return username +";"+ password +rides;
	}
	
	/**
	 * This method creates a bet with a question, minimum bet ammount and percentual profit
	 * 
	 * @param username to be added to the event
	 * @param password of that question
	 * @return Bet
	 */


		
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		User other = (User) obj;
		if (username != other.username)
			return false;
		return true;
	}
	
}
