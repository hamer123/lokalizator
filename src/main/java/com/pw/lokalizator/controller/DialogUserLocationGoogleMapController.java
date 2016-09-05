package com.pw.lokalizator.controller;

import javax.enterprise.context.Dependent;
import javax.inject.Named;

import org.primefaces.model.map.Overlay;

import com.pw.lokalizator.jsf.utilitis.OverlayIdentyfikator;
import com.pw.lokalizator.jsf.utilitis.OverlayIdentyfikator.OverlayIdentyfikatorBuilder;
import com.pw.lokalizator.model.google.map.GoogleMapModel;
import com.pw.lokalizator.model.entity.Location;
import com.pw.lokalizator.model.enums.LocalizationServices;
import com.pw.lokalizator.model.enums.Overlays;
import com.pw.lokalizator.model.enums.Providers;
import com.pw.lokalizator.serivce.qualifier.DialogUserLocationGoogleMap;

@Named
@Dependent
@DialogUserLocationGoogleMap
public class DialogUserLocationGoogleMapController extends GoogleMapController{

	public void onShowGPSLastLocation(){
		Overlay overlay = findGPSLastLocaitionMarker();
         setCenterIfLocationExist(overlay);
	}
	
	public void onShowNetworkNaszLastLocation(){
         Overlay overlay = findNetworkNaszLastLocationMarker();
         setCenterIfLocationExist(overlay);
	}
	
	public void onShowNetworkObcyLastLocation(){
         Overlay overlay = findNetworkObcyLastLocationMarker();
         setCenterIfLocationExist(overlay);
	}
	
	public boolean isNetworkObcyLastLocationExist(){
		return findNetworkObcyLastLocationMarker() != null;
	}
	
	private Overlay findNetworkObcyLastLocationMarker(){
		 OverlayIdentyfikator identyfikator = new OverlayIdentyfikatorBuilder().overlayType(Overlays.MARKER)
                 .providerType(Providers.NETWORK)
                 .localzationServiceType(LocalizationServices.OBCY)
                 .build();
         return findSingleOverlay(identyfikator);
	}
	
	public boolean isNetworkNaszLastLocationExist(){
		return findNetworkNaszLastLocationMarker() != null;
	}
	
	private Overlay findNetworkNaszLastLocationMarker(){
		 OverlayIdentyfikator identyfikator = new OverlayIdentyfikatorBuilder().overlayType(Overlays.MARKER)
                 .providerType(Providers.NETWORK)
                 .localzationServiceType(LocalizationServices.NASZ)
                 .build();
         return findSingleOverlay(identyfikator);
	}
	
	public boolean isGPSLastLocationExist(){
		return findGPSLastLocaitionMarker() != null;
	}
	
	private Overlay findGPSLastLocaitionMarker(){
		 OverlayIdentyfikator identyfikator = new OverlayIdentyfikatorBuilder().overlayType(Overlays.MARKER)
                 .providerType(Providers.GPS)
                 .build();
         return findSingleOverlay(identyfikator);
	}
	
	public void setCenterIfLocationExist(Overlay overlay){
        if(overlay != null){
            Location location = (Location)overlay.getData();
            setCenter(GoogleMapModel.center(location));
        }	
	}
}
