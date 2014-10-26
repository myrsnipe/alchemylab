package no.uio.sonen.alchemylab;

public enum WorldMaps {
    FLOWERFIELD("flowerfield.tmx");

    private final String filename;

    WorldMaps(String filename) {
        this.filename = filename;
    }

    public String getFilename() {
        return filename;
    }
}
