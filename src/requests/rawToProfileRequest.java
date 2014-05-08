package requests;

public class rawToProfileRequest extends Request {

	public String fileName;
	public String fileID;
	public String expid;
	public String processtype;
	public String[] parameters;
	public String metadata;
	public String genomeRelease;
	public String author;

	public rawToProfileRequest(String fileName, String fileID, String expid,
			String processtype, String[] parameters, String metadata,
			String genomeRelease, String author) {

		super("rawtoprofile", "/process", "PUT");
		this.fileName = fileName;
		this.fileID = fileID;
		this.expid = expid;
		this.processtype = processtype;
		this.parameters = parameters;
		this.metadata = metadata;
		this.genomeRelease = genomeRelease;
		this.author = author;


	}

}
