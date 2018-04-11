package com.mip.application.controllers;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.web.servlet.ModelAndView;

import com.mip.application.constants.SessionKeys;
import com.mip.application.constants.SuccessMessages;
import com.mip.application.model.BranchDetails;
import com.mip.application.model.UserDetails;
import com.mip.application.services.BranchService;
import com.mip.application.view.BranchVO;
import com.mip.framework.constants.MAVObjects;
import com.mip.framework.constants.MAVPaths;
import com.mip.framework.controllers.BasePlatformController;
import com.mip.framework.exceptions.MISPException;
import com.mip.framework.logger.LoggerFactory;
import com.mip.framework.logger.MISPLogger;

/**
 * <p>
 * <code>BranchController.java</code> contains all the methods pertaining to Branch
 * Management use case model. Branch Management use case model includes following
 * use cases : 
 * <table border="1">
 * 	<tr><th width="5%">Sl. No.</th><th>Use Case</th><th>Dispatcher action method signature</th></tr>
 * 	<tr><td>1.</td><td>Add Branch</td><td>{@link #addBranch(HttpServletRequest,HttpServletResponse,BranchVO)}</td></tr>
 * 	<tr><td>2.</td><td>List Branches</td><td>{@link #listBranches(HttpServletRequest,HttpServletResponse)}</td></tr>
 * 	<tr><td rowspan="2">3.</td><td rowspan="2">Modify Branch Details</td><td>{@link #showModifyBranchDetails(HttpServletRequest,HttpServletResponse,BranchVO)}</td></tr>
 * 	<tr><td>{@link #modifyBranchDetails(HttpServletRequest,HttpServletResponse,BranchVO)}</td></tr>
 * 	<tr><td>4.</td><td>De-activate Branch</td><td>{@link #deactivateBranch(HttpServletRequest,HttpServletResponse,BranchVO)}</td></tr>
 * </table>		
 * <br/>
 * This controller extends the <code>BasePlatformController</code> class 
 * of our MISP framework.
 * </p>
 * 
 * @see com.mip.framework.controllers.BasePlatformController
 * 
 * @author T H B S
 * 
 */
public class BranchController extends BasePlatformController {
	
	/**
	 * An instance of logger.
	 */
	private static MISPLogger logger = LoggerFactory.getInstance().getLogger(
			BranchController.class);

	/**
	 * Set inversion of Control for <code>BranchService</code>
	 */
	private BranchService branchService;

	public void setBranchService(BranchService branchService) {
		this.branchService = branchService;
	}

	/**
	 * This method handles the operation of adding a Branch Details record 
	 * into database. 
	 *  
	 * @param request
	 *            an instance of HTTPServletRequest
	 * 
	 * @param response
	 *            an instance of HTTPServletRequest
	 * 
	 * @param branchVO
	 *            BranchVO command object
	 * 
	 * @return upon success redirect to <code>addBranch.jsp</code>, 
	 *         and on failure to <code>global_error_page.jsp</code>
	 */
	public ModelAndView addBranch(HttpServletRequest request,
			HttpServletResponse response, BranchVO branchVO) {
		Object[] params = {branchVO};
		logger.entering("addBranch", params);

		ModelAndView mav = null;
		
		HttpSession session = request.getSession();
		UserDetails loginUserDetails = (UserDetails) session
				.getAttribute(SessionKeys.SESSION_USER_DETAILS);
		
		try {
			branchService.registerBranch(branchVO,loginUserDetails,false);

			mav = super.success(SuccessMessages.BRANCH_ADDED,
					MAVPaths.JSP_BRANCH_ADD);
		} 
		catch(MISPException exception) {			
			logger.error("An exception occured while adding a new  " +
					"branch details record.", exception);			
			mav = super.error("", MAVPaths.JSP_BRANCH_ADD);
		}
		catch (Exception exception){						
			logger.error("An exception occured while adding a new  " +
					"branch details record.", exception);			
			mav = super.error("", MAVPaths.JSP_BRANCH_ADD);
		}	

		logger.exiting("addBranch");
		return mav;
	}

	/**
	 * This method fetches a list of branch records from database.
	 * 
	 * @param request
	 *            an instance of HTTPServletRequest
	 * 
	 * @param response
	 *            an instance of HTTPServletRequest
	 * 
	 * @return upon success redirect to <code>listBranch.jsp</code>, and on
	 *         failure to <code>global_error_page.jsp</code>
	 */
	public ModelAndView listBranches(HttpServletRequest request,
			HttpServletResponse response) {
		
		logger.entering("listBranches");
		
		ModelAndView mav = null;
		
		List<BranchDetails> branchList = null;		
		try {
			branchList = branchService.listBranchDetails();	
			
			mav = new ModelAndView(MAVPaths.VIEW_BRANCH_LIST);
			mav.addObject(MAVObjects.LIST_BRANCH, branchList);		
		} 
		catch (MISPException exception) {		
			logger.error("An exception occured while fetching  " +
					"a list of branches.", exception);			
			mav = super.error("", MAVPaths.JSP_BRANCH_LIST);
		}
		catch (Exception exception){			
			logger.error("An exception occured while fetching  " +
					"a list of branches.", exception);			
			mav = super.error("", MAVPaths.JSP_BRANCH_LIST);
		}	

		logger.exiting("listBranches", branchList);
		return mav;
	}

	/**
	 * This method fetches branch details from DB and populates the screen so that 
	 * the administrator can modify the details of a branch from the platform.
	 * 
	 * @param request
	 *            an instance of HTTPServletRequest
	 * 
	 * @param response
	 *            an instance of HTTPServletRequest
	 * 
	 * @param branchVO
	 *            BranchVO command object
	 * 
	 * @return upon success redirect to <code>modifyBranch.jsp</code>, 
	 * 			and on failure to <code>global_error_page.jsp</code>
	 */
	public ModelAndView showModifyBranchDetails(HttpServletRequest request,
			HttpServletResponse response, BranchVO branchVO) {
		Object[] params = {branchVO};
		logger.entering("showModifyBranchDetails", params);

		ModelAndView mav = null;
		try {
			branchVO = branchService.getBranchDetails(branchVO.getBranchId());
			
			mav = new ModelAndView(MAVPaths.VIEW_BRANCH_MODIFY);
			mav.addObject(MAVObjects.VO_BRANCH, branchVO);
		} 
		catch (MISPException exception) {
			logger.error("An exception occured while fetching a particular " +
					"branch details record.", exception);			
			mav = super.error("", MAVPaths.JSP_BRANCH_LIST);
		}
		catch (Exception exception){			
			logger.error("An exception occured while fetching a particular " +
					"branch details record.", exception);			
			mav = super.error("", MAVPaths.JSP_BRANCH_LIST);
		}	

		logger.exiting("showModifyBranchDetails");
		return mav;
	}


	/**
	 * This method modifies the branch record as entered by the business
	 * administrator.
	 * 
	 * @param request
	 *            an instance of HTTPServletRequest
	 * 
	 * @param response
	 *            an instance of HTTPServletRequest
	 * 
	 * @param branchVO
	 *            BranchVO command object
	 * 
	 * @return upon success redirect to <code>listBranch.jsp </code>, and on
	 *         failure to <code>global_error_page.jsp</code>
	 */
	public ModelAndView modifyBranchDetails(HttpServletRequest request,
			HttpServletResponse response, BranchVO branchVO) {
		
		Object[] params = {branchVO};
		logger.entering("modifyBranchDetails", params);

		ModelAndView mav = null;

		String branchId = request.getParameter("branchId");

		HttpSession session = request.getSession();
		UserDetails loginUserDetails = (UserDetails) session
				.getAttribute(SessionKeys.SESSION_USER_DETAILS);

		try {
			branchVO.setBranchId(branchId);
			branchService.modifyBranchDetails(branchVO,loginUserDetails,false);

			mav = super.success(SuccessMessages.BRANCH_MODIFIED, 
						MAVPaths.JSP_BRANCH_LIST);
		} 
		catch (MISPException exception) {
			logger.error("An exception occured while updating " +
					"branch details record.", exception);			
			mav = super.error("", MAVPaths.JSP_BRANCH_LIST);
		}
		catch (Exception exception){	
			logger.error("An exception occured while updating " +
					"branch details record.", exception);			
			mav = super.error("", MAVPaths.JSP_BRANCH_LIST);
		}	

		logger.exiting("modifyBranchDetails");
		return mav;
	}

	/**
	 * This method de-activates the branch details record as entered by 
	 * the business administrator.
	 * 
	 * @param request
	 *            an instance of HTTPServletRequest
	 * 
	 * @param response
	 *            an instance of HTTPServletRequest
	 * 
	 * @param branchVO
	 *            BranchVO command object
	 * 
	 * @return upon success redirect to <code>listBranch.jsp</code>, and on
	 *         failure to <code>global_error_page.jsp</code>
	 */
	public ModelAndView deactivateBranch(HttpServletRequest request,
			HttpServletResponse response, BranchVO branchVO) {
		
		Object[] params = {branchVO};
		logger.entering("deactivateBranch", params);

		ModelAndView mav = null;

		String branchId = request.getParameter("branchId");

		HttpSession session = request.getSession();
		UserDetails loginUserDetails = (UserDetails) session
				.getAttribute(SessionKeys.SESSION_USER_DETAILS);

		try {
			branchVO.setBranchId(branchId);
			branchService.modifyBranchDetails(branchVO,loginUserDetails,true);

			mav = super.success(SuccessMessages.BRANCH_DEACTIVATED, 
						MAVPaths.JSP_BRANCH_LIST);
		} 
		catch (MISPException exception) {
			logger.error("An exception occured while updating " +
					"branch details record.", exception);			
			mav = super.error("", MAVPaths.JSP_BRANCH_LIST);
		}
		catch (Exception exception){	
			logger.error("An exception occured while updating " +
					"branch details record.", exception);			
			mav = super.error("", MAVPaths.JSP_BRANCH_LIST);
		}	

		logger.exiting("deactivateBranch");
		return mav;
	}
}
