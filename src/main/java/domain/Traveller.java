package domain;

import javax.persistence.*;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlID;
import javax.xml.bind.annotation.XmlIDREF;
import java.io.Serializable;
import java.util.List;
import java.util.Vector;

@XmlAccessorType(XmlAccessType.FIELD)
@Entity
public class Traveller extends User implements Serializable{

	
	private static final long serialVersionUID = 1L;
	
	private String name;
	@XmlIDREF
	@OneToMany(fetch=FetchType.EAGER, cascade=CascadeType.PERSIST)
	private List<Ride> rides=new Vector<Ride>();

	public Traveller() {
		super();
	}

	public Traveller(User currentUser) {
		super();
		this.username = currentUser.getUsername();
		this.password = currentUser.getPassword();
		this.type = currentUser.getType();
		this.email = currentUser.getEmail();
		this.name = currentUser.getUsername(); 
	}

	public Traveller(String username, String password, String type) {
		super(username, password, type);
		this.name = username; 
	}


	public String getEmail() {
		return email;
	}

	public boolean setEmail(String email) {
		this.email = email;
		return true;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}



	public String toString(){
		return email+";"+name+rides;
	}

	


	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Traveller other = (Traveller) obj;
		if (username == null) {
			if (other.username != null)
				return false;
		} else if (!username.equals(other.username))
			return false;
		return true;
	}



}
