package service;

import java.io.UnsupportedEncodingException;
import java.sql.SQLException;

import bean.User;
import dao.DaoImpl;
import util.Constant;

public class RegisterService {

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
	
	public int register(User user) {
		try {
			return daoImpl.registerUser(user);
		} catch (ClassNotFoundException | UnsupportedEncodingException | SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return Constant.Failed;
		}
	}
	
}
