package unisannio.trust.model.Request;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Costs {
    @JsonProperty("fixed")
    public Double fixed;

    @JsonProperty("per_km")
    public Double perKm;

    @JsonProperty("per_hour")
    public Double perHour;
}
