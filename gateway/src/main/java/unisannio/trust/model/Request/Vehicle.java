package unisannio.trust.model.Request;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class Vehicle {
    @JsonProperty("id")
    public Integer id;

    @JsonProperty("start")
    public List<Double> start;

    @JsonProperty("end")
    public List<Double> end;

    @JsonProperty("capacity")
    public List<Integer> capacity;

    @JsonProperty("costs")
    public Costs costs;

    @JsonProperty("time_window")
    public List<Integer> timeWindow;
}
