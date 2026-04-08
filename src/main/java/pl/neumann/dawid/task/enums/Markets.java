package pl.neumann.dawid.task.enums;

public enum Markets {
    POLAND("pl-PL"),
    NETHERLANDS("nl-NL"),
    GERMANY("de-DE");

    public final String language;

    private Markets(String language) {
        this.language = language;
    }
}
