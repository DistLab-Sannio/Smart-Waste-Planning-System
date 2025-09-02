package unisannio.trust.model.Request;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class Delivery {
    @JsonProperty("id")
    public Integer id;

    @JsonProperty("location")
    public List<Double> location;
}
