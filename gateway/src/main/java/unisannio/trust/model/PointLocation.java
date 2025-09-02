package unisannio.trust.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

import org.bson.codecs.pojo.annotations.BsonIgnore;

@JsonIgnoreProperties({"lon", "lat"})
public class PointLocation {
    @JsonProperty("type")
    public String type;
    @JsonProperty("coordinates")
    public List<Double> coordinates;

    @BsonIgnore
    public Double getLon() {
        return this.coordinates.get(0);
    }

    @BsonIgnore
    public Double getLat() {
        return this.coordinates.get(1);
    }
}
