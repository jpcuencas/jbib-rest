package org.expertojava.jbibrest.servlets;

import org.expertojava.jbibrest.modelo.Libro;
import org.expertojava.jbibrest.modelo.Recomendacion;
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

@WebServlet(name="listaRecomendaciones", urlPatterns="/listaRecomendaciones")
public class ListaRecomendaciones extends HttpServlet {

    @Inject
    LibroServicio libroServicio;

    protected void doPost(HttpServletRequest request,
                          HttpServletResponse response)  throws
            ServletException, IOException {

    }

    protected void doGet(HttpServletRequest request,
                         HttpServletResponse response) throws
    ServletException, IOException {

        Long libroId = Long.valueOf(request.getParameter("libroId"));
        int nRec = Integer.valueOf(request.getParameter("numRecomendaciones"));

        List<Recomendacion> listaRecomendaciones
                = libroServicio.listaRecomendaciones(libroId, nRec);
        Libro libro = libroServicio.buscaLibroPorId(libroId);

        response.setContentType("text/html");

        PrintWriter out = response.getWriter();
        out.println("<!DOCTYPE HTML PUBLIC \"" +
                "-//W3C//DTD HTML 4.0 " +
                "Transitional//EN\">");
        out.println("<HTML>");
        out.println("<BODY>");

        out.println("<p> El libro " + libro.getTitulo() +
                " se ha prestado junto con: </p>");
        out.println("<ul>");
        for (Recomendacion recomendacion : listaRecomendaciones ) {
            out.println("<li>");
            out.println(recomendacion.getLibroRelacionado().getTitulo());
            out.println("</li>");
        }
        out.println("</ul>");
        out.println("</BODY>");
        out.println("</HTML");
    }
}
