package wu.server.datastore;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import wu.datastore.Entity;
import wu.datastore.EntityNotFoundException;
import wu.datastore.Key;
import wu.datastore.KeyRange;
import wu.datastore.PreparedQuery;
import wu.datastore.Query;
import wu.datastore.RemoteDatastore;
import wu.datastore.Transaction;

import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;

public class RemoteDatastoreImpl extends RemoteServiceServlet  implements RemoteDatastore {

	public KeyRange allocateIds(String kind, long num) {
		// TODO Auto-generated method stub
		System.out.println("Call to allocateIds");
		return null;
	}

	public KeyRange allocateIds(Key parent, String kind, long num) {
		// TODO Auto-generated method stub
		return null;
	}

	public Transaction beginTransaction() {
		// TODO Auto-generated method stub
		return null;
	}

	public void delete(Key... keys) {
		System.out.println("Call to delete");
		// TODO Auto-generated method stub
		
	}

	public void delete(Iterable<Key> keys) {
		System.out.println("Call to delete");
		List<com.google.appengine.api.datastore.Key> k = 
			new ArrayList<com.google.appengine.api.datastore.Key>();
		for (wu.datastore.Key key : keys){
			k.add(Conversion.convert(key));
		}
		DatastoreServiceFactory.getDatastoreService().delete(k);
		// TODO Auto-generated method stub
	}

	public void delete(Transaction txn, Key... keys) {
		System.out.println("Call to delete");
		// TODO Auto-generated method stub
		
	}

	public void delete(Transaction txn, Iterable<Key> keys) {
		System.out.println("Call to delete");
		// TODO Auto-generated method stub
		
	}

	public Entity get(Key key) throws EntityNotFoundException {
		System.out.println("Call to get");
		// TODO Auto-generated method stub
		return null;
	}

	public Map<Key, Entity> get(Iterable<Key> keys) {
		System.out.println("Call to get");
		// TODO Auto-generated method stub
		return null;
	}

	public Entity get(Transaction txn, Key key) throws EntityNotFoundException {
		System.out.println("Call to get");
		// TODO Auto-generated method stub
		return null;
	}

	public Map<Key, Entity> get(Transaction txn, Iterable<Key> keys) {
		System.out.println("Call to get");
		// TODO Auto-generated method stub
		return null;
	}

	public Collection<Transaction> getActiveTransactions() {
		System.out.println("Call to getActiveTransactions");
		// TODO Auto-generated method stub
		return null;
	}

	public Transaction getCurrentTransaction() {
		System.out.println("Call to getCurrentTransaction");
		// TODO Auto-generated method stub
		return null;
	}

	public Transaction getCurrentTransaction(Transaction returnedIfNoTxn) {
		System.out.println("Call to getCurrentTransaction");
		
		// TODO Auto-generated method stub
		return null;
	}

	public PreparedQuery prepare(Query query) {
		System.out.println("Call to prepare");
		com.google.appengine.api.datastore.Query q = Conversion.convert(query);
		com.google.appengine.api.datastore.PreparedQuery pq = DatastoreServiceFactory.getDatastoreService().prepare(q);
		// TODO Auto-generated method stub
		return Conversion.convert(pq);
	}

	public PreparedQuery prepare(Transaction txn, Query query) {
		System.out.println("Call to prepare");
		// TODO Auto-generated method stub
		return null;
	}

	public Key put(Entity entity) {
		com.google.appengine.api.datastore.Entity ent = Conversion.convert(entity);
		com.google.appengine.api.datastore.Key key = DatastoreServiceFactory.getDatastoreService().put(ent);
		System.out.println("\nCall to put "+entity+"\n BECOMES \n"+ent+"  --   "+key);
		return Conversion.convert(key);
	}

	public List<Key> put(Iterable<Entity> entities) {
		System.out.println("Call to put");
		// TODO Auto-generated method stub
		return null;
	}

	public Key put(Transaction txn, Entity entity) {
		System.out.println("Call to put");
		// TODO Auto-generated method stub
		return null;
	}

	public List<Key> put(Transaction txn, Iterable<Entity> entities) {
		System.out.println("Call to put");
		// TODO Auto-generated method stub
		return null;
	}

}
