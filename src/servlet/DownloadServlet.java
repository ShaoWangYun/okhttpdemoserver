package servlet;

import com.google.gson.Gson;

import java.io.*;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@SuppressWarnings("serial")
public class DownloadServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    String lineTxt = null;
    int currentVersionCode;
    int newVersionCode;
    String downloadurl;

    public DownloadServlet() {
        super();
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setCharacterEncoding("UTF-8");
        //����ContentType�ֶ�ֵ
        response.setContentType("text/html;charset=utf-8");
        //��ȡ��Ҫ���ص��ļ�����
        String versioncode = request.getParameter("versionCode");
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        System.out.println("doPost!!!");
        response.setCharacterEncoding("UTF-8");
        //����ContentType�ֶ�ֵ
        response.setContentType("text/html;charset=utf-8");

        BufferedReader reader = request.getReader();
        String json = reader.readLine();
        System.out.println("json is : " + json);
        Gson gson = new Gson();
        ServerResponse serverResponse = gson.fromJson(json, ServerResponse.class);

        String encoding = "GBK";
        try {
            File file = new File("C:\\AutoUpdate\\NewVersionInfo.txt");
            if (file.isFile() && file.exists()) { //�ж��ļ��Ƿ����
                InputStreamReader read = new InputStreamReader(
                        new FileInputStream(file), encoding);//���ǵ������ʽ
                BufferedReader bufferedReader = new BufferedReader(read);
                lineTxt = bufferedReader.readLine();
                read.close();
                ServerResponse serverResponse1 = new ServerResponse();
                if(null!=lineTxt && !lineTxt.equals("")){
                    String[] strarray = lineTxt.split(";");
                    for(int i = 0;i<strarray.length;i++){
                        newVersionCode = Integer.valueOf(strarray[0]);
                        downloadurl = strarray[1];
                    }
                    currentVersionCode = Integer.valueOf(serverResponse.getCurrentVersionCode());
                    System.out.println("currentVersionCode is : "+currentVersionCode);
                    System.out.println("newVersionCode is : "+newVersionCode);
                    if(currentVersionCode<newVersionCode){
                       serverResponse1.setResponseResult(0);
                       serverResponse1.setCurrentVersionCode(String.valueOf(newVersionCode));
                       serverResponse1.setDownloadurl(downloadurl);
                    }else{
                        serverResponse1.setResponseResult(1);
                    }
                }else{
                    serverResponse1.setResponseResult(1);
                }
                PrintWriter out2 = response.getWriter();
                out2.write(gson.toJson(serverResponse1));
                out2.close();

            } else {
                System.out.println("�Ҳ���ָ�����ļ�");
            }
        } catch (Exception e) {
            System.out.println("��ȡ�ļ����ݳ���");
            e.printStackTrace();
        }

    }

    @Override
    public void init() throws ServletException {
        super.init();
        System.out.println("Servlet init");
    }
}
