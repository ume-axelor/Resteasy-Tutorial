package com.example.resource;

import com.example.Student;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.Map;

@Path("/hello")
public class HelloResource {

    private static final EntityManagerFactory emf = Persistence.createEntityManagerFactory("rdemo");

    @POST
    @Path("/addStudent")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response addStudent(Map<String, Object> data) {
        EntityManager em = emf.createEntityManager();
        try {
            em.getTransaction().begin();

            String id = (String) data.get("id");
            String name = (String) data.get("name");

            Student student = new Student();
            student.setId(id);
            student.setName(name);

            em.persist(student);
            em.getTransaction().commit();

            String response = "Student added with ID: " + student.getId();
            return Response.ok().entity(response).build();
        }
        catch(ProcessingException e){
        	e.printStackTrace();
        	String response = "Student not added";
        	return Response.ok().entity(response).build();
        }
        
        finally {
            em.close();
        }
    }
    

    @GET
    @Path("/getStudent/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getStudent(@PathParam("id") String id) {
        EntityManager em = emf.createEntityManager();
        try {
            Student student = em.find(Student.class, id);
            if (student != null) {
                return Response.ok().entity(student).build();
            } else {
                return Response.status(Response.Status.NOT_FOUND).entity("Student not found").build();
            }
        } finally {
            em.close();
        }
    }

    @PUT
    @Path("/updateStudent/{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response updateStudent(@PathParam("id") String id, Student updatedStudent) {
        EntityManager em = emf.createEntityManager();
        try {
            em.getTransaction().begin();
            Student existingStudent = em.find(Student.class, id);
            if (existingStudent != null) {
                existingStudent.setName(updatedStudent.getName());
                em.getTransaction().commit();
                return Response.ok().entity("Student updated successfully").build();
            } else {
                return Response.status(Response.Status.NOT_FOUND).entity("Student not found").build();
            }
        } catch (Exception e) {
            e.printStackTrace();
            return Response.serverError().entity("Error updating student").build();
        } finally {
            em.close();
        }
    }

    @DELETE
    @Path("/deleteStudent/{id}")
    public Response deleteStudent(@PathParam("id") String id) {
        EntityManager em = emf.createEntityManager();
        try {
            em.getTransaction().begin();
            Student student = em.find(Student.class, id);
            if (student != null) {
                em.remove(student);
                em.getTransaction().commit();
                return Response.ok().entity("Student deleted successfully").build();
            } else {
                return Response.status(Response.Status.NOT_FOUND).entity("Student not found").build();
            }
        } catch (Exception e) {
            e.printStackTrace();
            return Response.serverError().entity("Error deleting student").build();
        } finally {
            em.close();
        }
    }
    
}
