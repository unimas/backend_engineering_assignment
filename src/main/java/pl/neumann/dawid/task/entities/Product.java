package pl.neumann.dawid.task.entities;

import lombok.Data;

import java.util.List;

@Data
public class Product {
    String Name;
    String description;
    String specs;
    List<String> images;
}
