package org.expertojava.jbibrest.persistencia;

import org.expertojava.jbibrest.modelo.Usuario;

import javax.persistence.NoResultException;
import javax.persistence.TypedQuery;
import java.util.List;

public class UsuarioDao extends Dao<Usuario, Long> {
    String FIND_ALL_USUARIOS = "SELECT u FROM Usuario u";
    String FIND_USUARIO_POR_LOGIN = "SELECT u FROM Usuario u WHERE u.login = :login";

    @Override
    public Usuario find(Long id) {
        return em.find(Usuario.class, id);
    }

    public List<Usuario> listAllUsuarios() {
        TypedQuery<Usuario> query = em.createQuery(FIND_ALL_USUARIOS, Usuario.class);
        return query.getResultList();
    }

    public Usuario findUsuarioPorLogin(String login) {
        Usuario usuario;
        TypedQuery<Usuario> query = em.createQuery(FIND_USUARIO_POR_LOGIN, Usuario.class)
                .setParameter("login", login);
        try {
            usuario = query.getSingleResult();
        } catch (NoResultException ex) {
            usuario = null;
        }
        return usuario;
        }
}
