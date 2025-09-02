package unisannio.trust;

import jakarta.ws.rs.POST;
import unisannio.trust.model.Request.SolverRequest;
import unisannio.trust.model.Response.SolverResponse;

import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;

@RegisterRestClient(configKey = "vroom")
public interface SolverCaller {

    @POST
    SolverResponse sendToPlanner(SolverRequest request);
}
