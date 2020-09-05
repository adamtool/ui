package uniolunisaar.adam.ds.ui.cl.parameters;

import java.util.HashMap;
import java.util.Map;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.OptionBuilder;
import uniolunisaar.adam.exceptions.ui.cl.CommandLineParseException;

/**
 *
 * @author Manuel Gieseking
 */
public class IOParameters {

    private static final String PARAMETER_INPUT = "i";
    private static final String PARAMETER_OUTPUT = "o";

    public static Map<String, Option> createOptions() {
        Map<String, Option> options = new HashMap<>();

        OptionBuilder.hasArg();
        OptionBuilder.withArgName("file");
        OptionBuilder.withDescription("The path to the input file.");
        OptionBuilder.isRequired();
        OptionBuilder.withLongOpt("input");
        options.put(PARAMETER_INPUT, OptionBuilder.create(PARAMETER_INPUT));

        OptionBuilder.hasArg();
        OptionBuilder.withArgName("file");
        OptionBuilder.withDescription("The path to the output folder."
                + " If it's not given the path from the input file is used.");
        OptionBuilder.withLongOpt("output");
        options.put(PARAMETER_OUTPUT, OptionBuilder.create(PARAMETER_OUTPUT));

        return options;
    }

    public static String getInput(CommandLine line) throws CommandLineParseException {
        return line.getOptionValue(PARAMETER_INPUT);
    }

    public static String getOutput(CommandLine line) throws CommandLineParseException {
        String input = line.getOptionValue(PARAMETER_INPUT);
        String output;
        if (line.hasOption(PARAMETER_OUTPUT)) {
            output = line.getOptionValue(PARAMETER_OUTPUT);
        } else {
            if (input.lastIndexOf('.') <= 0) {
                throw new CommandLineParseException("The input path '" + input + "' doesn't have a file extension");
            }
            output = input.substring(0, input.lastIndexOf('.'));
        }
        return output;
    }

    public static String getPARAMETER_OUTPUT() {
        return PARAMETER_OUTPUT;
    }

    public static String getPARAMETER_INPUT() {
        return PARAMETER_INPUT;
    }

}
