package wu.datastore;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

/**
 * An interface to mimick the behaviour of the server side datastore low level API
 * 
 * Designed to facilitate the design of an application without defining many JDO classes and their GWT RPC counterparts.
 * 
 * Hardest thing to do is to emulate synchronicity/transactions but maybe we do not have to:
 * - do it aynchronous
 * - attach an id (long) to any call that should return
 * - link the response with the pending request
 * - fucking prepared query are not real objects but proxies to access many results (keep them on server until request is done) 
 * 	+ solution just force a list of result no matter what, handle very large result in some way.
 * 
 * TODO:
 * - mimick all classes with GWT RPCable counterparts          start with Key, entity, query
 * - do a server side implementation that
 * 	+ accept requests
 * 	+ translates RPC types to the actual classes
 *  + do the action 
 *  + translates the result back into RPC counterparts
 *  
 * Extensions:
 * - add some server side post treatment in a query so that no important information gets out.
 * - add some authenticated access to prevent regular web browser to reach critical information.
 * 
 * @author joris
 *
 */
@RemoteServiceRelativePath("datastore")
public interface RemoteDatastore extends RemoteService{

	public KeyRange allocateIds(String kind, long num);

	public KeyRange allocateIds(Key parent, String kind, long num);

	public Transaction beginTransaction() ;

	public void delete(Key... keys) ;

	public void delete(Iterable<Key> keys) ;

	public void delete(Transaction txn, Key... keys) ;

	public void delete(Transaction txn, Iterable<Key> keys) ;

	public Entity get(Key key) throws EntityNotFoundException ;

	public Map<Key, Entity> get(Iterable<Key> keys) ;

	public Entity get(Transaction txn, Key key) throws EntityNotFoundException ;

	public Map<Key, Entity> get(Transaction txn, Iterable<Key> keys) ;

	public Collection<Transaction> getActiveTransactions() ;

	public Transaction getCurrentTransaction() ;

	public Transaction getCurrentTransaction(Transaction returnedIfNoTxn) ;

	public PreparedQuery prepare(Query query) ;

	public PreparedQuery prepare(Transaction txn, Query query) ;

	public Key put(Entity entity) ;

	public List<Key> put(Iterable<Entity> entities) ;

	public Key put(Transaction txn, Entity entity) ;

	public List<Key> put(Transaction txn, Iterable<Entity> entities) ;

}
