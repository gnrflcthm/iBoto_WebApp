
$(document).ready(function() {
    var citySelect = false;
    var form = $("#regForm");
    $(".name").hide();
    $(".complete-registration").hide();
    $("#city-select").change(function() {
        if (!citySelect) {
            $(".name").show();
            citySelect = true;
        } else {
        	resetForm();
        }
    });
    $("#validate").click(function() {
    	var firstName = $("#fname").val();
    	var middleName = $("#mname").val();
    	var lastName = $("#lname").val();
    	var city = $("#city-select").val();
        $.ajax({
        	url: "regvalidate.jsp",
        	type: "POST",
        	data: {"validate": 0, "fname": firstName, "mname": middleName, "lname": lastName, "city": city},
        	beforeSend: function() {
        		$(this).hide();
        	},
        	success: function(res, status) {
        		if (res['valid']) {
        			$(".name-field").prop("disabled", true);
        			$(".name-field").addClass("is-valid");
        			$("#validate").hide();
        			$(".complete-registration").show();
        			var district = $("#district-select");
        			district.empty();
        			for (let i = 1; i <= res["district_count"]; i++) {
        				district.append(`<option value='${i}'>${i}</option>`);
        			}
        			if (res["district_count"] == 1) {
        				district.prop("disabled", true);
        			}
        		}
        	},
        	error: function(xhr, status, error) {
        		console.log(error);
        	}
        });
        
    });
    $("#register").click(function() {
    	var bday = $("input[type=date]")[0].value;
    	var pwd = $("#pwd").val();
    	var cnpwd = $("#cnpwd").val();
        $.ajax({
        	url: "regvalidate.jsp",
        	method: "POST",
        	data: {"validate": 1, "bday": bday, "pwd": pwd, "cnpwd": cnpwd},
        	beforeSend: function() {
        		
        	},
        	success: function(data, status) {
        		if (data['birthday']) {
        			$(".bday").prop("disabled", true);
        			$(".bday").addClass("is-valid");
        			$(".bday").removeClass("is-invalid");
        		} else {
        			$(".bday").removeClass("is-valid");
        			$(".bday").addClass("is-invalid");
        		}
        		if (data['password']) {
        			$(".pwd").prop("disabled", true);
        			$(".pwd").addClass("is-valid");
        			$(".pwd").removeClass("is-invalid");
        		} else {
        			$(".pwd").removeClass("is-valid");
        			$(".pwd").addClass("is-invalid");
        		}
        	},
        	error: function(xhr, status, error) {
        		alert("error")
        	},
        	complete: function(xhr, status) {
        		$("#regForm").submit();
        	}
        });
    });
});

function resetForm() {
	$(".name-field").prop("disabled", false);
	$(".name-field").removeClass("is-valid");
	$(".name-field").removeClass("is-invalid");
	$("#validate").show();
	$(".pwd").prop("disabled", false);
	$(".pwd").removeClass("is-valid");
	$(".pwd").removeClass("is-invalid");
	$(".pwd").val("");
	$("#email").val("");
	$("#phoneNum").val("");
	$("input[type=date]")[0].value = 0;
	$("input[type=date]").prop("disabled", false);
	$("input[type=date]").removeClass("is-valid");
	$("input[type=date]").removeClass("is-invalid");
	$(".complete-registration").hide();
}
