package domain;

import java.util.Date;


import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Id;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;

@Entity
@Table(name="USER")
public class User implements java.io.Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private int id;

	@Column(name="FIRST_NAME")
	private String name;

	@Column(name="LAST_NAME")
	private String forename;

	@Temporal(TemporalType.DATE)
	private Date added;

	public User(String name, String forename, Date added) {
		this.name = name;
		this.forename = forename;
		this.added = added;
	}

	public int getId(){
		return this.id;
	}
	public void setId(int id){
		this.id = id;
	}

	public String getName(){
		return name;
	}
	public void setName(String name){
		this.name = name;
	}

	public String getForename(){
		return forename;
	}
	public void setForename(String forename){
		this.forename = forename;
	}

	public Date getAdded(){
		return added;
	}
	public void setDate(Date date){
		this.added = date;
	}

	public String toString() {
		return this.id + " " + this.name + " " + this.forename + this.added;
	}
}
