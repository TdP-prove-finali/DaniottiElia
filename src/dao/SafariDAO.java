package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import model.Avvistamento;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;


public class SafariDAO {
	
	public List<Character> getVertici(){
		String sql = "SELECT idTratta FROM mappa";
		List<Character> result = new ArrayList<>();
		Connection conn = DBconnect.getConnection();

		try {
			PreparedStatement st = conn.prepareStatement(sql);
			
			ResultSet res = st.executeQuery();
			while (res.next()) {
				String id = res.getString("idTratta");
				if(!(result.contains(id.charAt(0))))
					result.add(id.charAt(0));
				if(!(result.contains(id.charAt(1))))
					result.add(id.charAt(1));
			}
			
			conn.close();
			return result;

		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public int getPesoArco(Character v1, Character v2){
		int peso=0;
		String sql= "SELECT lunghezza FROM mappa WHERE (inizio=? AND fine=?) OR (inizio=? AND fine=?)";
		Connection conn = DBconnect.getConnection();

		try {
			PreparedStatement st = conn.prepareStatement(sql);
			st.setString(1, v1.toString());
			st.setString(2, v2.toString());
			st.setString(3, v2.toString());
			st.setString(4, v1.toString());
			
			ResultSet res = st.executeQuery();
			if (res.next())
				peso=res.getInt("lunghezza");
			
			conn.close();
			return peso;

		} catch (SQLException e) {
			e.printStackTrace();
			return 0;
		}
	}
	
	public List<Avvistamento> getAvvistamentiUtili(String s1, String s2, String s3, String s4, String stagione) {

		String sql = "SELECT * from avvistamenti WHERE"
				+ "(specie=? OR specie=? OR specie=? OR specie=? OR branco=?) AND stagione=?";
		List<Avvistamento> result = new ArrayList<>();
		Connection conn = DBconnect.getConnection();

		try {
			PreparedStatement st = conn.prepareStatement(sql);
			st.setString(1, s1);
			st.setString(2, s2);
			st.setString(3, s3);
			st.setString(4, s4);
			st.setString(5, "migrazione");
			st.setString(6, stagione);
			
			ResultSet res = st.executeQuery();
			while (res.next()) {
				
				Avvistamento avv = new Avvistamento(res.getInt("id"), res.getString("specie"),
						res.getString("tratta"), res.getInt("aggressione"), res.getString("branco"),res.getInt("numerosità branco"));
				
				LocalTime orario = res.getTimestamp("orario").toLocalDateTime().toLocalTime();
				avv.setOrario(orario);
				
				if(avv.getBranco().equals("si")) {
					if(avv.getSpecie().equals(s1))
						avv.setPunteggio(6.0*1.5);
					if(avv.getSpecie().equals(s2))
						avv.setPunteggio(4.0*1.5);
					if(avv.getSpecie().equals(s3))
						avv.setPunteggio(2.5*1.5);
					if(avv.getSpecie().equals(s4))
						avv.setPunteggio(1.0*1.5);
				}
				
				else if(res.getString("branco").equals("no")) {
					if(avv.getSpecie().equals(s1))
						avv.setPunteggio(6.0);
					if(avv.getSpecie().equals(s2))
						avv.setPunteggio(4.0);
					if(avv.getSpecie().equals(s3))
						avv.setPunteggio(2.5);
					if(avv.getSpecie().equals(s4))
						avv.setPunteggio(1.0);
				}
				
				else if(avv.getSpecie().equals("migrazione"))
					avv.setPunteggio(15.0);
				
				else
					avv.setPunteggio(0.0);
				
				result.add(avv);
				
			}
			conn.close();
			return result;

		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public List<Avvistamento> getAvvistamentiUtili(String stagione) {
		String sql = "SELECT * from avvistamenti WHERE stagione=?";
		List<Avvistamento> result = new ArrayList<>();
		Connection conn = DBconnect.getConnection();

		try {
			PreparedStatement st = conn.prepareStatement(sql);
			st.setString(1, stagione);
			
			ResultSet res = st.executeQuery();
			while (res.next()) {
				
				Avvistamento avv = new Avvistamento(res.getInt("id"), res.getString("specie"),
					res.getString("tratta"), res.getInt("aggressione"), res.getString("branco"), res.getInt("numerosità branco"));
				
				LocalTime orario = res.getTimestamp("orario").toLocalDateTime().toLocalTime();
				avv.setOrario(orario);
				
				if(avv.getBranco().equals("si"))
					avv.setPunteggio(6*1.5);
				else if(avv.getBranco().equals("no"))
					avv.setPunteggio(6);
				else if(avv.getBranco().equals("migrazione"))
					avv.setPunteggio(15);
				
				result.add(avv);
				
			}
			conn.close();
			return result;

		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}

	public List<String> getSpecie() {
		String sql = "SELECT distinct(specie) FROM avvistamenti";
		List<String> result = new ArrayList<>();
		Connection conn = DBconnect.getConnection();

		try {
			PreparedStatement st = conn.prepareStatement(sql);
			
			ResultSet res = st.executeQuery();
			while (res.next()) {
				result.add(res.getString("specie"));
			}
			
			conn.close();
			return result;

		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}


	public List<Avvistamento> getAvvistamentiUtili(String stagione, Map<String,Double> animali) {
		List<Avvistamento> result = new ArrayList<Avvistamento>();
		
		String sql;
		if(stagione.equals("entrambe"))
			sql = "SELECT * FROM avvistamenti";
		else
			sql = "SELECT * FROM avvistamenti WHERE stagione=?";
		
		Connection conn = DBconnect.getConnection();

		try {
			PreparedStatement st = conn.prepareStatement(sql);
			if(!stagione.equals("entrambe"))
				st.setString(1, stagione);
			ResultSet res = st.executeQuery();
			
			while (res.next()) {
				if(animali.containsKey(res.getString("specie"))) {
					Avvistamento avv = new Avvistamento(res.getInt("id"), res.getString("specie"),
						res.getString("tratta"), res.getInt("aggressione"), res.getString("branco"), res.getInt("numerosità branco"));
						
					result.add(avv);
				}
			}
			
			conn.close();
			return result;

		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}

	

}
