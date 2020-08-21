package uniolunisaar.adam.logic.ui.cl.modules;

import java.util.HashMap;
import java.util.Map;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.OptionBuilder;
import org.apache.commons.cli.OptionGroup;
import uniolunisaar.adam.logic.ui.cl.AdamUI;
import uniolunisaar.adam.tools.Logger;

/**
 * Simple Wrapper for: - optional logger file - flag for the verbose version -
 * printing the help dialog - setting a debug flag
 *
 * @author Manuel Gieseking
 */
public abstract class AbstractSimpleModule extends AbstractModule {

    private static final String PARAMETER_LOGGER = "l";
    protected static final String PARAMETER_VERBOSE = "v";
    private static final String PARAMETER_SILENT = "psst";
    private static final String PARAMETER_HELP = "h";
    private static final String PARAMETER_DEBUG = "d";

    @Override
    protected Map<String, Option> createOptions() {
        Map<String, Option> ops = new HashMap<>();

        OptionBuilder.hasArg();
        OptionBuilder.withArgName("file");
        OptionBuilder.withDescription("The path to an optional logger file. If it's"
                + " not set, the information will be send to the terminal.");
        OptionBuilder.withLongOpt("logger");
        ops.put(PARAMETER_LOGGER, OptionBuilder.create(PARAMETER_LOGGER));

        OptionBuilder.withDescription("Makes the tool chatty.");
        OptionBuilder.withLongOpt("verbose");
        ops.put(PARAMETER_VERBOSE, OptionBuilder.create(PARAMETER_VERBOSE));

        OptionBuilder.withDescription("Makes the tool voiceless.");
        OptionBuilder.withLongOpt("silent");
        ops.put(PARAMETER_SILENT, OptionBuilder.create(PARAMETER_SILENT));

        OptionBuilder.withDescription("Prints this dialog.");
        OptionBuilder.withLongOpt("help");
        ops.put(PARAMETER_HELP, OptionBuilder.create(PARAMETER_HELP));

        OptionBuilder.withDescription("Get some debug infos.");
        OptionBuilder.withLongOpt("debug");
        ops.put(PARAMETER_HELP, OptionBuilder.create(PARAMETER_DEBUG));
        return ops;
    }

    public boolean isVerbose(CommandLine line) {
        return line.hasOption(PARAMETER_VERBOSE);
    }

    @Override
    protected Map<String, OptionGroup> createOptionGroups() {
        return new HashMap<>();
    }

    @Override
    public void execute(CommandLine line) throws Exception {
        if (line.hasOption(PARAMETER_HELP)) {
            this.printHelp();
            return;
        }
        Logger.getInstance().setVerbose(line.hasOption(PARAMETER_VERBOSE));
        Logger.getInstance().setSilent(line.hasOption(PARAMETER_SILENT));
        if (line.hasOption(PARAMETER_LOGGER)) {
            String log = line.getOptionValue(PARAMETER_LOGGER);
            Logger.getInstance().setPath(log);
            Logger.getInstance().setOutput(Logger.OUTPUT.FILE);
        }
        if (line.hasOption(PARAMETER_DEBUG)) {
            AdamUI.debug = true;
        }
    }

    public boolean isDebugging(CommandLine line) {
        return line.hasOption(PARAMETER_DEBUG);
    }
}
