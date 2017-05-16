package gui;

import com.sun.javafx.collections.ObservableListWrapper;
import data.Coordinate;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import processors.DecimalDegreeBoundaryCalculator;
import processors.BoundaryCalculator;
import processors.DecimalDegreeInputValidator;
import processors.InputValidator;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by mversaggi on 5/11/2017.
 */
public class CoordinateSystemPlannerController
{

    @FXML private AnchorPane            parentPane;
    @FXML private TextField             latitudeTextField;
    @FXML private TextField             longitudeTextField;
    @FXML private TextField             distanceFromCenter;
    @FXML private TextField             numPointsTextField;
    @FXML private ChoiceBox<ShapeType>  shapeChooser;

    @FXML private TableColumn<Coordinate, Double> latitudeColumn;
    @FXML private TableColumn<Coordinate, Double> longitudeColumn;
    @FXML private TableView<Coordinate> coordinateOutputTable;

    private final int numPointsForSquare = 4;

    public void initialize()
    {
        this.shapeChooser.setItems(new ObservableListWrapper<>(Arrays.asList(ShapeType.values())));
        this.shapeChooser.setValue(ShapeType.SQUARE);
        this.shapeChooser.valueProperty().addListener((observable, oldVal, newVal) -> shapeTypeChanged(newVal));
        this.numPointsTextField.setDisable(true);

        this.latitudeColumn.setCellValueFactory(param -> new ReadOnlyObjectWrapper<>(param.getValue().getLatitude()));
        this.longitudeColumn.setCellValueFactory(param -> new ReadOnlyObjectWrapper<>(param.getValue().getLongitude()));
    }

    private void shapeTypeChanged(ShapeType newType)
    {
        if (newType.equals(ShapeType.SQUARE))
        {
            numPointsTextField.setDisable(true);
        }
        else
        {
            numPointsTextField.setDisable(false);
        }
    }

    @FXML
    private void calculateBoundaryCooridnates()
    {
        int numPoints = getNumPoints();
        int distanceToEdge = getDistanceToEdgeFromCenter();
        Coordinate centerPoint = getCenterPointCoordinate();
        ObservableList<Coordinate > boundaryCoordinates = FXCollections.emptyObservableList();
        BoundaryCalculator boundaryCalculator = new DecimalDegreeBoundaryCalculator(centerPoint, numPoints, distanceToEdge);

        if (centerPoint != null)
        {
            boundaryCoordinates = FXCollections.observableArrayList(boundaryCalculator.calculateBoundaryCoordinates());
        }
        else
        {
            showErrorAlertWithMessage("Coordinates given are outside of latitude/longitude range:" +
                                      " -90 <= latitude <= 90, -180 <= longitude <= 180");
        }

        coordinateOutputTable.setItems(boundaryCoordinates);

        printCoordinatesToCSV(boundaryCoordinates);
    }

    private int getNumPoints()
    {
        int numPoints = numPointsForSquare;

        try
        {
            if (shapeChooser.getValue().equals(ShapeType.CIRCLE))
            {
                numPoints = Integer.valueOf(numPointsTextField.getText());
            }
        }
        catch (NumberFormatException e)
        {
            showErrorAlertWithMessage("Number of points is not a properly formatted number");
        }

        return numPoints;
    }

    private int getDistanceToEdgeFromCenter()
    {
        int distanceToEdgeFromCenter = 10000;

        try
        {
            if (shapeChooser.getValue().equals(ShapeType.CIRCLE))
            {
                distanceToEdgeFromCenter = Integer.valueOf(distanceFromCenter.getText());
            }
        }
        catch (NumberFormatException e)
        {
            showErrorAlertWithMessage("Number of points is not a properly formatted number");
        }

        return distanceToEdgeFromCenter;
    }

    private Coordinate getCenterPointCoordinate()
    {
        double latitude = getDoubleValueFromCoordinateFieldText(latitudeTextField.getText(), "Latitude");
        double longitude = getDoubleValueFromCoordinateFieldText(longitudeTextField.getText(), "Longitude");
        Coordinate centerPointCoordinate;
        InputValidator inputValidator = new DecimalDegreeInputValidator();

        if (latitude < -90 || latitude > 90 || longitude < -180 || longitude > 180)
        {
            centerPointCoordinate = null;
        }
        else
        {
            centerPointCoordinate = new Coordinate(latitude, longitude);
        }

        return centerPointCoordinate;
    }

    private double getDoubleValueFromCoordinateFieldText(String coordinateFieldText, String coordinateType)
    {
        double coordinateAfterValidation = Double.MIN_VALUE;

        try
        {
            coordinateAfterValidation = Double.valueOf(coordinateFieldText);
        }
        catch (NumberFormatException e)
        {
            showErrorAlertWithMessage(coordinateType + " coordinate field improperly " +
                                      "formatted, should be formatted as ###.######");
        }

        return coordinateAfterValidation;
    }

    private void showErrorAlertWithMessage(String errorMessage)
    {
        Alert alertDialog = new Alert(Alert.AlertType.ERROR, errorMessage, null);

        alertDialog.showAndWait();
    }

    private void printCoordinatesToCSV(ObservableList<Coordinate> boundaryCoordinates)
    {
        final String outputCsvFileName = "boundaryCoordinates.csv";
        FileWriter newCsvFileWriter;
        File file = new File(outputCsvFileName);

        try
        {
            if (file.exists())
            {
                if (!file.delete());
                {
                    System.err.println("Unable to delete " + outputCsvFileName);
                }
            }

            newCsvFileWriter = new FileWriter(outputCsvFileName);

            newCsvFileWriter.write("Latitude,Longitude\n");

            for (Coordinate currentCoordinate : boundaryCoordinates)
            {
                System.out.println("Writing coordinate to CSV: " + currentCoordinate);
                newCsvFileWriter.write(currentCoordinate.getLatitude() + "," + currentCoordinate.getLongitude() + "\n");
            }

            newCsvFileWriter.close();
        }
        catch (IOException e)
        {
            System.err.println("Unable to write to " + outputCsvFileName + ": " + e);
        }
    }

    @FXML
    private void quitProgram()
    {
        System.exit(0);
    }
}
