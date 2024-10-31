package com.projectmanager.services;

import com.projectmanager.entities.Project;
import com.projectmanager.entities.Task;
import com.projectmanager.entities.User;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.JsonNode;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

public class JsonFileManager implements Persistable {
    private static final String PROJECTS_FILE = "src/main/resources/projects.json";
    private static final String TASKS_FILE = "src/main/resources/tasks.json";
    private static final String USERS_FILE = "src/main/resources/users.json";
    private final ObjectMapper objectMapper;

    public JsonFileManager() {

        this.objectMapper = new ObjectMapper();
    }

    public void loadProjects(List<Project> projects) throws IOException {
        File file = new File(PROJECTS_FILE);

        // Verificar si el archivo existe antes de intentar leerlo
        if (file.exists()) {
            // Leer el archivo JSON y convertirlo en un JsonNode
            JsonNode jsonNode = objectMapper.readTree(file);

            // Limpiar la lista de proyectos antes de cargar nuevos
            projects.clear();

            // Iterar sobre cada elemento del array JSON (asumiendo que el JSON contiene una lista de proyectos)
            if (jsonNode.isArray()) {
                for (JsonNode node : jsonNode) {
                    // Extraer los datos del nodo JSON
                    String id = node.get("id").asText();
                    String name = node.get("name").asText();
                    String description = node.get("description").asText();
                    String status = node.get("status").asText();

                    // Crear un nuevo objeto Project con los datos extraídos
                    Project project = new Project(id, name, description, status);

                    // Agregar el proyecto a la lista
                    projects.add(project);
                }
            }
        } else {
            System.out.println("El archivo de proyectos no existe.");
        }
    }

    public void saveProjects(List<Project> projects) throws IOException {
        // Convertir la lista de proyectos a formato JSON
        String json = objectMapper.writeValueAsString(projects);

        // Escribir el JSON en el archivo de proyectos
        Files.write(Paths.get(PROJECTS_FILE), json.getBytes());
    }

    public void loadTasks(List<Task> tasks) throws IOException {
        File file = new File(TASKS_FILE);

        if (file.exists()) {
            JsonNode jsonNode = objectMapper.readTree(file);
            tasks.clear(); // Limpiar la lista antes de cargar nuevas tareas

            if (jsonNode.isArray()) {
                for (JsonNode node : jsonNode) {
                    String id = node.get("id").asText();
                    String name = node.get("name").asText();
                    String description = node.get("description").asText();
                    String status = node.get("status").asText();
                    String projectId = node.get("projectId").asText();

                    Task task = new Task(id, name, description, status, projectId);
                    tasks.add(task);
                }
            }
        } else {
            System.out.println("El archivo de tareas no existe.");
        }
    }

    public void saveTasks(List<Task> tasks) throws IOException {

        // Convertir la lista de tareas a formato JSON
        String json = objectMapper.writeValueAsString(tasks);

        // Escribir el JSON en el archivo
        Files.write(Paths.get(TASKS_FILE), json.getBytes());

    }

    public void loadUsers(List<User> users) throws IOException {
        File file = new File(USERS_FILE);

        if (file.exists()) {
            JsonNode jsonNode = objectMapper.readTree(file);
            users.clear(); // Limpiar la lista antes de cargar nuevos usuarios

            if (jsonNode.isArray()) {
                for (JsonNode node : jsonNode) {
                    String id = node.get("id").asText();
                    String name = node.get("name").asText();
                    String email = node.get("email").asText();
                    String phone = node.get("phoneNumber").asText();
                    // IMPLEMENTACION CORRECTA PARA extraer los IDs de las tareas asignadas
                    JsonNode assignedTasksNode = node.get("assignedTasksIds");

                    // Verificar si "assignedTasksIds" es un array
                    String[] validTaskIds = new String[assignedTasksNode.size()];

                    for (int i = 0; i < assignedTasksNode.size(); i++) {
                        validTaskIds[i] = assignedTasksNode.get(i).asText().trim(); // Extraer cada ID y eliminar espacios
                    }

                    // Crear el nuevo usuario
                    User user = new User(id, name, email, phone, List.of(validTaskIds));

                    // Agregar el usuario a la lista de usuarios
                    users.add(user);
                }
            }
        } else {
            System.out.println("El archivo de usuarios no existe.");
        }
    }

    public void saveUsers(List<User> users) throws IOException {
        String json = objectMapper.writeValueAsString(users);
        Files.write(Paths.get(USERS_FILE), json.getBytes());
    }
}
