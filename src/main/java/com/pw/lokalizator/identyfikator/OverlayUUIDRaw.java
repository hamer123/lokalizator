package com.pw.lokalizator.identyfikator;

import com.pw.lokalizator.model.enums.LocalizationServices;
import com.pw.lokalizator.model.enums.Overlays;
import com.pw.lokalizator.model.enums.Providers;

/**
 * Created by wereckip on 30.08.2016.
 */
class OverlayUUIDRaw {
    private long id;
    private String login;
    private Overlays overlay;
    private Providers provider;
    private LocalizationServices localizationService;

    public long getId() {
        return id;
    }

    public String getLogin() {
        return login;
    }

    public Overlays getOverlay() {
        return overlay;
    }

    public Providers getProvider() {
        return provider;
    }

    public LocalizationServices getLocalizationService() {
        return localizationService;
    }

    public static class OverlayUUIDRawBuilder{
        OverlayUUIDRaw overlayUUIDRaw = new OverlayUUIDRaw();

        public static OverlayUUIDRawBuilder insatnce(){
            return new OverlayUUIDRawBuilder();
        }

        public OverlayUUIDRawBuilder id(long id){
            this.overlayUUIDRaw.id = id;
            return this;
        }

        public OverlayUUIDRawBuilder login(String login){
            this.overlayUUIDRaw.login = login;
            return this;
        }

        public OverlayUUIDRawBuilder overlay(Overlays overlay){
            this.overlayUUIDRaw.overlay = overlay;
            return this;
        }

        public OverlayUUIDRawBuilder provider(Providers provider){
            this.overlayUUIDRaw.provider = provider;
            return this;
        }

        public OverlayUUIDRawBuilder localizationService(LocalizationServices localizationService){
            this.overlayUUIDRaw.localizationService = localizationService;
            return this;
        }

        public OverlayUUIDRaw build(){
            return this.overlayUUIDRaw;
        }
    }
}
