<%@page import="java.io.PrintWriter"%>
<%@page import="java.io.DataOutputStream"%>
<%@page import="java.io.DataInputStream"%>
<%@page import="java.net.Socket"%>
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://"
			+ request.getServerName() + ":" + request.getServerPort()
			+ path + "/";
%>

<!DOCTYPE HTML>
<html>
<head>
<base href="<%=basePath%>">

<title>Game</title>
	<meta charset="UTF-8">
	<link rel="stylesheet" type="text/css"
		href="bootstrap/css/bootstrap.css"/>
	<link rel="stylesheet" type="text/css" href="css/index.css"/>
</head>

<body>
	<div class="container">
		<div class="row">
			<div class="page-header">
				<h1>
					Five*Four&nbsp;<span class="glyphicon glyphicon-tower"
						aria-hidden="true"></span>&nbsp;21-POINTS&nbsp;&copy;<small>&nbsp;TinyGame</small>
				</h1>
			</div>
			<div class="col-lg-8">
				<table id="others"
					class="table table-responsive table-striped table-bordered col-lg-8">
					<tr>
						<th>Players</th>
						<th colspan="5">Cards</th>
						<th>Total</th>
					</tr>
					<tr>
						<td>who</td>
						<td><img src="" alt="0" /></td>
						<td><img src="" alt="0" /></td>
						<td><img src="" alt="0" /></td>
						<td><img src="" alt="0" /></td>
						<td><img src="" alt="0" /></td>
						<td><img src="" alt="0" /></td>
					</tr>
					<tr>
						<td>who</td>
						<td><img src="" alt="0" /></td>
						<td><img src="" alt="0" /></td>
						<td><img src="" alt="0" /></td>
						<td><img src="" alt="0" /></td>
						<td><img src="" alt="0" /></td>
						<td><img src="" alt="0" /></td>
					</tr>
					<tr>
						<td>who</td>
						<td><img src="" alt="0" /></td>
						<td><img src="" alt="0" /></td>
						<td><img src="" alt="0" /></td>
						<td><img src="" alt="0" /></td>
						<td><img src="" alt="0" /></td>
						<td><img src="" alt="0" /></td>
					</tr>
				</table>

				<table id="me"
					class="table table-responsive table-bordered table-hover col-lg-8">
					<tr>
						<th>MyName</th>
						<th colspan="5">MyCards</th>
						<th>Total</th>
					</tr>
					<tr id="myCards">
						<td>me</td>
						<td><img src="" alt="0" /></td>
						<td><img src="" alt="0" /></td>
						<td><img src="" alt="0" /></td>
						<td><img src="" alt="0" /></td>
						<td><img src="" alt="0" /></td>
						<td><img src="" alt="0" /></td>
					</tr>
					<tr>
						<td colspan="2" id="ready"><a class="btn btn-default">ready</a></td>
						<td colspan="3" id="more"><a class="btn btn-default">more</a></td>
						<td colspan="2" id="finish"><a class="btn btn-default">finish</a></td>
					</tr>
				</table>

			</div>

			<div id="msgBox" class="col-lg-4 well">
				<div>
					<marquee class="maq">Do not refresh the page when the game is running!</marquee>
				</div>
				<div id="msgPanel"></div>
				
<!-- 				<div id="sndBox">
					<input type="text"/><input type="button" value="send"/>
				</div> -->
				
				<div id="sndBox">
					<form class="form-inline">
					  <div class="form-group">
					    <div class="input-group">
					      <!-- <div class="input-group-addon">ToAll:</div> -->
					      <input type="text" class="form-control" placeholder="Message" style="min-width:260px;">
					    </div>
					  </div>
					  <input type="button" class="btn btn-primary" value="submit"/>
					</form>
				</div>
				
			</div>
			
			<div class="modal fade" id="loginModal" tabindex="-1" role="dialog" aria-labelledby="loginModalLabel">
			  <div class="modal-dialog" role="document">
			    <div class="modal-content">
			      <div class="modal-header">
			      <!--   <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button> -->
			        <h4 class="modal-title"><span class="glyphicon glyphicon-pencil" aria-hidden="true"></span>Login Window</h4>
			      </div>
			      <div class="modal-body">
			        <form class="form-inline">
			          <div class="form-group">
			            <div class="input-group">
			            	<div class="input-group-addon">Nickname: </div>
				            <input type="text" class="form-control" id="nickname" style="min-width:400px;">
				            <div id="valiNickResult" class="input-group-addon">
				            	<span class="glyphicon glyphicon-ok" aria-hidden="true"></span>
				            	<span class="glyphicon glyphicon-remove" aria-hidden="true"></span>			            	
				            </div>
			            </div>
			          </div>
<!-- 			          <div class="form-group">
			            <label for="message-text" class="control-label">Message:</label>
			            <textarea class="form-control" id="message-text"></textarea>
			          </div> -->
			        </form>
			      </div>
			      <div class="modal-footer">
<!-- 			        <button type="button" class="btn btn-default" data-dismiss="modal">Close</button> -->
			        <button id="loginBtn" type="button" class="btn btn-default">Submit</button>
			      </div>
			    </div>
			  </div>
			</div><!-- login modal -->
			
			<div class="modal fade" id="resModal">
			  <div class="modal-dialog">
			    <div class="modal-content">
			      <div class="modal-header">
			        <h4 class="modal-title">Game Result</h4>
			      </div>
			      <div class="modal-body" id="resultDiv"></div>
<!-- 			      <div class="modal-footer">
			        <button type="button" class="btn btn-default" data-dismiss="modal">Close</button>
			        <button type="button" class="btn btn-primary">Save changes</button>
			      </div> -->
			    </div><!-- /.modal-content -->
			  </div><!-- /.modal-dialog -->
			</div><!-- result modal -->
			
		</div>
	</div>

<%-- 	<%
		Socket socket = new Socket("localhost", 8081);
		DataInputStream inputStream;
		DataOutputStream outStream;
		while (true) {
			inputStream = new DataInputStream(socket.getInputStream());
			String msgIn = inputStream.readUTF();
			String msgOut = request.getParameter("message");
			if(msgOut!=null && msgOut != ""){
				String num  = request.getParameter("unm");
				outStream = new DataOutputStream(socket.getOutputStream());
				outStream.writeUTF("msg,"+msgOut+","+num);
			}
		}
	%> --%>
	<script type="text/javascript" src="bootstrap/js/jquery.min.js"></script>
	<script type="text/javascript" src="bootstrap/js/bootstrap.js"></script>
	<script type="text/javascript" src="js/index.js"></script>
	<script type="text/javascript" src="js/chat.js"></script>
</body>
</html>
