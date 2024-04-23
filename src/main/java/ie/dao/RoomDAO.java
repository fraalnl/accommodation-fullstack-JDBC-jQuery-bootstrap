package ie.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.QueryParam;

import ie.tus.ConnectionHelper;
import ie.tus.Room;

public class RoomDAO {

    public List<Room> findAll() {
        List<Room> list = new ArrayList<Room>();
        Connection c = null;
    	String sql = "SELECT * FROM listing ORDER BY name;";
        try {
            c = ConnectionHelper.getConnection();
            Statement s = c.createStatement(); 
            ResultSet rs = s.executeQuery(sql);
            while (rs.next()) {
                list.add(processRow(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
		} finally {
			ConnectionHelper.close(c);
		}
        return list;
    }

    protected Room processRow(ResultSet rs) throws SQLException {
        Room room = new Room();
        
        room.setId(rs.getInt("id"));
        room.setName(rs.getString("name"));
        room.setEmail(rs.getString("email"));
        room.setPhone(rs.getString("phone"));
        room.setAddress(rs.getString("address"));
        room.setEircode(rs.getString("eircode"));
        room.setDistance(rs.getDouble("distance"));
        room.setRoomType(rs.getString("room_type"));
     // match column name in sql(not Room.java); don't need to rerun on server to get the data after correcting
        room.setDurationStay(rs.getString("duration_stay")); 
        room.setRent(rs.getInt("rent"));
        room.setBills(rs.getString("bills"));
        room.setGenderPreference(rs.getString("gender_preference"));
        room.setAddMessage(rs.getString("add_message"));
        room.setImage(rs.getString("image"));
        
        return room; 
    }

	public Room findById(int id) {
		String query = "SELECT * FROM listing WHERE id= ?;";
		Connection connection = null; 
		Room room = null;
		
		try {
			connection = ConnectionHelper.getConnection();
			PreparedStatement ps = connection.prepareStatement(query);
			ps.setInt(1, id);
			
			ResultSet resultSet = ps.executeQuery();
			if(resultSet.next()) {
				room = processRow(resultSet);
			}	
		}catch(Exception e) {
			e.printStackTrace();
		}finally {
			ConnectionHelper.close(connection);
		}		
		return room;
	}

	public List<Room> findByRentAndDistance(
			@QueryParam("minRent") int minRent, // good practice to use QueryParam
			@QueryParam("maxRent") int maxRent, 
			@QueryParam("minDistance") double minDistance, 
			@QueryParam("maxDistance") double maxDistance) {
	    List<Room> list = new ArrayList<>();
	    Connection c = null;
	    PreparedStatement ps = null;
	    String sql = "SELECT * FROM listing WHERE rent >= ? AND rent <= ? AND distance >= ? AND distance <= ?";
//As it stands, both maxRent and maxDistance must be provided. No value means 0
	    try {
	        c = ConnectionHelper.getConnection();
	        ps = c.prepareStatement(sql);
	        ps.setInt(1, minRent);
	        ps.setInt(2, maxRent);
	        ps.setDouble(3, minDistance);
	        ps.setDouble(4, maxDistance);
	        
	        ResultSet rs = ps.executeQuery();
	        
	        while (rs.next()) {
	            list.add(processRow(rs));
	        }
	    } catch (SQLException e) {
	        e.printStackTrace();
	    } finally {
	        ConnectionHelper.close(c);
	        if (ps != null) {
	            try {
	                ps.close();
	            } catch (SQLException e) {
	                e.printStackTrace();
	            }
	        }
	    }
	    
	    return list;
	}

	public Room createRoom(Room room) {
		// As it stands, images are not handled, but may be developed in the future
		String query = "INSERT INTO `accommodation`.`listing`\r\n"
				+ "(`name`, `email`, `phone`, `address`, `eircode`, `distance`, `room_type`,"
				+ " `duration_stay`, `rent`, `bills`, `gender_preference`, `add_message`, `image`)\r\n"
				+ "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?);";
		Connection connection = null;
		
		try {
			connection = ConnectionHelper.getConnection();
			PreparedStatement ps = connection.prepareStatement(query);
			
			ps.setString(1, room.getName());
			ps.setString(2, room.getEmail());
			ps.setString(3, room.getPhone());
			ps.setString(4, room.getAddress());
			ps.setString(5, room.getEircode());
			ps.setDouble(6, room.getDistance());
			ps.setString(7, room.getRoomType());
			ps.setString(8, room.getDurationStay());
			ps.setInt(9, room.getRent());
			ps.setString(10, room.getBills());
			ps.setString(11, room.getGenderPreference());
			ps.setString(12, room.getAddMessage());
			ps.setString(13, room.getImage());
			
			ps.executeUpdate();
			ResultSet resultSet = ps.getGeneratedKeys();
			resultSet.next();
			
			room.setId(resultSet.getInt(1));//setId since id is auto_increment
			
			if(resultSet.next()) {
				room = processRow(resultSet);
			}	
		}catch(Exception e) {
			e.printStackTrace();
		}finally {
			ConnectionHelper.close(connection);
		}		
		return room;	
	}

	public Room update(Room room) {
		Connection c = null;
		try {
			c = ConnectionHelper.getConnection();
			PreparedStatement ps = c.prepareStatement("UPDATE listing SET name=?, "
					+ "email=?, phone=?, address=?, eircode=?, distance=?, room_type=?, duration_stay=?,"
					+ " rent=?, bills=?, gender_preference=? , add_message=?  , image=? WHERE id=?");
			
			ps.setString(1, room.getName());
			ps.setString(2, room.getEmail());
			ps.setString(3, room.getPhone());
			ps.setString(4, room.getAddress());
			ps.setString(5, room.getEircode());
			ps.setDouble(6, room.getDistance());
			ps.setString(7, room.getRoomType());
			ps.setString(8, room.getDurationStay());
			ps.setInt(9, room.getRent());
			ps.setString(10, room.getBills());
			ps.setString(11, room.getGenderPreference());
			ps.setString(12, room.getAddMessage());
			ps.setString(13, room.getImage());
			ps.setInt(14, room.getId());//setId; after correcting this line you should restart server
			
			ps.executeUpdate();
			
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			ConnectionHelper.close(c);
		}
		return room;		
	}
	
	public boolean remove(int id) {
		Connection c = null;
		try {
			c = ConnectionHelper.getConnection();
			PreparedStatement ps = c.prepareStatement("DELETE FROM listing WHERE id=?");
			ps.setInt(1, id);
			int count = ps.executeUpdate();
			return count == 1;
			
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException(e); 
		} finally {
			ConnectionHelper.close(c);
		}		
	}   
}