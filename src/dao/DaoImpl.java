package dao;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import bean.User;
import util.Constant;

public class DaoImpl {
	
	Dao dao = new Dao();
	
	//1.查询用户是否存在，输入account，返回boolean型变量true or false
	//注：该方法共在两个地方会得到使用
	//-注册时：调用该方法判断该用户是否已经存在，存在即已经被注册过无法重复注册
	//-登录时：调用该方法判断该用户是否已经存在，存在即可以进一步判断密码是否输入正确进而判断能否登录成功
    public boolean findUser(String account) throws ClassNotFoundException, SQLException {
        String str = "select * from user where account = '" + account + "'";
        Connection con = dao.getCon();
        Statement statement = con.createStatement();
        ResultSet rs = statement.executeQuery(str);
        while (rs.next()) {
            dao.closeAll(rs, statement, con);
            return true;
        }
        dao.closeAll(rs, statement, con);
        return false;
    }
    
    //2.如果用户没有被人注册过，那么就注册该用户。输入user对象，返回数据库操作之后的结果0 or !0
    public int registerUser(User user) throws ClassNotFoundException, SQLException, UnsupportedEncodingException {
        // TODO Auto-generated method stub
    	//这里需要对用户上传头像的问题做些说明，用户上传头像，会以字节流的形式穿到后台服务器，然后会以文件形式保存在后台，
    	//此时，更为合理的逻辑是把图像所保存的url保存为icon的value，而并非仍旧以字节流保存，然后返回给用户，那样的话，
    	//会占用大量的网络资源以及时间，影响用户体验。
    	//所以因为此处的转换，那么数据库中的icon还是varchar类型，但是对于数据实体类，则会有两个相关的属性值：byte[] icon和String iconUrl
    	//这两个值将用在不同的地方。前者为客户端向后台发送注册请求时以及修改头像时使用的，后者为后台返回给客户端时的icon的地址以及数据库中保存的值。
        try {
            createFileWithByte(user.getIcon(), user.getAccount());
            Connection conn = dao.getCon();
            String sql = "insert into user(iconurl,account,password) values ( ?, ?, ? )";
            PreparedStatement ptmt = conn.prepareStatement(sql);
            ptmt.setString(1, Constant.ipAddress+user.getAccount()+".png");
            ptmt.setString(2, user.getAccount());
            ptmt.setString(3, user.getPassword());
            ptmt.execute();
            return Constant.Success;
        } catch (Exception e) {
            e.printStackTrace();
            return Constant.Failed;
        }
    }

    //登录时，当该用户已经存在的前提下，则需要根据账户获取对应的用户数据，以用来校验密码是否正确，从而校验用户登录
    //同时，当用户登录成功之后还应该把该账户的信息返回给用户。
    public User getUser(String account) throws ClassNotFoundException, SQLException {
        User user = new User();
        String str = "select * from user where account = '" + account + "'";
        Connection con = dao.getCon();
        Statement statement = con.createStatement();
        ResultSet rs = statement.executeQuery(str);
        while (rs.next()) {
            try {
                user.setIconUrl(rs.getString("iconurl"));
                user.setAccount(rs.getString("account"));
                user.setPassword(rs.getString("password"));
            } catch (Exception e) {
                e.printStackTrace();
            }
            dao.closeAll(rs, statement, con);
            return user;
        }
        dao.closeAll(rs, statement, con);
        return null;
    }

    //更改密码
    public int updatepwd(User user) throws ClassNotFoundException, SQLException {
        // TODO Auto-generated method stub
        try {
            String account = user.getAccount();
            String password = user.getPassword();
            Connection con = dao.getCon();
            String sql = "update user set password = ? where account = ?";
            PreparedStatement ptmt = (PreparedStatement) con.prepareStatement(sql);
            ptmt.setString(1, password);
            ptmt.setString(2, account);
            ptmt.execute();
            return Constant.Success;
        } catch (Exception e) {
            e.printStackTrace();
            return Constant.Failed;
        }
    }

    //更改头像，关于更改头像的问题，需要说明的是：
    //用户更改头像时，因为头像信息是保存在固定路径，并且统一按照account命名，所以，即使用户重新上传了头像图片，那么从数据库角度来看，并没有发生任何改变
    //这也是使用url替换掉之前直接保存图像数据的原因，更加节省时间，节省网络资源。
    //当返回上传成功之后，客户端只需要做一次刷新，即可加载出新的头像信息。
    public int updateicon(User user) throws ClassNotFoundException {
        // TODO Auto-generated method stub
        try {
            String account = user.getAccount();
            createFileWithByte(user.getIcon(), account);
            return Constant.Success;
        } catch (Exception e) {
            e.printStackTrace();
            return Constant.Failed;
        }
    }

    //保存用户上传的头像数据到本地
    private void createFileWithByte(byte[] bytes, String filename) throws IOException {
        // TODO Auto-generated method stub
        /**
         * 创建File对象，其中包含文件所在的目录以及文件的命名
         */
        String fileName = filename + ".png"; // 文件可以为任何名，但得注意：必须要有后缀名。
//D:\Tomcat\icons
        File file = new File("D:\\Tomcat\\icons", fileName);
        // 创建FileOutputStream对象
        FileOutputStream outputStream = null;
        // 创建BufferedOutputStream对象
        BufferedOutputStream bufferedOutputStream = null;
        try {
            // 如果文件存在则删除
            if (file.exists()) {
                file.delete();
            }
            // 在文件系统中根据路径创建一个新的空文件
            file.createNewFile();
            // 获取FileOutputStream对象
            outputStream = new FileOutputStream(file);
            // 获取BufferedOutputStream对象
            bufferedOutputStream = new BufferedOutputStream(outputStream);
            // 往文件所在的缓冲输出流中写byte数据
            bufferedOutputStream.write(bytes);
            // 刷出缓冲输出流，该步很关键，要是不执行flush()方法，那么文件的内容是空的。
            bufferedOutputStream.flush();
        } catch (Exception e) {
            // 打印异常信息
            e.printStackTrace();
        } finally {
            // 关闭创建的流对象
            if (outputStream != null) {
                try {
                    outputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (bufferedOutputStream != null) {
                try {
                    bufferedOutputStream.close();
                } catch (Exception e2) {
                    e2.printStackTrace();
                }
            }
        }
    }

}
