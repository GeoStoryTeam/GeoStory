package wu.datastore;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import com.google.gwt.user.client.rpc.AsyncCallback;

public interface RemoteDatastoreAsync {

	void allocateIds(String kind, long num, AsyncCallback<KeyRange> callback);

	void allocateIds(Key parent, String kind, long num,
			AsyncCallback<KeyRange> callback);

	void beginTransaction(AsyncCallback<Transaction> callback);

	void delete(Key[] keys, AsyncCallback<Void> callback);

	void delete(Iterable<Key> keys, AsyncCallback<Void> callback);

	void delete(Transaction txn, Key[] keys, AsyncCallback<Void> callback);

	void delete(Transaction txn, Iterable<Key> keys,
			AsyncCallback<Void> callback);

	void get(Key key, AsyncCallback<Entity> callback);

	void get(Iterable<Key> keys, AsyncCallback<Map<Key, Entity>> callback);

	void get(Transaction txn, Key key, AsyncCallback<Entity> callback);

	void get(Transaction txn, Iterable<Key> keys,
			AsyncCallback<Map<Key, Entity>> callback);

	void getActiveTransactions(AsyncCallback<Collection<Transaction>> callback);

	void getCurrentTransaction(AsyncCallback<Transaction> callback);

	void getCurrentTransaction(Transaction returnedIfNoTxn,
			AsyncCallback<Transaction> callback);

	void prepare(Query query, AsyncCallback<PreparedQuery> callback);

	void prepare(Transaction txn, Query query,
			AsyncCallback<PreparedQuery> callback);

	void put(Entity entity, AsyncCallback<Key> callback);

	void put(Iterable<Entity> entities, AsyncCallback<List<Key>> callback);

	void put(Transaction txn, Entity entity, AsyncCallback<Key> callback);

	void put(Transaction txn, Iterable<Entity> entities,
			AsyncCallback<List<Key>> callback);

}
