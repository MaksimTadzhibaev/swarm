/*
Два типа данных:
F - Файл
D - Дирректория
 */

public enum TypeFiles {
    FILE ("F"), DIRECTORY ("D");

    private String nameType;

    public String getNameType() {
        return nameType;
    }

    TypeFiles(String nameType) {
        this.nameType = nameType;
    }
}
