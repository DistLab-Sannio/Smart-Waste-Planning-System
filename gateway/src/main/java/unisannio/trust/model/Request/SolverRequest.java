package unisannio.trust.model.Request;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class SolverRequest {

    @JsonProperty("shipments")
    public List<Shipment> shipments;
    @JsonProperty("options")
    public Options options;
    @JsonProperty("vehicles")
    public List<Vehicle> vehicles;
}
