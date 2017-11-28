package server.model;

public class File {
    private String name;
    private int size;
    private String owner;
    private Boolean premission;//kanse gör til bool
    private Boolean read;
    private Boolean write;
    private Boolean track;



    public File(String name, int size, String owner, Boolean premission, Boolean read, Boolean write, Boolean track) {
        this.name = name;
        this.size = size;
        this.owner = owner;
        this.premission = premission;
        this.read = read;
        this.write = write;
        this.track = track;

    }
    public String getName() {
        return name;
    }

    public int getSize() {
        return size;
    }

    public String getOwner() {
        return owner;
    }

    public Boolean getPremission() {
        return premission;
    }

    public Boolean getRead() {
        return read;
    }

    public Boolean getWrite() {
        return write;
    }

    public Boolean getTrack() {
        return track;
    }
}
