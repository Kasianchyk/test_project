package ru.javabegin.training.spring.dao.objects;

public class MP3 {

	private int id;
	private String name;
	private Singer singer;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Singer getSinger() {
		return singer;
	}

	public void setAuthor(Singer singer) {
		this.singer = singer;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}
}
