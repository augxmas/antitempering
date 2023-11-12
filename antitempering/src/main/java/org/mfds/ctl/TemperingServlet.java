package org.mfds.ctl;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;

import javax.naming.NamingException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.jdom2.JDOMException;
import org.jepetto.bean.Facade;
import org.jepetto.logger.DisneyLogger;
import org.jepetto.proxy.HomeProxy;
import org.jepetto.util.PropertyReader;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;



/**
 * Servlet implementation class TemperingServlet
 */
@WebServlet(name = "doit", urlPatterns = { "/doit" })
public class TemperingServlet extends HttpServlet {
	
	private static final long serialVersionUID = 1L;
	static PropertyReader reader = PropertyReader.getInstance();
	DisneyLogger cat = new DisneyLogger(TemperingServlet.class.getName());
	public static final String QUERY_FILE		= reader.getProperty("antitempering_query_file");
	public static final String CONTENTTYPE 		= "application/json;charset=utf-8";
	public static final String dataSource		= reader.getProperty("antitempering_mysql_datasource");
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public TemperingServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doIt(request,response);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doIt(request,response);
	}
	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doIt(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
		req.setCharacterEncoding("UTF-8");
		JSONParser parser = new JSONParser();
		JSONObject json = null;
		JSONObject reqData = null;
		JSONObject resData = null;
		BufferedReader reader = null;
		Map<String,String> map = new java.util.LinkedHashMap<String,String>();
		Map<String,String> dummy = new Hashtable<String,String>();
		
		String cmd = null;
		PrintWriter out = null;
		try {
			
			out = res.getWriter();
			
			reader = req.getReader();
			String _temp = "";
			StringBuffer buffer = new StringBuffer();
			while(_temp != null) {
				buffer.append(_temp);
				_temp = reader.readLine();
			}
			
			json = (JSONObject)parser.parse(buffer.toString());
			cmd = (String)json.get("cmd");
			reqData = (JSONObject)json.get("reqData");
			
			// TODO Auto-generated method stub
			HomeProxy proxy = HomeProxy.getInstance();
			Facade remote = proxy.getFacade();
			
			res.setContentType(CONTENTTYPE);
			JSONArray jArr = null;
			String args[] = new String[]{};
			
		
			if(cmd.equalsIgnoreCase("listbyvendor")) {
				dummy.put("vendor", reqData.get("vendor") != null ? reqData.get("vendor").toString(): "");
				dummy.put("code", reqData.get("code") != null ? reqData.get("code").toString(): "");
				jArr = remote.executeQueryJ(dataSource, QUERY_FILE, "listbyvendor", dummy, args);
				out.print(jArr.toJSONString());
				
			}
			else if(cmd.equalsIgnoreCase("approve")) {
				String code				= reqData.get("code").toString();
				String item				= reqData.get("item").toString();
				String vendor			= reqData.get("vendor").toString();
				String permitteddate	= reqData.get("permitteddate").toString();
				String updateddate		= reqData.get("updateddate").toString();
				String type				= reqData.get("type").toString();
				String manufacture		= reqData.get("manufacture").toString();
				String canceltype		= reqData.get("canceltype").toString();
				String canceleddate		= reqData.get("canceleddate").toString();
				String ingredient		= reqData.get("ingredient").toString();
				String regdate			= reqData.get("regdate").toString();
				String registor			= reqData.get("registor").toString();
				String _args1[] = new String[] {
					code,item,vendor,permitteddate,updateddate,type,manufacture,canceltype,canceleddate,ingredient,regdate,registor
				};
				String _args2[] = new String[] {
					code,			// 의약품 코드
					"APPLICATION",	// 인증 요청 종류
					"application",	// 인증 요청 내용
					"0",			// 참조값, 최초 등록
					regdate	,		// 등록일
					registor,		// 등록자
					"REQ"			// 구분(요청자, 승인자)
				};
				
				resData = new JSONObject();
				int cnt = remote.executeUpdateX(
							dataSource, 
							new String[] {QUERY_FILE,QUERY_FILE}, 
							new String[] {"approve","c_history"}, 
							new String[][]{_args1, _args2}
						  );
				
				map.put("code", "200");
				map.put("count", String.valueOf(cnt));
				resData.putAll(map);
				
			}else if(cmd.equalsIgnoreCase("history")) {
				resData = new JSONObject();
				String code		= reqData.get("code").toString();
				String type		= reqData.get("type").toString();
				String content	= reqData.get("content").toString();
				String ref		= reqData.get("ref").toString();
				String regdate	= reqData.get("regdate").toString();
				String registor	= reqData.get("registor").toString();
				String seg		= reqData.get("seg").toString();
				
				args 			= new String[] {
									code,		// 의약품 코드
									type,		// 인증 요청 종류
									content,	// 인증 요청 내용
									ref,		// 참조값
									regdate	,	// 등록일
									registor,	// 등록자
									seg			// 구분자(요청자, 승인자)
								};
				int cnt = remote.executeUpdate(dataSource, QUERY_FILE, "c_history", args);
				map.put("code", "200");
				map.put("count", String.valueOf(cnt));
				resData.putAll(map);
			}
			
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			map.put("code", "500");
			map.put("message", e.getLocalizedMessage());
			resData.putAll(map);
			e.printStackTrace();
		} catch (NamingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JDOMException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally {
			try {
				out.println(resData.toJSONString());
			}catch(Exception e) {
				
			}
		}
		
		
	}

}
