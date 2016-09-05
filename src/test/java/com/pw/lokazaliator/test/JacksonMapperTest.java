package com.pw.lokazaliator.test;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.module.jaxb.JaxbAnnotationModule;
import com.pw.lokalizator.model.entity.CellInfoGSM;
import com.pw.lokalizator.model.entity.LocationNetwork;
import com.pw.lokalizator.model.entity.WifiInfo;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;

/**
 * Created by Patryk on 2016-08-27.
 */


public class JacksonMapperTest {

    private ObjectMapper objectMapper;
    private LocationNetwork locationNetwork = new LocationNetwork();
    private String jsonLocationNetwork =
            "{" +
            "\"latitude\":51.6124053," +
            "\"longitude\":18.9724216," +
            "\"date\":\"2015-07-09T00:00:00.000Z\"," +
            "\"providerType\":\"NETWORK\"," +
            "\"accuracy\":0.8," +
            "\"localizationServices\":\"OBCY\"," +
            "\"wifiInfo\":{" +
                "\"frequency\":123," +
                "\"bssid\":\"ala ma kota\"," +
                "\"ipAddress\":1234567890," +
                "\"linkSpeed\":101," +
                "\"macAddress\":\"alala\"," +
                "\"rssi\":321," +
                "\"ssid\":\"ssid?\"" +
             "}," +
             "\"cellInfoMobile\":{" +
                    "\"cellInfoLte\"" +
                    ":{}" +
             "}," +
            "\"address\":{" +
                 "\"city\":\"???\"," +
                 "\"street\":\"???\"" +
             "}" +
            "}";

    @Before
    public void before(){
        this.objectMapper = new ObjectMapper();
//      this.objectMapper.setAnnotationIntrospector(new JaxbAnnotationIntrospector());
        this.objectMapper.registerModule(new JaxbAnnotationModule());

        CellInfoGSM cellInfoGSM = new CellInfoGSM();
        this.locationNetwork.setCellInfoMobile(cellInfoGSM);
    }

    @Test
    public void parseToObjectTest() throws IOException {
        LocationNetwork locationNetwork = objectMapper.readValue(this.jsonLocationNetwork, LocationNetwork.class);
    }

    @Test
    public void parseToJsonTest() throws JsonProcessingException {
        String json = objectMapper.writeValueAsString(this.locationNetwork);
        System.out.println(json);
    }
}
