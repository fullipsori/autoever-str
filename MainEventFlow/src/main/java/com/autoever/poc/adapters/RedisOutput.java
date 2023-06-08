package com.autoever.poc.adapters;

import org.slf4j.Logger;
import com.streambase.sb.*;
import com.streambase.sb.adapter.*;
import com.streambase.sb.operator.*;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

/**
* Generated by JDT StreamBase Client Templates (Version: 11.0.0).
*
* Output adapters take streams from an application and make them
* available outside of the application. Any implementation of 
* this class <b>must</b> extend class OutputAdapter. Output adapters
* should override method processTuple(int, Tuple) to receive tuples 
* from the application.
* <p>
* For in-depth information on implementing a custom adapter, please
* see "Developing StreamBase Embedded Adapters" in the StreamBase documentation.
* <p>
*/
public class RedisOutput extends OutputAdapter implements Parameterizable {

	public static final long serialVersionUID = 1663719532374L;
	private Schema inSchema;        // input to this operator
	private Schema.Field keyField;
	private Schema.Field valueField;
	private int keyFieldId;
	private int valueFieldId;
	private String keyFieldStr;
	private String valueFieldStr;
	
	static private JedisPool jedisPool;
	private Logger logger;
	
	// Properties
	private int redisPort;
	private String redisHost;
	private String displayName = "Redis Output Adapter";
    private Jedis publisherJedis;
    private JedisPoolConfig poolConfig;

	/**
	* The constructor is called when the Adapter instance is created, but before the Adapter 
	* is connected to the StreamBase application. We recommended that you set the initial input 
	* port and output port count in the constructor by calling setPortHints(inPortCount, outPortCount). 
	* The default is no input ports or output ports. The constructor may also set default values for 
	* adapter parameters. These values will be displayed in StreamBase Studio when a new adapter is 
	* dragged to the canvas, and serve as the default values for omitted optional parameters.
	*/
	public RedisOutput() {
		super();
		logger = getLogger();
		
		setPortHints(1, 0);
		setDisplayName(displayName);
		setShortDisplayName(this.getClass().getSimpleName());
		setRedisPort(6379);
		setRedisHost("127.0.0.1");

	}

	/**
	* Typecheck this adapter. The typecheck method is called after the adapter instance is
	* constructed, and once input ports are connected. The adapter should validate its parameters 
	* and throw PropertyTypecheckException (or TypecheckException) if any problems are found. 
	* The message associated with the thrown exceptions will be displayed in StreamBase Studio during authoring,
	* or printed on the console by sbd. Input adapters should set the schema of each output
	* port by calling the setOutputSchema(portNum, schema) method for each output port. 
	* If the adapter needs to change the number of input ports based on parameter values,
	* it should call requireInputPortCount(portCount) at this point. 
	*/
	public void typecheck() throws TypecheckException {
		// TODO Make sure all properties have valid values.
		logger.debug(getName()+": typecheck");
		requireInputPortCount(1);
		DataType dt;
		
		// the input must contain at least one string field
		String errmsg = "";
		inSchema = getInputSchema(0);
		if (!inSchema.hasField("key")) {
			errmsg += "input field named 'key' required\n";
		} else {
			try {
				keyField = inSchema.getField("key");
			} catch (TupleException e) {
				e.printStackTrace();
			}
			dt = keyField.getDataType();
			keyFieldId = keyField.getIndex();
			if (!DataType.STRING.equals(dt)) {
				errmsg += "input field named 'key' must be a string\n";
			}
		}
		
		if (!inSchema.hasField("value")) {
			errmsg += "input field named 'value' required\n";
		} else {
			try {
				valueField = inSchema.getField("value");
			} catch (TupleException e) {
				e.printStackTrace();
			}
			dt = valueField.getDataType();
			valueFieldId = valueField.getIndex();
			if (!DataType.STRING.equals(dt)) {
				errmsg += "input field named 'value' must be a string\n";
			}
		}
	
		if (!errmsg.isEmpty()) {
			errmsg = "Input must contain string input fields, key and value\n" + errmsg;
			throw new TypecheckException(errmsg);
		}
	}

	/**
	* Initialize the adapter. If typecheck succeeds, the init method is called before
	* the StreamBase application is started. Note that your adapter is not required to
	* define the init method, unless you need to register a runnable or perform
	* initialization of a resource such as, for example, a JDBC pool.
	*/
	public void init() throws StreamBaseException {
		super.init();
        poolConfig = new JedisPoolConfig();
        jedisPool = new JedisPool(poolConfig, redisHost, redisPort, 0);
		publisherJedis = jedisPool.getResource();
		publisherJedis.connect();		
		logger.debug(getName()+": init");
	}

	/**
	* Shutdown adapter by cleaning up any resources used (e.g. close an output file
	* if it has been opened). When the application is shutting down, the adapter's shutdown.
	* method will be called first. Once this has returned, all threads should exit and the
	* adapter is considered shutdown.
	*/
	public void shutdown() {
		logger.debug(getName()+": shutdown");
		publisherJedis.disconnect();
		publisherJedis.close();
		jedisPool.destroy();
	}

	/**
	 * This method will be called by the StreamBase server for each
	 * Tuple given to the adapter to process.  Output adapters should
	 * override this method to process tuples.
	 *<p>
	 * The default implementation does nothing.
	 * 
	 * @param inputPort the input port that the tuple is from (ports are zero based)
	 * @param tuple the tuple from the given input port
	 * @throws StreamBaseException Terminates the application. 
	
	 */
	public void processTuple(int inputPort, Tuple tuple) throws StreamBaseException {
		if (getLogger().isInfoEnabled()) {
			getLogger().info("processing a tuple at input port " + inputPort);
		}
		// TODO Send tuples to external data source
		keyFieldStr = tuple.getString(keyFieldId);
		valueFieldStr = tuple.getString(valueFieldId);
		
		publisherJedis.set(keyFieldStr, valueFieldStr);
		long result = publisherJedis.publish(keyFieldStr, valueFieldStr);
		
		logger.warn(getName() + ": result:" + result + " keyFieldStr:" + keyFieldStr + " valueFieldStr:" + valueFieldStr);
	}

	/***************************************************************************************
	 * The getter and setter methods provided by the Parameterizable object.               *
	 * StreamBase Studio uses them to determine the name and type of each property         *
	 * and obviously, to set and get the property values.                                  *
	 ***************************************************************************************/

	public void setRedisPort(int redisPort) {
		this.redisPort = redisPort;
	}

	public int getRedisPort() {
		return this.redisPort;
	}

	public void setRedisHost(String redisHost) {
		this.redisHost = redisHost;
	}

	public String getRedisHost() {
		return this.redisHost;
	}

	/** For detailed information about shouldEnable methods, see interface Parameterizable java doc 
	 *  @see Parameterizable 
	 */

	public boolean shouldEnableRedisPort() {
		// TODO implement custom enablement logic here
		return true;
	}

	public boolean shouldEnableRedisHost() {
		// TODO implement custom enablement logic here
		return true;
	}

}
