package com.pw.lokalizator.model.entity;

import java.io.Serializable;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;

@Entity
@NamedQueries(value = {
		@NamedQuery(name = "findByUserLoginAndDateYoungerThanOlderThanOrderByDateDesc", 
				    query = "SELECT l FROM LocationGPS l WHERE l.user.login =:login AND l.date > :older AND l.date < :younger ORDER BY l.date DESC")
})
public class LocationGPS extends Location implements Serializable{
	
}
