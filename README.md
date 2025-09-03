# Sustainable Planning of Pick-up and Delivery for Waste Collection

## Overview

This repository contains the main components used for experimenting with sustainable waste collection planning. The approach models the **Capacitated Vehicle Routing Problem with Pick-ups and Deliveries (CVRPPD)** to optimize routes for waste collection vehicles.

## Containerized Services

All required services are containerized and orchestrated using `docker-compose`. The core components include:

- **MongoDB**  
  Stores data related to street segments, supports fast search operations, and maintains updates on road networks and environmental conditions.

- **VROOM (Vehicle Routing Open-source Optimization Machine)**  
  An open-source optimization engine for solving various Vehicle Routing Problems (VRPs), including:  
  - TSP (Traveling Salesman Problem)  
  - CVRP (Capacitated VRP)  
  - VRPTW (VRP with Time Windows)  
  - MDHVRPTW (Multi-Depot Heterogeneous VRPTW)  
  - PDPTW (Pickup and Delivery Problem with Time Windows)

- **OSRM (Open Source Routing Machine)**  
  A high-performance routing engine designed to work with OpenStreetMap data.

## Planner Facade

A unified API Gateway is implemented using **Quarkus**, designed for cloud-native environments. This exposes a set of RESTful endpoints to interact with the planner and related services:

#### Available Endpoints

- `POST /api/v1/citydata/environmentconditions/batches`  
  Updates road conditions by adding or removing weights from street segments.

- `POST /api/v1/planner/problems`  
  Submits a new planning problem asynchronously. Accepts a JSON payload and returns a solution ID.

- `GET /api/v1/planner/plans`  
  Retrieves a planning solution using the solution ID.

- `DELETE /api/v1/planner/plans`  
  Deletes a planning solution to free up memory.

#### Input description
This section describes the structure of the JSON input used for defining the waste collection planning problem, modeled as a Capacitated Vehicle Routing Problem with Pick-ups and Deliveries (CVRPPD).

```json
{
  "shipments": [
    {
      "amount": [
        1073741824
      ],
      "pickup": {
        "id": 1073741824,
        "service": 1073741824,
        "location": [
          0.1
        ]
      },
      "delivery": {
        "id": 1073741824,
        "location": [
          0.1
        ]
      }
    }
  ],
  "options": {
    "g": true
  },
  "vehicles": [
    {
      "id": 1073741824,
      "start": [
        0.1
      ],
      "end": [
        0.1
      ],
      "capacity": [
        1073741824
      ],
      "costs": {
        "fixed": 0.1,
        "per_km": 0.1,
        "per_hour": 0.1
      },
      "time_window": [
        1073741824
      ]
    }
  ]
}
```

- The **`shipments`** field contains all shipment operations, including pick-up and delivery. Each shipment must specify both a collection and a delivery point.

  - **`amount`**: Quantity of waste at the pick-up location.
  - **`pickup`**:
    - `id`: Unique identifier of the pick-up location.
    - `service`: Time required to perform the pick-up.
    - `location`: Coordinates `[longitude, latitude]`.
  - **`delivery`**:
    - `id`: Unique identifier of the delivery location.
    - `location`: Coordinates `[longitude, latitude]`.

- **`options`** field is used to pass configuration settings for the planner.

  - **`g`**: If `true`, the planner returns a `MultiLineString` object for graphical visualization.

- The **`vehicles`** section defines the available fleet and their constraints.

  - **`id`**: Unique identifier of the vehicle.
  - **`start`**: Starting depot coordinates `[longitude, latitude]`.
  - **`end`**: Ending depot coordinates `[longitude, latitude]`.
  - **`capacity`**: Maximum load the vehicle can carry (e.g. amount of waste).
  - **`costs`**:
    - `fixed`: Fixed cost of using the vehicle.
    - `per_km`: Cost per kilometer (e.g., fuel).
    - `per_hour`: Cost per hour of operation (e.g., driver or energy).
  - **`time_window`**: Time frame `[start_time, end_time]` during which the vehicle is allowed to operate (e.g., in seconds or another unit).

#### Output description
This is a simplified example of the output returned by the planner after solving the routing problem. It contains information about the overall solution and the steps performed by each vehicle.

```json
{
  "code": 0,
  "summary": {
    "cost": 36509,
    "routes": 1,
    "unassigned": 0,
    "delivery": [2200],
    "amount": [2200],
    "pickup": [2200],
    "setup": 0,
    "service": 460,
    "duration": 289,
    "waiting_time": 0,
    "priority": 0,
    "distance": 2129,
    "violations": [],
    "computing_times": {
      "loading": 4,
      "solving": 0,
      "routing": 1
    }
  },
  "unassigned": [],
  "routes": [
    {
      "vehicle": 0,
      "cost": 36509,
      "delivery": [2200],
      "amount": [2200],
      "pickup": [2200],
      "setup": 0,
      "service": 460,
      "duration": 289,
      "waiting_time": 0,
      "priority": 0,
      "distance": 2129,
      "steps": [
        {
          "type": "start",
          "location": [0.0, 0.0],
          "setup": 0,
          "service": 0,
          "waiting_time": 0,
          "load": [0],
          "arrival": 0,
          "duration": 0,
          "violations": [],
          "distance": 0,
          "id": 0
        },
        {
          "type": "pickup",
          "location": [1.0, 1.0],
          "setup": 0,
          "service": 310,
          "waiting_time": 0,
          "load": [1480],
          "arrival": 4,
          "duration": 4,
          "violations": [],
          "distance": 24,
          "id": 0
        },
        {
          "type": "delivery",
          "location": [2.0, 2.0],
          "setup": 0,
          "service": 0,
          "waiting_time": 0,
          "load": [1480],
          "arrival": 552,
          "duration": 92,
          "violations": [],
          "distance": 732,
          "id": 3
        },
        {
          "type": "end",
          "location": [3.0, 3.0],
          "setup": 0,
          "service": 0,
          "waiting_time": 0,
          "load": [0],
          "arrival": 749,
          "duration": 289,
          "violations": [],
          "distance": 2129,
          "id": 0
        }
      ],
      "violations": [],
      "geometry": "qrazFu|dyAc@UCAB@jcT@BA@A..."
    }
  ]
}
```

- The **`summary`** section provides a global overview of the solution:
  - **`cost`**: Total cost of the computed solution.
  - **`routes`**: Number of active routes used.
  - **`unassigned`**: Number of unassigned jobs.
  - **`pickup`** / **`delivery`** / **`amount`**: Total quantities picked up and delivered.
  - **`service`**: Total service time.
  - **`duration`**: Total duration of operations.
  - **`distance`**: Total distance traveled.
  - **`computing_times`**: Time taken for each computation phase (loading, solving, routing).

- **`unassigned`** contains jobs or tasks that could not be scheduled into any route.
  - If empty (**`[]`**), all jobs were successfully assigned.

- The **`routes`** array lists all active vehicle routes.
  - Each route contains:
    - **`vehicle`**: Vehicle ID.
    - **`cost`**, **`pickup`**, **`delivery`**, **`amount`**, **`service`**, **`duration`**, **`distance`**: Same metrics as in **`summary`**, but scoped to the single route.
    - **`steps`**: Ordered sequence of actions the vehicle performs.
    - **`geometry`**: Encoded polyline representing the path on the map.

- Each route includes a **`steps`** array, detailing actions performed by the vehicle in order.
  - Each step includes:
    - **`type`**: The type of action (**`start`**, **`pickup`**, **`delivery`**, **`end`**).
    - **`location`**: Coordinates **`[longitude, latitude]`**.
    - **`service`**: Time spent performing the task.
    - **`arrival`**: Arrival time at this step.
    - **`duration`**: Time taken so far.
    - **`load`**: Vehicle load at this step.
    - **`id`**: Internal identifier of the job or node.

- The **`geometry`** field is an encoded polyline string representing the entire route path, useful for rendering the route on mapping platforms like Mapbox or Leaflet.

Real waste data may be available upon request after provider's evaluation.