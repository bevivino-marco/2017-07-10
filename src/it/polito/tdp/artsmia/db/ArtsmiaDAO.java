package it.polito.tdp.artsmia.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

import it.polito.tdp.artsmia.model.ArtObject;
import it.polito.tdp.artsmia.model.Ex_Obj;

public class ArtsmiaDAO {

	public List<ArtObject> listObjects() {
		
		String sql = "SELECT * from objects";
		List<ArtObject> result = new ArrayList<>();
		Connection conn = DBConnect.getConnection();

		try {
			PreparedStatement st = conn.prepareStatement(sql);
			ResultSet res = st.executeQuery();
			while (res.next()) {

				ArtObject artObj = new ArtObject(res.getInt("object_id"), res.getString("classification"), res.getString("continent"), 
						res.getString("country"), res.getInt("curator_approved"), res.getString("dated"), res.getString("department"), 
						res.getString("medium"), res.getString("nationality"), res.getString("object_name"), res.getInt("restricted"), 
						res.getString("rights_type"), res.getString("role"), res.getString("room"), res.getString("style"), res.getString("title"));
				
				result.add(artObj);
			}
			conn.close();
			return result;
			
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}

	public int mostreInComune(ArtObject o1, ArtObject o2) {
		String sql = "SELECT COUNT(*) AS cont " + 
				"FROM  exhibition_objects eo1 ,objects o1 ,exhibition_objects eo2, objects o2 " + 
				"WHERE eo1.exhibition_id=eo2.exhibition_id AND o1.object_id=eo1.object_id AND o2.object_id=eo2.object_id " + 
				"AND o1.object_id= ? AND o2.object_id=?  ";
		
		Connection conn = DBConnect.getConnection();

		try {
			int c=0;
			PreparedStatement st = conn.prepareStatement(sql);
			st.setInt(1, o1.getId());
			st.setInt(2, o2.getId());
			ResultSet res = st.executeQuery();
			
			if (res.next()) {
				
				c=res.getInt("cont");
				
			}
			conn.close();
			return c;
			
		} catch (SQLException e) {
			e.printStackTrace();
			return 0;
		}
	}

	public List <Ex_Obj> getRelazioni() {
		String sql = "SELECT o1.object_id , o2.object_id, COUNT( eo1.exhibition_id) AS c " + 
				"FROM  exhibition_objects eo1 ,objects o1 ,exhibition_objects eo2, objects o2 " + 
				"WHERE eo1.exhibition_id=eo2.exhibition_id AND o1.object_id!= o2.object_id AND o1.object_id=eo1.object_id AND o2.object_id=eo2.object_id " + 
				"GROUP  BY o1.object_id , o2.object_id  ";
		
		Connection conn = DBConnect.getConnection();

		try {
			int c=0;
			int o1 =0;
			int o2=0;
			List <Ex_Obj> lista = new LinkedList <>();
			PreparedStatement st = conn.prepareStatement(sql);
			
			ResultSet res = st.executeQuery();
			
			while (res.next()) {
				o1=res.getInt("o1.object_id");
				o2=res.getInt("o2.object_id");
				c=res.getInt("c");
				Ex_Obj eo = new Ex_Obj (o1,o2,c);
				lista.add(eo);
				
			}
			conn.close();
			return lista;
			
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}
	
}
