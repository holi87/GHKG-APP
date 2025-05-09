document.addEventListener("DOMContentLoaded", () => {
    const token = localStorage.getItem("token");

    if (!token) {
        document.getElementById("message").textContent = "Not authenticated.";
        return;
    }

    const carList = document.getElementById("carList");
    const carForm = document.getElementById("carForm");
    const carIdField = document.getElementById("carId");
    const nameField = document.getElementById("name");
    const fuelTypeField = document.getElementById("fuelType");
    const engineCapacityField = document.getElementById("engineCapacity");
    const messageDiv = document.getElementById("message");

    function loadFuelTypes() {
        fetch("/api/enums/fuel-types")
            .then(response => {
                console.log("FuelTypes response:", response);
                return response.json();
            })
            .then(data => {
                console.log("FuelTypes data:", data);
                fuelTypeField.innerHTML = "";
                data.forEach(type => {
                    const option = document.createElement("option");
                    option.value = type;
                    option.textContent = type;
                    fuelTypeField.appendChild(option);
                });
            })
            .catch(err => {
                console.error("FuelTypes error:", err);
            });
    }


    function loadCars() {
        fetch("/api/cars", {
            headers: {
                "Authorization": "Bearer " + token
            }
        })
            .then(response => {
                if (!response.ok) throw new Error("Unauthorized");
                return response.json();
            })
            .then(data => {
                const cars = Array.isArray(data) ? data : data.content || [];
                carList.innerHTML = "";

                if (cars.length === 0) {
                    const li = document.createElement("li");
                    li.className = "list-group-item";
                    li.textContent = "No cars available.";
                    carList.appendChild(li);
                } else {
                    cars.forEach(car => {
                        const li = document.createElement("li");
                        li.className = "list-group-item d-flex justify-content-between align-items-center";
                        li.innerHTML = `
                            <div>
                                <strong>${car.name}</strong> (${car.fuelType}, ${car.engineCapacity}cc)
                            </div>
                            <div>
                                <button class="btn btn-sm btn-outline-primary me-2" onclick="editCar('${car.id}')">Edit</button>
                                <button class="btn btn-sm btn-outline-danger" onclick="deleteCar('${car.id}')">Delete</button>
                            </div>`;
                        carList.appendChild(li);
                    });
                }
            })
            .catch(err => {
                messageDiv.textContent = err.message;
            });
    }

    carForm.addEventListener("submit", function (e) {
        e.preventDefault();
        const car = {
            id: carIdField.value || null,
            name: nameField.value,
            fuelType: fuelTypeField.value,
            engineCapacity: parseInt(engineCapacityField.value)
        };

        const method = carIdField.value ? "PUT" : "POST";
        const url = carIdField.value ? `/api/cars/${carIdField.value}` : "/api/cars";
        fetch(url, {
            method: method,
            headers: {
                "Content-Type": "application/json",
                "Authorization": "Bearer " + token
            },
            body: JSON.stringify(car)
        })
            .then(res => {
                if (!res.ok) throw new Error("Failed to save car");
                return res.json();
            })
            .then(() => {
                messageDiv.textContent = "Car saved successfully.";
                carForm.reset();
                carIdField.value = "";
                loadCars();
            })
            .catch(err => {
                messageDiv.textContent = err.message;
            });
    });

    window.editCar = function (id) {
        fetch(`/api/cars/${id}`, {
            headers: {
                "Authorization": "Bearer " + token
            }
        })
            .then(res => {
                if (!res.ok) throw new Error("Failed to load car");
                return res.json();
            })
            .then(car => {
                carIdField.value = car.id;
                nameField.value = car.name;
                fuelTypeField.value = car.fuelType;
                engineCapacityField.value = car.engineCapacity;
            });
    };

    window.deleteCar = function (id) {
        if (!confirm("Are you sure you want to delete this car?")) return;

        fetch(`/api/cars/${id}`, {
            method: "DELETE",
            headers: {
                "Authorization": "Bearer " + token
            }
        })
            .then(res => {
                if (!res.ok) throw new Error("Failed to delete car");
                loadCars();
                messageDiv.textContent = "Car deleted.";
            })
            .catch(err => {
                messageDiv.textContent = err.message;
            });
    };

    loadFuelTypes();
    loadCars();
});
