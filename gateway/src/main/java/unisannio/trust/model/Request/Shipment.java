package unisannio.trust.model.Request;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class Shipment {
    @JsonProperty("amount")
    public List<Integer> amount;

    @JsonProperty("pickup")
    public Pickup pickup;

    @JsonProperty("delivery")
    public Delivery delivery;
}
