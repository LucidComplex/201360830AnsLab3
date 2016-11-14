/**
 * Created by tan on 11/1/16.
 */
public class Sequence {
    private String name;
    private String sequence;

    public Sequence(String name) {
        this.name = name;
        this.sequence = "";
    }

    public String getName() {
        return name;
    }

    public String getSequence() {
        return sequence;
    }

    public void setSequence(String sequence) {
        this.sequence = sequence;
    }

    public void appendSequence(String part) {
        this.sequence += part;
    }
}
