
package com.pw.lokalizator.google.json;

import java.util.HashMap;
import java.util.Map;
import javax.annotation.Generated;
import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Generated("org.jsonschema2pojo")
@JsonPropertyOrder({
    "bounds",
    "location",
    "location_type",
    "viewport"
})
public class Geometry {

    @JsonProperty("bounds")
    private Bounds bounds;
    @JsonProperty("location")
    private Location location;
    @JsonProperty("location_type")
    private String locationType;
    @JsonProperty("viewport")
    private Viewport viewport;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    /**
     * No args constructor for use in serialization
     * 
     */
    public Geometry() {
    }

    /**
     * 
     * @param bounds
     * @param viewport
     * @param location
     * @param locationType
     */
    public Geometry(Bounds bounds, Location location, String locationType, Viewport viewport) {
        this.bounds = bounds;
        this.location = location;
        this.locationType = locationType;
        this.viewport = viewport;
    }

    /**
     * 
     * @return
     *     The bounds
     */
    @JsonProperty("bounds")
    public Bounds getBounds() {
        return bounds;
    }

    /**
     * 
     * @param bounds
     *     The bounds
     */
    @JsonProperty("bounds")
    public void setBounds(Bounds bounds) {
        this.bounds = bounds;
    }

    public Geometry withBounds(Bounds bounds) {
        this.bounds = bounds;
        return this;
    }

    /**
     * 
     * @return
     *     The location
     */
    @JsonProperty("location")
    public Location getLocation() {
        return location;
    }

    /**
     * 
     * @param location
     *     The location
     */
    @JsonProperty("location")
    public void setLocation(Location location) {
        this.location = location;
    }

    public Geometry withLocation(Location location) {
        this.location = location;
        return this;
    }

    /**
     * 
     * @return
     *     The locationType
     */
    @JsonProperty("location_type")
    public String getLocationType() {
        return locationType;
    }

    /**
     * 
     * @param locationType
     *     The location_type
     */
    @JsonProperty("location_type")
    public void setLocationType(String locationType) {
        this.locationType = locationType;
    }

    public Geometry withLocationType(String locationType) {
        this.locationType = locationType;
        return this;
    }

    /**
     * 
     * @return
     *     The viewport
     */
    @JsonProperty("viewport")
    public Viewport getViewport() {
        return viewport;
    }

    /**
     * 
     * @param viewport
     *     The viewport
     */
    @JsonProperty("viewport")
    public void setViewport(Viewport viewport) {
        this.viewport = viewport;
    }

    public Geometry withViewport(Viewport viewport) {
        this.viewport = viewport;
        return this;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }

    @JsonAnyGetter
    public Map<String, Object> getAdditionalProperties() {
        return this.additionalProperties;
    }

    @JsonAnySetter
    public void setAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
    }

    public Geometry withAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
        return this;
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(bounds).append(location).append(locationType).append(viewport).append(additionalProperties).toHashCode();
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if ((other instanceof Geometry) == false) {
            return false;
        }
        Geometry rhs = ((Geometry) other);
        return new EqualsBuilder().append(bounds, rhs.bounds).append(location, rhs.location).append(locationType, rhs.locationType).append(viewport, rhs.viewport).append(additionalProperties, rhs.additionalProperties).isEquals();
    }

}
