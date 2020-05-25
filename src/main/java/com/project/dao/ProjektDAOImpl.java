package com.project.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import com.project.datasource.DataSource;
import com.project.model.Projekt;

public class ProjektDAOImpl implements ProjektDAO {

	@Override
	public void setProjekt(Projekt projekt){
		boolean isInsert = projekt.getProjektId() == null;
		String query = isInsert ?
				"INSERT INTO projekt(nazwa, opis, data_czas_utworzenia, data_oddania) VALUES (?, ?, ?, ?)"
				: "UPDATE projekt SET nazwa = ?, opis = ?, data_czas_utworzenia = ?, data_oddania = ?"
				+ " WHERE projekt_id = ?";
		try (Connection connect = DataSource.getConnection();
				PreparedStatement prepStmt = connect.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
			//Wstawianie do zapytania odpowiednich wartoœci w miejsce znaków '?'
			//Uwaga! Indeksowanie znaków '?' zaczyna siê od 1!
			prepStmt.setString(1, projekt.getNazwa());
			prepStmt.setString(2, projekt.getOpis());
			if(projekt.getDataCzasUtworzenia() == null)
				projekt.setDataCzasUtworzenia(LocalDateTime.now());
			prepStmt.setObject(3,projekt.getDataCzasUtworzenia());
			prepStmt.setObject(4, projekt.getDataOddania());
			if(!isInsert) prepStmt.setInt(5, projekt.getProjektId());
			//Wysy³anie zapytania i pobieranie danych
			int liczbaDodanychWierszy = prepStmt.executeUpdate();
			//Pobieranie kluczy g³ównych, tylko dla nowo utworzonych projektów
			if (isInsert && liczbaDodanychWierszy > 0) {
				ResultSet keys = prepStmt.getGeneratedKeys();
				if (keys.next()) {
					projekt.setProjektId(keys.getInt(1));
				}
				keys.close();
			}
		}catch(SQLException e) {
			throw new RuntimeException(e);
		}
	}
	@Override
	public List<Projekt> getProjekty(Integer offset, Integer limit){
		List<Projekt> projekty = new ArrayList<>();
		String query = "SELECT * FROM projekt ORDER BY data_czas_utworzenia DESC"
				+ (offset != null ? " OFFSET ?" : "")
				+ (limit != null ? " LIMIT ?" : "");

		try (Connection connect = DataSource.getConnection();
				PreparedStatement preparedStmt = connect.prepareStatement(query)) {
			int i = 1;
			if (offset != null) {
				preparedStmt.setInt(i, offset);
				i += 1;
			}
			if (limit != null) {
				preparedStmt.setInt(i, limit);
			}
			try (ResultSet rs = preparedStmt.executeQuery()) {
				while (rs.next()) {
					Projekt projekt = new Projekt();
					projekt.setProjektId(rs.getInt("projekt_id"));
					projekt.setNazwa(rs.getString("nazwa"));
					projekt.setOpis(rs.getString("opis"));
					projekt.setDataCzasUtworzenia(rs.getObject("data_czas_utworzenia", LocalDateTime.class));
					projekt.setDataOddania(rs.getObject("data_oddania", LocalDate.class));
					projekty.add(projekt);
				}
			}
		}catch(SQLException e) {
			throw new RuntimeException(e);
		}
		return projekty;
	}
	
	@Override
	public Projekt getProjekt(Integer projektId) {
		String query = "SELECT * FROM projekt WHERE projekt_id = " + projektId;
		Projekt projekt = new Projekt();
		
		try (Connection connect = DataSource.getConnection();
				PreparedStatement preparedStmt = connect.prepareStatement(query)) {
			try (ResultSet rs = preparedStmt.executeQuery()) {
				while (rs.next()) {
					projekt.setProjektId(rs.getInt("projekt_id"));
					projekt.setNazwa(rs.getString("nazwa"));
					projekt.setOpis(rs.getString("opis"));
					projekt.setDataCzasUtworzenia(rs.getObject("data_czas_utworzenia", LocalDateTime.class));
					projekt.setDataOddania(rs.getObject("data_oddania", LocalDate.class));
					return projekt;
				}
			}
		}catch(SQLException e) {
			throw new RuntimeException(e);
		}
		return projekt;

	}
	@Override
	public void deleteProjekt(Integer projektId) {
		String query = "DELETE FROM projekt WHERE projekt_id = " + projektId;
		
		try (Connection connect = DataSource.getConnection();
				PreparedStatement prepStmt = connect.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
			
			prepStmt.executeUpdate();
		}catch(SQLException e) {
			throw new RuntimeException(e);
		}

	}
	@Override
	public List<Projekt> searchByNazwa(String search4, Integer offset, Integer limit) {
		List<Projekt> projekty = new ArrayList<>();
		String query = "SELECT * FROM projekt WHERE nazwa = '" + search4 + "' ORDER BY data_czas_utworzenia DESC"
				+ (offset != null ? " OFFSET ?" : "")
				+ (limit != null ? " LIMIT ?" : "");

		try (Connection connect = DataSource.getConnection();
				PreparedStatement preparedStmt = connect.prepareStatement(query)) {
			int i = 1;
			if (offset != null) {
				preparedStmt.setInt(i, offset);
				i += 1;
			}
			if (limit != null) {
				preparedStmt.setInt(i, limit);
			}
			try (ResultSet rs = preparedStmt.executeQuery()) {
				while (rs.next()) {
					Projekt projekt = new Projekt();
					projekt.setProjektId(rs.getInt("projekt_id"));
					projekt.setNazwa(rs.getString("nazwa"));
					projekt.setOpis(rs.getString("opis"));
					projekt.setDataCzasUtworzenia(rs.getObject("data_czas_utworzenia", LocalDateTime.class));
					projekt.setDataOddania(rs.getObject("data_oddania", LocalDate.class));
					projekty.add(projekt);
				}
			}
		}catch(SQLException e) {
			throw new RuntimeException(e);
		}
		return projekty;
	} 

}
