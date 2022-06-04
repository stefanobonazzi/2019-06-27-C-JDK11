package it.polito.tdp.crimes.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import it.polito.tdp.crimes.model.Arco;
import it.polito.tdp.crimes.model.Event;


public class EventsDao {
	
	public List<Event> listAllEvents(){
		String sql = "SELECT * FROM events" ;
		try {
			Connection conn = DBConnect.getConnection() ;

			PreparedStatement st = conn.prepareStatement(sql) ;
			
			List<Event> list = new ArrayList<>() ;
			
			ResultSet res = st.executeQuery() ;
			
			while(res.next()) {
				try {
					list.add(new Event(res.getLong("incident_id"),
							res.getInt("offense_code"),
							res.getInt("offense_code_extension"), 
							res.getString("offense_type_id"), 
							res.getString("offense_category_id"),
							res.getTimestamp("reported_date").toLocalDateTime(),
							res.getString("incident_address"),
							res.getDouble("geo_lon"),
							res.getDouble("geo_lat"),
							res.getInt("district_id"),
							res.getInt("precinct_id"), 
							res.getString("neighborhood_id"),
							res.getInt("is_crime"),
							res.getInt("is_traffic")));
				} catch (Throwable t) {
					t.printStackTrace();
					System.out.println(res.getInt("id"));
				}
			}
			
			conn.close();
			return list ;

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null ;
		}
	}
	
	public List<String> listEventCategory(){
		String sql = "SELECT DISTINCT `offense_category_id` "
				+ "FROM `events`" ;
		try {
			Connection conn = DBConnect.getConnection() ;
			PreparedStatement st = conn.prepareStatement(sql) ;
			List<String> list = new ArrayList<>() ;	
			ResultSet res = st.executeQuery() ;
			
			while(res.next()) {
				try {
					list.add(res.getString("offense_category_id"));
				} catch (Throwable t) {
					t.printStackTrace();
				}
			}
			
			conn.close();
			return list ;

		} catch (SQLException e) {
			e.printStackTrace();
			return null ;
		}
	}
	
	public List<String> listEventType(String category, LocalDate date){
		String sql = "SELECT DISTINCT `offense_type_id` "
				+ "FROM `events` "
				+ "WHERE `offense_category_id` = ? AND DATE(`reported_date`) = ?" ;
		try {
			Connection conn = DBConnect.getConnection();
			PreparedStatement st = conn.prepareStatement(sql);
			st.setString(1, category);
			st.setString(2, date.toString());
			List<String> list = new ArrayList<>();	
			ResultSet res = st.executeQuery();
			
			while(res.next()) {
				try {
					list.add(res.getString("offense_type_id"));
				} catch (Throwable t) {
					t.printStackTrace();
				}
			}
			
			conn.close();
			return list ;

		} catch (SQLException e) {
			e.printStackTrace();
			return null ;
		}
	}
	
	public List<LocalDate> listEventDate(){
		String sql = "SELECT DISTINCT DATE(`reported_date`) AS date\n"
				+ "FROM `events`" ;
		try {
			Connection conn = DBConnect.getConnection() ;
			PreparedStatement st = conn.prepareStatement(sql) ;
			List<LocalDate> list = new ArrayList<>() ;	
			ResultSet res = st.executeQuery() ;
			
			while(res.next()) {
				try {
					list.add(res.getDate("date").toLocalDate());
				} catch (Throwable t) {
					t.printStackTrace();
				}
			}
			
			conn.close();
			return list ;

		} catch (SQLException e) {
			e.printStackTrace();
			return null ;
		}
	}

	public List<Arco> listEdges(String category, LocalDate date) {
		String sql = "SELECT e1.`offense_type_id` AS type1, e2.`offense_type_id` AS type2, count(*) AS weight "
				+ "FROM `events` e1, `events` e2 "
				+ "WHERE e1.`offense_category_id` = ? AND DATE(e1.`reported_date`) = ? AND e1.`offense_category_id` = e2.`offense_category_id` "
				+ "		AND DATE(e1.`reported_date`) = DATE(e2.`reported_date`) AND e1.`offense_type_id` > e2.`offense_type_id` AND e1.`precinct_id` = e2.`precinct_id` "
				+ "GROUP BY e1.`offense_type_id`, e2.`offense_type_id`" ;
		try {
			Connection conn = DBConnect.getConnection();
			PreparedStatement st = conn.prepareStatement(sql);
			st.setString(1, category);
			st.setString(2, date.toString());
			List<Arco> list = new ArrayList<>();	
			ResultSet res = st.executeQuery();
			
			while(res.next()) {
				try {
					list.add(new Arco(res.getString("type1"), res.getString("type2"), (double)res.getInt("weight")));
				} catch (Throwable t) {
					t.printStackTrace();
				}
			}
			
			conn.close();
			return list ;

		} catch (SQLException e) {
			e.printStackTrace();
			return null ;
		}
	}
	
}
