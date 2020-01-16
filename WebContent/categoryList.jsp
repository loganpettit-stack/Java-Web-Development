<%@ page import="java.util.*"%>
<%@ page import="edu.umsl.math.beans.*"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="ISO-8859-1">
<!-- Latest compiled and minified CSS -->
<link rel="stylesheet"
	href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/css/bootstrap.min.css">

<!-- jQuery library -->
<script
	src="https://ajax.googleapis.com/ajax/libs/jquery/3.2.1/jquery.min.js"></script>

<!-- Latest compiled JavaScript -->
<script
	src="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/js/bootstrap.min.js"></script>

<script type="text/javascript"
	src="https://cdn.mathjax.org/mathjax/latest/MathJax.js?config=TeX-AMS-MML_HTMLorMML">
	
</script>
<script type="text/javascript">
	window.MathJax = {
		tex2jax : {
			inlineMath : [ [ '$', '$' ], [ "\\(", "\\)" ] ],
			processEscapes : true
		}
	};
</script>

<style>
outer {
	width: 100%;
	text-align: center;
	padding: 10 px;
}

.button1 {
	display: inline-block;
	font-size: 15px;
}
</style>

<title>Math Question Bank</title>
</head>
<body>
	<%
		List<Problem> myproblist = (List<Problem>) request.getAttribute("problist");
		List<Category> mycatlist = (List<Category>) request.getAttribute("catlist");
	%>
	<div class="container">
		<div class="row">
			<div class="col-md-offset-2 col-md-8">
				<div id="outer">
					<form name="categoryPage" action="ListCategory" method="post">
						<%
							for (Category cat : mycatlist) {
						%>
						<button class="button1" name="categoryButton"
							value="<%=cat.getCategoryId()%>"><%=cat.getCategoryType()%>
						</button>
						<%
							}
						%>
					</form>
					<form name="listmath" action="listmath" method="get">
						<button class="button1" onclick="window.location.href='listmath'" type="button">All Problems</button>
					</form>
				</div>
				<table width="100%" class="table table-bordered table-striped">
					<%
						for (Problem prob : myproblist) {
					%>
					<tr>
						<td id="pid<%=prob.getPid()%>" width="8%" class="text-center">
							<%=prob.getPid()%>
						</td>
						<td width="84%"><%=prob.getContent()%></td>
						<!-- <td width="8%"></td> -->
						<td width="84%"><%=prob.getCategoryList()%></td>
						<td>
							<form name="categoryForm" action="AssignCategoryServlet"
								method="post">
								<input type="hidden" name="pid" value="<%=prob.getPid()%>">

								<select name="category" id="category">
									<option value="" selected="selected" hidden="hidden"
										disabled="disabled">Please Select</option>
									<%
										for (Category cat : mycatlist) {
									%>

									<option id="cid<%=cat.getCategoryId()%>"
										value="<%=cat.getCategoryId()%>">
										<%=cat.getCategoryType()%>
									</option>

									<%
										}
									%>

								</select>
								<div>
									<button class="button1" type="submit" name="submit">Assign</button>
								</div>
							</form>
						</td>
					</tr>
					<%
						}
					%>
				</table>
				<div>
				<!-- <form action="addCategory" method="post">
					Create new math category:<br> <input type="text"
						name="newCategory" value=""> <br> <br> <input
						type="submit" value="submit">
				</form>
				<br> <br> -->
				<%-- <form action="addMathProblem" method="post">
					Create new math problem:<br>
					 <input type="text" name="newMathProblem" value=""> 
					 <br> 
					 <br>
					  <input type="submit" value="submit">
					  <br>
					   Provide category for math problem 
					   <select name="problemcategory" id="category">
						<option value="" selected="selected" hidden="hidden" disabled="disabled">Please Select</option>
						<%
							for (Category cat : mycatlist) {
						%>
						
						<option id="cid<%=cat.getCategoryId()%>"
							value="<%=cat.getCategoryId()%>">
							<%=cat.getCategoryType()%>
						</option>

						<%
							}
						%>

					</select>
				</form> --%>
				
				<form action="addCategory" method="post">
					Create new math category:<br>
					 <input type="text" name="newCategory" required>
					 <br> 
					 <br> 
					 <input type="submit" value="submit">
				</form>
				<br>
				<p>${param.addCatMessage}</p>
				<br> <br>
				<form action="addMathProblem" method="post">
					Create new math problem:<br>
					 <input type="text" name="newMathProblem" required> 
					 <br> 
					  <br>
					   Provide category for math problem 
					   <select name="problemCategory" id="problemCategory" required>
						<option value="" selected="selected" hidden="hidden" disabled="disabled">Please Select</option>
						<%
							for (Category cat : mycatlist) {
						%>
						
						<option id="cid<%=cat.getCategoryId()%>" value="<%=cat.getCategoryId()%>">
							<%=cat.getCategoryType()%>
						</option>

						<%
							}
						%>

					</select>
					 <br>
					 <input type="submit" value="submit">
				</form>
					<p>${param.addProbMessage}</p>
				</div>
			</div>
		</div>
	</div>
</body>