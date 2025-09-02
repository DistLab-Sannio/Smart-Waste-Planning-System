package unisannio.trust.model;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

public class LineStringLocation {
    @JsonProperty("type")
    public String type;
    @JsonProperty("coordinates")
    public List<List<Double>> coordinates;

}
