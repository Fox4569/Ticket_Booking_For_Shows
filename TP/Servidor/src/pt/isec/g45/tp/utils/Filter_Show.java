package pt.isec.g45.tp.utils;

public enum Filter_Show {
    NOME("Nome"),
    LOCALIDADE("Localidade"),
    GENERO("Genero"),
    DATA("Data");

    public final String label;
    private Filter_Show(String label) {
        this.label = label;
    }
}
