<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<body>
	<div class="row">
		<div class="container">
			<div class='col s12'>
         		<div class="card">
	         	  <div class="row card-content">
	         	  	<div class='row'>
		                <div class='col s12'>
		                  <h4 class="indigo-text center-align">Manage Contacts</h4><br/><br/>
		                </div>
                		<div class="row">
		                	<div class="col s2"></div>
			                <div class="col s6">
			                	<c:choose>
			                		<c:when test="${not empty errorMsg }">
			                			<h4 style="color:red;">Error found : </h4><br>
			                			<span style="color:red;">${errorMsg}</span>
			                		</c:when>
			                		<c:otherwise>
			                			<table class='bordered responsive-table' contactsTable>
											<thead>
												<tr>
													<th>Name</th>
													<th>Number(s)</th>
												</tr>
											</thead>
											<tbody>
												<c:choose>
													<c:when test="${not empty contacts}">
														<c:forEach items="${contacts}" var="contact">
															<tr>
											 					<td>${contact.fullName}</td>
											 					<td>${contact.phoneNumbers[0]} &nbsp; &nbsp; ${contact.phoneNumbers[1]}</td>
											 				</tr>
														</c:forEach>
													</c:when>
													<c:otherwise>
														<td>No contacts found</td>
													</c:otherwise>
												</c:choose>
											</tbody>
										</table>
			                		</c:otherwise>
			                	</c:choose>
			                </div>
			            </div>
	                </div>
	                
	         	  </div>
         	    </div>
         	</div>
		</div>
	</div>
</body>
<script>
$(document).ready(function() {
	datatableExperiment('contactsTable', 5);
});
</script>