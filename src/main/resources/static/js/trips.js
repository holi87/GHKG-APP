document.addEventListener("DOMContentLoaded", () => {
    const token = localStorage.getItem("token");
    let userRoles = [];

    function fetchTrips() {
        fetch("/api/trips", {
            headers: {"Authorization": "Bearer " + token}
        })
            .then(res => res.json())
            .then(trips => {
                const list = document.getElementById("tripList");
                list.innerHTML = "";

                if (!Array.isArray(trips) || trips.length === 0) {
                    list.innerHTML = "<li class='list-group-item'>No trips available.</li>";
                    return;
                }

                trips.forEach(trip => {
                    const li = document.createElement("li");
                    li.className = "list-group-item d-flex justify-content-between align-items-center";
                    li.innerHTML = `
                        <div>
                            <strong>${trip.name}</strong> (${trip.type})<br/>
                            From ${trip.startLocation} to ${trip.destination}, ${trip.distance} km, ${trip.duration}, Rating: ${trip.rating}
                        </div>
                        <div>
                            ${userRoles.includes("WORKER") || userRoles.includes("ADMIN") ? `<button class="btn btn-sm btn-outline-primary me-2" onclick="editTrip(${trip.id})">Edit</button>` : ""}
                            ${userRoles.includes("ADMIN") ? `<button class="btn btn-sm btn-outline-danger" onclick="deleteTrip(${trip.id})">Delete</button>` : ""}
                        </div>`;
                    list.appendChild(li);
                });
            });
    }

    window.showForm = function (trip = null) {
        document.getElementById("tripFormContainer").style.display = "block";
        document.getElementById("formTitle").textContent = trip ? "Edit Trip" : "Add Trip";

        if (trip) {
            document.getElementById("tripId").value = trip.id;
            document.getElementById("tripName").value = trip.name;
            document.getElementById("tripType").value = trip.type;
            document.getElementById("tripStart").value = trip.startLocation;
            document.getElementById("tripEnd").value = trip.destination;
            document.getElementById("tripDistance").value = trip.distance;
            document.getElementById("tripDuration").value = trip.duration;
            document.getElementById("tripRating").value = trip.rating;
        } else {
            document.getElementById("tripForm").reset();
            document.getElementById("tripId").value = "";
        }
    };

    window.hideForm = function () {
        document.getElementById("tripFormContainer").style.display = "none";
        document.getElementById("tripForm").reset();
    };

    window.editTrip = function (id) {
        fetch(`/api/trips/${id}`, {
            headers: {"Authorization": "Bearer " + token}
        })
            .then(res => {
                if (!res.ok) throw new Error("Unauthorized");
                return res.json();
            })
            .then(trip => showForm(trip));
    };

    window.deleteTrip = function (id) {
        if (!confirm("Are you sure you want to delete this trip?")) return;

        fetch(`/api/trips/${id}`, {
            method: "DELETE",
            headers: {"Authorization": "Bearer " + token}
        }).then(() => fetchTrips());
    };

    document.getElementById("tripForm").addEventListener("submit", function (e) {
        e.preventDefault();

        const id = document.getElementById("tripId").value;
        const method = id ? "PUT" : "POST";
        const url = id ? `/api/trips/${id}` : "/api/trips";

        const trip = {
            name: document.getElementById("tripName").value,
            type: document.getElementById("tripType").value,
            startLocation: document.getElementById("tripStart").value,
            destination: document.getElementById("tripEnd").value,
            distance: parseFloat(document.getElementById("tripDistance").value),
            duration: document.getElementById("tripDuration").value,
            rating: parseInt(document.getElementById("tripRating").value)
        };

        fetch(url, {
            method: method,
            headers: {
                "Authorization": "Bearer " + token,
                "Content-Type": "application/json"
            },
            body: JSON.stringify(trip)
        })
            .then(res => {
                if (!res.ok) throw new Error("Failed to save trip");
                return res.json();
            })
            .then(() => {
                hideForm();
                fetchTrips();
            })
            .catch(err => alert(err.message));
    });

    function loadTripTypes() {
        fetch("/api/enums/trip-types")
            .then(res => res.json())
            .then(types => {
                const select = document.getElementById("tripType");
                types.forEach(type => {
                    const opt = document.createElement("option");
                    opt.value = type;
                    opt.textContent = type.charAt(0) + type.slice(1).toLowerCase(); // e.g. Car
                    select.appendChild(opt);
                });
            });
    }

    function checkRoleAndInit() {
        if (!token) return;

        fetch("/api/me", {
            headers: {"Authorization": "Bearer " + token}
        })
            .then(res => res.ok ? res.json() : Promise.reject())
            .then(user => {
                userRoles = user.roles;
                if (userRoles.includes("WORKER") || userRoles.includes("ADMIN")) {
                    document.getElementById("addTripBtn").style.display = "inline-block";
                    document.getElementById("addTripBtn").addEventListener("click", () => showForm());
                }
                loadTripTypes();
                fetchTrips();
            })
            .catch(() => {
                document.getElementById("tripList").innerHTML = "<li class='list-group-item text-danger'>Unauthorized.</li>";
            });
    }

    checkRoleAndInit();
});
