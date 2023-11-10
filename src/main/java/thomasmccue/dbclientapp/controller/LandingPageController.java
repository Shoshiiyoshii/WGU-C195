package thomasmccue.dbclientapp.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import thomasmccue.dbclientapp.Main;
import thomasmccue.dbclientapp.dao.AppointmentDao;
import thomasmccue.dbclientapp.dao.CustomerDao;
import thomasmccue.dbclientapp.model.*;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.time.*;
import java.time.LocalDateTime;
import java.time.temporal.*;
import java.util.*;

public class LandingPageController implements Initializable {
    @FXML
    private Label ynUpcomingAppointment, nextAppt, nextApptWith, whenNext, whoNext, title, customersTitle, customerErrorMessage, apptErrorMessage;
    @FXML
    public TableView<Customer> custTable;
    @FXML
    private TableColumn<Customer, Integer> custIdBottomCol, divisionIdCol;
    @FXML
    private TableColumn<Customer, String> nameCol, addressCol, postalCodeCol, phoneCol, createDateCol, createdByCol, lastUpdated, lastUpdatedByCol, countryCol;
    @FXML
    private Button custAddButton, custUpdateButton, custDeleteButton, apptAddButton1, apptUpdateButton, apptDeleteButton,exitButton, apptReportButton, contactSchedulesButton, custRecordCreationReportButton;
    @FXML
    private TableView<Appointment> apptTable;
    @FXML
    private TableColumn<Appointment, Integer> apptIdCol, contactIdCol, custIdTopCol, userIdCol;
    @FXML
    private TableColumn<Appointment, String> titleCol, descCol, locationCol, typeCol, startDTCol, endDTCol;
    @FXML
    private RadioButton weekRadio, monthRadio, allTimeRadio;

    public void clickCustAdd(ActionEvent event) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("addOrUpdateCustomer.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        Stage stage = new Stage();

        AddOrUpdateCustomerController controller = fxmlLoader.getController();
        controller.setUpAdd("Add A Customer To The System", "Save");
        stage.setTitle("Add A Customer To The System");
        stage.setScene(scene);
        stage.show();
    }
    public void clickCustUpdate(ActionEvent event) throws IOException {
        SelectionModel<Customer> selectionModel = custTable.getSelectionModel();
        Customer selectedCust = selectionModel.getSelectedItem();

        if(selectedCust == null){
            customerErrorMessage.setText("Please first select a Customer to Modify.");
        } else{
            customerErrorMessage.setText("");
            FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("addOrUpdateCustomer.fxml"));
            Scene scene = new Scene(fxmlLoader.load());
            Stage stage = new Stage();

            AddOrUpdateCustomerController controller = fxmlLoader.getController();
            controller.setUpModify("Modify An Existing Customer", "Save Update", selectedCust);
            stage.setTitle("Modify An Existing Customer");
            stage.setScene(scene);
            stage.show();
        }

    }
    public void clickCustDelete(ActionEvent event) throws IOException{
        SelectionModel<Customer> selectionModel = custTable.getSelectionModel();
        Customer selectedCust = selectionModel.getSelectedItem();

        if(selectedCust == null) {
            customerErrorMessage.setText("Please first select a Customer to Delete.");
        } else {
            customerErrorMessage.setText("");
            FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("deleteDialog.fxml"));
            Scene scene = new Scene(fxmlLoader.load());
            Stage stage = new Stage();

            DeleteDialogController controller = fxmlLoader.getController();
            controller.setUpCustomerDelete(selectedCust);
            stage.setTitle("Delete Customer");
            stage.setScene(scene);
            stage.show();
        }


    }

    public void radioFilter(ActionEvent event){
        //get the current date to compare
        LocalDate now = LocalDate.now();
        if(monthRadio.isSelected()){
            //get the current month and year
            int currentMonth = now.getMonthValue();
            int currentYear = now.getYear();

            //FIXME LAMBDA HERE
            //create a filtered list of appointments that occur during the current month and year
            FilteredList<Appointment> thisMonthsAppts = new FilteredList<>(AppointmentDao.displayAppt, appointment -> {
                LocalDateTime start = appointment.getStart();
                return start.getMonthValue() == currentMonth && start.getYear() == currentYear;
            });

            // set the filtered list to the TableView
            apptTable.setItems(thisMonthsAppts);

        } else if (weekRadio.isSelected()) {
            //get current week range
            TemporalField fieldISO = WeekFields.of(Locale.getDefault()).dayOfWeek();
            LocalDate startOfWeek = now.with(fieldISO, 1); // Start of week (e.g., Monday)
            LocalDate endOfWeek = now.with(fieldISO, 7); // End of week (e.g., Sunday)

            //FIXME LAMBDA HERE
            FilteredList<Appointment> thisWeeksAppts = new FilteredList<>(AppointmentDao.displayAppt, appointment -> {
                LocalDateTime start = appointment.getStart();
                return !start.toLocalDate().isBefore(startOfWeek) && !start.toLocalDate().isAfter(endOfWeek);
            });

            apptTable.setItems(thisWeeksAppts);
        }else if(allTimeRadio.isSelected()){
           apptTable.setItems(AppointmentDao.displayAppt);
        }
    }
    public void clickApptAdd(ActionEvent event) throws IOException {
       /* FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("addOrModifyAppointmentPage.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        Stage stage = new Stage();

        AddOrUpdateAppointmentController controller = fxmlLoader.getController();
        controller.setUpAdd("Create a new Appointment", "Save");
        stage.setTitle("Create A New Appointment");
        stage.setScene(scene);
        stage.show();*/
    }
    public void clickApptUpdate(ActionEvent event) throws IOException{
       /* SelectionModel<Appointment> selectionModel = apptTable.getSelectionModel();
        Appointment selectedAppt = selectionModel.getSelectedItem();

        if(selectedAppt == null) {
            apptErrorMessage.setText("Please select an existing appointment to modify.");

        } else {
            apptErrorMessage.setText("");
            FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("addOrModifyAppointmentPage.fxml"));
            Scene scene = new Scene(fxmlLoader.load());
            Stage stage = new Stage();

            AddOrUpdateAppointmentController controller = fxmlLoader.getController();
            int apptID = selectedAppt.getApptId();
            controller.setUpModify("Update An Existing Appointment", "Save Changes", apptID);
            controller.preFillFields(selectedAppt);

            stage.setTitle("Update Existing Appointment");
            stage.setScene(scene);
            stage.show();
        }*/
    }

    public void clickApptDelete(ActionEvent event) throws SQLException, IOException {
      /*  FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("deleteDialog.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        Stage stage = new Stage();
        stage.setTitle("Delete Appointment");
        stage.setScene(scene);

      DeleteDialogController dialogController = fxmlLoader.getController();

        SelectionModel<Appointment> selectionModel = apptTable.getSelectionModel();
        Appointment selectedAppt = selectionModel.getSelectedItem();
        if(selectedAppt != null) {
            apptErrorMessage.setText("");
            dialogController.setAppointment(selectedAppt);
            dialogController.setToBeDeletedAppt();

            stage.show();
        } else{
            apptErrorMessage.setText("First select an appointment to delete.");
        }*/
    }

    public void exitClicked(ActionEvent event)throws IOException {
        Stage stage = (Stage) exitButton.getScene().getWindow();
        stage.close();
    }
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        custTable.setItems(CustomerDao.getAllCust());

        custIdBottomCol.setCellValueFactory(new PropertyValueFactory<>("customerId"));
        nameCol.setCellValueFactory(new PropertyValueFactory<>("customerName"));
        addressCol.setCellValueFactory(new PropertyValueFactory<>("address"));
        postalCodeCol.setCellValueFactory(new PropertyValueFactory<>("postalCode"));
        phoneCol.setCellValueFactory(new PropertyValueFactory<>("phone"));
        createDateCol.setCellValueFactory(new PropertyValueFactory<>("createDate"));
        createdByCol.setCellValueFactory(new PropertyValueFactory<>("createdBy"));
        lastUpdated.setCellValueFactory(new PropertyValueFactory<>("lastUpdate"));
        lastUpdatedByCol.setCellValueFactory(new PropertyValueFactory<>("lastUpdatedBy"));
        divisionIdCol.setCellValueFactory(new PropertyValueFactory<>("divisionId"));
        countryCol.setCellValueFactory(new PropertyValueFactory<>("country"));


        //populate appointment table with all appointments, if table is not yet populated
        apptTable.setItems(AppointmentDao.getAllAppointments());

        apptIdCol.setCellValueFactory(new PropertyValueFactory<>("apptId"));
        titleCol.setCellValueFactory(new PropertyValueFactory<>("title"));
        descCol.setCellValueFactory(new PropertyValueFactory<>("desc"));
        locationCol.setCellValueFactory(new PropertyValueFactory<>("location"));
        contactIdCol.setCellValueFactory(new PropertyValueFactory<>("contactId"));
        typeCol.setCellValueFactory(new PropertyValueFactory<>("type"));
        startDTCol.setCellValueFactory(new PropertyValueFactory<>("start"));
        endDTCol.setCellValueFactory(new PropertyValueFactory<>("end"));
        custIdTopCol.setCellValueFactory(new PropertyValueFactory<>("custId"));
        userIdCol.setCellValueFactory(new PropertyValueFactory<>("userId"));

        allTimeRadio.setSelected(true);

    }
}
