package unisannio.trust.repository;

import io.quarkus.mongodb.panache.PanacheMongoRepository;
import jakarta.enterprise.context.ApplicationScoped;
import unisannio.trust.model.Segment;

@ApplicationScoped
public class SegmentRepository implements PanacheMongoRepository<Segment> {
    public void insert(Segment segment) {
        persist(segment);
    }

    public void delete() {
        deleteAll();
    }

}
