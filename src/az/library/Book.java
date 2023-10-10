package az.library;

import java.time.LocalDate;

public class Book {
	private Integer id;
	private String name;
	private String author;
	private LocalDate registrationDate;
	private String language;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getAuthor() {
		return author;
	}

	public void setAuthor(String author) {
		this.author = author;
	}

	public LocalDate getRegistrationDate() {
		return registrationDate;
	}

	public void setRegistrationDate(LocalDate registrationDate) {
		this.registrationDate = registrationDate;
	}

	public String getLanguage() {
		return language;
	}

	public void setLanguage(String language) {
		this.language = language;
	}

	public Book(Integer id, String name, String author, LocalDate registrationDate, String language) {
		this.id = id;
		this.name = name;
		this.author = author;
		this.registrationDate = registrationDate;
		this.language = language;
	}

	@Override
	public String toString() {
		return "Book [id=" + id + ", name=" + name + ", author=" + author + ", registrationDate=" + registrationDate
				+ ", language=" + language + "]";
	}

	public Book() {

	}

}
