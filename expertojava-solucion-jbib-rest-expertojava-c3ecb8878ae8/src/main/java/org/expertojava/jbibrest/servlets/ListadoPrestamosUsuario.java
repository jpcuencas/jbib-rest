package org.expertojava.jbibrest.servlets;

import org.expertojava.jbibrest.modelo.Libro;
import org.expertojava.jbibrest.modelo.Prestamo;
import org.expertojava.jbibrest.servicio.LibroServicio;
import org.expertojava.jbibrest.servicio.UsuarioServicio;

import javax.ejb.EJB;
import javax.inject.Inject;
import javax.persistence.Index;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

@WebServlet(name="listaPrestamosUsuario", urlPatterns="/listaPrestamosUsuario")
public class ListadoPrestamosUsuario extends HttpServlet {

    @Inject
    UsuarioServicio usuarioServicio;
    @Inject
    LibroServicio libroServicio;

    protected void doPost(HttpServletRequest request,
                          HttpServletResponse response)  throws
            ServletException, IOException {

    }

    protected void doGet(HttpServletRequest request,
                         HttpServletResponse response) throws
    ServletException, IOException {

        Long usuarioId = Long.parseLong(request.getParameter("usuario"));
        List<Prestamo> prestamos = usuarioServicio.prestamosActivos(usuarioId);

        response.setContentType("text/html");

        PrintWriter out = response.getWriter();
        out.println("<!DOCTYPE HTML PUBLIC \"" +
                "-//W3C//DTD HTML 4.0 " +
                "Transitional//EN\">");
        out.println("<HTML>");
        out.println("<BODY>");
        out.println("<ul>");
        Libro libro;
        for (Prestamo prestamo : prestamos ) {
            libro = libroServicio.buscaLibroPorId(prestamo.getEjemplar().getLibroId());
            out.println("<li>");
            out.println(prestamo.getEjemplar().getId() + " - "
                    + libro.getTitulo() + " - "
                    + prestamo.getDeberiaDevolverseEl());
            out.println("</li>");
        }
        out.println("</ul>");
        out.println("</BODY>");
        out.println("</HTML");
    }
}
