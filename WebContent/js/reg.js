
$(document).ready(function() {
    var citySelect = false;
    var form = $("#regForm");
    $(".name").hide();
    $(".complete-registration").hide();
    $("#city-select").change(function() {
        if (!citySelect) {
            $(".name").show(500);
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
        		$(this).hide(500);
        	},
        	success: function(res, status) {
        		if (res['valid']) {
        			$(".name-field").prop("readonly", true);
        			$(".name-field").addClass("is-valid");
        			$("#validate").hide(500);
        			$(".complete-registration").show(500);
        			var district = $("#district-select");
        			district.empty();
        			for (let i = 0; i <= res["district_count"]; i++) {
        				if (i === 0) {
        					district.append(`<option value="0" selected disabled hidden></option>`);
        					continue;
        				}
        				district.append(`<option value='${i}'>${i}</option>`);
        			}
        			if (res["district_count"] == 1) {
        				district.prop("readonly", true);
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
    	var email = $("#email").val();
    	var phoneNum = $("#phoneNum").val();
    	var complete;
        $.ajax({
        	url: "regvalidate.jsp",
        	method: "POST",
        	data: {"validate": 1, "bday": bday, "pwd": pwd, "cnpwd": cnpwd, "email": email, "phoneNum": phoneNum},
        	beforeSend: function() {
        		
        	},
        	success: function(data, status) {
        		complete = data['birthday'] && data['password'] && 
        				   data['email'] && data['phoneNum'];
        		if (data['birthday']) {
        			$(".bday").prop("readonly", true);
        			$(".bday").addClass("is-valid");
        			$(".bday").removeClass("is-invalid");
        		} else {
        			$(".bday").removeClass("is-valid");
        			$(".bday").addClass("is-invalid");
        		}
        		if (data['password']) {
        			$(".pwd").prop("readonly", true);
        			$(".pwd").addClass("is-valid");
        			$(".pwd").removeClass("is-invalid");
        		} else {
        			$(".pwd").removeClass("is-valid");
        			$(".pwd").addClass("is-invalid");
        		}
        		if (data['email']) {
        			$("#email").addClass("is-valid");
        			$("#email").removeClass("is-invalid");
        		} else {
        			$("#email").removeClass("is-valid");
        			$("#email").addClass("is-invalid");
        		}
        		if (data['phoneNum']) {
        			$("#phoneNum").addClass("is-valid");
        			$("#phoneNum").removeClass("is-invalid");
        		} else {
        			$("#phoneNum").removeClass("is-valid");
        			$("#phoneNum").addClass("is-invalid");
        		}
        	},
        	error: function(xhr, status, error) {
        		alert("error")
        	},
        	complete: function(xhr, status) {
                if (complete) {
                	$("#regForm").submit();
                }
        	}
        });
    });
});

function resetForm() {
	$(".name-field").prop("readonly", false);
	$(".name-field").removeClass("is-valid");
	$(".name-field").removeClass("is-invalid");
	$("#validate").show(500);
	$(".pwd").prop("readonly", false);
	$(".pwd").removeClass("is-valid");
	$(".pwd").removeClass("is-invalid");
	$(".pwd").val("");
	$("#email").val("");
	$("#phoneNum").val("");
	$("input[type=date]")[0].value = 0;
	$("input[type=date]").prop("readonly", false);
	$("input[type=date]").removeClass("is-valid");
	$("input[type=date]").removeClass("is-invalid");
	$(".complete-registration").hide(500);
}
