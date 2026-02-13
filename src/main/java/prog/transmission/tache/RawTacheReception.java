package prog.transmission.tache;

public class RawTacheReception extends AbstractTacheReception<String> {

    public RawTacheReception(int port) {
        super(port);
    }

    @Override
    protected String convert(String value) {
        return value;
    }

    @Override
    public void reset() {
        this.updateValue("");
    }
}
