package models;

import java.sql.Timestamp;
import java.util.*;

import javax.persistence.*;

import play.db.ebean.*;
import play.db.ebean.Model.Finder;
import play.data.format.*;
import play.data.validation.*;

import com.avaje.ebean.*;

@Entity
public class ResCosts extends Model{
	
	//attributes that are mapped to table rescosts
	@Id
	private Long costsID;
	private double depositPaid;
	private Date datePaid;
	private String payMethod;
	//attributes that are derived/calculated
	@Transient
	private double totAccom;
	@Transient
	private double totAdMeals;
	@Transient
	private double totChMeals;
	@Transient
	private double total;
	@Transient
	private double depositDue;
	@Transient
	private double balance;

	
	//getters and setters
	public Long getCostsID() {
		return costsID;
	}
	public void setCostsID(Long costsID) {
		this.costsID = costsID;
	}
	public double getDepositPaid() {
		return depositPaid;
	}
	public void setDepositPaid(double depositPaid) {
		this.depositPaid = depositPaid;
	}
	public Date getDatePaid() {
		return datePaid;
	}
	public void setDatePaid(Date datePaid) {
		this.datePaid = datePaid;
	}
	public String getPayMethod() {
		return payMethod;
	}
	public void setPayMethod(String payMethod) {
		this.payMethod = payMethod;
	}
	public double getTotAccom() {
		return totAccom;
	}
	public void setTotAccom(double totAccom) {
		this.totAccom = totAccom;
	}
	public double getTotAdMeals() {
		return totAdMeals;
	}
	public void setTotAdMeals(double totAdMeals) {
		this.totAdMeals = totAdMeals;
	}
	public double getTotChMeals() {
		return totChMeals;
	}
	public void setTotChMeals(double totChMeals) {
		this.totChMeals = totChMeals;
	}
	public double getTotal() {
		return total;
	}
	public void setTotal(double total) {
		this.total = total;
	}
	public double getDepositDue() {
		return depositDue;
	}
	public void setDepositDue(double depositDue) {
		this.depositDue = depositDue;
	}
	public double getBalance() {
		return balance;
	}
	public void setBalance(double balance) {
		this.balance = balance;
	}
	
	//constructor
	public ResCosts(){
		this.totAccom=0;
		this.totAdMeals=0;
		this.totChMeals=0;
		this.total=0;
		this.depositDue=0;
		this.balance=0;
		this.depositDue=0;
	}
		
	//method to calculate all the attributes, for a reservation r
	public void addTotals(Reserve r){
		this.totAccom = r.getLen()*r.getNumRooms()*r.getRoomRate();
		this.totAdMeals = r.getLen()*r.getMealAdult()*r.getNumAdults();
		this.totChMeals = r.getLen()*r.getMealChild()*r.getNumChild();
		this.total = (totAccom + totAdMeals + totChMeals + r.getWhaleRate() + r.getTransfer());
		this.depositDue = total*30/100;
		this.balance = total - depositDue;
	}
	
	
	
}	

