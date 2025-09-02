db = db.getSiblingDB("trust")
db.createCollection("segments")

var file = fs.readFileSync("/initdb/segments.json","utf8")
var lines = file.split('\n')

var obj = NaN
for (var i = 0, l = lines.length; i < l; i++){
    obj = JSON.parse(lines[i])
    obj["_id"] = new ObjectId(obj["_id"])
    obj["measurements"] = []
    db.segments.insertOne(obj); 
}