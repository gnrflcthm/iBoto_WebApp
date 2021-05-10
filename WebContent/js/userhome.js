const loading = `
	<div class="col-md-12 d-flex justify-content-center h-100">
		<div class="spinner-border align-self-center" role="status"></div>
	</div>
`;

const startVoting = `
	<div class="col-md-12 d-flex justify-content-center h-100">
		<button class="btn btn-lg align-self-center">Start Voting</button>
	</div> 
`;

const noElections = `
	<div class="col-md-12 d-flex justify-content-center h-100">
		<h4 class="align-self-center text-secondary">
			There are no ongoing elections.
		</h4>
	</div>
`;
	

$(document).ready(function() {
	const contentBox = $(".content-box");
	$.ajax({
		url: "service",
		type: "POST",
		data: {service: "election", uid: userID},
		beforeSend: function(xhr) {
			contentBox.append(loading);
		},
		success: function(data, status, xhr) {
			console.log(data);
		}
	});
	console.log("Henlo");
});