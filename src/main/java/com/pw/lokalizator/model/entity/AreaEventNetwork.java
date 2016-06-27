package com.pw.lokalizator.model.entity;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToOne;

@Entity
@NamedQueries(value ={
		@NamedQuery(name  = "AreaEventNetwork.findByAreaId", query = "SELECT a FROM AreaEventNetwork a WHERE a.area.id = :id"),
		@NamedQuery(name  = "AreaEventNetwork.findAllWhereMailSendIsTrue",
		            query = "SELECT a FROM AreaEventNetwork a WHERE a.mailSend = true"),
		@NamedQuery(name  = "AreaEventNetwork.findAll",
                    query = "SELECT a FROM AreaEventNetwork a"),
        @NamedQuery(name  = "AreaEventNetwork.removeById",
                    query = "DELETE FROM AreaEventNetwork a WHERE a.id =:id")
})
public class AreaEventNetwork extends AreaEvent{

	@OneToOne
    private LocationNetwork locationNetwork;
    
    public AreaEventNetwork() {
	}
    
    public AreaEventNetwork(LocationNetwork locationNetwork){
    	this.locationNetwork = locationNetwork;
    }

	public void setLocationNetwork(LocationNetwork locationNetwork) {
		this.locationNetwork = locationNetwork;
	}

	@Override
	public Location getLocation() {
		return locationNetwork;
	}

}
