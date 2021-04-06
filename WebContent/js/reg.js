
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
            $("#validate").prop("disabled", false);
            $(".complete-registration").hide();
            $("#district-select").prop("disabled", false);
            $(".name-field").prop("disabled", false);
            $(".name-field").removeClass("is-valid");
            $("#validate").show();
            clearCompleteReg();
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
        		$(this).prop("disabled", true);
        		$(this).html(
        				`<span class="spinner-border spinner-border-sm" role="status" aria-hidden="true"></span> Loading...`
        		);
        	},
        	success: function(res, status) {
        		if (res['valid']) {
        			$(".name-field").prop("disabled", true);
        			$(".name-field").addClass("is-valid");
        			$("#validate").hide();
        			$(".complete-registration").show();
        			var district = $("#district-select");
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
    $("form").submit(function(e) {
        let password = validatePassword();
        let birthday = validateBirthday();
        e.preventDefault();
    });
});

function validFields(fields) {
    for (let i = 0; i < fields.length; i++) {
        if (fields[i].classList.contains("is-invalid")) {
            return false;
        } 
    }
    return true;
}

function validatePassword() {
    let passFields = $(".pwd");
    return passFields[0].value === passFields[1].value; 
}

function validateBirthday() {
    let birthday = $("input[type=date]")[0].value;
    console.log(birthday);
}

function clearCompleteReg() {
    $(".bday").val("");
    $("#district-select").val("");
    $(".pwd").val("");
    $("#email").val("");
    $("#phoneNum").val("");
}