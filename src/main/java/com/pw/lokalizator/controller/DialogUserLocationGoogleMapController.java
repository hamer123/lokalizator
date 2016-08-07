package com.pw.lokalizator.controller;

import java.util.List;

import javax.enterprise.context.Dependent;
import javax.inject.Named;

import org.primefaces.model.map.Overlay;

import com.pw.lokalizator.jsf.utilitis.OverlayIdentyfikator;
import com.pw.lokalizator.jsf.utilitis.OverlayIdentyfikator.OverlayIdentyfikatorBuilder;
import com.pw.lokalizator.model.GoogleMapModel;
import com.pw.lokalizator.model.entity.Location;
import com.pw.lokalizator.model.enums.LocalizationServices;
import com.pw.lokalizator.model.enums.Overlays;
import com.pw.lokalizator.model.enums.Providers;
import com.pw.lokalizator.serivce.qualifier.DialogGoogleMap;

@Named
@Dependent
@DialogGoogleMap
public class GoogleMapSingleUserDialogController extends GoogleMapController{


	public void onShowGPSLastLocation(){
		 OverlayIdentyfikator identyfikator = new OverlayIdentyfikatorBuilder().overlayType(Overlays.MARKER)
				                                                               .providerType(Providers.GPS)
		                                                                       .build();
		 List<Overlay>overlays = findOverlay(identyfikator);
         setCenterIfLocationExist(overlays);
	}
	
	public void onShowNetworkNaszLastLocation(){
		 OverlayIdentyfikator identyfikator = new OverlayIdentyfikatorBuilder().overlayType(Overlays.MARKER)
                 .providerType(Providers.NETWORK)
                 .localzationServiceType(LocalizationServices.NASZ)
                 .build();
         List<Overlay>overlays = findOverlay(identyfikator);
         setCenterIfLocationExist(overlays);
	}
	
	public void onShowNetworkObcyLastLocation(){
		 OverlayIdentyfikator identyfikator = new OverlayIdentyfikatorBuilder().overlayType(Overlays.MARKER)
                 .providerType(Providers.NETWORK)
                 .localzationServiceType(LocalizationServices.OBCY)
                 .build();
         List<Overlay>overlays = findOverlay(identyfikator);
         setCenterIfLocationExist(overlays);
	}
	
	private void setCenterIfLocationExist(List<Overlay>overlays){
        if(!overlays.isEmpty()){
            Overlay overlay = overlays.get(0);
            Location location = (Location)overlay.getData();
            setCenter( GoogleMapModel.center(location) );
        }	
	}
	
}
