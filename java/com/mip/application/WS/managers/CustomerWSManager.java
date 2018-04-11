package com.mip.application.WS.managers;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;
import java.rmi.RemoteException;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.Invocation;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.glassfish.jersey.client.ClientConfig;

import com.mip.application.constants.FaultMessages;
import com.mip.application.model.AssignOfferRequest;
import com.mip.application.model.LoyaltyPackRequest;
import com.mip.application.model.ReactivationCustomerRequest;
import com.mip.application.model.RegisterCustomerRequest;
import com.mip.application.model.UserDetails;
import com.mip.application.view.CustomerVO;
import com.mip.framework.constants.PlatformConstants;
import com.mip.framework.exceptions.WSException;
import com.mip.framework.logger.LoggerFactory;
import com.mip.framework.logger.MISPLogger;
import com.mip.framework.utils.DateUtil;
import com.mip.framework.utils.StringUtil;
import com.tigo.www.adjustaccountdata.AccChgRecType;
import com.tigo.www.adjustaccountdata.AdjustAccountRequest;
import com.tigo.www.adjustaccountdata.AdjustAccountResult;
import com.tigo.www.adjustaccountservice.AdjustAccountProxy;
import com.tigo.www.commonservicedata.ChannelType;
import com.tigo.www.commonservicedata.DeductionType;
import com.tigo.www.commonservicedata.GenderType;
import com.tigo.www.commonservicedata.IsNetworkChurned;
import com.tigo.www.commonservicedata.ProductSelected;
import com.tigo.www.commonservicedata.RelationshipType;
import com.tigo.www.customerregistrationbpservice.CustomerRegistrationBPProxy;
import com.tigo.www.customerregistrationdata.CustomerRegistrationRequest;
import com.tigo.www.customerregistrationdata.CustomerRegistrationResponse;
import com.tigo.www.deregistercustomerdata.DeregisterCustomerRequest;
import com.tigo.www.deregistercustomerdata.DeregisterCustomerResponse;
import com.tigo.www.deregistercustomerservice.DeregisterCustomerProxy;
import com.tigo.www.faultdata.FaultType;
import com.tigo.www.offerunsubscribedata.OfferUnsubscribeRequest;
import com.tigo.www.offerunsubscribedata.OfferUnsubscribeResponse;
import com.tigo.www.offerunsubscribeservice.OfferUnsubscribeProxy;
import com.tigo.www.removecustomerregistrationdata.RemoveCustomerRegistrationProxy;
import com.tigo.www.removecustomerregistrationdata.RemoveCustomerRegistrationRequest;
import com.tigo.www.removecustomerregistrationdata.RemoveCustomerRegistrationResponse;
import com.tigo.www.switchdeductionbpservice.SwitchDeductionBPProxy;
import com.tigo.www.switchdeductiondata.SwitchDeductionRequest;
import com.tigo.www.switchdeductiondata.SwitchDeductionResponse;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
/*
 * HISTORY
 * =====================================================================
 * Version | Date        | Who              | Comments
 * ---------------------------------------------------------------------
 * 1.0     | May 20, 2011  | T H B S          |Initial Version.
 * =====================================================================
 */

/**
 * <p>
 * <code>CustomerWSManager.java</code> class contains the implementation of
 * customer registration web-service invocation. Values received from the
 * service layer is set to the web-service request object and the response is
 * sent to the appropriate higher layer.
 * </p>
 * 
 * @author T H B S
 * 
 *         <p>
 *         For all the web service exception details,
 *         </p>
 * @see com.mip.framework.exceptions.WSException
 * 
 */
public class CustomerWSManager {

	/**
	 * An instance of logger.
	 */
	private static MISPLogger logger = LoggerFactory.getInstance().getLogger(
			CustomerWSManager.class);

	/**
	 * This method invokes the Customer Registration Web service to persist the
	 * customer information in the platform database. This is synchronous web
	 * service call.
	 * 
	 * @param custVO
	 *            - The instance of the customer information object
	 * 
	 * @param user
	 *            - The instance of User detail class holding the user details
	 *            of the logged in user.
	 * 
	 * @param URL
	 *            - This is the URL of the Customer Registration Web service
	 *            that is to be invoked.
	 * 
	 * @return - True for Successful registration, False for Failure
	 *         registration.
	 */
	public boolean invokeRegisterCustWS(RegisterCustomerRequest registerCustomerRequest, UserDetails user,
			String URL) throws WSException {
		logger.entering("invokeRegisterCustWS: ", registerCustomerRequest,
				user, URL);
		boolean operationResult = false;
		String result = "";

		String URI = URL;
		ClientConfig config = new ClientConfig();
		Client client = ClientBuilder.newClient(config);
		Gson jsonConverter = new GsonBuilder().create();
		String responseMessage = "";

		try {
			WebTarget target = client.target(URI);
			Invocation.Builder builder = target.request();
			String jsonString = jsonConverter.toJson(registerCustomerRequest);

			Response response = builder
					.accept(MediaType.APPLICATION_JSON)
					.post(Entity.entity(jsonString, MediaType.APPLICATION_JSON));
			String regStatus = response.readEntity(String.class);

			JsonParser parser = new JsonParser();
			JsonObject json = (JsonObject) parser.parse(regStatus);
			int statusCode = Integer.parseInt(json.get("responseCode")
					.toString());
			responseMessage = json.get("responseMessage").toString();
			
			logger.info("responseCode-->", statusCode);
			logger.info("responseMessage-->", responseMessage);
			
			if(statusCode == 200 )
				operationResult=true;

		}  catch (Exception exception) {
			logger.error("A unknown exception has "
					+ "occured while processing the registration request.");
			logger.error(exception.getLocalizedMessage());

			throw new WSException(exception);
		}

		logger.exiting("invokeRegisterCustWS", operationResult);
		return operationResult;
	}

	/**
	 * This method implements the functionality to invoke "Un-Subscribe Offer"
	 * web-service.
	 * 
	 * @param custVO
	 *            an instance of {@link com.mip.application.view.CustomerVO}
	 * 
	 * @param URL
	 *            <code><b>Un-Subscribe Offer</b></code> webservice URL
	 * 
	 * @return TRUE if successful, FALSE otherwise.
	 * 
	 * @throws WSException
	 */
	@Deprecated
	public boolean invokeUnsubscribeOfferWS(CustomerVO custVO, String URL)
			throws WSException {
		logger.entering("invokeUnsubscribeOfferWS", custVO, URL);

		OfferUnsubscribeRequest offerUnsubRequest = new OfferUnsubscribeRequest();
		OfferUnsubscribeResponse offerUnsubResponse = new OfferUnsubscribeResponse();
		boolean response = false;

		try {
			OfferUnsubscribeProxy wsProxy = new OfferUnsubscribeProxy(URL);

			offerUnsubRequest.setMSISDN(custVO.getMsisdn());
			/*
			 * offerUnsubRequest.setIsImmediate
			 * (Boolean.valueOf(custVO.getDeRegisterEffectiveFrom()
			 * .equalsIgnoreCase("1")? "TRUE" : "FALSE"));
			 */

			offerUnsubRequest.setIsImmediate(true);
			offerUnsubResponse = wsProxy
					.processOfferUnsubscribeRequest(offerUnsubRequest);

			logger.debug("Status Code: ", offerUnsubResponse.getStatus()
					.getStatusCode());
			logger.debug("Status Message: ", offerUnsubResponse.getStatus()
					.getStatusMessage());

			response = true;
		} catch (FaultType businessFault) {
			// These faults are considered to be business fault and hence we
			// do not stop our execution here. The response under these return
			// are considered success.
			if (businessFault.getFaultCode1().equalsIgnoreCase(
					FaultMessages.OFFER_UNSUBSCRIBE_FAULT_BUSINESS_B1_CODE)
					|| businessFault
							.getFaultCode1()
							.equalsIgnoreCase(
									FaultMessages.OFFER_UNSUBSCRIBE_FAULT_BUSINESS_B2_CODE)) {
				logger.debug("Business Fault Code: ",
						businessFault.getFaultCode1());
				logger.debug("Business Fault Message: ",
						businessFault.getFaultMessage());
				response = true;
				logger.exiting("invokeUnsubscribeOfferWS", response);
				return response;
			} else {
				logger.error("A FaultType error has occured "
						+ "while processing the web-service request.");
				logger.error(businessFault.getFaultActor1());
				logger.error(businessFault.getFaultCode1());
				logger.error(businessFault.getFaultDetail());
				logger.error(businessFault.getFaultMessage());
				logger.error(businessFault.getFaultOriginator());

				throw new WSException(businessFault);
			}

		} catch (RemoteException remoteException) {
			logger.error("A remote exception has occured while "
					+ "processing the web-service request.");
			logger.error(remoteException.getLocalizedMessage());

			throw new WSException(remoteException);
		} catch (Exception exception) {
			logger.error("A unknown exception has "
					+ "occured while processing the web-service request.");
			logger.error(exception.getLocalizedMessage());

			throw new WSException(exception);
		}

		logger.exiting("invokeUnsubscribeOfferWS", response);
		return response;
	}

	/**
	 * This method implements the functionality to invoke
	 * "Remove Customer Registration" webservice.
	 * 
	 * @param custVO
	 *            an instance of {@link com.mip.application.view.CustomerVO}
	 * 
	 * @param user
	 *            an instance of {@link com.mip.application.model.UserDetails}
	 * 
	 * @param URL
	 *            "Remove Customer Registration" webservice URL.
	 * 
	 * @return TRUE if successful, FALSE otherwise.
	 * 
	 * @throws WSException
	 */
	@Deprecated
	public boolean invokeRemoveCustomerRegistrationWS(CustomerVO custVO,
			UserDetails user, String URL) throws WSException {
		logger.entering("invokeRemoveCustomerRegistrationWS", custVO, user, URL);
		boolean response = false;

		RemoveCustomerRegistrationProxy wsProxy = new RemoveCustomerRegistrationProxy(
				URL);

		RemoveCustomerRegistrationRequest removeCustomerRegistrationRequest = new RemoveCustomerRegistrationRequest();

		removeCustomerRegistrationRequest.setMSISDN(custVO.getMsisdn());
		removeCustomerRegistrationRequest.setDeRegisteredBy(new BigInteger(
				String.valueOf(user.getUserId())));

		RemoveCustomerRegistrationResponse removeCustomerRegistrationResponse = new RemoveCustomerRegistrationResponse();

		try {
			removeCustomerRegistrationResponse = wsProxy
					.processRemoveCustomerRegistrationRequest(removeCustomerRegistrationRequest);

			logger.debug("Status Code: ", removeCustomerRegistrationResponse
					.getStatus().getStatusCode());
			logger.debug("Status Message: ", removeCustomerRegistrationResponse
					.getStatus().getStatusMessage());

			response = true;
		} catch (FaultType businessFault) {
			logger.error("A FaultType error has occured "
					+ "while processing the web-service request.");
			logger.error(businessFault.getFaultActor1());
			logger.error(businessFault.getFaultCode1());
			logger.error(businessFault.getFaultDetail());
			logger.error(businessFault.getFaultMessage());
			logger.error(businessFault.getFaultOriginator());

			throw new WSException(businessFault);
		} catch (RemoteException remoteException) {
			logger.error("A remote exception has occured while "
					+ "processing the web-service request.");
			logger.error(remoteException.getLocalizedMessage());

			throw new WSException(remoteException);
		} catch (Exception exception) {
			logger.error("A unknown exception has "
					+ "occured while processing the web-service request.");
			logger.error(exception.getLocalizedMessage());

			throw new WSException(exception);
		}

		logger.exiting("invokeRemoveCustomerRegistrationWS", response);

		return response;
	}

	/**
	 * This method implements the functionality to invoke "Deregister Customer"
	 * webservice.
	 * 
	 * @param custVO
	 *            an instance of {@link com.mip.application.view.CustomerVO}
	 * 
	 * @param user
	 *            an instance of {@link com.mip.application.model.UserDetails}
	 * 
	 * @param URL
	 *            "Deregister Customer" webservice URL.
	 * 
	 * @return TRUE if successful, FALSE otherwise.
	 * 
	 * @throws WSException
	 */
	@Deprecated
	public boolean invokeDeregisterCustomerWS(CustomerVO custVO,
			UserDetails user, String URL) throws WSException {
		logger.entering("invokeDeregisterCustomerWS", custVO, user, URL);
		boolean response = false;

		DeregisterCustomerProxy wsProxy = new DeregisterCustomerProxy(URL);

		DeregisterCustomerRequest wsRequest = new DeregisterCustomerRequest();

		wsRequest.setDeRegisteredBy(new BigInteger(String.valueOf(user
				.getUserId())));
		wsRequest.setIsNetworkChurned(IsNetworkChurned.fromString(custVO
				.getDeRegisterOption()));
		wsRequest.setMSISDN(custVO.getMsisdn());

		DeregisterCustomerResponse wsResponse = new DeregisterCustomerResponse();

		try {
			wsResponse = wsProxy.processDeregisterCustomerRequest(wsRequest);

			logger.debug("Status Code: ", wsResponse.getStatus()
					.getStatusCode());
			logger.debug("Status Message: ", wsResponse.getStatus()
					.getStatusMessage());
			response = true;
		} catch (FaultType businessFault) {
			logger.error("A FaultType error has occured "
					+ "while processing the web-service request.");
			logger.error(businessFault.getFaultActor1());
			logger.error(businessFault.getFaultCode1());
			logger.error(businessFault.getFaultDetail());
			logger.error(businessFault.getFaultMessage());
			logger.error(businessFault.getFaultOriginator());
			response = true;
			// throw new WSException(businessFault);
		} catch (RemoteException remoteException) {
			logger.error("A remote exception has occured while "
					+ "processing the web-service request.");
			logger.error(remoteException.getLocalizedMessage());

			throw new WSException(remoteException);
		} catch (Exception exception) {
			logger.error("A unknown exception has "
					+ "occured while processing the web-service request.");
			logger.error(exception.getLocalizedMessage());

			throw new WSException(exception);
		}

		logger.exiting("invokeDeregisterCustomerWS", response);

		return response;
	}

	/**
	 * This method invokes the adjust account web service and deducts the amount
	 * from the customer's account for the MSISDN passed to the web service.
	 * 
	 * @param msisdn
	 *            Customer's MSISDN
	 * 
	 * @param proxy
	 *            AdjustAccountProxy object to connect to web-sevice
	 * 
	 * @param adjustAccountRequest
	 *            AdjustAccountRequest object containing input data to
	 *            web-service
	 * 
	 * @throws WSException
	 *             If any {@link RemoteException} occurs
	 */
	public boolean invokeAccountAdjustWS(String msisdn,
			AdjustAccountProxy proxy, AdjustAccountRequest adjustAccountRequest)
			throws WSException {

		logger.entering("invokeAccountAdjustWS", msisdn);

		boolean result = false;
		AdjustAccountResult adjustAccountResult;
		try {
			adjustAccountResult = proxy
					.processAdjustAccountRequest(adjustAccountRequest);

			AccChgRecType[] accChgRec = adjustAccountResult.getAccChgRec();
			if (accChgRec != null && accChgRec[0] != null) {
				long chgAcctBal = accChgRec[0].getChgAcctBal();
				if (chgAcctBal < 0) {
					chgAcctBal = -chgAcctBal;
				}

				BigDecimal cedis = new BigDecimal(chgAcctBal).divide(
						new BigDecimal(PlatformConstants.ONE_GHC_UNIT), 2,
						RoundingMode.HALF_UP);

				BigDecimal peswas = cedis.multiply(new BigDecimal(100));

				logger.info(String.valueOf(chgAcctBal), " units [", cedis,
						" Ghana Cedis or ", peswas,
						" Peswas] were successfully refunded for MSISDN [",
						msisdn, "].");

				result = true;
			}
		} catch (FaultType e) {
			logger.error("FaultType Exception occured while invoking "
					+ "AccountAdjustWS : ", e);

			logger.info("The refund failed for MSISDN [", msisdn,
					"]. Error Code [", e.getFaultCode1(),
					"]. Error Description [", e.getFaultMessage(), "].");

		} catch (RemoteException e) {
			logger.error("RemoteException occured while invoking "
					+ "AccountAdjustWS : ", e);

			logger.info("The refund failed for MSISDN [", msisdn,
					"]. Error Description [", e.getMessage(), "].");

		} catch (Exception e) {
			logger.error("Exception occured while invoking AccountAdjustWS : ",
					e);

			logger.info("The refund failed for MSISDN[", msisdn,
					"]. Error Description[", e.getMessage(), "].");

		}

		logger.exiting("invokeAccountAdjustWS", result);
		return result;
	}

	/**
	 * This method use to invoke change deduction mode service 
	 * @param customerVO
	 * @param userDetails
	 * @param url
	 * @return boolean true for success , false for failure 
	 */
	public boolean invokeChangeDeductionModeWS(CustomerVO customerVO,
			UserDetails userDetails, String url) {
		logger.entering("invokeChangeDeductionModeWS", customerVO, userDetails,
				url);
		boolean result = false;

		try {
			SwitchDeductionBPProxy proxy = new SwitchDeductionBPProxy(url);
			SwitchDeductionRequest wsrequest = new SwitchDeductionRequest();

			wsrequest.setMSISDN(customerVO.getMsisdn());
			wsrequest.setDeductionType(DeductionType.fromString(customerVO
					.getDeductionMode()));
			wsrequest.setRegisteredBy(BigInteger.valueOf(userDetails
					.getUserId()));
			wsrequest.setRegistrationChannel(ChannelType
					.fromString("Dashboard"));

			SwitchDeductionResponse wsResponse = new SwitchDeductionResponse();
			wsResponse = proxy.processSwitchDeductionBPRequest(wsrequest);
			result = true;
			logger.debug("Status Code: ", wsResponse.getStatus()
					.getStatusCode());
			logger.debug("Status Message: ", wsResponse.getStatus()
					.getStatusMessage());

		} catch (FaultType e) {
			logger.error("A FaultType error has occured "
					+ "while processing the web-service request.");
			logger.error(e.getFaultActor1());
			logger.error(e.getFaultCode1());
			logger.error(e.getFaultDetail());
			logger.error(e.getFaultMessage());
			logger.error(e.getFaultOriginator());
			result = false;
		} catch (RemoteException e) {
			logger.error("A remote exception has occured while processing the web-service request.");
			logger.error(e.getLocalizedMessage());
			result = false;
		} catch (Exception e) {
			logger.error("A unknown exception has "
					+ "occured while processing the web-service request.");
			logger.error(e.getLocalizedMessage());
			result = false;
		}

		logger.exiting("invokeChangeDeductionModeWS", result);
		return result;

	}

	public boolean  invokeReactivationCustWS  (
			ReactivationCustomerRequest reactivationCustomerRequest,String URL) throws WSException {
		logger.entering("invokeReactivationCustWS: ", reactivationCustomerRequest,
				URL);
		boolean operationResult = false;
		String result = "";

		String URI = URL;
		ClientConfig config = new ClientConfig();
		Client client = ClientBuilder.newClient(config);
		Gson jsonConverter = new GsonBuilder().create();
		String responseMessage = "";

		try {
			WebTarget target = client.target(URI);
			Invocation.Builder builder = target.request();
			String jsonString = jsonConverter.toJson(reactivationCustomerRequest);

			Response response = builder
					.accept(MediaType.APPLICATION_JSON)
					.post(Entity.entity(jsonString, MediaType.APPLICATION_JSON));
			String regStatus = response.readEntity(String.class);

			JsonParser parser = new JsonParser();
			JsonObject json = (JsonObject) parser.parse(regStatus);
			
			
			int statusCode = Integer.parseInt(json.get("responseCode")
					.toString());
			responseMessage = json.get("responseMessage").toString();
			
			logger.info("responseCode-->", statusCode);
			logger.info("responseMessage-->", responseMessage);
			
			if(statusCode == 200 )
				operationResult=true;

		}  catch (Exception exception) {
			logger.error("A unknown exception has "
					+ "occured while processing the registration request.");
			logger.error(exception.getLocalizedMessage());

			throw new WSException(exception);
		}

		logger.exiting("invokeReactivationCustWS", operationResult);
		return operationResult;
	}

	public int invokeAssignOfferWS(CustomerVO custVO, UserDetails user,
			String URL) throws WSException {
		logger.entering("invokeAssignOfferWS: ", custVO,user,
				URL);
		boolean operationResult = false;
		
		ClientConfig config = new ClientConfig();
		Client client = ClientBuilder.newClient(config);
		Gson jsonConverter = new GsonBuilder().create();
		String responseMessage = "";
		int statusCode;
        AssignOfferRequest assignOfferRequest = new AssignOfferRequest();
		try {
			
			assignOfferRequest.setMsisdn(custVO.getMsisdn());
			assignOfferRequest.setProductIds(custVO.getProductId());
			assignOfferRequest.setProductCoverLevelId(custVO.getProductCoverIdIP());
			assignOfferRequest.setRegBy(user.getUserId());
			assignOfferRequest.setRegChnId(1);
			
			logger.info("AssignOfferRequest-->", assignOfferRequest);
			
			WebTarget target = client.target(URL);
			Invocation.Builder builder = target.request();
			
			
			String jsonString = jsonConverter.toJson(assignOfferRequest);

			Response response = builder
					.accept(MediaType.APPLICATION_JSON)
					.post(Entity.entity(jsonString, MediaType.APPLICATION_JSON));
			String regStatus = response.readEntity(String.class);

			JsonParser parser = new JsonParser();
			JsonObject json = (JsonObject) parser.parse(regStatus);
			statusCode = Integer.parseInt(json.get("responseCode")
					.toString());
			responseMessage = json.get("responseMessage").toString();
			
			logger.info("responseCode-->", statusCode);
			logger.info("responseMessage-->", responseMessage);
			
			if(statusCode == 500 )
			{
				throw new Exception();
			}
				

		}  catch (Exception exception) {
			logger.error("A unknown exception has "
					+ "occured while processing the registration request.");
			logger.error(exception.getLocalizedMessage());

			throw new WSException(exception);
		}

		logger.exiting("invokeAssignOfferWS", statusCode);
		return statusCode;
	}

	
	/*public boolean invokeLoyaltyDatapackServiceWS(RegisterCustomerRequest registerCustomerRequest, UserDetails user,
			String URL) throws WSException {
		
		logger.entering("invokeLoyaltyDatapackServiceWS: ", registerCustomerRequest,
				user, URL);
		boolean operationResult = false;
		String result = "";

		String URI = URL;
		ClientConfig config = new ClientConfig();
		Client client = ClientBuilder.newClient(config);
		Gson jsonConverter = new GsonBuilder().create();
		String responseMessage = "";

		try {
			WebTarget target = client.target(URI);
			Invocation.Builder builder = target.request();
			String jsonString = jsonConverter.toJson(registerCustomerRequest);

			Response response = builder
					.accept(MediaType.APPLICATION_JSON)
					.post(Entity.entity(jsonString, MediaType.APPLICATION_JSON));
			String regStatus = response.readEntity(String.class);

			JsonParser parser = new JsonParser();
			JsonObject json = (JsonObject) parser.parse(regStatus);
			int statusCode = Integer.parseInt(json.get("responseCode")
					.toString());
			responseMessage = json.get("responseMessage").toString();
			
			logger.info("responseCode-->", statusCode);
			logger.info("responseMessage-->", responseMessage);
			
			if(statusCode == 200 )
				operationResult=true;

		}  catch (Exception exception) {
			logger.error("A unknown exception has "
					+ "occured while processing the registration request.");
			logger.error(exception.getLocalizedMessage());

			throw new WSException(exception);
		}

		logger.exiting("invokeLoyaltyDatapackServiceWS", operationResult);
		return operationResult;
	}*/
	
	/**
	 * @param loyaltyPackRequest
	 * @param userId
	 * @param URL
	 * @return
	 * @throws WSException
	 */
	public int invokeLoyaltyVoicepackServiceWS(LoyaltyPackRequest loyaltyPackRequest, int userId,
			String URL) throws WSException {
		
		logger.entering("invokeLoyaltyVoicepackServiceWS: ", loyaltyPackRequest,
				userId ,URL);
		
		boolean operationResult = false;
		int responseCode=-1;

		String URI = URL;
		ClientConfig config = new ClientConfig();
		Client client = ClientBuilder.newClient(config);
		Gson jsonConverter = new GsonBuilder().create();
		String responseMessage = "";

		try {
			WebTarget target = client.target(URI);
			Invocation.Builder builder = target.request();
			String jsonString = jsonConverter.toJson(loyaltyPackRequest);

			Response response = builder
					.accept(MediaType.APPLICATION_JSON)
					.post(Entity.entity(jsonString, MediaType.APPLICATION_JSON));
			String regStatus = response.readEntity(String.class);

			JsonParser parser = new JsonParser();
			JsonObject json = (JsonObject) parser.parse(regStatus);
			
			logger.info("responseCode :: ", json.get("responseCode").getAsString());
			
			responseCode = Integer.parseInt(json.get("responseCode").getAsString());
					//.toString();
			responseMessage = json.get("responseMessage").toString();
			
			/* JSONParser parser = new JSONParser();
							JSONObject obj = (JSONObject) parser
									.parse(responseStr);

							JSONObject jsonResponseEntity = (JSONObject) parser
									.parse(obj.get("resourceReference")
											.toString());

							code = jsonResponseEntity.get("resourceURL")
									.toString();
*/
			
			logger.info("responseCode-->", responseCode);
			logger.info("responseMessage-->", responseMessage);
			
			/*if(responseCode == 200 )
				operationResult=true;
			//operationResult=true;
*/
		}  catch (Exception exception) {
			logger.error("A unknown exception has "
					+ "occured while processing the localty pack request.");
			logger.error(exception.getLocalizedMessage());

			throw new WSException(exception);
		}

		logger.exiting("invokeLoyaltyVoicepackServiceWS", responseCode);
		return responseCode;
	}
	

	
}
