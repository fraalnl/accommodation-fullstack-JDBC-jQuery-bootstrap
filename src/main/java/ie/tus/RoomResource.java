package ie.tus;

import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import ie.dao.RoomDAO;


@Path("/rooms")
public class RoomResource {

	RoomDAO dao = new RoomDAO();
	
	@GET
//This annotation instructs the server to marshal the returned Java objects into JSON
	@Produces({ MediaType.APPLICATION_JSON })
	public List<Room> findAll() {
		System.out.println("findAll");
		return dao.findAll(); 
	}
	
	@GET
	@Path("{roomId}")
	@Produces({ MediaType.APPLICATION_JSON})
	public Response findRoomById(@PathParam("roomId") int id) {
		Room room = dao.findById(id);
		return Response
				.status(200)
				.entity(room)
				.header("test-content", "example_of_header")
				.build();	
	}
	
	@GET
    @Path("/search")
    @Produces(MediaType.APPLICATION_JSON)
    public Response findByRentAndDistance(@QueryParam("minRent") int minRent,
                                          @QueryParam("maxRent") int maxRent,
                                          @QueryParam("minDistance") double minDistance,
                                          @QueryParam("maxDistance") double maxDistance) {
        List<Room> rooms = dao.findByRentAndDistance(minRent, maxRent, minDistance, maxDistance);

        return Response
				.status(200)
				.entity(rooms)
				.build();
    }	
	
	@POST
	@Consumes({MediaType.APPLICATION_JSON})
	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	public Response addRoom(Room room) {
		Room createdRoom = dao.createRoom(room);
		
		return Response
		.status(201) 
		.entity(createdRoom) // the room object created by user's input is returned by server as well
		.build();
	}
	
	@PUT @Path("{id}")
	@Consumes({MediaType.APPLICATION_JSON})
	@Produces({MediaType.APPLICATION_JSON})
	public Response update(Room room) { 
		dao.update(room);
		return Response.status(201).entity(room).build();
	}
	
	@DELETE @Path("{id}")
	@Produces({MediaType.APPLICATION_JSON})
	public Response remove(@PathParam("id") int id) {		
		dao.remove(id);
		return Response.status(204).build();	
	}
}