package com.mip.framework.castor;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.net.URL;

import org.exolab.castor.mapping.Mapping;
import org.exolab.castor.mapping.MappingException;
import org.exolab.castor.xml.MarshalException;
import org.exolab.castor.xml.Marshaller;
import org.exolab.castor.xml.Unmarshaller;
import org.exolab.castor.xml.ValidationException;
import org.exolab.castor.xml.XMLContext;

import com.mip.framework.exceptions.ConfigFileNotFoundException;
import com.mip.framework.exceptions.MISPException;
import com.mip.framework.logger.LoggerFactory;
import com.mip.framework.logger.MISPLogger;
import com.mip.framework.utils.IOUtil;

public class CastorService {
	
	private static final String CASTOR_MAPPING_FILE = "com/mip/framework/castor/castor-mapping.xml";
	private static final String SMS_PLACE_HOLDER_FILE = "com/mip/framework/castor/sms-place-holders.xml";
	
	Mapping mapping;
	File smsPlaceHolderFile;
	
	/**
	 * An instance of logger.
	 */
	private static MISPLogger logger = LoggerFactory.getInstance().getLogger(
			CastorService.class);
	
	/**
	 * Default Constructor loads both mapping file and adminConfig file.
	 * 
	 * @throws MISPException
	 */
	public CastorService() throws MISPException{
		try {
			// Load mapping file
			URL mappingURL = IOUtil.locate(CASTOR_MAPPING_FILE);
			mapping = new Mapping(); 
			mapping.loadMapping(mappingURL);
			
			// Load adminConfig file
			URL smsPlaceHolderURL = IOUtil.locate(SMS_PLACE_HOLDER_FILE);			
			smsPlaceHolderFile = new File(smsPlaceHolderURL.getFile());
			
		} catch (ConfigFileNotFoundException e) {
			logger.error("An exception occured while loading the " +
					"mapping file and the adminConfig file.", e);
			throw new MISPException(e);
		} catch (IOException e) {
			logger.error("An exception occured while loading the " +
					"mapping file and the adminConfig file.", e);
			throw new MISPException(e);
		} catch (MappingException e) {
			logger.error("An exception occured while loading the " +
					"mapping file and the adminConfig file.", e);
			throw new MISPException(e);
		}    
		
	}

	/**
	 * Marshals 
	 * 
	 * @param smsPlaceHolders
	 * @throws MISPException
	 */
	public void marshal(SMSPlaceHolders smsPlaceHolders) throws MISPException{
			
		Object[] params = {smsPlaceHolders};
		logger.entering("marshal", params);	
				
		try {    
			// Initialize and configure XMLContext
			XMLContext context = new XMLContext();
			context.addMapping(mapping);

			// Create a Writer to the file to marshal to
			Writer writer = new FileWriter(smsPlaceHolderFile);

			// Create a new Marshaler
			Marshaller marshaller = context.createMarshaller();
			marshaller.setWriter(writer);
			
			// Marshal the person object
			marshaller.marshal(smsPlaceHolders);
			
		} 
		catch (IOException e) {
			logger.error("An exception occured while marshalling " +
					"the XML file from Object.", e);
			throw new MISPException(e);
		} 
		catch (MappingException e) {
			logger.error("An exception occured while marshalling " +
					"the XML file from Object.", e);
			throw new MISPException(e);
		} 
		catch (MarshalException e) {
			logger.error("An exception occured while marshalling " +
					"the XML file from Object.", e);
			throw new MISPException(e);
		} 
		catch (ValidationException e) {
			logger.error("An exception occured while marshalling " +
					"the XML file from Object.", e);
			throw new MISPException(e);
		}
		
		logger.exiting("marshal");
	}

	

	public SMSPlaceHolders unmarshal() throws MISPException{
			
		logger.entering("unmarshal");	
		
		SMSPlaceHolders smsPlaceHolders = null;
		try {		
			// Initialize and configure XMLContext	
			XMLContext context = new XMLContext();
			context.addMapping(mapping);
	
			// Create a Writer to the file to marshal to
			Reader reader = new FileReader(smsPlaceHolderFile);
	
			// Create a new Unmarshaler
			Unmarshaller unmarshaller = context.createUnmarshaller();
			unmarshaller.setClass(SMSPlaceHolders.class);
	
			// Unmarshal the person object
			smsPlaceHolders = (SMSPlaceHolders)unmarshaller.unmarshal(reader);		
		} 
		catch (IOException e) {
			logger.error("An exception occured while unmarshalling " +
					"the Object from XML file.", e);
			throw new MISPException(e);
		} 
		catch (MappingException e) {
			logger.error("An exception occured while marshalling " +
					"the Object from XML file.", e);
			throw new MISPException(e);
		} 
		catch (MarshalException e) {
			logger.error("An exception occured while marshalling " +
					"the Object from XML file.", e);
			throw new MISPException(e);
		} 
		catch (ValidationException e) {
			logger.error("An exception occured while marshalling " +
					"the Object from XML file.", e);
			throw new MISPException(e);
		}
		logger.exiting("unmarshal",smsPlaceHolders);
		return smsPlaceHolders;
	}
}
