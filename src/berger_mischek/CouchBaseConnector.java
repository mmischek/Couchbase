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

/**
 * This CouchBaseConnector connects to the couchbase cluster via the ip-address
 * and inserts a new design document. Furthermore it reads these inserted design
 * document and prints out the amount of views it contains
 * 
 * @author Benedikt
 * @version 1.0
 */
public class CouchBaseConnector {

	Bucket bucket;
	Cluster cluster;
	BucketManager bucketManager;

	/**
	 * connect provides the necessary bucket and bucketManager by entering the
	 * IP-address
	 */
	public void connect() {
		cluster = CouchbaseCluster.create("192.168.74.135");
		bucket = cluster.openBucket("default", 60, TimeUnit.SECONDS);
		bucketManager = bucket.bucketManager();
	}

	/**
	 * the create method creates a new designDocument, containing the new view.
	 * This design Document will be also inserted into the bucketManager
	 */
	public void create() {
		try {
			DesignDocument designDoc = DesignDocument.create("personlist",
					Arrays.asList(DefaultView.create("be_personlist",
							"function (doc, meta) { " + 
									"emit(meta.id, [doc.firstname, doc.lastname]);" + 
									"}")));
			bucketManager.insertDesignDocument(designDoc, true);
		} catch (DesignDocumentAlreadyExistsException e) {
			System.err.print("design document already exists!");
		}
	}

	/**
	 * reads the inserted design document "personlist" and prints the size of
	 * views in the console
	 */
	public void read() {
		// get the design document with the name "personlist"
		DesignDocument designDoc = bucketManager.getDesignDocument("personlist", true);
		// print out the amount of views of this design document
		System.out.println(designDoc.name() + " contains " + designDoc.views().size() + " view(s)\n");
		List<DesignDocument> designDocs = bucketManager.getDesignDocuments(true);
		System.out.println("the selected bucket '" + bucket.name() + "' contains of " + designDocs.size()
				+ " the following design document(s):");
		for (DesignDocument doc : designDocs) {
			System.out.println("\t" + "the design document " + doc.name() + " has " + doc.views().size() + " view(s)");
		}
	}

	/**
	 * creates a new couchBaseConnector and calls the needed methods
	 * 
	 * @param args
	 *            main-method parameter
	 */
	public static void main(String[] args) {
		CouchBaseConnector view = new CouchBaseConnector();
		view.connect();
		view.create();
		view.read();
	}
}
