const loading = `
	<div class="col-md-12 d-flex justify-content-center h-100 loading">
		<div class="spinner-border align-self-center" role="status"></div>
	</div>
`;

const backArrow = `
	<a class="return position-absolute start-0 d-block"><i class="bi bi-chevron-left"></i>Return</a>
`;


$(document).ready(function() {
	$("nav h4.mobile-title b").text("Results");
	$("#navItems ul li .re").addClass("active");
	const contentBox = $(".content-box");
	init(contentBox);
	$(".content-title").on("click", ".return", function() {
		contentBox.empty();
		$(".content-title").find("a").remove();
		init(contentBox);
	});
});

function init(container) {
	$(".content-frame h4 b").text("Results");
	$.ajax({
		url: "results",
		type: "POST",
		data: {"service": "elections", "uid": userID},
		beforeSend: function(xhr) {
			container.append(loading);
		},
		success: function(data, status, xhr) {
			loadVotedElections(container, data["votedElections"]);
		}
	});
	
}

function loadVotedElections(container, data) {
	container.empty();
	for (let i = 0; i < data.length; i++) {
		container.append(`
			<div class="col-12 voted-election my-2">
				<div class="card h-0">
					<div class="card-header d-flex justify-content-between">
						<h4 class="mt-2">${data[i]['electionName']}</h4>
						<a class="link-primary align-self-center download-res" target="_blank" href="results?refNum=${data[i]['referenceNumber']}">Save a Copy</a> 
					</div>
					<div class="card-body d-flex justify-content-between">
						<div class="row w-100">
							<p class="card-text col-12 col-md-3">Reference Number:</p>
							<p class="card-text col-12 col-md-4"><b>${data[i]['referenceNumber']}</b></p>
							<p class="card-text col-12 col-md-3">Date Voted:</p>
							<p class="card-text col-12 col-md-2"><b>${data[i]['dateVoted']}</b></p>
						</div>
					</div>
					<div class="collapse" id="summ${data[i]['referenceNumber']}">
						<div class="card">
							<div class="card-body">
							</div>
						</div>
					</div>
					<button class="card-footer btn d-block w-100 d-flex justify-content-center info-toggle" aria-expanded="false" data-target="#summ${data[i]['referenceNumber']}"><p class="d-block mb-0">Show More </p><i class="bi bi-caret-down-fill d-block"></i></button>
				</div>
			</div>
		`);
		$.ajax({
			url: "results",
			type: "POST",
			data: {service: "votedcandidates", voteID: data[i]['referenceNumber']},
			success: function(cands, status, xhr) {
				let collapse = container.find(`#summ${data[i]['referenceNumber']} .card .card-body`);
				collapse.append(`
					<div class="row">
						<div class="col-6">
							<h4 class="my-3">Mayor: </h4>
							<h4 class="my-3">Vice Mayor: </h4>
							<h4>Councilors: </h4>
						</div>
					</div>
				`);
				collapse.find(".row").append("<div class='col-6 names text-end'></div>");
				let names = collapse.find(".row .names");
				names.append(`<h5 class="my-3"><b>${cands['mayor']['name']}</b></h5>`)
				names.append(`<h5 class="my-3"><b>${cands['viceMayor']['name']}</b></h5>`)
				for (let i = 0; i < cands['councilorCandidates'].length; i++) {
					names.append(`<h5>${cands['councilorCandidates'][i]['name']}</h5>`)
				}
				names.append(`
					<button class="btn mt-4 election-poll d-block d-md-inline col-12 col-md-6" data-id="${data[i]['electionID']}" data-name="${data[i]['electionName']}">See Current Standings</button>
				`);
			}
		});
	}
	container.on("click", ".voted-election .card .election-poll", function() {
		let electionID = $(this).data("id");
		let electionName = $(this).data('name');
		$.ajax({
			url: "results",
			type: "POST",
			data: {service: "electionpoll", electionID: electionID},
			beforeSend: function(xhr) {
				container.append(loading);
			},
			success: function(data, status, xhr) {
				console.log(data);
				loadElectionPoll(container, data, electionName);
			}
		});
	});
	container.on("click", ".voted-election .card .info-toggle", function() {
		let target = $(this).data("target");
		let isExpanded = $(this).attr("aria-expanded");
		if (isExpanded === "false") {
			$(target).collapse("show");
			$(this).attr("aria-expanded", "true");
			$(this).find("p").text("Show Less");
			$(this).find("i").remove();
			$(this).append(`<i class="bi bi-caret-up-fill"></i>`);
		} else {
			$(target).collapse("hide");
			$(this).attr("aria-expanded", "false");
			$(this).find("p").text("Show More");
			$(this).find("i").remove();
			$(this).append(`<i class="bi bi-caret-down-fill"></i>`);
		}
		
	});
}

function loadElectionPoll(container, data, electionName) {
	container.empty();
	container.append(`
		<div class="col-12">
			<div class="card">
				<div class="card-header btn-group p-0">
					<button class="btn active candidate-tab" data-target="#mayorPoll">Mayor</button>
					<button class="btn candidate-tab" data-target="#vmayorPoll">Vice Mayor</button>
					<button class="btn candidate-tab" data-target="#councilorPoll">Councilor</button>
				</div>
			</div>
		</div>
	`);
	
	let mayorPoll = $(`<div class="card-body poll" id="mayorPoll"></div>`);
	mayorPoll.append(`
			<table class="table table-hover">
				<thead>
					<tr>
						<th>Candidate</td>
						<th class="text-end">Vote Count</td>
					</tr>
				</thead>
				<tbody>
					
				</tbody>
			</table>
	`);
	for (let i = 0; i < data['mayors'].length; i++) {
		mayorPoll.find('table tbody').append(`
			<tr>
				<td>${data['mayors'][i]['name']}</td>
				<td class="text-end">${data['mayors'][i]['voteCount']}</td>
			</tr>
		`)
	}
	let vmayorPoll = $(`<div class="card-body poll" id="vmayorPoll"></div>`);
	vmayorPoll.hide();
	vmayorPoll.append(`
			<table class="table table-hover">
				<thead>
					<tr>
						<th>Candidate</td>
						<th class="text-end">Vote Count</td>
					</tr>
				</thead>
				<tbody>
					
				</tbody>
			</table>
	`);
	for (let i = 0; i < data['viceMayors'].length; i++) {
		vmayorPoll.find('table tbody').append(`
			<tr>
				<td>${data['viceMayors'][i]['name']}</td>
				<td class="text-end">${data['viceMayors'][i]['voteCount']}</td>
			</tr>
		`)
	}
	let councilorPoll = $(`<div class="card-body poll" id="councilorPoll"></div>`);
	councilorPoll.hide();
	councilorPoll.append(`
			<table class="table table-hover">
				<thead>
					<tr>
						<th>Candidate</td>
						<th class="text-end">Vote Count</td>
					</tr>
				</thead>
				<tbody>
					
				</tbody>
			</table>
	`);
	for (let i = 0; i < data['councilors'].length; i++) {
		councilorPoll.find('table tbody').append(`
			<tr>
				<td>${data['councilors'][i]['name']}</td>
				<td class="text-end">${data['councilors'][i]['voteCount']}</td>
			</tr>
		`)
	}
	container.on("click", ".candidate-tab", function() {
		let tab = $(this);
		let target = tab.data("target");
		container.find(".poll").slideUp(250).promise().done(function() {
			container.find(`${target}`).slideDown(250);						
		});
		container.find(".candidate-tab").removeClass("active");
		tab.addClass("active");
	});
	container.append(mayorPoll);
	container.append(vmayorPoll);
	container.append(councilorPoll);
	$(".content-title h4 b").text(electionName);
	$(".content-title").prepend(backArrow);
}