package com.mip.application.dal.managers;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.collections.map.ListOrderedMap;
import org.hibernate.Hibernate;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.transform.Transformers;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;

import com.mip.application.constants.DBObjects;
import com.mip.application.model.CustomerDetails;
import com.mip.application.model.InsuredRelativeDetails;
import com.mip.application.model.ReactivationCustomerRequest;
import com.mip.application.model.UserDetails;
import com.mip.application.view.CustomerDeregistrationVO;
import com.mip.application.view.CustomerVO;
import com.mip.application.view.LoyaltyCustomerVO;
import com.mip.application.view.ModifyCustomerVO;
import com.mip.application.view.SMSTemplateVO;
import com.mip.framework.constants.PlatformConstants;
import com.mip.framework.dao.impls.DataAccessManager;
import com.mip.framework.exceptions.DBException;
import com.mip.framework.logger.LoggerFactory;
import com.mip.framework.logger.MISPLogger;
import com.mip.framework.utils.DateUtil;
import com.mip.framework.utils.StringUtil;

/**
 * <p>
 * <code>CustomerManager.java</code> contains all the methods pertaining to
 * Customer Management use case model. This is a manager class for all customer
 * management modules. The methods in this class access the database through
 * HIBERNATE template.
 * </p>
 * 
 * @author T H B S
 * 
 */
public class CustomerManager extends
		DataAccessManager<CustomerDetails, Integer> {

	/**
	 * Set inversion of control for <code>insuredRelativeDetails</code> and
	 * <code>AdminConfigManager</code>.
	 */
	private InsuredRelativeDetails insuredRelativeDetails;

	/* private AdminConfigManager adminConfigManager; */

	/**
	 * Set inversion of Control for <code>JdbcTemplate</code>.
	 */
	private JdbcTemplate jdbcTemplate;

	/**
	 * This is a setter method for creating dependency injection for
	 * <code>insuredRelativeDetails</code>
	 * 
	 * @param insuredRelativeDetails
	 *            An instance of InsuredRelativeDetails class
	 */
	public void setInsuredRelativeDetails(
			InsuredRelativeDetails insuredRelativeDetails) {

		this.insuredRelativeDetails = insuredRelativeDetails;
	}

	/*
	 * public void setAdminConfigManager(AdminConfigManager adminConfigManager)
	 * { this.adminConfigManager = adminConfigManager; }
	 */

	public void setJdbcTemplate(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}

	/**
	 * An instance of logger.
	 */
	private static MISPLogger logger = LoggerFactory.getInstance().getLogger(
			CustomerManager.class);

	/**
	 * Default constructor of the CustomerManager class
	 */
	public CustomerManager() {
		super(CustomerDetails.class);
	}

	/**
	 * This method updates the modified customer details to the database.
	 * 
	 * @param custModel
	 *            contains the modified customer details.
	 * 
	 * @param insModel
	 *            contains the modified Insured Relative details.
	 * @param addIrd
	 * 
	 * @return boolean value indicating success or failure of update operation
	 * 
	 * @throws <code>DBException</code>, if any database error occurs.
	 */
	public boolean updateCustomerDetails(CustomerDetails custModel,
			InsuredRelativeDetails insModel) throws DBException {
		logger.entering("updateCustomerDetails",custModel,insModel);

		boolean isModified = false;

		try {
			getHibernateTemplate().update(custModel);
			if (null != insModel && !StringUtil.isEmpty(insModel.getFname()) && !insModel.getFname().isEmpty())
				getHibernateTemplate().update(insModel);

			isModified = true;
		} catch (DataAccessException daException) {
			logger.error(
					"Error occured while updating the customer details : ",
					daException);
			throw new DBException(daException);
		}

		logger.exiting("updateCustomerDetails", isModified);

		return isModified;
	}

	/**
	 * This method uses the criteria query of HIBERNATE to search the customer
	 * details for the given input.
	 * 
	 * @param searchMap
	 *            search criteria, contains FNAME, SNAME, MSISDN and STATUS.
	 * 
	 * @return <code>ArrayList<CustomerDetails></code> object
	 * 
	 * @throws <code>DBException</code>, if any database error occurs.
	 */
	@SuppressWarnings("unchecked")
	public ArrayList<CustomerVO> searchCustomerDetails(
			HashMap<String, String> searchMap) throws DBException {
		logger.entering("searchCustomerDetails", searchMap);

		ArrayList<CustomerVO> listCustomerVO = null;
		
		try {
			StringBuilder sqlQuery = new StringBuilder("SELECT ")
					.append("CD.cust_id AS custId, ")
					.append("CD.msisdn AS msisdn, ")
					.append("CONCAT(CD.fname,' ',CD.sname) AS custName, ")
					.append("CS.is_confirmed  AS confirmed, ")
					.append("concat(u.fname,' ',u.sname)  AS regBy, ")
					.append("DATE_FORMAT(CS.reg_date,'%d/%m/%Y %H:%i:%s') AS regDate, ")
					.append("IFNULL(DATE_FORMAT(CS.conf_date,'%d/%m/%Y %H:%i:%s'),'') AS confDate, ")
					.append("(Select pd.product_name from product_details od where pd.product_id = CS.product_id group by CS.cust_id ) as offerName,  ")
					
					//added by gokul for fetching product id
					.append("(Select pd.product_id from product_details od where pd.product_id = CS.product_id group by CS.cust_id ) as offerId ")
					//added by gokul for fetching product id
					
					.append("FROM CUSTOMER_DETAILS CD ")
					.append("JOIN CUSTOMER_SUBSCRIPTION CS ON CD.cust_id=CS.cust_id ")
					.append("INNER JOIN user_details u on u.user_id=CS.reg_by ")
					.append("INNER JOIN product_details pd on pd.product_id = CS.product_id ")
					//.append("WHERE CS.is_deactivated=0 AND ");
					.append("WHERE CS.is_deactivated in (0,2) AND "); // added to check and display the suspended customers
			
			if (!StringUtil.isEmpty(searchMap.get("fname"))
					&& !StringUtil.isEmpty(searchMap.get("sname"))
					&& !StringUtil.isEmpty(searchMap.get("msisdn"))
					&& !StringUtil.isEmpty(searchMap.get("custId"))) {
				sqlQuery.append("CD.fname like '%")
						.append(searchMap.get("fname"))
						.append("%' AND CD.sname like \"%")
						.append(searchMap.get("sname"))
						.append("%\" AND CD.msisdn = '")
						.append(searchMap.get("msisdn"))
						.append(" AND CS.cust_id = '")
						.append(searchMap.get("custId")).append("' ");
			} else if (!StringUtil.isEmpty(searchMap.get("fname"))
					&& !StringUtil.isEmpty(searchMap.get("sname"))
					&& !StringUtil.isEmpty(searchMap.get("custId"))) {
				sqlQuery.append("CD.fname like '%")
						.append(searchMap.get("fname"))
						.append("%' AND CD.sname like \"%")
						.append(searchMap.get("sname"))
						.append("%\" AND  CS.cust_id = '")
						.append(searchMap.get("custId")).append("' ");
			} else if (!StringUtil.isEmpty(searchMap.get("fname"))
					&& !StringUtil.isEmpty(searchMap.get("sname"))
					&& !StringUtil.isEmpty(searchMap.get("msisdn"))) {
				sqlQuery.append("CD.fname like '%")
						.append(searchMap.get("fname"))
						.append("%' AND CD.sname like \"%")
						.append(searchMap.get("sname"))
						.append("%\" AND CD.msisdn = '")
						.append(searchMap.get("msisdn")).append("' ");
			} else if (!StringUtil.isEmpty(searchMap.get("fname"))
					&& !StringUtil.isEmpty(searchMap.get("sname"))) {
				sqlQuery.append("CD.fname like '%")
						.append(searchMap.get("fname"))
						.append("%' AND CD.sname like \"%")
						.append(searchMap.get("sname")).append("%\" ");

			} else if (!StringUtil.isEmpty(searchMap.get("sname"))
					&& !StringUtil.isEmpty(searchMap.get("msisdn"))) {
				sqlQuery.append("CD.sname like \"%")
						.append(searchMap.get("sname"))
						.append("%\" AND CD.msisdn = '")
						.append(searchMap.get("msisdn")).append("' ");

			} else if (!StringUtil.isEmpty(searchMap.get("sname"))
					&& !StringUtil.isEmpty(searchMap.get("custId"))) {
				sqlQuery.append("CD.sname like \"%")
						.append(searchMap.get("sname"))
						.append("%\" AND CS.cust_id = '")
						.append(searchMap.get("custId")).append("' ");

			} else if (!StringUtil.isEmpty(searchMap.get("fname"))
					&& !StringUtil.isEmpty(searchMap.get("msisdn"))) {
				sqlQuery.append("CD.fname like \"%")
						.append(searchMap.get("fname"))
						.append("%\" AND CD.msisdn = '")
						.append(searchMap.get("msisdn")).append("' ");

			} else if (!StringUtil.isEmpty(searchMap.get("fname"))
					&& !StringUtil.isEmpty(searchMap.get("custId"))) {
				sqlQuery.append("CD.fname like \"%")
						.append(searchMap.get("fname"))
						.append("%\" AND CS.cust_id = '")
						.append(searchMap.get("custId")).append("' ");

			} else if (!StringUtil.isEmpty(searchMap.get("sname"))) {
				sqlQuery.append("CD.sname like \"%")
						.append(searchMap.get("sname")).append("%\" ");
			}

			else if (!StringUtil.isEmpty(searchMap.get("fname"))) {
				sqlQuery.append("CD.fname like \"%")
						.append(searchMap.get("fname")).append("%\" ");
			}

			else if (!StringUtil.isEmpty(searchMap.get("msisdn"))) {
				sqlQuery.append("CD.msisdn = '")
						.append(searchMap.get("msisdn")).append("' ");
			} 
			else if (!StringUtil.isEmpty(searchMap.get("custId"))) {
				sqlQuery.append("CS.cust_id = '")
						.append(searchMap.get("custId")).append("' ");
			}

			//sqlQuery.append(" GROUP BY CS.product_id "); old code
			
			//below code added inorder to fetch all the details of the customer while searching through name
			sqlQuery.append(" order BY CS.cust_id ");
			

			listCustomerVO = (ArrayList<CustomerVO>)getSession()
					.createSQLQuery(sqlQuery.toString())
					.addScalar("custId", Hibernate.STRING)
					.addScalar("msisdn", Hibernate.STRING)
					.addScalar("custName", Hibernate.STRING)
					.addScalar("confirmed", Hibernate.BYTE)
					.addScalar("regBy", Hibernate.STRING)
					.addScalar("regDate", Hibernate.STRING)
					.addScalar("confDate", Hibernate.STRING)
					.addScalar("offerName", Hibernate.STRING)
					.addScalar("offerId", Hibernate.STRING)
					.setResultTransformer(
							Transformers.aliasToBean(CustomerVO.class))
							.list();

		} catch (HibernateException hibernateException) {
			logger.error("Error occured while searching customer based "
					+ "on firstname, surname and/or MSISDN: ",
					hibernateException);
			throw new DBException(hibernateException);
		} catch (Exception exception) {
			logger.error("Error occured while searching customer based "
					+ "on firstname, surname and/or MSISDN: ", exception);
		}
		logger.exiting("searchCustomerDetails", listCustomerVO);

		return listCustomerVO;
	}

	@SuppressWarnings("unchecked")
	public ArrayList<CustomerVO> getDeregisteredCustomerList(
			HashMap<String, String> searchMap) throws DBException {
		logger.entering("searchCustomerDetails", searchMap);

		ArrayList<CustomerVO> listCustomerVO = null;
		
		try {
			StringBuilder sqlQuery = new StringBuilder("SELECT ")
					.append("PC.cust_id AS custId, ")
					.append("BC.msisdn AS msisdn, ")
					.append("CONCAT(BC.fname,' ',BC.sname) AS custName, ")
					.append("PC.is_confirmed  AS confirmed, ")
					.append("concat(u.fname,' ',u.sname)  AS regBy, ")
					.append("DATE_FORMAT(PC.reg_date,'%d/%m/%Y %H:%i:%s') AS regDate, ")
					.append("IFNULL(DATE_FORMAT(PC.conf_date,'%d/%m/%Y %H:%i:%s'),'') AS confDate, ")
					.append("IFNULL(DATE_FORMAT(PC.pc_date,'%d/%m/%Y %H:%i:%s'),'') AS deRegisteredDate, ")
					.append("(Select pd.product_name from product_details od where pd.product_id = PC.product_id group by PC.cust_id ) as offerName,  ")
					
					// added by gokul for getting the offer_id
					.append("(Select pd.product_id from product_details od where pd.product_id = PC.product_id group by PC.cust_id ) as offerId ")
					// added by gokul for getting the offer_id
					
					.append("FROM BIMA_CANCELLATIONS BC ")
					.append("JOIN PRODUCT_CANCELLATIONS PC ON BC.BC_ID=PC.BC_ID ")
					.append("INNER JOIN user_details u on u.user_id=PC.reg_by ")
					.append("INNER JOIN product_details pd on pd.product_id = PC.product_id ")
					.append("WHERE ");

			if (!StringUtil.isEmpty(searchMap.get("fname"))
					&& !StringUtil.isEmpty(searchMap.get("sname"))
					&& !StringUtil.isEmpty(searchMap.get("msisdn"))
					&& !StringUtil.isEmpty(searchMap.get("custId"))) {
				sqlQuery.append("BC.fname like '%")
						.append(searchMap.get("fname"))
						.append("%' AND BC.sname like \"%")
						.append(searchMap.get("sname"))
						.append("%\" AND BC.msisdn = '")
						.append(searchMap.get("msisdn"))
						.append(" AND PC.cust_id = '")
						.append(searchMap.get("custId")).append("' ");
			} else if (!StringUtil.isEmpty(searchMap.get("fname"))
					&& !StringUtil.isEmpty(searchMap.get("sname"))
					&& !StringUtil.isEmpty(searchMap.get("custId"))) {
				sqlQuery.append("BC.fname like '%")
						.append(searchMap.get("fname"))
						.append("%' AND BC.sname like \"%")
						.append(searchMap.get("sname"))
						.append("%\" AND  PC.cust_id = '")
						.append(searchMap.get("custId")).append("' ");
			} else if (!StringUtil.isEmpty(searchMap.get("fname"))
					&& !StringUtil.isEmpty(searchMap.get("sname"))
					&& !StringUtil.isEmpty(searchMap.get("msisdn"))) {
				sqlQuery.append("BC.fname like '%")
						.append(searchMap.get("fname"))
						.append("%' AND BC.sname like \"%")
						.append(searchMap.get("sname"))
						.append("%\" AND BC.msisdn = '")
						.append(searchMap.get("msisdn")).append("' ");
			} else if (!StringUtil.isEmpty(searchMap.get("fname"))
					&& !StringUtil.isEmpty(searchMap.get("sname"))) {
				sqlQuery.append("BC.fname like '%")
						.append(searchMap.get("fname"))
						.append("%' AND BC.sname like \"%")
						.append(searchMap.get("sname")).append("%\" ");

			} else if (!StringUtil.isEmpty(searchMap.get("sname"))
					&& !StringUtil.isEmpty(searchMap.get("msisdn"))) {
				sqlQuery.append("BC.sname like \"%")
						.append(searchMap.get("sname"))
						.append("%\" AND BC.msisdn = '")
						.append(searchMap.get("msisdn")).append("' ");

			} else if (!StringUtil.isEmpty(searchMap.get("sname"))
					&& !StringUtil.isEmpty(searchMap.get("custId"))) {
				sqlQuery.append("BC.sname like \"%")
						.append(searchMap.get("sname"))
						.append("%\" AND PC.cust_id = '")
						.append(searchMap.get("custId")).append("' ");

			} else if (!StringUtil.isEmpty(searchMap.get("fname"))
					&& !StringUtil.isEmpty(searchMap.get("msisdn"))) {
				sqlQuery.append("BC.fname like \"%")
						.append(searchMap.get("fname"))
						.append("%\" AND BC.msisdn = '")
						.append(searchMap.get("msisdn")).append("' ");

			} else if (!StringUtil.isEmpty(searchMap.get("fname"))
					&& !StringUtil.isEmpty(searchMap.get("custId"))) {
				sqlQuery.append("BC.fname like \"%")
						.append(searchMap.get("fname"))
						.append("%\" AND PC.cust_id = '")
						.append(searchMap.get("custId")).append("' ");

			} else if (!StringUtil.isEmpty(searchMap.get("sname"))) {
				sqlQuery.append("BC.sname like \"%")
						.append(searchMap.get("sname")).append("%\" ");
			}

			else if (!StringUtil.isEmpty(searchMap.get("fname"))) {
				sqlQuery.append("BC.fname like \"%")
						.append(searchMap.get("fname")).append("%\" ");
			}

			else if (!StringUtil.isEmpty(searchMap.get("msisdn"))) {
				sqlQuery.append("BC.msisdn = '")
						.append(searchMap.get("msisdn")).append("' ");
			} 
			else if (!StringUtil.isEmpty(searchMap.get("custId"))) {
				sqlQuery.append("PC.cust_id = '")
						.append(searchMap.get("custId")).append("' ");
			}
			
			
			sqlQuery.append("AND pc.product_id not in ")
					.append("(select concat(cs1.product_id) ")
					.append("from customer_subscription cs1 ")
					.append("join customer_details cd1 on cd1.cust_id = cs1.cust_id ")
					.append("where ");
			
			
			
			if (!StringUtil.isEmpty(searchMap.get("fname"))
					&& !StringUtil.isEmpty(searchMap.get("sname"))
					&& !StringUtil.isEmpty(searchMap.get("msisdn"))
					&& !StringUtil.isEmpty(searchMap.get("custId"))) {
				sqlQuery.append("cd1.fname like '%")
						.append(searchMap.get("fname"))
						.append("%' AND cd1.sname like \"%")
						.append(searchMap.get("sname"))
						.append("%\" AND cd1.msisdn = '")
						.append(searchMap.get("msisdn"))
						.append(" AND cs1.cust_id = '")
						.append(searchMap.get("custId")).append("' ");
			} else if (!StringUtil.isEmpty(searchMap.get("fname"))
					&& !StringUtil.isEmpty(searchMap.get("sname"))
					&& !StringUtil.isEmpty(searchMap.get("custId"))) {
				sqlQuery.append("cd1.fname like '%")
						.append(searchMap.get("fname"))
						.append("%' AND cd1.sname like \"%")
						.append(searchMap.get("sname"))
						.append("%\" AND  cs1.cust_id = '")
						.append(searchMap.get("custId")).append("' ");
			} else if (!StringUtil.isEmpty(searchMap.get("fname"))
					&& !StringUtil.isEmpty(searchMap.get("sname"))
					&& !StringUtil.isEmpty(searchMap.get("msisdn"))) {
				sqlQuery.append("cd1.fname like '%")
						.append(searchMap.get("fname"))
						.append("%' AND cd1.sname like \"%")
						.append(searchMap.get("sname"))
						.append("%\" AND cd1.msisdn = '")
						.append(searchMap.get("msisdn")).append("' ");
			} else if (!StringUtil.isEmpty(searchMap.get("fname"))
					&& !StringUtil.isEmpty(searchMap.get("sname"))) {
				sqlQuery.append("cd1.fname like '%")
						.append(searchMap.get("fname"))
						.append("%' AND cd1.sname like \"%")
						.append(searchMap.get("sname")).append("%\" ");

			} else if (!StringUtil.isEmpty(searchMap.get("sname"))
					&& !StringUtil.isEmpty(searchMap.get("msisdn"))) {
				sqlQuery.append("cd1.sname like \"%")
						.append(searchMap.get("sname"))
						.append("%\" AND cd1.msisdn = '")
						.append(searchMap.get("msisdn")).append("' ");

			} else if (!StringUtil.isEmpty(searchMap.get("sname"))
					&& !StringUtil.isEmpty(searchMap.get("custId"))) {
				sqlQuery.append("cd1.sname like \"%")
						.append(searchMap.get("sname"))
						.append("%\" AND cs1.cust_id = '")
						.append(searchMap.get("custId")).append("' ");

			} else if (!StringUtil.isEmpty(searchMap.get("fname"))
					&& !StringUtil.isEmpty(searchMap.get("msisdn"))) {
				sqlQuery.append("cd1.fname like \"%")
						.append(searchMap.get("fname"))
						.append("%\" AND cd1.msisdn = '")
						.append(searchMap.get("msisdn")).append("' ");

			} else if (!StringUtil.isEmpty(searchMap.get("fname"))
					&& !StringUtil.isEmpty(searchMap.get("custId"))) {
				sqlQuery.append("cd1.fname like \"%")
						.append(searchMap.get("fname"))
						.append("%\" AND cs1.cust_id = '")
						.append(searchMap.get("custId")).append("' ");

			} else if (!StringUtil.isEmpty(searchMap.get("sname"))) {
				sqlQuery.append("cd1.sname like \"%")
						.append(searchMap.get("sname")).append("%\" ");
			}

			else if (!StringUtil.isEmpty(searchMap.get("fname"))) {
				sqlQuery.append("cd1.fname like \"%")
						.append(searchMap.get("fname")).append("%\" ");
			}

			else if (!StringUtil.isEmpty(searchMap.get("msisdn"))) {
				sqlQuery.append("cd1.msisdn = '")
						.append(searchMap.get("msisdn")).append("' ");
			} 
			else if (!StringUtil.isEmpty(searchMap.get("custId"))) {
				sqlQuery.append("cs1.cust_id = '")
						.append(searchMap.get("custId")).append("' ");
			}
			sqlQuery.append("AND cs1.is_deactivated=0) ");
			//sqlQuery.append(" GROUP BY PC.product_id "); old code
			
			// below code addded by gokul inorder to fetch the latest values from the group function ::: start
			sqlQuery.append("AND pc.pc_id in (select max(pc1.pc_id) from product_cancellations pc1 join bima_cancellations bc1 on pc1.bc_id = bc1.bc_id where ");
			
			if (!StringUtil.isEmpty(searchMap.get("fname"))
					&& !StringUtil.isEmpty(searchMap.get("sname"))
					&& !StringUtil.isEmpty(searchMap.get("msisdn"))
					&& !StringUtil.isEmpty(searchMap.get("custId"))) {
				sqlQuery.append("bc1.fname like '%")
						.append(searchMap.get("fname"))
						.append("%' AND bc1.sname like \"%")
						.append(searchMap.get("sname"))
						.append("%\" AND bc1.msisdn = '")
						.append(searchMap.get("msisdn"))
						.append(" AND bc1.cust_id = '")
						.append(searchMap.get("custId")).append("' ");
			} else if (!StringUtil.isEmpty(searchMap.get("fname"))
					&& !StringUtil.isEmpty(searchMap.get("sname"))
					&& !StringUtil.isEmpty(searchMap.get("custId"))) {
				sqlQuery.append("bc1.fname like '%")
						.append(searchMap.get("fname"))
						.append("%' AND bc1.sname like \"%")
						.append(searchMap.get("sname"))
						.append("%\" AND  bc1.cust_id = '")
						.append(searchMap.get("custId")).append("' ");
			} else if (!StringUtil.isEmpty(searchMap.get("fname"))
					&& !StringUtil.isEmpty(searchMap.get("sname"))
					&& !StringUtil.isEmpty(searchMap.get("msisdn"))) {
				sqlQuery.append("bc1.fname like '%")
						.append(searchMap.get("fname"))
						.append("%' AND bc1.sname like \"%")
						.append(searchMap.get("sname"))
						.append("%\" AND bc1.msisdn = '")
						.append(searchMap.get("msisdn")).append("' ");
			} else if (!StringUtil.isEmpty(searchMap.get("fname"))
					&& !StringUtil.isEmpty(searchMap.get("sname"))) {
				sqlQuery.append("bc1.fname like '%")
						.append(searchMap.get("fname"))
						.append("%' AND bc1.sname like \"%")
						.append(searchMap.get("sname")).append("%\" ");

			} else if (!StringUtil.isEmpty(searchMap.get("sname"))
					&& !StringUtil.isEmpty(searchMap.get("msisdn"))) {
				sqlQuery.append("bc1.sname like \"%")
						.append(searchMap.get("sname"))
						.append("%\" AND bc1.msisdn = '")
						.append(searchMap.get("msisdn")).append("' ");

			} else if (!StringUtil.isEmpty(searchMap.get("sname"))
					&& !StringUtil.isEmpty(searchMap.get("custId"))) {
				sqlQuery.append("bc1.sname like \"%")
						.append(searchMap.get("sname"))
						.append("%\" AND bc1.cust_id = '")
						.append(searchMap.get("custId")).append("' ");

			} else if (!StringUtil.isEmpty(searchMap.get("fname"))
					&& !StringUtil.isEmpty(searchMap.get("msisdn"))) {
				sqlQuery.append("bc1.fname like \"%")
						.append(searchMap.get("fname"))
						.append("%\" AND bc1.msisdn = '")
						.append(searchMap.get("msisdn")).append("' ");

			} else if (!StringUtil.isEmpty(searchMap.get("fname"))
					&& !StringUtil.isEmpty(searchMap.get("custId"))) {
				sqlQuery.append("bc1.fname like \"%")
						.append(searchMap.get("fname"))
						.append("%\" AND bc1.cust_id = '")
						.append(searchMap.get("custId")).append("' ");

			} else if (!StringUtil.isEmpty(searchMap.get("sname"))) {
				sqlQuery.append("bc1.sname like \"%")
						.append(searchMap.get("sname")).append("%\" ");
			}

			else if (!StringUtil.isEmpty(searchMap.get("fname"))) {
				sqlQuery.append("bc1.fname like \"%")
						.append(searchMap.get("fname")).append("%\" ");
			}

			else if (!StringUtil.isEmpty(searchMap.get("msisdn"))) {
				sqlQuery.append("bc1.msisdn = '")
						.append(searchMap.get("msisdn")).append("' ");
			} 
			else if (!StringUtil.isEmpty(searchMap.get("custId"))) {
				sqlQuery.append("bc1.cust_id = '")
						.append(searchMap.get("custId")).append("' ");
			}
			
			sqlQuery.append(" group by pc1.product_id) ");		
			sqlQuery.append("order by bc.bc_date desc ");
			
			// code addded by gokul inorder to fetch the latest values from the group function ::: end

			listCustomerVO = (ArrayList<CustomerVO>)getSession()
					.createSQLQuery(sqlQuery.toString())
					.addScalar("custId", Hibernate.STRING)
					.addScalar("msisdn", Hibernate.STRING)
					.addScalar("custName", Hibernate.STRING)
					.addScalar("confirmed", Hibernate.BYTE)
					.addScalar("regBy", Hibernate.STRING)
					.addScalar("regDate", Hibernate.STRING)
					.addScalar("confDate", Hibernate.STRING)
					.addScalar("offerName", Hibernate.STRING)
					.addScalar("deRegisteredDate",Hibernate.STRING)
					.addScalar("offerId",Hibernate.STRING)
					.setResultTransformer(
							Transformers.aliasToBean(CustomerVO.class))
							.list();

		} catch (HibernateException hibernateException) {
			logger.error("Error occured while searching customer based "
					+ "on firstname, surname and/or MSISDN: ",
					hibernateException);
			throw new DBException(hibernateException);
		} catch (Exception exception) {
			logger.error("Error occured while searching customer based "
					+ "on firstname, surname and/or MSISDN: ", exception);
		}
		logger.exiting("searchCustomerDetails", listCustomerVO);

		return listCustomerVO;
	}

	/**
	 * This method retrieves the customer details from the database for the
	 * given customer Id.
	 * 
	 * @param custId
	 *            Customer Id of the registered customer.
	 * 
	 * @return <code>CustomerDetails</code> Object of type CustomerDetails.
	 * 
	 * @throws <code>DBException</code>, if any database error occurs.
	 */
	public CustomerDetails getCustomerDetails(String custId) throws DBException {

		logger.entering("getCustomerDetails");

		CustomerDetails customerDetails = new CustomerDetails();

		try {
			customerDetails = (CustomerDetails) getHibernateTemplate().get(
					CustomerDetails.class, Integer.parseInt(custId));

		} catch (HibernateException hibernateException) {

			logger.error("Error retrieving customer details: ",
					hibernateException);
			throw new DBException(hibernateException);
		}

		logger.exiting("getCustomerDetails");
		return customerDetails;
	}

	/**
	 * This method retrieves the insured relative details from the database for
	 * the given customer Id.
	 * 
	 * @param custId
	 *            Customer Id of the registered customer.
	 * 
	 * @return <code>InsuredRelativeDetails</code> Object of type
	 *         InsuredRelativeDetails.
	 * 
	 * @throws <code>DBException</code>, if any database error occurs.
	 */
	@SuppressWarnings("unchecked")
	public InsuredRelativeDetails getInsuredRelativeDetails(String custId)
			throws DBException {

		logger.entering("getInsuredRelativeDetails");

		ArrayList<InsuredRelativeDetails> insRelDet = null;

		try {
			String SELECT_QUERY_FOR_INSRELATIVE = "FROM InsuredRelativeDetails insRelDetMO where "
					+ "insRelDetMO.customerDetails.custId = "
					+ Integer.parseInt(custId);

			insRelDet = (ArrayList<InsuredRelativeDetails>) getHibernateTemplate()
					.find(SELECT_QUERY_FOR_INSRELATIVE);

			Iterator<InsuredRelativeDetails> itr = insRelDet.iterator();

			while (itr.hasNext()) {
				insuredRelativeDetails = itr.next();
			}
		} catch (HibernateException ex) {

			logger.error("Error retrieving Insured Relative Details: " + ex);

			throw new DBException(ex);
		}
		logger.exiting("getInsuredRelativeDetails");
		return insuredRelativeDetails;
	}

	/**
	 * This method retrieves the count of customers registered between the given
	 * date range and are not assigned with any offer.
	 * 
	 * @param fromDate
	 *            , <code>Date</code> holds the from date
	 * @param toDate
	 *            , <code>Date</code> holds the to date
	 * @return count, the count of the customers.
	 * @throws <code>DBException</code>, if any database error occurs.
	 */
	@SuppressWarnings("rawtypes")
	public long fetchRegCustomersBwnDates(Date fromDate, Date toDate)
			throws DBException {
		logger.entering("fetchRegCustomersBwnDates", fromDate, toDate);

		long count = 0;

		try {
			// Query to retrieve the count of registered customers between the
			// given dates.
			StringBuilder selectSql = new StringBuilder(
					"SELECT COUNT(*) FROM CustomerSubscription cs, ");

			selectSql
					.append(" CustomerDetails cd WHERE cs.customerDetails.custId = cd.custId ");

			selectSql
					.append(" AND cd.createdDate between :startDate and :toDate AND ");

			selectSql.append("cd.confirmed=1 AND ");

			selectSql
					.append(" cs.offerDetails.offerId = null AND cs.dormant=0 ");

			Query query = super.getSession().createQuery(selectSql.toString());

			query.setParameter("startDate", fromDate);
			query.setParameter("toDate", toDate);

			List customerList = query.list();

			count = ((Long) customerList.get(0)).longValue();

		} catch (DataAccessException daException) {
			logger.error("Exception occured while fetching customers",
					daException);
			throw new DBException(daException);
		}

		logger.exiting("fetchRegCustomersBwnDates", count);

		return count;
	}

	/**
	 * This methods queries the database to check is the input MSISDN is already
	 * assigned to a customer.
	 * 
	 * @param msisdn
	 *            input MSISDN.
	 * 
	 * @return true if exists and false otherwise.
	 * 
	 * @throws DBException
	 */
	@SuppressWarnings("unchecked")
	public boolean checkIfMSISDNExists(String msisdn, int customerId)
			throws DBException {
		logger.entering("checkIfMSISDNExists", msisdn, customerId);

		int rowCount;
		List<CustomerDetails> customerList = null;
		try {
			if (customerId != 0) {
				Object[] params = { msisdn, customerId };

				customerList = getHibernateTemplate().find(
						"FROM CustomerDetails cd where cd.msisdn = ? and "
								+ "cd.custId <> ? ", params);
			} else
				customerList = getHibernateTemplate().find(
						"FROM CustomerDetails cd where cd.msisdn = ? ", msisdn);

			rowCount = customerList.size();
		} catch (DataAccessException exception) {
			logger.error("Exception occured while validating MSISDN", exception);
			throw new DBException(exception);
		}

		logger.exiting("checkIfMSISDNExists");

		if (rowCount > 0)
			return true;
		else
			return false;
	}

	/**
	 * This methods retrieves the customer details based on MSISDN.
	 * 
	 * @param msisdn
	 *            input MSISDN.
	 * 
	 * @return <code>CustomerDetails</code> Object of type CustomerDetails.
	 * 
	 * @throws DBException
	 *             If any {@link DataAccessException} occurs
	 */
	public CustomerVO getCustomerFromMSISDN(String msisdn) throws DBException {
		logger.entering("getCustomerFromMSISDN");

		CustomerVO custDetails = null;
		try {
			StringBuilder selectSql = new StringBuilder(" SELECT ")
					.append(" cd.cust_id AS custId, cd.fname AS fname, cd.sname AS sname, ")
					.append(" cd.msisdn AS msisdn, cd.age AS age, cd.dob AS dob, cd.implied_age AS impliedAge,")
					.append(" cd.gender AS gender, concat(ud.fname,' ',ud.sname) AS modifiedBy, ")
					.append(" cd.modified_date AS modifiedDate, (DATE_FORMAT(MIN(conf_date),'%Y-%m-%d %T')) AS confirmedDate, ")
					.append(" cs.is_confirmed AS confirmed, group_concat(product_id order by product_id) AS purchasedProducts")
					.append(" FROM CUSTOMER_DETAILS cd, CUSTOMER_SUBSCRIPTION cs, USER_DETAILS ud WHERE ")
					.append(" cd.cust_id=cs.cust_id AND ud.user_id=cd.modified_by ")
					.append(" AND cd.msisdn='").append(msisdn).append("'");

			logger.info("Query: ", selectSql.toString());
			custDetails = (CustomerVO) jdbcTemplate.queryForObject(selectSql
					.toString(), new BeanPropertyRowMapper(CustomerVO.class));

		} catch (DataAccessException exception) {
			logger.error("Exception occured while getting Customer Details",
					exception);
			throw new DBException(exception);
		}

		logger.exiting("getCustomerFromMSISDN", custDetails);
		return custDetails;
	}

	/**
	 * This methods retrieves the customer details based on MSISDN.
	 * 
	 * @param msisdn
	 *            input MSISDN.
	 * 
	 * @return <code>CustomerDetails</code> Object of type CustomerDetails.
	 * 
	 * @throws DBException
	 *             If any {@link DataAccessException} occurs
	 */
	@SuppressWarnings("unchecked")
	public List<CustomerVO> getCustomerFromMSISDNForClaims(String msisdn)
			throws DBException {
		logger.entering("getCustomerFromMSISDNForClaims");

		List<CustomerVO> custDetailsList = null;
		try {
			StringBuilder selectSql = new StringBuilder(" SELECT ")
					.append(" cd.cust_id AS custId, cd.age AS age,cd.implied_age AS impliedAge, cd.fname AS fname, cd.sname AS sname,")
					.append(" cd.msisdn AS msisdn,  pd.product_name AS purchasedProducts, ")
					.append(" cs.is_confirmed AS confirmed,DATE_FORMAT(cs.conf_date, '%Y-%m-%d %T') AS confirmedDate, ")
					.append(" concat(udr.fname, ' ', udr.sname) AS regBy,concat(ud.fname, ' ', ud.sname) AS modifiedBy, ")
					.append(" DATE_FORMAT(cd.modified_date, '%Y-%m-%d %T') AS modifiedDate")
					.append(" FROM CUSTOMER_DETAILS cd JOIN  CUSTOMER_SUBSCRIPTION cs ON cs.cust_id = cd.cust_id")
					.append(" JOIN  USER_DETAILS ud ON ud.user_id = cd.modified_by JOIN  PRODUCT_DETAILS pd ON pd.product_id = cs.product_id")
					.append(" LEFT JOIN USER_DETAILS udr ON udr.user_id = cs.reg_by")
					.append(" WHERE cs.is_confirmed = 1  ")
					.append(" AND cd.msisdn='").append(msisdn).append("'");

			logger.info("Query: ", selectSql.toString());

			SQLQuery sqlQuery = super.getSession().createSQLQuery(
					selectSql.toString());

			sqlQuery.addScalar("custId", Hibernate.STRING);
			sqlQuery.addScalar("age", Hibernate.STRING);
			sqlQuery.addScalar("fname", Hibernate.STRING);
			sqlQuery.addScalar("sname", Hibernate.STRING);
			sqlQuery.addScalar("msisdn", Hibernate.STRING);
			sqlQuery.addScalar("purchasedProducts", Hibernate.STRING);
			sqlQuery.addScalar("confirmedDate", Hibernate.STRING);
			sqlQuery.addScalar("regBy", Hibernate.STRING);
			sqlQuery.addScalar("modifiedBy", Hibernate.STRING);
			sqlQuery.addScalar("modifiedDate", Hibernate.STRING);
			sqlQuery.addScalar("impliedAge", Hibernate.STRING);

			custDetailsList = sqlQuery.setResultTransformer(
					Transformers.aliasToBean(CustomerVO.class)).list();

		} catch (DataAccessException exception) {
			logger.error("Exception occured while getting Customer Details",
					exception);
			throw new DBException(exception);
		}

		logger.exiting("getCustomerFromMSISDNForClaims", custDetailsList);
		return custDetailsList;
	}

	/**
	 * This method is used to frame SMS when customer is not confirmed, and
	 * using modify customer he has selected some offer.
	 * 
	 * @param custModel
	 *            contains the customer details
	 * 
	 * @return smsAddedToQueue boolean result saying whether the message has
	 *         been added to queue or not
	 * 
	 */
	public boolean frameSMS(CustomerDetails custModel) throws DBException {

		logger.entering("frameSMS");

		boolean smsAddedToQueue = false;
		try {
			Query query = super
					.getSession()
					.createSQLQuery("CALL SEND_SMS(:msisdn, :smsTemplate)")
					.setParameter("msisdn", custModel.getMsisdn())
					.setParameter("smsTemplate",
							PlatformConstants.SMS_DASHBOARD_XL_REG_SUCCESS);
			query.executeUpdate();

			smsAddedToQueue = true;
		} catch (DataAccessException daException) {
			logger.error("Can't fetch the sms and "
					+ "add it to in_queue for respective msisdn");
			throw new DBException(daException);
		}

		logger.exiting("sending SMS", smsAddedToQueue);

		return smsAddedToQueue;
	}

	/**
	 * This method retrieves the count of customers assigned to an offer based
	 * on their MSISDN
	 * 
	 * @param msisdn
	 *            - holding the customer's MSISDN
	 * @return customerCount, the count of the customers.
	 * @throws <code>DBException</code>, if any database error occurs.
	 */
	@SuppressWarnings("unchecked")
	public CustomerDeregistrationVO getCustomerDetailsForDereg(String msisdn)
			throws DBException {
		logger.entering("getCustomerDetailsForDereg", msisdn);

		CustomerDeregistrationVO customerDeregVO = null;
		List<CustomerDeregistrationVO> customerList = null;

		try {

			StringBuilder selectSql = new StringBuilder("SELECT ")
					.append(" cd.cust_id custId, ")
					.append(" cd.is_confirmed confirmed, ")
					.append(" cs.offer_id offerId, ")
					.append(" cs.is_offer_subscribed isOfferSubscribed, ")
					.append(" cs.deducted_offer_amount deductedOfferAmount ")
					.append(" FROM ").append(DBObjects.TABLE_CUSTOMER_DETAILS)
					.append(" cd LEFT OUTER JOIN ")
					.append(DBObjects.TABLE_CUSTOMER_SUBSCRIPTION)
					.append(" cs ").append(" ON cd.cust_id=cs.cust_id ")
					.append(" WHERE cd.msisdn = ?");

			SQLQuery sqlQuery = super.getSession().createSQLQuery(
					selectSql.toString());

			sqlQuery.setParameter(0, msisdn);
			sqlQuery.addScalar("custId", Hibernate.INTEGER);
			sqlQuery.addScalar("confirmed", Hibernate.INTEGER);
			sqlQuery.addScalar("offerId", Hibernate.INTEGER);
			sqlQuery.addScalar("isOfferSubscribed", Hibernate.INTEGER);
			sqlQuery.addScalar("deductedOfferAmount", Hibernate.BIG_DECIMAL);

			customerList = sqlQuery.setResultTransformer(
					Transformers.aliasToBean(CustomerDeregistrationVO.class))
					.list();

			if (customerList.size() > 0)
				customerDeregVO = customerList.get(0);

		} catch (DataAccessException exception) {

			logger.error("Exception occured while fetching customer details",
					exception);
			throw new DBException(exception);
		}

		logger.exiting("getCustomerDetailsForDereg", customerDeregVO);
		return customerDeregVO;
	}

	/**
	 * 
	 * @param msisdn
	 * @return
	 * @throws DBException
	 */
	@SuppressWarnings("unchecked")
	public CustomerDeregistrationVO checkIfAlreadyDeregister(int custId)
			throws DBException {
		logger.entering("checkIfAlreadyDeregister", custId);

		CustomerDeregistrationVO customerDeregVO = null;
		List<CustomerDeregistrationVO> customerList = null;

		try {

			// Query to retrieve the count of registered customers based on
			// MSISDN.
			StringBuilder selectSql = new StringBuilder("SELECT ")
					.append(" is_network_churned networkChurned ")
					.append(" FROM ")
					.append(DBObjects.TABLE_DEREGISTERED_CUSTOMERS)
					.append(" WHERE cust_id = ?")
					.append(" ORDER BY dr_date DESC");

			SQLQuery sqlQuery = super.getSession().createSQLQuery(
					selectSql.toString());

			sqlQuery.setParameter(0, custId);
			sqlQuery.addScalar("networkChurned", Hibernate.INTEGER);

			customerList = sqlQuery.setResultTransformer(
					Transformers.aliasToBean(CustomerDeregistrationVO.class))
					.list();

			customerDeregVO = customerList.get(0);

		} catch (DataAccessException exception) {

			logger.error(
					"Exception occured while fetching deregistered customer details",
					exception);
			throw new DBException(exception);
		}

		logger.exiting("checkIfAlreadyDeregister", customerDeregVO);
		return customerDeregVO;
	}

	/**
	 * 
	 * @param offerId
	 * @return
	 * @throws DBException
	 */
	public float getOfferChargesFromOffer(int offerId) throws DBException {
		logger.entering("getOfferChargesFromOffer", offerId);

		Float offerCharges = 0F;

		try {

			StringBuilder selectSql = new StringBuilder("SELECT ")
					.append(" DISTINCT(offer_cover_charges) ").append(" FROM ")
					.append(DBObjects.TABLE_PRODUCT_COVER_DETAILS)
					.append(" WHERE offer_id = ?");

			SQLQuery sqlQuery = super.getSession().createSQLQuery(
					selectSql.toString());

			sqlQuery.setParameter(0, offerId);

			List<?> list = sqlQuery.list();

			offerCharges = (Float) list.get(0);

		} catch (DataAccessException daException) {
			logger.error("Exception occured while fetching offer charges");
			throw new DBException(daException);
		}

		logger.exiting("getOfferChargesFromOffer", offerCharges);
		return offerCharges.floatValue();
	}

	/**
	 * 
	 * @param msisdn
	 * @param userId
	 * @param networkChurned
	 * @param refundSuccess
	 * 
	 * @throws DBException
	 */
	public int deregisterCustomer(String msisdn, int userId,
			String productList, int churnType,int deregChurnType) throws DBException {
		Object[] params = { msisdn, userId, productList, churnType ,deregChurnType};
		logger.entering("deregisterCustomer", params);

		int result = 0;

		try {

			StringBuilder selectSql = new StringBuilder("CALL ")
					.append("DEREGISTER_CUSTOMER(?,?,?,?,?)");

			SQLQuery sqlQuery = super.getSession().createSQLQuery(
					selectSql.toString());

			sqlQuery.setParameter(0, msisdn);
			sqlQuery.setParameter(1, userId);
			sqlQuery.setParameter(2, productList);
			sqlQuery.setParameter(3, churnType);
			sqlQuery.setParameter(4, deregChurnType);

			List<?> transactionResult = sqlQuery.list();

			
			result = (Integer) transactionResult.get(0);

		} catch (DataAccessException daException) {

			logger.error("Exception occured while deregistering customer");
			throw new DBException(daException);

		}

		logger.exiting("deregisterCustomer", result);
		return result;
	}

	/**
	 * This method return the registration/de-registration status of the input
	 * MSISDN
	 * 
	 * @param msisdn
	 *            Customer's MSISDN
	 * @return Comma Separated Status Ex:
	 *         "isRegistered, isConfirmed, isSubscribed, isDeregistered"
	 * @throws DBException
	 */
	@SuppressWarnings("unchecked")
	public String checkMSISDNStatus(String msisdn) throws DBException {
		logger.entering("checkMSISDNStatus", msisdn);

		String returnString = "0";
		try {

			StringBuilder queryString = new StringBuilder("SELECT ")
					.append(" IF(cd.cust_id > 0, 1, 0) AS registered, ")
					.append(" IF(cd.is_confirmed = 1, 1, 0) AS confirmed, ")
					.append(" IF(cd.offer_id > 0, 'CD', IF(cs.offer_id > 0, 'CS', '') ) AS offer, ")
					.append(" IF(cs.is_offer_subscribed IS NULL, 0, cs.is_offer_subscribed) AS subscribed, ")
					.append(" IF(dr.is_network_churned IS NULL, -1, dr.is_network_churned) AS churned ")
					.append(" FROM ").append(DBObjects.TABLE_CUSTOMER_DETAILS)
					.append(" cd LEFT OUTER JOIN ")
					.append(DBObjects.TABLE_CUSTOMER_SUBSCRIPTION)
					.append(" cs ON cd.cust_id=cs.cust_id ")
					.append(" LEFT OUTER JOIN ")
					.append(DBObjects.TABLE_DEREGISTERED_CUSTOMERS)
					.append(" dr ON cd.cust_id=dr.cust_id ")
					.append(" WHERE cd.msisdn=").append(msisdn)
					.append(" ORDER BY dr.dr_id LIMIT 1");

			List<ListOrderedMap> valueList = jdbcTemplate
					.queryForList(queryString.toString());

			if (valueList.size() > 0) {
				String registered = String.valueOf(valueList.get(0).get(
						"registered"));
				String confirmed = String.valueOf(valueList.get(0).get(
						"confirmed"));
				String offer = String.valueOf(valueList.get(0).get("offer"));
				String subscribed = String.valueOf(valueList.get(0).get(
						"subscribed"));
				String churned = String
						.valueOf(valueList.get(0).get("churned"));

				returnString = new StringBuilder(registered).append(",")
						.append(confirmed).append(",").append(offer)
						.append(",").append(subscribed).append(",")
						.append(churned).toString();
			}

		} catch (DataAccessException daException) {
			logger.error("An error occured while checking MSISDN status",
					daException);
			throw new DBException(daException);
		}

		logger.exiting("checkMSISDNStatus", returnString);
		return returnString;
	}

	public CustomerVO checkCustomerExists(String msisdn) throws DBException {
		logger.entering("checkCustomerExists", msisdn);

		CustomerVO customerVO = new CustomerVO();
		try {
			StringBuilder sqlQuery = new StringBuilder("SELECT ")
			.append("IFNULL(CD.cust_id,0) AS custId, ")
			.append("CD.fname AS fname, ")
			.append("CD.sname AS sname, ")
			.append("CD.age AS age, ")
			.append("CD.implied_age AS impliedAge, ")
			.append("DATE_FORMAT(CD.dob,'%d/%m/%Y') AS dob, ")
			.append("CD.gender AS gender, ")
			.append("CD.msisdn AS msisdn, ")
			.append("CD.deduction_type AS deductionMode, ")
			.append("IFNULL(IRD.ins_id,0) AS insId, ")
			.append("IFNULL(IRD.fname,'') AS insRelFname, ")
			.append("IFNULL(IRD.sname,'') AS insRelSurname, ")
			.append("IFNULL(IRD.age,'') AS insRelAge, ")
			.append("IFNULL(IRD.ins_msisdn,'') AS insMsisdn, ")
			.append("IFNULL(IRD.offer_id,'') AS insOfferId, ")
			.append("IFNULL(DATE_FORMAT(IRD.dob,'%d/%m/%Y'),'') AS insRelIrDoB, ")
			.append("IFNULL(IRD.cust_relationship,0) AS insRelation, ")
			
			.append("IFNULL(NOM.fname,'') AS ipNomFirstName, ")
			.append("IFNULL(NOM.sname,'') AS ipNomSurName,  ")
			.append("IFNULL(NOM.age,'')  AS ipNomAge, ")
			.append("IFNULL(NOM.ins_msisdn,'')  AS ipInsMsisdn, ")
			
			.append("concat(ud.fname, ' ', ud.sname) AS modifiedBy,")
			.append("DATE_FORMAT(cd.modified_date, '%Y-%m-%d %T') AS modifiedDate,")
			
			.append("CS.reg_by AS createdBy, ")
			.append("GROUP_CONCAT(CS.product_level_id order by product_level_id) as productCoverIdIP, ")
			.append("IFNULL(DATE_FORMAT(MIN(CS.reg_date),'%d/%m/%Y %T'),'') AS createdDate, ")
			.append("GROUP_CONCAT(CS.product_id ORDER BY cs.product_id) AS purchasedProducts , ")
			.append("GROUP_CONCAT(CS.is_deactivated ORDER BY cs.product_id) AS deactivatedProducts , ")
			
			.append("GROUP_CONCAT(CS.product_level_id ORDER BY cs.product_id) AS productCoverIdIP ")
			.append("FROM CUSTOMER_DETAILS CD LEFT OUTER JOIN INSURED_RELATIVE_DETAILS IRD ON CD.cust_id=IRD.cust_id and IRD.offer_id=2 ")
			.append("LEFT OUTER JOIN INSURED_RELATIVE_DETAILS NOM ON CD.cust_id=NOM.cust_id and NOM.offer_id=4 ")
			.append("JOIN CUSTOMER_SUBSCRIPTION CS ON CD.cust_id=CS.cust_id JOIN USER_DETAILS UD ON UD.user_id = CD.modified_by ")
			.append("WHERE CD.msisdn='" + msisdn + "'");
			logger.info("Query: ", sqlQuery.toString());
			customerVO = (CustomerVO) jdbcTemplate.queryForObject(sqlQuery
					.toString(), new BeanPropertyRowMapper(CustomerVO.class));
		} catch (EmptyResultDataAccessException e) {
			logger.info("customerVO: ", customerVO.getCustId());
		} catch (DataAccessException e) {
			logger.error(
					"Exception occured while retrieving subscribed customer details.",
					e);
			throw new DBException(e);
		}

		logger.exiting("checkCustomerExists", customerVO);
		return customerVO;
	}
	
	@SuppressWarnings("unchecked")
	public List<CustomerVO> checkDeregisteredCustomerExists(String custId) throws DBException {
		logger.entering("checkDeregisteredCustomerExists", custId);

		List<CustomerVO> customerVOList = null;
		try {
			StringBuilder selectSql = new StringBuilder("SELECT ")
					.append("IFNULL(bc.fname,'') as fname, ")
					.append("IFNULL(bc.sname,'') as sname, ")
					.append("IFNULL(bc.msisdn,'') as msisdn, ")
					.append("IFNULL(DATE_FORMAT(bc.dob,'%d/%m/%Y'),'') as dob, ")
					.append("IFNULL(bc.age,'') as age, ")
					.append("IFNULL(bc.gender,'') as gender, ")
					.append("IFNULL(bc.irfname,'') as insRelFname, ")
					.append("IFNULL(bc.irsname,'') as insRelSurname, ")
					.append("IFNULL(DATE_FORMAT(bc.irdob,'%d/%m/%Y'),'') as insRelIrDoB, ")
					.append("IFNULL(bc.irage,'') as insRelAge, ")
					.append("IFNULL(bc.irMsisdn,'') as insMsisdn, ")
					.append("IFNULL(bc.cust_relationship,'') as insRelation, ")
					.append("IFNULL(bc.kinfname,'') as ipNomFirstName, ")
					.append("IFNULL(bc.kinsname,'') as ipNomSurName, ")
					.append("IFNULL(bc.kinage,'') as ipNomAge, ")
					.append("IFNULL(bc.kiMsisdn,'') as ipInsMsisdn ")
					.append("from bima_cancellations bc inner  ")
					.append("join product_cancellations pc on bc.bc_id=pc.bc_id ")

					.append("where pc.cust_id='" + custId + "'")
					.append("group by bc.cust_id ");
					
			logger.info("Query: ", selectSql.toString());
			
			SQLQuery sqlQuery = super.getSession().createSQLQuery(
					selectSql.toString());

			sqlQuery.addScalar("fname", Hibernate.STRING);
			sqlQuery.addScalar("sname", Hibernate.STRING);
			sqlQuery.addScalar("msisdn", Hibernate.STRING);
			sqlQuery.addScalar("dob", Hibernate.STRING);
			sqlQuery.addScalar("age", Hibernate.STRING);
			sqlQuery.addScalar("gender", Hibernate.STRING);
			sqlQuery.addScalar("insRelFname", Hibernate.STRING);
			sqlQuery.addScalar("insRelSurname", Hibernate.STRING);
			sqlQuery.addScalar("insRelIrDoB", Hibernate.STRING);
			sqlQuery.addScalar("insRelAge", Hibernate.STRING);
			sqlQuery.addScalar("insRelation", Hibernate.STRING);
			sqlQuery.addScalar("ipNomFirstName", Hibernate.STRING);
			sqlQuery.addScalar("ipNomSurName", Hibernate.STRING);
			sqlQuery.addScalar("ipNomAge", Hibernate.STRING);
			sqlQuery.addScalar("insMsisdn", Hibernate.STRING);
			sqlQuery.addScalar("ipInsMsisdn", Hibernate.STRING);
			
			customerVOList = sqlQuery.setResultTransformer(
					Transformers.aliasToBean(CustomerVO.class))
					.list();
			
		 } catch (DataAccessException exception) {

			logger.error("Exception occured while fetching customer details",
					exception);
			throw new DBException(exception);
		
		} 

		logger.exiting("checkDeregisteredCustomerExists", customerVOList);
		return customerVOList;
	}

	public CustomerVO checkCustomerExistsForReg(String msisdn)
			throws DBException {
		logger.entering("checkCustomerExistsForReg", msisdn);

		CustomerVO customerVO = new CustomerVO();
		try {
			StringBuilder sqlQuery = new StringBuilder("SELECT ")
					.append("IFNULL(CD.cust_id,0) AS custId, ")
					.append("CD.fname AS fname, ")
					.append("CD.sname AS sname, ")
					.append("CD.age AS age, ")
					.append("DATE_FORMAT(CD.dob,'%d/%m/%Y') AS dob, ")
					.append("CD.gender AS gender, ")
					.append("CD.msisdn AS msisdn, ")
					.append("CD.deduction_type AS deductionMode, ")
					.append("IFNULL(IRD.ins_id,0) AS insId, ")
					.append("IFNULL(IRD.fname,'') AS insRelFname, ")
					.append("IFNULL(IRD.sname,'') AS insRelSurname, ")
					.append("IFNULL(IRD.age,'') AS insRelAge, ")
					.append("IFNULL(DATE_FORMAT(IRD.dob,'%d/%m/%Y'),'') AS insRelIrDoB, ")
					.append("IFNULL(IRD.cust_relationship,0) AS insRelation, ")
					.append("CS.reg_by AS createdBy, ")
					.append("IFNULL(DATE_FORMAT(MIN(CS.reg_date),'%d/%m/%Y %T'),'') AS createdDate, ")
					.append("GROUP_CONCAT(CS.product_id ORDER BY cs.product_id) AS purchasedProducts , ")
					.append("GROUP_CONCAT(CS.product_level_id ORDER BY cs.product_id) AS productCoverIdIP ")
					.append("FROM CUSTOMER_DETAILS CD LEFT OUTER JOIN INSURED_RELATIVE_DETAILS IRD ON CD.cust_id=IRD.cust_id ")
					.append("JOIN CUSTOMER_SUBSCRIPTION CS ON CD.cust_id=CS.cust_id ")
					.append("WHERE CD.msisdn='" + msisdn + "'")
					.append(" AND CS.product_id = 3 AND CS.is_deactivated ");

			logger.info("Query: ", sqlQuery.toString());
			customerVO = (CustomerVO) jdbcTemplate.queryForObject(sqlQuery
					.toString(), new BeanPropertyRowMapper(CustomerVO.class));
		} catch (EmptyResultDataAccessException e) {
			logger.info("customerVO: ", customerVO.getCustId());
		} catch (DataAccessException e) {
			logger.error(
					"Exception occured while retrieving subscribed customer details.",
					e);
			throw new DBException(e);
		}

		logger.exiting("checkCustomerExistsForReg", customerVO);
		return customerVO;
	}

	public boolean registerCust(CustomerVO custVO, UserDetails user,int  responseCode)
 throws Exception {
		Object[] params = { custVO, user, responseCode };
		logger.entering("registerCust", params);
		Boolean isRegistered = false;

		try {

			if (responseCode == 2)
				isRegistered = true;
			else if (responseCode == 3) {

				Query queryAddInsured = super
						.getSession()
						.createSQLQuery(
								" INSERT INTO INSURED_RELATIVE_DETAILS( "
										+ " fname, sname, age, dob, cust_id, cust_relationship, offer_id,ins_msisdn) "
										+ " VALUES "
										+ " (:fname, :sname, :age, :dob, :custId, :custRelationship, :offerId,:insMsisdn) ")
						.setParameter("fname", custVO.getInsRelFname())
						.setParameter("sname", custVO.getInsRelSurname())
						.setParameter("age", custVO.getInsRelAge())
						.setParameter(
								"dob",
								StringUtil.isEmpty(custVO.getInsRelIrDoB()) ? null
										: DateUtil.toSQLDate(custVO
												.getInsRelIrDoB()))
						.setParameter("custId", custVO.getCustId())
						.setParameter("custRelationship",
								custVO.getInsRelation())
						.setParameter("offerId", "2")
						.setParameter("insMsisdn", custVO.getInsMsisdn())
						;

				int row = queryAddInsured.executeUpdate();
				if (row > 0){
					isRegistered = true;
					sendSMStoNominee(custVO, "1,2");
					
					
				}
				
			} else if (responseCode == 4) {
				Query queryAddInsured = super
						.getSession()
						.createSQLQuery(
								" INSERT INTO INSURED_RELATIVE_DETAILS( "
										+ " fname, sname, age, dob, cust_id, cust_relationship,offer_id,ins_msisdn) "
										+ " VALUES "
										+ " (:fname, :sname, :age, :dob, :custId, :custRelationship, :offerId,:insMsisdn) ")
						.setParameter("fname", custVO.getIpNomFirstName())
						.setParameter("sname", custVO.getIpNomSurName())
						.setParameter("age", custVO.getIpNomAge())
						.setParameter("dob", null)
						.setParameter("custId", custVO.getCustId())
						.setParameter("custRelationship", null)
						.setParameter("offerId", "4")
						.setParameter("insMsisdn", custVO.getIpInsMsisdn());

				int row = queryAddInsured.executeUpdate();
				if (row > 0){
					isRegistered = true;
					sendSMStoNominee(custVO, "4");
					
					
				}

			} else if (responseCode == 5) {

				Query queryAddInsured = super
						.getSession()
						.createSQLQuery(
								" INSERT INTO INSURED_RELATIVE_DETAILS( "
										+ " fname, sname, age, dob, cust_id, cust_relationship, offer_id,ins_msisdn) "
										+ " VALUES "
										+ " (:fname, :sname, :age, :dob, :custId, :custRelationship, :offerId,:insMsisdn) ")
						.setParameter("fname", custVO.getInsRelFname())
						.setParameter("sname", custVO.getInsRelSurname())
						.setParameter("age", custVO.getInsRelAge())
						.setParameter(
								"dob",
								StringUtil.isEmpty(custVO.getInsRelIrDoB()) ? null
										: DateUtil.toSQLDate(custVO
												.getInsRelIrDoB()))
						.setParameter("custId", custVO.getCustId())
						.setParameter("custRelationship",
								custVO.getInsRelation())
						.setParameter("offerId", "2")
						.setParameter("insMsisdn", custVO.getInsMsisdn())
						;

				int row = queryAddInsured.executeUpdate();
				if (row > 0)
					isRegistered = true;

				queryAddInsured = super
						.getSession()
						.createSQLQuery(
								" INSERT INTO INSURED_RELATIVE_DETAILS( "
										+ " fname, sname, age, dob, cust_id, cust_relationship,offer_id,ins_msisdn) "
										+ " VALUES "
										+ " (:fname, :sname, :age, :dob, :custId, :custRelationship, :offerId,:insMsisdn) ")
						.setParameter("fname", custVO.getIpNomFirstName())
						.setParameter("sname", custVO.getIpNomSurName())
						.setParameter("age", custVO.getIpNomAge())
						.setParameter("dob", null)
						.setParameter("custId", custVO.getCustId())
						.setParameter("custRelationship", null)
						.setParameter("offerId", "4")
						.setParameter("insMsisdn", custVO.getIpInsMsisdn());

				row = queryAddInsured.executeUpdate();
				if (row > 0)
					isRegistered = true;
								
				
				sendSMStoNominee(custVO, "1,2");
				sendSMStoNominee(custVO, "4");
					
					
				
			}

			else {
				isRegistered = false;
			}

		} catch (Exception e) {
			logger.error("Exception occured while registering the customer", e);
			throw new Exception(e);
		}
		logger.exiting("registerCust", isRegistered);
		return isRegistered;
	}

	public CustomerVO getCustProductsForDeReg(String msisdn) throws DBException {
		logger.entering("getCustProductsForDeReg", msisdn);

		CustomerVO customerVO = new CustomerVO();
		try {
			StringBuilder sqlQuery = new StringBuilder("SELECT ")
					.append("IFNULL(CD.cust_id,0) AS custId, ")
					.append("CD.msisdn AS msisdn, ")
					.append("IFNULL(GROUP_CONCAT(CS.product_id ORDER BY cs.product_id),0) AS purchasedProducts ")
					.append("FROM CUSTOMER_DETAILS CD ")
					.append("JOIN CUSTOMER_SUBSCRIPTION CS ON CD.cust_id=CS.cust_id ")
					.append("WHERE CD.msisdn='" + msisdn + "'")
					.append(" AND CS.is_deactivated=0");

			logger.info("Query: ", sqlQuery.toString());
			customerVO = (CustomerVO) jdbcTemplate.queryForObject(sqlQuery
					.toString(), new BeanPropertyRowMapper(CustomerVO.class));
		} catch (EmptyResultDataAccessException e) {
			logger.info("customerVO: ", customerVO.getCustId());
		} catch (DataAccessException e) {
			logger.error(
					"Exception occured while retrieving customer details.", e);
			throw new DBException(e);
		}

		logger.exiting("getCustProductsForDeReg", customerVO);
		return customerVO;
	}

	public List<CustomerVO> getCustomerDetailsForChangeMode(String msisdn)
			throws DBException {
		logger.entering("getCustomerDetailsForChangeMode", msisdn);
		List<CustomerVO> customerVO = null;
		try {
			StringBuilder sqlQuery = new StringBuilder(
					"select cust_id as custId ,msisdn as msisdn ,deduction_type as deductionMode ")
					.append("from customer_details  where  msisdn=").append(
							msisdn);

			customerVO = getSession()
					.createSQLQuery(sqlQuery.toString())
					.addScalar("msisdn", Hibernate.STRING)
					.addScalar("deductionMode", Hibernate.STRING)
					.addScalar("custId", Hibernate.STRING)
					.setResultTransformer(
							Transformers.aliasToBean(CustomerVO.class)).list();

		} catch (DataAccessException exception) {
			logger.error("Exception occured while getting PA customer",
					exception);
			throw new DBException(exception);
		}
		logger.exiting("getCustomerDetailsForChangeMode", customerVO);
		return customerVO;

	}

	public boolean checkIfMSISDNExistsForChangeMode(String msisdn)
			throws DBException {

		logger.entering("checkIfMSISDNExistsForChangeMode", msisdn);

		int rowCount;
		List<CustomerDetails> customerList = null;
		try {

			customerList = getHibernateTemplate()
					.find("FROM CustomerDetails cd where cd.deductionMode<>0 and cd.msisdn = ?  ",
							msisdn);

			rowCount = customerList.size();
		} catch (DataAccessException exception) {
			logger.error("Exception occured while validating MSISDN", exception);
			throw new DBException(exception);
		}

		logger.exiting("checkIfMSISDNExistsForChangeMode");

		if (rowCount > 0)
			return true;
		else
			return false;

	}

	public boolean checkIfMSISDNExistsForCoverHistory(String msisdn)
			throws DBException {
		logger.entering("checkIfMSISDNExistsForCoverHistory", msisdn);

		int rowCount;
		List<CustomerDetails> customerList = null;
		List<CustomerVO> customerVO = null;
		boolean result = false;
		try {

			/*
			 * customerList = getHibernateTemplate().find(
			 * "FROM CustomerDetails cd  where  cd.msisdn = ?", msisdn);
			 */
			StringBuilder sql = new StringBuilder()
					.append("SELECT msisdn as msisdn FROM customer_details cd join customer_subscription cs ")
					.append(" on cd.cust_id=cs.cust_id ")
					.append(" where cd.msisdn= '").append(msisdn)
					.append("' and is_confirmed=1 ");

			customerVO = getSession()
					.createSQLQuery(sql.toString())

					.addScalar("msisdn", Hibernate.STRING)
					.setResultTransformer(
							Transformers.aliasToBean(CustomerVO.class)).list();

			// rowCount = customerList.size();
			rowCount = customerVO.size();
		} catch (DataAccessException exception) {
			logger.error("Exception occured while validating MSISDN", exception);
			throw new DBException(exception);
		}

		logger.info("rowCount", rowCount);
		if (rowCount > 0)
			result = true;
		else
			result = false;

		logger.exiting("checkIfMSISDNExistsForCoverHistory", result);
		return result;

	}

	public List<CustomerVO> coverHistoryDetails(String msisdn)
			throws DBException {
		logger.entering("coverHistoryDetails", msisdn);

		List<CustomerVO> customerVO = null;
		try {
			StringBuilder sqlQuery = new StringBuilder(
					"SELECT  cust_id as custId,")
					.append(" msisdn as msisdn,")
					.append("prev_month_usage as prevMonthUsage,")
					.append("cover_free as coverFree,")
					.append(" if( charges_xl = 0.00 , 0, charges_xl ) as chargesXL,")
					.append("cover_xl as coverXL,")
					.append(" if( charges_hp = 0.00 ,0, charges_hp ) as chargesHP,")
					.append("cover_hp as coverHP,")
					.append(" if( charges_ip = 0.00 ,0, charges_ip ) as chargesIP,")
					.append("cover_ip as coverIP,")
					.append("concat( month ,'-',year ) as month,")
					.append("year as year,")
					.append("created_date as createdDate ")
					.append("FROM coverhistory ").append("Where msisdn='")
					.append(msisdn).append("' order by created_date desc");

			customerVO = getSession()
					.createSQLQuery(sqlQuery.toString())
					.addScalar("custId", Hibernate.STRING)
					.addScalar("msisdn", Hibernate.STRING)
					.addScalar("prevMonthUsage", Hibernate.STRING)
					.addScalar("coverFree", Hibernate.STRING)
					.addScalar("chargesXL", Hibernate.STRING)
					.addScalar("coverXL", Hibernate.STRING)
					.addScalar("chargesHP", Hibernate.STRING)
					.addScalar("coverHP", Hibernate.STRING)
					.addScalar("chargesIP", Hibernate.STRING)
					.addScalar("coverIP", Hibernate.STRING)
					.addScalar("month", Hibernate.STRING)
					.addScalar("year", Hibernate.STRING)
					.addScalar("createdDate", Hibernate.STRING)
					.setResultTransformer(
							Transformers.aliasToBean(CustomerVO.class)).list();

		} catch (DataAccessException exception) {
			logger.error("Exception occured while geting cover history",
					exception);
			throw new DBException(exception);
		}
		logger.exiting("coverHistoryDetails", customerVO.size());
		return customerVO;

	}

	public List<CustomerVO> moreCoverHistoryDetails(String msisdn)
			throws DBException {

		logger.entering("getCoverHistoryDetails", msisdn);

		List<CustomerVO> coverHistoryList = null;

		Calendar cal = Calendar.getInstance();
		cal.setTime(new Date());

		int currMonth = cal.get(Calendar.MONTH);
		int currYear = cal.get(Calendar.YEAR);

		logger.debug("currMonth : ", currMonth);
		logger.debug("currYear : ", currYear);

		// Cover history data available from July 2012
		int startYear = 2012;
		// int startMonth = 6;

		// Build the sql query
		StringBuilder queryString = new StringBuilder("");

		while (startYear <= currYear) {

			if (startYear == currYear && currMonth == 0) {

				break;

			}

			if (startYear != 2012) {

				queryString.append(" union ");
				queryString
						.append(" select cover_history_id as coverHistoryId,cust_id as customerDetails,msisdn as msisdn,");
				queryString
						.append(" arpu as avgRevenuePerUser,earned_cover as earnedCover,cumulative_cover as cumulativeCover,");
				queryString
						.append(" month as month, year as year, created_date as createdDate from cover_history_q1_")
						.append(startYear).append(" where msisdn='");
				queryString.append(msisdn);
				queryString.append("'");

				if (startYear == currYear && currMonth <= 2) {

					break;
				}

				queryString.append(" union ");
				queryString
						.append(" select cover_history_id as coverHistoryId,cust_id as customerDetails,msisdn as msisdn,");
				queryString
						.append(" arpu as avgRevenuePerUser,earned_cover as earnedCover,cumulative_cover as cumulativeCover,");
				queryString
						.append(" month as month, year as year, created_date as createdDate from cover_history_q2_")
						.append(startYear).append(" where msisdn='");
				queryString.append(msisdn);
				queryString.append("'");

				if (startYear == currYear && currMonth <= 5) {

					break;
				}
			}

			if (startYear == 2012) {
				queryString
						.append(" select cover_history_id as coverHistoryId,cust_id as customerDetails,msisdn as msisdn,");
				queryString
						.append(" arpu as avgRevenuePerUser,earned_cover as earnedCover,cumulative_cover as cumulativeCover,");
				queryString
						.append(" month as month, year as year, created_date as createdDate from cover_history_q3_")
						.append(startYear).append(" where msisdn='");
				queryString.append(msisdn);
				queryString.append("'");
			} else {

				queryString.append(" union ");

				queryString
						.append(" select cover_history_id as coverHistoryId,cust_id as customerDetails,msisdn as msisdn,");
				queryString
						.append(" arpu as avgRevenuePerUser,earned_cover as earnedCover,cumulative_cover as cumulativeCover,");
				queryString
						.append(" month as month, year as year, created_date as createdDate from cover_history_q3_")
						.append(startYear).append(" where msisdn='");
				queryString.append(msisdn);
				queryString.append("'");

			}
			if (startYear == currYear && currMonth <= 8) {

				break;
			}

			queryString.append(" union ");
			queryString
					.append(" select cover_history_id as coverHistoryId,cust_id as customerDetails,msisdn as msisdn,");
			queryString
					.append(" arpu as avgRevenuePerUser,earned_cover as earnedCover,cumulative_cover as cumulativeCover,");
			queryString
					.append(" month as month, year as year, created_date as createdDate from cover_history_q4_")
					.append(startYear).append(" where msisdn='");
			queryString.append(msisdn);
			queryString.append("'");

			startYear = startYear + 1;
		}

		queryString.append(" order by createdDate desc ");

		logger.debug("sql query : ", queryString.toString());

		/*
		 * try { coverHistoryList = jdbcTemplateCH.query(queryString.toString(),
		 * new BeanPropertyRowMapper(CoverHistory.class)); } catch
		 * (DataAccessException e) {
		 * logger.error("Error retrieving cover history details ", e); throw new
		 * DBException(e); }
		 */

		logger.exiting("getCoverHistoryDetails",
				(coverHistoryList == null) ? null : coverHistoryList.size());
		return coverHistoryList;

	}

	/**
	 * This method get customer details for deregistration
	 * 
	 * @param msisdn
	 * @return
	 * @throws DBException
	 */
	@SuppressWarnings("unchecked")
	public List<CustomerVO> customerDetailsForDereg(String msisdn)
			throws DBException {
		logger.entering("customerDetailsForDereg", msisdn);

		List<CustomerVO> customerDetailsList = new ArrayList<CustomerVO>();
		try {

			StringBuilder sqlQuery = new StringBuilder()
					.append(" select concat(fname,' ' ,sname) custName, ")
					.append(" msisdn as msisdn ,")
					.append(" (select product_name from product_details pd where pd.product_id= cs.product_id) as purchasedProducts,")
					// .append(" (select truncate(product_cover,0) from product_cover_details pcd where pcd.product_level_id= cs.product_level_id ) as regProductLevel,")
					.append(" if( is_confirmed =1,'Confirmed','Not confirmed' ) as isConfirmed ,")
					.append(" truncate(deducted_offer_amount,2) as deductedOfferAmount, ")
					.append(" truncate(earned_cover,0) as earnedCover ,")
					.append(" prev_month_usage as prevMonthUsage ")
					.append(" from  customer_details cd join   customer_subscription cs ")
					.append(" ON cd.cust_id = cs.cust_id ")
					.append(" where  cd.msisdn = '").append(msisdn).append("'")
					.append(" and cs.is_deactivated <> 1 ");

			logger.info("Query: ", sqlQuery.toString());

			customerDetailsList = getSession()
					.createSQLQuery(sqlQuery.toString())
					.addScalar("msisdn", Hibernate.STRING)
					.addScalar("custName", Hibernate.STRING)
					.addScalar("purchasedProducts", Hibernate.STRING)
					// .addScalar("regProductLevel", Hibernate.STRING)
					.addScalar("isConfirmed", Hibernate.STRING)
					.addScalar("deductedOfferAmount", Hibernate.STRING)
					.addScalar("earnedCover", Hibernate.STRING)
					.addScalar("prevMonthUsage", Hibernate.STRING)
					.setResultTransformer(
							Transformers.aliasToBean(CustomerVO.class)).list();

		} catch (DataAccessException e) {
			logger.error(
					"Exception occured while retrieving customer details.", e);
			throw new DBException(e);
		}

		logger.exiting("customerDetailsForDereg", customerDetailsList);
		return customerDetailsList;

	}

	/**
	 * This method get deregistered customer details
	 * 
	 * @param msisdn
	 * @return
	 * @throws DBException
	 */
	@SuppressWarnings("unchecked")
	public List<CustomerVO> customerDeregisteredDetails(String msisdn)
			throws DBException {
		logger.entering("customerDeregisteredDetails", msisdn);

		List<CustomerVO> custDetails = new ArrayList<CustomerVO>();
		String custId;

		try {
			StringBuilder sqlQuery = new StringBuilder();

			
			sqlQuery = new StringBuilder()
			.append(" select bc.cust_id  as custId,")
			.append(" concat(fname, ' ', sname) as custName,")
			.append(" msisdn as msisdn , ")
			.append(" ( select product_name from product_details pd where pd.product_id=pc.product_id )  as purchasedProducts ,")
			//.append(" ( select truncate(product_cover,0) from product_cover_details pcd where pcd.product_level_id= pc.product_level_id  )  as regProductLevel, ")
			.append(" truncate(deducted_offer_amount,2) as deductedOfferAmount, ")
			.append(" truncate(earned_cover,0) as  earnedCover , ")
			.append(" prev_month_usage as prevMonthUsage , ")
			.append(" ( select concat (user_uid , ' - ' , fname, ' ' , sname) from user_details where user_id= pc_by ) as deRegBy ,")
			.append(" DATE_FORMAT(pc_date, ' %Y-%m-%d %H:%i:%s')as deRegDate , ")
			.append(" DATE_FORMAT(record_deletion_date, ' %Y-%m-%d %H:%i:%s')  as dateOfCustomerRemoval ")
			.append(" from bima_cancellations bc join product_cancellations pc ")
			.append(" on bc.bc_id=pc.bc_id ")
			.append(" where bc.msisdn =")
			.append(msisdn);

			custDetails = getSession()
					.createSQLQuery(sqlQuery.toString())
					.addScalar("custId", Hibernate.STRING)
					.addScalar("custName", Hibernate.STRING)
					.addScalar("msisdn", Hibernate.STRING)
					.addScalar("purchasedProducts", Hibernate.STRING)
					//.addScalar("regProductLevel", Hibernate.STRING)
					.addScalar("deductedOfferAmount", Hibernate.STRING)
					.addScalar("earnedCover", Hibernate.STRING)
					.addScalar("prevMonthUsage", Hibernate.STRING)
					.addScalar("deRegBy", Hibernate.STRING)
					.addScalar("deRegDate", Hibernate.STRING)
					.addScalar("dateOfCustomerRemoval", Hibernate.STRING)
					.setResultTransformer(
							Transformers.aliasToBean(CustomerVO.class))
					.list();
	
			
	

	} catch (Exception e) {

		logger.error(
				"Exception occured while retrieving customer details.", e);
		throw new DBException(e);
	}

	logger.exiting("customerDeregisteredDetails", custDetails);
	return custDetails;
	}

	@SuppressWarnings("unchecked")
	public CustomerVO getOfferSubscriptionFromCustId(int custId)
			throws DBException {
		logger.entering("getOfferSubscriptionFromCustId", custId);

		List<CustomerVO> customerVOList = null;
		CustomerVO customerVO = null;
		try {
			StringBuilder sqlQuery = new StringBuilder("SELECT ")
					.append(" GROUP_CONCAT(product_id order by product_id) as purchasedProducts,")
					.append(" GROUP_CONCAT(product_level_id order by product_level_id) as productCoverIdIP")
					.append(" FROM ").append(" customer_subscription ")
					.append(" WHERE ").append(" cust_id = ").append(custId);

			customerVOList = getSession()
					.createSQLQuery(sqlQuery.toString())
					.addScalar("purchasedProducts", Hibernate.STRING)
					.addScalar("productCoverIdIP", Hibernate.STRING)
					.setResultTransformer(
							Transformers.aliasToBean(CustomerVO.class)).list();

			customerVO = customerVOList.get(0);

		} catch (DataAccessException exception) {
			logger.error("Exception occured while geting cover history",
					exception);
			throw new DBException(exception);
		}
		logger.exiting("getOfferSubscriptionFromCustId", customerVO);
		return customerVO;

	}

	
	/*This method will fetch the Deregistered Customer Offer Details*/
	@SuppressWarnings("unchecked")
	public CustomerVO getDeregisteredOfferSubscriptionFromCustId(String custId)
			throws DBException {
		logger.entering("getDeregisteredOfferSubscriptionFromCustId", custId);

		List<CustomerVO> customerVOList = null;
		CustomerVO customerVO = null;
		try {
			StringBuilder sqlQuery = new StringBuilder("SELECT ")
					.append(" GROUP_CONCAT(product_id order by product_id) as purchasedProducts,")
					.append(" GROUP_CONCAT(product_level_id order by product_level_id) as productCoverIdIP")
					.append(" FROM ").append(" product_cancellations ")
					.append("where cust_id='" + custId + "'");

			customerVOList = getSession()
					.createSQLQuery(sqlQuery.toString())
					.addScalar("purchasedProducts", Hibernate.STRING)
					.addScalar("productCoverIdIP", Hibernate.STRING)
					.setResultTransformer(
							Transformers.aliasToBean(CustomerVO.class)).list();

			customerVO = customerVOList.get(0);

		} catch (DataAccessException exception) {
			logger.error("Exception occured while geting cover history",
					exception);
			throw new DBException(exception);
		}
		logger.exiting("getDeregisteredOfferSubscriptionFromCustId", customerVO);
		return customerVO;

	}

	/**
	 * This method get the modified customer details
	 * 
	 * @param msisdn
	 * @return
	 * @throws DBException
	 */

	@SuppressWarnings("unchecked")
	public List<ModifyCustomerVO> getSummaryDetailsChange(String custId,
			String recordsLimit) throws DBException {
		logger.entering("getSummaryDetailsChange", custId);

		List<ModifyCustomerVO> customerDetailsList = new ArrayList<ModifyCustomerVO>();
		try {

			StringBuilder sqlQuery = new StringBuilder()
					.append(" SELECT ")
					.append(" ca.cust_id as custId,")
					.append(" ca.msisdn as msisdn ,")
					.append(" new_fname as newFname , ")
					.append(" old_fname as oldFname ,")
					.append(" new_sname as newSname,")
					.append(" old_sname as oldSname,")
					.append(" new_dob as newDob,")
					.append(" old_dob as oldDob,")
					.append(" new_age as newAge,")
					.append(" old_age as oldAge,")
					.append(" new_gender as newGender,")
					.append(" old_gender as oldGender,")
					.append(" new_ird_fname as newInsRelFname,")
					.append(" old_ird_fname as oldInsRelFname,")
					.append(" new_ird_sname as newInsRelSurname ,")
					.append(" old_ird_sname as oldInsRelSurname ,")
					.append("new_ird_dob as newInsRelIrDoB,")
					.append("old_ird_dob as oldInsRelIrDoB,")
					.append(" new_ird_age as newInsRelAge ,")
					.append(" old_ird_age as oldInsRelAge ,")
					.append(" new_cust_relationship as newCustRelationship,")
					.append(" old_cust_relationship as oldCustRelationship,")
					.append("new_ird_msisdn as newInsMsisdn,")
					.append("old_ird_msisdn as oldInsMsisdn,")
					.append("new_ip_ins_fname as newIpNomFirstName,")
					.append("old_ip_ins_fname as oldIpNomFirstName,")
					.append("new_ip_ins_sname as newIpNomSurName ,")
					.append("old_ip_ins_sname as oldINomSurName,")
					.append("new_ip_ins_age as newIpNomAge,")
					.append("old_ip_ins_age as oldIpNomAge,")
					.append("new_ip_ins_msisdn as newIpInsMsisdn,")
					.append("old_ip_ins_msisdn as oldIpInsMsisdn,")
					.append(" concat( ud.user_uid ,' - ',ud.fname ,' ' ,ud.sname )  as modifiedBy, ")
					.append(" date_format(ca.modified_date,'%d/%m/%Y %h:%i %p' ) as modifiedDate ")
					.append(" FROM customer_audit ca JOIN   user_details ud on ca.modified_by = ud.user_id where ca.cust_id='")
					.append(custId)
					.append("' order by ca.modified_date desc limit ")
					.append(recordsLimit);
			;

			logger.info("Query: ", sqlQuery.toString());

			customerDetailsList = getSession()
					.createSQLQuery(sqlQuery.toString())
					.addScalar("custId", Hibernate.STRING)
					.addScalar("msisdn", Hibernate.STRING)
					.addScalar("newFname", Hibernate.STRING)
					.addScalar("oldFname", Hibernate.STRING)
					.addScalar("newSname", Hibernate.STRING)
					.addScalar("oldSname", Hibernate.STRING)
					.addScalar("newDob", Hibernate.STRING)
					.addScalar("oldDob", Hibernate.STRING)
					.addScalar("newAge", Hibernate.STRING)
					.addScalar("oldAge", Hibernate.STRING)
					.addScalar("newGender", Hibernate.STRING)
					.addScalar("oldGender", Hibernate.STRING)
					.addScalar("newInsRelFname", Hibernate.STRING)
					.addScalar("oldInsRelFname", Hibernate.STRING)
					.addScalar("newInsRelSurname", Hibernate.STRING)
					.addScalar("oldInsRelSurname", Hibernate.STRING)
					.addScalar("newInsRelIrDoB", Hibernate.STRING)
					.addScalar("oldInsRelIrDoB", Hibernate.STRING)
					.addScalar("newInsRelAge", Hibernate.STRING)
					.addScalar("oldInsRelAge", Hibernate.STRING)
					.addScalar("newCustRelationship", Hibernate.STRING)
					.addScalar("oldCustRelationship", Hibernate.STRING)
					.addScalar("newInsMsisdn", Hibernate.STRING)
					.addScalar("oldInsMsisdn", Hibernate.STRING)
					.addScalar("newIpNomFirstName", Hibernate.STRING)
					.addScalar("oldIpNomFirstName", Hibernate.STRING)
					.addScalar("newIpNomSurName", Hibernate.STRING)
					.addScalar("oldINomSurName", Hibernate.STRING)
					.addScalar("newIpNomAge", Hibernate.STRING)
					.addScalar("oldIpNomAge", Hibernate.STRING)
					.addScalar("newIpInsMsisdn", Hibernate.STRING)
					.addScalar("oldIpInsMsisdn", Hibernate.STRING)
					.addScalar("modifiedDate", Hibernate.STRING)
					.addScalar("modifiedBy", Hibernate.STRING)
					.setResultTransformer(
							Transformers.aliasToBean(ModifyCustomerVO.class))
					.list();

		} catch (DataAccessException e) {
			logger.error(
					"Exception occured while retrieving customer details.", e);
			throw new DBException(e);
		}

		logger.exiting("getSummaryDetailsChange", customerDetailsList);
		return customerDetailsList;

	}



	// Checks customer has grace period or not
	public int checkGracePeriod(String msisdn, int productId)  throws Exception {
		
		logger.entering("checkGracePeriod ", msisdn,productId);

		int resultCode=-1;
						
		try {
			StringBuilder sqlQuery = new StringBuilder("SELECT count(*) as CustCount FROM customer_details cd ")
			.append(" INNER JOIN  customer_subscription cs")
			.append(" ON  cd.cust_id=cs.cust_id  WHERE msisdn ='")
			.append(msisdn)
			.append("' AND product_id=")
			.append(productId)
			.append(" AND cs.is_deactivated = 0 ")
			.append(" AND datediff(curdate(), date(conf_date)) > 365");
			
			logger.info("Query  ",sqlQuery.toString());
		
			 
		       int count=  (int) getSession().createSQLQuery(sqlQuery.toString())
					.addScalar("CustCount", Hibernate.INTEGER).list().get(0);
								
			logger.info("checkGracePeriod ",count);
			
			if(count>0) {
				resultCode=1;
			} else {
				resultCode=0;
			}
			
			
		} catch (DataAccessException exception) {
			logger.error("Exception occured while checking grace period",
					exception);
			throw new DBException(exception);
		}
		logger.info("checkGracePeriod ",resultCode);
		return resultCode;


	}

/*public Date getNoClaimBonusDate(String msisdn)  throws Exception {
		
		logger.entering("getNoClaimBonusDate ", msisdn);
		Date claimDate;

		int resultCode=-1;
						
		try {
			StringBuilder sqlQuery = new StringBuilder("SELECT bonus_claim_date as claimBonusDate FROM  ")
			.append(" coverhistory  ")			
			.append("WHERE msisdn ='")
			.append(msisdn)
			.append("' order by coverHistory_id desc ")
			.append(" limit 1");
						
			logger.info("Query  ",sqlQuery.toString());
		
			 
		        claimDate=  (Date) getSession().createSQLQuery(sqlQuery.toString())
					.addScalar("claimBonusDate", Hibernate.DATE).list().get(0);
								
			logger.info("claimBonusDate ",claimDate);
					
		} catch (DataAccessException exception) {
			logger.error("Exception occured while getting no claim bonus date",
					exception);
			throw new DBException(exception);
		}
		logger.info("getNoClaimBonusDate ",claimDate);
		return claimDate;


	}*/

public Date getNoClaimBonusDate(String msisdn)  throws Exception {
		
		logger.entering("getNoClaimBonusDate ", msisdn);
		Date claimDate=null;
		
		try {
						
			StringBuilder sqlQuery = new StringBuilder("SELECT loyalty_provided_date as claimBonusDate FROM  ")
			.append(" loyalty_customers lc  ")			
			.append(" WHERE lc.msisdn ='")
			.append(msisdn)
			.append("' AND lc.product_id=4 LIMIT 1");	
			logger.info("Query  ",sqlQuery.toString());
			
			List<?> claimDateList= getSession().createSQLQuery(sqlQuery.toString())
					.addScalar("claimBonusDate", Hibernate.DATE).list();
								
			if(claimDateList.size()!=0){
				claimDate=(Date) claimDateList.get(0);
			}
								
			logger.info("claimBonusDate ",claimDate);
					
		} catch (DataAccessException exception) {
			logger.error("Exception occured while getting no claim bonus date",
					exception);
			throw new DBException(exception);
		}
		logger.info("getNoClaimBonusDate ",claimDate);
		return claimDate;


	}

	public void insertIntoCustomerAuditTable(ModifyCustomerVO modifyCustomerVO) throws Exception {
		logger.entering("insertIntoCustomerAuditTable", modifyCustomerVO);

		try {

			StringBuilder sqlQuery = new StringBuilder()
					.append("  INSERT INTO customer_audit ( ")
					.append(" cust_id,msisdn, new_fname,old_fname,new_sname,old_sname,new_age,old_age, ")
					.append(" new_dob,old_dob,new_gender,old_gender,new_ird_fname,old_ird_fname,new_ird_sname, ")
					.append(" old_ird_sname,new_ird_age,old_ird_age,new_ird_dob,old_ird_dob,new_ird_msisdn,old_ird_msisdn, ")
					.append(" new_cust_relationship,old_cust_relationship, ")
                    .append(" new_ip_ins_fname,old_ip_ins_fname,new_ip_ins_sname,old_ip_ins_sname,new_ip_ins_age,old_ip_ins_age,")
                    .append(" new_ip_ins_msisdn,old_ip_ins_msisdn,")
					.append(" modified_by,modified_date ) ")
					.append(" VALUES  ( ")
						
					.append(" :custId,:msisdn,:newFname,:oldFname,:newSname,:oldSname,:newAge,:oldAge, ")
					.append(" :newDob,:oldDob, :newGender, :oldGender, :newIrdFname, :oldIrdFname,:newIrdSname,")
					.append(" :oldIrdSname,:newIrdAge,:oldIrdAge, :newIrdDob, :oldIrdDob, :newIrdMsisdn,:oldIrdMsisdn,")
					.append(" :newCustRelationship,:oldCustRelationship,  ")
					.append(" :newIpInsFname,:oldIpInsFname,:newIpInsSname,:oldIpInsSname,:newIpInsAge,:oldIpInsAge,")
                    .append(" :newIpInsMsisdn,:oldIpInsMsisdn,")
					
					
					.append(" :modifiedBy, :modifiedDate )")
					;
					
			java.util.Date date = new Date();
			Object currentDate = new java.sql.Timestamp(date.getTime());
			
			logger.info("Query ", sqlQuery.toString());
			Query queryAddInCustAudit = super.getSession()
					.createSQLQuery(sqlQuery.toString())
					
					.setParameter("custId", modifyCustomerVO.getCustId())
					.setParameter("msisdn", modifyCustomerVO.getMsisdn())
					
					.setParameter("newFname", modifyCustomerVO.getNewFname())
					.setParameter("oldFname", modifyCustomerVO.getOldFname())
					
					.setParameter("newSname", modifyCustomerVO.getNewSname())
					.setParameter("oldSname", modifyCustomerVO.getOldSname())
					
					.setParameter("newAge", modifyCustomerVO.getNewAge())
					.setParameter("oldAge", modifyCustomerVO.getOldAge())
					
					.setParameter("newDob", modifyCustomerVO.getNewDob())
					.setParameter("oldDob", modifyCustomerVO.getOldDob())
					
									
					.setParameter("newGender", modifyCustomerVO.getNewGender())
					.setParameter("oldGender", modifyCustomerVO.getOldGender())
					
									
					.setParameter("newIrdFname",modifyCustomerVO.getNewInsRelFname())
					.setParameter("oldIrdFname",modifyCustomerVO.getOldInsRelFname())
					
					.setParameter("newIrdSname",modifyCustomerVO.getNewInsRelSurname())
					.setParameter("oldIrdSname",modifyCustomerVO.getOldInsRelSurname())
					
					.setParameter("newIrdAge",	modifyCustomerVO.getNewInsRelAge())
					.setParameter("oldIrdAge",  modifyCustomerVO.getOldInsRelAge())
					
					.setParameter("newIrdDob",	modifyCustomerVO.getNewInsRelIrDoB())
					.setParameter("oldIrdDob",  modifyCustomerVO.getOldInsRelIrDoB())
					
										
					.setParameter("newIrdMsisdn",	modifyCustomerVO.getNewInsMsisdn())
					.setParameter("oldIrdMsisdn",  modifyCustomerVO.getOldInsMsisdn())
										
					
					.setParameter("newCustRelationship",modifyCustomerVO.getNewCustRelationship())
					.setParameter("oldCustRelationship",modifyCustomerVO.getOldCustRelationship())
										
		
									
					
                    .setParameter("newIpInsFname",modifyCustomerVO.getNewIpNomFirstName())
					.setParameter("oldIpInsFname",modifyCustomerVO.getOldIpNomFirstName())
					
					.setParameter("newIpInsSname",modifyCustomerVO.getNewIpNomSurName())
					.setParameter("oldIpInsSname",modifyCustomerVO.getOldINomSurName())
					
					.setParameter("newIpInsAge",modifyCustomerVO.getNewIpNomAge())
					.setParameter("oldIpInsAge",modifyCustomerVO.getOldIpNomAge())
					
					.setParameter("newIpInsMsisdn",modifyCustomerVO.getNewIpInsMsisdn())
					.setParameter("oldIpInsMsisdn",modifyCustomerVO.getOldIpInsMsisdn())
                                                        
					
					
					.setParameter("modifiedBy", modifyCustomerVO.getModifiedBy())
					.setParameter("modifiedDate", currentDate);
		
			queryAddInCustAudit.executeUpdate();
			

		} catch (Exception e) {
			logger.error(
					"Exception occured while retrieving customer details.", e);
			throw new Exception();
		}

		logger.exiting("insertIntoCustomerAuditTable");

	}

	@SuppressWarnings("unchecked")
	public String getProductDeactivedDetails(String custId) throws Exception {
		logger.entering("getProductDeactivedDetails", custId);

		List<CustomerVO> customerVOList = null;
		CustomerVO customerVO = null;
		String productList=null;
		try {
			StringBuilder sqlQuery = new StringBuilder().
					append("SELECT group_concat(product_id) as productIds from customer_subscription ")
					.append(" where is_deactivated=1 and  cust_id=")
					.append(custId);
					

			customerVOList = getSession()
					.createSQLQuery(sqlQuery.toString())
					.addScalar("productIds", Hibernate.STRING)
					.setResultTransformer(
							Transformers.aliasToBean(CustomerVO.class)).list();

			if(!customerVOList.isEmpty())
			{
				
				customerVO = customerVOList.get(0);
				productList=customerVO.getProductIds();
			}
			
		} catch (Exception exception) {
			logger.error("Exception occured while geting cover history",
					exception);
			throw new Exception(exception);
		}
		logger.exiting("getProductDeactivedDetails", productList);
		return productList;
		
	}

	@SuppressWarnings("unchecked")
	public CustomerVO checkCustomerExistInBimaProductCancellation(String msisdn) throws DBException {
		logger.entering("checkCustomerExistInBimaProductCancellation", msisdn);

		CustomerVO customerVO = new CustomerVO();
		List<CustomerVO> custDetails =new ArrayList<CustomerVO>();
		try {
			StringBuilder sqlQuery = new StringBuilder()
			        .append(" SELECT ")
					.append("  bc.cust_id  AS custId, ")
					.append(" fname AS fname, ")
					.append(" sname AS sname, ")
					.append(" age AS age, ")
					.append(" gender AS gender, ")
					.append(" DATE_FORMAT(bc.dob,'%d/%m/%Y') AS dob, ")
					.append(" msisdn AS msisdn, ")
					.append(" irfname AS insRelFname , ")
					.append(" irsname AS insRelSurname, ")
					.append(" irage AS insRelAge , ")
					.append(" IFNULL(DATE_FORMAT(irdob,'%d/%m/%Y'),'') AS insRelIrDoB, ")
					.append(" IFNULL(cust_relationship,0) AS insRelation, ")
					.append(" IFNULL(kinfname,'') AS ipNomFirstName, ")
					.append(" IFNULL(kinsname,'') AS ipNomSurName, ")
					.append(" IFNULL(kinage,'')  AS ipNomAge ")
					.append(" FROM bima_cancellations bc ")
					.append(" JOIN product_cancellations pc ON bc.bc_id = pc.bc_id ")
					.append(" WHERE msisdn='" + msisdn + "'")
					.append(" order by bc.bc_id desc ");

			logger.info("Query: ", sqlQuery.toString());
			custDetails = getSession()
					.createSQLQuery(sqlQuery.toString())
					.addScalar("custId", Hibernate.STRING)
					.addScalar("fname", Hibernate.STRING)
					.addScalar("sname", Hibernate.STRING)
					.addScalar("age", Hibernate.STRING)
					.addScalar("gender", Hibernate.STRING)
					.addScalar("dob", Hibernate.STRING)
					.addScalar("msisdn", Hibernate.STRING)
					
									
					.addScalar("insRelFname", Hibernate.STRING)
					.addScalar("insRelSurname", Hibernate.STRING)
					.addScalar("insRelAge", Hibernate.STRING)
					.addScalar("insRelIrDoB", Hibernate.STRING)
					.addScalar("insRelation", Hibernate.STRING)
					
					
					.addScalar("ipNomFirstName", Hibernate.STRING)
					.addScalar("ipNomSurName", Hibernate.STRING)
					.addScalar("ipNomAge", Hibernate.STRING)
					
					
					
					.setResultTransformer(
							Transformers.aliasToBean(CustomerVO.class))
					.list();
			
			if(custDetails.isEmpty())
				customerVO.setCustId("0");
			else
				customerVO=custDetails.get(0);
		}catch (DataAccessException e) {
			logger.error(
					"Exception occured while retrieving subscribed customer details.",
					e);
			throw new DBException(e);
		}

		logger.exiting("checkCustomerExistInBimaProductCancellation", customerVO);
		return customerVO;
	}

	@SuppressWarnings("unchecked")
	public String checkInProductCancellation(String custId,String productReg) throws DBException {
		logger.entering("checkInProcheckInProductCancellationductCancellation", custId);

		
		List<CustomerVO> custDetails =new ArrayList<CustomerVO>();
		String prodcutList=null;
		try {
			StringBuilder sqlQuery = new StringBuilder()
			        .append(" SELECT  group_concat(distinct(product_id) order by product_id) as custProductCancellation ")
			        .append(" from product_cancellations where cust_id=")
					.append( custId)
					.append(" AND product_id not in (")
					.append(productReg)
					.append(")")
					;

			logger.info("Query: ", sqlQuery.toString());
			custDetails = getSession()
					.createSQLQuery(sqlQuery.toString())
					.addScalar("custProductCancellation", Hibernate.STRING)
					.setResultTransformer(
							Transformers.aliasToBean(CustomerVO.class))
					.list();
			
			if(custDetails.isEmpty())
				prodcutList=null;
			else
				prodcutList=custDetails.get(0).getCustProductCancellation();
		}catch (DataAccessException e) {
			logger.error(
					"Exception occured while retrieving subscribed customer details.",
					e);
			throw new DBException(e);
		}

		logger.exiting("checkInProductCancellation", prodcutList);
		return prodcutList;
	}
	
	@SuppressWarnings("unchecked")
	public void sendSMStoNominee(CustomerVO custVO,String productId) throws Exception
 {
		logger.entering("sendSMStoNominee", custVO,productId);
		StringBuilder sqlQuery = new StringBuilder();
	
		try {
			sqlQuery = new StringBuilder()
			.append("CALL SEND_SMS_TO_BENEFICIARY( :custMsisdn,:productId) ");
			Query query=null;
			
			
			logger.info("custMsisdn : ", custVO.getMsisdn());
			logger.info("productId :", productId);
			query = super
						.getSession()
						.createSQLQuery(sqlQuery.toString())
						.setParameter("custMsisdn", custVO.getMsisdn())
						.setParameter("productId", productId);
				
			
		    logger.info("Query ", query.toString());
				
			query.executeUpdate();
					
					
	

		} catch (Exception e) {
			logger.error("Exception occurred while sending sms ", e);
			throw new Exception();
		}

		logger.exiting("sendSMStoNominee");
	}

	
	@SuppressWarnings("unchecked")
	public List<LoyaltyCustomerVO> getLoyalCustomers(CustomerVO custVO) throws DBException {
		
		logger.entering("getLoyalCustomers ",custVO);

		List<LoyaltyCustomerVO> listCustomerVO=null;
		try {
				StringBuilder sqlQuery = new StringBuilder("SELECT ")
				.append("  concat(cd.fname,' ' ,cd.sname) As customerName, ")
				.append(" lc.msisdn AS msisdn ,IF(lc.is_loyalty_provided=1,'NO','YES') AS  isLoyaltyEligible, ")
				.append(" if(cs.is_deactivated=0 ,'CONFIRMED','DEACTIVATED') AS  isConfirmed, ")
				//.append(" cs.conf_date AS confirmedDate ,cs.is_confirmed AS isConfirmed, loyalty_provided_date AS loyaltyProvidedDate, ")
				.append(" cs.conf_date AS confirmedDate , IFNULL(loyalty_provided_date,'N/A') AS loyaltyProvidedDate, ")
				.append(" lc.product_id  as productId, ")
				.append(" lc.is_data  as isDataPackage, ")
				//.append(" ,GROUP_CONCAT(lc.product_id ORDER BY lc.product_id) AS purchasedProducts ")
				.append(" pd.product_name as productName ")
				.append(" FROM customer_details cd   INNER JOIN loyalty_customers lc   ")
				.append(" ON  cd.cust_id=lc.cust_id")
				.append(" INNER JOIN  customer_subscription cs   ")
				.append(" ON lc.sn_id=cs.sn_id ")
				.append(" INNER JOIN product_details pd ")
				.append(" ON lc.product_id =pd.product_id ")
				.append(" WHERE lc.msisdn= '" )
				.append(custVO.getMsisdn())
				.append("' AND lc.product_id in (2,3) ")
				 .append(" AND lc.is_loyalty_provided <> 2 ")// AND lc.is_loyalty_provided=0 ")
				.append("  AND lc.is_active=1 ")
				//.append(" AND TIMESTAMPDIFF(YEAR,DATE(cs.conf_date),curDate())=1 AND month(cs.conf_date)=month(curDate()) ");
				.append("  AND cs.is_deactivated=0 ");
			
		
				listCustomerVO = getSession()
						.createSQLQuery(sqlQuery.toString())
						.addScalar("customerName", Hibernate.STRING)
						.addScalar("msisdn", Hibernate.STRING)					
						.addScalar("isLoyaltyEligible", Hibernate.STRING)
						.addScalar("confirmedDate", Hibernate.STRING)
						.addScalar("isConfirmed", Hibernate.STRING)
						.addScalar("loyaltyProvidedDate", Hibernate.STRING)
						.addScalar("productId", Hibernate.INTEGER)
						.addScalar("isDataPackage", Hibernate.INTEGER)
						.addScalar("productName", Hibernate.STRING)
						.setResultTransformer(
								Transformers.aliasToBean(LoyaltyCustomerVO.class)).list();
				
				logger.debug("loyal customers :: " ,listCustomerVO);

		} catch (DataAccessException exception) {
			logger.error("Exception occured while getting Loyalty Customer Details",
					exception);
			throw new DBException(exception);
		}

		logger.exiting("getLoyalCustomers", listCustomerVO);
		return listCustomerVO;
	}

	@SuppressWarnings("unchecked")
	public LoyaltyCustomerVO getLoyaltyInformation(CustomerVO custVO) throws DBException {
		
		logger.entering("getLoyaltyInformation ",custVO);

		List<LoyaltyCustomerVO> loyaltyInfoList=null;
		
		LoyaltyCustomerVO loyaltyInfo=null;
		
		try {
				StringBuilder sqlQuery = new StringBuilder("SELECT ")
				.append("  concat(cd.fname,' ' ,cd.sname) As customerName, ")
				.append(" lc.msisdn AS msisdn ,lc.is_loyalty_provided AS  isLoyaltyEligible, ")
				.append(" cs.conf_date AS confirmedDate ,cs.is_confirmed AS isConfirmed, IFNULL(loyalty_provided_date,'N/A')  AS loyaltyProvidedDate, ")
				.append(" lc.product_id  as productId, ")
				.append(" lc.is_data  as isDataPackage, ")
				//.append(" ,GROUP_CONCAT(lc.product_id ORDER BY lc.product_id) AS purchasedProducts ")
				.append(" pd.product_name as productName ,")
				.append(" lc.sn_id as snId , ")
				.append(" lc.cust_id as custId, ")
				.append(" lc.lc_id as lcId ")
				.append(" FROM customer_details cd   INNER JOIN loyalty_customers lc   ")
				.append(" ON  cd.cust_id=lc.cust_id")
				.append(" INNER JOIN  customer_subscription cs   ")
				.append(" ON lc.sn_id=cs.sn_id ")
				.append(" INNER JOIN product_details pd ")
				.append(" ON lc.product_id =pd.product_id ")
				.append(" WHERE lc.msisdn= '" )
				.append(custVO.getMsisdn())
				.append(" ' AND lc.product_id in ( ")
				.append(custVO.getProductId())
				.append(" ) AND lc.is_loyalty_provided=0 ")
				.append("  AND lc.is_active=1 ")
				.append("  AND cs.is_deactivated=0 ");
			
		
				loyaltyInfoList = (List<LoyaltyCustomerVO>) getSession()
						.createSQLQuery(sqlQuery.toString())
						.addScalar("customerName", Hibernate.STRING)
						.addScalar("msisdn", Hibernate.STRING)					
						.addScalar("isLoyaltyEligible", Hibernate.STRING)
						.addScalar("confirmedDate", Hibernate.STRING)
						.addScalar("isConfirmed", Hibernate.STRING)
						.addScalar("loyaltyProvidedDate", Hibernate.STRING)
						.addScalar("productId", Hibernate.INTEGER)
						.addScalar("isDataPackage", Hibernate.INTEGER)
						.addScalar("productName", Hibernate.STRING)
						.addScalar("snId", Hibernate.INTEGER)
						.addScalar("custId", Hibernate.INTEGER)
						.addScalar("lcId", Hibernate.INTEGER)
						.setResultTransformer(
								Transformers.aliasToBean(LoyaltyCustomerVO.class)).list();
				
				logger.debug("getLoyaltyInformation " , loyaltyInfoList);
				
				if(null != loyaltyInfoList && loyaltyInfoList.size()!=0){
					loyaltyInfo=loyaltyInfoList.get(0);
				}
				
				logger.debug("loyal Info  :: " ,loyaltyInfo);

		} catch (DataAccessException exception) {
			logger.error("Exception occured while getting loyalty info",
					exception);
			throw new DBException(exception);
		}

		logger.exiting("getLoyaltyInformation", loyaltyInfo);
		
		return loyaltyInfo;
	}

	@SuppressWarnings("unchecked")
	public boolean updatePackage(CustomerVO custVO) throws DBException {
		
		logger.entering("updatePackage ",custVO);

		int resultCode=-1;
		
		boolean result=false;
		
		try {
			
		StringBuilder updatedQuery=new StringBuilder();
			updatedQuery.append(" UPDATE loyalty_customers SET is_data =:isData where lc_id = :lcId ");
			
			logger.debug("updatedPackage :: ", updatedQuery);
		     
			 resultCode = (Integer) getSession()
				     .createSQLQuery(updatedQuery.toString())
				      .setParameter("isData", custVO.getIsData())
				      .setParameter("lcId", custVO.getLcId())
				     .executeUpdate();
			 
			 if(resultCode==1) {
				 
				 result=true;
				 
			 } else {
				 
				 result=false;
			 }
			 
		
				
				logger.info(" loyalty package updated ", result);
			
			
		} catch (DataAccessException exception) {
			logger.error("Exception occured while updating loyalty info",
					exception);
			throw new DBException(exception);
		}

		logger.exiting("updatePackage", result);
		
		return result;
	}
	/**
	 * @param custVO
	 * @param userId
	 * @param statusCode
	 * @param responseCode
	 * @return
	 * @throws DBException
	 */
	public boolean updateLoyaltyInfo(CustomerVO custVO, int userId, String statusCode, int responseCode) throws DBException {
		
		logger.entering("updateLoyaltyInfo ",custVO,userId,statusCode, responseCode);

		int resultCode=-1;
		
		boolean result=false;
		
		try {
			
			StringBuilder updatedQuery=new StringBuilder();
			updatedQuery.append(" CALL UPDATE_LOYALTY_PROVIDED(:lcId,:isData,:productID,:statusCode,:responseCode,:userId)");
			
			logger.debug("updatedQuery :: ", updatedQuery);
		     
			 resultCode = (Integer) getSession()
				     .createSQLQuery(updatedQuery.toString())
				     .setParameter("lcId", custVO.getLcId())
				     .setParameter("isData", custVO.getIsData())
				     .setParameter("productID", custVO.getProductId())
				     .setParameter("statusCode", statusCode)
				     .setParameter("responseCode", responseCode)
				     .setParameter("userId", userId)
				     .uniqueResult();
			 
			 if(resultCode==0) {
				 
				 result=true;
				 
			 } else {
				 
				 result=false;
			 }
			
			
		} catch (DataAccessException exception) {
			logger.error("Exception occured while updating loyalty info",
					exception);
			throw new DBException(exception);
		}

		logger.exiting("updateLoyaltyInfo", result);
		
		return result;
	}

}
