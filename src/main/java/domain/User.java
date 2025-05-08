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

	private float walletBalance;

	public String getEmail() {
		return email;
	}

	@XmlIDREF
	@OneToMany(fetch=FetchType.EAGER, cascade=CascadeType.PERSIST)
	private List<Ride> rides=new Vector<Ride>();

	public User() {
		super();
		this.walletBalance = 0;
	}

	
	public User(String username, String password, String type) {
		this.username = username;
		this.password = password;
		this.type = type;
		this.walletBalance = 0;

		
		if (username.contains("@")) {
			this.email = username;
		} else {
			this.email = username + "@gmail.com";
		}
	}

	
	public boolean setEmail(String email) {
		
		if (email == null || email.trim().isEmpty()) {
			System.out.println("Warning: Attempted to set null or empty email address");
			return false;
		}

		
		if (!email.matches("^[A-Za-z0-9+_.-]+@(.+)$")) {
			System.out.println("Warning: Invalid email format: " + email);
			return false;
		}

		this.email = email;
		System.out.println("Email address updated for user " + username + ": " + email);
		return true;
	}

	
	public String getValidEmail() {
		if (this.email == null || this.email.trim().isEmpty()) {
			
			if (username.contains("@")) {
				return username; 
			} else {
				return username + "@example.com";
			}
		}
		return this.email;
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

	public float getWalletBalance() {
		return walletBalance;
	}

	public void setWalletBalance(float balance) {
		this.walletBalance = balance;
	}

	public boolean addMoneyToWallet(float amount) {
		if (amount > 0) {
			this.walletBalance += amount;
			return true;
		}
		return false;
	}

	public boolean withdrawMoneyFromWallet(float amount) {
		System.out.println("User.withdrawMoneyFromWallet: Current balance: " + this.walletBalance + ", Withdrawal amount: " + amount);
		if (amount <= 0) {
			System.out.println("User.withdrawMoneyFromWallet: Invalid amount (must be positive): " + amount);
			return false;
		}
		if (this.walletBalance < amount) {
			System.out.println("User.withdrawMoneyFromWallet: Insufficient funds: " + this.walletBalance + " < " + amount);
			return false;
		}

		
		this.walletBalance -= amount;
		System.out.println("User.withdrawMoneyFromWallet: New balance after withdrawal: " + this.walletBalance);
		return true;
	}

	public String toString(){
		return username +";"+ password +rides;
	}

	



	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		User other = (User) obj;
		if (username == null) {
			if (other.username != null)
				return false;
		} else if (!username.equals(other.username))
			return false;
		return true;
	}

}
