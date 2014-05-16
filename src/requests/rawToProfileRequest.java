package requests;

public class rawToProfileRequest extends Request {

    public String expid;
    public String[] parameters;
    public String metadata;
    public String genomeVersion;
    public String author;

    /**
     * Creates the request for creating profile data from RAW-file.
     *
     * @param fileName
     * @param parameters
     * @param metadata
     * @param genomeRelease
     * @param author
     */
    public rawToProfileRequest(String expid, String[] parameters, String metadata,
            String genomeVersion, String author) {

        super("rawtoprofile", "/process", "PUT");
        this.expid = expid;
        this.parameters = parameters;
        this.metadata = metadata;
        this.genomeVersion = genomeVersion;
        this.author = author;

    }

}
