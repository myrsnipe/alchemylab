package no.uio.sonen.alchemylab;

public enum WorldMaps {
    WORLD1("smb-1-1.tmx");

    private final String filename;

    WorldMaps(String filename) {
        this.filename = filename;
    }

    public String getFilename() {
        return filename;
    }
}
