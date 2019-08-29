package servlet;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;

import bean.ServerResponse;
import bean.User;
import dao.DaoImpl;
import service.LoginService;
import util.Constant;

@SuppressWarnings("serial")
public class LoginServlet extends HttpServlet {
	private Gson gson = new Gson();
	private LoginService loginService = new LoginService();
	private DaoImpl daoImpl = new DaoImpl();
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
		System.out.println("Login:user is "+user.toString());
		String note = user.getNote();
		switch (note) {
		case "check":
			int result_check = loginService.check(user);
			serverResponse.setResponseCode(result_check);
			if (result_check == Constant.Failed) {
				serverResponse.setResponseInfo("校验账户失败，请重试");
			} else if (result_check == Constant.UserNotFound) {
				serverResponse.setResponseInfo("用户不存在");
			} else if (result_check == Constant.UserHaveExit) {
				serverResponse.setResponseInfo("校验通过");
			}
			break;
		case "login":
			int result_login = loginService.login(user);
			serverResponse.setResponseCode(result_login);
			if (result_login == Constant.Failed) {
				serverResponse.setResponseInfo("登录失败，请重试");
			} else if (result_login == Constant.PasswordWrong) {
				serverResponse.setResponseInfo("账号/密码错误");
			} else if (result_login == Constant.Success) {
				serverResponse.setResponseInfo("登录成功");
				User currentUser;
				try {
					currentUser = daoImpl.getUser(user.getAccount());
					serverResponse.setUser(currentUser);
				} catch (ClassNotFoundException | SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			break;
		}
		PrintWriter out = resp.getWriter();
		out.write(gson.toJson(serverResponse));
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
		System.out.println("LoginServlet init");
	}

}
