package requests;

import util.AnnotationDataValue;

/**
 * This class represents a "Add an experiment" request in an application for
 * genome researchers. This request adds a experiment to the database of the
 * application.
 *
 * @author
 *
 */
public class AddExperimentRequest extends Request {
    /**
     * Attributes needed for the request.
     *
     */
    public String name;
    public String createdBy;
    public AnnotationDataValue[] annotations;

    /**
     * Constructor creating the request.
     *
     * @param experimentName
     *            String representing the name of the experiment.
     * @param createdBy
     *            String representing the user creating the experiment.
     * @param annotations
     *            An array representing the annotations assigned to the
     *            experiment.
     */
    public AddExperimentRequest(String experimentName, String createdBy,
            AnnotationDataValue[] annotations) {
        super("addexperiment", "/experiment", "POST");
        this.name = experimentName;
        this.createdBy = createdBy;
        this.annotations = annotations;
    }
}
