
$(document).ready(function() {
	$("nav h4.mobile-title b").text("Profile");
	$("#navItems ul li .prf").addClass("active");
	const contentBox = $(".content-box");
	loadPassChange(contentBox.find(".card"));
	$("#updatePass").click(function() {
		let target = $(this).data('target');
		var expanded = $(this).attr("aria-expanded");
		if (expanded === "true") {
			$(target).collapse("hide");
			$(this).attr("aria-expanded", false);
		} else {
			$(target).collapse("show");
			$(this).attr("aria-expanded", true);
		}
	});
	contentBox.on("click", "#submit", function() {
		let opw = contentBox.find("#opw").val();
		let npw = contentBox.find("#npw").val();
		let rnpw = contentBox.find("#rnpw").val();
		$.ajax({
			url: "profile",
			type: "POST",
			data: {service: "password", opw: opw, npw: npw, rnpw: rnpw},
			beforeSend: function(xhr) {
				$("#submit").html(`<span class="spinner-border spinner-border-sm" role="status" aria-hidden="true"></span>`);
			},
			success: function(data, status, xhr) {
				if (data['status'] === 'success') {
					$("input").addClass("is-valid");
					$("#updatePass").click();
					contentBox.find("#chPsFr #upwf small").remove();
					resetForms();
					$("#submit").html("Update");
					$("#updatePass").after(`<small class="text-success ml-2"> Successfully Updated Password</small>`);
				} else {
					if (!contentBox.has("#chPsFr #upwf small").length) {
						contentBox.find("#chPsFr #upwf").prepend(`<small class="text-danger d-block">Invalid Input</small>`);
					}
					$("#submit").html("Update");
				}
			}
		});
	});
});

function loadPassChange(container) {
	var form = $(`<div class="collapse" id="chPsFr"></div>`);
	form.append(`
		<div class="card-body" id="upwf">
			<label>Old Password</label>
			<input type="password" class="form-control" id="opw" />
			<label>New Password</label>
			<input type="password" class="form-control" id="npw" />
			<label>Retype Password</label>
			<input type="password" class="form-control" id="rnpw" />
			<p class="mt-2 mb-0 text-end">
				<button class="btn" id="submit">Update</button>
			</p>
		</div>
	`);
	container.append(form);
}

function resetForms() {
	$("input").val("");
	$("input").removeClass("is-valid");
	
}