package az.library;

import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.ResourceBundle;

import javax.swing.JOptionPane;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;

public class BookController implements Initializable {

	boolean updateMode;
	int selectedId = 0;

	@FXML
	private TextField searchTF;

	@FXML
	private Button saveButton;

	@FXML
	private TextField authorTF;

	@FXML
	private TableColumn<Book, String> languageColumn;

	@FXML
	private TextField nameTF;

	@FXML
	private ComboBox<String> languageCB;

	@FXML
	private DatePicker registrationDateDP;

	@FXML
	private TableColumn<Book, LocalDate> registrationDateColumn;

	@FXML
	private TableColumn<Book, String> authorColumn;

	@FXML
	private Label rowCountLabel;

	@FXML
	private TableColumn<Book, String> nameColumn;

	@FXML
	private TableView<Book> booksTable;

	@FXML
	private TableColumn<Book, Integer> idColumn;

	@FXML
	void saveButtonAction(ActionEvent event) throws SQLException {
		String name = nameTF.getText();
		if (name.isEmpty()) {
			JOptionPane.showMessageDialog(null, "Adı daxil edin");
			return;
		}

		if (registrationDateDP.getValue() == null) {
			JOptionPane.showMessageDialog(null, "Tarixi yazın.");
			return;
		}

		if (languageCB.getValue() == null) {
			JOptionPane.showMessageDialog(null, "Dili seçin.");
			return;
		}
		String author = authorTF.getText();
		LocalDate registrationDate = registrationDateDP.getValue();
		String language = languageCB.getValue();

		Book book = new Book(0, name, author, registrationDate, language);
		Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/javabooks", "root", "1234");
		Statement st = conn.createStatement();
		if (updateMode) {
			st.executeUpdate("update books set name = '" + book.getName() + "',author ='" + book.getAuthor()
					+ "', registrationDate ='" + book.getRegistrationDate() + "'," + "language='" + book.getLanguage()
					+ "' where id = " + selectedId);
			updateMode = false;
			saveButton.setText("Yeni kitab");
		} else {
			st.executeUpdate("insert into books (name,author,registrationDate,language)" + "values ('" + book.getName()
					+ "','" + book.getAuthor() + "','" + book.getRegistrationDate() + "','" + book.getLanguage()
					+ "')");
		}
		showBooks();
		nameTF.setText("");
		authorTF.setText("");
	}

	@FXML
	void deleteButtonAction(ActionEvent event) throws SQLException {
		Book selectedBook = booksTable.getSelectionModel().getSelectedItem();
		if (selectedBook == null) {
			JOptionPane.showMessageDialog(null, "Siyahıdan tələbə seçin");
			return;
		}

		Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/javabooks", "root", "1234");
		Statement st = conn.createStatement();
		st.executeUpdate("delete from books where id =" + selectedBook.getId());

		showBooks();
	}

	@FXML
	void editButtonAction(ActionEvent event) {
		Book selectedBook = booksTable.getSelectionModel().getSelectedItem();
		if (selectedBook == null) {
			JOptionPane.showMessageDialog(null, "Siyahıdan kitab seçin");
			return;
		}

		nameTF.setText(selectedBook.getName());
		authorTF.setText(selectedBook.getAuthor());
		registrationDateDP.setValue(selectedBook.getRegistrationDate());
		languageCB.setValue(selectedBook.getLanguage());

		updateMode = true;
		selectedId = selectedBook.getId();
		saveButton.setText("Yadda saxla");
	}

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {

		saveButton.setText("Yeni kitab");

		languageCB.getItems().add("Azərbaycan");
		languageCB.getItems().add("Rus");
		languageCB.getItems().add("İngilis");
		languageCB.getItems().add("Türk");

		idColumn.setCellValueFactory(new PropertyValueFactory("id"));
		nameColumn.setCellValueFactory(new PropertyValueFactory("name"));
		authorColumn.setCellValueFactory(new PropertyValueFactory("author"));
		registrationDateColumn.setCellValueFactory(new PropertyValueFactory("registrationDate"));
		languageColumn.setCellValueFactory(new PropertyValueFactory("language"));

		try {
			showBooks();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	private void showBooks() throws SQLException {
		Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/javabooks", "root", "1234");
		Statement st = conn.createStatement();
		ResultSet setirler = st.executeQuery("select * from books");
		ArrayList<Book> booksList = new ArrayList<Book>();

		while (setirler.next()) {
			Book s = new Book();
			s.setId(setirler.getInt("id"));
			s.setName(setirler.getString("name"));
			s.setAuthor(setirler.getString("author"));
			s.setRegistrationDate(setirler.getDate("registrationDate").toLocalDate());
			s.setLanguage(setirler.getString("language"));
			booksList.add(s);

		}
		ObservableList<Book> list = FXCollections.observableArrayList();
		list.addAll(booksList);
		booksTable.setItems(list);

		rowCountLabel.setText("Kitab sayı: " + booksList.size());
	}

	@FXML
	void searchTFAction(ActionEvent event) throws SQLException {
		String search = searchTF.getText();
		System.out.println(search);

		showBooks("where name like '%" + search + "%';");
	}

	@FXML
	void searchTFKeyReleased(ActionEvent event) throws SQLException {
		String search = searchTF.getText();
		System.out.println(search);

		showBooks("where name like '%" + search + "%';");
	}

	private void showBooks(String q) throws SQLException {
		Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/javabooks", "root", "1234");
		Statement st = conn.createStatement();
		ResultSet setirler = st.executeQuery("select * from books " + q);
		ArrayList<Book> booksList = new ArrayList<Book>();

		while (setirler.next()) {
			Book s = new Book();
			s.setId(setirler.getInt("id"));
			s.setName(setirler.getString("name"));
			s.setAuthor(setirler.getString("author"));
			s.setRegistrationDate(setirler.getDate("registrationDate").toLocalDate());
			s.setLanguage(setirler.getString("language"));
			booksList.add(s);

		}
		ObservableList<Book> list = FXCollections.observableArrayList();
		list.addAll(booksList);
		booksTable.setItems(list);

		rowCountLabel.setText("Kitab sayı: " + booksList.size());
	}

}
