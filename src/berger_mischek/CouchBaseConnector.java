package berger_mischek;

import com.couchbase.client.java.Bucket;
import com.couchbase.client.java.Cluster;
import com.couchbase.client.java.bucket.BucketManager;
import com.couchbase.client.java.error.DesignDocumentAlreadyExistsException;
import com.couchbase.client.java.view.DefaultView;
import com.couchbase.client.java.CouchbaseCluster;
import com.couchbase.client.java.view.DesignDocument;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class CouchBaseConnector {
	
	Bucket bucket;
	Cluster cluster;
	BucketManager bucketManager;

	public void connect(){
		cluster = CouchbaseCluster.create("192.168.74.135");
		bucket = cluster.openBucket("default", 60, TimeUnit.SECONDS);
		bucketManager = bucket.bucketManager();
	}
	
	public void create(){
		try {
			DesignDocument designDoc = DesignDocument.create("personlist",
					Arrays.asList(
							DefaultView.create("be_personlist",
								"function (doc, meta) { "
								+ "emit(meta.id, [doc.firstname, doc.lastname]);"
								+ "}")
							));
			bucketManager.insertDesignDocument(designDoc, true);
		} catch (DesignDocumentAlreadyExistsException e) {
			System.err.print("design document already exists!");
		}
	}

	public void read(){
		DesignDocument designDoc = bucketManager.getDesignDocument("personlist", true);
		System.out.println(designDoc.name() + " contains " + designDoc.views().size() + " view(s)\n");
		List<DesignDocument> designDocs = bucketManager.getDesignDocuments(true);
		System.out.println("the selected bucket '" + bucket.name() + "' contains of " + designDocs.size() + " the following design document(s):");
		for (DesignDocument doc : designDocs) {
			System.out.println("\t" + "the design document " + doc.name() + " has " + doc.views().size() + " view(s)");
		}
	}
	
	public static void main(String []args){
		CouchBaseConnector view = new CouchBaseConnector();
		view.connect();
		view.create();
		view.read();
	}
}
