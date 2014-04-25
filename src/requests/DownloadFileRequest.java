package requests;

public class DownloadFileRequest extends Request {
    public String fileName;
    public String fileFormat;

    public DownloadFileRequest(String fileName, String fileFormat, String uID) {
	super("downloadfile", "/file/downloadFile", "GET");
	this.fileName = fileName;
	this.fileFormat = fileFormat;
    }

}
