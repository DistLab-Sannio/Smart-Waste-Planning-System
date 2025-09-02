package unisannio.trust.model.Response;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class Step {
    @JsonProperty("type")
    public String type;
    @JsonProperty("location")
    public List<Double> location;
    @JsonProperty("setup")
    public int setup;
    @JsonProperty("service")
    public int service;
    @JsonProperty("waiting_time")
    public int waitingTime;
    @JsonProperty("load")
    public List<Integer> load;
    @JsonProperty("arrival")
    public int arrival;
    @JsonProperty("duration")
    public int duration;
    @JsonProperty("violations")
    public List<Integer> violations;
    @JsonProperty("distance")
    public int distance;
}
