package org.expertojava.jbibrest.servlets;

import org.expertojava.jbibrest.modelo.Usuario;
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

@WebServlet(name="recuperarUsuario", urlPatterns="/recuperarUsuario")
public class RecuperarUsuario extends HttpServlet {

    @Inject
    UsuarioServicio usuarioServicio;

    protected void doPost(HttpServletRequest request,
                          HttpServletResponse response)  throws
            ServletException, IOException {

    }

    protected void doGet(HttpServletRequest request,
                         HttpServletResponse response) throws
    ServletException, IOException {

        String login = request.getParameter("login");
        Usuario usuario = usuarioServicio.recuperarUsuario(login);

        response.setContentType("text/html");

        PrintWriter out = response.getWriter();
        out.println("<!DOCTYPE HTML PUBLIC \"" +
                "-//W3C//DTD HTML 4.0 " +
                "Transitional//EN\">");
        out.println("<HTML>");
        out.println("<BODY>");

        out.println("<p>" + usuario.getId() + " - " + usuario.getLogin() + " "
                     + usuario.getNombre() + " " + usuario.getApellido1() + " "
                     + usuario.getApellido2() + "(" + usuario.getPrestamos().size() + " prestamos activos");

        out.println("</BODY>");
        out.println("</HTML");
    }
}
