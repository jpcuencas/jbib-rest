package org.expertojava.jbibrest.servlets;

import org.expertojava.jbibrest.modelo.Libro;
import org.expertojava.jbibrest.servicio.LibroServicio;

import javax.ejb.EJB;
import javax.inject.Inject;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

@WebServlet(name="listaLibrosTitulo", urlPatterns="/listaLibrosTitulo")
public class ListadoLibrosTitulo extends HttpServlet {

    @Inject
    LibroServicio libroServicio;

    protected void doPost(HttpServletRequest request,
                          HttpServletResponse response)  throws
            ServletException, IOException {

    }

    protected void doGet(HttpServletRequest request,
                         HttpServletResponse response) throws
    ServletException, IOException {


        String titulo = request.getParameter("titulo");


        List<Libro> listaLibros
                = libroServicio.buscaLibrosTituloContiene(titulo);

        response.setContentType("text/html");

        PrintWriter out = response.getWriter();
        out.println("<!DOCTYPE HTML PUBLIC \"" +
                "-//W3C//DTD HTML 4.0 " +
                "Transitional//EN\">");
        out.println("<HTML>");
        out.println("<BODY>");
        out.println("<ul>");
        for (Libro libro : listaLibros ) {
            out.println("<li>");
            out.println(libro.getId() + " - " + libro.getAutor() + " : " + libro.getTitulo());
            out.println("</li>");
        }
        out.println("</ul>");
        out.println("</BODY>");
        out.println("</HTML");
    }
}
