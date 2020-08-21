package uniolunisaar.adam.logic.ui.cl.modules.exporter;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Map;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.OptionBuilder;
import uniol.apt.module.exception.ModuleException;
import uniolunisaar.adam.exceptions.ui.cl.CommandLineParseException;
import uniolunisaar.adam.logic.ui.cl.modules.AbstractModule;
import uniolunisaar.adam.logic.ui.cl.modules.AbstractSimpleModule;
import uniolunisaar.adam.logic.ui.cl.modules.Modules;
import uniolunisaar.adam.tools.Logger;
import uniolunisaar.adam.tools.Tools;

/**
 *
 * @author Manuel Gieseking
 */
public abstract class AbstractExporter extends AbstractSimpleModule {

    private static final String name = "export";
    private static final String descr = "Exports some data of the tool.";

    private static final String PARAMETER_OUTPUT = "o";
    private static final String PARAMETER_LATEX_HELP = "eh";
    private static final String PARAMETER_BASH_COMPLETION = "bc";

    @Override
    protected Map<String, Option> createOptions() {
        Map<String, Option> options = super.createOptions();

        OptionBuilder.isRequired();
        OptionBuilder.hasArg();
        OptionBuilder.withArgName("file");
        OptionBuilder.withDescription("The path to the output folder for saving the exported data.");
        OptionBuilder.withLongOpt("out");
        options.put(PARAMETER_OUTPUT, OptionBuilder.create(PARAMETER_OUTPUT));

        OptionBuilder.withDescription("Exports the help dialogues as LaTeX files to the given folder.");
        OptionBuilder.withLongOpt("exp_help");
        options.put(PARAMETER_LATEX_HELP, OptionBuilder.create(PARAMETER_LATEX_HELP));

        OptionBuilder.withDescription("Exports the bash_completion function for this tool.");
        OptionBuilder.withLongOpt("bash_comp");
        options.put(PARAMETER_BASH_COMPLETION, OptionBuilder.create(PARAMETER_BASH_COMPLETION));

        return options;
    }

    protected abstract Modules getModules();

    @Override
    public void execute(CommandLine line) throws IOException, InterruptedException, FileNotFoundException, ModuleException, Exception {
        super.execute(line);
        String output = line.getOptionValue(PARAMETER_OUTPUT);

        if (line.hasOption(PARAMETER_LATEX_HELP)) {
            File dir = new File(output);
            boolean created = dir.mkdir();
            if (created) {
                Logger.getInstance().addMessage("Folder " + output + " created.", false);
            }
            String out = output + File.separator + "ui.tex";
            try (PrintWriter pw = new PrintWriter(out, "UTF-8")) {
                getModules().printPossibleModules(pw);
                Logger.getInstance().addMessage(out + " saved.", false);
            }
            out = (output + File.separator + "helpDialogs.tex");
            try (PrintWriter pw1 = new PrintWriter(out, "UTF-8")) {
                for (AbstractModule mod : getModules().getModules()) {
                    pw1.append("\\subsection*{Module: " + mod.getName().replace("_", "\\_") + "}");
                    pw1.append(System.lineSeparator());
                    pw1.append(mod.getDescr().replace("_", "\\_"));
                    pw1.append(" The help dialogue: ");
                    pw1.append(System.lineSeparator());
                    pw1.append("\\lstinputlisting[mathescape]{" + mod.getName() + ".tex}");
                    pw1.append(System.lineSeparator());
                    String file = output + File.separator + mod.getName() + ".tex";
                    try (PrintWriter pw = new PrintWriter(file, "UTF-8")) {
                        mod.printHelp(pw);
                        Logger.getInstance().addMessage(file + " saved.", false);
                    }
                }
                Logger.getInstance().addMessage(out + " saved.", false);
            }
        } else if (line.hasOption(PARAMETER_BASH_COMPLETION)) {
            Tools.saveFile(output, BashCompletion.getBashCompletionContent(getModules()));
        }
    }

    protected String getOutput(CommandLine line) {
        return line.getOptionValue(PARAMETER_OUTPUT);
    }

    @Override
    public String getDescr() {
        return descr;
    }

    @Override
    public String getName() {
        return name;
    }
}
