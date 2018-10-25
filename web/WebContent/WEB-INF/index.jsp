
<style>
.pointer  {
	cursor: pointer;
}
</style>

<body>
	<div class="row">
		<div class="container">
			<div class='col s12'>
         		<div class="card">
	         	  <div class="row card-content">
	         	  	<div class='row'>
		                <div class='col s12'>
		                  <h4 class="indigo-text center-align">Miscellaneous</h4>
		                </div>
	                </div>
	         	  	<div class="row dashboard-boxes">
		                <div class="col s12">
							<div class="col s12 m3 m-mb-2 pointer" id="uploadToDrive">
							    <div class="db-box primary-bg">
							        <span class="sub-text upload-box">Upload Files to Drive</span>
							    </div>
							</div>
							
							<div class="col s12 m3 m-mb-2 pointer" id="filesMetada">
							    <div class="db-box primary-bg">
							        <span class="sub-text upload-box">Get Files Metadata</span>
							    </div>
							</div>
							
							<div class="col s12 m3 m-mb-2 pointer" id="manageContacts">
							    <div class="db-box primary-bg">
							        <span class="sub-text upload-box">Manage Contacts</span>
							    </div>
							</div>
					    </div>
					    
					    
				   </div>
				   
	         	  </div>
         	    </div>
         	</div>
		</div>
	</div>
</body>

<script type="text/javascript">

$("div#uploadToDrive").on("click", function(){
	location.href = "${pageContext.request.contextPath}/pub/uploadToDrive.html";
});

$("div#filesMetada").on("click", function(){
	location.href = "${pageContext.request.contextPath}/pub/getFilesMetadata.html";
});

$("div#manageContacts").on("click", function(){
	location.href = "${pageContext.request.contextPath}/pub/manageContacts.html";
});

</script>