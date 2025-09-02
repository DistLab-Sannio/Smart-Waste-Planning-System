package unisannio.trust.model.Response;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ComputingTimes {
    @JsonProperty("loading")
    public int loading;
    @JsonProperty("solving")
    public int solving;
    @JsonProperty("routing")
    public int routing;
}
