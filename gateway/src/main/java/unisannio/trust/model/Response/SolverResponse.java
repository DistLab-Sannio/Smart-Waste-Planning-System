package unisannio.trust.model.Response;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class SolverResponse {
    @JsonProperty("code")
    public int code;
    @JsonProperty("summary")
    public Summary summary;
    @JsonProperty("unassigned")
    public List<Integer> unassigned;
    @JsonProperty("routes")
    public List<Route> routes;

}
