# Flowchart Management System

This is a simple system for managing flowcharts consisting of nodes and edges. Each flowchart is represented as a directed
graph, where nodes are connected by edges. The system provides basic CRUD operations to manage flowcharts and additional
features like validation and querying of nodes.

## Features

### Basic CRUD Operations:

1. **Create Flowchart**: Allows the creation of a new flowchart with a unique ID, nodes, and edges.
2. **Fetch Flowchart**: Fetches the details of a flowchart by its ID, including its nodes and edges.
3. **Update Flowchart**: Adds or removes nodes and edges in an existing flowchart.
4. **Delete Flowchart**: Deletes an existing flowchart by its ID.

### Additional features:

1. **Validate Graph**: Validates the flowchart graph for correctness, ensuring that there are no cycles or isolated
   nodes.
2. **Fetch Outgoing Edges**: Fetches all outgoing edges for a given node.
3. **Swagger Documentation**: Basic API documentation using Swagger for ease of use and testing.
4. **Query Nodes Connected to a Specific Node**: Allows querying all nodes connected to a specific node (both directly
   and indirectly).

## Prerequisites

Before running the application, ensure you have the following installed:

- [Java 21](https://adoptopenjdk.net/)
- [Maven](https://maven.apache.org/) (for building the project)
- [Docker](https://www.docker.com/products/docker-desktop) (optional, for Dockerizing the application)
- [Swagger UI](https://swagger.io/tools/swagger-ui/) for API testing (optional, integrated into the system)
- H2 database (for data persistence )
  

## Project Setup

### 1. Clone the repository:

```bash
git clone https://github.com/rinshadkv/flowchart-assignment.git

cd flowchart-assignment
```

### 2. Build the project:

To build the project, use Maven:

```commandline
mvn clean package
```

This will generate the flowchart.jar file in the target directory.

### 3. Run the application:

Without Docker:
Run the application locally using the following command:

```commandline
mvn spring-boot:run
```

This will start the Spring Boot application on http://localhost:8080.

With Docker:
If you prefer to use Docker, first build the Docker image:

```commandline
docker build -t flowchart .
```

Then, run the container:

```commandline
docker run -p 8080:8080 
```

The application will be accessible at http://localhost:8080.

### Deployed link

You can also use the deployed version of the application (hosted on the free tier so  availability may vary, maybe its
takes time to load some contents):
https://flowchart-oppq.onrender.com/

## Swagger Documentation

For API testing and documentation, you can use the Swagger UI at the following link:

[Swagger UI Link](https://flowchart-oppq.onrender.com/swagger-ui/index.html)

For local running, access Swagger UI at: http://localhost:8080/swagger-ui/index.html
