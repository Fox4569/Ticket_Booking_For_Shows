package pt.isec.g45.tp.utils;

public enum FilterShow {
    NOME("descricao"),
    LOCALIDADE("localidade"),
    GENERO("tipo"),
    DATA("data_hora");

    public final String label;
    private FilterShow(String label) {
        this.label = label;
    }
}
