package org.expertojava.jbibrest.servlets;


import org.expertojava.jbibrest.modelo.Libro;
import org.expertojava.jbibrest.persistencia.LibroDao;

import javax.annotation.Resource;
import javax.inject.Inject;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.transaction.SystemException;
import javax.transaction.UserTransaction;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

@WebServlet(name="holamundo", urlPatterns="/holamundo")
public class HolaMundo extends HttpServlet {

    @Inject
    LibroDao libroDao;
    @Resource
    UserTransaction tx;

    protected void doPost(HttpServletRequest request,
                          HttpServletResponse response)  throws
            ServletException, IOException {

    }

    protected void doGet(HttpServletRequest request,
                         HttpServletResponse response) throws
            ServletException, IOException {
        response.setContentType("text/html");
        String isbnStr = request.getParameter("isbn");

        // Comprobamos entradas no nulas

        PrintWriter out = response.getWriter();

        if (isbnStr == null || isbnStr.equals("")) {
            List<Libro> libros = libroDao.listAllLibros();
            out.println("<HTML>");
            out.println("<BODY>");
            out.println("<h1>Libros en la base de datos</h1>");
            out.println("<ul>");
            for (Libro libro : libros) {
                out.println("<li> " + libro.getTitulo() + "</li>");
            }
            out.println("</ul>");
            out.println("</BODY>");
            out.println("</HTML");
        }

        else {
            // Llamamos al modelo para construir la respuesta
            Libro libro = new Libro(isbnStr);
            libro.setAutor("Kent Beck");
            libro.setTitulo("Extreme Programming Explained");

            try {
                tx.begin();
                libro = libroDao.create(libro);
                tx.commit();
            } catch (Exception ex) {
                try {
                    tx.rollback();
                } catch (SystemException e) {
                    e.printStackTrace();
                }
            }
            out.println("<!DOCTYPE HTML PUBLIC \"" +
                    "-//W3C//DTD HTML 4.0 " +
                    "Transitional//EN\">");
            out.println("<HTML>");
            out.println("<BODY>");
            out.println("<h1>Creado un nuevo libro</h1>");
            out.println("<p>");
            out.println(libro.toString());
            out.println("</p>");
            out.println("</BODY>");
            out.println("</HTML");
        }
    }
}
