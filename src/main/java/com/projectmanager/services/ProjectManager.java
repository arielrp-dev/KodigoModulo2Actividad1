package com.projectmanager.services;

import com.projectmanager.entities.Project;
import com.projectmanager.entities.Task;
import com.projectmanager.entities.User;
import com.projectmanager.gui.*;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.ArrayList;
import java.util.List;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Objects;

public class ProjectManager extends Component {
    private static List<Project> projects;
    private final List<Task> tasks;
    private final List<User> users;

    public ProjectManager() {
        projects = new ArrayList<>();
        this.tasks = new ArrayList<>();
        this.users = new ArrayList<>();
    }

    public List<String> getAssignedTasksIds(String selectedUserId) throws IOException {
        // Crear instancia de JsonFileManager
        JsonFileManager jsonFileManager = new JsonFileManager();

        jsonFileManager.loadUsers(users);
        jsonFileManager.loadTasks(tasks);

        // Buscar el usuario con el ID seleccionado
        User selectedUser = null;
        for (User user : users) {
            if (user.getId().equals(selectedUserId)) {
                selectedUser = user;
                break;
            }
        }

        // Si el usuario no fue encontrado, mostrar un mensaje de error
        if (selectedUser == null) {
            throw new IllegalArgumentException("User with ID " + selectedUserId + " not found.");
        }
        // Retornar la lista de IDs de tareas asignadas
        return selectedUser.getAssignedTasksIds();
    }

    public Project getProjectById(String projectId) {
        JsonFileManager jsonFileManager = new JsonFileManager();

        try {
            jsonFileManager.loadProjects(projects);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        for (Project project : projects) {
            if (Objects.equals(project.getId(), projectId)) {
                return project;
            }
        }
        return null;
    }

    // Métodos de acción para las opciones de "Add"
    public void addProjectAction() throws IOException {
        // Lógica para agregar un proyecto
        JsonFileManager jsonFileManager = new JsonFileManager();
        ProjectPanel projectPanel = new ProjectPanel();
        jsonFileManager.loadProjects(projects);


        String[] fieldLabels = {"Project Name", "Project Description", "Project Status"};
        // Crear un FormDialog para ingresar los datos del nuevo proyecto
        FormDialog formDialog = new FormDialog(this, "Add New Project", fieldLabels);
        formDialog.setModalityType(Dialog.ModalityType.APPLICATION_MODAL); // Hacer el diálogo modal
        formDialog.pack(); // Ajustar el tamaño al contenido
        formDialog.setLocationRelativeTo(this); // Centrar el diálogo
        formDialog.setVisible(true); // Mostrar el diálogo de manera modal

        // Verificar si se confirmó el formulario
        if (formDialog.isConfirmed()) {
            // Obtener los valores ingresados en el formulario
            String[] fieldValues = formDialog.getFieldValues();

            if (fieldValues != null && fieldValues.length == 3) { // Validar que los valores están completos
                String projectName = fieldValues[0].trim();
                String projectDescription = fieldValues[1].trim();
                String projectStatus = fieldValues[2].trim();

                // Validar los campos
                if (!projectName.isEmpty() && !projectStatus.isEmpty()) {
                    // Generar un nuevo ID para el proyecto basado en la fecha y hora invertida
                    String newProjectId = generateReversedTimestampID();

                    // Crear un nuevo objeto Project
                    Project newProject = new Project(newProjectId, projectName, projectDescription, projectStatus);

                    // Agregar el proyecto a la lista
                    projects.add(newProject);

                    // Actualizar la tabla de proyectos
                    projectPanel.loadProjects(projects);

                    // Mostrar un mensaje de éxito
                    UIUtils.showInfoMessage(this, "Project added successfully.");
                } else {
                    UIUtils.showErrorMessage(this, "Project Name and Status are required.");
                }
            } else {
                UIUtils.showErrorMessage(this, "Form data is incomplete.");
            }
        }

        // Guardar los proyectos en el archivo JSON
        jsonFileManager.saveProjects(projects);
    }

    public void addTaskAction() throws IOException {
        // Lógica para agregar una tarea
        JsonFileManager jsonFileManager = new JsonFileManager();
        TaskPanel taskPanel = new TaskPanel();
        ProjectManager projectManager = new ProjectManager();

        // Cargar los proyectos y tareas desde el archivo JSON
        jsonFileManager.loadTasks(tasks);
        jsonFileManager.loadProjects(projects);

        // Etiquetas para los campos de entrada
        String[] fieldLabels = {"Task Name", "Task Description", "Task Status", "Project ID"};

        // Crear un FormDialog para ingresar los datos de la nueva tarea
        FormDialogTask formDialogTask = new FormDialogTask(this, "Add New Task", fieldLabels);
        formDialogTask.setModalityType(Dialog.ModalityType.APPLICATION_MODAL); // Hacer el diálogo modal
        formDialogTask.pack(); // Ajustar el tamaño al contenido
        formDialogTask.setLocationRelativeTo(this); // Centrar el diálogo
        formDialogTask.setVisible(true);

        // Verificar si se confirmó el formulario
        if (formDialogTask.isConfirmed()) {
            // Obtener los valores ingresados en el formulario
            String[] fieldValues = formDialogTask.getFieldValues();

            if (fieldValues != null && fieldValues.length == 4) { // Validar que los valores están completos
                String taskName = fieldValues[0].trim();
                String taskDescription = fieldValues[1].trim();
                String taskStatus = fieldValues[2].trim();
                String projectIdString = fieldValues[3].trim();

                // Validar los campos (puedes agregar más validaciones si es necesario)
                if (!taskName.isEmpty() && !taskStatus.isEmpty() && !projectIdString.isEmpty()) {
                    try {

                        // Verificar que el ID del proyecto exista en la lista de proyectos
                        Project project = projectManager.getProjectById(projectIdString);
                        if (project == null) {
                            UIUtils.showErrorMessage(this, "Project with ID " + projectIdString + " does not exist.");
                            return;
                        }

                        // Generar un nuevo ID para la tarea basado en la fecha y hora invertida
                        String newTaskId = generateReversedTimestampID();

                        // Crear un nuevo objeto Task
                        Task newTask = new Task(newTaskId, taskName, taskDescription, taskStatus, projectIdString);

                        // Agregar la tarea a la lista
                        tasks.add(newTask);

                        // Actualizar la tabla de tareas
                        taskPanel.loadTasks(tasks);

                        // Mostrar un mensaje de éxito
                        UIUtils.showInfoMessage(this, "Task added successfully.");
                    } catch (NumberFormatException e) {
                        UIUtils.showErrorMessage(this, "Invalid Project ID format.");
                    }
                } else {
                    UIUtils.showErrorMessage(this, "All fields are required.");
                }
            } else {
                UIUtils.showErrorMessage(this, "Form data is incomplete.");
            }
        }

        // Guardar los cambios en el archivo JSON
        jsonFileManager.saveTasks(tasks);
        jsonFileManager.saveProjects(projects);
    }

    public void addUserAction() throws IOException {
        // Lógica para agregar un usuario
        JsonFileManager jsonFileManager = new JsonFileManager();
        UserPanel userPanel = new UserPanel();

        // Cargar los usuarios y las tareas desde el archivo JSON
        jsonFileManager.loadUsers(users);
        jsonFileManager.loadTasks(tasks);

        // Etiquetas para los campos de entrada
        String[] fieldLabels = {"User Name", "User Email", "User Phone Number", "Assigned Task IDs (comma-separated)"};

        /// Crear un FormDialog para ingresar los datos del nuevo usuario
        FormDialogUser formDialogUser = new FormDialogUser(this, "Add New User", fieldLabels);
        formDialogUser.setModalityType(Dialog.ModalityType.APPLICATION_MODAL); // Hacer el diálogo modal
        formDialogUser.setLocationRelativeTo(this); // Centrar el diálogo
        formDialogUser.pack(); // Ajustar el tamaño al contenido
        formDialogUser.setVisible(true); // Hacer visible el diálogo

        // Verificar si se confirmó el formulario
        if (formDialogUser.isConfirmed()) {
            // Obtener los valores ingresados en el formulario
            String[] fieldValues = formDialogUser.getFieldValues();
            System.out.println("length: " + fieldValues.length);

            if (fieldValues.length == 5) { // Validar que los valores están completos
                String userName = fieldValues[0].trim(); // obtener el nombre del usuario
                String userEmail = fieldValues[1].trim();   // obtener el correo del usuario
                String userPhoneNumber = fieldValues[2].trim(); // obtener el número de teléfono del usuario
                String assignedTaskIdsString = fieldValues[3].trim(); // obtener los IDs de las tareas asignadas
                System.out.println("Assigned Task ID: " + assignedTaskIdsString); // ID ingresado por el usuario

                // Validar los campos (puedes agregar más validaciones si es necesario)
                if (!userName.isEmpty() && !userEmail.isEmpty() && !userPhoneNumber.isEmpty()) {
                    // Generar un nuevo ID para el usuario utilizando el nombre y la fecha invertida
                    String newUserId = generateUserId(userName);

                    // Convertir los IDs de tareas de string a array de String
                    String[] assignedTaskIdsArray = assignedTaskIdsString.split(","); // Separar por comas
                    for (int i = 0; i < assignedTaskIdsArray.length; i++) {
                        assignedTaskIdsArray[i] = assignedTaskIdsArray[i].trim(); // Eliminar espacios en blanco
                    }

                    System.out.println("Assigned Task ID: " + Arrays.toString(assignedTaskIdsArray)); // ID ingresado por el usuario
                    for (Task task : tasks) {
                        System.out.println("Task ID in List: " + task.getId()); // ID de la lista de tareas
                    }

                    // Validar que los ID de las tareas existan
                    List<String> validTaskIds = new ArrayList<>();
                    for (String taskId : assignedTaskIdsArray) {
                        boolean taskExists = tasks.stream().anyMatch(task -> task.getId().equals(taskId));
                        if (taskExists) {
                            validTaskIds.add(taskId);
                        } else {
                            UIUtils.showErrorMessage(this, "Task ID " + taskId + " does not exist.");
                            return; // Terminar si un ID no es válido
                        }
                    }

                    // Crear un nuevo objeto User con los ID de las tareas asignadas
                    User newUser = new User(newUserId, userName, userEmail, userPhoneNumber, validTaskIds); // AQUI ME QUEDE

                    // Agregar el usuario a la lista
                    users.add(newUser);

                    // Actualizar la tabla de usuarios
                    userPanel.loadUsers(users);

                    // Mostrar un mensaje de éxito
                    UIUtils.showInfoMessage(this, "User added successfully with assigned tasks.");
                } else {
                    UIUtils.showErrorMessage(this, "All fields are required.");
                }
            } else {
                UIUtils.showErrorMessage(this, "Form data is incomplete.");
            }
        }

        // Guardar los cambios en el archivo JSON
        jsonFileManager.saveUsers(users);
    }

    // Métodos de acción para las opciones de "Update"
    public void updateProjectAction() throws IOException {
        // Lógica para actualizar un proyecto
        JsonFileManager jsonFileManager = new JsonFileManager();
        ProjectPanel projectPanel = new ProjectPanel();

        // Cargar los proyectos desde el archivo JSON
        jsonFileManager.loadProjects(projects);

        // Verificar si hay proyectos cargados
        if (projects.isEmpty()) {
            UIUtils.showErrorMessage(this, "No projects available to update.");
            return;
        }

        // Crear una lista desplegable con los proyectos en el formato "ID - Nombre del Proyecto"
        String[] projectOptions = new String[projects.size()];
        for (int i = 0; i < projects.size(); i++) {
            Project project = projects.get(i);
            projectOptions[i] = project.getId() + " - " + project.getName();
        }

        // Mostrar el cuadro de diálogo para seleccionar el proyecto a actualizar
        String selectedProject = (String) JOptionPane.showInputDialog(
                this,
                "Select a project to update:",
                "Update Project",
                JOptionPane.PLAIN_MESSAGE,
                null,
                projectOptions,
                projectOptions[0] // Primer proyecto seleccionado por defecto
        );

        // Si el usuario seleccionó un proyecto
        if (selectedProject != null && !selectedProject.isEmpty()) {
            // Obtener el ID del proyecto seleccionado (el ID está antes del primer guion '-')
            String selectedProjectId = selectedProject.split(" - ")[0];

            // Ejemplo de cómo obtener el proyecto desde la lista de proyectos
            Project projectToUpdate = projects.stream()
                    .filter(project -> project.getId().equals(selectedProjectId)) // Asegúrate de tener selectedProjectId
                    .findFirst()
                    .orElse(null);

            // Verificar si el proyecto existe
            if (projectToUpdate == null) {
                UIUtils.showErrorMessage(this, "Selected project does not exist.");
                return;
            }

            // Etiquetas para los campos de entrada (nombre, descripción, estado del proyecto)
            String[] fieldLabels = {"Project Name", "Project Description", "Project Status"};

            // Crear un formulario para editar los detalles del proyecto
            FormDialog formDialog = new FormDialog(this, "Update Project", fieldLabels);

            // Prellenar los campos con la información actual del proyecto
            formDialog.setFieldValue(0, projectToUpdate.getName());
            formDialog.setFieldValue(1, projectToUpdate.getDescription());

            formDialog.setModalityType(Dialog.ModalityType.APPLICATION_MODAL); // Hacer el diálogo modal
            formDialog.pack(); // Ajustar el tamaño al contenido
            formDialog.setLocationRelativeTo(this); // Centrar el diálogo
            formDialog.setVisible(true); // Mostrar el diálogo

            // Verificar si se confirmó el formulario
            if (formDialog.isConfirmed()) {
                // Obtener los nuevos valores ingresados en el formulario
                String[] fieldValues = formDialog.getFieldValues();

                if (fieldValues != null && fieldValues.length == 3) { // Validar que los valores están completos
                    String projectName = fieldValues[0].trim();
                    String projectDescription = fieldValues[1].trim();
                    String projectStatus = fieldValues[2].trim();

                    // Validar que los campos importantes no estén vacíos
                    if (!projectName.isEmpty() && !projectStatus.isEmpty()) {
                        // Actualizar el proyecto con los nuevos valores
                        projectToUpdate.setName(projectName);
                        projectToUpdate.setDescription(projectDescription);
                        projectToUpdate.setStatus(projectStatus);

                        // Guardar los cambios en el archivo JSON
                        jsonFileManager.saveProjects(projects);

                        // Crear una lista temporal para almacenar las tareas
                        List<Project> refreshedProjects = new ArrayList<>();

                        // Cargar las tareas desde el archivo JSON
                        jsonFileManager.loadProjects(refreshedProjects);

                        // Actualizar la tabla de proyectos
                        projectPanel.loadProjects(refreshedProjects);

                        projectPanel.refreshProjects();

                        // Mostrar un mensaje de éxito
                        UIUtils.showInfoMessage(this, "Project updated successfully.");
                    } else {
                        UIUtils.showErrorMessage(this, "Project Name and Status are required.");
                    }
                } else {
                    UIUtils.showErrorMessage(this, "Form data is incomplete.");
                }
            }
        } else {
            UIUtils.showErrorMessage(this, "No project selected.");
        }

    }

    public void updateTaskAction() throws IOException {
        // Lógica para actualizar una tarea
        JsonFileManager jsonFileManager = new JsonFileManager();
        TaskPanel taskPanel = new TaskPanel();
        ProjectManager projectManager = new ProjectManager();

        // Cargar los proyectos y tareas desde el archivo JSON
        jsonFileManager.loadTasks(tasks);
        jsonFileManager.loadProjects(projects);

        // Verificar si hay tareas disponibles
        if (tasks.isEmpty()) {
            UIUtils.showErrorMessage(this, "No tasks available to update.");
            return;
        }

        // Crear una lista desplegable con las tareas en el formato "ID - Nombre de la Tarea"
        String[] taskOptions = new String[tasks.size()];
        for (int i = 0; i < tasks.size(); i++) {
            Task task = tasks.get(i);
            taskOptions[i] = task.getId() + " - " + task.getName();
        }

        // Mostrar el cuadro de diálogo para seleccionar la tarea a actualizar
        String selectedTask = (String) JOptionPane.showInputDialog(
                this,
                "Select a task to update:",
                "Update Task",
                JOptionPane.PLAIN_MESSAGE,
                null,
                taskOptions,
                taskOptions[0] // Primer tarea seleccionada por defecto
        );

        // Verificar si el usuario seleccionó una tarea
        if (selectedTask != null && !selectedTask.isEmpty()) {
            // Obtener el ID de la tarea seleccionada (el ID está antes del primer guion '-')
            String selectedTaskId = selectedTask.split(" - ")[0];

            // Encontrar la tarea en la lista de tareas usando su ID
            Task taskToUpdate = tasks.stream()
                    .filter(task -> task.getId().equals(selectedTaskId))
                    .findFirst()
                    .orElse(null);

            // Verificar si la tarea existe
            if (taskToUpdate == null) {
                UIUtils.showErrorMessage(this, "Selected task does not exist.");
                return;
            }

            // Etiquetas para los campos de entrada (nombre, descripción, estado, proyecto)
            String[] fieldLabels = {"Task Name", "Task Description", "Task Status", "Project ID"};

            // Crear un formulario para editar los detalles de la tarea
            FormDialogTask formDialogTask = new FormDialogTask(this, "Update Task", fieldLabels);

            // Prellenar los campos con la información actual de la tarea
            formDialogTask.setFieldValue(0, taskToUpdate.getName());
            formDialogTask.setFieldValue(1, taskToUpdate.getDescription());

            formDialogTask.setModalityType(Dialog.ModalityType.APPLICATION_MODAL); // Hacer el diálogo modal
            formDialogTask.pack(); // Ajustar el tamaño al contenido
            formDialogTask.setLocationRelativeTo(this); // Centrar el diálogo
            formDialogTask.setVisible(true); // Mostrar el diálogo

            // Verificar si se confirmó el formulario
            if (formDialogTask.isConfirmed()) {
                // Obtener los nuevos valores ingresados en el formulario
                String[] fieldValues = formDialogTask.getFieldValues();
                System.out.println("Name: " + fieldValues[0].trim());
                System.out.println("Description: " + fieldValues[1].trim());
                System.out.println("Status: " + fieldValues[2].trim());
                System.out.println("Project ID: " + fieldValues[3].trim());

                if (fieldValues.length == 4) { // Validar que los valores están completos
                    String taskName = fieldValues[0].trim();
                    String taskDescription = fieldValues[1].trim();
                    String taskStatus = fieldValues[2].trim();
                    String projectIdString = fieldValues[3].trim();

                    // Validar los campos (puedes agregar más validaciones si es necesario)
                    if (!taskName.isEmpty() && !taskStatus.isEmpty() && !projectIdString.isEmpty()) {
                        // Verificar que el ID del proyecto exista en la lista de proyectos
                        Project project = projectManager.getProjectById(projectIdString);
                        if (project == null) {
                            UIUtils.showErrorMessage(this, "Project with ID " + projectIdString + " does not exist.");
                            return;
                        }

                        // Actualizar la tarea con los nuevos valores
                        taskToUpdate.setName(taskName);
                        taskToUpdate.setDescription(taskDescription);
                        taskToUpdate.setStatus(taskStatus);
                        taskToUpdate.setProjectId(projectIdString);

                        // Mostrar las tareas en formato de lista en consola
                        System.out.println("Saving the following tasks:");
                        for (Task task : tasks) {
                            System.out.println("- Task ID: " + task.getId() + ", Name: " + task.getName() +
                                    ", Description: " + task.getDescription() +
                                    ", Status: " + task.getStatus() +
                                    ", Project ID: " + task.getProjectId());
                        }

                        System.out.println("Task updated: " + taskToUpdate.getName() + " - " + taskToUpdate.getDescription() + " - " + taskToUpdate.getStatus() + " - " + taskToUpdate.getProjectId());

                        // Guardar los cambios en el archivo JSON
                        jsonFileManager.saveTasks(tasks);

                        // Actualizar la tabla de tareas
                        taskPanel.loadTasks(tasks);

                        // Mostrar un mensaje de éxito
                        UIUtils.showInfoMessage(this, "Task updated successfully.");
                    } else {
                        UIUtils.showErrorMessage(this, "All fields are required.");
                    }
                } else {
                    UIUtils.showErrorMessage(this, "Form data is incomplete.");
                }
            }
        } else {
            UIUtils.showErrorMessage(this, "No task selected.");
        }
    }

    public void updateUserAction() throws IOException {
        ProjectManager projectManager = new ProjectManager();

        // Lógica para actualizar un usuario
        JsonFileManager jsonFileManager = new JsonFileManager();
        UserPanel userPanel = new UserPanel();

        // Cargar los usuarios desde el archivo JSON
        jsonFileManager.loadUsers(users);

        // Verificar si hay usuarios disponibles
        if (users.isEmpty()) {
            UIUtils.showErrorMessage(this, "No users available to update.");
            return;
        }

        // Crear una lista desplegable con los usuarios en el formato "ID - Nombre del Usuario"
        String[] userOptions = new String[users.size()];
        for (int i = 0; i < users.size(); i++) {
            User user = users.get(i);
            userOptions[i] = user.getId() + " - " + user.getName();
        }

        // Mostrar el cuadro de diálogo para seleccionar el usuario a actualizar
        String selectedUser = (String) JOptionPane.showInputDialog(
                this,
                "Select a user to update:",
                "Update User",
                JOptionPane.PLAIN_MESSAGE,
                null,
                userOptions,
                userOptions[0] // Primer usuario seleccionado por defecto
        );

        // Verificar si el usuario seleccionó un usuario
        if (selectedUser != null && !selectedUser.isEmpty()) {
            // Obtener el ID del usuario seleccionado (el ID está antes del primer guion '-')
            String selectedUserId = selectedUser.split(" - ")[0];

            // Ejemplo de cómo obtener el usuario desde la lista de usuarios
            User userToUpdate = users.stream()
                    .filter(user -> user.getId().equals(selectedUserId)) // Asegúrate de tener selectedUserId
                    .findFirst()
                    .orElse(null);

            // Verificar si el usuario existe
            if (userToUpdate == null) {
                UIUtils.showErrorMessage(this, "Selected user does not exist.");
                return; // Terminar si el usuario no existe
            }

            // Etiquetas para los campos de entrada (nombre, correo, teléfono, IDs de tareas)
            String[] fieldLabels = {"User Name", "User Email", "User Phone", "Assigned Task IDs"};

            // Crear un formulario para editar los detalles del usuario
            FormDialogUser formDialogUser = new FormDialogUser(this, "Update User", fieldLabels);

            // Prellenar los campos con la información actual del usuario
            formDialogUser.setFieldValue(0, userToUpdate.getName());
            formDialogUser.setFieldValue(1, userToUpdate.getEmail());
            formDialogUser.setFieldValue(2, userToUpdate.getPhoneNumber());

            // Obtener las tareas asignadas al usuario y preseleccionarlas en los checkboxes
            List<String> assignedTaskIds = projectManager.getAssignedTasksIds(selectedUserId);
            formDialogUser.setSelectedTasks(assignedTaskIds);

            formDialogUser.setModalityType(Dialog.ModalityType.APPLICATION_MODAL); // Hacer el diálogo modal
            formDialogUser.pack(); // Ajustar el tamaño al contenido
            formDialogUser.setLocationRelativeTo(this); // Centrar el diálogo
            formDialogUser.setVisible(true); // Mostrar el diálogo

            // Verificar si se confirmó el formulario
            if (formDialogUser.isConfirmed()) {
                // Obtener los nuevos valores ingresados en el formulario
                String[] fieldValues = formDialogUser.getFieldValues();

                if (fieldValues != null && fieldValues.length == 5) { // Validar que los valores están completos
                    String userName = fieldValues[0].trim();
                    String userEmail = fieldValues[1].trim();
                    String userPhone = fieldValues[2].trim();
                    String taskIdsString = fieldValues[3].trim();

                    // Validar los campos (puedes agregar más validaciones si es necesario)
                    if (!userName.isEmpty() && !userEmail.isEmpty() && !userPhone.isEmpty() && !taskIdsString.isEmpty()) {
                        // Convertir la cadena de IDs de tareas en un array
                        String[] taskAssignedIds = taskIdsString.split(",");

                        // Actualizar el usuario con los nuevos valores
                        userToUpdate.setName(userName);
                        userToUpdate.setEmail(userEmail);
                        userToUpdate.setPhoneNumber(userPhone);
                        userToUpdate.setAssignedTasksIds(taskAssignedIds);

                        // Guardar los cambios en el archivo JSON
                        jsonFileManager.saveUsers(users);

                        // Actualizar la tabla de usuarios
                        userPanel.loadUsers(users);

                        // Mostrar un mensaje de éxito
                        UIUtils.showInfoMessage(this, "User updated successfully.");
                    } else {
                        UIUtils.showErrorMessage(this, "All fields are required.");
                    }
                } else {
                    UIUtils.showErrorMessage(this, "Form data is incomplete.");
                }
            }
        } else {
            UIUtils.showErrorMessage(this, "No user selected.");
        }
        jsonFileManager.saveUsers(users);
    }


    // Métodos de acción para las opciones de "Delete"
    public void deleteProjectAction() throws IOException {
        JsonFileManager jsonFileManager = new JsonFileManager();

        // Cargar los proyectos desde el archivo JSON
        jsonFileManager.loadProjects(projects);

        // Verificar si hay proyectos disponibles
        if (projects.isEmpty()) {
            UIUtils.showErrorMessage(this, "No projects available to delete.");
            return;
        }

        // Crear una lista desplegable con los proyectos en el formato "ID - Nombre del Proyecto"
        String[] projectOptions = new String[projects.size()];
        for (int i = 0; i < projects.size(); i++) {
            Project project = projects.get(i);
            projectOptions[i] = project.getId() + " - " + project.getName();
        }

        // Mostrar el cuadro de diálogo para seleccionar el proyecto a eliminar
        String selectedProject = (String) JOptionPane.showInputDialog(
                this,
                "Select a project to delete:",
                "Delete Project",
                JOptionPane.PLAIN_MESSAGE,
                null,
                projectOptions,
                projectOptions[0] // Primer proyecto seleccionado por defecto
        );

        // Verificar si el usuario seleccionó un proyecto
        if (selectedProject != null && !selectedProject.isEmpty()) {
            // Obtener el ID del proyecto seleccionado (el ID está antes del primer guion '-')
            String selectedProjectId = selectedProject.split(" - ")[0];

            // Obtener el proyecto desde la lista
            Project projectToDelete = projects.stream()
                    .filter(project -> project.getId().equals(selectedProjectId))
                    .findFirst()
                    .orElse(null);

            if (projectToDelete == null) {
                UIUtils.showErrorMessage(this, "Selected project does not exist.");
                return;
            }

            // Eliminar el proyecto de la lista
            projects.remove(projectToDelete);

            // Guardar los cambios en el archivo JSON
            jsonFileManager.saveProjects(projects);

            // Mostrar un mensaje de éxito
            UIUtils.showInfoMessage(this, "Project deleted successfully.");
        } else {
            UIUtils.showErrorMessage(this, "No project selected.");
        }
    }

    public void deleteTaskAction() throws IOException {
        JsonFileManager jsonFileManager = new JsonFileManager();
        TaskPanel taskPanel = new TaskPanel();

        // Cargar las tareas desde el archivo JSON
        jsonFileManager.loadTasks(tasks);
        // Cargar los proyectos si es necesario (puedes incluir esto si necesitas verificar proyectos)
        jsonFileManager.loadProjects(projects);

        // Verificar si hay tareas disponibles
        if (tasks.isEmpty()) {
            UIUtils.showErrorMessage(this, "No tasks available to delete.");
            return;
        }

        // Crear una lista desplegable con las tareas en el formato "ID - Nombre de la Tarea"
        String[] taskOptions = new String[tasks.size()];
        for (int i = 0; i < tasks.size(); i++) {
            Task task = tasks.get(i);
            taskOptions[i] = task.getId() + " - " + task.getName();
        }

        // Mostrar el cuadro de diálogo para seleccionar la tarea a eliminar
        String selectedTask = (String) JOptionPane.showInputDialog(
                this,
                "Select a task to delete:",
                "Delete Task",
                JOptionPane.PLAIN_MESSAGE,
                null,
                taskOptions,
                taskOptions[0] // Primer tarea seleccionada por defecto
        );

        // Verificar si el usuario seleccionó una tarea
        if (selectedTask != null && !selectedTask.isEmpty()) {
            // Obtener el ID de la tarea seleccionada (el ID está antes del primer guion '-')
            String selectedTaskId = selectedTask.split(" - ")[0];

            // Obtener la tarea desde la lista
            Task taskToDelete = tasks.stream()
                    .filter(task -> task.getId().equals(selectedTaskId))
                    .findFirst()
                    .orElse(null);

            if (taskToDelete == null) {
                UIUtils.showErrorMessage(this, "Selected task does not exist.");
                return;
            }

            // Eliminar la tarea de la lista
            tasks.remove(taskToDelete);

            // Guardar los cambios en el archivo JSON
            jsonFileManager.saveTasks(tasks);

            // Actualizar la tabla de tareas
            taskPanel.loadTasks(tasks);

            // Mostrar un mensaje de éxito
            UIUtils.showInfoMessage(this, "Task deleted successfully.");
        } else {
            UIUtils.showErrorMessage(this, "No task selected.");
        }
    }

    public void deleteUserAction() throws IOException {
        JsonFileManager jsonFileManager = new JsonFileManager();

        // Cargar los usuarios desde el archivo JSON
        jsonFileManager.loadUsers(users);

        // Verificar si hay usuarios disponibles
        if (users.isEmpty()) {
            UIUtils.showErrorMessage(this, "No users available to delete.");
            return;
        }

        // Crear una lista desplegable con los usuarios en el formato "ID - Nombre del Usuario"
        String[] userOptions = new String[users.size()];
        for (int i = 0; i < users.size(); i++) {
            User user = users.get(i);
            userOptions[i] = user.getId() + " - " + user.getName();
        }

        // Mostrar el cuadro de diálogo para seleccionar el usuario a eliminar
        String selectedUser = (String) JOptionPane.showInputDialog(
                this,
                "Select a user to delete:",
                "Delete User",
                JOptionPane.PLAIN_MESSAGE,
                null,
                userOptions,
                userOptions[0] // Primer usuario seleccionado por defecto
        );

        // Verificar si el usuario seleccionó un usuario
        if (selectedUser != null && !selectedUser.isEmpty()) {
            // Obtener el ID del usuario seleccionado (el ID está antes del primer guion '-')
            String selectedUserId = selectedUser.split(" - ")[0];

            // Obtener el usuario desde la lista
            User userToDelete = users.stream()
                    .filter(user -> user.getId().equals(selectedUserId))
                    .findFirst()
                    .orElse(null);

            if (userToDelete == null) {
                UIUtils.showErrorMessage(this, "Selected user does not exist.");
                return;
            }

            // Eliminar el usuario de la lista
            users.remove(userToDelete);

            // Guardar los cambios en el archivo JSON
            jsonFileManager.saveUsers(users); // Asegúrate de que este método exista

            // Mostrar un mensaje de éxito
            UIUtils.showInfoMessage(this, "User deleted successfully.");
        } else {
            UIUtils.showErrorMessage(this, "No user selected.");
        }
    }

    public static String generateReversedTimestampID() {
        // Obtener la fecha y hora actuales
        LocalDateTime now = LocalDateTime.now();

        // Formatear la fecha y hora en el formato deseado: aaaammddhhmm
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMddHHmm");
        String timestamp = now.format(formatter);


        // Retornar el ID generado
        return new StringBuilder(timestamp).reverse().toString();
    }

    // Método para obtener las iniciales del nombre completo
    private static String getInitials(String fullName) {
        StringBuilder initials = new StringBuilder();
        String[] nameParts = fullName.split("\\s+"); // Separar el nombre por espacios
        for (String part : nameParts) {
            if (!part.isEmpty()) {
                initials.append(part.charAt(0)); // Agregar la primera letra de cada palabra
            }
        }
        return initials.toString().toUpperCase(); // Devolver las iniciales en mayúsculas
    }

    // Método para obtener los dígitos de la fecha de creación al revés
    private static String getReversedTimestamp() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmm");
        String currentTimestamp = dateFormat.format(new Date());
        return new StringBuilder(currentTimestamp).reverse().toString(); // Invertir los dígitos de la fecha
    }

    // Método para generar el ID del usuario
    public static String generateUserId(String fullName) {
        String initials = getInitials(fullName); // Obtener iniciales del nombre
        String reversedTimestamp = getReversedTimestamp(); // Obtener la fecha invertida
        return initials + reversedTimestamp; // Concatenar las iniciales y la fecha invertida
    }
}
