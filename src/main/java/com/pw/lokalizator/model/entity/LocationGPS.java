package com.pw.lokalizator.model.entity;
import javax.persistence.*;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

@NamedQueries(value = {
		@NamedQuery(name = "findByUserLoginAndDateYoungerThanOlderThanOrderByDateDesc", 
				    query = "SELECT l FROM LocationGPS l WHERE l.user.login =:login AND l.date > :older AND l.date < :younger ORDER BY l.date DESC")
})
@Entity
@PrimaryKeyJoinColumn(referencedColumnName = "location_id", name = "location_gps_id")
@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class LocationGPS extends Location{
}
