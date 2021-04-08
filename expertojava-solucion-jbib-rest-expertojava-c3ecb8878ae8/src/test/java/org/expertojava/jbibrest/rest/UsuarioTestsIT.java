package org.expertojava.jbibrest.rest;


import org.dbunit.IDatabaseTester;
import org.dbunit.JdbcDatabaseTester;
import org.dbunit.database.DatabaseConfig;
import org.dbunit.database.IDatabaseConnection;
import org.dbunit.dataset.IDataSet;
import org.dbunit.ext.mysql.MySqlDataTypeFactory;
import org.dbunit.util.fileloader.DataFileLoader;
import org.dbunit.util.fileloader.FlatXmlDataFileLoader;
import org.expertojava.jbibrest.rest.dto.PrestamoItem;
import org.expertojava.jbibrest.rest.dto.UsuarioDetalle;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import javax.json.Json;
import javax.json.JsonObject;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import static uk.co.datumedge.hamcrest.json.SameJSONAs.sameJSONAs;

public class UsuarioTestsIT {
    private Client client;
    private IDatabaseTester databaseTester;

    /* Borra los datos de las tablas mensajes y usuarios
       Inserta los datos contenidos en el fichero /src/test/resources/fichero_datos.xml
     */
    public void inicializamos_BD_con_datos(String ficheroDatos) {
        try {
            DataFileLoader loader = new FlatXmlDataFileLoader();
            IDataSet dataSet = loader.load(ficheroDatos);

            this.databaseTester.setDataSet(dataSet);

            //Borramos los datos de las tablas e insertamos los de fichero_datos.xml
            this.databaseTester.onSetup();
        } catch (Exception ex) {
            ex.printStackTrace();
            fail("Excepción al inicializar la Base de datos para los tests");
        }
    }

    @Before
    public void initClient()
    {
        this.client = ClientBuilder.newClient();
        try {
            this.databaseTester = new JdbcDatabaseTester("com.mysql.jdbc.Driver",
                    "jdbc:mysql://localhost:3306/biblioteca", "root", "expertojava")
            { //para que no aparezca el Warning sobreescribimos el método getConnection()
                @Override
                public IDatabaseConnection getConnection() throws Exception {
                    IDatabaseConnection connection = super.getConnection();
                    connection.getConfig().setProperty(
                            DatabaseConfig.PROPERTY_DATATYPE_FACTORY,
                            new MySqlDataTypeFactory());
                    return connection;
                }
            };
            inicializamos_BD_con_datos("/dbunit/dataset3-rest.xml");
        } catch (Exception ex) {
            ex.printStackTrace();
            fail("Excepción al acceder a la Base de datos para los tests");
        }

        //inicializamosDatos("/init-bd-rest.xml");
    }

    @After
    public void closeClient()
    {
        this.client.close();
    }

    @Test
    public void consultarUsuarioRegistrado() throws Exception {

        String loginEsperado = "vicente.casamayor";
        String mailEsperado = null;
        UsuarioDetalle detalle_real = this.client.target("http://localhost:8080/jbib-rest/api/usuarios/vicente.casamayor")
                .request(MediaType.APPLICATION_JSON)
                .header("Authorization", "Basic dmljZW50ZS5jYXNhbWF5b3I6dmljZW50ZQ==")
                .get(UsuarioDetalle.class);

        Assert.assertEquals(loginEsperado, detalle_real.login);
        Assert.assertNull(detalle_real.eMail);
    }

    @Test
    public void consultarUsuarioNORegistrado() throws Exception {
        String loginEsperado = "vicente.casamayor";
        String mailEsperado = null;

        JsonObject respuesta_esperada =
                Json.createObjectBuilder()
                        .add("status", "Unauthorized")
                        .add("code", 401)
                        .add("message", "El usuario no es el logueado")
                        .build();

        Response respuesta = this.client
                .target("http://localhost:8080/jbib-rest/api/usuarios/roberto.garcia")
                .request(MediaType.APPLICATION_JSON)
                .header("Authorization", "Basic dmljZW50ZS5jYXNhbWF5b3I6dmljZW50ZQ==")
                .get();

        String respuestaReal= respuesta.readEntity(JsonObject.class).toString();

        //Comprobamos que recibimos el mensaje de error correcto
        Assert.assertThat(respuestaReal,
                sameJSONAs(respuesta_esperada.toString())
                        .allowingExtraUnexpectedFields()
        );
    }

    @Test
    public void solicitarPrestamoDevuelveNuevoPrestamo() throws Exception {
        // Anabel Garcia solicita el préstamo del libro 1

        JsonObject bodyEntity =
                Json.createObjectBuilder()
                        .add("id", 1)
                        .build();
        Response response = this.client.target("http://localhost:8080/jbib-rest/api/usuarios/3/prestamos")
                .request(MediaType.APPLICATION_JSON)
                .header("Authorization", "Basic YW5hYmVsLmdhcmNpYTphbmFiZWw=")
                .post(Entity.json(bodyEntity));
        System.out.println("Resultado del prestamo: " + response.getStatus());
        assertTrue(response.getStatusInfo() == Response.Status.CREATED);

        PrestamoItem prestamoCreado = (PrestamoItem) response.getEntity();

        response.close();

    }

    @Test
    public void devolverPrestamo() throws Exception {
        // Vicente Casamayor solicita devolver el ejemplar 1 prestado

        JsonObject bodyEntity =
                Json.createObjectBuilder()
                        .add("id", 2)
                        .build();
        Response response = this.client.target("http://localhost:8080/jbib-rest/api/usuarios/1/devoluciones")
                .request(MediaType.APPLICATION_JSON)
                .header("Authorization", "Basic dmljZW50ZS5jYXNhbWF5b3I6dmljZW50ZQ==")
                .post(Entity.json(bodyEntity));
        System.out.println("Resultado de la devolucion: " + response.getStatus());
        assertTrue(response.getStatusInfo() == Response.Status.OK);

        JsonObject respuesta_real = response.readEntity(JsonObject.class);

        JsonObject bodyEntityResponse =
                Json.createObjectBuilder()
                        .add("resultado", "DEVOLUCION_FUERA_DE_PLAZO")
                        .build();

        assertTrue(bodyEntityResponse.equals(respuesta_real));

    }
}
