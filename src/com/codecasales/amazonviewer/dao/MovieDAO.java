package com.codecasales.amazonviewer.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import com.codecasales.amazonviewer.db.IDBConection;
import com.codecasales.amazonviewer.model.Movie;
import static com.codecasales.amazonviewer.db.DataBase.*;

public interface MovieDAO extends IDBConection{
	
	default Movie setMovieViewed(Movie movie, Date date){
		try (Connection connection = connecToDB()){
			SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
			String dateString = df.format(date);
			Statement statement = connection.createStatement();
			String query = "INSERT INTO " + TVIEWED +
					" (" + TVIEWED_IDMATERIAL + ", " + TVIEWED_IDELEMENT 
					+ ", " + TVIEWED_IDUSER + ", " + TVIEWED_DATE + ")" +
					"VALUES(1, " + movie.getId() + ", 1, '" +
					 dateString + "')";
			System.out.println(query);
			if(statement.executeUpdate(query) > 0) {
				System.out.println("Se marco en visto");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return movie;
	} 
	
	default ArrayList<Movie> read(){
		ArrayList<Movie> movies = new ArrayList<Movie>();
		try (Connection connection = connecToDB()){
			String query = "SELECT * FROM " + TMOVIE;
			PreparedStatement ps = connection.prepareStatement(query);
			ResultSet rs = ps.executeQuery();
			while(rs.next()) {
				Movie movie = new Movie(
						rs.getString(TMOVIE_TITLE),
						rs.getString(TMOVIE_GENERE),
						rs.getString(TMOVIE_CREATOR),
						Integer.valueOf(rs.getString(TMOVIE_DURATION)),
						Short.valueOf(rs.getString(TMOVIE_YEAR)));
				movie.setId(Integer.valueOf(rs.getString(TMOVIE_ID)));
				movie.setViewed(getMovieViewed(ps, connection, Integer.valueOf(rs.getString(TMOVIE_ID))));
				movies.add(movie);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return movies;
	}
	
	private boolean getMovieViewed(PreparedStatement ps, Connection connection, int id_movie) {
		boolean viewed = false;
		String query = "SELECT * FROM " + TVIEWED +
				" WHERE " + TVIEWED_IDMATERIAL + " = ?" +
				" AND " + TVIEWED_IDELEMENT + " = ?" +
				" AND " + TVIEWED_IDUSER + " = ?";
		ResultSet rs = null;
		try {
			ps = connection.prepareStatement(query);
			ps.setInt(1, 1);
			ps.setInt(2, id_movie);
			ps.setInt(3, 1);
			
			rs = ps.executeQuery();
			viewed = rs.next();
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		return viewed;
	}
	
	default String getDateViewed(int id_movie) {
		String query = "SELECT " + TVIEWED_DATE + " FROM " + TVIEWED +
				" WHERE " + TVIEWED_IDELEMENT + " = " + id_movie;
		try (Connection connection = connecToDB()){
			PreparedStatement ps = connection.prepareStatement(query);
			ResultSet rs = ps.executeQuery();
			if(rs.next()) {
				return rs.getString(TVIEWED_DATE);
			}
			return null;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
}
