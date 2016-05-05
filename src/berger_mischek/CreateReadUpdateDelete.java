package berger_mischek;


import com.couchbase.client.java.CouchbaseCluster;
import com.couchbase.client.java.Bucket;
import com.couchbase.client.java.Cluster;
import java.util.concurrent.TimeUnit;
import com.couchbase.client.java.document.JsonDocument;
import com.couchbase.client.java.document.json.JsonObject;

public class CreateReadUpdateDelete {
	
	public static void main(String []args){
		
		// connect to database and bucket
		Cluster cluster = CouchbaseCluster.create("192.168.74.135");
		Bucket bucket = cluster.openBucket("default", 60, TimeUnit.SECONDS);
		
		JsonObject person1 = JsonObject.empty()
				.put("firstname", "Benedikt")
				.put("lastname", "Berger")
				.put("age", 18)
				.put("city", "Wien");
		
		JsonDocument doc1 = JsonDocument.create("bberger", person1);
		JsonDocument response = bucket.upsert(doc1);
		
	}
	
}
