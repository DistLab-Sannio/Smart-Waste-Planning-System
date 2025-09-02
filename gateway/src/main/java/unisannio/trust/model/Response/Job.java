package unisannio.trust.model.Response;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Job extends Step{
    @JsonProperty("id")
    public int id;
}
