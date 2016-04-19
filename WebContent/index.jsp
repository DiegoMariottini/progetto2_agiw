<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<title>BooH</title>
</head>
<body>
<script src="codiceJS/index.js"></script>


	<div
		style="position: absolute; width: 500px; height: 200px; left: 50%; top: 50%; margin-left: -250px; margin-top: -100px; text-align: center;">
		<form onSubmit='return false' name='campoDiRicerca' id='campoDiRicerca' action="ricerca.do" method="post">
			<font face="Times New Roman" size="6" color="GREEN"> BooH
			</br>
			
			
			<input onSubmit='return false' id="query" type="search" name="query" size="40%"><br>
			<% String query=(String) request.getParameter("query"); 
			session.setAttribute("query", query); 
			%>

			<button name='cerca' onclick='SubmitSenzaSpazi(query.value)'>
				cerca
			</button>
			
			</font>
		</form>
	</div>

</body>
</html>