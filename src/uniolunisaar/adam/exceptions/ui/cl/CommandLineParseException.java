package uniolunisaar.adam.exceptions.ui.cl;

import org.apache.commons.cli.ParseException;

/**
 *
 * @author Manuel Gieseking
 */
public class CommandLineParseException extends ParseException {

    private static final long serialVersionUID = 1L;

    public CommandLineParseException(String message) {
        super(message);
    }
}
