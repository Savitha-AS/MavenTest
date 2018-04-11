package com.mip.framework.handlers;

import java.io.IOException;
import java.util.StringTokenizer;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.mip.framework.logger.LoggerFactory;
import com.mip.framework.logger.MISPLogger;

/**
 * A request handler for MISP application.All requests from platform will be
 * served by this class which is the gateway to access any service.
 * 
 * <p>
 * All *.task requests are served by this implementation class.
 * Example: &lt;moduleName&gt;.&lt;requestType&gt;.&lt;taskName&gt;.task,
 * based on &lt;requestType&gt; <code>MISPRequestHandler</code> invokes the
 * mapped controller or a view.
 * 
 * @author T H B S
 * @version 1.0, 17/05/2011
 * 
 */
public class PlatformRequestHandler extends HttpServlet
{

	/**
	 * An instance of logger.
	 */
	private static MISPLogger logger = LoggerFactory.getInstance().getLogger(
											PlatformRequestHandler.class);
	
   /**
	 * Generated SerialVersion ID.
	 */
	private static final long serialVersionUID = -7800786580449021611L;


    /**
     * The doGet method of the servlet. <br>
     * 
     * This method is called when a form has its tag value method equals to get.
     * 
     * @param request
     *            the request send by the client to the server
     * @param response
     *            the response send by the server to the client
     * @throws ServletException
     *             if an error occurred
     * @throws IOException
     *             if an error occurred
     */
    public void doGet(HttpServletRequest request, HttpServletResponse response)
        throws ServletException, IOException
    {

        this.serveRequest(request, response);

    }

    /**
     * The doPost method of the servlet. <br>
     * 
     * This method is called when a form has its tag value method equals to
     * post.
     * 
     * @param request
     *            the request send by the client to the server
     * @param response
     *            the response send by the server to the client
     * @throws ServletException
     *             if an error occurred
     * @throws IOException
     *             if an error occurred
     */
    public void doPost(HttpServletRequest request, HttpServletResponse response)
        throws ServletException, IOException
    {

        this.serveRequest(request, response);
    }

    /**
     * The request handler's service method. The request URI is parsed and
     * dispatched to appropriate service.
     * <p>
     * This handler looks for the &lt;requestType&gt; in the URI and servers the
     * appropriate service.
     * 
     * <p>
     * In case of any exception from the service layer, controller automatically
     * dispatches the request to error page.
     * 
     * @param request
     *            the request send by the client to the server
     * @param response
     *            the response send by the server to the client
     * @throws ServletException
     *             if an error occurred
     * @throws IOException
     *             if an error occurred
     */

    private void serveRequest(HttpServletRequest request,
    		HttpServletResponse response) throws ServletException,IOException
    {
    	String requestEvent = request.getServletPath();
    	
		int slash = requestEvent.lastIndexOf('/');
		if (slash >= 0)
			requestEvent = requestEvent.substring(slash + 1);
		
		String moduleName = null;
        String requestType = null;
        String taskName = null;
        String requestResolver = null;
        
        // Parse the URI and fetch the tokens
        StringTokenizer requestToken = new StringTokenizer(requestEvent, ".");
        moduleName = requestToken.nextToken();
        requestType = requestToken.nextToken();
        taskName = requestToken.nextToken();
        
        if(requestType.equalsIgnoreCase("controller"))
        	requestResolver = new StringBuilder("/") 
	        					.append(moduleName)
	        					.append("/")
	        					.append(taskName)
	        					.append(".htm").toString();			  
        else
        	requestResolver = new StringBuilder("/")
        						.append("pageRequest")
        						.append("/")
								.append("handlePageRequest.htm")
								.append("?mn=")
								.append(moduleName)
								.append("&tn=")
								.append(taskName).toString();
       
       if (response.isCommitted()) 
       {
    	   logger.error("Response already commited."
    			   				+ "Cannot serve the request.");
    	   return;
       }
       
       logger.debug("Request handler serving the request...", requestResolver);
       request.getRequestDispatcher(requestResolver).forward(request, response);
    }
}