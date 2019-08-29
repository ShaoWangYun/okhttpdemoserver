package servlet;

public class ServerResponse {
    private int responseResult;
    private String currentVersionCode;
    private String downloadurl;

    public int getResponseResult() {
        return responseResult;
    }

    public void setResponseResult(int responseResult) {
        this.responseResult = responseResult;
    }

    public String getCurrentVersionCode() {
        return currentVersionCode;
    }

    public void setCurrentVersionCode(String currentVersionCode) {
        this.currentVersionCode = currentVersionCode;
    }

    public String getDownloadurl() {
        return downloadurl;
    }

    public void setDownloadurl(String downloadurl) {
        this.downloadurl = downloadurl;
    }

    public ServerResponse(int responseResult, String currentVersionCode, String downloadurl) {
        this.responseResult = responseResult;
        this.currentVersionCode = currentVersionCode;
        this.downloadurl = downloadurl;
    }

    public ServerResponse() {
    }

    @Override
    public String toString() {
        return "ServerResponse{" +
                "responseResult=" + responseResult +
                ", currentVersionCode='" + currentVersionCode + '\'' +
                ", downloadurl='" + downloadurl + '\'' +
                '}';
    }
}
