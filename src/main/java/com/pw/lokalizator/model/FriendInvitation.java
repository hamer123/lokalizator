package com.pw.lokalizator.model;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.NamedNativeQueries;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.TableGenerator;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

@Entity
@NamedQueries(value={
		@NamedQuery(name="FriendInvitation.findInvitationByUsersId", query="SELECT fi FROM FriendInvitation fi "
				+ "WHERE ( fi.from.id =:id AND fi.to.id =:id2 ) OR ( fi.to.id =:id AND fi.from.id =:id2)"),
		@NamedQuery(name="FriendInvitation.findInvitationBySenderId", query="SELECT new com.pw.lokalizator.model.FriendInvitation("
			    + " fi.id, fi.date, fi.from.login) FROM FriendInvitation fi WHERE fi.to.id =:id"),
})
@NamedNativeQueries(value={
})
public class FriendInvitation implements Serializable{
	@Id
    @TableGenerator(
            name="invitationGen", 
            table="ID_GEN", 
            pkColumnName="GEN_KEY", 
            valueColumnName="GEN_VALUE", 
            pkColumnValue="INVITATION_ID"
            )
	@GeneratedValue(strategy=GenerationType.TABLE, generator="invitationGen")
	private long id;
	
	@ManyToOne
	private User from;
	@ManyToOne
	private User to;
	
	@Temporal(TemporalType.TIMESTAMP)
	private Date date;
	
	public FriendInvitation(){}
	
	public FriendInvitation(long id, Date date, String login){
		this.id = id;
		this.date = date;
		this.from = new User();
		from.setLogin(login);
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public User getFrom() {
		return from;
	}

	public void setFrom(User from) {
		this.from = from;
	}

	public User getTo() {
		return to;
	}

	public void setTo(User to) {
		this.to = to;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}
	
}
