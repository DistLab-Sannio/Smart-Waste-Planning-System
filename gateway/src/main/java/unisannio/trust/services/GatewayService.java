package unisannio.trust.services;

import unisannio.trust.SolverCaller;
import unisannio.trust.controller.GatewayController;
import unisannio.trust.model.Measurement;
import unisannio.trust.model.Segment;
import unisannio.trust.model.Request.SolverRequest;
import unisannio.trust.model.Response.SolverResponse;
import unisannio.trust.repository.MeasurementRepository;
import unisannio.trust.repository.SegmentRepository;
import unisannio.trust.services.osrm.OsrmContainerManager;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.Response.Status;

import org.bson.BsonArray;
import org.bson.BsonDocument;
import org.bson.BsonDouble;
import org.bson.BsonString;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.eclipse.microprofile.rest.client.inject.RestClient;

import com.mongodb.client.model.Indexes;

import java.io.*;
import java.net.URI;
import java.util.Arrays;
import java.util.Map;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

@Path("trust/api/v1")
public class GatewayService implements GatewayController{
    @Inject
    MeasurementRepository measurementRepository;
    @Inject
    SegmentRepository segmentRepository;

    @ConfigProperty(name = "baseuriIP")
    private String baseUriIp;
    @ConfigProperty(name = "baseuriPORT")
    private String baseUriPort;
    private static final double MIN_DISTANCE = 0.01; 
    private static final double MAX_DISTANCE = 10; 
    private AtomicInteger atomicInteger = new AtomicInteger(0);
    private final SolverCaller solverCaller;
    private Map<Integer, SolverResponse> responses;
    
    // Executor service per eseguire il lavoro in background
    private final ExecutorService executorService = Executors.newFixedThreadPool(10);


    public GatewayService(@RestClient SolverCaller solverCaller) {
        this.solverCaller = solverCaller;
        this.responses = new ConcurrentHashMap<Integer, SolverResponse>();
    }

    public Response deleteProblem(@QueryParam("problem") int problem) {
        if(responses.remove(problem) != null) {
            return Response.noContent().build();
        }
        return Response.status(Response.Status.NOT_FOUND).build();
    }

    public Response createProblem(SolverRequest request) {
        int currentProblem = atomicInteger.getAndIncrement();
        executorService.submit(new ProblemSolver(request, solverCaller, this, currentProblem));
        return Response.created(URI.create("http://" + baseUriIp + ":" + baseUriPort + "/trust/api/v1/planner/plans?problem=" + currentProblem)).build();
    }

    public Response getPlan(@QueryParam("problem") Integer problemId) {
        SolverResponse solverResponse = null;
        if(responses.containsKey(problemId)){
            solverResponse = responses.get(problemId);
            return Response.status(Response.Status.OK).entity(solverResponse).build();
        }
        return Response.status(Response.Status.NOT_FOUND).build();
    }

    public Response updateConditions(File file) throws IOException, ExecutionException, InterruptedException {
        OsrmContainerManager ocm = new OsrmContainerManager(); //why not a bean?
        ocm.update(file);
        return Response.status(Response.Status.NO_CONTENT).build();
    }

    public Response deleteMeasurements(){
        this.measurementRepository.deleteAll();
        return Response.status(Response.Status.NO_CONTENT).build();
    }

    private Segment getNearestSegmentByLonLat(Double lon, Double lat) {
        this.segmentRepository.mongoCollection().createIndex(Indexes.geo2dsphere("location"));
        BsonDocument geometry = new BsonDocument().append("type", new BsonString("Point")).append("coordinates", new BsonArray(Arrays.asList(new BsonDouble(lon), new BsonDouble(lat))));
        BsonDocument nearSphere = new BsonDocument().append("$geometry", geometry).append("$minDistance", new BsonDouble(MIN_DISTANCE)).append("$maxDistance", new BsonDouble(MAX_DISTANCE));
        BsonDocument query = new BsonDocument().append("location", new BsonDocument().append("$nearSphere", nearSphere));
        return this.segmentRepository.find(query).firstResult();
    }

    public Response createMeasurement(Measurement measurement) {
        Segment segment = getNearestSegmentByLonLat(measurement.location.getLon(), measurement.location.getLat());
        if(segment != null) {
            segment.measurements.add(measurement);
            measurement.segment = segment;
            this.measurementRepository.insert(measurement);
            this.segmentRepository.persistOrUpdate(segment);
            return Response.created(URI.create("http://" + baseUriIp + ":" + baseUriPort + "/trust/api/v1//citydata/environmentconditions/measurement/" + measurement.id)).build();
        } else {
            return Response.status(Status.INTERNAL_SERVER_ERROR).entity("No segment found!").build();
        }
    }

    /*public Response createSegment(Segment segment) {
        this.segmentRepository.insert(segment);
        return Response.created(URI.create("http://" + baseUriIp + ":" + baseUriPort + "/trust/api/v1/citydata/segments/" + segment.getId())).build();
    }*/

    private class ProblemSolver implements Runnable {
        private SolverRequest request;
        private SolverCaller solverCaller;
        private GatewayService endpoint;
        private int currentProblem;
        public ProblemSolver(SolverRequest request, SolverCaller solverCaller, GatewayService endpoint, int currentProblem) {
            this.request = request;
            this.solverCaller = solverCaller;
            this.endpoint = endpoint;
            this.currentProblem = currentProblem;
        }

        @Override
        public void run() {
            SolverResponse response = this.solverCaller.sendToPlanner(request);
            endpoint.responses.put(currentProblem, response);
        }
    }
    
}
