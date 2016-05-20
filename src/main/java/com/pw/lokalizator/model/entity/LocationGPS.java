package com.pw.lokalizator.model.entity;

import java.io.Serializable;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@Entity
@DiscriminatorValue(value="GPS")
public class LocationGPS extends Location implements Serializable{
	
}
