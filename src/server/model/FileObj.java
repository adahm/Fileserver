package server.model;

public class FileObj {
    private String name;
    private int size;
    private String owner;
    private Boolean privateFile;//kanse gör til bool
    private Boolean read;
    private Boolean write;
    private Boolean track;



    public FileObj(String name, int size, String owner, Boolean privateFile, Boolean read, Boolean write, Boolean track) {
        this.name = name;
        this.size = size;
        this.owner = owner;
        this.privateFile = privateFile;
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

    public Boolean getPrivateFile() {
        return privateFile;
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

    @Override
    public String toString() {
        return "File{" +
                "name='" + name + '\'' +
                ", size=" + size +
                ", owner='" + owner + '\'' +
                ", privateFile=" + privateFile +
                ", read=" + read +
                ", write=" + write +
                ", track=" + track +
                '}';
    }
}
