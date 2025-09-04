package Matrices.EditorTabs;

import Matrices.Data.FeatureDSMData;
import Matrices.IOHandlers.FeatureIOHandler;
import UI.MatrixMetaDataPane;
import UI.MatrixViews.FeatureView;
import UI.SideBarViews.FeatureSideBar;
import javafx.beans.binding.Bindings;

import java.io.File;


/**
 * The editor tab for feature matrices. Only shows the matrix view as the main content
 */
public class FeatureEditorTab extends AbstractEditorTab {

    /**
     * Takes the data for a feature DSM
     *
     * @param matrixData  the data for the feature matrix
     */
    public FeatureEditorTab(FeatureDSMData matrixData, FeatureIOHandler ioHandler) {
        super(matrixData, ioHandler);
        this.matrixView = new FeatureView((FeatureDSMData) this.matrixData, 12.0);
        this.matrixSideBar = new FeatureSideBar((FeatureDSMData) this.matrixData, (FeatureView) this.matrixView);
    }

    /**
     * Creates a new matrix object by reading in a file. Throws IllegalArgumentException if there was an error reading
     * the file
     *
     * @param file    the file object that contains a feature dsm to read
     */
    public FeatureEditorTab(File file) {
        matrixIOHandler = new FeatureIOHandler(file);
        matrixData = matrixIOHandler.readFile();
        if(matrixData == null) {
            throw new IllegalArgumentException("There was an error reading the matrix at " + file);  // error because error occurred on file read
        }
        matrixIOHandler.setMatrix(matrixData);

        matrixView = new FeatureView((FeatureDSMData) matrixData, 12.0);
        matrixSideBar = new FeatureSideBar((FeatureDSMData) matrixData, (FeatureView) matrixView);

        this.metadata = new MatrixMetaDataPane(this.matrixData);
        this.isSaved.bind(this.matrixData.getWasModifiedProperty().not());  // saved when not modified

        this.title.bind(Bindings.createStringBinding(() -> {
            String title = matrixIOHandler.getSavePath().getName();
            if (matrixData.getWasModifiedProperty().get()) {
                title += "*";
            }
            return title;
        }, matrixData.getWasModifiedProperty()));
    }

}
