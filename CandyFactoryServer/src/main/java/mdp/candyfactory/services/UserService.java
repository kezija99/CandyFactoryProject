package mdp.candyfactory.services;

import java.io.FileNotFoundException;
import java.io.IOException;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import mdp.candyfactory.model.User;
import mdp.candyfactory.repositories.UsersRepository;

@Path("/users")
public class UserService {
	
	UsersRepository usersRepo = new UsersRepository();
	
	@POST
	@Path("/register")
	@Consumes(MediaType.APPLICATION_JSON)
	public Response register(User user) throws FileNotFoundException, IOException {
		if(usersRepo.register(user))
			return Response.status(200).entity(user).build();
		else
			return Response.status(500).entity("Error adding user.").build();
	}
	
	@POST
	@Path("/login/{user}/{pass}")
	public Response login(@PathParam("user") String username, @PathParam("pass") String password) throws FileNotFoundException, IOException {
		if(usersRepo.login(username, password))
			return Response.status(200).entity("Success.").build();
		else
			return Response.status(500).entity("Error logging in.").build();
	}
}
