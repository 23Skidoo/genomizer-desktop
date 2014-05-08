package requests;

public class AddAnnotationRequest extends Request {

    /** TODO changed this from private to public, should be private? maybe (isak - dv12ilr)*/
	public String name;
	public String[] type;
	public String defaultType;
	public Boolean forced;

	public AddAnnotationRequest(String name, String[] categories, Boolean forced) {
		super("addAnnotation", "/annotation", "POST");
		this.name = name;
		this.type = categories;
		this.defaultType = "unknown";
		this.forced = forced;
	}

}
