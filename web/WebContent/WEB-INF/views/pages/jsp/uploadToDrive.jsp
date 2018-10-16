<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
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
		                  <h4 class="indigo-text center-align">Upload to Google Drive</h4><br/><br/>
		                </div>
		                <form:form modelAttribute="uploadFilesForm" name="uploadFilesForm" id="uploadFilesForm" method="POST" enctype="multipart/form-data">
		                		<div class="row">
				                	<div class="col s2"></div>
					                <div class="col s6">
					                  <a class="btn file-btn"> <span>Select file</span> 
					                      <input type="file" id="files" accept="*" multiple>
									  </a>
					                </div>
					                <div class="col s4">
					                	<a class="btn btn-default" id="btnUpload" onclick="uploadFiles();">Upload File(s)</a>
					                </div>
					            </div>
		                </form:form>
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
<script type="text/javascript">
$(document).ready(function () {
	$('input[type=file]').change(function () {
   		$('#divFiles').html('');
     	for (var i = 0; i < this.files.length; i++) {
     		var fileName = this.files[i].name;
         	 $("#divFiles").append("<div id='" + i + "'><span style='color:green'>" + fileName + "</span>" + 
         			"<a class='clear-icon' href='javascript:void(0);' onclick='removeDoc(" + i + ")' tabIndex='6'><img width='10' height='10' src='${pageContext.request.contextPath}/images/clear.png'></a></div><br/>");
        }
    })
});

function removeDoc(fileId) {
	$("#divFiles #"+fileId).remove();
}

function uploadFiles(){
	var files = document.getElementById("files")//All files
	if(files.files.length == 0 || files == 'undefined'){
		callErrorToast("Please select files");
		return false;
	}
	
	console.log("Length : " + files.files.length);
	var formData = new FormData();
	for (var i = 0; i < files.files.length; i++) {
		formData.append('file', files.files[i]);
	}
	
	$.ajax({
        url : '${pageContext.request.contextPath}/pub/uploadFilesToDrive.html',
        type : 'POST',
        data : formData,
        processData: false,  // tell jQuery not to process the data
        contentType: false,  // tell jQuery not to set contentType
        beforeSend : function(xhr) {
        	$("#btnUpload").addClass("disabled");
        	$("#beforeProcessing").removeClass("hide");
			$("#beforeProcessing").html("<span colspan='4'><img width='200' height='200' src='${pageContext.request.contextPath}/images/indicator.gif' /></span>");
        },
        success : function(message) {
        	$("#beforeProcessing").html("");
		    $("#beforeProcessing").addClass("hide");
		    $("#btnUpload").removeClass("disabled");
		    $("#divFiles").html("");
   			if (message.indexOf("|") != -1) {
   				var toastContent = "";
   				var msgArr = message.split("|");
   				if (msgArr[0] == "error") {
   					callErrorToast(msgArr[1]);
   					return;
   				}
   			} else {
   				callErrorToast(files.files.length + " files has been uploaded");
   			}
        },
        error: function(jq, status, message) {}
 	});
}

</script>