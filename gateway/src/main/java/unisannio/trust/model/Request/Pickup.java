package unisannio.trust.model.Request;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class Pickup {
    @JsonProperty("id")
    public Integer id;

    @JsonProperty("service")
    public Integer service;

    @JsonProperty("location")
    public List<Double> location;
}
