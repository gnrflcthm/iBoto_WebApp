const loading = `
	<div class="col-md-12 d-flex justify-content-center h-100 loading">
		<div class="spinner-border align-self-center" role="status"></div>
	</div>
`;

const startVoting = `
	<div class="col-md-12 d-flex justify-content-center h-100">
		<button class="btn btn-lg align-self-center start-voting">Start Voting</button>
	</div> 
`;

const noElections = `
	<div class="col-md-12 d-flex justify-content-center h-100">
		<h4 class="align-self-center text-secondary">
			There are no ongoing elections.
		</h4>
	</div>
`;

const backArrow = `
	<a class="return position-absolute start-0 d-block"><i class="bi bi-chevron-left"></i>Return</a>
`;
	

const vote = {"mayor": {}, "viceMayor": {}, "councilors": []}

$(document).ready(function() {
	const contentBox = $(".content-box");
	var electionID = ""
	initialize(contentBox, userID);
	contentBox.on("click", ".election-card", function() {
		let election = $(this);
		electionID = election.data("election-id");
		$.ajax({
			url: "service",
			type: "POST",
			data: {service: "voteCheck", uid: userID, electionID: electionID},
			beforeSend: function(xhr) {
				contentBox.append(loading);
			},
			success: function(data, status, xhr) {
				if (data["voted"] === "false") {
					contentBox.empty();
					contentBox.append(startVoting);
				} else {
					contentBox.empty();
					contentBox.append(`
						<div class="col-12 justify-self-center">
							<div class="card">
								<div class="card-body d-flex justify-content-between">
									<h4 class="card-title">You Have Already Voted</h4>
									<p class="card-text">Date Voted: ${data['dateVoted']}</p>
								</div>
								<div class="card-footer d-flex justify-content-end">
									<a class="btn" href="results">Check Vote Summary</a>
								</div>
							</div>
						</div>
					`);
				}
				let title = election.find("h4").text();
				$(".content-title h4 b").text(title);
				$(".content-title").prepend(backArrow);
			}
		});
	});
	
	$(".content-title").on("click", ".return", function() {
		contentBox.empty();
		$(".content-title").find("a").remove();
		initialize(contentBox, userID);
	});
	
	contentBox.on("click", ".start-voting", function() {
		$.ajax({
			url: "service",
			type: "POST",
			data: {service: "candidates", electionID: electionID, uid: userID},
			beforeSend: function(xhr) {
				contentBox.append(loading);
			},
			success: function(data, status, xhr) {
				loadCandidates(data, contentBox, electionID, userID);
			}
		});
	});
});

function loadElections(data, container) {
	if (data.length === 0) {
		container.empty();
		container.append(noElections);
	} else {
		container.empty();
		for (let i = 0; i < data.length; i++) {
			var current = data[i];
			container.append(`
					<div class="col-12 col-md-4">
						<div class="card">
							<div class="card-body election-card" data-election-id="${current.id}">
								<h4 class="card-title">${current.name}</h4>
								<h5 class="card-subtite text-muted">${current.city}</h5>
								<h6 class="card-subtitle text-muted">${current.dateStart}</h6>
							</div>
						</div>
					</div>
			`);
		}
	}
}

function loadCandidates(data, container, electionID, userID) {
	console.log(data);
	container.empty();
	var content = $("<div class='content'></div>");
	var mayors = data['mayorCandidates'];
	var viceMayors = data['viceMayorCandidates'];
	var councilors = data['councilorCandidates'];
	var maxCouncilors = data['councilorCount'];
	content.append(`
			<div class="row p-6 my-3">
				<div class="col-3"><button class="candidate-tab btn h-100 w-100 m active" data-target="m-candidates">Mayor</button></div>
				<div class="col-3"><button class="candidate-tab btn h-100 w-100 vm disabled" data-target="vm-candidates">Vice Mayor</button></div>
				<div class="col-3"><button class="candidate-tab btn h-100 w-100 spc disabled" data-target="spc-candidates">Councilor</button></div>
				<div class="col-3"><button class="candidate-tab btn h-100 w-100 sub disabled" data-target="submit">Submit</button></div>
			</div>
	`);
	
	var mayorVote = $('<div class="row p-6 mb-3 m-candidates section-tab"></div>');
	for (let i = 0; i < mayors.length; i++) {
		loadMayorCard(mayorVote, mayors[i]);
	}
	
	var viceMayorVote = $('<div class="row p-6 mb-3 vm-candidates section-tab"></div>')
	viceMayorVote.hide();
	for (let i = 0; i < viceMayors.length; i++) {
		loadViceMayorCard(viceMayorVote, viceMayors[i])
	}
	
	var councilorVote = $('<div class="row p-6 mb-3 spc-candidates section-tab"></div>');
	councilorVote.hide();
	for (let i = 0; i < councilors.length; i++) {
		loadCouncilorCard(councilorVote, councilors[i]);
	}
	var completeVote = $('<div class="row p-6 mb-3 submit justify-content-center section-tab"></div>');
	completeVote.hide();
	completeVote.append(`
		<div class="col-6">
            <div class="card">
                <div class="card-body">
                    <h4 class="card-title">Summary of Vote</h4>
                    <h5>Mayor</h5>
                    <ul>
                        <li class="voted-mayor"></li>
                    </ul>
                    <h5>Vice Mayor</h5>
                    <ul>
                        <li class="voted-vicemayor"></li>
                    </ul>
                    <h5>Sangguniang Panlalawigan Councilors</h5>
                    <ul class="voted-councilors">
                    </ul>
                    <button class="btn w-100" id="submit-vote">Submit Vote</button>
                </div>
            </div>
        </div>
	`);
	
	container.on("click", ".candidate-tab", function() {
		var tab = $(this);
		var target = tab.data("target");
		container.find(".section-tab").slideUp(250).promise().done(function() {
			container.find(`.${target}`).slideDown(250);						
		});
		container.find(".candidate-tab").removeClass("active");
		tab.addClass("active");
	});
	container.on("click", ".m-candidate .mayor-btn", function() {
		let btn = $(this);
		let candidateID = btn.data("candidate-id");
		let candidateName = btn.data("name");
		vote['mayor'] = {"id": candidateID, "name": candidateName};
		container.find(".m-candidate .mayor-btn").removeClass("selected");
		btn.addClass("selected");
		container.find(".vm").removeClass("disabled");
		updateVote(completeVote);
	});
	container.on("click", ".vm-candidate .vm-btn", function() {
		let btn = $(this);
		let candidateID = btn.data("candidate-id");
		let candidateName = btn.data("name");
		vote['viceMayor'] = {"id": candidateID, "name": candidateName};
		container.find(".vm-candidate .vm-btn").removeClass("selected");
		btn.addClass("selected");
		container.find(".spc").removeClass("disabled");
		updateVote(completeVote);
	});
	container.on("click", ".spc-candidate .spc-btn", function() {
		let btn = $(this);
		let candidateID = btn.data("candidate-id");
		let candidateName = btn.data("name");
		let removed = false;
		for (let i = 0; i < vote['councilors'].length; i++) {
			if (vote['councilors'][i].id === candidateID) {
				vote['councilors'].splice(i, 1);
				removed = true;
				btn.removeClass("selected");
			}
		}
		if (!removed && vote['councilors'].length < maxCouncilors) {
			vote['councilors'].push({"id": candidateID, "name": candidateName});
			btn.addClass("selected");
		}
		if (vote['councilors'].length >= maxCouncilors) {
			$(".spc-candidate .spc-btn").each(function(index, element) {
				if (!$(this).hasClass("selected")) {
					$(this).addClass("full");
				}
			});
		} else {
			$(".spc-candidate .spc-btn").removeClass("full");
		}
		container.find(".sub").removeClass("disabled");
		updateVote(completeVote);
	});
	container.on("click", ".submit #submit-vote", function() {
		$.ajax({
			url: "service",
			type: "POST",
			data: {service: "vote", data: vote, electionID: electionID, userID: userID},
			beforeSend: function(xhr) {
				container.append(loading);
			},
			success: function(data, status, xhr) {
				if (data['status'] === 'success') {
					container.empty();
					container.append(`
						<div class="col-12 d-flex justify-content-center h-100">
							<div class="col-8">
								<div class="card">
									<div class="card-body">
										<h4 class="card-title"><b>Thank You For Voting!</b></h4>
										<p class="card-text">Your Reference Number is:</p>
										<h5 class="ref-number"><b>${data['referenceNumber']}</b></h5>
									</div>
								</div>
							</div>
						</div>
					`)
				} else {
					
				}
			}
		});
	})
	content.append(mayorVote);
	content.append(viceMayorVote);
	content.append(councilorVote);
	content.append(completeVote);
	container.append(content);
}

function initialize(container, userID) {
	$(".content-title h4 b").text($("#navItems ul li a.active").text());
	$.ajax({
		url: "service",
		type: "POST",
		data: {service: "election", uid: userID},
		beforeSend: function(xhr) {
			container.append(loading);
		},
		success: function(data, status, xhr) {
			loadElections(data, container);
		}
	});
}

function updateVote(container) {
	container.find("ul .voted-mayor").text(vote['mayor']['name']);
	container.find("ul .voted-vicemayor").text(vote['viceMayor']['name']);
	var votedCouncilors = '';
	for (let i = 0; i < vote['councilors'].length; i++) {
		votedCouncilors += `<li>${vote['councilors'][i]['name']}</li>`;
	}
	container.find("ul.voted-councilors").empty();
	container.find("ul.voted-councilors").append(votedCouncilors);
}

function loadMayorCard(container, data) {
	container.append(`
		<div class="col-12 col-md-6 my-3 m-candidate">
			<div class="card">
				<div class="card-body">
					<h4 class="card-title">${data['name']}</h4>
					<div class="row">
						<div class="col-12 col-md-6">
							<button class="btn btn-outline-light rad w-100 mb-2 mb-md-0" type="button" data-bs-toggle="collapse" data-bs-target="${"#plat" + data['id']}">
								Platforms
							</button>
						</div>
						<div class="col-12 col-md-6">
							<button class="btn btn-outline-light rad w-100 mayor-btn" data-name="${data['name']}" data-candidate-id="${data['id']}">Select</button>
						</div>
						<div class="collapse mt-3" id="${"plat" + data['id']}">
                            <div class="card">
                                <div class="card-body">
                                </div>
                            </div>
                        </div>
					</div>
				</div>
			</div>
		</div>
	`);
	let platforms = data['platforms'];
	for (let i = 0 ; i < platforms.length; i++) {
		container.find(`${"#plat" + data['id']} .card .card-body`).append(`
			<h5 class="card-title">${platforms[i]['name']}</h5>
            <p class="card-text">
                ${platforms[i]['description']}
            </p>
		`);		
	}
}

function loadViceMayorCard(container, data) {
	container.append(`
		<div class="col-12 col-md-6 my-3 vm-candidate">
			<div class="card">
				<div class="card-body">
					<h4 class="card-title">${data['name']}</h4>
					<div class="row">
						<div class="col-12 col-md-6">
							<button class="btn btn-outline-light rad w-100 mb-2 mb-md-0" type="button" data-bs-toggle="collapse" data-bs-target="#${"plat" + data['id']}">
								Platforms
							</button>
						</div>
						<div class="col-12 col-md-6">
							<button class="btn btn-outline-light rad w-100 vm-btn" data-name="${data['name']}" data-candidate-id="${data['id']}">Select</button>
						</div>
						<div class="collapse mt-3" id="${"plat" + data['id']}">
                            <div class="card">
                                <div class="card-body">
                                </div>
                            </div>
                        </div>
					</div>
				</div>
			</div>
		</div>
	`);
	let platforms = data['platforms'];
	for (let i = 0 ; i < platforms.length; i++) {
		container.find(`${"#plat" + data['id']} .card .card-body`).append(`
			<h5 class="card-title">${platforms[i]['name']}</h5>
            <p class="card-text">
                ${platforms[i]['description']}
            </p>
		`);		
	}
}

function loadCouncilorCard(container, data) {
	container.append(`
		<div class="col-12 col-md-6 my-3 spc-candidate">
			<div class="card">
				<div class="card-body">
					<h4 class="card-title">${data['name']}</h4>
					<div class="row">
						<div class="col-12 col-md-6">
							<button class="btn btn-outline-light rad w-100 mb-2 mb-md-0" type="button" data-bs-toggle="collapse" data-bs-target="${"#plat" + data['id']}">
								Platforms
							</button>
						</div>
						<div class="col-12 col-md-6">
							<button class="btn btn-outline-light rad w-100 spc-btn" data-name="${data['name']}" data-candidate-id="${data['id']}">Select</button>
						</div>
						<div class="collapse mt-3" id="${"plat" + data['id']}">
                            <div class="card">
                                <div class="card-body">
                                </div>
                            </div>
                        </div>
					</div>
				</div>
			</div>
		</div>
	`);
	let platforms = data['platforms'];
	for (let i = 0 ; i < platforms.length; i++) {
		container.find(`${"#plat" + data['id']} .card .card-body`).append(`
			<h5 class="card-title">${platforms[i]['name']}</h5>
            <p class="card-text">
                ${platforms[i]['description']}
            </p>
		`);		
	}
}