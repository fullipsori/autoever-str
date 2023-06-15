package com.autoever.poc.adapters;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.autoever.poc.common.NumUtils;
import com.autoever.poc.common.RawDataField;
import com.autoever.poc.parser.AutoDataHeaderField;
import com.autoever.poc.parser.DefaultPreProcessor;
import com.autoever.poc.parser.PreProcessable;
import com.autoever.poc.parser.can.CanPreProcessor;
import com.autoever.poc.parser.ccp.CCPPreProcessor;
import com.autoever.poc.parser.gps.GPSPreProcessor;
import com.streambase.sb.*;
import com.streambase.sb.operator.*;
import com.streambase.sb.util.Base64;

/**
 * Generated by JDT StreamBase Client Templates (Version: 11.0.1).
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
public class VdmsRawParser extends Operator implements Parameterizable {

	public static final long serialVersionUID = 1685696973117L;
	// Properties
	private parserTypeEnum parserType;

	// Enum definition for property parserType 
	public static enum parserTypeEnum {
		DEFAULT("default"), CAN("CAN"), CCP("CCP"), GPS("GPS");

		private final String rep;

		private parserTypeEnum(String s) {
			rep = s;
		}

		public String toString() {
			return rep;
		}
	}

	private String displayName = "Vdms Message Parser";
	// Local variables
	private int inputPorts = 1;
	private int outputPorts = 2;

	private Schema OutputSchema = null;
	private Schema rawHeaderSchema = null;
	private Schema StatusSchema = null;
	private PreProcessable preprocessor = null;

	/**
	* The constructor is called when the Operator instance is created, but before the Operator 
	* is connected to the StreamBase application. We recommended that you set the initial input
	* port and output port count in the constructor by calling setPortHints(inPortCount, outPortCount).
	* The default is 1 input port, 1 output port. The constructor may also set default values for 
	* operator parameters. These values will be displayed in StreamBase Studio when a new instance
	* of this operator is  dragged to the canvas, and serve as the default values for omitted
	* optional parameters.
	 */
	public VdmsRawParser() {
		super();
		setPortHints(inputPorts, outputPorts);
		setDisplayName(displayName);
		setShortDisplayName(this.getClass().getSimpleName());
		setParserType(parserTypeEnum.DEFAULT);

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
			Schema kafkaSchema = getNamedSchema("KafkaVDMSDataSchema");
			Schema inputSchema = getInputSchema(0);
			inputSchema.getField("kafkaMessage").checkType(CompleteDataType.forTuple(kafkaSchema));
			inputSchema.getField("binaryData").checkType(CompleteDataType.forBlob());
			inputSchema.getField("filePath").checkType(CompleteDataType.forString());

			ArrayList<Schema.Field> outputSchemaField = new ArrayList<>();
			rawHeaderSchema = getNamedSchema("VDMSBaseRaw");

			outputSchemaField.add(new Schema.Field("IsStarted", CompleteDataType.forBoolean()));
			outputSchemaField.add(new Schema.Field("IsEnded", CompleteDataType.forBoolean()));
			outputSchemaField.add(new Schema.Field("MSGIdx", CompleteDataType.forInt()));
			outputSchemaField.add(new Schema.Field("RawHeader", CompleteDataType.forTuple(rawHeaderSchema)));
			
			if(getParserType() == parserTypeEnum.CAN) {
				CanPreProcessor.addSchemaField(outputSchemaField);
			}else if(getParserType() == parserTypeEnum.CCP) {
				CCPPreProcessor.addSchemaField(outputSchemaField);
			}else if(getParserType() == parserTypeEnum.GPS) {
				GPSPreProcessor.addSchemaField(outputSchemaField);
			}else {
				DefaultPreProcessor.addSchemaField(outputSchemaField);
			}
			outputSchemaField.add(new Schema.Field("PassThroughs", CompleteDataType.forTuple(inputSchema)));
				
			Schema outputSchema = new Schema(null, outputSchemaField);

			List<Schema.Field> fields = List.of(
				new Schema.Field("TerminalID", CompleteDataType.forString()),
				new Schema.Field("MessageType", CompleteDataType.forInt()),
				new Schema.Field("GenCount", CompleteDataType.forInt())
			);
			Schema statusSchema = new Schema(null, fields);

			setOutputSchema(0, outputSchema);
			setOutputSchema(1, statusSchema);
		} catch(TupleException e) {
			throw new TypecheckException(e);
		}

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
		if (inputPort > 0) {
			getLogger().info("operator skipping tuple at input port" + inputPort);
			return;
		}

		String filePath = tuple.getString("filePath");
		Tuple kafkaMessage = tuple.getTuple("kafkaMessage");
		ByteArrayView binData = null;
		try{
			binData = tuple.getBlobBuffer("binaryData");
		}catch(Exception e) {
			binData = null;
		}

		try {
			byte[] allBytes = null;
			if(binData != null && binData.length()>0) {
				allBytes = binData.array();
			}else if(filePath != null && !filePath.isEmpty()) {
				Path file = Paths.get(filePath);
				allBytes = Files.readAllBytes(file);
//				Files.delete(file);
			}else {
				return;
			}

			if(allBytes != null && allBytes.length > 0) {
				List<Tuple> tuples = GetTuples(tuple, allBytes);
				if(tuples != null && !tuples.isEmpty()) {
					sendOutput(0, tuples);
					
					Tuple stuple = StatusSchema.createTuple();
					stuple.setField(0, kafkaMessage.getString("TerminalID"));
					stuple.setField(1, kafkaMessage.getInt("MessageType"));
					stuple.setField(2, tuples.size() -2);  // 2 : startTuple, endTuple 

					try {
						sendOutput(1, stuple);
					} catch (StreamBaseException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} 
		
		
	}

	public List<Tuple> GetTuples(Tuple inputTuple, byte[] hcpMessage) {

		final int[] dlcSize = {0,1,2,3,4,5,6,7,8,12,16,20,24,32,48,64};
		final int headerSize = 10;	//IBBI(10byte)
		int sIndex = 0;
		
		List<Tuple> tuples = new ArrayList<>();
		int msgIdx = 0;

		try {
			int rawcount = hcpMessage.length;
			Tuple kafkaMessage = inputTuple.getTuple("kafkaMessage");
			long baseTime = kafkaMessage.getLong("BaseTime");

			if(preprocessor != null) {
				preprocessor.initialize(kafkaMessage);
			}
			
			Tuple startTuple = OutputSchema.createTuple();
			startTuple.setBoolean("IsStarted", true);
			startTuple.setBoolean("IsEnded", false);
			startTuple.setInt("MSGIdx", msgIdx++);
			startTuple.setTuple("PassThroughs", inputTuple);
			tuples.add(startTuple);

			while((sIndex + headerSize) < rawcount) {

				int dlcIndex = NumUtils.getIntFromBig(hcpMessage, sIndex, AutoDataHeaderField.DataLength.getsize());
				sIndex += AutoDataHeaderField.DataLength.getsize();
				int curIndex =  sIndex;
				double deltaTime = (double)(NumUtils.getLongFromBig(hcpMessage, curIndex, AutoDataHeaderField.DeltaTime.getsize()) * 0.00005);
				curIndex += AutoDataHeaderField.DeltaTime.getsize();
				int dataFlag = NumUtils.getIntFromBig(hcpMessage, curIndex, AutoDataHeaderField.DataFlag.getsize());
				curIndex += AutoDataHeaderField.DataFlag.getsize();
				int dataChannel = NumUtils.getIntFromBig(hcpMessage, curIndex, AutoDataHeaderField.DataChannel.getsize());
				curIndex += AutoDataHeaderField.DataChannel.getsize();
				int dataId = NumUtils.getIntFromBig(hcpMessage, curIndex, AutoDataHeaderField.DataID.getsize());
				curIndex += AutoDataHeaderField.DataID.getsize();
				int dataSize = dlcSize[dlcIndex];
				byte[] rawData = null; 
				if(dataSize > 0) {
					rawData =  Arrays.copyOfRange(hcpMessage, curIndex, curIndex + dataSize);
				}
				
				Tuple dataTuple = OutputSchema.createTuple();
				dataTuple.setBoolean("IsStarted", false);
				dataTuple.setBoolean("IsEnded", false);
				dataTuple.setInt("MSGIdx", msgIdx);
				{
					Tuple headerTuple = rawHeaderSchema.createTuple();
					headerTuple.setInt(RawDataField.DataChannel.getValue(), dataChannel);
					headerTuple.setDouble(RawDataField.DeltaTime.getValue(), deltaTime);
					headerTuple.setInt(RawDataField.MSGInfo.getValue(), dataFlag);
					headerTuple.setInt(RawDataField.DataID.getValue(), dataId);
					headerTuple.setInt(RawDataField.DLC.getValue(), dlcIndex);
					if(rawData != null) headerTuple.setString(RawDataField.DATA.getValue(), Base64.encodeBytes(rawData));
					headerTuple.setLong(RawDataField.BaseTime.getValue(), baseTime);
					dataTuple.setTuple("RawHeader", headerTuple);
				}
				dataTuple.setTuple("PassThroughs", inputTuple);
				
				if(preprocessor == null || preprocessor.preProcess(kafkaMessage, dataTuple, rawData)) {
					tuples.add(dataTuple);
					msgIdx++;
				}else {
					// no action
				}
				sIndex += (dataSize + headerSize);
			}

			Tuple endTuple = OutputSchema.createTuple();
			endTuple.setBoolean("IsStarted", false);
			endTuple.setBoolean("IsEnded", true);
			endTuple.setInt("MSGIdx", msgIdx+1); //started + ended + count(tuples)
			endTuple.setTuple("PassThroughs", inputTuple);
			tuples.add(endTuple);

		}catch(Exception e) {
			e.printStackTrace();
//			System.out.println("RawParser Exception:" + e.getMessage());
			return tuples;
		}
		
		return tuples;
	}
	
	
	/**
	 * If typecheck succeeds, the init method is called before the StreamBase application
	 * is started. Note that your Operator class is not required to define the init method,
	 * unless (for example) you need to perform initialization of a resource such as a JDBC
	 * pool, if your operator is making JDBC calls. StreamBase Studio does not call this
	 * during authoring.
	 */
	public void init() throws StreamBaseException {
		super.init();

		if(parserType == parserTypeEnum.CAN) {
			preprocessor = new CanPreProcessor();
		}else if(parserType == parserTypeEnum.CCP){
			preprocessor = new CCPPreProcessor();
		}else if(parserType == parserTypeEnum.GPS){
			preprocessor = new GPSPreProcessor();
		}else {
			preprocessor = new DefaultPreProcessor();
		}

		OutputSchema = getRuntimeOutputSchema(0);
		StatusSchema = getRuntimeOutputSchema(1);

	}

	/**
	*  The shutdown method is called when the StreamBase server is in the process of shutting down.
	*/
	public void shutdown() {

	}

	/***************************************************************************************
	 * The getter and setter methods provided by the Parameterizable object.               *
	 * StreamBase Studio uses them to determine the name and type of each property         *
	 * and obviously, to set and get the property values.                                  *
	 ***************************************************************************************/

	public void setParserType(parserTypeEnum parserType) {
		this.parserType = parserType;
	}

	public parserTypeEnum getParserType() {
		return this.parserType;
	}

	/** For detailed information about shouldEnable methods, see interface Parameterizable java doc 
	 *  @see Parameterizable 
	 */

	public boolean shouldEnableParserType() {
		// TODO implement custom enablement logic here
		return true;
	}

}
