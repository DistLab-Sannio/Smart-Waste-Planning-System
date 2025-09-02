package unisannio.trust.model;

import java.util.List;
import java.util.Map;

import org.bson.codecs.pojo.annotations.BsonIgnore;
import org.bson.codecs.pojo.annotations.BsonProperty;
import org.bson.types.ObjectId;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import io.quarkus.mongodb.panache.common.MongoEntity;

@MongoEntity(collection="segments")
@JsonIgnoreProperties({"measurements","id"})
public class Segment {
    @JsonProperty("location")
    public LineStringLocation location;
    @JsonProperty("from_osmid")
    @BsonProperty("from_osmid")
    public Long fromOsmid;
    @JsonProperty("from_osmid")
    @BsonProperty("to_osmid")
    public Long toOsmid;
    @JsonProperty("tags")
    public Map<String, String> tags;
    @JsonProperty("measurements")
    public List<Measurement> measurements;
    @JsonProperty("id")
    public ObjectId id; 

    public String toString() {
        return this.location + ";" + this.fromOsmid + ";" + this.toOsmid + ";" + this.tags + ";" + this.id;
    }

}
