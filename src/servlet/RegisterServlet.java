package servlet;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;

import bean.ServerResponse;
import bean.User;
import service.RegisterService;
import util.Constant;

@SuppressWarnings("serial")
public class RegisterServlet extends HttpServlet {
	private Gson gson = new Gson();
	private RegisterService registerService = new RegisterService();
	private ServerResponse serverResponse = new ServerResponse();

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		// TODO Auto-generated method stub
		super.doGet(req, resp);
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		
		req.setCharacterEncoding("UTF-8");
		resp.setContentType("application/json;charset=utf-8");//指定返回的格式为JSON格式
        req.setCharacterEncoding("UTF-8");//setContentType与setCharacterEncoding的顺序不能调换，否则还是无法解决中文乱码的问题

		BufferedReader reader = req.getReader();
		String json = reader.readLine();
		User user = gson.fromJson(json, User.class);
		System.out.println("Register user is "+user.toString());
		String note = user.getNote();
		switch (note) {
		case "check":
			System.out.println("RegisterServlet note:Check User");
			int result_check = registerService.check(user);
			System.out.println("RegisterServlet note:Check result is : "+result_check);
			serverResponse.setResponseCode(result_check);
			if (result_check == Constant.Failed) {
				serverResponse.setResponseInfo("用户校验失败，请重试");
			} else if (result_check == Constant.UserNotFound) {
				serverResponse.setResponseInfo("用户校验通过");
			} else if (result_check == Constant.UserHaveExit) {
				serverResponse.setResponseInfo("该用户名已被注册");
			}
			break;
		case "register":
			System.out.println("RegisterServlet note:register");
			int result_register = registerService.register(user);
			System.out.println("RegisterServlet note:register result is : "+result_register);
			serverResponse.setResponseCode(result_register);
			if (result_register == Constant.Failed) {
				serverResponse.setResponseInfo("注册失败，请重试");
			} else if (result_register == Constant.Success) {
				serverResponse.setResponseInfo("注册成功");
			}
			break;
		}
		PrintWriter out = resp.getWriter();
		out.write(gson.toJson(serverResponse));
		System.out.println("RegisterServlet return gson is : "+gson.toJson(serverResponse));
		out.close();
	}

	@Override
	public void destroy() {
		// TODO Auto-generated method stub
		super.destroy();
	}

	@Override
	public void init() throws ServletException {
		// TODO Auto-generated method stub
		super.init();
		System.out.println("RegisterServlet init");
	}
	
	

}
