package org.expertojava.jbibrest.servlets;

import org.expertojava.jbibrest.modelo.Prestamo;
import org.expertojava.jbibrest.servicio.UsuarioServicio;

import javax.ejb.EJB;
import javax.inject.Inject;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

@WebServlet(name="realizarPrestamosLibro", urlPatterns="/realizarPrestamoLibro")
public class RealizarPrestamosLibro extends HttpServlet {

    @Inject
    UsuarioServicio usuarioServicio;

    protected void doPost(HttpServletRequest request,
                          HttpServletResponse response)  throws
            ServletException, IOException {

    }

    protected void doGet(HttpServletRequest request,
                         HttpServletResponse response) throws
    ServletException, IOException {

        Long usuarioId = Long.parseLong(request.getParameter("usuario"));
        Long ejemplarId = Long.parseLong(request.getParameter("ejemplar"));
        Prestamo prestamo = usuarioServicio.realizarPrestamo(usuarioId, ejemplarId);

        response.setContentType("text/html");

        PrintWriter out = response.getWriter();
        out.println("<!DOCTYPE HTML PUBLIC \"" +
                "-//W3C//DTD HTML 4.0 " +
                "Transitional//EN\">");
        out.println("<HTML>");
        out.println("<BODY>");
        out.println("<p> Prestado el ejemplar. Fecha de devolucion: " +
           prestamo.getDeberiaDevolverseEl() + "</p>");
        out.println("</BODY>");
        out.println("</HTML");
    }
}
