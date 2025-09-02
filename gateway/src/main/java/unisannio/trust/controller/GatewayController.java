package unisannio.trust.controller;

import unisannio.trust.model.Measurement;
import unisannio.trust.model.Segment;
import unisannio.trust.model.Request.SolverRequest;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.io.*;
import java.util.concurrent.ExecutionException;

@Path("trust/api/v1")
public interface GatewayController {

    //Planner
    @POST
    @Path("/planner/problems")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response createProblem(SolverRequest request);

    @GET
    @Path("/planner/plans")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getPlan(@QueryParam("problem") Integer problemId);

    @DELETE
    @Path("/planner/plans")
    public Response deleteProblem(@QueryParam("problem") int problem);

    //Citydata
    @POST
    @Path("/citydata/environmentconditions/batches")
    @Consumes(MediaType.TEXT_PLAIN)
    public Response updateConditions(File file) throws IOException, ExecutionException, InterruptedException;

    /*@POST
    @Path("/citydata/environmentconditions/measurements")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response createMeasurement(Measurement measurement);

    @DELETE
    @Path("/citydata/environmentconditions/measurements")
    public Response deleteMeasurements();*/

    //Map
    /*@GET
    @Path("/citydata/segments")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getSegmentByLonLat(@QueryParam("lon") Double lon, @QueryParam("lat") Double lat, @QueryParam("type") String type);*/

    /*@POST
    @Path("/citydata/segments")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response createSegment(Segment segment);*/
}
