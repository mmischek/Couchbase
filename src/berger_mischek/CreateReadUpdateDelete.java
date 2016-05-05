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
		
		// the JsonObject person1 will be created and filled with the necessary data
		JsonObject person1 = JsonObject.empty()
				.put("firstname", "Benedikt")
				.put("lastname", "Berger")
				.put("age", 18)
				.put("city", "Wien");
		
		// creating json document with person1
		JsonDocument doc1 = JsonDocument.create("bberger", person1);
		// insert the json document into the bucket 
		JsonDocument response1 = bucket.upsert(doc1);
		
		// the JsonObject person1 will be created and filled with the necessary data
		JsonObject person2 = JsonObject.empty()
				.put("firstname", "Matthias")
				.put("lastname", "Mischek")
				.put("age", 18)
				.put("city", "Korneuburg");
		
		// creating json document with person1
		JsonDocument doc2 = JsonDocument.create("mmischek", person1);
		// insert the json document into the bucket 
		JsonDocument response2 = bucket.upsert(doc1);
		
		
		JsonDocument bberger = bucket.get("bberger");
		if (bberger == null) {
			System.err.println("Document not found!");
		} else {
			// reading data
			System.out.println("Last name: " + bberger.content().getString("lastname"));
			System.out.println("First name: " + bberger.content().getString("firstname"));
			System.out.println("Age: " + bberger.content().getInt("age"));
			
			// update bberger with a new age
			bberger.content().put("Age", 18);
			// update bucket
			JsonDocument updated = bucket.replace(bberger);
			
			System.out.println("Age updated");
			System.out.println("Age: " + bberger.content().getInt("age"));
		}
		
		cluster.disconnect();
		
	}
	
}
