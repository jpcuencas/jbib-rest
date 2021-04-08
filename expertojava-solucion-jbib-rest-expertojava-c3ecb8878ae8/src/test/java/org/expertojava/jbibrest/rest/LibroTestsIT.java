package org.expertojava.jbibrest.rest;

import org.dbunit.IDatabaseTester;
import org.dbunit.JdbcDatabaseTester;
import org.dbunit.database.DatabaseConfig;
import org.dbunit.database.IDatabaseConnection;
import org.dbunit.dataset.IDataSet;
import org.dbunit.ext.mysql.MySqlDataTypeFactory;
import org.dbunit.util.fileloader.DataFileLoader;
import org.dbunit.util.fileloader.FlatXmlDataFileLoader;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import javax.json.Json;
import javax.json.JsonArray;
import javax.ws.rs.ServerErrorException;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import static org.junit.Assert.fail;
import static uk.co.datumedge.hamcrest.json.SameJSONAs.sameJSONAs;


public class LibroTestsIT {
    private Client client;
    private IDatabaseTester databaseTester;

    /* Borra los datos de las tablas mensajes y usuarios
       Inserta los datos contenidos en el fichero /src/test/resources/fichero_datos.xml
     */
    public void inicializamosDatos(String ficheroDatos) {
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

            inicializamosDatos("/dbunit/dataset3-rest.xml");

        } catch (Exception ex) {
            ex.printStackTrace();
            fail("Excepción al acceder a la Base de datos para los tests");
        }

    }

    @After
    public void closeClient()
    {
        this.client.close();
    }

    @Test
    public void consultamosTodosLosLibros() throws Exception {

        //Recuperamos los datos de los usuarios utilizando JsonArray
        JsonArray json_array = client.target("http://localhost:8080/jbib-rest/api/libros/")
                .request(MediaType.APPLICATION_JSON)
                .get(JsonArray.class);
        String jsonStringReal = json_array.toString();

        //Comprobamos que hay tres libros en la biblioteca
        Assert.assertEquals(3, json_array.size());

        JsonArray jsonIsbnsLibrosEsperados =
                Json.createArrayBuilder()
                        .add(Json.createObjectBuilder().add("isbn", "0321127420"))
                        .add(Json.createObjectBuilder().add("isbn", "0132350882"))
                        .add(Json.createObjectBuilder().add("isbn", "0321146530"))
                        .build();

        //Comprobamos que los isbns de los libros de la biblioteca son correctos
        Assert.assertThat(jsonStringReal,
                sameJSONAs(jsonIsbnsLibrosEsperados.toString())
                        .allowingExtraUnexpectedFields()
                        .allowingAnyArrayOrdering());
    }

    @Test
    public void consultaDeLibrosPorAutor() throws Exception {

        //Recuperamos los datos de los usuarios utilizando JsonArray
        JsonArray json_array = client.target("http://localhost:8080/jbib-rest/api/libros?autor=Robert%20C.%20Martin")
                .request(MediaType.APPLICATION_JSON)
                .get(JsonArray.class);
        String jsonStringReal = json_array.toString();

        //Comprobamos que hay un libro del autor Robert C. Martin
        Assert.assertEquals(1, json_array.size());

        JsonArray jsonIsbnsLibrosEsperados =
                Json.createArrayBuilder()
                        .add(Json.createObjectBuilder().add("isbn", "0132350882"))
                        .build();

        //Comprobamos que los isbns de los libros devueltos son correctos
        Assert.assertThat(jsonStringReal,
                sameJSONAs(jsonIsbnsLibrosEsperados.toString())
                        .allowingExtraUnexpectedFields()
                        .allowingAnyArrayOrdering());
    }

    @Test
    public void consultaDeLibrosPorAutorYTitulo() throws Exception {

        //Recuperamos los datos de los usuarios utilizando JsonArray
        try {
            JsonArray json_array = client.target("http://localhost:8080/jbib-rest/api/libros?autor=Martin%20Fowler&titulo=titulo")
                    .request(MediaType.APPLICATION_JSON)
                    .get(JsonArray.class);
            String json_string_real = json_array.toString();
            fail();
        } catch (ServerErrorException error){
            int codigo = error.getResponse().getStatus();
            Response.StatusType mensaje = error.getResponse().getStatusInfo();
            Assert.assertEquals(501,codigo);
            Assert.assertEquals(Response.Status.NOT_IMPLEMENTED, mensaje);

        }
    }


}
