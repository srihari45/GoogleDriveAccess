<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<body>
	<div class="row">
		<div id="beforeProcessing" class="hide indicator"></div>
		<div class="container">
			<div class='col s12'>
         		<div class="card">
	         	  <div class="row card-content">
	         	  	<div class='row'>
		                <div class='col s12'>
		                  <h4 class="indigo-text center-align">Files Metadata</h4><br/><br/>
		                </div>
                		<div class="row">
		                	<div class="col s2"></div>
			                <div class="col s6">
			                	<c:choose>
			                		<c:when test="${not empty errorList }">
			                			<h4 style="color:red;">Error found : </h4><br>
			                			<span style="color:red;">${errorList}</span>
			                		</c:when>
			                		<c:otherwise>
			                			<table class='bordered responsive-table'>
											<thead>
												<tr>
													<th>File Name</th>
												</tr>
											</thead>
											<tbody>
												<c:choose>
													<c:when test="${not empty list}">
														<c:forEach items="${list}" var="strVal">
															<tr>
											 					<td>${strVal}</td>
											 				</tr>
														</c:forEach>
													</c:when>
													<c:otherwise>
														<td>No Files Found</td>
													</c:otherwise>
												</c:choose>
											</tbody>
										</table>
			                		</c:otherwise>
			                	</c:choose>
			                </div>
			            </div>
	                </div>
	                
	                <div class="row">
	                	<div class="col s2"></div>
		                <div class='col s10'>
		                	<div id="divFiles">
			                </div>
		                </div>
		            </div>
            
	         	  </div>
         	    </div>
         	</div>
		</div>
	</div>
</body>