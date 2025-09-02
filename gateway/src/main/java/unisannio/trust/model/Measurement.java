package unisannio.trust.model;

import org.bson.codecs.pojo.annotations.BsonId;
import org.bson.codecs.pojo.annotations.BsonIgnore;
import org.bson.types.ObjectId;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.quarkus.mongodb.panache.common.MongoEntity;

@MongoEntity(collection="measurements")
@JsonIgnoreProperties({"segment","id"})
public class Measurement {
    @JsonProperty("location")
    public PointLocation location;
    @JsonProperty("pollutionIndex")
    public double pollutionIndex;
    @JsonProperty("timestamp")
    public long timestamp;
    @JsonProperty("segment")
    @BsonIgnore
    public Segment segment;
    @JsonProperty("id")
    public ObjectId id; 
}
