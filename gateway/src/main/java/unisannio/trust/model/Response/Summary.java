package unisannio.trust.model.Response;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class Summary {

    @JsonProperty("cost")
    public int cost;
    @JsonProperty("routes")
    public int routes;
    @JsonProperty("unassigned")
    public int unassigned;
    @JsonProperty("delivery")
    public List<Integer> delivery;
    @JsonProperty("amount")
    public List<Integer> amount;
    @JsonProperty("pickup")

    public List<Integer> pickup;
    @JsonProperty("setup")
    public int setup;
    @JsonProperty("service")
    public int service;
    @JsonProperty("duration")
    public int duration;
    @JsonProperty("waiting_time")
    public int waitingTime;
    @JsonProperty("priority")
    public int priority;
    @JsonProperty("distance")
    public int distance;
    @JsonProperty("violations")
    public List<Integer> violations;
    @JsonProperty("computing_times")
    public ComputingTimes computingTimes;

}
