package com.autoever.poc.parser.gps;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import com.autoever.poc.common.TimeUtils;
import com.streambase.sb.*;
import com.streambase.sb.operator.*;

/**
 * Generated by JDT StreamBase Client Templates (Version: 11.0.0).
 *
 * This class is used as a Java Operator in a StreamBase application.
 * One instance will be created for each Java Operator in a StreamBase 
 * application. 
 * <p>
 * Enqueue methods should only be called from processTuple.
 * @see Parameterizable
 * @see Operator
 * For in-depth information on implementing a custom Java Operator, please see
 * "Developing StreamBase Java Operators" in the StreamBase documentation.
 */
public class GPSDataParser extends Operator implements Parameterizable {

	public static final long serialVersionUID = 1664950870708L;
	private String displayName = "GPS Data Parser";
	// Local variables
	private int inputPorts = 1;
	private int outputPorts = 1;

	private Schema outputSchema;
	private Schema gpsParserSchema;
	private Schema.Field dataList;
	private Schema.Field messageID;
	private Schema.Field kafkaMessage;
	private Schema.Field filePath;
	private Schema.Field binaryData;
	private Schema dataSchema;

	/**
	* The constructor is called when the Operator instance is created, but before the Operator 
	* is connected to the StreamBase application. We recommended that you set the initial input
	* port and output port count in the constructor by calling setPortHints(inPortCount, outPortCount).
	* The default is 1 input port, 1 output port. The constructor may also set default values for 
	* operator parameters. These values will be displayed in StreamBase Studio when a new instance
	* of this operator is  dragged to the canvas, and serve as the default values for omitted
	* optional parameters.
	 */
	public GPSDataParser() {
		super();
		setPortHints(inputPorts, outputPorts);
		setDisplayName(displayName);
		setShortDisplayName(this.getClass().getSimpleName());

	}

	/**
	* The typecheck method is called after the Operator instance is connected in the StreamBase
	* application, allowing the Operator to validate its properties. The Operator class may 
	* change the number of input or output ports by calling the requireInputPortCount(portCount)
	* method or the setOutputSchema(schema, portNum) method. If the verifyInputPortCount method 
	* is passed a different number of ports than the Operator currently has, a PortMismatchException
	* (subtype of TypecheckException) is thrown.
	*/
	public void typecheck() throws TypecheckException {
		// typecheck: require a specific number of input ports
		requireInputPortCount(inputPorts);

		// TODO Ensure that all properties have valid values, and typecheck the input schemas here
		try {

			Schema inputSchema = getInputSchema(0);
			Schema kafkaSchema = getNamedSchema("KafkaVDMSDataSchema");
			gpsParserSchema = getNamedSchema("GPSParserSchema");
			dataSchema = getNamedSchema("GPSParserDataSchema");
            
			kafkaMessage = inputSchema.getField("kafkaMessage");
			kafkaMessage.checkType(CompleteDataType.forTuple(kafkaSchema));
			filePath = inputSchema.getField("filePath");
			filePath.checkType(CompleteDataType.forString());
			binaryData = inputSchema.getField("binaryData");
			binaryData.checkType(CompleteDataType.forBlob());
			
			messageID = inputSchema.getField("messageID");
			messageID.checkType(CompleteDataType.forString());

			dataList = gpsParserSchema.getField("dataList");
			dataList.checkType(CompleteDataType.forList(CompleteDataType.forTuple(dataSchema)));

			setOutputSchema(0, gpsParserSchema);
		} catch (TupleException e) {
			throw new TypecheckException(e);
		}

	}

	public void init() throws StreamBaseException {
		super.init();
		outputSchema = getRuntimeOutputSchema(0);
	}

	/**
	* This method will be called by the StreamBase server for each Tuple given
	* to the Operator to process. This is the only time an operator should 
	* enqueue output Tuples.
	* @param inputPort the input port that the tuple is from (ports are zero based)
	* @param tuple the tuple from the given input port
	* @throws StreamBaseException Terminates the application.
	*/
	public void processTuple(int inputPort, Tuple tuple) throws StreamBaseException {
		Tuple kafkaMsg = tuple.getTuple(kafkaMessage);

		String path = tuple.getString(filePath);
		ByteArrayView binData = tuple.getBlobBuffer(binaryData);
		Tuple output = outputSchema.createTuple();

		try {
			byte[] allBytes = null;
			if(path != null && !path.isEmpty()) {
				Path file = Paths.get(path);
				allBytes = Files.readAllBytes(file);
				Files.delete(file);
			}else if(binData != null && binData.length()>0) {
				allBytes = binData.array();
			}else {
				return;
			}

			if(allBytes != null && allBytes.length > 0) {
				output.setString("messageID", tuple.getString(messageID));
				GPSData gpsData = new GPSData(kafkaMsg, allBytes, dataSchema);
//				System.out.println("messageID:" + tuple.getString(messageID) + " GPS Data Count:" + gpsData.gpsData.size());
				if(gpsData.gpsData.size() > 0) {
					output.setField(dataList, gpsData.gpsData);
				}
			} 
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} finally {
			sendOutput(0, output);
		}
		
	}
	
}

