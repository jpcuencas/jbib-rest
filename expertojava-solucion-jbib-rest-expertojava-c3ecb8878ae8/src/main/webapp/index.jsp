<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.0 Transitional//EN">
<html>
<head>
    <title>Start Page</title>
    <meta http-equiv="Content-Type" content="text/html">
    <meta charset="UTF-8">
</head>
<body>
<h1>Pruebas métodos de negocio</h1>
<hr/>
<p><a href="<%=request.getContextPath()%>/listaLibros">Listado de libros</a></p>
<hr/>
<p>Listado de recomendaciones</p>
<form action="<%=request.getContextPath()%>/listaRecomendaciones">
    <p>Identificador del libro: <input type="text" name="libroId"></p>
    <p>Número de recomendaciones: <input type="text" name="numRecomendaciones"></p>
    <input type="submit" value="Enviar">
</form>
<hr/>
<p>Recuperar usuario</p>
<form action="<%=request.getContextPath()%>/recuperarUsuario">
    <p>Login de usuario: <input type="text" name="login"></p>
    <input type="submit" value="Recuperar">
</form>
<hr/>
<p>Listado libros por título</p>
<form action="<%=request.getContextPath()%>/listaLibrosTitulo">
    <p>Titulo del libro: <input type="text" name="titulo"></p>
    <input type="submit" value="Listar">
</form>
<hr/>
<p>Listado libros por autor</p>
<form action="<%=request.getContextPath()%>/listaLibrosAutor">
    <p>Autor: <input type="text" name="autor"></p>
    <input type="submit" value="Listar">
</form>
<hr/>
<p>Listado pŕestamos Usuario</p>
<form action="<%=request.getContextPath()%>/listaPrestamosUsuario">
    <p>Identificador de usuario: <input type="text" name="usuario"></p>
    <input type="submit" value="Listar">
</form>
<hr/>
<p>Realizar préstamo</p>
<form action="<%=request.getContextPath()%>/realizarPrestamoLibro">
    <p>Identificador de usuario: <input type="text" name="usuario"></p>
    <p>Identificador de ejemplar: <input type="text" name="ejemplar"></p>
    <input type="submit" value="Prestar">
</form>
<hr/>
</body>
</html>
