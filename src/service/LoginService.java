package service;

import java.sql.SQLException;

import bean.User;
import dao.DaoImpl;
import util.Constant;

public class LoginService {
	
	private DaoImpl daoImpl = new DaoImpl();
	
	public int check(User user) {
		String account = user.getAccount();
		try {
			boolean UserisExit = daoImpl.findUser(account);
			if(UserisExit) {
				return Constant.UserHaveExit;
			}else {
				return Constant.UserNotFound;
			}
			
		} catch (ClassNotFoundException | SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return Constant.Failed;
		}
	}
	
	public int login(User user) {
		String account = user.getAccount();
		String password = user.getPassword();
		try {
			User getUser = daoImpl.getUser(account);
			if(getUser.getPassword().contentEquals(password)) {
				return Constant.Success;
			} else {
				return Constant.PasswordWrong;
			}
		} catch (ClassNotFoundException | SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return Constant.Failed;
		}
	}
}
