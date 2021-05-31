import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.time.ZoneOffset;

/*
Данный класс описывает файлы и директории в основном окне
1. Тип файла
2. Имя файла
3. Размер файла
4.Дата изменения файла
 */

public class FileInfo{
    private TypeFiles type;
    private String name;
    private Long size;
    private LocalDateTime dateOfChange;

    public FileInfo(Path path){
        try {
        this.name = path.getFileName().toString();
        this.size = Files.size(path);
        this.type = Files.isDirectory(path) ? TypeFiles.DIRECTORY : TypeFiles.FILE;
        if (this.type == TypeFiles.DIRECTORY) {this.size = -1L;}
        this.dateOfChange = LocalDateTime.ofInstant(Files.getLastModifiedTime(path).toInstant(), ZoneOffset.ofHours(0));
        } catch (IOException e) {
            System.out.println("no information available");
        }
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getSize() {
        return size;
    }

    public void setSize(Long size) {
        this.size = size;
    }

    public LocalDateTime getDateOfChange() {
        return dateOfChange;
    }

    public void setDateOfChange(LocalDateTime dateOfChange) {
        this.dateOfChange = dateOfChange;
    }

    public TypeFiles getType() {
        return type;
    }

    public void setType(TypeFiles type) {
        this.type = type;
    }
}
