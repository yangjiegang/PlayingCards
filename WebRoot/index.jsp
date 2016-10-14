<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>

<!DOCTYPE HTML>
<html>
  <head>
    <base href="<%=basePath%>">
    
    <title>Game</title>
    <meta charset="UTF-8">
	<link rel="stylesheet" type="text/css" href="bootstrap/css/bootstrap.css">
	
  </head>
  
<body>
	<div class="container">
		<div class="row">
			<div class="page-header">
				<h1>Five&nbsp;<span class="glyphicon glyphicon-tower" aria-hidden="true"></span>&nbsp;二十一点&nbsp;&copy;<small>&nbsp;悠闲小游戏</small></h1>
			</div>
			<div class="col-lg-8">
				<table id="others" class="table table-responsive table-striped table-bordered col-lg-8">
					<tr>
						<th>Players</th>
						<th colspan="5">Cards</th>
						<th>Total</th>
					</tr>
					<tr>
						<td>who</td>
						<td>0</td>
						<td>0</td>
						<td>0</td>
						<td>0</td>
						<td>0</td>
						<td>0</td>
					</tr>
					<tr>
						<td>who</td>
						<td>0</td>
						<td>0</td>
						<td>0</td>
						<td>0</td>
						<td>0</td>
						<td>0</td>
					</tr>
					<tr>
						<td>who</td>
						<td>0</td>
						<td>0</td>
						<td>0</td>
						<td>0</td>
						<td>0</td>
						<td>0</td>
					</tr>
				</table>
				
				<table id="me" class="table table-responsive table-bordered table-hover col-lg-8">
					<tr>
						<th>MyName</th>
						<th colspan="5">MyCards</th>
						<th>Total</th>
					</tr>
					<tr id="myCards">
						<td>me</td>
						<td>0</td>
						<td>0</td>
						<td>0</td>
						<td>0</td>
						<td>0</td>
						<td>0</td>
					</tr>
					<tr>
						<td colspan="2" id="ready"><a class="btn btn-default">ready</a></td>
						<td colspan="3" id="more"><a class="btn btn-default">more</a></td>
						<td colspan="2" id="finish"><a class="btn btn-default">finish</a></td>
					</tr>
				</table>
						
			</div>

			<div class="col-lg-4" style="border:1px solid grey; height:250px;">
				<div style="margin:50px auto; padding:10px;">
					<p>Input your nickname following:</p>
					<input type="text" id="nickname"/>
				</div>
			</div>
		</div>
	</div>
	
	<script type="text/javascript" src="bootstrap/js/jquery.min.js"></script>
	<script type="text/javascript" src="bootstrap/js/bootstrap.js"></script>
	<script type="text/javascript" src="js/index.js"></script>
</body>
</html>
