package uniolunisaar.adam.logic.ui.cl.modules;

import java.io.ByteArrayOutputStream;
import java.io.PrintWriter;
import java.util.Collection;
import java.util.Map;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.OptionGroup;
import org.apache.commons.cli.Options;
import uniolunisaar.adam.logic.ui.cl.AdamUI;

/**
 *
 * @author Manuel Gieseking
 */
public abstract class AbstractModule {

    private Options options = null;

    public abstract String getName();

    public abstract String getDescr();

    protected abstract Map<String, Option> createOptions();

    protected abstract Map<String, OptionGroup> createOptionGroups();

    public abstract void execute(CommandLine line) throws Exception;

    public Collection<Option> getOptionsList() {
        return createOptions().values();
    }

    public Options getOptions() {
        if (options == null) {
            options = new Options();
            Collection<Option> opts = getOptionsList();
            for (Option option : opts) {
                options.addOption(option);
            }
            Collection<OptionGroup> optsgroups = createOptionGroups().values();
            for (OptionGroup option : optsgroups) {
                options.addOptionGroup(option);
            }
        }
        return options;
    }

    public String getHelp() {
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        PrintWriter pw = new PrintWriter(os);
        printHelp(pw);
        return os.toString();
    }

    public void printHelp(PrintWriter pw) {
        // automatically generate the help statement
        HelpFormatter formatter = new HelpFormatter();
//        formatter.printHelp(pw, HelpFormatter.DEFAULT_WIDTH - 2, "sh adam.sh " + this.getName(), this.getDescr(), this.getOptions(), HelpFormatter.DEFAULT_LEFT_PAD, HelpFormatter.DEFAULT_DESC_PAD, null, true);
        formatter.printHelp(pw, AdamUI.CONSOLE_WIDTH, "sh adam.sh " + this.getName(), this.getDescr(), this.getOptions(), HelpFormatter.DEFAULT_LEFT_PAD, HelpFormatter.DEFAULT_DESC_PAD, null, true);
        pw.flush();
    }

    public void printHelp() {
        printHelp(new PrintWriter(System.out));
    }
}
