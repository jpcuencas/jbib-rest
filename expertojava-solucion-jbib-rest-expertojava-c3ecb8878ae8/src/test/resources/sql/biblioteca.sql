USE biblioteca;
DELETE FROM Recomendacion;
DELETE FROM Prestamo;
DELETE FROM Ejemplar;
DELETE FROM PrestamoHistorico;
DELETE FROM Multa;
DELETE FROM MultaHistorica;
DELETE FROM Usuario;
DELETE FROM Libro;

INSERT INTO Libro(id, titulo, autor,isbn, numPaginas, portadaURI)
VALUES ('1','Patterns Of Enterprise Application Architecture', 'Martin Fowler',
 '0321127420', '533','0321127420.jpg');
INSERT INTO Libro(id, titulo, autor,isbn, numPaginas, portadaURI)
VALUES ('2','Clean Code', 'Robert C. Martin',
 '0132350882', '288','0132350882.jpg');
INSERT INTO Libro(id, titulo, autor,isbn, numPaginas, portadaURI)
VALUES ('3','Test Driven Development', 'Kent Beck',
 '0321146530', '192','0321146530.jpg');

INSERT INTO Recomendacion(id, libro_id,libroRelacionado_id) VALUES ('1','2', '3');
INSERT INTO Recomendacion(id, libro_id,libroRelacionado_id) VALUES ('2','2', '1');
INSERT INTO Recomendacion(id, libro_id,libroRelacionado_id) VALUES ('3','3', '2');

INSERT INTO Ejemplar(id, codigoEjemplar, fechaAdquisicion, libroId)
VALUES ('1', '001', '2014-10-01', '1');
INSERT INTO Ejemplar(id, codigoEjemplar, fechaAdquisicion, libroId)
VALUES ('2', '002', '2014-10-01', '1');
INSERT INTO Ejemplar(id, codigoEjemplar, fechaAdquisicion, libroId)
VALUES ('3', '001', '2014-11-01', '2');
INSERT INTO Ejemplar(id, codigoEjemplar, fechaAdquisicion, libroId)
VALUES ('4', '001', '2014-11-21', '3');

INSERT INTO Usuario(id,tipo,login, nombre, apellido1, apellido2)
VALUES ('1', 'PROFESOR', 'vicente.casamayor', 'Vicente', 'Casamayor', 'Garcia');
INSERT INTO Usuario(id,tipo,login, nombre, apellido1, apellido2)
VALUES ('2', 'ALUMNO', 'antonio.perez', 'Antonio', 'Perez', 'Sierra');
INSERT INTO Usuario(id,tipo,login, nombre, apellido1, apellido2)
VALUES ('3', 'ALUMNO', 'anabel.garcia', 'Anabel', 'Garcia', 'Sierra');

INSERT INTO Prestamo(id,ejemplar_id,usuario_id,fecha,deberiaDevolverseEl)
VALUES ('2', '4', '2', '2014-12-01', '2014-12-05');
INSERT INTO Prestamo(id,ejemplar_id,usuario_id,fecha,deberiaDevolverseEl)
VALUES ('3', '2', '1', '2014-11-01', '2014-11-30');
INSERT INTO Prestamo(id,ejemplar_id,usuario_id,fecha,deberiaDevolverseEl)
VALUES ('4', '3', '1', '2014-11-01', '2014-11-30');

INSERT INTO Multa(id,desde,hasta,usuario_id)
VALUES ('1','2014-12-02', '2015-12-12', 2)


