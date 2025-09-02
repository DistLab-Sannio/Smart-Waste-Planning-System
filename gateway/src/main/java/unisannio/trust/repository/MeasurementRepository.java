package unisannio.trust.repository;

import io.quarkus.mongodb.panache.PanacheMongoRepository;
import jakarta.enterprise.context.ApplicationScoped;
import unisannio.trust.model.Measurement;

@ApplicationScoped
public class MeasurementRepository implements PanacheMongoRepository<Measurement> {
    public void insert(Measurement point) {
        persist(point);
    }

    public void delete() {
        deleteAll();
    }

}
