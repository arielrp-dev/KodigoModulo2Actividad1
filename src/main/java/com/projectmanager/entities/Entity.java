package com.projectmanager.entities;

public abstract class Entity {                      // Clase abstracta
    private String id;                              // Atributo id
    private String name;                            // Atributo name
                                                    // Constructor
    public Entity(String id, String name) {         // Constructor con dos parámetros
        this.id = id;                               // Inicializar el atributo id
        this.name = name;                           // Inicializar el atributo name
    }
                                                    // Getters y Setters
    public String getId() {                         // Método getId
        return id;                                  // Retornar el atributo id
    }

    public void setId(String id) {                  // Método setId con un parámetro
        this.id = id;                               // Asignar el valor del parámetro al atributo id
    }

    public String getName() {                       // Método getName
        return name;                                // Retornar el atributo name
    }

    public void setName(String name) {              // Método setName con un parámetro
        this.name = name;                           // Asignar el valor del parámetro al atributo name
    }
}
